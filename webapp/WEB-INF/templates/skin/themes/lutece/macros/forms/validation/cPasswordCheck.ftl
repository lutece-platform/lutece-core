<#--
Macro: cPasswordCheck

Description: Generates a password requirements checklist (character length, uppercase, digit) displayed as help text below a password field.

Parameters:
- id (string, required): the ID of the associated password input field.

Snippet:

    Password check below a password input:

    <@cInputPassword name='new_password' id='new_password' />
    <@cPasswordCheck id='new_password' />

-->
<#macro cPasswordCheck id >
<#local checkMsg><@cInline class='charlength'><@parisIcon name='check' /> #i18n{portal.theme.labelNbChars}</@cInline> <@cInline class='uppercase'><@parisIcon name='check' /> #i18n{portal.theme.labelNbUppercase} </@cInline> <@cInline class='digit'><@parisIcon name='check' /> #i18n{portal.theme.labelNbDigit}</@cInline></#local>
<@cFormHelp id checkMsg />
</#macro>