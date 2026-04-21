<#--
Macro: cStepDone

Description: Generates a completed step in a multi-step form, displaying a check mark, summary content, and an edit button to navigate back to the step.

Parameters:
- step (string, required): the step number.
- title (string, required): the title of the step.
- idx (number, required): the index of the step used for the edit action value.
- titleLevel (number, optional): HTML heading level for the title tag. Default: 2.
- actionName (string, optional): action name for the edit/go-to-step button. Default: 'action_doGoToStep'.
- actionLabel (string, optional): label for the edit button. Default: '#i18n{portal.theme.labelUpdate}'.
- actionAriaLabelKey (string, optional): i18n key for the ARIA label of the edit button. Default: 'portal.theme.ariaLabelUpdate'.
- actionHref (string, optional): URL for the edit link (used instead of submit button). Default: ''.
- actionClass (string, optional): CSS class for the edit button/link. Default: ''.
- actionParams (string, optional): additional HTML attributes for the edit button. Default: ''.
- class (string, optional): CSS class for the step section. Default: ''.
- id (string, optional): the ID of the element. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.

Showcase:
- desc: "Étape terminée - @cStepDone"
- newFeature: false

Snippet:

    Completed step with summary:

    <@cStepDone step='1' title='Personal information' idx=0>
        <p>John Doe - john.doe@email.com</p>
    </@cStepDone>

    Completed step with link-based edit action:

    <@cStepDone step='1' title='Account details' idx=0 actionHref='jsp/site/Portal.jsp?page=form&step=1' actionLabel='Edit'>
        <p>Username: johndoe</p>
    </@cStepDone>

-->
<#macro cStepDone step title idx titleLevel=2 actionName='action_doGoToStep' actionLabel='#i18n{portal.theme.labelUpdate}' actionAriaLabelKey='portal.theme.ariaLabelUpdate' actionHref='' actionClass='' actionParams='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@cSection class='step step-done ${class!}' id=id params=params>
	<@cSection class='step-title'>
        <@cContainer class='d-flex justify-content-between align-items-baseline'>
            <@cTitle class='title' level=titleLevel params='title="${title}"' >
                <@cText type='span' class='step-number'><svg width="32" height="32" role="img" aria-label="${i18n('portal.theme.labelStepDone', title)?html}" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M12.9607 23.9932L5.33203 16.3882L7.67726 14.0502L12.9607 19.3173L24.3201 7.99316L26.6654 10.3311L12.9607 23.9932Z" fill="white"/></svg></@cText>
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