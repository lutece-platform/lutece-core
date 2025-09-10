<#--
  Macro: offcanvas
  Description: Generates an off-canvas component for a sliding panel overlay.
  Parameters:
  - id (string, required): the ID of the off-canvas component.
  - position (string, optional): the position of the off-canvas component - locked to end in the corporate theme.
  - title (string, optional): the title of the off-canvas component.
  - btnColor (string, optional): the color of the toggle button.
  - btnTitle (string, optional): the text on the toggle button.
  - btnIcon (string, optional): the icon for the toggle button.
  - btnClass (string, optional): additional classes for the toggle button.
  - bodyClass (string, optional): additional classes for the off-canvas body.
  - backdrop (string, optional, default="true"): whether to show a backdrop when the off-canvas component is open.
  - size (string, optional): the size of the off-canvas component ("full", "half", "auto", or "sm").
  - btnSize (string, optional): the size of the toggle button.
  - targetUrl (string, optional): the URL to load content from when the off-canvas component is opened.
  - targetElement (string, optional): the ID of the element to load content into.
  - redirectForm (boolean, optional): whether to redirect the form when submitted.
  - badgeContent (string, optional): the content of the badge on the toggle button.
  - badgeColor (string, optional): the color of the badge.
  -->
 <#macro offcanvas id position='end' title='' btnColor='primary' btnTitle='' hideTitle=[] btnTitleShow=true btnIcon='' btnClass='' bodyClass='' backdrop='true' size='auto' btnSize='' targetUrl='' targetElement='' redirectForm=true badgeContent='' badgeColor='' deprecated...>
<@deprecatedWarning args=deprecated />
<a id="btn-${id}" class="btn<#if btnColor !=''> btn-${btnColor}</#if><#if btnSize?has_content> btn-${btnSize}</#if>${btnClass}<#if badgeContent?has_content> position-relative</#if>" onclick="event.preventDefault();" data-bs-toggle="offcanvas" data-bs-scroll=false data-bs-backdrop="${backdrop}" href="#${id}" role="button" aria-controls="${id}" <#if badgeContent?has_content>style="overflow:inherit"</#if> title="${btnTitle}">
    <#if btnIcon!=''>
        <@icon style=btnIcon />
    </#if>
    <#if btnIcon!='' && btnTitle!='' && btnTitleShow>
        <span class="ms-1">
    </#if>
    <#if btnTitleShow>${btnTitle}</#if>
    <span class="visually-hidden">${btnTitle}</span>
    <#if btnIcon!='' && btnTitle!='' && btnTitleShow>
        </span>
    </#if>
    <#if badgeContent?has_content>
        <#if badgeColor?has_content>
            <#assign bgColor="bg-" + badgeColor>
        <#else>
            <#assign bgColor="bg-light text-dark">
        </#if>
        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill shadow border ${bgColor}" style="transform: translate(-50%, -50%) !important;">
            ${badgeContent}
        </span>
    </#if>
</a>
<div class="offcanvas offcanvas-end <#if size !=''>w-${size}</#if>" data-lutece-load-content-url="${targetUrl}" data-lutece-load-content-target="${targetElement}" data-lutece-redirectForm=<#if redirectForm>true<#else>false</#if> tabindex="-1" id="${id}" aria-labelledby="${id}Label">
    <div class="offcanvas-header border-bottom text-break <#if title=''>position-absolute end-0 px-2 pt-2 border-0<#else>px-4</#if>">
        <button type="button" class="border btn btn-light btn-rounded btn-icon position-absolute end-0 me-4" data-bs-dismiss="offcanvas" aria-label="Fermer">
            <i class="ti ti-x fs-5"></i>
        </button>
        <#if title!=''>
            <h2 class="offcanvas-title fw-bolder me-5 text-start" id="${id}Label">
                ${title}
            </h2>
        </#if>
    </div>
    <div id="offcanvas-body-${id}" class="offcanvas-body <#if bodyClass?has_content>${bodyClass}</#if> text-break">
        <#nested>
    </div>
</div>
<#if targetUrl?has_content>
<script type="module">
import LuteceBSOffCanvas from "./themes/shared/modules/bootstrap/luteceBSOffCanvas.js";
new LuteceBSOffCanvas(`${id}`);
</script>
</#if>
</#macro>