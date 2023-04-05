<#macro offcanvas id position="start" title="" btnColor="" btnTitle="" btnIcon="" backdrop="true" size="sm" btnSize="" targetUrl="" targetElement="" redirectForm=true size="">
  <@deprecatedWarning args=deprecated />
  <a id="btn-${id}" class="btn btn-primary <#if btnColor !=''>btn-${btnColor}</#if> <#if btnSize?has_content>btn-${btnSize}</#if>" data-bs-toggle="offcanvas" data-bs-scroll=false data-bs-backdrop="${backdrop}" href="#${id}" role="button" aria-controls="${id}">
    <#if btnIcon!="">
      <@icon style=btnIcon />
    </#if>
        <#if btnIcon!="" && btnTitle!="">
    &nbsp;
    </#if>
    ${btnTitle}
    <#if btnIcon="">
      <i class="ti ti-arrow-narrow-right"></i>
    </#if>
  </a>
  <div class="offcanvas offcanvas-${position} 
    <#if size == 'full'>h-100 w-100
    <#elseif size == 'half' && (position == 'end' || position == 'start')>w-50
    <#elseif size == 'half' && (position == 'top' || position == 'bottom')>h-50
    <#elseif size == 'auto' && (position == 'end' || position == 'start')>w-auto
    <#elseif size == 'auto' && (position == 'top' || position == 'bottom')>h-auto
    </#if>" 
     data-lutece-load-content-url="${targetUrl}" data-lutece-load-content-target="${targetElement}" data-lutece-redirectForm=<#if redirectForm>true<#else>false</#if> tabindex="-1" id="${id}" aria-labelledby="${id}Label">
    <div class="offcanvas-header text-break <#if title=''>position-absolute end-0 px-2 pt-2 border-0<#else>px-4</#if>">
       <#if title!=''><h2 class="offcanvas-title fw-bolder" id="${id}Label">${title}</h2></#if>
      <button type="button" class="ms-3 border btn btn-light btn-rounded btn-icon" data-bs-dismiss="offcanvas" aria-label="Close">
        <i class="ti ti-x fs-5"></i>
      </button>
    </div>
    <div id="offcanvas-body-${id}" class="offcanvas-body px-4 text-break">
      <#nested>
    </div>
  </div>
  <#if targetUrl?has_content>
    <script type="module">
      import LuteceContentLoader from './themes/shared/modules/luteceContentLoader.js';
      const loader = new LuteceContentLoader();
      loader.addEventListener('start', () => {
          loader.dataStore.destinationElement.innerHTML = `<div class="d-flex justify-content-center align-items-center h-100"><div class="spinner-border text-primary" role="status"></div></div>`;
      });
      loader.addEventListener('success', event => {
          contentWriter(event.detail.targetElement, loader.dataStore.destinationElement);
      });
      loader.addEventListener('error', event => {
          window.location.href = loader.targetUrl;
      });
      loader.addEventListener('warning', event => {
          window.location.href = loader.targetUrl;
      });
      const loadContent = (isRedirectForm, targetUrl, targetElement, offCanvasElement) => {
          const destinationElement = offCanvasElement.querySelector('.offcanvas-body');
          loader.setTargetUrl(targetUrl);
          loader.setTargetElement(targetElement);
          loader.setDataStoreItem('destinationElement', destinationElement);
          loader.setDataStoreItem('isRedirectForm', isRedirectForm);
          loader.load();
      };
      const addUrlParameter = (key, offcanvasHierarchy) => {
          let searchParams = new URLSearchParams(window.location.search);
          searchParams.set(key, offcanvasHierarchy);
          const url = new URL(window.location.href);
          url.search = searchParams.toString();
          window.history.pushState({}, '', url.toString());
      };
      const getUrlParameter = key => {
          let searchParams = new URLSearchParams(window.location.search);
          return searchParams.get(key);
      };
      const manageOffcanvasStateInUrl = (offcanvasElement, offcanvasState) => {
          const offcanvasId = offcanvasElement.id;
          let offcanvasHierarchy = getUrlParameter('offcanvasHierarchy');
          if (!offcanvasHierarchy) {
              offcanvasHierarchy = offcanvasId;
          } else {
              if (!offcanvasHierarchy.includes(offcanvasId)) {
                  offcanvasHierarchy += '.' + offcanvasId;
              }
          }
          addUrlParameter('offcanvasHierarchy', offcanvasHierarchy);
          if (!offcanvasState) {
              const offcanvasIds = offcanvasHierarchy.split('.');
              const index = offcanvasIds.indexOf(offcanvasId);
              if (index !== -1) {
                  offcanvasIds.splice(index, 1);
                  offcanvasHierarchy = offcanvasIds.join('.');
                  addUrlParameter('offcanvasHierarchy', offcanvasHierarchy);
              }
          }
      };
      const contentWriter = (targetElement, destinationElement) => {
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
                          addOffCanvasRules(offcanvasElement);
                          offcanvas.show();
                          loadContent(offcanvasElement.getAttribute('data-lutece-redirectForm'), offcanvasElement.getAttribute('data-lutece-load-content-url'), offcanvasElement.getAttribute('data-lutece-load-content-target'), offcanvasElement);
                      });
                  });
              destinationElement.appendChild(targetElement);
              if (loader.dataStore.isRedirectForm) {
                  contentFormRedirect(destinationElement);
              }
              tabsLink(targetElement)
          }
      };
      const tabsLink = ( element ) => {
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
          const tabToActivate = element.querySelector(`.nav-link[href="#`+activeTab+`"]`);
          if (tabToActivate) {
            tabToActivate.click();
          }
        }
              }
     const contentFormRedirect = destinationElement => {
        const form = destinationElement.querySelector('form');
        if (form && form.action) {
          form.addEventListener('submit', e => {
            e.preventDefault();
            const formData = new FormData(form);
            const isMultipart = form.enctype.toLowerCase() === 'multipart/form-data';
            fetch(form.action, {
              method: 'POST',
              body: isMultipart ? formData : new URLSearchParams(formData),
            })
              .then(data => {
                // remove from hierarchy;
                const offcanvasHierarchy = getUrlParameter('offcanvasHierarchy');
                const offcanvasIds = offcanvasHierarchy.split('.');
                offcanvasIds.pop();
                addUrlParameter('offcanvasHierarchy', offcanvasIds.join('.'));
                window.location.reload();
              })
              .catch(error => {
                destinationElement.innerHTML = error;
              });
          });
        }
      };
      const addOffCanvasRules = offcanvasElement => {
          offcanvasElement.addEventListener('hidden.bs.offcanvas', () => {
              loader.cancel();
              if (!offcanvasElement.classList.contains('show')) {
                  offcanvasElement.querySelector('.offcanvas-body')
                      .innerHTML = '';
                  manageOffcanvasStateInUrl(offcanvasElement, false);
                  offcanvasElement.replaceWith(offcanvasElement.cloneNode(true));
              }
          });
          offcanvasElement.addEventListener('shown.bs.offcanvas', () => {
              manageOffcanvasStateInUrl(offcanvasElement, true);
          });
      };
      const openOffcanvas = (offcanvasId) => {
          return new Promise((resolve) => {
              const offcanvasElement = document.getElementById(offcanvasId);
              if (offcanvasElement) {
                  addOffCanvasRules(offcanvasElement);
                  const loadContentCallback = () => {
                      loadContent(offcanvasElement.getAttribute('data-lutece-redirectForm'), offcanvasElement.getAttribute('data-lutece-load-content-url'), offcanvasElement.getAttribute('data-lutece-load-content-target'), offcanvasElement);
                      offcanvasElement.removeEventListener('shown.bs.offcanvas', loadContentCallback);
                  };
                  offcanvasElement.addEventListener('shown.bs.offcanvas', loadContentCallback);
                  loader.addEventListener('success', () => {
                      resolve();
                  });
                  const offcanvas = new bootstrap.Offcanvas(offcanvasElement);
                  offcanvas.show();
              } else {
                  resolve();
              }
          });
      };
      const initialize = () => {
          const btn = window.document.getElementById(`btn-${id}`);
          btn.removeAttribute('data-bs-toggle');
          btn && btn.addEventListener('click', event => {
              event.stopPropagation();
              event.preventDefault();
              const offcanvasElement = document.querySelector(btn.getAttribute('href'));
              openOffcanvas(offcanvasElement.id);
          });
          const offcanvasHierarchy = getUrlParameter('offcanvasHierarchy');
          if (offcanvasHierarchy) {
              const offcanvasIds = offcanvasHierarchy.split('.');
              if (offcanvasIds && `${id}` == offcanvasIds[0]) {
                  const openOffcanvasSequence = offcanvasIds.reduce((promiseChain, offcanvasId) => {
                      return promiseChain.then(() => openOffcanvas(offcanvasId));
                  }, Promise.resolve());
              }
          }
      };
      window.document.addEventListener('DOMContentLoaded', initialize);
    </script>
  </#if>
</#macro>
