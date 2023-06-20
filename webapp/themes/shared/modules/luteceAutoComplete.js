import LuteceContentLoader from './luteceContentLoader.js';

const LOADING_START = 'loading-start';
const LOADING_END = 'loading-end';
const LOADING_ERROR = 'loading-error';

class LuteceAutoComplete extends EventTarget {
  constructor(autocompleteElement) {
    super();
    this.extractAttributes(autocompleteElement);
    this.init();
    this.isItemSelected = false;
    this.originalValue = '';
  }

  async updateAutocomplete(event) {
    const input = event.target;
    if (input.value.length < this.minimumInputLength) {
      if (input.value.length > 0) {
        this.resultList.innerHTML = '';
      }
      return;
    }
    this.loader.setTargetUrl(`${this.suggestionsUrl}` + input.value);
    this.loader.setDataStoreItem('inputValue', input.value);
    this.dispatchEvent(new Event(LOADING_START));
    await this.loader.load();
    this.dispatchEvent(new Event(LOADING_END));
  }

  init() {
    this.searchInput.addEventListener('focus', this.onSearchInputFocus.bind(this));
    this.searchInput.addEventListener('keydown', this.onSearchInputKeyDown.bind(this));
    this.searchInput.addEventListener('blur', this.onSearchInputBlur.bind(this));

    this.searchInput.addEventListener('keyup', this.debounce((event) => {
      this.updateAutocomplete(event);
    }, 300));
    this.loader.addEventListener('success', this.onLoaderSuccess.bind(this));
    this.loader.addEventListener('error', this.onLoaderError.bind(this));
    this.removeBtn.addEventListener('click', this.onRemoveBtnClick.bind(this));
    this.addEventListener(LOADING_ERROR, this.onLoadingError.bind(this));
    this.addEventListener(LOADING_START, this.onLoadingStart.bind(this));
    this.addEventListener(LOADING_END, this.onLoadingEnd.bind(this));
    window.addEventListener('resize', this.adjustWidths.bind(this));
    this.adjustWidths();
  }

  onSearchInputFocus() {
    this.dropdown.style.display = 'block'; 
    this.inputWasEmpty = this.searchInput.value === '';
    this.originalValue = this.searchInput.value;
  }


  onSearchInputBlur() {
    setTimeout(() => {
      if (!this.isItemSelected) {
        this.searchInput.value = this.originalValue;
      }
      this.dropdown.style.display = 'none';
      this.isItemSelected = false;
    }, 200);
  }
  onSearchInputKeyDown(event) {
    if (event.key === 'Enter') {
      event.preventDefault();
    }
  }

  onItemSelect() {
    this.isItemSelected = true;
  }


  onLoaderSuccess(event) {
    const suggestions = event.detail.targetElement;
    this.resultList.innerHTML = '';
    suggestions.forEach((suggestion) => {
      this.resultList.appendChild(this.itemTemplate(suggestion));
    });
  }

  onLoaderError(event) {
    console.error('Error loading suggestions:', event.detail.error);
    this.dispatchEvent(new Event(LOADING_ERROR));
  }

  onRemoveBtnClick() {
    this.dropdown.querySelectorAll('.list-group-item.active').forEach(el => {
      el.classList.remove('active');
    });
    this.searchInput.value = '';
    this.copyFields.forEach(item => {
      document.querySelector('input[name=' + item.inputName + ']').value = '';
    });
    this.removeBtn.classList.add('d-none');
  }

  onBtnClick() {
    this.searchInput.focus();
  }

  onLoadingError() {
    this.updateLoader(this.loaderIconClasses.error, this.loaderIconClasses.loading);
  }

  onLoadingStart() {
    this.updateLoader(this.loaderIconClasses.loading, [...this.loaderIconClasses.search, ...this.loaderIconClasses.error]);
  }

  onLoadingEnd() {
    this.updateLoader(this.loaderIconClasses.search, this.loaderIconClasses.loading);
    if (this.resultList.childElementCount === 0) {
      const emptyItem = this.createEl('li', this.emptyClass, this.emptyLabel);
      this.resultList.appendChild(emptyItem);
    }
  }

