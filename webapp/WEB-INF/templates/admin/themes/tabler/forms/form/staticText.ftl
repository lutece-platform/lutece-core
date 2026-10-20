<#-- Macro: staticText

Description: Generates a static text element.

Parameters:
- inForm (boolean, optional): whether the static text element is inside a form control.
- color (string, optional): the color of the text, using a Bootstrap color class (e.g. "primary").
- id (string, optional): the ID of the static text element.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Static text inside a form:

    <@staticText>
        This value cannot be modified.
    </@staticText>

    Colored static text outside a form:

    <@staticText inForm=false color='danger'>
        This record has been deleted.
    </@staticText>

-->
<#macro staticText inForm=true color='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<p class="<#if inForm>form-control-plaintext</#if><#if color?has_content> text-${color}</#if>"<@commonAttrs id=id params=params />>
<#nested>
</p>
</#macro>