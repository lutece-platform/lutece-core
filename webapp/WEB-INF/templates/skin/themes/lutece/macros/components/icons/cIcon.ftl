<#-- icon
deprecated
 -->
<#macro cIcon name='check' class='' id='' params='' prefix='ti ti-' type='span' deprecated...>
<@deprecatedWarning args=deprecated />
<@cInline type=type class='${prefix}${name} ${class}' id=id params=params />
</#macro>