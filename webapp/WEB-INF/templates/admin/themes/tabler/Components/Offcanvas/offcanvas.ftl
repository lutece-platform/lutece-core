<#macro offcanvas id position="start" title="" btnColor="" btnTitle="" btnIcon="" backdrop="true" size="sm" btnSize="" targetUrl="" targetElement="" redirectForm=true size="">
  <@deprecatedWarning args=deprecated />
  <a id="btn-${id}" class="btn btn-primary <#if btnColor !=''>btn-${btnColor}</#if> <#if btnSize?has_content>btn-${btnSize}</#if>" data-bs-toggle="offcanvas" data-lutece-load-content-url="${targetUrl}" data-lutece-load-content-target="${targetElement}" data-lutece-redirectForm=<#if redirectForm>true<#else>false</#if> href="#${id}" role="button" aria-controls="${id}">
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
    data-bs-scroll=false data-bs-backdrop="${backdrop}" tabindex="-1" id="${id}" aria-labelledby="${id}Label">
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
      import LuteceContentLoader from './js/modules/luteceContentLoader.js';
      
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

      const contentWriter = (targetElement, destinationElement) => {
        if (targetElement && destinationElement) {
          destinationElement.innerHTML = '';
          targetElement.querySelectorAll('*[data-bs-toggle="offcanvas"]').forEach(element => {
            element.removeAttribute('data-bs-toggle');

            element.addEventListener('click', event => {
              event.stopPropagation();
              event.preventDefault();
              const offcanvasElement = element.parentNode.querySelector(element.getAttribute('href'));
              const offcanvas = new bootstrap.Offcanvas(offcanvasElement);
              addOffCanvasHiddenRules(offcanvasElement);
              offcanvas.show();

              loadContent(element.getAttribute('data-lutece-redirectForm'), element.getAttribute('data-lutece-load-content-url'), element.getAttribute('data-lutece-load-content-target'), element.parentNode.querySelector(element.getAttribute('href')));
            });
          });
          destinationElement.appendChild(targetElement);
          if(loader.dataStore.isRedirectForm) {
            contentFormRedirect(destinationElement);
          }
        }
      };

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
                window.location.reload();
              })
              .catch(error => {
                destinationElement.innerHTML = error;
              });
          });
        }
      };

      const addOffCanvasHiddenRules = offcanvasElement => {
        offcanvasElement.addEventListener('hidden.bs.offcanvas', () => {
          loader.cancel();
          for (const prop in offcanvasElement) {
            if (/^on/.test(prop) && typeof offcanvasElement[prop] === 'function') {
              offcanvasElement.removeEventListener(prop.slice(2), offcanvasElement[prop]);
            }
          }
        });
      };

      const initialize = () => {
        const btn = window.document.getElementById(`btn-${id}`);
        btn && btn.addEventListener('click', () => {
          const offcanvasElement = document.querySelector(btn.getAttribute('href'));
          loadContent( btn.getAttribute('data-lutece-redirectForm'), btn.getAttribute('data-lutece-load-content-url'), btn.getAttribute('data-lutece-load-content-target'),  offcanvasElement);
          addOffCanvasHiddenRules(offcanvasElement)
        });
      };

      window.document.addEventListener('DOMContentLoaded', initialize);
    </script>
  </#if>
</#macro>
