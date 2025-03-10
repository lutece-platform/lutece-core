<#-- Macro: cStepDone

Description: Defines a macro that show a step that has been done

Parameters:
@param - step - string - optional - required - Step number
@param - title - string - optional - required - the title of the step
@param - idx - boolean - optional - Index of the step
@param - titleLevel - number - optional - HTML level of the title tag, default 2
@param - class - string - optional - the CSS class of the element, default '' 
@param - id - string - optional - the ID of the element, default ''
@param - actionName - string - optional - Action name for submit step button, default 'action_doGoToStep'
@param - actionLabel - string - optional - Label for action step button, default '#i18n{theme.labelUpdate}'
@param - actionAriaLabelKey - string - optional - Aria label for action step button, default '#i18n{theme.ariaLabelUpdate}'
@param - actionHref - string - optional - Url for action step button, default '#i18n{theme.ariaLabelUpdate}'
@param - actionClass - string - optional - Class for action step button, default '#i18n{theme.ariaLabelUpdate}'
@param - actionParams - string - optional - additional HTML attributes for action step button, default '#i18n{theme.ariaLabelUpdate}'
@param - params - string - optional - additional HTML attributes to include in the parent block element default ''
-->
<#macro cStepDone step title idx titleLevel=2 actionName='action_doGoToStep' actionLabel='#i18n{theme.labelUpdate}' actionAriaLabelKey='theme.ariaLabelUpdate' actionHref='' actionClass='' actionParams='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@cSection class='step step-done ${class!}' id=id params=params>
	<@cSection class='step-title'>
        <@cContainer class='d-flex justify-content-between align-items-baseline'>
            <@cTitle class='title' level=titleLevel params='title="${title}"' >
                <@cText type='span' class='step-number'><svg width="32" height="32" role="img" aria-label="${i18n('theme.labelStepDone', title)?html}" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M12.9607 23.9932L5.33203 16.3882L7.67726 14.0502L12.9607 19.3173L24.3201 7.99316L26.6654 10.3311L12.9607 23.9932Z" fill="white"/></svg></@cText>
                <@cText type='span'>${title}</@cText>
            </@cTitle>
            <@cSection type='span' class='d-none d-sm-block pl-2'>
            <#if actionHref !=''>
                <@cLink label=actionLabel! class='btn btn-secondary btn-sm-block ${actionClass!}' href=actionHref params='aria-label="${i18n(actionAriaLabelKey, title)?html}" ${actionParams!}' />
            <#elseif actionName !=''>
                <@cBtn class='secondary btn-sm-block' label=actionLabel! params='name="${actionName}"value="${idx!}" aria-label="${i18n(actionAriaLabelKey, title)?html}" formnovalidate' />
            </#if>
            </@cSection>    
        </@cContainer>    
	</@cSection>
	<@cContainer>
        <@cSection type='div' class='step-content'>
            <#nested>   
            <@cSection type='div' class='d-block d-sm-none mt-m'>
            <#if actionHref !=''>
                <@cLink label=actionLabel! class='btn btn-primary btn-sm-block ${actionClass!}' href=actionHref params='aria-label="${i18n(actionAriaLabelKey, title)?html}" ${actionParams!}' />
            <#elseif actionName !=''>
                <@cBtn class='primary btn-sm-block' label=actionLabel! params='name="${actionName}"value="${idx!}" aria-label="${i18n(actionAriaLabelKey, title)?html}" formnovalidate' />
            </#if>
            </@cSection>    
        </@cSection>
    </@cContainer>
</@cSection>
</#macro>