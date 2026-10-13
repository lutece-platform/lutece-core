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
<link href="${commonsSiteSharedPath}${commonsSiteCssPath}tabler-icons-filled.min.css?version=${commonsGlobalThemeVersion}" crossorigin="anonymous" rel="stylesheet">
<link href="${commonsSiteSharedPath}${commonsSiteCssPath}tabler-icons.min.css?version=${commonsGlobalThemeVersion}" crossorigin="anonymous" rel="stylesheet">
<!-- Site CSS include                   -->
<link href="${commonsSiteCssPath}site.css?theme=${commonsGlobalThemeCode!}${commonsGlobalThemeVersion}" crossorigin="anonymous" rel="stylesheet">
</#macro>
<#macro themeJSLinks>
<!-- Shared JS                          -->
<script src="${commonsSiteThemePath}${commonsSiteJsPath}vendor/bootstrap.bundle.min.js?version=${commonsGlobalThemeVersion}"></script>
<script src="${commonsSharedThemePath}${commonsSiteJsPath}shared.js?version=${commonsGlobalThemeVersion}"></script>
<script src="${commonsSiteThemePath}${commonsSiteJsPath}theme.min.js?version=${commonsGlobalThemeVersion}"></script>
<script src="${commonsSiteThemePath}${commonsSiteJsPath}theme-utils.min.js?version=${commonsGlobalThemeVersion}"></script>
<!-- Site CSS include                   -->
<script src="${commonsSiteJsPath}site.js?theme=${commonsGlobalThemeCode!}${commonsGlobalThemeVersion}"></script>
</#macro>
<#-- MAIN VARS MANAGEMENT               -->
<#assign mainSite = 'Lutece' />
<#assign iconDefaultPrefixSelector = '.ti' />
<#-- LINKS MANAGEMENT                   -->
<#assign hasSiteMap = ( !dskey('portal.theme.site_property.menu.siteMapMenu.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.siteMapMenu.checkbox') =='1' )?then( 'true', 'false' ) />
<#assign urlMainSite>https://${mainSite?lower_case}.paris.fr</#assign>
<#-- ---------------------------------- -->
<#-- DO NOT REMOVE - END                -->
<#-- ---------------------------------- -->
<#-- Theme Specific VARS                -->
<#-- ---------------------------------- -->
<#assign logoHeaderDS = dskey('portal.theme.site_property.menu.logo')!''>
<#assign logoHeader><#if logoHeaderDS?has_content && !logoHeaderDS?starts_with('DS Value')>${logoHeaderDS}<#else>${commonsSiteThemePath}images/logo-footer.png</#if></#assign>
<#assign logoFooter>${dskey('portal.theme.site_property.layout.footer.logoFooter')!'${commonsSiteThemePath}images/logo.png'}</#assign>
<#assign hasSearchMenu = ( !dskey('portal.theme.site_property.menu.search.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.search.checkbox') =='1' )?then( 'true', 'false' ) />
<#assign footerLinkContact><#if dskey('portal.theme.site_property.Url.contactURL') !=''>${dskey('portal.theme.site_property.Url.contactURL')!'${urlMainSite}/contact'}</#if></#assign>
<#assign footerLinkContactLabel><#if dskey('portal.theme.site_property.Url.contactURLLabel') !=''>${dskey('portal.theme.site_property.Url.contactURLLabel')!'${urlMainSite}/contact'}</#if></#assign>
<#assign footerLinkLegal><#if dskey('portal.theme.site_property.Url.legalURL') !=''>${dskey('portal.theme.site_property.Url.legalURL')!'${urlMainSite}/mentionslegales'}</#if></#assign>
<#assign footerLinkCgu><#if dskey('portal.theme.site_property.Url.cguURL') !=''>${dskey('portal.theme.site_property.Url.cguURL')!'${urlMainSite}/mentionslegales'}</#if></#assign>
<#assign footerLinkAccessibility>${dskey('portal.theme.site_property.Url.accessibilityURL')!'${urlMainSite}/accessibilite'}</#assign>
<#assign footerLinkCookies>${dskey('portal.theme.site_property.Url.cookieURL')!'${urlMainSite}/cookies'}</#assign>
<#assign footerSocialTitleTw = 'X' />        
<#assign footerSocialLinkTw = 'https://x.com/lutecenews' />        
<#assign footerSocialTitleLi = 'Github' />
<#assign footerSocialTitleGithub = 'Github' />       
<#assign footerSocialLinkGithub = 'https://github.com/lutece-platform/' />  
<#assign footerLinkWiki = ( dskey('portal.theme.site_property.Url.wikiURL') !='' )?then( ( dskey('portal.theme.site_property.Url.wikiURL')!'' )?string, '' ) />
<#assign footerSocialTitleFb = 'Facebook' />       
<#assign footerSocialLinkFb = 'https://www.facebook.com/paris' />       
<#assign footerSocialTitleBs = 'Bluesky' />        
<#assign footerSocialLinkBs = 'https://bsky.app/profile/paris.fr' />        
<#assign footerSocialTitleIn = 'Instagram' />        
<#assign footerSocialLinkIn = 'https://www.instagram.com/paris_maville/' />        
<#assign footerSocialTitleLi = 'LinkedIn' />
<#assign footerSocialLinkLi = 'https://www.linkedin.com/company/villedeparis/' />
<#-- END MAIN VARS MANAGEMENT           -->
<#-- XSS MANAGEMENT                     -->   
<#assign xssChars = ( dskey('portal.theme.site_property.xss.xssChars') )?string />
<#assign xssMessage = ( dskey('portal.theme.site_property.xss.xssMsg') )?string />
<#-- AUTH MANAGEMENT                    -->
<#assign urlAccount = ( dskey('portal.theme.site_property.Url.account') )?string />
<#assign urlAuth = ( dskey('portal.theme.site_property.Url.auth') )?string />
<#-- END AUTH MANAGEMENT                -->
<#-- FORM VALIDATION MESSAGES           -->
<#assign formValidationMsgRequired = ( dskey('portal.theme.site_property.formvalidation.msg.required') )?string />
<#assign formValidationMsgEmail = ( dskey('portal.theme.site_property.formvalidation.msg.email') )?string />
<#assign formValidationMsgUrl = ( dskey('portal.theme.site_property.formvalidation.msg.url') )?string />
<#assign formValidationMsgNumber = ( dskey('portal.theme.site_property.formvalidation.msg.number') )?string />
<#assign formValidationMsgMin = ( dskey('portal.theme.site_property.formvalidation.msg.min') )?string />
<#assign formValidationMsgMax = ( dskey('portal.theme.site_property.formvalidation.msg.max') )?string />
<#assign formValidationMsgMinlength = ( dskey('portal.theme.site_property.formvalidation.msg.minlength') )?string />
<#assign formValidationMsgMaxlength = ( dskey('portal.theme.site_property.formvalidation.msg.maxlength') )?string />
<#assign formValidationMsgPattern = ( dskey('portal.theme.site_property.formvalidation.msg.pattern') )?string />
<#assign formValidationMsgStep = ( dskey('portal.theme.site_property.formvalidation.msg.step') )?string />
<#assign formValidationMsgTel = ( dskey('portal.theme.site_property.formvalidation.msg.tel') )?string />
<#assign formValidationMsgDate = ( dskey('portal.theme.site_property.formvalidation.msg.date') )?string />
<#assign formValidationMsgTime = ( dskey('portal.theme.site_property.formvalidation.msg.time') )?string />
<#assign formValidationMsgFile = ( dskey('portal.theme.site_property.formvalidation.msg.file') )?string />
<#assign formValidationMsgFiletype = ( dskey('portal.theme.site_property.formvalidation.msg.filetype') )?string />
<#assign formValidationMsgFilesize = ( dskey('portal.theme.site_property.formvalidation.msg.filesize') )?string />
<#assign formValidationMsgMismatch = ( dskey('portal.theme.site_property.formvalidation.msg.mismatch') )?string />
<#assign formValidationMsgCustom = ( dskey('portal.theme.site_property.formvalidation.msg.custom') )?string />
<#assign formValidationErrorClass = ( dskey('portal.theme.site_property.formvalidation.errorClass') )?string />
<#assign formValidationValidClass = ( dskey('portal.theme.site_property.formvalidation.validClass') )?string />
<#assign formValidationErrorFeedbackClass = ( dskey('portal.theme.site_property.formvalidation.errorFeedbackClass') )?string />
<#assign formValidationHelpClass = ( dskey('portal.theme.site_property.formvalidation.helpClass') )?string />
<#assign formValidationErrorIconSvg = ( dskey('portal.theme.site_property.formvalidation.errorIconSvg') )?string />
<#-- END FORM VALIDATION MESSAGES       -->
<#-- ---------------------------------- -->
<#-- Theme Specific Macros              -->
<#-- Sample : <#include "macros/components/feature/feature.ftl" /> -->
<#-- ---------------------------------- -->