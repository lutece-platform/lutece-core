<#--
Macro: cConsentTacServiceMatomo

Description: Generates a TarteAuCitron Matomo analytics service registration with site ID and host configuration.

Parameters:
- id (number, optional): The Matomo site ID. Default: 0.
- host (string, optional): The Matomo server URL. Default: ''.

showcase:
- desc: Enregistrement de Matomo Analytics pour TarteAuCitron - @cConsentTacServiceMatomo

Snippet:

    Register Matomo analytics inside cConsentTac:

    <@cConsentTac privacyLink='jsp/site/Portal.jsp?page=privacy'>
        <@cConsentTacServiceMatomo id=5 host='https://analytics.example.com/matomo/' />
    </@cConsentTac>

-->
<#macro cConsentTacServiceMatomo id=0 host=''>
<@cConsentTacService code='matomo'>
tarteaucitron.user.matomoId = ${id};
tarteaucitron.user.matomoHost = '${host}';
</@cConsentTacService>
</#macro>