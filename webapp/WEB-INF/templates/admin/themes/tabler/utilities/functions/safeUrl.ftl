<#-- Function: safeUrl

Description: Returns a URL that can be written in a link (href, action, location), or a fallback when
its scheme is not a safe one. HTML escaping does not stop a javascript: or data: URL from running when
the link is followed (OWASP XSS Prevention Cheat Sheet: URLs from data must be validated). Accepted: a
relative URL, http, https, mailto and tel. White space and control characters are ignored when the
scheme is read, as browsers do ("java script:").

Parameters:
- url (string or markup, required): the URL, typically read from a site property. A markup value
  (a capture) is returned as is when it is accepted, so it is not escaped twice.
- fallback (string, optional): the value returned for a refused URL. Default: '#'.

Snippet:

    <a href="${safeUrl(dskey('portal.theme.site_property.Url.legalURL'))}">...</a>

-->
<#function safeUrl url fallback='#'>
<#local text = url?is_markup_output?then( url?markup_string, url?string ) />
<#local compact = text?lower_case?replace( '[\\x00-\\x20]', '', 'r' ) />
<#local colon = compact?index_of( ':' ) />
<#if colon lt 0>
<#return url />
</#if>
<#local scheme = compact[0..<colon] />
<#if scheme?matches( '.*[/?#].*' ) || [ 'http', 'https', 'mailto', 'tel' ]?seq_contains( scheme )>
<#return url />
</#if>
<#return fallback />
</#function>
