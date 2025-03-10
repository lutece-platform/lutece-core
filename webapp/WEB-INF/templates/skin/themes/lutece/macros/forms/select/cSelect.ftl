<#-- Macro: cSelect

Description: Defines a macro that show a select tag

Parameters:
@param - name - string - optional - required - the name of of the element
@param - class - string - optional - the CSS class of the element, default 'custom-select' 
@param - size - string - optional - permet d'ajouter un suffixe à la classe CSS 'form-control', valeur possible 'lg' ou 'sm'
@param - id - string - optional - the ID of the element, default ''
@param - helpMsg - string - optional - Content of the help message for radio, default ''
@param - errorMsg - string - optional - Content of the error message for radio, default ''
@param - params - optional - additional HTML attributes to include in the ckeckbox element default ''
@param - multiple - boolean - optional - Set multiple attribute to select default false
@param - disabled - boolean - optional - Disable element, default false
@param - readonly - boolean - optional - Set element readonly, default false
@param - checked - boolean - optional - Check the element, default false
@param - required - boolean - optional - Set element as required, default false
@param - autocomplete- string - Default '' , autocomplete pour l'input https://developer.mozilla.org/fr/docs/Web/HTML/Attributes/autocomplete 
@param - html5Required - boolean - optional - permet d'indiquer si le champs doit utliser l'attribut html5 required (par défaut: true)
-->
<#macro cSelect name class='form-select' size='' id='' params='' multiple=false disabled=false autocomplete='' readonly=false required=false html5Required=true helpMsg='' errorMsg='' deprecated...>
<@deprecatedWarning args=deprecated />
<#assign idMsg><#if id!=''>${id}<#else>${name!}</#if></#assign>
<#if helpMsg !=''><@cFormHelp idMsg helpMsg /></#if>
<select name="${name!}" class="${class!}<#if size!=''> form-select-${size!}</#if><#if errorMsg!=''> is-invalid</#if>"<#if id!=''> id="${id}"</#if><#if autocomplete!=''> autocomplete="${autocomplete}"</#if><#if params!=''> ${params}</#if><#if disabled> disabled</#if><#if required> <#if html5Required>required</#if> aria-required="true"</#if><#if readonly> readonly</#if><#if multiple> multiple</#if><#if errorMsg!=''> aria-invalid="true" aria-describedby="error_${idMsg!}"<#elseif helpMsg!=''> aria-describedby="help_${idMsg!}"</#if>>
<#nested>
</select>
<#if errorMsg!=''><#assign idMsg><#if id!=''>${id}<#else>${name!}</#if></#assign><@cFormError idMsg errorMsg /></#if>
</#macro>