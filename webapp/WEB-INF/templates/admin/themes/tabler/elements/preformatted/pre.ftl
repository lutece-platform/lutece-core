<#--
Macro: pre

Description: Generates an HTML preformatted text element with a specified ID, class, display settings, alignment, and nested content block.

Parameters:
- id (string, optional): the ID for the preformatted text element.
- class (string, optional): additional classes to add to the preformatted text element.
- hide (list, optional): a list of display settings to hide the preformatted text element, e.g., ["md", "lg"].
- collapsed (boolean, optional): whether to collapse the preformatted text element.
- align (string, optional): the alignment setting for the preformatted text element, e.g., "center" or "right".
- params (string, optional): additional parameters to add to the HTML code.

Snippet:

    Simple preformatted text block:

    <@pre>
        Line 1: Hello
        Line 2: World
    </@pre>

    Preformatted block with custom class and ID:

    <@pre class='bg-light p-3 rounded' id='log-output'>
        [INFO] Application started successfully.
        [INFO] Listening on port 8080.
    </@pre>

-->
<#macro pre id='' class='' hide=[] collapsed=false align='' params='' deprecated...>
<#local class = class?is_markup_output?then(class?markup_string, class) />
<@deprecatedWarning args=deprecated />
<#if collapsed><#local class += ' ' + 'collapse' /></#if>
<#local class += ' ' + alignmentSettings(align,'') + ' ' + displaySettings(hide,'block') />
<pre<#if class?has_content> class="${class?trim}"</#if><#if params?has_content> ${params}</#if><#if id?has_content> ${id}</#if>>
	<#nested>
</pre>
</#macro>