<#--
Macro: cStepper

Description: Generates a multi-step progress stepper displaying sequential steps with status indicators, pictograms, and expandable content.

Parameters:
- steps (object, required): Collection of step objects with properties: title, content, status ('done', 'in-progress', ''), url.
- haspicto (boolean, optional): Whether to display step pictogram images. Default: false.
- hasidx (boolean, optional): Whether to display numbered index bullets. Default: false.
- title (boolean, optional): Whether to display step titles with heading style. Default: false.
- showMore (boolean, optional): Whether to display a "show more" button for step content. Default: false.
- labelMore (string, optional): Label for the expand button. Default: i18n("portal.theme.labelShowMore").
- a11StatusMsg (string, optional): Accessible status message for the active step. Default: ''.
- class (string, optional): Additional CSS classes (use 'pie' for pie-chart style). Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Showcase:
- desc: Stepper - @cStepper
- newFeature: false

Snippet:

    Basic stepper:

    <@cStepper steps=mySteps />

    Stepper with titles and expandable content:

    <@cStepper steps=mySteps title=true showMore=true hasidx=true />

    Stepper with pictograms:

    <@cStepper steps=mySteps haspicto=true title=true />

-->
<#macro cStepper steps=steps haspicto=false hasidx=false title=false titleLevel=3 showMore=false labelMore=i18n("portal.theme.labelShowMore") a11StatusMsg='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="stepper">
    <ol class="stepper-list <#if hasidx> list-idx</#if><#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> >
    <#if steps?has_content>
		<#assign nbSteps = steps?size >
        <#list steps as step>
	        <#if class='pie'>
	            <li class="step-${step?index + 1}<#if step.status!=''> ${step.status!}</#if>">
		            <span>${step?index + 1} - ${step.title!}</span>
		            <#if step.status?contains('active')><span class="visually-hidden">${a11StatusMsg}</span></#if>
	            </li>
	            <li class="bs-stepper-line"></li>
	        <#else>
	            <li<#if step.status??> class="${step.status}"</#if><#if step.status?? && (step.status == 'in-progress')>aria-current="step"</#if>>
				<#if haspicto>
					<figure class="p-3 mb-0" data-idx="${step?index + 1}">
						<img src="${step.url!}" class="img-fluid" alt="${step.title!}">
					</figure>
				<#else>
					<div class="idx" data-idx="${step?index + 1}" aria-hidden="true">
						<#if step.status?? && (step.status == 'done')>
							<@cIcon name='check' title='' class='main-color' params='aria-label="#i18n{portal.theme.labelStepDone}"' />
						<#else>
							<span>${step?index + 1}</span>
						</#if>
					</div>
				</#if>
					<p class="d-block d-sm-none text-secondary fs-5">${i18n('portal.theme.labelStepOf', step?index+1, nbSteps)}</p>
					<#local content>
					<div class="stepper-footer">
						<button type='button' class='btn btn-stepper-more main-color btn-mini'>
							<@cIcon name='plus' />
							<span class="button-label">${labelMore}</span>
						</button>
						<button type="button" class="btn btn-stepper-less main-color btn-mini">
							<@cIcon name='minus' />
							<span class="button-label">#i18n{portal.util.labelClose}</span>
						</button>
					</div>
					</#local>
				<#if title>
					<h${titleLevel} id="step-title-${step?index + 1}" class="stepper-title text-center font-weight-bold mt-2">${step.title!}</h${titleLevel}>
					<#if step.content??><div class="stepper-content<#if showMore> truncate</#if>">${step.content!}</div></#if>
					<#if step.content?? && showMore>${content!}</#if>
				<#else>
					<p id="step-title-${step?index + 1}" class="stepper-title">${step.title!}</p>
					<#if step.content??><div class="stepper-content<#if showMore> truncate</#if>">${step.content!}</div></#if>
					<#if step.content?? && showMore>${content!}</#if>
				</#if>
	            </li>
	            <li class="bs-stepper-container">
		            <#if step.status?? && (step.status == 'in-progress')>
		            	<div class="bs-stepper-line start"></div>
		            	<div class="bs-stepper-line end"></div>
		            <#else>
		            	<div class="bs-stepper-line <#if step.status??>${step.status}</#if>"></div>
		            </#if>
	            </li>
	        </#if>
        </#list>
    </#if>
    <#nested>
    </ol>
</div>
</#macro>