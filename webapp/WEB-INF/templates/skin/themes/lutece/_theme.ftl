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
<#assign hasSiteMap><#if !dskey('portal.theme.site_property.menu.siteMapMenu.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.siteMapMenu.checkbox') =='1'>true<#else>false</#if></#assign>
<#assign urlMainSite>https://${mainSite?lower_case}.paris.fr</#assign>
<#-- ---------------------------------- -->
<#-- DO NOT REMOVE - END                -->
<#-- ---------------------------------- -->
<#-- Theme Specific VARS                -->
<#-- ---------------------------------- -->
<#assign logoHeader>${commonsSiteThemePath}images/logo.png</#assign>
<#assign logoFooter>${commonsSiteThemePath}images/logo.png</#assign>
<#assign hasSearchMenu><#if !dskey('portal.theme.site_property.menu.search.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.search.checkbox') =='1'>true<#else>false</#if></#assign>
<#assign footerLinkContact><#if dskey('portal.theme.site_property.Url.contactURL') !=''>${dskey('portal.theme.site_property.Url.contactURL')!'${urlMainSite}/contact'}</#if></#assign>
<#assign footerLinkContactLabel><#if dskey('portal.theme.site_property.Url.contactURLLabel') !=''>${dskey('portal.theme.site_property.Url.contactURLLabel')!'${urlMainSite}/contact'}</#if></#assign>
<#assign footerLinkLegal><#if dskey('portal.theme.site_property.Url.legalURL') !=''>${dskey('portal.theme.site_property.Url.legalURL')!'${urlMainSite}/mentionslegales'}</#if></#assign>
<#assign footerLinkCgu><#if dskey('portal.theme.site_property.Url.cguURL') !=''>${dskey('portal.theme.site_property.Url.cguURL')!'${urlMainSite}/mentionslegales'}</#if></#assign>
<#assign footerLinkAccessibility>${dskey('portal.theme.site_property.Url.accessibilityURL')!'${urlMainSite}/accessibilite'}</#assign>
<#assign footerLinkCookies>${dskey('portal.theme.site_property.Url.cookieURL')!'${urlMainSite}/cookies'}</#assign>
<#assign footerSocialTitleTw>X</#assign>        
<#assign footerSocialLinkTw>https://x.com/lutecenews</#assign>        
<#assign footerSocialTitleLi>Github</#assign>
<#assign footerSocialTitleGithub>Github</#assign>       
<#assign footerSocialLinkGithub>https://github.com/lutece-platform/</#assign>  
<#assign footerLinkWiki><#if dskey('portal.theme.site_property.Url.wikiURL') !=''>${dskey('portal.theme.site_property.Url.wikiURL')!''}</#if></#assign>
<#assign footerSocialTitleFb>Facebook</#assign>       
<#assign footerSocialLinkFb>https://www.facebook.com/paris</#assign>       
<#assign footerSocialTitleBs>Bluesky</#assign>        
<#assign footerSocialLinkBs>https://bsky.app/profile/paris.fr</#assign>        
<#assign footerSocialTitleIn>Instagram</#assign>        
<#assign footerSocialLinkIn>https://www.instagram.com/paris_maville/</#assign>        
<#assign footerSocialTitleLi>LinkedIn</#assign>
<#assign footerSocialLinkLi>https://www.linkedin.com/company/villedeparis/</#assign>
<#-- END MAIN VARS MANAGEMENT           -->
<#-- XSS MANAGEMENT                     -->   
<#assign xssChars>${dskey('portal.theme.site_property.xss.xssChars')}</#assign>
<#assign xssMessage>${dskey('portal.theme.site_property.xss.xssMsg')}</#assign>
<#-- AUTH MANAGEMENT                    -->
<#assign urlAccount>${dskey('portal.theme.site_property.Url.account')}</#assign>
<#assign urlAuth>${dskey('portal.theme.site_property.Url.auth')}</#assign>
<#-- END AUTH MANAGEMENT                -->
<#-- FORM VALIDATION MESSAGES           -->
<#assign formValidationMsgRequired>${dskey('portal.theme.site_property.formvalidation.msg.required')}</#assign>
<#assign formValidationMsgEmail>${dskey('portal.theme.site_property.formvalidation.msg.email')}</#assign>
<#assign formValidationMsgUrl>${dskey('portal.theme.site_property.formvalidation.msg.url')}</#assign>
<#assign formValidationMsgNumber>${dskey('portal.theme.site_property.formvalidation.msg.number')}</#assign>
<#assign formValidationMsgMin>${dskey('portal.theme.site_property.formvalidation.msg.min')}</#assign>
<#assign formValidationMsgMax>${dskey('portal.theme.site_property.formvalidation.msg.max')}</#assign>
<#assign formValidationMsgMinlength>${dskey('portal.theme.site_property.formvalidation.msg.minlength')}</#assign>
<#assign formValidationMsgMaxlength>${dskey('portal.theme.site_property.formvalidation.msg.maxlength')}</#assign>
<#assign formValidationMsgPattern>${dskey('portal.theme.site_property.formvalidation.msg.pattern')}</#assign>
<#assign formValidationMsgStep>${dskey('portal.theme.site_property.formvalidation.msg.step')}</#assign>
<#assign formValidationMsgTel>${dskey('portal.theme.site_property.formvalidation.msg.tel')}</#assign>
<#assign formValidationMsgDate>${dskey('portal.theme.site_property.formvalidation.msg.date')}</#assign>
<#assign formValidationMsgTime>${dskey('portal.theme.site_property.formvalidation.msg.time')}</#assign>
<#assign formValidationMsgFile>${dskey('portal.theme.site_property.formvalidation.msg.file')}</#assign>
<#assign formValidationMsgFiletype>${dskey('portal.theme.site_property.formvalidation.msg.filetype')}</#assign>
<#assign formValidationMsgFilesize>${dskey('portal.theme.site_property.formvalidation.msg.filesize')}</#assign>
<#assign formValidationMsgMismatch>${dskey('portal.theme.site_property.formvalidation.msg.mismatch')}</#assign>
<#assign formValidationMsgCustom>${dskey('portal.theme.site_property.formvalidation.msg.custom')}</#assign>
<#assign formValidationErrorClass>${dskey('portal.theme.site_property.formvalidation.errorClass')}</#assign>
<#assign formValidationValidClass>${dskey('portal.theme.site_property.formvalidation.validClass')}</#assign>
<#assign formValidationErrorFeedbackClass>${dskey('portal.theme.site_property.formvalidation.errorFeedbackClass')}</#assign>
<#assign formValidationHelpClass>${dskey('portal.theme.site_property.formvalidation.helpClass')}</#assign>
<#assign formValidationErrorIconSvg>${dskey('portal.theme.site_property.formvalidation.errorIconSvg')}</#assign>
<#-- END FORM VALIDATION MESSAGES       -->
<#-- ---------------------------------- -->
<#-- Theme Specific Macros              -->
<#-- Sample : <#include "macros/components/feature/feature.ftl" /> -->
<#-- ---------------------------------- -->