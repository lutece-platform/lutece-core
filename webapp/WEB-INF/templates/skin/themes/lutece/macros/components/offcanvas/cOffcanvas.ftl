<#--
Macro: cOffcanvas

Description: Generates a Bootstrap offcanvas sliding panel with an optional toggle button, supporting AJAX and iframe content loading.

Parameters:
- id (string, required): Unique identifier for the offcanvas component.
- position (string, optional): Slide-in direction ('start', 'end', 'top', 'bottom'). Default: 'end'.
- class (string, optional): Additional CSS classes for the offcanvas element. Default: ''.
- title (string, optional): Title displayed in the offcanvas header. Default: ''.
- btnTitle (string, optional): Text on the toggle button (button is only rendered if set). Default: ''.
- btnColor (string, optional): Bootstrap color variant for the toggle button. Default: 'primary'.
- btnTitleShow (boolean, optional): Whether to display the button title text visually. Default: true.
- btnIcon (string, optional): Paris icon name for the toggle button. Default: ''.
- btnClass (string, optional): Additional CSS classes for the toggle button. Default: ''.
- bodyClass (string, optional): Additional CSS classes for the offcanvas body. Default: ''.
- backdrop (string, optional): Whether to show a backdrop ('true' or 'false'). Default: 'true'.
- size (string, optional): Width class suffix ('full', 'half', 'auto', 'sm'). Default: 'auto'.
- btnSize (string, optional): Bootstrap button size class suffix. Default: ''.
- targetUrl (string, optional): URL to load content from via AJAX when opened. Default: ''.
- targetElement (string, optional): ID of the element to load AJAX content into. Default: ''.
- useIframe (boolean, optional): Whether to load content via iframe instead of AJAX. Default: false.
- redirectForm (boolean, optional): Whether to redirect form submissions. Default: true.
- badgeContent (string, optional): Text content for a badge on the toggle button. Default: ''.
- badgeColor (string, optional): Bootstrap color for the badge. Default: ''.

Showcase:
- desc: Panneau coulissant - @cOffcanvas
- guide: navigation
- bs: components/offcanvas
- newFeature: true

Snippet:

    Basic offcanvas with toggle button:

    <@cOffcanvas id='sidePanel' btnTitle='Open panel' title='Side Panel'>
        <p>Panel content goes here.</p>
    </@cOffcanvas>

    Offcanvas with icon button and badge:

    <@cOffcanvas id='notifications' btnTitle='Notifications' btnIcon='bell' btnColor='outline-primary' badgeContent='3' badgeColor='danger' title='Your notifications'>
        <p>You have 3 new notifications.</p>
    </@cOffcanvas>

    Offcanvas loading content via AJAX:

    <@cOffcanvas id='detailPanel' btnTitle='View details' position='end' size='half' targetUrl='jsp/site/Portal.jsp?page=detail' targetElement='detailContent' title='Details'>
        <div id="detailContent">Loading...</div>
    </@cOffcanvas>

-->
<#macro cOffcanvas id position='end' class='' title='' btnTitle='' btnColor='primary' btnTitleShow=true btnIcon='' btnClass='' bodyClass='' backdrop='true' size='auto' btnSize='' targetUrl='' targetElement='' useIframe=false redirectForm=true badgeContent='' badgeColor='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if btnTitle !=''>
<a id="btn-${id}" class="btn<#if btnColor !=''> btn-${btnColor} </#if><#if btnSize?has_content> btn-${btnSize}</#if> ${btnClass}<#if badgeContent?has_content> position-relative</#if>" onclick="event.preventDefault();" data-bs-toggle="offcanvas" data-bs-scroll=false data-bs-backdrop="${backdrop}" href="#${id}" role="button" aria-controls="${id}" <#if badgeContent?has_content>style="overflow:inherit"</#if> title="${btnTitle}">
    <#if btnIcon!=''><@parisIcon name=btnIcon /></#if>
    <#if btnIcon!='' && btnTitle!='' && btnTitleShow><span class="ms-xs"></#if>
    <#if btnTitleShow>${btnTitle}</#if>
    <span class="visually-hidden">${btnTitle}</span>
    <#if btnIcon!='' && btnTitle!='' && btnTitleShow></span></#if>
    <#if badgeContent?has_content><#if badgeColor?has_content><#assign bgColor="bg-" + badgeColor><#else><#assign bgColor="bg-light text-dark"></#if>
    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill shadow border ${bgColor}" style="transform: translate(-50%, -50%) !important;">${badgeContent}</span>
    </#if>
</a>
</#if>
<div class="offcanvas offcanvas-${position} <#if size !=''>w-${size}</#if><#if class!=''> ${class}</#if>" data-lutece-load-content-url="${targetUrl}" data-lutece-load-content-target="${targetElement}" data-lutece-use-iframe=<#if useIframe>true<#else>false</#if> data-lutece-redirectForm=<#if redirectForm>true<#else>false</#if> tabindex="-1" id="${id}" aria-labelledby="${id}Label">
    <div class="offcanvas-header border-bottom d-flex justify-content-between align-items-center">
        <#if title!=''><h2 class="h4 offcanvas-title p-0 pe-xl m-0 me-xl" id="${id}Label">${title}</h2></#if>
        <button type="button" class="border btn btn-light btn-rounded btn-icon end-0 mx-sm p-0" data-bs-dismiss="offcanvas" aria-label="Fermer">
            <@parisIcon name='close' class='mt-xs' />
        </button>
    </div>
    <div id="offcanvas-body-${id}" class="offcanvas-body<#if bodyClass?has_content> ${bodyClass}</#if> text-break ps-xl">
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