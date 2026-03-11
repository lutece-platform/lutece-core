<#--
  Macro: offcanvas
  Description: Generates an off-canvas component for a sliding panel overlay.
  Parameters:
  - id (string, required): the ID of the off-canvas component.
  - position (string, optional): the position of the off-canvas component ("start", "end", "top", or "bottom").
  - title (string, optional): the title of the off-canvas component.
  - btnColor (string, optional): the color of the toggle button.
  - btnTitle (string, optional): the text on the toggle button.
  - btnDropdown (boolean, optional, default=false): whether to add adropdown menu set in btnDropdownContent
  - btnDropdownContent (string, optional): Dropdonw menu content added if btnDropdown is true.
  - hideTitle (array, optional): an array of breakpoints to hide the button title.
  - btnIcon (string, optional): the icon for the toggle button.
  - btnClass (string, optional): additional classes for the toggle button.
  - bodyClass (string, optional): additional classes for the off-canvas body.
  - backdrop (string, optional, default="true"): whether to show a backdrop when the off-canvas component is open.
  - size (string, optional): the size of the off-canvas component ("full", "half", "auto", or "sm").
  - btnSize (string, optional): the size of the toggle button.
  - targetUrl (string, optional): the URL to load content from when the off-canvas component is opened.
  - targetElement (string, optional): the ID of the element to load content into.
  - useIframe (boolean, optional, default=false): whether to load content via iframe instead of AJAX.
  - redirectForm (boolean, optional): whether to redirect the form when submitted.
  - reloadOnClose (boolean, optional, default=false): whether to reload the parent page when the off-canvas is closed.
  - badgeContent (string, optional): the content of the badge on the toggle button.
  - badgeColor (string, optional): the color of the badge.

  Snippet:

    Basic offcanvas panel with nested content:

    <@offcanvas id='detailsPanel' title='Details' btnTitle='Open Details' btnIcon='info-circle'>
        <p>Here is the content of the offcanvas panel.</p>
    </@offcanvas>

    Offcanvas loading content from a URL:

    <@offcanvas id='editPanel' position='end' title='Edit Record' btnTitle='Edit' btnIcon='pencil' btnColor='primary' size='half' targetUrl='jsp/admin/EditRecord.jsp?id=1' targetElement='#edit-form' />

    Offcanvas with badge and custom backdrop:

    <@offcanvas id='notifications' position='start' title='Notifications' btnTitle='Alerts' btnIcon='bell' btnColor='warning' backdrop='true' badgeContent='3' badgeColor='danger'>
        <p>You have 3 unread notifications.</p>
    </@offcanvas>

    Full-width bottom offcanvas:

    <@offcanvas id='filterPanel' position='bottom' title='Filters' btnTitle='Show Filters' btnIcon='filter' size='full' btnSize='sm'>
        <p>Filter options go here.</p>
    </@offcanvas>

  -->
<#macro offcanvas id position='end' class='' title='' btnColor='primary' btnTitle='' btnDropdown=false btnDropdownContent='' hideTitle=[] btnIcon='' btnClass='' btnDisabled=false bodyClass='' badgeContent='' badgeColor='' backdrop='true' size='auto' btnSize='' targetUrl='' targetElement='' useIframe=false redirectForm=true reloadOnClose=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if btnDropdown><div class="btn-group"></#if>
<a id="btn-${id}" class="btn<#if btnColor !=''> btn-${btnColor}</#if><#if btnSize?has_content> btn-${btnSize}</#if><#if btnClass!=''> ${btnClass}</#if><#if badgeContent?has_content> position-relative</#if>"<#if btnDisabled> disabled</#if> onclick="event.preventDefault();" data-bs-toggle="offcanvas" data-bs-scroll=false data-bs-backdrop="${backdrop}" href="#${id}" role="button" aria-controls="${id}" <#if badgeContent?has_content>style="overflow:inherit"</#if><#if params!=''> ${params}</#if>>
    <#if btnIcon!=''><@icon style='${btnIcon}' /></#if>
    <#-- Visibility of button title -->
    <#local displayTitleClass = displaySettings( hideTitle,'inline-flex') />
    <#if displayTitleClass != ''><span class="${displayTitleClass}"></#if>${btnTitle}<#if displayTitleClass != ''></span></#if>
    <#if badgeContent?has_content><#if badgeColor?has_content><#assign bgColor="bg-" + badgeColor><#else><#assign bgColor=" text-dark"></#if>
        <@tag color=badgeColor class='position-absolute top-0 start-100 translate-middle z-2'>${badgeContent}</@tag>
    </#if>
</a>
<#if btnDropdown>
${btnDropdownContent!}
</div>
</#if>
<#if useIframe>
<script>
(function() {
    const btn = document.getElementById('btn-${id}');
    if ( btn ) {
        btn.setAttribute('data-lutece-offcanvas-id', '${id}'); 
        btn.setAttribute('data-lutece-offcanvas-title', '#i18n{portal.util.labelModify}'); 
        btn.setAttribute('data-lutece-offcanvas-classes', 'offcanvas-${position}<#if size !=''> w-${size}</#if><#if class!=''> ${class}</#if>');
        btn.setAttribute('data-lutece-use-iframe', 'true');
        btn.setAttribute('data-lutece-load-content-url', '${targetUrl}');
        btn.setAttribute('data-lutece-load-content-target', '${targetElement}');
        btn.setAttribute('data-lutece-redirect-form', '<#if redirectForm>true<#else>false</#if>');
        btn.setAttribute('data-lutece-reload-on-close', '<#if reloadOnClose>true<#else>false</#if>');
    }
})();
</script>
<#else>
<div class="offcanvas offcanvas-${position} <#if size !=''>w-${size}</#if><#if class!=''> ${class}</#if>" data-lutece-load-content-url="${targetUrl}" data-lutece-load-content-target="${targetElement}" data-lutece-use-iframe=<#if useIframe>true<#else>false</#if> data-lutece-redirectForm=<#if redirectForm>true<#else>false</#if> data-lutece-reload-on-close=<#if reloadOnClose>true<#else>false</#if> tabindex="-1" id="${id}" aria-labelledby="${id}Label">
    <div class="offcanvas-header border-bottom text-break <#if title=''>position-absolute end-0 px-2 pt-2 border-0<#else>px-4</#if>">
        <button type="button" class="border btn btn-light btn-rounded btn-icon position-absolute end-0 me-4" data-bs-dismiss="offcanvas" aria-label="Fermer">
            <i class="ti ti-x fs-5"></i>
        </button>
        <#if title!=''><h2 class="offcanvas-title fw-bolder me-5 text-start" id="${id}Label">${title}</h2></#if>
    </div>
    <div id="offcanvas-body-${id}" class="offcanvas-body <#if bodyClass?has_content>${bodyClass}</#if> text-break">
        <#nested>
    </div>
</div>
</#if>
<#if targetUrl?has_content>
<script type="module">
import LuteceBSOffCanvas from "./themes/shared/modules/bootstrap/luteceBSOffCanvas.js";
new LuteceBSOffCanvas(`${id}`);
</script>
</#if>
</#macro>