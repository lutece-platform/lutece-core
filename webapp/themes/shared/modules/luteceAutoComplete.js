import LuteceContentLoader from './luteceContentLoader.js';

const LOADING_START = 'loading-start';
const LOADING_END = 'loading-end';
const LOADING_ERROR = 'loading-error';

class LuteceAutoComplete extends EventTarget {
  constructor(autocompleteElement, options = {}) {
    super();
    this.source = options.source || null;
    this.additionalParamElement = options.additionalParamElement || null;
    this.extractAttributes( autocompleteElement );
    this.init();
    this.isItemSelected = false;
    this.activeDescendantId = '';
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

    // Prevent mousedown on dropdown from stealing DOM focus from input
    this.dropdown.addEventListener('mousedown', (e) => e.preventDefault());

    this.loader.addEventListener('success', this.onLoaderSuccess.bind(this));
    this.loader.addEventListener('error', this.onLoaderError.bind(this));
    this.removeBtn.addEventListener('click', this.onRemoveBtnClick.bind(this));

    this.addEventListener(LOADING_ERROR, this.onLoadingError.bind(this));
    this.addEventListener(LOADING_START, this.onLoadingStart.bind(this));
    this.addEventListener(LOADING_END, this.onLoadingEnd.bind(this));

    window.addEventListener('resize', this.adjustWidths.bind(this));

    this.adjustWidths();
  }
  
  /* --- ARIA Combobox: virtual focus helpers --- */

  setActiveDescendant( item ) {
    this.clearSelected();
    if ( item ) {
      item.classList.add('active');
      item.setAttribute('aria-selected', 'true');
      this.activeDescendantId = item.getAttribute('id');
      this.searchInput.setAttribute('aria-activedescendant', this.activeDescendantId);
      item.scrollIntoView({ block: 'nearest' });
    }
  }

  clearActiveDescendant() {
    this.clearSelected();
    this.activeDescendantId = '';
    this.searchInput.setAttribute('aria-activedescendant', '');
  }

  showDropdown() {
    this.dropdown.style.display = 'block';
    this.dropdown.classList.remove('d-none');
    if ( this.listSize > 0 ) {
      this.searchInput.setAttribute('aria-expanded', 'true');
      this.open = true;
    }
  }

  closeDropdown() {
    this.dropdown.style.display = 'none';
    this.dropdown.classList.add('d-none');
    this.searchInput.setAttribute('aria-expanded', 'false');
    this.clearActiveDescendant();
    this.open = false;
  }

  /* Navigate to next/previous option with wrapping */
  moveActiveDescendant( direction ) {
    if ( this.listSize === 0 ) { return; }
    const current = this.activeDescendantId ? document.getElementById( this.activeDescendantId ) : null;
    let next;
    if ( direction === 'down' ) {
      next = current && current.nextElementSibling ? current.nextElementSibling : this.resultList.firstElementChild;
    } else {
      next = current && current.previousElementSibling ? current.previousElementSibling : this.resultList.lastElementChild;
    }
    // Skip the empty message item (no role="option")
    if ( next && next.getAttribute('role') !== 'option' ) { return; }
    this.setActiveDescendant( next );
  }

  onSearchInputFocus() {
    this.clearActiveDescendant();
    this.originalValue = this.searchInput.value;
    // Show dropdown only if there are already results to display
    if ( this.listSize > 0 ) {
      this.showDropdown();
    }
  }

  onSearchInputBlur() {
    setTimeout(() => {
      if ( !this.isItemSelected && !this.allowFreeText ) {
        this.searchInput.value = this.originalValue;
      }
      this.closeDropdown();
      this.isItemSelected = false;
    }, 200);
  }

