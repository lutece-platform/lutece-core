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
    }, options);
    this.init();
  }
  /**
   * Initializes the search functionality by adding an event listener to the search input element.
   */
  init() {
    this.searchInput.addEventListener('input', this.debounce(() => {
      const searchTerm = this.searchInput.value.toLowerCase();
      this.searchElementList.forEach(element => {
        const searchContent = this.getSearchContent(element);
        const isVisible = searchContent.includes(searchTerm);
        if (this.options.highlight) {
          if (searchTerm.length > 0) {
            this.removeHighlight(element);
            this.addHighlight(element, searchTerm);
          } else {
            this.removeHighlight(element);
          }
        }
        element.style.display = isVisible ? '' : 'none';
      });
    }, this.options.debounceTime));
    this.searchInput.addEventListener(
      "keydown",
      (event) => {
        if (event.keyCode === 13) {
          event.preventDefault();
        }
      });
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
          .map(child => child.textContent.toLowerCase())
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
}