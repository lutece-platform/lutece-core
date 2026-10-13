<#ftl strip_whitespace=true strip_text=true />
<#-- DO NOT UPDATE                            -->
<#-- WARNING : be careful to white-space and lines break in FreeMarker macros.
 # This macro template can be used to output white-space-sensitive formats (like RSS files).
 # See http://dev.lutece.paris.fr/jira/browse/LUTECE-765
-->
<#-- THEME LINKS AND LABELS VARIABLES -->
<#-- Theme Code           -->
<#assign commonsGlobalThemeCode = ( !dskey('theme.globalThemeCode')?starts_with('DS') && dskey('theme.globalThemeCode') !='' )?then( ( dskey('theme.globalThemeCode') )?string, 'lutece' ) />
<#assign commonsGlobalThemeVersion = ( !dskey('theme.globalThemeVersion')?starts_with('DS') && dskey('theme.globalThemeVersion') !='' )?then( ( dskey('theme.globalThemeVersion') )?string, '1.0' ) />
<#-- Path                 -->
<#assign commonsSiteSharedPath='themes/shared/' /> 
<#assign commonsSiteThemePath='themes/skin/${commonsGlobalThemeCode}/' /> 
<#assign commonsSharedThemePath='themes/skin/shared/' />
<#assign commonsTplPath='${commonsGlobalThemeCode}/tpl/' />
<#assign commonsSiteJsPath='js/' /> 
<#assign commonsSiteJsModulesPath='js/modules/' /> 
<#assign commonsSiteCssPath='css/' /> 
<#assign commonsSiteImagesPath='images/' /> 
<#assign commonsMacrosPath='macros/' />
<#assign commonsFtlPath='${commonsGlobalThemeCode}/macros/' />
<#-- Theme Macros                                                               -->
<#-- MACRO cTpl : If find theme template then use it else use current template  -->
<#macro cTpl tpl=''>
<#local tplName = ( !tpl?has_content )?then( ( .caller_template_name?keep_after("skin/") )?string, ( tpl )?string ) />
<#local tplPath='../themes/${commonsGlobalThemeCode}/tpl/${tplName}' >
<#assign optTemp = .get_optional_template( tplPath )>
<#if optTemp.exists>
<@optTemp.include />
<#else>
<#nested> 
</#if>
</#macro>
<#-- MACRO cMacro : If find theme macro then use it else use current one -->
<#macro cMacro name='' group='' >
<#local macroPath='../themes/${commonsGlobalThemeCode}/macros/${group}/${name}.ftl' >
<#assign macroTheme = .get_optional_template( macroPath )>
<#if macroTheme.exists><@macroTheme.include /><#else><#include "${commonsMacrosPath}${group}/${name}.ftl" /></#if>
</#macro>
<#-- THEME SPEC                            -->
<#include "${commonsGlobalThemeCode}/_theme.ftl" />
<#-- MACROS LIST                            -->
<#include "theme_commons_macros.ftl" />
<#-- BANNER MANAGEMENT        -->
<#assign hasBanner = ( !dskey('portal.theme.site_property.banner.shown.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.banner.shown.checkbox') =='1' )?then( 'true', 'false' ) />
<#assign urlDefaultBannerImage = ( dskey('portal.theme.site_property.banner') )?string />
<#assign isBannerOnlyHome = ( !dskey('portal.theme.site_property.banner.onlyhome.checkbox')?starts_with('DS') && dskey('portal.theme.site_property.banner.onlyhome.checkbox') =='1' )?then( 'true', 'false' ) />
<#assign isBannerFixed = ( !dskey('portal.theme.site_property.banner.fixed.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.banner.fixed.checkbox') =='1' )?then( 'true', 'false' ) />
<#assign hasBannerInternalStyle = ( !dskey('portal.theme.site_property.banner.internal.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.banner.internal.checkbox') =='1' )?then( 'true', 'false' ) />
<#-- END BANNER MANAGEMENT    -->
<#-- MENU MANAGEMENT          -->
<#assign isRtl = ( !dskey('portal.theme.site_property.layout.dir.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.layout.dir.checkbox') =='1' )?then( 'true', 'false' ) />
<#assign hasUserThemeSwitch = ( !dskey('portal.theme.site_property.menu.user.themes.switch.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.user.themes.switch.checkbox') =='1' )?then( 'true', 'false' ) />
<#assign hasUserThemeDensity = ( !dskey('portal.theme.site_property.menu.user.themes.density.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.user.themes.density.checkbox') =='1' )?then( 'true', 'false' ) />
<#assign hasUserThemeColors = ( !dskey('portal.theme.site_property.menu.user.themes.colors.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.user.themes.colors.checkbox') =='1' )?then( 'true', 'false' ) />
<#assign isDark = ( dskey('portal.theme.site_property.layout.theme.checkbox') == '1' )?then( 'true', 'false' ) />
<#assign skipLinkMenu = ( !dskey('portal.theme.site_property.menu.skipLinkMenu.checkbox')?starts_with('DS') && dskey('portal.theme.site_property.menu.skipLinkMenu.checkbox') == '1' )?then( 'true', 'false' ) />
<#assign skipLinkMainId = ( dskey('portal.theme.site_property.menu.skipLinkMainId') )?string />
<#assign hasDefaultMenu = ( !dskey('portal.theme.site_property.menu.hasDefaultMenu.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.hasDefaultMenu.checkbox') == '1' )?then( 'true', 'false' ) />
<#assign hasSearchMenu = ( !dskey('portal.theme.site_property.menu.search.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.search.checkbox') == '1' )?then( 'true', 'false' ) />
<#assign hasTranslateMenu = ( !dskey('portal.theme.site_property.menu.translate.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.translate.checkbox') == '1' )?then( 'true', 'false' ) />
<#assign isFixedMenu = ( dskey('portal.theme.site_property.menu.fixedMenu.checkbox') == '1' )?then( 'true', 'false' ) />
<#assign isMainSidebarMenu = ( dskey('portal.theme.site_property.menu.sidebarMenu.checkbox') == '1' )?then( 'true', 'false' ) />
<#assign isMainSidebarMenuCollapse = ( dskey('portal.theme.site_property.menu.sidebarMenuCollapse.checkbox') == '1' )?then( 'true', 'false' ) />
<#assign urlDefaultSearch = ( dskey('portal.theme.site_property.Url.search')!'' )?string />
<#assign mainNavClass='' />
<#-- MENU MANAGEMENT          -->
<#-- LAYOUT MANAGEMENT        -->
<#assign isLayoutFluid = ( dskey('portal.theme.site_property.layout.type.checkbox') == '1' )?then( 'true', 'false' ) />
<#-- END LAYOUT MANAGEMENT    -->
<#-- UTILS MANAGEMENT         -->
<#assign addGoToTop = ( dskey('portal.theme.site_property.menu.gototop.checkbox') == '1' )?then( 'true', 'false' ) />
<#assign isTargetDefaultIconShown = ( dskey('portal.theme.site_property.link.showTargetIcon.checkbox') == '1' )?then( 'true', 'false' ) />
<#-- END UTILS MANAGEMENT     -->