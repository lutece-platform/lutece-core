<#--
Macro: accordionPanel

Description: Generates an HTML panel element for an accordion-style UI element with expandable and collapsible content.

Parameters:
- color (string, optional): the background color of the panel element.
- collapsed (boolean, optional): whether the panel should be collapsed by default.
- childId (string, required): the ID of the child element.
- id (string, optional): the ID of the panel element. If not provided, a default ID will be generated.
- params (string, optional): additional HTML attributes to include in the panel element.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Expanded accordion panel:

    <@accordionPanel childId='detailsPanel' collapsed=false>
        <@accordionHeader title='Details' />
        <@accordionBody>
            <p>This panel is expanded by default.</p>
        </@accordionBody>
    </@accordionPanel>

    Collapsed accordion panel with color:

    <@accordionPanel childId='settingsPanel' collapsed=true color='light'>
        <@accordionHeader title='Settings' />
        <@accordionBody>
            <p>This panel is initially collapsed.</p>
        </@accordionBody>
    </@accordionPanel>

-->
<#macro accordionPanel color='' collapsed=true childId='' id='' params='' attrs={}>
<div<@commonAttrs id=id classBase='card' + color?has_content?then( ' bg-' + color, '' ) params=params attrs=attrs />>
<#if collapsed>
	<#assign aClass = 'collapsed'>
	<#assign expanded = 'false'>
	<#assign childClass = 'collapse'>
<#else>
	<#assign aClass = ''>
	<#assign expanded = 'true'>
	<#assign childClass = 'collapse show'>
</#if>
<#assign childId = childId />
<#nested>
</div>
</#macro>