  /**
   * W3C ARIA Combobox keyboard interaction.
   * DOM focus always stays on the textbox; visual focus is managed via aria-activedescendant.
   */
  onSearchInputKeyDown(event) {
    const hasVisualFocus = this.activeDescendantId !== '';

    switch ( event.key ) {
      case 'ArrowDown':
        event.preventDefault();
        if ( !this.open && this.listSize > 0 ) {
          this.showDropdown();
        }
        if ( event.altKey ) {
          // Alt+Down: open without moving visual focus
          break;
        }
        this.moveActiveDescendant('down');
        break;

      case 'ArrowUp':
        event.preventDefault();
        if ( !this.open && this.listSize > 0 ) {
          this.showDropdown();
        }
        this.moveActiveDescendant('up');
        break;

      case 'Enter':
        event.preventDefault();
        if ( hasVisualFocus ) {
          document.getElementById( this.activeDescendantId ).click();
        }
        if ( this.open ) {
          this.closeDropdown();
        }
        break;

      case 'Escape':
        event.preventDefault();
        if ( this.open ) {
          this.closeDropdown();
        } else {
          this.searchInput.value = '';
        }
        break;

      case 'ArrowRight':
      case 'ArrowLeft':
      case 'Home':
      case 'End':
        // Return visual focus to textbox, let default cursor behaviour happen
        this.clearActiveDescendant();
        break;

      default:
        // Printable characters: clear visual focus and let the character be typed
        if ( event.key.length === 1 && !event.ctrlKey && !event.metaKey ) {
          this.clearActiveDescendant();
        }
        // Ensure dropdown is visible when there are results
        if ( this.listSize > 0 && this.searchInput.value !== '' ) {
          this.showDropdown();
        }
        break;
    }
  }

  clearSelected( ){
    this.dropdown.querySelectorAll( `.list-group-item.active` ).forEach( el => {
      el.classList.remove('active');
      el.setAttribute('aria-selected', 'false');
    });
  }

  onItemSelected() {
    this.isItemSelected = true;
    this.closeDropdown();
    this.removeBtn.classList.remove('d-none');
  }

  onLoaderSuccess(event) {
    this.renderSuggestions(event.detail.targetElement);
  }

  renderSuggestions(suggestions) {
    this.ariaLive.textContent = '';
    this.listSize = suggestions.length;
    this.resultList.innerHTML = '';
    this.clearActiveDescendant();
    suggestions.forEach( (suggestion, index ) => {
      this.resultList.appendChild( this.itemTemplate( suggestion, index ) );
    });
  }

  onLoaderError(event) {
    console.error('Error loading suggestions:', event.detail.error);
    this.dispatchEvent(new Event(LOADING_ERROR));
  }

  onRemoveBtnClick() {
    this.closeDropdown();
    this.searchInput.value = '';
    this.copyFields.forEach(item => {
      const copyField = document.querySelector('input[name=' + item.inputName + ']');
      if( copyField ) {
        copyField.value = '';
        copyField.dispatchEvent(new Event('change'));
      }
    });
    this.removeBtn.classList.add('d-none');
    this.searchInput.focus();
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
      this.ariaLive.textContent = this.emptyLabel;
      // Show dropdown with empty message but do not set aria-expanded (no options)
      this.dropdown.style.display = 'block';
      this.dropdown.classList.remove('d-none');
    } else {
      this.ariaLive.textContent = `${this.resultList.childElementCount} résultats disponibles`;
      this.showDropdown();
    }
  }
  
  itemTemplate( suggestion, index ) {
    const idx = index + 1;
    const item = this.createEl( 'li', this.suggestionItemClass);
    item.setAttribute( 'id', 'lutece-autocomplete-option-' + idx );
    item.setAttribute( 'aria-posinset', idx );
    item.setAttribute( 'aria-setsize', this.listSize );
    item.setAttribute( 'aria-selected', 'false');
    item.setAttribute( 'role', 'option');
    item.setAttribute( 'data-value', suggestion[this.itemValueFieldName]);
    this.copyFields.forEach( copyField => {
      item.setAttribute( 'data-' + copyField.inputName, suggestion[copyField.resultFieldName]);
    });
    item.setAttribute( 'data-label', this.itemTitleFieldNames.map( field => suggestion[field]).join(" ") );
    item.addEventListener('click', ({ currentTarget }) => {
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