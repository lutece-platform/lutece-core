<#ftl strip_whitespace=true strip_text=true />
<#-- DO NOT UPDATE                            -->
<#-- WARNING : be careful to white-space and lines break in FreeMarker macros.
 # This macro template can be used to output white-space-sensitive formats (like RSS files).
 # See http://dev.lutece.paris.fr/jira/browse/LUTECE-765
-->
<#-- THEME LINKS AND LABELS VARIABLES -->
<#-- Theme Code           -->
<#assign commonsGlobalThemeCode><#if !dskey('theme.globalThemeCode')?starts_with('DS') && dskey('theme.globalThemeCode') !=''>${dskey('theme.globalThemeCode')}<#else>lutece</#if></#assign>
<#assign commonsGlobalThemeVersion><#if !dskey('theme.globalThemeVersion')?starts_with('DS') && dskey('theme.globalThemeVersion') !=''>${dskey('theme.globalThemeVersion')}<#else>1.0</#if></#assign>
<#-- Path                 -->
<#assign commonsSiteThemePath='themes/skin/${commonsGlobalThemeCode}/' /> 
<#assign commonsSharedThemePath='themes/skin/shared/' />
<#assign commonsMacrosPath='${commonsGlobalThemeCode}/macros/' />
<#assign commonsTplPath='${commonsGlobalThemeCode}/tpl/' />
<#assign commonsSiteJsPath='js/' /> 
<#assign commonsSiteCssPath='css/' /> 
<#assign commonsSiteCssImages='images/' /> 
<#-- Doc Generator Path -->
<#assign commonsFtlPath='${commonsGlobalThemeCode}/macros/' />
<#-- Theme Macros             -->
<#macro cTpl tpl=''>
<#local tplName><#if tpl=''>${.caller_template_name?keep_after("skin/")}<#else>${tpl}</#if></#local>
<#local tplPath='../themes/${commonsGlobalThemeCode}/tpl/${tplName}' >
<#assign optTemp = .get_optional_template( tplPath )>
<#if optTemp.exists>
<@optTemp.include />
<#else>
<#nested> 
</#if>
</#macro>
<#-- THEME SPEC                            -->
<#include "${commonsGlobalThemeCode}/_theme.ftl" />
<#-- MACROS LIST                            -->
<#include "theme_commons_macros.ftl" />
<#-- BANNER MANAGEMENT        -->
<#assign hasBanner><#if !dskey('theme.site_property.banner.shown.checkbox')?starts_with('DS') &&  dskey('theme.site_property.banner.shown.checkbox') =='1'>true<#else>false</#if></#assign>
<#assign urlDefaultBannerImage>${dskey('theme.site_property.banner')}</#assign>
<#assign showWholeSiteBannerEverywhere><#if dskey('theme.site_property.banner.showSiteImgEverywhere.checkbox') =='1'>false<#else>true</#if></#assign>
<#assign isBannerOnlyHome=showWholeSiteBannerEverywhere?boolean >
<#-- END BANNER MANAGEMENT    -->
<#-- MENU MANAGEMENT          -->
<#assign isRtl><#if !dskey('theme.site_property.dir.checkbox')?starts_with('DS') &&  dskey('theme.site_property.dir.checkbox') =='1'>true<#else>false</#if></#assign>
<#assign isDark><#if dskey('theme.site_property.theme.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign skipLinkMenu><#if !dskey('theme.site_property.menu.skipLinkMenu.checkbox')?starts_with('DS') && dskey('theme.site_property.menu.skipLinkMenu.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign skipLinkMainId>${dskey('theme.site_property.menu.skipLinkMainId')}</#assign>
<#assign hasDefaultMenu><#if dskey('theme.site_property.menu.hasDefaultMenu.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign isFixedMenu><#if dskey('theme.site_property.menu.fixedMenu.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign isMainSidebarMenu><#if dskey('theme.site_property.menu.sidebarMenu.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign isMainSidebarMenuCollapse><#if dskey('theme.site_property.menu.sidebarMenuCollapse.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign urlDefaultSearch>${dskey('theme.site_property.Url.search')!}</#assign>
<#-- MENU MANAGEMENT          -->
<#-- LAYOUT MANAGEMENT        -->
<#assign isLayoutFluid><#if dskey('theme.site_property.layout.type.checkbox') == '1'>true<#else>false</#if></#assign>
<#-- END LAYOUT MANAGEMENT    -->
<#-- UTILS MANAGEMENT         -->
<#assign addGoToTop><#if dskey('theme.site_property.menu.gototop.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign isTargetDefaultIconShown><#if dskey('theme.site_property.link.showTargetIcon.checkbox') == '1'>true<#else>false</#if></#assign>
<#-- END UTILS MANAGEMENT     -->