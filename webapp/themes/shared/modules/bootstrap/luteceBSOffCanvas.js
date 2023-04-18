/**
 * LuteceBSOffCanvas - A class for managing bootstrap off-canvas elements.
 */
import LuteceContentLoader from '../luteceContentLoader.js';

export default class LuteceBSOffCanvas {
    /**
     * Constructor for the LuteceBSOffCanvas class.
     * @param {string} id - The ID of the off-canvas element.
     */
    constructor(id) {
        this.id = id;
        this.loader = new LuteceContentLoader();

        // Event listeners for the loader
        this.loader.addEventListener('start', () => {
            this.loader.dataStore.destinationElement.innerHTML = `<div class="d-flex justify-content-center align-items-center h-100"><div class="spinner-border text-primary" role="status"></div></div>`;
        });

        this.loader.addEventListener('success', event => {
            this.contentWriter(event.detail.targetElement, this.loader.dataStore.destinationElement);
        });

        this.loader.addEventListener('error', event => {
            window.location.href = this.loader.targetUrl;
        });

        this.loader.addEventListener('warning', event => {
            window.location.href = this.loader.targetUrl;
        });

        this.initialize();
    }
    /**
     * Load content into the off-canvas element.
     * @param {boolean} isRedirectForm - Whether the content is a redirect form.
     * @param {string} targetUrl - The URL for loading content.
     * @param {string} targetElement - The target element for the content.
     * @param {HTMLElement} offCanvasElement - The off-canvas element.
     */
    loadContent = (isRedirectForm, targetUrl, targetElement, offCanvasElement) => {
        const destinationElement = offCanvasElement.querySelector('.offcanvas-body');
        this.loader.setTargetUrl(targetUrl);
        this.loader.setTargetElement(targetElement);
        this.loader.setDataStoreItem('destinationElement', destinationElement);
        this.loader.setDataStoreItem('isRedirectForm', isRedirectForm);
        this.loader.load();
    };
    /**
     * Add a URL parameter.
     * @param {string} key - The key of the URL parameter.
     * @param {string} offcanvasHierarchy - The off-canvas hierarchy.
     */
    addUrlParameter = (key, offcanvasHierarchy) => {
        let searchParams = new URLSearchParams(window.location.search);
        searchParams.set(key, offcanvasHierarchy);
        const url = new URL(window.location.href);
        url.search = searchParams.toString();
        window.history.pushState({}, '', url.toString());
    };
    /**
     * Get a URL parameter.
     * @param {string} key - The key of the URL parameter.
     * @returns {string} - The value of the URL parameter.
     */
    getUrlParameter = key => {
        let searchParams = new URLSearchParams(window.location.search);
        return searchParams.get(key);
    };
    /**
     * Manage the off-canvas state in the URL.
     * @param {HTMLElement} offcanvasElement - The off-canvas element.
     * @param {boolean} offcanvasState - The state of the off-canvas element.
     */
    manageOffcanvasStateInUrl = (offcanvasElement, offcanvasState) => {
        const offcanvasId = offcanvasElement.id;
        let offcanvasHierarchy = this.getUrlParameter('offcanvasHierarchy');
        if (!offcanvasHierarchy) {
            offcanvasHierarchy = offcanvasId;
        } else {
            if (!offcanvasHierarchy.includes(offcanvasId)) {
                offcanvasHierarchy += '.' + offcanvasId;
            }
        }
        this.addUrlParameter('offcanvasHierarchy', offcanvasHierarchy);
        if (!offcanvasState) {
            const offcanvasIds = offcanvasHierarchy.split('.');
            const index = offcanvasIds.indexOf(offcanvasId);
            if (index !== -1) {
                offcanvasIds.splice(index, 1);
                offcanvasHierarchy = offcanvasIds.join('.');
                this.addUrlParameter('offcanvasHierarchy', offcanvasHierarchy);
            }
        }
    };
    /**
     * Write content to a target element.
     * @param {HTMLElement} targetElement - The target element.
     * @param {HTMLElement} destinationElement - The destination element.
     */
    contentWriter = (targetElement, destinationElement) => {
        if (targetElement && destinationElement) {
            destinationElement.innerHTML = '';
            targetElement.querySelectorAll('*[data-bs-toggle="offcanvas"]')
                .forEach(element => {
                    element.removeAttribute('data-bs-toggle');
                    element.addEventListener('click', event => {
                        event.stopPropagation();
                        event.preventDefault();
                        const offcanvasElement = element.parentNode.querySelector(element.getAttribute('href'));
                        const offcanvas = new bootstrap.Offcanvas(offcanvasElement);
                        this.addOffCanvasRules(offcanvasElement);
                        offcanvas.show();
                        this.loadContent(offcanvasElement.getAttribute('data-lutece-redirectForm'), offcanvasElement.getAttribute('data-lutece-load-content-url'), offcanvasElement.getAttribute('data-lutece-load-content-target'), offcanvasElement);
                    });
                });
            destinationElement.appendChild(targetElement);
            if (this.loader.dataStore.isRedirectForm === 'true') {
                this.contentFormRedirect(destinationElement);
            }
            this.tabsLink(targetElement)
        }
    };
    /**
     * Handle tab links.
     * @param {HTMLElement} element - The element containing tab links.
     */
    tabsLink = (element) => {
        const tabs = element.querySelectorAll('.nav-link');
        tabs.forEach(tab => {
            tab.addEventListener('show.bs.tab', (e) => {
                const url = new URL(window.location.href);
                url.searchParams.set('tab', e.target.getAttribute('href').substring(1));
                window.history.pushState({}, '', url);
            });
        });
        const urlParams = new URLSearchParams(window.location.search);
        const activeTab = urlParams.get('tab');
        if (activeTab) {
            const tabToActivate = element.querySelector(`.nav-link[href="#` + activeTab + `"]`);
            if (tabToActivate) {
                tabToActivate.click();
            }
        }
    }
    /**
     * Handle content form redirect.
     * @param {HTMLElement} destinationElement - The destination element.
     */
    contentFormRedirect = destinationElement => {
        const form = destinationElement.querySelector('form');
        if (form && form.action) {
            form.addEventListener('submit', e => {
                e.preventDefault();
                const formData = new FormData(form);
                const isMultipart = form.enctype.toLowerCase() === 'multipart/form-data';
                const button = e.explicitOriginalTarget || e.submitter;
                if (button && button.name) {
                    formData.append(button.name, button.value);
                }
                fetch(form.action, {
                        method: 'POST',
                        body: isMultipart ? formData : new URLSearchParams(formData),
                    })
                    .then(data => {
                        // remove from hierarchy;
                        const offcanvasHierarchy = this.getUrlParameter('offcanvasHierarchy');
                        const offcanvasIds = offcanvasHierarchy.split('.');
                        offcanvasIds.pop();
                        this.addUrlParameter('offcanvasHierarchy', offcanvasIds.join('.'));
                        window.location.reload();
                    })
                    .catch(error => {
                        destinationElement.innerHTML = error;
                    });
            });
        }
    };
    /**
     * Add off-canvas rules.
     * @param {HTMLElement} offcanvasElement - The off-canvas element.
     */
    addOffCanvasRules = offcanvasElement => {
        offcanvasElement.addEventListener('hidden.bs.offcanvas', () => {
            this.loader.cancel();
            if (!offcanvasElement.classList.contains('show')) {
                offcanvasElement.querySelector('.offcanvas-body')
                    .innerHTML = '';
                this.manageOffcanvasStateInUrl(offcanvasElement, false);
                offcanvasElement.replaceWith(offcanvasElement.cloneNode(true));
            }
        });
        offcanvasElement.addEventListener('shown.bs.offcanvas', () => {
            this.manageOffcanvasStateInUrl(offcanvasElement, true);
        });
    };
    /**
     * Open the off-canvas element.
     * @param {string} offcanvasId - The ID of the off-canvas element.
     * @returns {Promise} - A promise that resolves when the off-canvas element is open.
     */
    openOffcanvas = (offcanvasId) => {
        return new Promise((resolve) => {
            const offcanvasElement = document.getElementById(offcanvasId);
            if (offcanvasElement) {
                this.addOffCanvasRules(offcanvasElement);
                const loadContentCallback = () => {
                    this.loadContent(offcanvasElement.getAttribute('data-lutece-redirectForm'), offcanvasElement.getAttribute('data-lutece-load-content-url'), offcanvasElement.getAttribute('data-lutece-load-content-target'), offcanvasElement);
                    offcanvasElement.removeEventListener('shown.bs.offcanvas', loadContentCallback);
                };
                offcanvasElement.addEventListener('shown.bs.offcanvas', loadContentCallback);
                this.loader.addEventListener('success', () => {
                    resolve();
                });
                const offcanvas = new bootstrap.Offcanvas(offcanvasElement);
                offcanvas.show();
            } else {
                resolve();
            }
        });
    };
    /**
     * Initialize the off-canvas element.
     */
    initialize = () => {
        const btn = window.document.getElementById(`btn-${this.id}`);
        btn.removeAttribute('data-bs-toggle');
        btn && btn.addEventListener('click', event => {
            event.stopPropagation();
            event.preventDefault();
            const offcanvasElement = document.querySelector(btn.getAttribute('href'));
            this.openOffcanvas(offcanvasElement.id);
        });
        const offcanvasHierarchy = this.getUrlParameter('offcanvasHierarchy');
        if (offcanvasHierarchy) {
            const offcanvasIds = offcanvasHierarchy.split('.');
            if (offcanvasIds && `${this.id}` == offcanvasIds[0]) {
                offcanvasIds.reduce((promiseChain, offcanvasId) => {
                    return promiseChain.then(() => this.openOffcanvas(offcanvasId));
                }, Promise.resolve());
            }
        }
    };
}