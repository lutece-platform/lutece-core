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
<#-- Configuration du theme : des valeurs de texte, pas du balisage. Le bloc plainText garantit
     que chaque capture produit une String dans les deux modes d'autoescape, sinon les builtins
     de chaine (?boolean, ?lower_case, ...) et les chemins de template les refusent. -->
<#outputformat "plainText">
<#assign mainSite>Lutece</#assign>
<#assign mainSite = mainSite?is_markup_output?then(mainSite?markup_string, mainSite) />
<#assign iconDefaultPrefixSelector>.ti</#assign>
<#assign iconDefaultPrefixSelector = iconDefaultPrefixSelector?is_markup_output?then(iconDefaultPrefixSelector?markup_string, iconDefaultPrefixSelector) />
<#-- LINKS MANAGEMENT                   -->
<#assign hasSiteMap><#if !dskey('portal.theme.site_property.menu.siteMapMenu.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.siteMapMenu.checkbox') =='1'>true<#else>false</#if></#assign>
<#assign hasSiteMap = hasSiteMap?is_markup_output?then(hasSiteMap?markup_string, hasSiteMap) />
<#assign urlMainSite>https://${mainSite?lower_case}.paris.fr</#assign>
<#assign urlMainSite = urlMainSite?is_markup_output?then(urlMainSite?markup_string, urlMainSite) />
<#-- ---------------------------------- -->
<#-- DO NOT REMOVE - END                -->
<#-- ---------------------------------- -->
<#-- Theme Specific VARS                -->
<#-- ---------------------------------- -->
<#assign logoHeaderDS = dskey('portal.theme.site_property.menu.logo')!''>
<#assign logoHeader><#if logoHeaderDS?has_content && !logoHeaderDS?starts_with('DS Value')>${logoHeaderDS}<#else>${commonsSiteThemePath}images/logo.png</#if></#assign>
<#assign logoHeader = logoHeader?is_markup_output?then(logoHeader?markup_string, logoHeader) />
<#assign logoFooter>${dskey('portal.theme.site_property.layout.footer.logoFooter')!'${commonsSiteThemePath}images/logo.png'}</#assign>
<#assign logoFooter = logoFooter?is_markup_output?then(logoFooter?markup_string, logoFooter) />
<#assign hasSearchMenu><#if !dskey('portal.theme.site_property.menu.search.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.search.checkbox') =='1'>true<#else>false</#if></#assign>
<#assign hasSearchMenu = hasSearchMenu?is_markup_output?then(hasSearchMenu?markup_string, hasSearchMenu) />
<#assign footerLinkContact><#if dskey('portal.theme.site_property.Url.contactURL') !=''>${dskey('portal.theme.site_property.Url.contactURL')!'${urlMainSite}/contact'}</#if></#assign>
<#assign footerLinkContact = footerLinkContact?is_markup_output?then(footerLinkContact?markup_string, footerLinkContact) />
<#assign footerLinkContactLabel><#if dskey('portal.theme.site_property.Url.contactURLLabel') !=''>${dskey('portal.theme.site_property.Url.contactURLLabel')!'${urlMainSite}/contact'}</#if></#assign>
<#assign footerLinkContactLabel = footerLinkContactLabel?is_markup_output?then(footerLinkContactLabel?markup_string, footerLinkContactLabel) />
<#assign footerLinkLegal><#if dskey('portal.theme.site_property.Url.legalURL') !=''>${dskey('portal.theme.site_property.Url.legalURL')!'${urlMainSite}/mentionslegales'}</#if></#assign>
<#assign footerLinkLegal = footerLinkLegal?is_markup_output?then(footerLinkLegal?markup_string, footerLinkLegal) />
<#assign footerLinkCgu><#if dskey('portal.theme.site_property.Url.cguURL') !=''>${dskey('portal.theme.site_property.Url.cguURL')!'${urlMainSite}/mentionslegales'}</#if></#assign>
<#assign footerLinkCgu = footerLinkCgu?is_markup_output?then(footerLinkCgu?markup_string, footerLinkCgu) />
<#assign footerLinkAccessibility>${dskey('portal.theme.site_property.Url.accessibilityURL')!'${urlMainSite}/accessibilite'}</#assign>
<#assign footerLinkAccessibility = footerLinkAccessibility?is_markup_output?then(footerLinkAccessibility?markup_string, footerLinkAccessibility) />
<#assign footerLinkCookies>${dskey('portal.theme.site_property.Url.cookieURL')!'${urlMainSite}/cookies'}</#assign>
<#assign footerLinkCookies = footerLinkCookies?is_markup_output?then(footerLinkCookies?markup_string, footerLinkCookies) />
<#assign footerSocialTitleTw>X</#assign>
<#assign footerSocialTitleTw = footerSocialTitleTw?is_markup_output?then(footerSocialTitleTw?markup_string, footerSocialTitleTw) />        
<#assign footerSocialLinkTw>https://x.com/lutecenews</#assign>
<#assign footerSocialLinkTw = footerSocialLinkTw?is_markup_output?then(footerSocialLinkTw?markup_string, footerSocialLinkTw) />        
<#assign footerSocialTitleLi>Github</#assign>
<#assign footerSocialTitleLi = footerSocialTitleLi?is_markup_output?then(footerSocialTitleLi?markup_string, footerSocialTitleLi) />
<#assign footerSocialTitleGithub>Github</#assign>
<#assign footerSocialTitleGithub = footerSocialTitleGithub?is_markup_output?then(footerSocialTitleGithub?markup_string, footerSocialTitleGithub) />       
<#assign footerSocialLinkGithub>https://github.com/lutece-platform/</#assign>
<#assign footerSocialLinkGithub = footerSocialLinkGithub?is_markup_output?then(footerSocialLinkGithub?markup_string, footerSocialLinkGithub) />  
<#assign footerLinkWiki><#if dskey('portal.theme.site_property.Url.wikiURL') !=''>${dskey('portal.theme.site_property.Url.wikiURL')!''}</#if></#assign>
<#assign footerLinkWiki = footerLinkWiki?is_markup_output?then(footerLinkWiki?markup_string, footerLinkWiki) />
<#assign footerSocialTitleFb>Facebook</#assign>
<#assign footerSocialTitleFb = footerSocialTitleFb?is_markup_output?then(footerSocialTitleFb?markup_string, footerSocialTitleFb) />       
<#assign footerSocialLinkFb>https://www.facebook.com/paris</#assign>
<#assign footerSocialLinkFb = footerSocialLinkFb?is_markup_output?then(footerSocialLinkFb?markup_string, footerSocialLinkFb) />       
<#assign footerSocialTitleBs>Bluesky</#assign>
<#assign footerSocialTitleBs = footerSocialTitleBs?is_markup_output?then(footerSocialTitleBs?markup_string, footerSocialTitleBs) />        
<#assign footerSocialLinkBs>https://bsky.app/profile/paris.fr</#assign>
<#assign footerSocialLinkBs = footerSocialLinkBs?is_markup_output?then(footerSocialLinkBs?markup_string, footerSocialLinkBs) />        
<#assign footerSocialTitleIn>Instagram</#assign>
<#assign footerSocialTitleIn = footerSocialTitleIn?is_markup_output?then(footerSocialTitleIn?markup_string, footerSocialTitleIn) />        
<#assign footerSocialLinkIn>https://www.instagram.com/paris_maville/</#assign>
<#assign footerSocialLinkIn = footerSocialLinkIn?is_markup_output?then(footerSocialLinkIn?markup_string, footerSocialLinkIn) />        
<#assign footerSocialTitleLi>LinkedIn</#assign>
<#assign footerSocialTitleLi = footerSocialTitleLi?is_markup_output?then(footerSocialTitleLi?markup_string, footerSocialTitleLi) />
<#assign footerSocialLinkLi>https://www.linkedin.com/company/villedeparis/</#assign>
<#assign footerSocialLinkLi = footerSocialLinkLi?is_markup_output?then(footerSocialLinkLi?markup_string, footerSocialLinkLi) />
<#-- END MAIN VARS MANAGEMENT           -->
<#-- XSS MANAGEMENT                     -->   
<#assign xssChars>${dskey('portal.theme.site_property.xss.xssChars')}</#assign>
<#assign xssChars = xssChars?is_markup_output?then(xssChars?markup_string, xssChars) />
<#assign xssMessage>${dskey('portal.theme.site_property.xss.xssMsg')}</#assign>
<#assign xssMessage = xssMessage?is_markup_output?then(xssMessage?markup_string, xssMessage) />
<#-- AUTH MANAGEMENT                    -->
<#assign urlAccount>${dskey('portal.theme.site_property.Url.account')}</#assign>
<#assign urlAccount = urlAccount?is_markup_output?then(urlAccount?markup_string, urlAccount) />
<#assign urlAuth>${dskey('portal.theme.site_property.Url.auth')}</#assign>
<#assign urlAuth = urlAuth?is_markup_output?then(urlAuth?markup_string, urlAuth) />
<#-- END AUTH MANAGEMENT                -->
<#-- FORM VALIDATION MESSAGES           -->
<#assign formValidationMsgRequired>${dskey('portal.theme.site_property.formvalidation.msg.required')}</#assign>
<#assign formValidationMsgRequired = formValidationMsgRequired?is_markup_output?then(formValidationMsgRequired?markup_string, formValidationMsgRequired) />
<#assign formValidationMsgEmail>${dskey('portal.theme.site_property.formvalidation.msg.email')}</#assign>
<#assign formValidationMsgEmail = formValidationMsgEmail?is_markup_output?then(formValidationMsgEmail?markup_string, formValidationMsgEmail) />
<#assign formValidationMsgUrl>${dskey('portal.theme.site_property.formvalidation.msg.url')}</#assign>
<#assign formValidationMsgUrl = formValidationMsgUrl?is_markup_output?then(formValidationMsgUrl?markup_string, formValidationMsgUrl) />
<#assign formValidationMsgNumber>${dskey('portal.theme.site_property.formvalidation.msg.number')}</#assign>
<#assign formValidationMsgNumber = formValidationMsgNumber?is_markup_output?then(formValidationMsgNumber?markup_string, formValidationMsgNumber) />
<#assign formValidationMsgMin>${dskey('portal.theme.site_property.formvalidation.msg.min')}</#assign>
<#assign formValidationMsgMin = formValidationMsgMin?is_markup_output?then(formValidationMsgMin?markup_string, formValidationMsgMin) />
<#assign formValidationMsgMax>${dskey('portal.theme.site_property.formvalidation.msg.max')}</#assign>
<#assign formValidationMsgMax = formValidationMsgMax?is_markup_output?then(formValidationMsgMax?markup_string, formValidationMsgMax) />
<#assign formValidationMsgMinlength>${dskey('portal.theme.site_property.formvalidation.msg.minlength')}</#assign>
<#assign formValidationMsgMinlength = formValidationMsgMinlength?is_markup_output?then(formValidationMsgMinlength?markup_string, formValidationMsgMinlength) />
<#assign formValidationMsgMaxlength>${dskey('portal.theme.site_property.formvalidation.msg.maxlength')}</#assign>
<#assign formValidationMsgMaxlength = formValidationMsgMaxlength?is_markup_output?then(formValidationMsgMaxlength?markup_string, formValidationMsgMaxlength) />
<#assign formValidationMsgPattern>${dskey('portal.theme.site_property.formvalidation.msg.pattern')}</#assign>
<#assign formValidationMsgPattern = formValidationMsgPattern?is_markup_output?then(formValidationMsgPattern?markup_string, formValidationMsgPattern) />
<#assign formValidationMsgStep>${dskey('portal.theme.site_property.formvalidation.msg.step')}</#assign>
<#assign formValidationMsgStep = formValidationMsgStep?is_markup_output?then(formValidationMsgStep?markup_string, formValidationMsgStep) />
<#assign formValidationMsgTel>${dskey('portal.theme.site_property.formvalidation.msg.tel')}</#assign>
<#assign formValidationMsgTel = formValidationMsgTel?is_markup_output?then(formValidationMsgTel?markup_string, formValidationMsgTel) />
<#assign formValidationMsgDate>${dskey('portal.theme.site_property.formvalidation.msg.date')}</#assign>
<#assign formValidationMsgDate = formValidationMsgDate?is_markup_output?then(formValidationMsgDate?markup_string, formValidationMsgDate) />
<#assign formValidationMsgTime>${dskey('portal.theme.site_property.formvalidation.msg.time')}</#assign>
<#assign formValidationMsgTime = formValidationMsgTime?is_markup_output?then(formValidationMsgTime?markup_string, formValidationMsgTime) />
<#assign formValidationMsgFile>${dskey('portal.theme.site_property.formvalidation.msg.file')}</#assign>
<#assign formValidationMsgFile = formValidationMsgFile?is_markup_output?then(formValidationMsgFile?markup_string, formValidationMsgFile) />
<#assign formValidationMsgFiletype>${dskey('portal.theme.site_property.formvalidation.msg.filetype')}</#assign>
<#assign formValidationMsgFiletype = formValidationMsgFiletype?is_markup_output?then(formValidationMsgFiletype?markup_string, formValidationMsgFiletype) />
<#assign formValidationMsgFilesize>${dskey('portal.theme.site_property.formvalidation.msg.filesize')}</#assign>
<#assign formValidationMsgFilesize = formValidationMsgFilesize?is_markup_output?then(formValidationMsgFilesize?markup_string, formValidationMsgFilesize) />
<#assign formValidationMsgMismatch>${dskey('portal.theme.site_property.formvalidation.msg.mismatch')}</#assign>
<#assign formValidationMsgMismatch = formValidationMsgMismatch?is_markup_output?then(formValidationMsgMismatch?markup_string, formValidationMsgMismatch) />
<#assign formValidationMsgCustom>${dskey('portal.theme.site_property.formvalidation.msg.custom')}</#assign>
<#assign formValidationMsgCustom = formValidationMsgCustom?is_markup_output?then(formValidationMsgCustom?markup_string, formValidationMsgCustom) />
<#assign formValidationErrorClass>${dskey('portal.theme.site_property.formvalidation.errorClass')}</#assign>
<#assign formValidationErrorClass = formValidationErrorClass?is_markup_output?then(formValidationErrorClass?markup_string, formValidationErrorClass) />
<#assign formValidationValidClass>${dskey('portal.theme.site_property.formvalidation.validClass')}</#assign>
<#assign formValidationValidClass = formValidationValidClass?is_markup_output?then(formValidationValidClass?markup_string, formValidationValidClass) />
<#assign formValidationErrorFeedbackClass>${dskey('portal.theme.site_property.formvalidation.errorFeedbackClass')}</#assign>
<#assign formValidationErrorFeedbackClass = formValidationErrorFeedbackClass?is_markup_output?then(formValidationErrorFeedbackClass?markup_string, formValidationErrorFeedbackClass) />
<#assign formValidationHelpClass>${dskey('portal.theme.site_property.formvalidation.helpClass')}</#assign>
<#assign formValidationHelpClass = formValidationHelpClass?is_markup_output?then(formValidationHelpClass?markup_string, formValidationHelpClass) />
<#assign formValidationErrorIconSvg>${dskey('portal.theme.site_property.formvalidation.errorIconSvg')}</#assign>
<#assign formValidationErrorIconSvg = formValidationErrorIconSvg?is_markup_output?then(formValidationErrorIconSvg?markup_string, formValidationErrorIconSvg) />
</#outputformat>
<#-- END FORM VALIDATION MESSAGES       -->
<#-- ---------------------------------- -->
<#-- Theme Specific Macros              -->
<#-- Sample : <#include "macros/components/feature/feature.ftl" /> -->
<#-- ---------------------------------- -->