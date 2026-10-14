<#-- Function: cssUrl

Description: Returns a URL that can be written in a CSS url("...") value, or '' when it cannot. HTML
escaping does not protect CSS: the browser decodes the attribute before parsing the style, so a quote
or a parenthesis in the URL would end url( ) and add CSS (OWASP XSS Prevention Cheat Sheet, rule #4:
only a validated value in a CSS property value). A URL holding a quote, a parenthesis, a backslash, a
space or < > is refused.

Parameters:
- url (string, required): the URL, a data: URL included.

Snippet:

    <div style="background-image:url('${cssUrl(imageUrl)}')"></div>

-->
<#function cssUrl url>
<#local value = ( url!'' )?trim />
<#return value?matches( '[A-Za-z0-9._~:/?#\\[\\]@!$&*+,;=%-]+' )?then( value, '' ) />
</#function>
