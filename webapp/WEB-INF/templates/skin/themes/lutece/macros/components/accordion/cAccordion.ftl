<#--
Macro: cAccordion

Description: Generates a collapsible accordion panel that reveals or hides associated content when a stacked title is clicked or activated via keyboard or screen reader.

Parameters:
- id (string, required): The unique identifier for the accordion.
- title (string, required): The title text displayed in the accordion header.
- btnTitle (string, optional): The label for the accordion toggle button. Default: ‘’.
- class (string, optional): Additional CSS class(es) for the accordion container. Default: ‘’.
- titleClass (string, optional): CSS class(es) applied to the accordion title. Default: ‘’.
- titleLevel (number, optional): The heading level for the accordion title. Default: 3.
- subTitle (string, optional): Subtitle text displayed below the title. Default: ‘’.
- subTitleClass (string, optional): CSS class(es) for the subtitle. Default: ‘’.
- btnClass (string, optional): CSS class(es) for the collapse toggle icon. Default: ‘’.
- btnShowLabel (string, optional): Text for the data-show-label attribute. Default: ‘#i18n{portal.theme.labelShowDetail}’.
- btnHideLabel (string, optional): Text for the data-hide-label attribute. Default: ‘#i18n{portal.theme.labelHideDetail}’.
- header (string, optional): HTML content added alongside the title. Default: ‘’.
- border (boolean, optional): If true, a border is added to the accordion. Default: false.
- state (boolean, optional): If true, the accordion is expanded by default. Default: true.
- hasCollapse (boolean, optional): If true, shows the collapse toggle icon. Default: true.
- params (string, optional): Additional HTML attributes for the accordion. Default: ‘’.

Snippet:

    Basic usage:

    <@cAccordion id=’faq-1’ title=’How to create an account?’>
        <p>Follow the registration steps on the portal homepage.</p>
    </@cAccordion>

    Collapsed by default with border:

    <@cAccordion id=’faq-2’ title=’Terms of service’ state=false border=true>
        <p>Please read the terms carefully before proceeding.</p>
    </@cAccordion>

-->
<#macro cAccordion id title btnTitle='' class='' titleClass='' titleLevel=3 subTitle='' subTitleClass='' btnClass='' btnShowLabel='#i18n{portal.theme.labelShowDetail}' btnHideLabel='#i18n{portal.theme.labelHideDetail}' header='' border=false state=true hasCollapse=true params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local iconType><#if class?contains('danger')>danger<#elseif class?contains('warning')>warning<#elseif class?contains('success')>success<#else>info</#if></#local>
<#local accordionRole><#if class?contains('danger')>alert<#elseif class?contains('warning')>alert<#elseif class?contains('success')>status<#else>status</#if></#local>
<div class="accordion" id="acc${id!}" <#if class?contains('outline')>role="${accordionRole}"</#if> >
    <div class="card ${class!}<#if border> with-border</#if><#if subTitle !=''> with-subtitle</#if>" ${params!}>
        <#local cardTitleClass>card-header m-0<#if !state> collapsed</#if></#local>
        <#local titleParams>data-bs-toggle="collapse" data-bs-target="#collapseAcc${id}" <#if hasCollapse>aria-expanded="<#if state>true<#else>false</#if></#if>" aria-controls="collapseAcc${id}"</#local>
        <@cTitle level=titleLevel class=cardTitleClass params=titleParams>
            <button class="btn btn-link btn-block btn-header-accordion w-100<#if iconType != 'info'> main-${iconType}-color-text</#if>" type="button" data-bs-toggle="collapse" data-show-label="${btnShowLabel}" data-hide-label="${btnHideLabel}" data-bs-target="#collapseAcc${id}" aria-expanded="<#if state>true<#else>false</#if>" aria-controls="collapseAcc${id}" aria-labelledby="headingAcc${id}">
            <span class="d-flex<#if !class?contains('outline') && header =''> flex-column<#else> align-items-center</#if> flex-1">
            <#if class?contains('outline')>
                <span class="card-header-prepend">
                <@parisIcon name='alert-${iconType}' />
                </span>
                <span<#if titleClass !=''>class="d-block ${titleClass}"</#if> id="headingAcc${id}">${title}</span>
                <#if header !=''>${header}</#if>
                <#if subTitle !=''><span class="card-subtitle w-100<#if subTitleClass !=''> ${subTitleClass}</#if>">${subTitle}</span></#if>
                <#if hasCollapse><span class="card-header-separator"></span></#if>
            <#else>
                <span class="card-title d-block<#if titleClass !=''> ${titleClass}</#if>" id="headingAcc${id}">${title}</span>
                <#if header !=''>${header}</#if>
                <#if subTitle !=''><span class="card-subtitle w-100<#if subTitleClass !=''> ${subTitleClass}</#if>">${subTitle}</span></#if>
            </#if>
            </span>
            <#if hasCollapse>
            <span class="accordion-toggle ms-auto">
                <#if btnTitle !=''><span class="btn-label-accordion d-none d-md-inline-block<#if iconType != 'info'> main-${iconType}-color-text</#if>">${btnTitle}</span></#if>
                <span class="btn-accordion<#if btnClass !=''> ${btnClass}</#if>">
                    <#local btnClass>${btnClass}<#if iconType != 'info'> main-${iconType}-color-text</#if></#local>
                    <@parisIcon name='arrow-bottom' class=btnClass title='' />
                </span>
            </span>
            </#if>
            </button>
        </@cTitle>
        <#if hasCollapse>
            <div id="collapseAcc${id}" class="m-0 collapse <#if state>show</#if>" role="region" aria-labelledby="headingAcc${id}"  data-parent="#acc${id!}">
                <div class="card-body"<#if iconType != 'info'> style="border-color:var(--main-${iconType}-color-text)"</#if>>
                    <#nested>
                </div>
            </div>
            <div class="card-footer d-block d-sm-none text-center<#if class?contains('alert')> bg-transparent</#if>">
                <button class="btn btn-link btn-accordion" type="button" data-bs-toggle="collapse" data-bs-target="#collapseAcc${id}" aria-expanded="<#if state>true<#else>false</#if>" aria-controls="collapseAcc${id}">
                    <svg class="paris-icon paris-icon-arrow-bottom" aria-hidden="true" focusable="false">
                        <use xlink:href="#paris-icon-arrow-bottom"></use>
                    </svg>
                </button>	
            </div>
        </#if>
    </div>
</div>
</#macro>