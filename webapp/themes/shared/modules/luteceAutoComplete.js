import LuteceContentLoader from './luteceContentLoader.js';

/**
 * LuteceAutoComplete is a class to handle autocomplete functionality.
 */
class LuteceAutoComplete extends EventTarget {
  /**
   * Constructs a new LuteceAutoComplete instance.
   * @param {HTMLInputElement} searchInput - The input element for the search.
   * @param {HTMLElement} resultList - The container for the suggestion list.
   * @param {string} suggestionsUrl - The URL to fetch the suggestions from.
   * @param {string} suggestionsPath - The path to extract the suggestions from the response JSON.
   * @param {Function} itemTemplate - A function to generate the HTML element for a suggestion.
   * @param {number} [minimumInputLength=1] - The minimum input length required to fetch suggestions.
   */
    constructor(searchInput, resultList, suggestionsUrl, suggestionsPath, itemTemplate, minimumInputLength = 1) {
        super();
        this.searchInput = searchInput;
        this.loader = new LuteceContentLoader("", "json", suggestionsPath);
        this.suggestionsUrl = suggestionsUrl;
        this.itemTemplate = itemTemplate;
        this.resultList = resultList;
        this.minimumInputLength = minimumInputLength;
    
        this.init();
      }

  /**
   * Updates the autocomplete suggestions based on user input.
   * @param {Event} event - The input event.
   */
  async updateAutocomplete(event) {
    const input = event.target;

    if (input.value.length >= this.minimumInputLength) {
      this.loader.setTargetUrl(`${this.suggestionsUrl}?q=` + input.value);
      this.loader.setDataStoreItem('inputValue', input.value);
      this.dispatchEvent(new Event('loading-start'));
      await this.loader.load();
      this.dispatchEvent(new Event('loading-end'));
    } else if( input.value.length > 0 ) {
      this.resultList.innerHTML = '';
    }
  }

  /**
   * Handles the loader success event.
   * @param {Event} event - The success event.
   */
  onLoaderSuccess(event) {
    const suggestions = event.detail.targetElement;
    this.resultList.innerHTML = '';
    suggestions.forEach((suggestion) => {
        this.resultList.appendChild(this.itemTemplate(suggestion));
    });

  }

  /**
   * Initializes the LuteceAutoComplete instance.
   */
  init() {
    this.searchInput.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
        }
    });
    this.searchInput.addEventListener('keyup', this.debounce((event) => {
        this.updateAutocomplete(event);
      }, 300));


    this.loader.addEventListener('success', this.onLoaderSuccess.bind(this));
    this.loader.addEventListener('error', (event) => {
      console.error('Error loading suggestions:', event.detail.error);
      this.dispatchEvent(new Event('loading-error'));
    });

    this.resultList.style.minWidth = this.searchInput.offsetWidth + 'px';

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

}

export default LuteceAutoComplete;