  itemTemplate(suggestion) {
    const item = this.createEl('li', this.suggestionItemClass);
    item.setAttribute('data-value', suggestion[this.itemValueFieldName]);
    this.copyFields.forEach(copyField => {
      item.setAttribute('data-' + copyField.inputName, suggestion[copyField.resultFieldName]);
    });
    item.setAttribute('data-label', this.itemTitleFieldNames.map(field => suggestion[field]).join(" "));
    item.addEventListener('click', ({
      currentTarget
    }) => {
      this.isItemSelected = true;
      this.onItemSelect();
      this.dropdown.querySelectorAll(`.list-group-item.active`).forEach(el => el.classList.remove('active'));
      currentTarget.classList.add('active');
      this.searchInput.value = currentTarget.getAttribute('data-value');
      this.copyFields.forEach(item => {
        console.log(item.inputName)
        console.log(document.querySelector('input[name=' + item.inputName + ']'));
        console.log(currentTarget.getAttribute('data-' + item.inputName))
        document.querySelector('input[name=' + item.inputName + ']').value = currentTarget.getAttribute('data-' + item.inputName);
      });
      this.removeBtn.classList.remove('d-none');
    });
     item.append(
      this.createEl('p', this.titleClass, this.itemTitleFieldNames.map(field => suggestion[field]).join(" ")),
      this.createEl('p', this.descriptionClass, this.itemDescriptionFieldNames.map(field => suggestion[field]).join(" ")),
      this.itemTagsFieldNames.reduce((tags, field) => (tags.appendChild(this.createEl('span', this.tagClass, suggestion[field])), tags), this.createEl('span'))
    );
    return item;
  }

  debounce(func, wait) {
    let timeout;
    return (...args) => {
      clearTimeout(timeout);
      timeout = setTimeout(() => func.apply(this, args), wait);
    };
  }

  createEl(type, classNames = [], textContent = '') {
    const el = document.createElement(type);
    el.classList.add(...classNames);
    el.textContent = textContent;
    return el;
  }

  updateLoader(add, remove) {
    this.searchLoader.classList.add(...add);
    this.searchLoader.classList.remove(...remove);
  }

  adjustWidths() {
    setTimeout(() => {
      this.dropdown.style.minWidth = this.searchInput.offsetWidth + 'px';
      this.resultList.style.minWidth = this.searchInput.offsetWidth + 'px';
    }, 200);
  }

  extractAttributes(element) {
    this.autocompleteElement = element;
    this.loader = new LuteceContentLoader("", "json", element.getAttribute('data-suggestionsPath'));
    this.searchInput = element.querySelector('.lutece-autocomplete-search-input');
    this.suggestionsUrl = element.getAttribute('data-suggestionsUrl');
    this.minimumInputLength = element.getAttribute('data-minimumInputLength');
    this.itemTitleFieldNames = JSON.parse(element.getAttribute('data-itemTitleFieldNames'));
    this.itemDescriptionFieldNames = JSON.parse(element.getAttribute('data-itemDescriptionFieldNames'));
    this.itemTagsFieldNames = JSON.parse(element.getAttribute('data-itemTagsFieldNames'));
    this.copyFields = JSON.parse(element.getAttribute('data-copyFields'));
    this.emptyLabel = element.getAttribute('data-emptyLabel');
    this.formInput = element.querySelector('.lutece-autocomplete-value-input');
    this.searchLabel = element.getAttribute('data-searchLabel');
    this.removeBtn = element.querySelector('.lutece-autocomplete-remove');
    this.searchLoader = element.querySelector('.lutece-autocomplete-search-icon');
    this.dropdown = element.querySelector('.lutece-autocomplete-dropdown');
    this.resultList = element.querySelector('.lutece-autocomplete-result-container');
    this.itemValueFieldName = "value";
    this.emptyClass = JSON.parse(element.getAttribute('data-emptyClass')) || [];
    this.suggestionItemClass = JSON.parse(element.getAttribute('data-suggestionItemClass')) || [];
    this.titleClass = JSON.parse(element.getAttribute('data-titleClass')) || [];
    this.descriptionClass = JSON.parse(element.getAttribute('data-descriptionClass')) || [];
    this.tagClass = JSON.parse(element.getAttribute('data-tagClass')) || [];
    this.loaderIconClasses = JSON.parse(element.getAttribute('data-loaderIconClasses')) || {
      loading: [],
      error: [],
      search: []
    };
  }
}

export default LuteceAutoComplete;
