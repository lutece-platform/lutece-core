/**
 * Loads remote HTML content into a target element.
 */
export default class LuteceContentLoader extends EventTarget {
    /**
     * Creates a new instance of `LuteceContentLoader`.
     *
     * @param {string} url - The URL of the remote HTML content.
     * @param {string} element - The selector for the target element in the HTML content.
     */
    constructor(url, element) {
      super();
      this.url = url;
      this.element = element;
      this.controller = new AbortController();
    }
  
    /**
     * Loads the remote HTML content and emits appropriate events.
     *
     * @fires LuteceContentLoader#start
     * @fires LuteceContentLoader#success
     * @fires LuteceContentLoader#warning
     * @fires LuteceContentLoader#error
     */
    async load() {
      try {
        this.dispatchEvent(new Event('start'));
        const response = await fetch(this.url, { signal: this.controller.signal });
        const html = await response.text();
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');
        const targetElement = doc.querySelector(this.element);
  
        if (targetElement) {
          this.dispatchEvent(new CustomEvent('success', { detail: targetElement }));
        } else {
          this.dispatchEvent(new CustomEvent('warning', { detail: doc }));
        }
      } catch (error) {
        if (error.name !== 'AbortError') {
          this.dispatchEvent(new CustomEvent('error', { detail: error }));
        }
      }
    }
  
    /**
     * Cancel the current loading process if one is in progress.
     */
    cancel() {
      this.controller.abort();
    }
  }
  