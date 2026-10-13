<#-- Macro: unstyledList

Description: Generates an HTML unordered list (<ul>) with the "unstyled" class, which removes default list styles, and optional attributes.

Parameters:
- id (string, optional): the ID attribute of the unordered list.
- params (string, optional): additional attributes to add to the unordered list, in the form of a string of HTML attributes.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Basic unstyled list:

    <@unstyledList>
        <li>Item without bullet point</li>
        <li>Another item without bullet point</li>
    </@unstyledList>

    Unstyled list with ID and custom attributes:

    <@unstyledList id='nav-links' params='data-role="navigation"'>
        <li><a href='/home'>Home</a></li>
        <li><a href='/about'>About</a></li>
        <li><a href='/contact'>Contact</a></li>
    </@unstyledList>

-->
<#macro unstyledList id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<ul class="unstyled"<@commonAttrs id=id params=params attrs=attrs />>
	<#assign liClass = "margin">
	<#nested>
</ul>
</#macro>