<#-- Services List for @cConsentTac https://tarteaucitron.io/fr/install/                                                -->
<#-- Refer to doc for correct use with embed for services. Most of them need to upldate your js/html source code :      -->
<#-- Most used: monparis, twitter, addThis, dailymotion, facebook, googlefonts, slideshare, vimeo,  youtube             -->
<#-- MACRO cConsentTacService :            -->
<#-- Attributes                            -->
<#-- code: 'monparis'                      -->
<#macro cConsentOrejimeService id='' title='' description='' required=true exempt=false deprecated...>
<@deprecatedWarning args=deprecated />
<#if code !=''>
<#nested>
{
    id: '${id}',
    title: '${title}',
    description: "${description}",
    isMandatory: ${required?c},
    isExempt: ${exempt?c},
},
</#if>
</#macro>