<#--
Macro: cFormHelp

Description: Generates a help text message associated with a form field, rendered as a form-text element linked via aria-describedby.

Parameters:
- id (string, required): the ID of the related field, used to generate the help element ID (help_{id}).
- label (string, required): the help message content.
- class (string, optional): adds a CSS class to the help text. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.

Showcase:
- desc: "Aide de formulaire - @cFormHelp"
- newFeature: false

Snippet:

    Help text for an email field:

    <@cFormHelp id='email' label='We will never share your email with anyone.' />
    <@cInput name='email' id='email' type='email' />

    Help text with custom class:

    <@cFormHelp id='password' label='Must contain at least 8 characters.' class='text-info' />

-->
<#macro cFormHelp id label class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@cText id='help_${id!}' class='form-text ${class!}' params=params>${label!}</@cText>
</#macro>