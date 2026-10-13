<#--
Macro: cFormRow

Description: Generates a form row container (wraps cRow) to organize form fields horizontally in a grid layout.

Parameters:
- class (string, optional): CSS class for the row. Default: ''.
- id (string, optional): the ID of the row. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Showcase:
- desc: "Ligne de formulaire - @cFormRow"
- newFeature: false

Snippet:

    Form row with two fields side by side:

    <@cFormRow>
        <@cCol cols='6'>
            <@cField label='First name' for='firstname'>
                <@cInput name='firstname' id='firstname' />
            </@cField>
        </@cCol>
        <@cCol cols='6'>
            <@cField label='Last name' for='lastname'>
                <@cInput name='lastname' id='lastname' />
            </@cField>
        </@cCol>
    </@cFormRow>

-->
<#macro cFormRow class='' id='' params='' attrs={}>
<@cRow class=class id=id params=params attrs=attrs >
<#nested>
</@cRow>
</#macro>