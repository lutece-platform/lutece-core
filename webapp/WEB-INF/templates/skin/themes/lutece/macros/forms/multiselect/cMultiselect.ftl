<#-- Macro: cMultiselect

Description: Defines a macro that show a multiselect tag

Parameters:
@param - class - string - optional - the CSS class of the element, default 'custom-select' 
@param - labelOptionSelection - string - optional - Label for the selection button 
@param - maxSelectedOptions - number - optional - Number of items that can be selected, default 0 (unlimited)
@param - id - string - optional - the ID of the element, default ''
@param - helpMsg - string - optional - Content of the help message for radio, default ''
@param - errorMsg - string - optional - Content of the error message for radio, default ''
@param - params - optional - additional HTML attributes to include in the ckeckbox element default ''
@param - disabled - boolean - optional - Disable element, default false
-->
<#macro cMultiselect class='' labelOptionSelection='#i18n{theme.labelOptionSelection}' maxSelectedOptions=0 id='' helpMsg='' errorMsg='' params='' disabled=false deprecated...>
<@deprecatedWarning args=deprecated />
<#local cId><#if id!=''>${id}<#else>msg-${random()!}</#if></#local>
<#if helpMsg !=''><@cFormHelp cId helpMsg /></#if>
<div class="multiselect ${class}" id="${cId}" data-maxoptions="${maxSelectedOptions}"> 
	<div class="tags-container mt-sm"></div>
    <div class="dropdown">
        <button class="form-select multi-select <#if disabled>disabled</#if>" type="button" id='btn-${cId}' data-bs-toggle="dropdown" aria-expanded="false"<#if disabled> disabled</#if> params=${params}>
            ${labelOptionSelection!}
        </button>
        <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
        	<#nested>
        </ul>
    </div>
</div>
<#if errorMsg!=''><@cFormError cId errorMsg /></#if>
</#macro>