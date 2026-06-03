/**
 * Loads remote content
 */
export default class LuteceContentLoader extends EventTarget {
  /**
   * Creates a new instance of LuteceContentLoader.
   * @class
   * @param {string} targetUrl - The URL of the remote content.
   * @param {string} targetType - The type of content being loaded ("html", "json").
   * @param {string} targetElement - The selector for the target element in the HTML content / the json path.
  */
  constructor(targetUrl, targetType, targetElement) {
    super();
    this.targetUrl = targetUrl;
    this.targetType = targetType;
    this.targetElement = targetElement;
    this.dataStore = {};
    this.controller = new AbortController();
  }

  /**
   * Loads the remote content and emits appropriate events.
   *
   * @fires LuteceContentLoader#start
   * @fires LuteceContentLoader#success
   * @fires LuteceContentLoader#warning
   * @fires LuteceContentLoader#error
   */
  async load() {
    if (this.controller) {
      this.controller.abort();
    }
    this.controller = new AbortController();

    try {
      this.dispatchEvent(new Event('start'));
      const response = await fetch(this.targetUrl, {
        signal: this.controller.signal
      });

      let targetElement;
      if (this.targetType === 'json') {
        const json = await response.json();
        targetElement = json[this.targetElement];
      } else {
        const text = await response.text();
        const parser = new DOMParser();
        const doc = parser.parseFromString(text, 'text/html');
        targetElement = doc.querySelector(this.targetElement);
      }

      if (targetElement) {
        this.dispatchEvent(new CustomEvent('success', {
          detail: {
            dataStore: this.dataStore,
            targetElement: targetElement
          }
        }));
      } else {
        this.dispatchEvent(new CustomEvent('warning', {
          detail: {
            dataStore: this.dataStore,
            document: this.targetType === 'json' ? json : doc
          }
        }));
      }
    } catch (error) {
      if (error.name !== 'AbortError') {
        this.dispatchEvent(new CustomEvent('error', {
          detail: {
            dataStore: this.dataStore,
            error: error
          }
        }));
      }
    }
  }
  /**
   * Cancel the current loading process if one is in progress.
   */
  cancel() {
    this.controller.abort();
  }

  /**
   * Set url of the remote HTML content.
   * @param {string} targetUrl 
   */
  setTargetUrl(targetUrl) {
    this.targetUrl = targetUrl;
  }

  /**
   * Set selector for the target element in the HTML content.
   * @param {string} element 
   */
  setTargetElement(targetElement) {
    this.targetElement = targetElement;
  }

   /**
   * Stores custom data in the instance.
   *
   * @param {string} key - The key under which the data will be stored.
   * @param {*} value - The value to be stored.
   */
   setDataStoreItem(key, value) {
    this.dataStore[key] = value;
  }

  /**
   * Retrieves custom data from the instance.
   *
   * @param {string} key - The key under which the data is stored.
   * @returns {*} The stored data, or undefined if the key does not exist.
   */
  getDataStoreItem(key) {
    return this.dataStore[key];
  }
}