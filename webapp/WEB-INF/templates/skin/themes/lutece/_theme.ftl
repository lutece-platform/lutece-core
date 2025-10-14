<#ftl strip_whitespace=true strip_text=true />
<#-- WARNING : be careful to white-space and lines break in FreeMarker macros.
 # This macro template can be used to output white-space-sensitive formats (like RSS files).
 # See http://dev.lutece.paris.fr/jira/browse/LUTECE-765
-->
<#-- ---------------------------------- -->
<#-- THEME LINKS AND LABELS VARIABLES   -->
<#-- ---------------------------------- -->
<#-- DO NOT REMOVE                      -->
<#-- ---------------------------------- -->
<#-- Theme Infos                        -->
<#macro commonsName>Thème LUTECE</#macro>
<#macro commonsDescription>Default theme for FO theme </#macro>
<#macro commonsPreview>${commonsSiteThemePath}/images/preview.png</#macro>
<#-- THEME LINKS AND LABELS VARIABLES                                               -->
<#macro themeCSSLinks>
<!-- Theme CSS include                  -->
<link href="${commonsSiteThemePath}${commonsSiteCssPath}theme<#if isRtl?boolean>.rtl</#if>.min.css?version=${commonsGlobalThemeVersion}" crossorigin="anonymous" rel="stylesheet">
<link href="${commonsSharedThemePath}${commonsSiteCssPath}shared.css?version=${commonsGlobalThemeVersion}" crossorigin="anonymous" rel="stylesheet">
<script src="${commonsSiteSharedPath}${commonsSiteCssPath}tabler-icons.min.css?version=${commonsGlobalThemeVersion}"></script>
<!-- Site CSS include                  -->
<link href="${commonsSiteCssPath}site.css?theme=${commonsGlobalThemeCode!}${commonsGlobalThemeVersion}" crossorigin="anonymous" rel="stylesheet">
</#macro>
<#macro themeJSLinks>
<!-- Shared JS                          -->
<script src="${commonsSiteThemePath}${commonsSiteJsPath}vendor/bootstrap.bundle.min.js?version=${commonsGlobalThemeVersion}"></script>
<script src="${commonsSharedThemePath}${commonsSiteJsPath}shared.js?version=${commonsGlobalThemeVersion}"></script>
<script type="module" src="${commonsSiteThemePath}${commonsSiteJsPath}theme.min.js?version=${commonsGlobalThemeVersion}"></script>
<!-- Site CSS include                  -->
<script src="${commonsSiteJsPath}site.js?theme=${commonsGlobalThemeCode!}${commonsGlobalThemeVersion}"></script>
</#macro>
<#-- MAIN VARS MANAGEMENT               -->
<#assign mainSite>Lutece</#assign>
<#-- LINKS MANAGEMENT                   -->
<#assign hasSiteMap><#if !dskey('theme.site_property.menu.siteMapMenu.checkbox')?starts_with('DS') &&  dskey('theme.site_property.menu.siteMapMenu.checkbox') =='1'>true<#else>false</#if></#assign>
<#assign urlMainSite>https://${mainSite?lower_case}.paris.fr</#assign>
<#-- ---------------------------------- -->
<#-- DO NOT REMOVE - END                -->
<#-- ---------------------------------- -->
<#-- Theme Specific VARS                -->
<#-- ---------------------------------- -->
<#assign logoHeader>${commonsSiteThemePath}images/logo.png</#assign>
<#assign logoFooter>${commonsSiteThemePath}images/logo.png</#assign>
<#assign footerLinkContact><#if dskey('theme.site_property.Url.contactURL') !=''>${dskey('theme.site_property.Url.contactURL')!'${urlMainSite}/contact'}</#if></#assign>
<#assign footerLinkContactLabel><#if dskey('theme.site_property.Url.contactURLLabel') !=''>${dskey('theme.site_property.Url.contactURLLabel')!'${urlMainSite}/contact'}</#if></#assign>
<#assign footerLinkLegal><#if dskey('theme.site_property.Url.legalURL') !=''>${dskey('theme.site_property.Url.legalURL')!'${urlMainSite}/mentionslegales'}</#if></#assign>
<#assign footerLinkCgu><#if dskey('theme.site_property.Url.cguURL') !=''>${dskey('theme.site_property.Url.cguURL')!'${urlMainSite}/mentionslegales'}</#if></#assign>
<#assign footerLinkAccessibility>${dskey('theme.site_property.Url.accessibilityURL')!'${urlMainSite}/accessibilite'}</#assign>
<#assign footerLinkCookies>${dskey('theme.site_property.Url.cookieURL')!'${urlMainSite}/cookies'}</#assign>
<#assign footerSocialTitleTw>X</#assign>        
<#assign footerSocialLinkTw>https://x.com/lutecenews</#assign>        
<#assign footerSocialTitleLi>Github</#assign>
<#assign footerSocialTitleGithub>Github</#assign>       
<#assign footerSocialLinkGithub>https://github.com/lutece-platform/</#assign>  
<#assign footerLinkWiki><#if dskey('theme.site_property.Url.wikiURL') !=''>${dskey('theme.site_property.Url.wikiURL')!''}</#if></#assign>
<#-- END MAIN VARS MANAGEMENT           -->
<#-- XSS MANAGEMENT                     -->   
<#assign xssChars>${dskey('theme.site_property.xss.xssChars')}</#assign>
<#assign xssMessage>${dskey('theme.site_property.xss.xssMsg')}</#assign>
<#-- AUTH MANAGEMENT                    -->
<#assign urlAccount>${dskey('theme.site_property.Url.account')}</#assign>
<#assign urlAuth>${dskey('theme.site_property.Url.auth')}</#assign>
<#-- END AUTH MANAGEMENT                -->
<#-- ---------------------------------- -->
<#-- Theme Specific Macros              -->
<#-- Sample : <#include "macros/components/feature/feature.ftl" /> -->
<#-- ---------------------------------- -->