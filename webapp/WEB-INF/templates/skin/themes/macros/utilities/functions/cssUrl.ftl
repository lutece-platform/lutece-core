<#-- Function: cssUrl

Description: Returns a URL that can be written in a CSS url("...") value, or '' when it cannot. HTML
escaping does not protect CSS: the browser decodes the attribute before parsing the style, so a quote
or a parenthesis in the URL would end url( ) and add CSS (OWASP XSS Prevention Cheat Sheet, rule #4:
only a validated value in a CSS property value). A URL holding a quote, a parenthesis, a backslash, a
space or < > is refused, and so is a scheme other than http and https (javascript:, vbscript:...),
as in safeUrl; a data: URL is kept only for an image.

Parameters:
- url (string or markup, required): the URL: relative, http, https or data:image/.

Snippet:

    <div style="background-image:url('${cssUrl(imageUrl)}')"></div>

-->
<#function cssUrl url>
<#local value = url?is_markup_output?then( url?markup_string, url?string )?trim />
<#if !value?matches( '[A-Za-z0-9._~:/?#\\[\\]@!$&*+,;=%-]+' )>
<#return '' />
</#if>
<#local lower = value?lower_case />
<#local colon = lower?index_of( ':' ) />
<#if colon lt 0 || lower[0..<colon]?matches( '.*[/?#].*' ) || [ 'http', 'https' ]?seq_contains( lower[0..<colon] ) || lower?starts_with( 'data:image/' )>
<#return value />
</#if>
<#return '' />
</#function>
