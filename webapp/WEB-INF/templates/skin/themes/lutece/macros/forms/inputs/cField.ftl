<#--
Macro: cField

Description: Generates a form field wrapper with a label, combining a label and nested input elements within a block container.

Parameters:
- label (string, optional): the label text for the field. Default: ''.
- labelClass (string, optional): adds a CSS class to the label. Default: ''.
- labelData (string, optional): additional text appended to the label. Default: ''.
- for (string, optional): links the label to the input via the for attribute. Default: ''.
- showLabel (boolean, optional): displays or hides the label. Default: true.
- required (boolean, optional): marks the field as required with an asterisk. Default: false.
- class (string, optional): adds a CSS class to the field container. Default: 'mb-3'.
- id (string, optional): the ID of the field container. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.

Showcase:
- desc: "Champ de formulaire - @cField"
- newFeature: false

Snippet:

    Basic text field with label:

    <@cField label='Last name' for='lastname' required=true>
        <@cInput name='lastname' id='lastname' />
    </@cField>

    Field with help text appended to label:

    <@cField label='Email' labelData='(professional)' for='email'>
        <@cInput name='email' id='email' type='email' placeholder='you@example.com' />
    </@cField>

    Field with hidden label:

    <@cField label='Search' for='search' showLabel=false>
        <@cInput name='search' id='search' placeholder='Search...' />
    </@cField>

-->
<#macro cField label='' labelClass='' labelData='' for='' showLabel=true required=false class='mb-3' id='' params='' >
<@cBlock class='${class}' id=id params=params>
<#if label!=''>
<#assign fieldLabel>${label!} ${labelData!}</#assign>
<@cLabel label=fieldLabel class=labelClass for=for showLabel=showLabel required=required />
</#if>
<#assign propagateRequired = required>
<#nested>
<#assign propagateRequired = false>
</@cBlock>
</#macro>