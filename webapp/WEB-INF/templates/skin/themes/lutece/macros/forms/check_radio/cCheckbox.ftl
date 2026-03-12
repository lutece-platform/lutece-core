<#--
Macro: cCheckbox

Description: Generates a checkbox input element. Wraps cFormCheck with type 'checkbox' by default.

Parameters:
- name (string, required): the name of the element.
- label (string, required): the label associated to the input.
- type (string, optional): the type, 'checkbox' or 'button'. Default: 'checkbox'.
- class (string, optional): the CSS class of the element. Default: 'form-check'.
- id (string, optional): the ID of the element. Default: ''.
- value (string, optional): the value of the element. Default: ''.
- btnClass (string, optional): CSS class for button label, only used if type is 'button'. Default: ''.
- labelClass (string, optional): the CSS class of the label. Default: ''.
- selectionButton (boolean, optional): adds a selection box around the checkbox. Default: false.
- selectionLabel (string, optional): label for the selection box. Default: ''.
- textCenter (boolean, optional): centers text on all selections. Default: false.
- errorMsg (string, optional): content of the error message. Default: ''.
- helpMsg (string, optional): content of the help message. Default: ''.
- inline (boolean, optional): sets inline display. Default: false.
- disabled (boolean, optional): disables the element. Default: false.
- readonly (boolean, optional): sets the element as readonly. Default: false.
- checked (boolean, optional): checks the element. Default: false.
- required (boolean, optional): sets element as required. Default: false.
- html5Required (boolean, optional): uses the HTML5 required attribute. Default: true.
- showRequiredLabel (boolean, optional): shows the required asterisk on the wrapping label (true) or on the checkbox label (false). Default: true.
- params (string, optional): additional HTML attributes. Default: ''.

Showcase:
- desc: "Case à cocher - @cCheckbox"
- bs: "forms/checks-radios"
- newFeature: false

Snippet:

    Basic checkbox:

    <@cCheckbox name='accept_terms' label='I accept the terms and conditions' />

    Checked and required checkbox:

    <@cCheckbox name='agreement' label='I agree to the privacy policy' checked=true required=true />

    Inline checkboxes:

    <@cCheckbox name='option_a' label='Option A' value='a' inline=true />
    <@cCheckbox name='option_b' label='Option B' value='b' inline=true />

    Toggle button style:

    <@cCheckbox name='subscribe' label='Subscribe' type='button' btnClass='btn btn-outline-primary' />

-->
<#macro cCheckbox name label type='checkbox' class='form-check' id='' value='' btnClass='' labelClass='' selectionButton=false selectionLabel='' textCenter=false errorMsg='' helpMsg='' inline=false disabled=false readonly=false checked=false required=false html5Required=true showRequiredLabel=true params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@cFormCheck name=name label=label type=type class=class labelClass=labelClass btnClass=btnClass id=id value=value errorMsg=errorMsg helpMsg=helpMsg params=params inline=inline selectionButton=selectionButton selectionLabel=selectionLabel textCenter=textCenter disabled=disabled readonly=readonly checked=checked required=required html5Required=html5Required showRequiredLabel=showRequiredLabel />
</#macro>
<#--
Macro: cCheckboxList

Description: Generates a checkbox element that supports nested content, wrapping cFormCheck with type 'checkbox' and forwarding nested content.

Parameters:
- name (string, required): the name of the element.
- label (string, required): the label associated to the input.
- type (string, optional): the type, 'checkbox' or 'button'. Default: 'checkbox'.
- class (string, optional): the CSS class of the element. Default: 'form-check'.
- id (string, optional): the ID of the element. Default: ''.
- value (string, optional): the value of the element. Default: ''.
- btnClass (string, optional): CSS class for button label, only used if type is 'button'. Default: ''.
- labelClass (string, optional): the CSS class of the label. Default: ''.
- selectionButton (boolean, optional): adds a selection box around the checkbox. Default: false.
- selectionLabel (string, optional): label for the selection box. Default: ''.
- textCenter (boolean, optional): centers text on all selections. Default: false.
- errorMsg (string, optional): content of the error message. Default: ''.
- helpMsg (string, optional): content of the help message. Default: ''.
- inline (boolean, optional): sets inline display. Default: false.
- disabled (boolean, optional): disables the element. Default: false.
- readonly (boolean, optional): sets the element as readonly. Default: false.
- checked (boolean, optional): checks the element. Default: false.
- required (boolean, optional): sets element as required. Default: false.
- html5Required (boolean, optional): uses the HTML5 required attribute. Default: true.
- params (string, optional): additional HTML attributes. Default: ''.

Snippet:

    Checkbox with nested content:

    <@cCheckboxList name='plan_premium' label='Premium plan' selectionButton=true>
        <p>Includes all premium features and priority support.</p>
    </@cCheckboxList>

-->
<#macro cCheckboxList name label  type='checkbox' class='form-check' id='' value='' btnClass='' labelClass='' selectionButton=false selectionLabel='' textCenter=false errorMsg='' helpMsg='' inline=false disabled=false readonly=false checked=false required=false html5Required=true params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@cFormCheck name=name label=label type=type class=class labelClass=labelClass btnClass=btnClass id=id value=value errorMsg=errorMsg helpMsg=helpMsg params=params inline=inline selectionButton=selectionButton selectionLabel=selectionLabel textCenter=textCenter disabled=disabled readonly=readonly checked=checked required=required html5Required=html5Required showRequiredLabel=showRequiredLabel>
<#nested>
</@cFormCheck>
</#macro>