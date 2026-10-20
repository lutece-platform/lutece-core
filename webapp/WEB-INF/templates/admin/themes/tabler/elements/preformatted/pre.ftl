<#--
Macro: pre

Description: Generates an HTML preformatted text element with a specified ID, class, display settings, alignment, and nested content block.

Parameters:
- id (string, optional): the ID for the preformatted text element.
- class (string, optional): additional classes to add to the preformatted text element.
- hide (list, optional): a list of display settings to hide the preformatted text element, e.g., ["md", "lg"].
- collapsed (boolean, optional): whether to collapse the preformatted text element.
- align (string, optional): the alignment setting for the preformatted text element, e.g., "center" or "right".
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

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
<#macro pre id='' class='' hide=[] collapsed=false align='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local classExtra = collapsed?then( 'collapse', '' ) + ' ' + alignmentSettings( align, '' ) + ' ' + displaySettings( hide, 'block' ) />
<pre<@commonAttrs id=id class=class classExtra=classExtra params=params />>
	<#nested>
</pre>
</#macro>