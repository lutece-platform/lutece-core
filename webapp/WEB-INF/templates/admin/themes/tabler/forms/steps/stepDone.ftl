<#-- Macro: stepDone

Description: Defines a macro that show a step that has been done

Parameters:
@param - title - string - optional - required - the title of the step
@param - step - string - optional - Step number, default ''
@param - idx - boolean - optional - Index of the step
@param - titleLevel - number - optional - HTML level of the title tag, default 2
@param - class - string - optional - the CSS class of the element, default '' 
@param - id - string - optional - the ID of the element, default ''
@param - actionName - string - optional - Action name for submit step button, default 'action_doGoToStep'
@param - actionLabel - string - optional - Label for action step button, default '#i18n{portal.theme.labelUpdate}'
@param - actionAriaLabelKey - string - optional - Aria label for action step button, default '#i18n{portal.theme.ariaLabelUpdate}'
@param - actionHref - string - optional - Url for action step button, default '#i18n{portal.theme.ariaLabelUpdate}'
@param - actionClass - string - optional - Class for action step button, default '#i18n{portal.theme.ariaLabelUpdate}'
@param - actionParams - string - optional - additional HTML attributes for action step button, default '#i18n{portal.theme.ariaLabelUpdate}'
@param - params - string - optional - additional HTML attributes to include in the parent block element default ''

Snippet:

    Completed step with default edit button:

    <@stepDone step='1' title='Personal Information' idx=0>
        <li>First Name: John</li>
        <li>Last Name: Doe</li>
    </@stepDone>

    Completed step with a link-based action button:

    <@stepDone step='2' title='Contact Details' idx=1 actionHref='jsp/admin/EditContact.jsp?id=1' actionLabel='Modify' actionClass='btn-sm'>
        <li>Email: john.doe@example.com</li>
    </@stepDone>

    Completed step with custom action name and label:

    <@stepDone step='1' title='Address' idx=0 actionName='action_doEditStep' actionLabel='Edit this step'>
        <li>123 Main Street, Paris</li>
    </@stepDone>

-->
<#macro stepDone title idx step='' titleLevel=2 actionName='action_doGoToStep' actionLabel='#i18n{portal.theme.labelUpdate}' actionAriaLabelKey='portal.theme.ariaLabelUpdate' actionHref='' actionClass='' actionParams='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local stepNumber><#if step?has_content>${step}<#else><svg width="32" height="32" role="img" aria-label="<#outputformat "HTML">${i18n('portal.theme.labelStepDone', title)}</#outputformat>" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M12.9607 23.9932L5.33203 16.3882L7.67726 14.0502L12.9607 19.3173L24.3201 7.99316L26.6654 10.3311L12.9607 23.9932Z" fill="white"/></svg></#if></#local>
<@div class='step step-done ${class!}' id=id params=params>
	<@div class='step-title'>
        <@div class='d-flex justify-content-between align-items-baseline w-100'>
            <#local paramsAttr1>title="${title}"</#local>
            <@h class='title' level=titleLevel params=paramsAttr1 >
                <@span class='step-number'>${stepNumber}</@span>
                <@span>${title?replace('- hidden','')}</@span>
            </@h>
            <@span class='d-none d-sm-block pl-2'>
            <#if actionHref?has_content>
                <#local esc1><#outputformat "HTML">${i18n(actionAriaLabelKey, title)}</#outputformat></#local>
                <#local paramsAttr2>aria-label="${esc1}" ${actionParams!}</#local>
                <@aButton label=actionLabel! class='btn btn-outline-primary ${actionClass!}' href=actionHref params=paramsAttr2 />
            <#elseif actionName?has_content>
                <#local esc2><#outputformat "HTML">${i18n(actionAriaLabelKey, title)}</#outputformat></#local>
                <#local paramsAttr3>name="${actionName}"value="${idx!}" aria-label="${esc2}" formnovalidate</#local>
                <@button class='outline-primary' label=actionLabel! params=paramsAttr3 />
            </#if>
            </@span>    
        </@div>    
	</@div>
    <@div class='step-content'>
        <@ul class='list-unstyled'>
            <#nested>   
        </@ul>
        <@div class='d-block d-sm-none mt-m'>
        <#if actionHref?has_content>
            <#local esc3><#outputformat "HTML">${i18n(actionAriaLabelKey, title)}</#outputformat></#local>
            <#local paramsAttr4>aria-label="${esc3}" ${actionParams!}</#local>
            <@aButton label=actionLabel! class='btn btn-primary btn-sm-block ${actionClass!}' href=actionHref params=paramsAttr4 />
        <#elseif actionName?has_content>
            <#local esc4><#outputformat "HTML">${i18n(actionAriaLabelKey, title)}</#outputformat></#local>
            <#local paramsAttr5>name="${actionName}"value="${idx!}" aria-label="${esc4}" formnovalidate</#local>
            <@button class='primary btn-sm-block' label=actionLabel! params=paramsAttr5 />
        </#if>
        </@div>    
    </@div>
</@div>
</#macro>