<#--
Macro: cFormRow

Description: Generates a form row container (wraps cRow) to organize form fields horizontally in a grid layout.

Parameters:
- class (string, optional): CSS class for the row. Default: ''.
- id (string, optional): the ID of the row. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.

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
<#macro cFormRow class='' id='' params=''>
<@cRow class=class id=id params=params >
<#nested>
</@cRow>
</#macro>