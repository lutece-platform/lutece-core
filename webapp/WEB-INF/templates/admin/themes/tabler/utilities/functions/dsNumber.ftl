<#-- Function: dsNumber

Description: Returns the number stored in a datastore key, or a default value when the key is missing
or does not hold an integer. It replaces the <#attempt>${dskey(key)?number}<#recover>0</#attempt>
captures: with HTML auto-escaping a capture is markup, not a number nor a string.

Parameters:
- key (string, required): the datastore key.
- fallback (number, optional): the value returned when the key holds no integer. Default: 0.

Snippet:

    <#if dsNumber('portal.site.site_property.layout.menu.home.checkbox') == 1>...</#if>

-->
<#function dsNumber key fallback=0>
<#local value = ( dskey( key )!'' )?trim />
<#return value?matches( '-?[0-9]+' )?then( value?number, fallback ) />
</#function>
