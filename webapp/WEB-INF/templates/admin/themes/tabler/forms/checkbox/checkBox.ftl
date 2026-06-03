<#--
Macro: checkBox

Description: Generates an HTML checkbox element with a specified ID, name, class, label, orientation, value, tabindex, title, disabled, readonly, checked, parameters, and mandatory flag.

Parameters:
- name (string, required): the name for the checkbox element.
- id (string, optional): the ID for the checkbox element.
- class (string, optional): additional classes to add to the checkbox element.
- labelKey (string, optional): the label for the checkbox element.
- orientation (string, optional): the orientation for the checkbox element, either "vertical" (default) or "switch".
- value (string, optional): the value for the checkbox element.
- tabIndex (string, optional): the tabindex for the checkbox element.
- title (string, optional): the title for the checkbox element.
- disabled (boolean, optional): whether the checkbox element is disabled.
- readonly (boolean, optional): whether the checkbox element is readonly.
- checked (boolean, optional): whether the checkbox element is checked.
- params (string, optional): additional parameters to add to the HTML code.
- mandatory (boolean, optional): whether the checkbox element is mandatory.
-->
<#macro checkBox name id='' class='' labelKey='' labelClass='' orientation='vertical' value='' tabIndex='' title='' disabled=false readonly=false checked=false params='' mandatory=false deprecated...>
<@deprecatedWarning args=deprecated />	
<#if id = ''><#local id = name /></#if>
<#if orientation!='switch'>
	<#if orientation='vertical'><div class="custom-control custom-checkbox"<#if params!=''> ${params}</#if>></#if>
	<input type="checkbox" class="custom-control-input<#if class!=''> ${class}</#if>" id="${id}" name="${name}"<#if value!=''> value="${value}"</#if><#if tabIndex!=''> tabindex="${tabIndex}"</#if><#if checked> checked</#if><#if disabled> disabled</#if><#if readonly> readonly</#if><#if mandatory> required</#if> />
	<label class="custom-control-label<#if orientation!='vertical'> checkbox-inline</#if><#if labelClass!=''> ${labelClass!}</#if>" for="${id}" <#if title!=''> title="${title}"</#if>>
	<#if labelKey!=''>${labelKey}<#else><#nested></#if>
	</label>
	<#if orientation='vertical'></div></#if>
<#else>
	<label class="form-check form-switch" for="${id}" <#if title!=''> title="${title}"</#if><#if params!=''> ${params}</#if>>
    	<input class="form-check-input<#if class!=''> ${class}</#if>" type="checkbox"  id="${id}" name="${name}" value="<#if value!=''>${value}</#if>"<#if tabIndex!=''> tabindex="${tabIndex}"</#if><#if checked> checked</#if><#if disabled> disabled</#if><#if readonly> readonly</#if><#if params!=''> ${params}</#if><#if mandatory> required</#if>>
   		<span class="form-check-label<#if labelClass!=''> ${labelClass!}</#if>"><#if labelKey!=''>${labelKey}<#else><#nested></#if></span>
  </label>
</#if>
</#macro>