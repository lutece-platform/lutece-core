<#-- Macro: commonAttrs

Description: Writes the attributes every macro shares: id, class and params. It is the single place where they are
written, so the escaping rule of these attributes is decided once for all the macros: it calls the htmlAttributes
function of the core (HtmlAttributesMethod), which writes each attribute preceded by a space and nothing for an empty
value of the macro.
The same macro is declared for the back office (admin/themes/tabler/utilities/attributes/commonAttrs.ftl):
keep both files identical.

Parameters:
- id (string, optional): the id attribute.
- classBase (string, optional): classes the calling macro writes before class, as a plain string
  (e.g. 'btn btn-primary').
- class (string or markup, optional): the class parameter of the calling macro, as its caller passed it.
- classExtra (string, optional): classes the calling macro adds after class, as a plain string. A
  macro never rewrites its class parameter: it accumulates its own classes in classBase or classExtra.
- macroParams (map, optional): attributes the calling macro writes itself (role, aria-*, data-*...). They win over
  params.
- params (map, optional): the params of the calling macro, as its caller passed them: additional HTML attributes as
  name/value pairs, e.g. {'aria-label': label, 'hidden': !visible}. Names out of the allow list (event handlers,
  URLs, styles, class: pass it to the class parameter...) are refused, every value is escaped (a markup included): a true boolean writes the name alone,
  false writes nothing, any other value is written, even empty (alt=""). A string or a markup fails the rendering:
  params is a map.

Snippet:

    Inside a macro:

    <#macro alertBox id='' class='' collapsed=false params={} deprecated...>
    <#local classExtra = collapsed?then( 'collapse', '' ) />
    <div<@commonAttrs id=id classBase='alert' class=class classExtra=classExtra macroParams={'role': 'alert'} params=params />>
    ...

    Calling a macro with attributes:

    <@div id='main' params={'role': 'region', 'aria-label': label, 'hidden': !visible}>...</@div>

-->
<#macro commonAttrs id='' classBase='' class='' classExtra='' macroParams={} params={}>
${htmlAttributes( { 'id': id, 'class': classBase + ' ' + class + ' ' + classExtra } + macroParams, params )}<#t>
</#macro>
