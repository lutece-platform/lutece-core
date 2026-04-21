<#--
Macro: _footerMenu

Description: Generates the default footer menu links (legal, CGU, accessibility, sitemap, wiki). This is a private macro used internally by cMainFooter and cMainFooterSocial.

Parameters:
- None.

Snippet:

    Internal usage within a footer macro:

    <@_footerMenu />

-->
<#macro _footerMenu>
<#if footerLinkLegal !=''>
<#if !dskey('theme.site_property.Url.legalURLLabel')?starts_with('DS') && dskey('theme.site_property.Url.legalURLLabel') !=''>
<#local title=dskey('theme.site_property.Url.legalURLLabel') /><#else><#local title='#i18n{portal.theme.labelLegalInfo}' /></#if>
<@_footerLinkItem title=title url=footerLinkLegal role='' target='_blank'  />
</#if>
<#if footerLinkCgu !=''>
<#if !dskey('theme.site_property.Url.cguURLLabel')?starts_with('DS') && dskey('theme.site_property.Url.cguURLLabel') !=''>
<#local title=dskey('theme.site_property.Url.cguURLLabel') /><#else><#local title='#i18n{portal.theme.labelCgu}' /></#if>
<@_footerLinkItem title=title url=footerLinkCgu role='' target='_blank' />
</#if>
<#if footerLinkAccessibility !=''>
<#if !dskey('theme.site_property.Url.accessibilityLabel')?starts_with('DS') && dskey('theme.site_property.Url.accessibilityLabel') !=''>
<#local title=dskey('theme.site_property.Url.accessibilityLabel') /><#else><#local title='#i18n{portal.theme.labelAccessibility}' /></#if>
<@_footerLinkItem title=title url=footerLinkAccessibility role='' target='_blank' />
</#if>
<@_footerLinkItem title='${mainSite}' url=urlMainSite role='' target='_blank' />
<#if hasSiteMap?boolean>
<@_footerLinkItem title='#i18n{portal.site.site_map.pageTitle} 'role='' url="jsp/site/Portal.jsp?page=map" />
</#if>
<#if footerLinkWiki !=''>
<#if !dskey('theme.site_property.Url.wikiURLLabel')?starts_with('DS') && dskey('theme.site_property.Url.wikiURLLabel') !=''>
<#local title=dskey('theme.site_property.Url.wikiURLLabel') /><#else><#local title='#i18n{portal.theme.labelWiki}' /></#if>
<@_footerLinkItem title=title url=footerLinkWiki role='' target='_blank' />
</#if>
<#--
<#if footerLinkDataProtection !=''>
<#if !dskey('theme.site_property.Url.dataURLLabel')?starts_with('DS') && dskey('theme.site_property.Url.dataURLLabel') !=''>
<#local title=dskey('theme.site_property.Url.dataURLLabel') /><#else><#local title='#i18n{portal.theme.labelDataProtection}' /></#if>
<@_footerLinkItem title=title url=footerLinkDataProtection role='' target='_blank' />
</#if>
<#if !dskey('theme.site_property.Url.cookieURLLabel')?starts_with('DS') && dskey('theme.site_property.Url.cookieURLLabel') !=''>
<#local title=dskey('theme.site_property.Url.cookieURLLabel') /><#else><#local title='#i18n{portal.theme.labelCookies}' /></#if>
<@_footerLinkItem title=title url=footerLinkCookies role='' target='_blank' />
-->
</#macro>
<#--
Macro: _footerLinkItem

Description: Generates a single footer navigation link item as a list element. This is a private macro used internally by _footerMenu.

Parameters:
- title (string, required): Label text for the footer link.
- url (string, required): Destination URL for the link. If empty, only nested content is rendered without a link wrapper.
- urlClass (string, optional): CSS class(es) for the anchor element. Default: ''.
- target (string, optional): Link target attribute ('_top', '_blank', '_parent'). Default: ''.
- role (string, optional): ARIA role for the link. Default: 'navitem'.
- showTitle (boolean, optional): If true, adds a title attribute with the label text. Default: false.
- id (string, optional): Unique identifier for the list item. Default: ''.
- class (string, optional): Additional CSS class(es) for the list item. Default: ''.
- params (string, optional): Additional HTML attributes for the list item. Default: ''.

Snippet:

    Basic footer link:

    <@_footerLinkItem title='Legal Notice' url='jsp/site/Portal.jsp?page=legal' target='_blank' />

    Footer link with nested icon:

    <@_footerLinkItem title='Site Map' url='jsp/site/Portal.jsp?page=map'>
        <i class="ti ti-map"></i>
    </@_footerLinkItem>

-->
<#macro _footerLinkItem title url urlClass='' target='' role='navitem' showTitle=false id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li class="list-inline-item<#if class !='' > ${class!}</#if>"<#if id !='' > id="${id!}"</#if><#if params!=''> ${params}</#if>>
<#if url !=''>
    <a <#if urlClass !='' >class="${urlClass!}"</#if><#if role !=''> role="${role!}"</#if> href="${url}" <#if showTitle>title="${title!}"</#if><#if target!=''> target="${target}"</#if>>
        ${title!}<#if target='_blank'> <span class="visually-hidden">#i18n{portal.theme.newWindowLink}</span></#if>
        <#nested>
    </a>
<#else>
    <#nested>
</#if>
</li> 
</#macro>