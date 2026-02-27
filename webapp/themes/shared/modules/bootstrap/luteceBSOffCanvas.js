/**
 * LuteceBSOffCanvas - A class for managing bootstrap off-canvas elements.
 * 
 * Supports two content loading modes:
 * 1. AJAX Mode (default): Loads HTML fragments and injects them into the offcanvas body
 * 2. Iframe Mode: Loads content in an embedded iframe (set data-lutece-use-iframe="true")
 * 
 * To use iframe mode in FreeMarker:
 * <@offcanvas id="myCanvas" targetUrl="/some/page" useIframe=true />
 * 
 * Or manually with data attributes:
 * <div class="offcanvas" data-lutece-use-iframe="true" data-lutece-load-content-url="/some/page">
 * 
 * Events:
 * - offcanvasLoaded: Fired when content finishes loading (includes method: 'ajax' or 'iframe')
 * - offcanvasLoadError: Fired when iframe fails to load
 * - offcanvasOpened: Fired when offcanvas is shown
 * - offcanvasClosed: Fired when offcanvas is hidden
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
    loadContent = async (isRedirectForm, targetUrl, targetElement, offCanvasElement) => {
        const useIframe = offCanvasElement.getAttribute('data-lutece-use-iframe') === 'true';
        
        if (useIframe) {
            // Ensure offcanvas structure exists at bottom of page
            this.loadContentIframe(targetUrl, offCanvasElement);
        } else {
            const destinationElement = offCanvasElement.querySelector('.offcanvas-body');
            this.loader.setTargetUrl(targetUrl);
            this.loader.setTargetElement(targetElement);
            this.loader.setDataStoreItem('destinationElement', destinationElement);
            this.loader.setDataStoreItem('isRedirectForm', isRedirectForm);
            await this.loader.load();
            window.dispatchEvent(new CustomEvent('offcanvasLoaded', { detail: { id: offCanvasElement.id } }));
        }
    };

    /**
     * Load content into the off-canvas element via an iframe.
     * @param {string} iframeUrl - The URL to load in the iframe.
     * @param {HTMLElement} offCanvasElement - The off-canvas element.
     */
    loadContentIframe = (iframeUrl, offCanvasElement) => {
        const bodyElement = offCanvasElement.querySelector('.offcanvas-body');
        
        if (!bodyElement) return;

        // Display loader while iframe is loading
        bodyElement.innerHTML = `<div class="offcanvas-loader d-flex justify-content-center align-items-center h-100"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div></div>`;

        // Create iframe element
        const iframe = document.createElement('iframe');
        iframe.style.width = '100%';
        iframe.style.height = '100%';
        iframe.style.border = 'none';
        iframe.style.opacity = '0'; // Hide iframe until cleanup is complete
        iframe.src = iframeUrl;
        iframe.title = offCanvasElement.getAttribute('aria-labelledby') || 'Content';

        // Listen for cleanup complete message from iframe
        const cleanupCompleteHandler = (event) => {
            if (event.data === 'offcanvasCleanupComplete') {
                // Show iframe and hide loader
                iframe.style.opacity = '1';
                const loaderElement = bodyElement.querySelector('.offcanvas-loader');
                if (loaderElement) {
                    loaderElement.remove();
                }
                window.removeEventListener('message', cleanupCompleteHandler);
            }
        };
        window.addEventListener('message', cleanupCompleteHandler);

        // Handle iframe load event
        iframe.addEventListener('load', () => {
            // Inject cleanup script into iframe
            this.injectIframeCleanupScript(iframe, bodyElement);

            window.dispatchEvent(new CustomEvent('offcanvasLoaded', { detail: { id: offCanvasElement.id, method: 'iframe' } }));
           
            iframe.contentWindow.postMessage('offcanvasLoaded', '*');
        });

        // Handle iframe error
        iframe.addEventListener('error', () => {
            console.error('Failed to load iframe:', iframeUrl);
            bodyElement.innerHTML = `<div class="alert alert-danger">Failed to load content from ${iframeUrl}</div>`;
            window.dispatchEvent(new CustomEvent('offcanvasLoadError', { detail: { id: offCanvasElement.id, url: iframeUrl } }));
            window.removeEventListener('message', cleanupCompleteHandler);
        });

        // Add iframe to body (keep loader visible)
        bodyElement.appendChild(iframe);
    };

    /**
     * Ensure that a Bootstrap offcanvas structure exists for the given ID.
     * If it doesn't exist, create it and append it to the body.
     * @param {string} offcanvasId - The ID for the offcanvas element.
     */
    ensureOffCanvasStructure = (offcanvas) => {
        
        // Create new offcanvas structure
        const offcanvasDiv = document.createElement('div');
        offcanvasDiv.id = offcanvas.dataset.luteceOffcanvasId;
        offcanvasDiv.setAttribute('tabindex', '-1');
        offcanvasDiv.className = `offcanvas ${offcanvas.dataset.luteceOffcanvasClasses}`;
        offcanvasDiv.setAttribute('data-lutece-use-iframe', offcanvas.dataset.luteceUseIframe);
        offcanvasDiv.setAttribute('data-lutece-load-content-url', offcanvas.dataset.luteceLoadContentUrl);
        offcanvasDiv.setAttribute('data-lutece-load-content-target', offcanvas.dataset.luteceLoadContentTarget);
        offcanvasDiv.setAttribute('data-lutece-redirect-form', offcanvas.dataset.luteceRedirectForm);
        
        // Create header
        const header = document.createElement('div');
        header.className = 'offcanvas-header';
        
        const title = document.createElement('h4');
        title.className = 'offcanvas-title';
        title.textContent = offcanvas.dataset.luteceOffcanvasTitle;
        
        const closeButton = document.createElement('button');
        closeButton.type = 'button';
        closeButton.className = 'btn-close';
        closeButton.setAttribute('data-bs-dismiss', 'offcanvas');
        closeButton.setAttribute('aria-label', 'Close');
        
        header.appendChild(title);
        header.appendChild(closeButton);
        
        // Create body
        const body = document.createElement('div');
        body.className = 'offcanvas-body';
        
        // Assemble offcanvas
        offcanvasDiv.appendChild(header);
        offcanvasDiv.appendChild(body);
        
        // Append to document body
        document.body.appendChild(offcanvasDiv);
    };

    /**
     * Inject a cleanup script into the iframe to remove header, footer, and other page elements.
     * This script removes elements when the iframe receives the 'offcanvasLoaded' message.
     * @param {HTMLIFrameElement} iframe - The iframe element.
     * @param {HTMLElement} bodyElement - The offcanvas body element containing the loader.
     */
    injectIframeCleanupScript = (iframe, bodyElement) => {
        try {
            const iframeDoc = iframe.contentDocument || iframe.contentWindow.document;
            
            if (!iframeDoc) return;

            // Create and inject the cleanup script
            const script = iframeDoc.createElement('script');
            script.textContent = `
                function performCleanup() {
                    // Remove header element
                    const header = document.querySelector('header');
                    if (header) header.remove();
                    
                    // Remove footer element
                    const footer = document.querySelector('footer');
                    if (footer) footer.remove();
                    
                    // Remove top nav
                    const pageNav = document.querySelector('header.navbar-expand-md');
                    if (pageNav) pageNav.remove();
                    
                    // Remove page header elements
                    const pageHeader = document.querySelector('.page-header');
                    if (pageHeader) pageHeader.remove();
                    
                    const pageHeaderById = document.querySelector('#page-header');
                    if (pageHeaderById) pageHeaderById.remove();
                    
                    // Remove breadcrumb
                    const breadcrumb = document.querySelector('.breadcrumb');
                    if (breadcrumb) breadcrumb.remove();
                   
                    // Remove page_body class
                    const pageBody = document.querySelector('.page-body');
                    const pageBodyContainer = document.querySelector('.page-body .container-xl');
                    if (pageBody) pageBody.classList.remove('page-body') 
                    if (pageBodyContainer) pageBodyContainer.classList.remove('container-xl');
                    
                    // Add white background
                    if (document.querySelector('body')) {
                        document.querySelector('body').classList.add('bg-white');
                    }
                        
                    // Show layout wrapper if present
                    const layoutWrapper = document.querySelector('#lutece-layout-wrapper');
                    if (layoutWrapper) {
                        layoutWrapper.classList.remove('d-none');
                    }

                    document.querySelectorAll('.btn.form-validation').forEach( btn => {
                        if( btn.nodeName === 'BUTTON' ) {
                            btn.setAttribute('formtarget', '_top');
                        } else if (btn.nodeName === 'A') {
                            btn.setAttribute('target', '_top');
                        }
                    });
                    window.parent.postMessage('offcanvasCleanupComplete', '*');
                    document.body.style.opacity = '1'; // Show content after cleanup is done
                }

                window.addEventListener('load', function() {
                    document.body.style.opacity = '0'; // Hide content until cleanup is done
                    performCleanup();
                });

                window.addEventListener('message', function(event) {
                    if (event.data === 'offcanvasLoaded') {
                        performCleanup();
                        window.parent.postMessage('offcanvasCleanupComplete', '*');
                    }
                });
            `;
            
            iframeDoc.body.appendChild(script);
        } catch (error) {
            console.warn('Could not inject cleanup script into iframe (possibly cross-origin):', error);
            // Show iframe anyway if we can't inject cleanup script
            iframe.style.opacity = '1';
            if (bodyElement) {
                const loaderElement = bodyElement.querySelector('.offcanvas-loader');
                if (loaderElement) {
                    loaderElement.style.display = 'none';
                }
            }
        }
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
                        if ( offcanvasElement.getAttribute('data-lutece-load-content-url') ) {
                            this.addOffCanvasRules(offcanvasElement);
                            offcanvas.show();
                            this.loadContent(offcanvasElement.getAttribute('data-lutece-redirect-form'), offcanvasElement.getAttribute('data-lutece-load-content-url'), offcanvasElement.getAttribute('data-lutece-load-content-target'), offcanvasElement);
                        } else {
                            offcanvas.show();
                        }
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
     * @param {HTMLElement} offcanvasElement -  The off-canvas element.
     */
    addOffCanvasRules = offcanvasElement => {
        offcanvasElement.addEventListener('hidden.bs.offcanvas', () => {
            this.loader.cancel();
            if (!offcanvasElement.classList.contains('show')) {
                offcanvasElement.querySelector('.offcanvas-body')
                    .innerHTML = '';
                this.manageOffcanvasStateInUrl(offcanvasElement, false);
                offcanvasElement.replaceWith(offcanvasElement.cloneNode(true));
                window.dispatchEvent(new CustomEvent('offcanvasClosed', { detail: { id: offcanvasElement.id } }));
            }
        });
        offcanvasElement.addEventListener('shown.bs.offcanvas', () => {
            this.manageOffcanvasStateInUrl(offcanvasElement, true);
            window.dispatchEvent(new CustomEvent('offcanvasOpened', { detail: { id: offcanvasElement.id } }));
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
        if (!btn) return;
        
        btn.removeAttribute('data-bs-toggle');
        btn.addEventListener('click', event => {
            event.stopPropagation();
            event.preventDefault();
            // Use this.id directly since we know the offcanvas ID from constructor
            if( btn.getAttribute('data-lutece-use-iframe') === 'true' ) {
                this.ensureOffCanvasStructure(btn);
            }
            this.openOffcanvas(this.id);
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