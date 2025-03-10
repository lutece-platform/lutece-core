<#-- Services List for @cConsentTac https://tarteaucitron.io/fr/install/                                                -->
<#-- Refer to doc for correct use with embed for services. Most of them need to upldate your js/html source code :      -->
<#-- Most used: monparis, twitter, addThis, dailymotion, facebook, googlefonts, slideshare, vimeo,  youtube             -->
<#-- MACRO cConsentTacService :            -->
<#-- Attributes                            -->
<#-- code: 'monparis'                      -->
<#macro cConsentTacService code='monparis'  deprecated...>
<@deprecatedWarning args=deprecated />
<#nested>
(tarteaucitron.job = tarteaucitron.job ||   []).push('${code}');
</#macro>