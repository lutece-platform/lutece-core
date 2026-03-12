<#--
Macro: parisIcon

Description: Generates an SVG icon from the Paris icon sprite set with accessibility support.

Parameters:
- name (string, required): Name of the icon from the Paris icon pack.
- type (string, optional): Accessibility type, either 'decorative' (hidden from screen readers) or 'informative' (announced by screen readers). Default: 'decorative'.
- id (string, optional): HTML id attribute. Default: ''.
- class (string, optional): Additional CSS classes for the icon. Default: ''.
- title (string, optional): Accessible title for informative icons. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Showcase:
- desc: Icône Paris SVG - @parisIcon
- newFeature: false

Snippet:

    Decorative icon (default):

    <@parisIcon name='arrow-right' />

    Informative icon with title:

    <@parisIcon name='check' type='informative' title='Validated' class='text-success' />

    Icon with custom class and attributes:

    <@parisIcon name='close' class='ms-2 main-color' params='aria-label="Close"' />

-->
<#macro parisIcon name type='decorative' id='' class='' title='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local rId=random() /> 
<svg class="paris-icon paris-icon-${name!}<#if class!=''> ${class!}</#if>"<#if id!=''> id="${id!}"</#if>data-mce-svg="paris-icon paris-icon-${name!}" <#if type !='decorative' && title!=''> aria-labelledby="paris-icon-${name!}-paris-title-${rId}"<#elseif type='decorative'>aria-hidden="true"</#if> focusable="false" role="img"<#if params!=''> ${params!}</#if>>
<#if type !='decorative' && title!=''><title id="paris-icon-${name!}-paris-title-${rId}">${title}</title></#if>
<use xlink:href="#paris-icon-${name!}"></use>
</svg>
</#macro>