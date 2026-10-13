<#-- Macro: commonAttrs

Description: Writes the attributes every macro shares: id, class, params and attrs. It is the single
place where they are written, so the escaping rule of these attributes is decided once for all the
macros. It writes nothing for an empty value, and each attribute is preceded by a space.
The same macro is declared for the back office (admin/themes/tabler/utilities/attributes/commonAttrs.ftl):
keep both files identical.

Parameters:
- id (string, optional): the id attribute.
- classBase (string, optional): classes the calling macro writes before class, as a plain string
  (e.g. 'btn btn-primary').
- class (string or markup, optional): the class parameter of the calling macro, as its caller passed
  it. It is written as is, never rewritten.
- classExtra (string, optional): classes the calling macro adds after class, as a plain string. A
  macro never rewrites its class parameter: it accumulates its own classes in classBase or
  classExtra, so the type of the parameter (string or markup) is kept.
- params (string, optional): a bundle of attributes written as is, e.g. 'data-x="1" aria-hidden="true"'.
  Deprecated: use attrs. The caller writes the HTML structure, so a value cannot be escaped.
- attrs (hash, optional): additional attributes as name/value pairs, e.g. {'aria-hidden': 'true'}.
  The caller gives only data, the macro writes the structure (name, equal sign, quotes). The value
  is written like any other interpolation of the page: with auto-escaping on
  (service.freemarker.templateAutoEscape=true) it is escaped, so it can never close the attribute
  or add another one, and a markup value is written as is. A true boolean writes a valueless
  attribute ({'disabled': true}), a false boolean writes nothing ({'hidden': !visible}); any other
  value is written, even empty (alt=""), exactly as a params bundle would. A name that is not a
  valid attribute name is ignored.

Snippet:

    Inside a macro:

    <#macro alertBox id='' class='' collapsed=false params='' attrs={} deprecated...>
    <#local classExtra = collapsed?then( 'collapse', '' ) />
    <div<@commonAttrs id=id classBase='alert' class=class classExtra=classExtra params=params attrs=attrs />>
    ...

    Calling a macro with attributes:

    <@div id='main' attrs={'role': 'region', 'aria-label': label, 'hidden': !visible}>...</@div>

-->
<#macro commonAttrs id='' classBase='' class='' classExtra='' params='' attrs={}>
<#local before = classBase?trim?replace( '\\s+', ' ', 'r' ) />
<#local after = classExtra?trim?replace( '\\s+', ' ', 'r' ) />
<#if id?has_content> id="${id}"</#if><#t>
<#if before?has_content || class?has_content || after?has_content> class="<#t>
${before}<#if before?has_content && class?has_content> </#if>${class}<#if ( before?has_content || class?has_content ) && after?has_content> </#if>${after}"<#t>
</#if>
<#if params?has_content> ${params}</#if><#t>
<#list attrs as attrName, attrValue>
<#if attrName?matches( '[A-Za-z_:][-A-Za-z0-9_:.]*' )>
<#if attrValue?is_boolean>
<#if attrValue> ${attrName}</#if><#t>
<#else>
 ${attrName}="<#if attrValue?is_number>${attrValue?c}<#else>${attrValue}</#if>"<#rt>
</#if>
</#if>
</#list>
</#macro>
