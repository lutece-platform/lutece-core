<#--
Macro: fcMessage

Description: Generates a FranceConnect informational message with a link to the FranceConnect portal and nested content.

Parameters:
- label (string, optional): Label text for the FranceConnect link. Default: '#i18n{portal.theme.labelFranceConnect}'.
- url (string, optional): URL of the FranceConnect link. Default: 'https://franceconnect.gouv.fr/'.
- class (string, optional): CSS class for the link element. Default: ''.
- pclass (string, optional): CSS class for the paragraph wrapper. Default: ''.
- brlink (boolean, optional): Whether to wrap nested content in a block-level span for line break. Default: true.

Snippet:

    Basic usage with nested text:

    <@fcMessage>
        Qu'est-ce que FranceConnect ?
    </@fcMessage>

    Custom label and CSS class:

    <@fcMessage label='En savoir plus sur FranceConnect' class='fw-bold' pclass='mt-3'>
        Un dispositif qui simplifie la connexion aux services en ligne.
    </@fcMessage>

-->
<#macro fcMessage label='#i18n{portal.theme.labelFranceConnect}' url='https://franceconnect.gouv.fr/' class='' pclass='' brlink=true deprecated...>
<@cText class=pclass>
<#if brlink><span class="d-block"></#if>
<#nested>
<#if brlink></span></#if>
<@cLink target='_blank' href=url label=label class=class showTarget=true params='rel="noopener"' />
</@cText>
</#macro>