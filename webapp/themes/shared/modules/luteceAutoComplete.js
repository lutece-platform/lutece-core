import LuteceContentLoader from './luteceContentLoader.js';

const LOADING_START = 'loading-start';
const LOADING_END = 'loading-end';
const LOADING_ERROR = 'loading-error';

const KEYCODES = {
  13: 'enter',
  27: 'escape',
  32: 'space',
  38: 'up',
  40: 'down'
}

class LuteceAutoComplete extends EventTarget {
  constructor(autocompleteElement, options = {}) {
    super();
    this.source = options.source || null;
    this.additionalParamElement = options.additionalParamElement || null;
    this.extractAttributes( autocompleteElement );
    this.init();
    this.isItemSelected = false;
    this.isItemSelectedId = '';
    this.originalValue = '';
    this.open = false;
    this.listSize = 0;
  }

  async updateAutocomplete( event ) {
    const input = event.target;
    if (input.value.length < this.minimumInputLength ) {
      if (input.value.length > 0) {
        this.resultList.innerHTML = '';
      }
      return;
    }

    this.dispatchEvent( new Event(LOADING_START) );

    if (this.source) {
      this.source(input.value, (suggestions) => {
        this.renderSuggestions(suggestions || []);
        this.dispatchEvent( new Event(LOADING_END) );
      });
    } else {
      const url = this.suggestionsUrl + input.value;
      if ( this.additionalParamElement != null ) {
        this.loader.setTargetUrl(url + "&additionalParam" + "=" + this.additionalParamElement.value);
      } else {
        this.loader.setTargetUrl(url);
      }
      this.loader.setDataStoreItem('inputValue', input.value);
      await this.loader.load();
      this.dispatchEvent( new Event(LOADING_END));
    }
  }

