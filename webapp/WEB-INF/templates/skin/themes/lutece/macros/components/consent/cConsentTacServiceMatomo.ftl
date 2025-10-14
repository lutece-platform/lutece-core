<#-- MACRO cConsentTacServiceMatomo :                                               -->
<#-- Attributes                                                                     -->
<#-- id: 0                                                                          -->
<#-- host: https://yoururl/matomo/                                                  -->
<#-- matomo: 0=use nested to get Matomo params  / 1  use id and host macro params   -->
<#macro cConsentTacServiceMatomo id=0 host=''>
<@cConsentTacService code='matomo'>
tarteaucitron.user.matomoId = ${id};
tarteaucitron.user.matomoHost = '${host}';
</@cConsentTacService>
</#macro>