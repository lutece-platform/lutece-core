/**
A class for searching through a list of elements and highlighting search matches.
*/
export default class LuteceSearchList {
    /**
     * Creates a new LuteceList instance.
     * @param {HTMLInputElement} searchInput - The search input element.
     * @param {HTMLElement[]} elementsList - The list of elements to search through.
     * @param {Object} options - The options object.
     * @param {boolean} options.highlight - Whether to highlight search matches.
     */
    constructor(
      searchInput,
      elementsList,
      options = {
        highlight: true,
      }
    ) {
      this.searchInput = searchInput;
      this.elementsList = elementsList;
      this.highlight = options.highlight;
      // remove animation and transition to avoid flickering
      this.elementsList.forEach((element) => {
        element.style.transition = "none";
        element.style.animation = "none";
        Array.from(element.childNodes).forEach((child) => {
          if (child.nodeType === 1) {
            // only target element nodes
            child.style.transition = "none";
            child.style.animation = "none";
          }
        });
      });
      this.init();
    }
    /**
     * Initializes the search input event listener.
     */
    init() {
      this.searchInput.addEventListener(
        "input",
        this.debounce((event) => {
          this.search(event.target.value);
        }, 300)
      );
      this.searchInput.addEventListener(
        "keydown",
        (event) => {
          if (event.keyCode === 13) {
            event.preventDefault();
          }
        });
    }
    /**
     * Debounces a function by delaying its execution until the specified time has passed since the last invocation.
     * @param {Function} func - The function to debounce.
     * @param {number} wait - The delay in milliseconds.
     * @returns {Function} The debounced function.
     */
    debounce(func, wait) {
      let timeout;
      return function (...args) {
        const context = this;
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(context, args), wait);
      };
    }
    /**
     * Searches the elements list for the query and updates the display accordingly.
     * @param {string} query - The search query.
     */
    search() {
      const searchTerm = this.searchInput.value.trim().toLowerCase();
      this.elementsList.forEach((element) => {
        this.removeHighlight(element);
        if (searchTerm !== "") {
          const text = element.textContent.toLowerCase();
          if (text.includes(searchTerm)) {
            this.highlight && this.addHighlight(element, searchTerm);
            this.showElement(element);
          } else {
            this.hideElement(element);
          }
        } else {
          this.showElement(element);
        }
      });
    }
    /**
     * Highlights the search matches in an element while maintaining the original formatting.
     * @param {HTMLElement} element - The element to highlight.
     * @param {RegExp} regex - The regular expression to match the search query.
     */
    addHighlight(element, searchTerm) {
      const regex = new RegExp(`(>[^<]*?)(${searchTerm})([^>]*?<)`, "gi");
      const originalInnerHTML = element.innerHTML;
      const highlightedInnerHTML = originalInnerHTML.replace(
        regex,
        "$1<mark>$2</mark>$3"
      );
      element.innerHTML = highlightedInnerHTML;
    }
    /**
     * Remove hightlight from an element.
     * @param {HTMLElement} element - The element to reset.
     */
    removeHighlight(element) {
      const markRegex = /(<mark>|<\/mark>)/gi;
      element.innerHTML = element.innerHTML.replace(markRegex, "");
    }
    /**
     * Shows an element.
     * @param {HTMLElement} element - The element to show.
     */
    showElement(element) {
      element.style.display = "";
    }
    /**
     * hide an element.
     * @param {HTMLElement} element - The element to hide.
     */
    hideElement(element) {
      element.style.display = "none";
    }
  }