  init(){
    this.searchInput.addEventListener( 'focus', this.onSearchInputFocus.bind(this));
    this.searchInput.addEventListener( 'keydown', this.onSearchInputKeyDown.bind(this));
    this.searchInput.addEventListener( 'blur', this.onSearchInputBlur.bind(this));

    this.searchInput.addEventListener( 'keyup', this.debounce((event) => {
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
  
  /* Search Key Events */
  handleSearchDownArrow(event) {
    event.preventDefault()
    if ( this.open && this.listSize > 0 ){
       this.resultList.firstElementChild.setAttribute('aria-selected','true')
       this.resultList.firstElementChild.classList.add('active')
       this.resultList.firstElementChild.focus()
       const activeItemId = this.resultList.firstElementChild.getAttribute('id')
       this.searchInput.setAttribute('aria-activedescendant', activeItemId );
       this.isItemSelected = true;
       this.isItemSelectedId = activeItemId;
    }
  }
  
  /* Result List Key Events */
  handleListArrows( key ) {
    let activeItem = this.resultList.querySelector('[aria-selected="true"]'), nextMenuItem;
    if (this.resultList.childElementCount === 0) { return; }
    if( key === 'up' ){
      nextMenuItem = ( activeItem.previousSibling != null ) ? activeItem.previousSibling : this.resultList.lastElementChild; //last item in list
    } else {
      nextMenuItem = ( activeItem.nextSibling  != null ) ? activeItem.nextSibling : this.resultList.firstElementChild; //first item in list
    }
    activeItem.setAttribute('aria-selected', 'false')
    this.clearSelected( )
    nextMenuItem.classList.add('active');
    nextMenuItem.setAttribute('aria-selected', 'true')
    const activeItemId = nextMenuItem.getAttribute('id')
    this.isItemSelectedId = activeItemId;
    this.searchInput.setAttribute('aria-activedescendant', activeItemId );
  }

  onSearchInputFocus() {
    this.searchInput.setAttribute('aria-activedescendant','');
    this.searchInput.setAttribute('aria-expanded','true');
    this.dropdown.style.display = 'block'; 
    this.inputWasEmpty = this.searchInput.value === '';
    this.originalValue = this.searchInput.value;
  }

  onSearchInputBlur() {
    setTimeout(() => {
      if (!this.isItemSelected ) {
        if (!this.allowFreeText) {
          this.searchInput.value = this.originalValue;
        }
        this.dropdown.style.display = 'none';
        this.isItemSelected = false;
        this.searchInput.setAttribute( 'aria-expanded', 'false' );
      } else {
        this.searchInput.setAttribute( 'aria-expanded', 'false' );
        this.searchInput.removeAttribute( 'aria-activedescendant' );
        this.open = false
      }
    }, 200);
  }

  onResultKeyDown(event) {
    switch (KEYCODES[event.keyCode]) {
      case 'up':
        event.preventDefault();
        this.handleListArrows('up')
        break
      case 'down':
        event.preventDefault();
        this.handleListArrows('down')
        break
      case 'space':
        event.preventDefault();
        break
      case 'enter':
        event.preventDefault();
        document.getElementById(this.isItemSelectedId).click();
        break
      case 'escape':
        event.preventDefault();
        this.dropdown.classList.add('d-none')
        this.searchInput.setAttribute('aria-expanded','false');
        this.searchInput.focus();
        break
      default:
        break
    }
  }

  onSearchInputKeyDown(event) {
    switch (KEYCODES[event.keyCode]) {
      case 'up':
        event.preventDefault();
        break
      case 'down':
        this.handleSearchDownArrow(event)
        break
      case 'enter':
        if ( this.resultList.childElementCount > 0 ){
          this.dropdown.classList.remove('d-none')
        }
        this.searchInput.setAttribute('aria-expanded','true');
        event.preventDefault();
        break
      case 'escape':
        this.dropdown.classList.add('d-none')
        this.searchInput.setAttribute('aria-expanded','false');
        event.preventDefault();
        break
      default:
        if ( this.resultList.childElementCount > 0 && this.searchInput.value !='' ){
          this.dropdown.classList.remove('d-none')
        }
        break
    }
  }

  clearSelected( ){
    this.dropdown.querySelectorAll( `.list-group-item.active` ).forEach( el => el.classList.remove('active') );
  }

  onItemSelected() {
    this.isItemSelected = true;
    this.dropdown.classList.add('d-none')
    this.clearSelected()
    this.removeBtn.classList.remove('d-none');
  }

  onLoaderSuccess(event) {
    this.renderSuggestions(event.detail.targetElement);
  }

  renderSuggestions(suggestions) {
    this.ariaLive.textContent = '';
    this.listSize = suggestions.length;
    this.resultList.innerHTML = '';
    suggestions.forEach( (suggestion, index ) => {
      this.resultList.appendChild( this.itemTemplate( suggestion, index ) );
    });
  }

  onLoaderError(event) {
    console.error('Error loading suggestions:', event.detail.error);
    this.dispatchEvent(new Event(LOADING_ERROR));
  }

  onRemoveBtnClick() {    
    this.clearSelected()
    this.dropdown.classList.add('d-none');
    this.searchInput.value = '';
    this.copyFields.forEach(item => {
      const copyField = document.querySelector('input[name=' + item.inputName + ']');
      if( copyField ) {
        copyField.value = '';
        copyField.dispatchEvent(new Event('change'));
      }
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
    this.updateLoader( this.loaderIconClasses.loading, [...this.loaderIconClasses.search, ...this.loaderIconClasses.error]);
  }

   onLoadingEnd(){
      this.updateLoader(this.loaderIconClasses.search, this.loaderIconClasses.loading);
      if (this.resultList.childElementCount === 0) {
        const emptyItem = this.createEl('li', this.emptyClass, this.emptyLabel);
        this.resultList.appendChild(emptyItem);
        this.searchInput.setAttribute('aria-expanded','true');
      } else {
        this.open=true;
        this.searchInput.setAttribute('aria-expanded','true');
        this.ariaLive.textContent = `${this.resultList.childElementCount} éléments trouvés`
        
      }
  }
  
  itemTemplate( suggestion, index ) {
    const idx = index + 1
    const item = this.createEl( 'li', this.suggestionItemClass);
    item.setAttribute( 'id', 'lutece-autocomplete-option-' + idx );
    item.setAttribute( 'aria-posinset', idx );
    item.setAttribute( 'aria-setsize', this.listSize );
    item.setAttribute( 'aria-selected', 'false');
    item.setAttribute( 'role', 'option');
    item.setAttribute( 'tabindex', '-1');
    item.setAttribute( 'data-value', suggestion[this.itemValueFieldName]);
    this.copyFields.forEach( copyField => {
      item.setAttribute( 'data-' + copyField.inputName, suggestion[copyField.resultFieldName]);
    });
    item.setAttribute( 'data-label', this.itemTitleFieldNames.map( field => suggestion[field]).join(" ") );
    item.addEventListener('click', ({ currentTarget }) => {
      currentTarget.classList.add( 'active' );
      this.isItemSelectedId = currentTarget.getAttribute('id');
      this.searchInput.value = currentTarget.getAttribute( 'data-value' );
      this.copyFields.forEach(item => {
        const copyField = document.querySelector('input[name=' + item.inputName + ']');
        if( copyField ) {
          copyField.value = currentTarget.getAttribute('data-' + item.inputName);
          copyField.dispatchEvent(new Event('change'));
        }
      });
      this.removeBtn.classList.remove('d-none');
      this.onItemSelected();
      this.autocompleteElement.dispatchEvent(new CustomEvent('autocomplete:select', {
        bubbles: true,
        detail: { suggestion: suggestion, element: currentTarget }
      }));
    });
    item.addEventListener('keydown', this.onResultKeyDown.bind(this) );
    
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
    this.ariaLive = element.querySelector('.lutece-autocomplete-status');
    this.loader = new LuteceContentLoader( "", "json", element.getAttribute('data-suggestionsPath') );
    this.searchInput = element.querySelector('.lutece-autocomplete-search-input');
    this.suggestionsUrl = element.getAttribute('data-suggestionsUrl');
    this.minimumInputLength = element.getAttribute('data-minimumInputLength');
    this.itemTitleFieldNames = JSON.parse(element.getAttribute('data-itemTitleFieldNames'));
    this.itemDescriptionFieldNames = JSON.parse(element.getAttribute('data-itemDescriptionFieldNames'));
    this.itemTagsFieldNames = JSON.parse(element.getAttribute('data-itemTagsFieldNames'));
    this.copyFields = JSON.parse(element.getAttribute('data-copyFields'));
    this.emptyLabel = element.getAttribute( 'data-emptyLabel' );
    this.formInput = element.querySelector( '.lutece-autocomplete-value-input' );
    this.searchLabel = element.getAttribute( 'data-searchLabel' );
    this.removeBtn = element.querySelector( '.lutece-autocomplete-remove' );
    this.searchLoader = element.querySelector( '.lutece-autocomplete-search-icon' );
    this.dropdown = element.querySelector( '.lutece-autocomplete-dropdown' );
    this.resultList = element.querySelector('.lutece-autocomplete-result-container');
    this.itemValueFieldName = element.getAttribute('data-itemValueFieldName');
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
    this.allowFreeText = element.getAttribute('data-allowFreeText') === 'true';
  }
}

export default LuteceAutoComplete;