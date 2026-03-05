<#--
Macro: cMainNavItem

Description: Generates a single navigation item (li element with an optional link) for use inside the main navigation bar.

Parameters:
- title (string, required): Label text for the navigation item.
- url (string, required): Destination URL. If empty, only nested content is rendered without a link wrapper.
- urlClass (string, optional): CSS class(es) for the anchor element. Default: ''.
- target (string, optional): Link target attribute ('_top', '_blank', '_parent'). Default: ''.
- role (string, optional): ARIA role for the link. Default: 'navitem'.
- showTitle (boolean, optional): If true, adds a title attribute with the label text. Default: false.
- id (string, optional): Unique identifier for the list item. Default: ''.
- class (string, optional): Additional CSS class(es) for the list item. Default: ''.
- params (string, optional): Additional HTML attributes for the list item. Default: ''.

Snippet:

    Basic navigation item:

    <@cMainNavItem title='Home' url='.' />

    Navigation item opening in a new window:

    <@cMainNavItem title='Documentation' url='https://lutece.paris.fr/docs' target='_blank' />

    Navigation item with nested icon:

    <@cMainNavItem title='' url='jsp/site/Portal.jsp?page=search'>
        <i class="ti ti-search"></i>
        <span class="visually-hidden">Search</span>
    </@cMainNavItem>

-->
<#macro cMainNavItem title url urlClass='' target='' role='navitem' showTitle=false id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li class="nav-item<#if class !='' > ${class!}</#if>"<#if id !='' > id="${id!}"</#if><#if params!=''> ${params}</#if>>
<#if url !=''>
    <a class="nav-link<#if urlClass !='' > ${urlClass!}</#if>"<#if role !=''> role="${role!}"</#if> href="${url}" <#if showTitle>title="${title!}"</#if><#if target!=''> target="${target}"</#if>>
        ${title!}<#if target='_blank'> <span class="visually-hidden">#i18n{portal.theme.newWindowLink}</span></#if>
        <#nested>
    </a>
<#else>
    <#nested>
</#if>
</li> 
</#macro>