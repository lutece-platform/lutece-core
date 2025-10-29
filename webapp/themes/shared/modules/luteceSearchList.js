/**
A class for searching through a list of elements and highlighting search matches.
*/
export default class LuteceSearchList {
  /**
   * Creates a new instance of LuteceSearchList.
   * @param {HTMLElement} searchInput - The search input element.
   * @param {NodeList} searchElementList - The list of elements to search through.
   * @param {Object} [options] - Optional parameters for customizing search behavior.
   */
  constructor(searchInput, searchElementList, options) {
    this.searchInput = searchInput;
    this.searchElementList = searchElementList;
    this.options = Object.assign({
      searchableChild: [],
      highlight: false,
      debounceTime: 100,
      emptyMessageElement: null,
      hideClass: null,
      toggleList: false,
      toggleDefaultState: 'list',
      toggleId: 'card-to-list',
      toggleRow: false,
      toggleRowSelector: '.row',
      toggleSelector: '.card',
      toggleBtnPrefix: 'btn',
      toggleBtnClass: 'primary',
      toggleBtnClassAlign: ['d-none', 'd-sm-block'],
      toggleBtnShowXs: false,
      toggleLabel: 'Toggle List',
      toggleLabelOff: 'Toggle Cards',
      toggleIconPrefix: 'ti',
      toggleIconOn: 'ti-list',
      toggleIconOff: 'ti-cards',
      toggleCardParentId: 'page-header-tools .mb-3',  
      toggleCardClass: ['flex-row'],  
      toggleCardHeaderClass : ['flex-column','justify-content-around','align-items-start'],  
      toggleCardFooterClass : ['d-flex','align-items-center','mt-0'],
      extraSearchFunction: () => {}
    }, options);
    this.init();
  }
   /**
   * Initializes the search functionality by adding an event listener to the search input element.
   */
  init() {
    this.searchInput.addEventListener('input', this.debounce(() => {
      const searchTerm = this.searchInput.value.toLowerCase();
      this.searchElementList.forEach( element => {
        const searchContent = this.getSearchContent(element);
        const isVisible = searchContent.includes(searchTerm);
        if ( this.options.highlight ) {
          if (searchTerm.length > 0) {
            this.removeHighlight(element);
            this.addHighlight(element, searchTerm);
          } else {
            this.removeHighlight(element);
          }
        }
        if ( this.options.hideClass) {
          isVisible ? element.classList.remove(`${this.options.hideClass}`) : element.classList.add(`${this.options.hideClass}`);
        } else {
          element.style.display = isVisible ? '' : 'none';
        }
      });
      this.options.extraSearchFunction();
    }, this.options.debounceTime ));

    this.searchInput.addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
          event.preventDefault();
        }
    });

    if ( this.options.toggleList ) {
      const displayType = this.getToggleState();
      const cardParent = this.searchInput.closest( `#${this.options.toggleCardParentId}` );
      const carRows = this.options.toggleRow ? document.querySelector( this.options.toggleRowSelector ) : null;
      const btnCardToList = this.setCardToListBtn( cardParent );
      
      if( displayType === 'list' ){
        if (carRows != null) {
          this.displayElements( btnCardToList );
        }
      }

      btnCardToList.addEventListener('click', (event) => {
        if (carRows != null) {
          this.displayElements( btnCardToList );
        }
      });
    }
  }

  /**
   * Returns the searchable content of an element as a string.
   * @param {HTMLElement} element - The element to get the searchable content for.
   * @returns {string} The searchable content of the element.
   */
  getSearchContent(element) {
    if (this.options.searchableChild.length === 0) {
      return element.textContent.toLowerCase();
    }
    return this.options.searchableChild
      .map(selector => {
        const children = element.querySelectorAll(selector);
        return Array.from(children)
          .map( child => child.textContent.toLowerCase())
          .join(' ');
      })
      .join(' ');
  }

  /**
   * Returns a debounced version of a function.
   * @param {Function} func - The function to debounce.
   * @param {number} wait - The time in milliseconds to wait before executing the debounced function.
   * @returns {Function} The debounced function.
   */
  debounce(func, wait) {
    let timeout;
    return (...args) => {
      const context = this;
      clearTimeout(timeout);
      timeout = setTimeout(() => func.apply(context, args), wait);
    };
  }

  /**
   * Highlights search matches in an element's children.
   * @param {HTMLElement} element - The element to highlight search matches in.
   * @param {string} searchTerm - The search term to highlight.
   */
  addHighlight(element, searchTerm) {
    this.options.searchableChild.forEach(selector => {
      const children = element.querySelectorAll(selector);
      children.forEach(child => {
        const regex = new RegExp(`(>[^<]*?)(${searchTerm})([^>]*?<)`, "gi");
        const originalInnerHTML = child.innerHTML;
        const highlightedInnerHTML = originalInnerHTML.replace(
          regex,
          "$1<mark>$2</mark>$3"
        );
        child.innerHTML = highlightedInnerHTML;
      });
    });
  }

  /**
   * Removes highlighting from an element's children.
   * @private
   * @param {HTMLElement} element - The element to remove highlighting from.
   */
  removeHighlight(element) {
    this.options.searchableChild.forEach(selector => {
      const children = element.querySelectorAll(selector);
      children.forEach(child => {
        const regex = /(<mark>|<\/mark>)/gi;
        child.innerHTML = child.innerHTML.replace(regex, '');
      });
    });
  }

  /**
   * Add button to toggle between card and list view.
   * @private
   * @param {HTMLElement} element - The element to remove highlighting from.
   */
  setCardToListBtn( element ) {
    const btnToggler = document.createElement("button");
    const iconToggler = document.createElement('i');
    btnToggler.classList.add( this.options.toggleBtnPrefix, `${this.options.toggleBtnPrefix}-${this.options.toggleBtnClass}` )
    btnToggler.setAttribute( 'id', this.options.toggleId )
    btnToggler.setAttribute( 'aria-label', this.options.toggleLabel )
    btnToggler.setAttribute( 'title', this.options.toggleLabel )
    if( !this.options.toggleBtnShowXs ){ btnToggler.classList.add( ...this.options.toggleBtnClassAlign ) }
    iconToggler.classList.add( this.options.toggleIconPrefix, this.options.toggleIconOn )
    btnToggler.appendChild( iconToggler )
    element.appendChild( btnToggler )
    return btnToggler
  }

  /**
   * Toggles  display between card and list view.
   * @private
   * @param {HTMLElement} element - The element to remove highlighting from.
   * @param {HTMLElement} btn - The button element to update.
   */
  toggleElements( element, btn ) {
    const icon = btn.querySelector( `.${this.options.toggleIconPrefix}` )
    const status = element.classList.length == 0
     if ( !status ){
      icon.classList.add( this.options.toggleIconOff  )
      icon.classList.remove( this.options.toggleIconOn )
      element.setAttribute( 'data-classes', element.className )
      btn.setAttribute('aria-label', this.options.toggleLabelOff )
      btn.setAttribute('title', this.options.toggleLabelOff )
      element.className = '';
    } else {
      icon.classList.remove( this.options.toggleIconOff )
      icon.classList.add( this.options.toggleIconOn )
      btn.setAttribute('title', this.options.toggleLabel )
      btn.setAttribute('aria-label', this.options.toggleLabel )
      const aClass = element.getAttribute('data-classes')
      element.className=aClass;
    }
    const cards = element.querySelectorAll( this.options.toggleSelector )
    this.toggleCardElements( cards, status )
    this.saveToggleState( status )
  }

  /**
   * Toggles cards display between card and list view.
   * @private
   * @param {HTMLElement} element - The element to remove highlighting from.
   * @param {boolean} status - The current display status.
   */
  toggleCardElements( elements, status ) {
    elements.forEach( card => {
      if ( !status ){
        this.options.toggleCardClass.forEach( (c) => { 
          card.classList.add( c );
        })
      } else {
        this.options.toggleCardClass.forEach( (c) => { 
          card.classList.remove( c );
        })
      }
      card.querySelectorAll(`${this.options.toggleSelector}-header`).forEach( cardHeader => {
        if ( !status ){
          this.options.toggleCardHeaderClass.forEach( (h) => { 
            cardHeader.classList.add( h );
          })
        } else {
          this.options.toggleCardHeaderClass.forEach( (h) => { 
            cardHeader.classList.remove( h );
          })
        }
      })
      card.querySelectorAll(`${this.options.toggleSelector}-footer`).forEach( cardFooter => {
        if ( !status ){
          this.options.toggleCardFooterClass.forEach( ( f ) => { 
            cardFooter.classList.add( f );
          })
        } else {
          this.options.toggleCardFooterClass.forEach( ( f ) => { 
            cardFooter.classList.remove( f );
          })
        }
      })
    })
  }

  /**
  * Set Element display
  * @private
  * @param {HTMLElement} btn - The button element to update.
  */
  displayElements( btn ){
      const mainRow = document.querySelector(this.options.toggleRowSelector);
      // Remove class starting with 'row-cols' and save it
      if ( mainRow.getAttribute( 'data-classes' ) === null ) {
        mainRow.setAttribute( 'data-classes', mainRow.className )
        mainRow.className = 'row';
      } else {
        mainRow.className = mainRow.getAttribute( 'data-classes' );
        mainRow.removeAttribute( 'data-classes' )
      }  

      this.searchElementList.forEach(element => {
        this.toggleElements(element, btn );
      });
  }

  /**
   * Save  toggle state 
   * @private
   * @param {HTMLElement} element - The element to remove highlighting from.
   */
  saveToggleState( toggled ) {
    const toggleState = toggled ? 'card' : 'list';
    localStorage.setItem( this.options.toggleId, toggleState );
  }

  /**
   * Retrieve toggle state 
   * @private
   * @param {HTMLElement} element - The element to remove highlighting from.
   */
  getToggleState( ) {
    const tgState = localStorage.getItem( this.options.toggleId );
    if( tgState != null ) {
      return tgState;
    } else {
      return  this.options.toggleDefaultState;
    }
  }
}