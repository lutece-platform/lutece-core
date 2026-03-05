<#--
Macro: cRadio

Description: Generates a radio button input element. Wraps cFormCheck with type 'radio' by default.

Parameters:
- name (string, required): the name of the element.
- label (string, required): the label associated to the input.
- type (string, optional): the type, 'radio' or 'button'. Default: 'radio'.
- class (string, optional): the CSS class of the element. Default: 'form-check'.
- labelClass (string, optional): the CSS class of the label, can be 'visually-hidden' to hide it. Default: ''.
- btnClass (string, optional): CSS class for button label, only used if type is 'button'. Default: ''.
- id (string, optional): the ID of the element. Default: ''.
- value (string, optional): the value of the element. Default: ''.
- errorMsg (string, optional): content of the error message. Default: ''.
- helpMsg (string, optional): content of the help message. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.
- inline (boolean, optional): sets inline display. Default: false.
- selectionButton (boolean, optional): adds a selection box around the radio. Default: false.
- selectionLabel (string, optional): label for the selection box. Default: ''.
- textCenter (boolean, optional): centers text on all selections. Default: false.
- disabled (boolean, optional): disables the element. Default: false.
- readonly (boolean, optional): sets the element as readonly. Default: false.
- checked (boolean, optional): checks the element. Default: false.
- required (boolean, optional): sets element as required. Default: false.
- html5Required (boolean, optional): uses the HTML5 required attribute. Default: true.
- showRequiredLabel (boolean, optional): shows the required asterisk on the wrapping label (true) or on the radio label (false). Default: true.

Snippet:

    Basic radio group:

    <@cRadio name='gender' label='Male' value='male' />
    <@cRadio name='gender' label='Female' value='female' />
    <@cRadio name='gender' label='Other' value='other' />

    Inline radio group:

    <@cRadio name='size' label='Small' value='S' inline=true />
    <@cRadio name='size' label='Medium' value='M' inline=true />
    <@cRadio name='size' label='Large' value='L' inline=true />

    Toggle button style:

    <@cRadio name='plan' label='Monthly' value='monthly' type='button' btnClass='btn btn-outline-primary' />
    <@cRadio name='plan' label='Yearly' value='yearly' type='button' btnClass='btn btn-outline-primary' />

    With selection box:

    <@cRadio name='delivery' label='Express delivery' value='express' selectionButton=true selectionLabel='2-3 business days'>
        <p>Fast shipping option</p>
    </@cRadio>

-->
<#macro cRadio name label type='radio' class='form-check' labelClass='' btnClass='' id='' value='' errorMsg='' helpMsg='' params='' inline=false selectionButton=false selectionLabel='' textCenter=false disabled=false readonly=false checked=false required=false html5Required=true showRequiredLabel=true deprecated...>
<@deprecatedWarning args=deprecated />
<#assign nestedContent><#nested></#assign>
<@cFormCheck name=name label=label type=type class=class labelClass=labelClass btnClass=btnClass id=id value=value errorMsg=errorMsg helpMsg=helpMsg params=params inline=inline selectionButton=selectionButton selectionLabel=selectionLabel textCenter=textCenter disabled=disabled readonly=readonly checked=checked required=required html5Required=html5Required showRequiredLabel=showRequiredLabel nestedContent=nestedContent />
</#macro>