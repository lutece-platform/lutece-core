<#--
Macro: cConsentTacService

Description: Generates a TarteAuCitron service registration entry to declare a third-party service for cookie consent management.

Parameters:
- code (string, optional): The TarteAuCitron service code (e.g., 'youtube', 'matomo', 'twitter', 'dailymotion', 'googlefonts'). Default: 'monparis'.

Snippet:

    Register a YouTube service:

    <@cConsentTacService code='youtube' />

    Register multiple services inside cConsentTac:

    <@cConsentTac privacyLink='jsp/site/Portal.jsp?page=privacy'>
        <@cConsentTacService code='youtube' />
        <@cConsentTacService code='twitter' />
    </@cConsentTac>

-->
<#macro cConsentTacService code='monparis'  deprecated...>
<@deprecatedWarning args=deprecated />
<#nested>
(tarteaucitron.job = tarteaucitron.job ||   []).push('${code}');
</#macro>