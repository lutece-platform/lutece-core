<#-- Macro: coloredBg

Description: Wraps content in an element with a colored background.

Parameters:
- color (string): the color of the background, using a Bootstrap color class (e.g. "primary", "secondary", "success", "info", "warning", "danger", "light", or "dark").
- type (string, optional): the type of the element to use (e.g. "div", "p", "span", etc.).
- id (string, optional): the ID of the element.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Paragraph with a primary background color:

    <@coloredBg color='primary'>
        <span class="text-white">Highlighted text on primary background</span>
    </@coloredBg>

    Div with a warning background and custom ID:

    <@coloredBg color='warning' type='div' id='alert-box'>
        <p>Warning: This action cannot be undone.</p>
    </@coloredBg>

-->
<#macro coloredBg color='' type='p' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<${type} class="bg-${color}"<@commonAttrs id=id params=params />>
	<#nested>
</${type}>
</#macro>