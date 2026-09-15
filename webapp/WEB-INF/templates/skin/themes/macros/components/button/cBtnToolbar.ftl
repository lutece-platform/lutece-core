<#--
Macro: cBtnToolbar

Description: Generates a toolbar container for grouping multiple button groups together.

Parameters:
- label (string, required): The aria-label for the toolbar.
- type (string, optional): Layout type ('vertical' for stacked). Default: ''.
- class (string, optional): Additional CSS class(es) for the toolbar. Default: ''.
- id (string, optional): The unique identifier for the toolbar. Default: ''.
- params (string, optional): Additional HTML attributes for the toolbar. Default: ''.

Showcase:
- desc: Barre d'outils - @cBtnToolbar
- bs: components/button-group
- newFeature: false
- updatedFeature: false
- deprecated: false

Snippet:

    Toolbar with button groups:

    <@cBtnToolbar label='Document actions'>
        <@cBtnGroup label='Editing'>
            <@cBtn label='Cut' class='secondary' />
            <@cBtn label='Copy' class='secondary' />
            <@cBtn label='Paste' class='secondary' />
        </@cBtnGroup>
        <@cBtnGroup label='Formatting'>
            <@cBtn label='Bold' class='secondary' />
            <@cBtn label='Italic' class='secondary' />
        </@cBtnGroup>
    </@cBtnToolbar>

-->
<#macro cBtnToolbar label type='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local btnClass>btn-toolbar<#if type='vertical'>-vertical</#if><#if class?has_content> ${class}</#if></#local>
<#local btnClass = btnClass?is_markup_output?then(btnClass?markup_string, btnClass) />
<#local paramsAttr1>${params} role="toolbar" aria-label="${label}"</#local>
<@cSection type='div' class=btnClass id=id params=paramsAttr1>
    <#nested>
</@cSection>
</#macro>