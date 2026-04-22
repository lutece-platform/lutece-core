<#--
Macro: cPasswordCheck

Description: Generates a password requirements checklist (character length, uppercase, digit) displayed as help text below a password field.

Parameters:
- id (string, required): the ID of the associated password input field.

Showcase:
- desc: "Validation mot de passe - @cPasswordCheck"
- newFeature: false

Snippet:

    Password check below a password input:

    <@cInputPassword name='new_password' id='new_password' />
    <@cPasswordCheck id='new_password' />

-->
<#macro cPasswordCheck id >
<#local checkMsg><@cInline class='charlength'><@cIcon name='check' /> #i18n{portal.theme.labelNbChars}</@cInline> <@cInline class='uppercase'><@cIcon name='check' /> #i18n{portal.theme.labelNbUppercase} </@cInline> <@cInline class='digit'><@cIcon name='check' /> #i18n{portal.theme.labelNbDigit}</@cInline></#local>
<@cFormHelp id checkMsg />
</#macro>