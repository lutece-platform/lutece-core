<#--
Macro: cStepContent

Description: Generates the content area of a step in a multi-step form, with optional iteration support for repeatable sections.

Parameters:
- title (string, required): the title of the step content.
- id (string, optional): the ID of the element. Default: ''.
- class (string, optional): CSS class for the element. Default: ''.
- help (string, optional): help text for the step content. Default: ''.
- iterable (boolean, optional): enables iteration numbering. Default: false.
- iteration (number, optional): the current iteration number. Default: 0.
- labelAddIteration (string, optional): label for the add iteration button. Default: '#i18n{portal.portal.theme.labelAdd}'.
- labelDelIteration (string, optional): label for the delete iteration button. Default: '#i18n{portal.portal.theme.labelDelete}'.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Basic step content:

    <@cStepContent title='Personal information'>
        <@cField label='First name' for='firstname' required=true>
            <@cInput name='firstname' id='firstname' />
        </@cField>
    </@cStepContent>

-->
<#macro cStepContent title id='' class='' help='' iterable=false iteration=0 labelAddIteration='#i18n{portal.portal.theme.labelAdd}' labelDelIteration='#i18n{portal.portal.theme.labelDelete}' params={}></#macro>