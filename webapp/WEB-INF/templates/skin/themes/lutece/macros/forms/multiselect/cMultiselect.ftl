<#--
Macro: cMultiselect

Description: Generates a multiselect dropdown component with tag-based selection, supporting a maximum number of options and custom labels.

Parameters:
- class (string, optional): CSS class for the multiselect container. Default: ''.
- labelOptionSelection (string, optional): label for the dropdown toggle button. Default: '#i18n{portal.theme.labelOptionSelection}'.
- maxSelectedOptions (number, optional): maximum number of items that can be selected, 0 for unlimited. Default: 0.
- id (string, optional): the ID of the element. Default: ''.
- helpMsg (string, optional): content of the help message. Default: ''.
- errorMsg (string, optional): content of the error message. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.
- disabled (boolean, optional): disables the multiselect. Default: false.

Showcase:
- desc: "Sélection multiple - @cMultiselect"
- newFeature: false

Snippet:

    Basic multiselect:

    <@cMultiselect>
        <@cMultiselectOption name='categories' label='Sport' value='sport' />
        <@cMultiselectOption name='categories' label='Culture' value='culture' />
        <@cMultiselectOption name='categories' label='Education' value='education' />
    </@cMultiselect>

    Multiselect with max 3 selections:

    <@cMultiselect id='interests' maxSelectedOptions=3>
        <@cMultiselectOption name='interests' label='Music' value='music' />
        <@cMultiselectOption name='interests' label='Travel' value='travel' />
        <@cMultiselectOption name='interests' label='Food' value='food' />
        <@cMultiselectOption name='interests' label='Art' value='art' />
    </@cMultiselect>

-->
<#macro cMultiselect class='' labelOptionSelection='#i18n{portal.theme.labelOptionSelection}' maxSelectedOptions=0 id='' helpMsg='' errorMsg='' params='' disabled=false deprecated...>
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