<#--
Macro: cMultiselectOption

Description: Generates an individual option item for the cMultiselect component, rendered as a checkbox inside a dropdown list item.

Parameters:
- name (string, required): the name of the option input.
- label (string, required): the label associated to the option.
- class (string, optional): CSS class for the option list item. Default: ''.
- classCheckbox (string, optional): CSS class for the checkbox inside the option. Default: ''.
- value (string, optional): the value associated to the option. Default: ''.
- id (string, optional): the ID of the element. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.
- disabled (boolean, optional): disables the option. Default: false.

Snippet:

    Basic multiselect option:

    <@cMultiselectOption name='tags' label='Technology' value='tech' />

    Disabled option:

    <@cMultiselectOption name='tags' label='Archived' value='archived' disabled=true />

-->
<#macro cMultiselectOption name label class='' classCheckbox='' value='' id='' params='' attrs={} disabled=false deprecated...>
<@deprecatedWarning args=deprecated />
<@chItem class=class!>
    <@cCheckbox class=classCheckbox name=name id=id label=label value=value type='button' disabled=disabled params=params attrs=attrs/>
</@chItem>    
</#macro>