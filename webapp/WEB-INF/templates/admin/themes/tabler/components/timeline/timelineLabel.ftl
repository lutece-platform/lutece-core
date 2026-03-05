<#--
Macro: timelineLabel

Description: Generates a label element for a timeline. This macro is a placeholder intended for timeline section separators or date labels. The current implementation renders no visible output.

Parameters:
- bg (string, optional): the background color for the label, using a Bootstrap color name (default is "primary").
- label (string, optional): the text content of the label.
- class (string, optional): additional CSS classes to add to the element.
- id (string, optional): the ID of the element.
- params (string, optional): additional parameters to add to the HTML code.

Snippet:

    Basic timeline label:

    <@timelineLabel label='March 2025' />

    Timeline label with custom background color:

    <@timelineLabel bg='info' label='Week 12' />

-->
<#macro timelineLabel bg='primary' label='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<!-- no label -->
</#macro>