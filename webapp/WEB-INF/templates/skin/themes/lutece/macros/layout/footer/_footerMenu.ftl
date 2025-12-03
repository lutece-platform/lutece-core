<#-- Macro: _footerMenu

Description: Private only used in cMainFooter and cMainFooterSocial macros

Parameters:
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
<#-- Macro: _footerLinkItem

Description: affiche un élément de navigation.

Parameters:
@param - id - string - optional - identifiant unique de l'élément de navigation
@param - class - string - optional - classe(s) css de l'élément de navigation
@param - title - string - required - titre de l'élément de navigation
@param - url - string - required - url de redirection de l'élément de navigation, si vide n'ajoute pas la balise a autour du contenu #nested
@param - urlClass - string - optional - classe(s) css de l'élément lien de navigation
@param - target - string - optional - les valeurs possibles sont '', _top, _blank, _parent
@param - role - string - optional - les valeurs possibles sont '', navitem
@param - showTitle - boolean - required -  ajoute l'attribute title avec le libellé du paramètre "title"
@param - params - string - optional - permet d'ajouter des paramètres HTML à l'élément de navigation
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