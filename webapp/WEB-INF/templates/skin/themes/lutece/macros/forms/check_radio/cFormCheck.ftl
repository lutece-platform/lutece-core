<#-- Macro: cFormCheck

Description: Defines a macro that show a checkbox or a radio button

Parameters:
@param - name - string - required - the name of of the element
@param - label - string - required - the label associated to the input
@param - type - string - the type of check, can be 'checkbox', 'radio' or 'button'
@param - helpMsg - string - optional - Content of the help message for checkbox, default ''
@param - errorMsg - string - optional - Content of the error message for checkbox, default ''
@param - class - string - optional - the CSS class of the element, default 'custom-checkbox' 
@param - btnClass - string - optional - Only use if type is 'button' - default ''
@param - id - string - optional - the ID of the element, default ''
@param - value - string - optional - the value of the element, default ''
@param - selectionButton - boolean - optional - Add box to the checkbox, default false 
@param - selectionLabel - string - optional - Add label to the "selection" box default ''
@param - textCenter - boolean - optional - Center text on all select default false
@param - params - optional - additional HTML attributes to include in the ckeckbox element default ''
@param - inline - boolean - optional - Set inline checkbox default false
@param - disabled - boolean - optional - Disable element, default false
@param - readonly - boolean - optional - Set element readonly, default false
@param - checked - boolean - optional - Check the element, default false
@param - required - boolean - optional - Set element as required, default false
@param - html5Required - boolean - optional - permet d'indiquer si le champs doit utliser l'attribut html5 required (par défaut: true)
@param - showRequiredLabel - boolean - optional - indique si l'affichage de l'étoile pour "required" s'affiche sur le label (false)  ou le label englobant les input (true) (par défaut: true)@param - #nested - String - Any text to add un label
-->
<#macro cFormCheck name label type class='form-check' id='' value='' btnClass='' labelClass='' selectionButton=false selectionLabel='' nestedContent='' textCenter=false errorMsg='' helpMsg='' inline=false disabled=false readonly=false checked=false required=false html5Required=true showRequiredLabel=true params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local cType><#if type='button'>checkbox<#else>${type}</#if></#local>
<#local cId><#if id=''>${name}-${random()}<#else>${id}</#if></#local>
<#local idMsg><#if id!=''>${id}<#else>${name!}</#if></#local>
<#if helpMsg !=''><@cFormHelp idMsg helpMsg /></#if>
<#if type !='button'>
<div class="<#if class='form-check'>form-check<#else> ${class}</#if><#if selectionButton> btn-selection</#if><#if inline> form-check-inline</#if><#if errorMsg!=''> is-invalid</#if><#if labelClass?contains('visually-hidden')> hidden-label</#if>">
<#if selectionButton><div class="selection-check"></#if>
    <input type="${cType}" id="${cId!}" name="${name!}" class="form-check-input" <#if value!=''>value="${value}"</#if><#if errorMsg !=''> aria-invalid="true"</#if><#if disabled> disabled</#if><#if required><#if html5Required> required</#if> aria-required="true"</#if><#if readonly> readonly</#if><#if checked> checked</#if><#if params!=''> ${params!}</#if>>
    <#local cFCLabel>${label!} <#nested></#local>
    <#local cFCClass>form-check-label<#if labelClass!=''> ${labelClass}</#if><#if textCenter> w-100 justify-content-center</#if></#local>
    <#local cFCRequired=required />
    <#if !showRequiredLabel><#local cFCRequired=false /></#if>
    <@cLabel label=cFCLabel class=cFCClass for=cId required=cFCRequired/>
<#if selectionButton></div></#if>
<#if !selectionButton>${nestedContent}</#if>
<#if selectionLabel!=''><p class="selection-subtitle ms-m my-sm<#if textCenter> text-center</#if>">${selectionLabel}</p></#if>
<#if selectionButton && nestedContent?has_content><div class="selection-content">${nestedContent}</div></#if>
</div>
<#else>
    <input type="${cType!'checkbox'}" name="${name!}" class="btn-check<#if class!=''> ${class}</#if><#if errorMsg!=''> is-invalid</#if>" id="${cId!?replace(',','-')}" autocomplete="off"<#if value!=''> value="${value}"</#if><#if errorMsg !=''>= aria-invalid="true"</#if><#if disabled> disabled</#if><#if required> required</#if><#if readonly> readonly</#if><#if checked> checked</#if><#if params!=''> ${params!}</#if>>
    <#local cFCClass><#if btnClass!=''>${btnClass}<#else>btn btn-outline-primary</#if></#local>
    <#local cFCRequired=required />
    <#if !showRequiredLabel><#local cFCRequired=false /></#if>
    <@cLabel label=label! class=cFCClass for=cId required=cFCRequired  />
</#if>  
<#if errorMsg !=''><@cFormError idMsg errorMsg /></#if>
</#macro>