<#--
Macro: cStepper

Description: Generates a multi-step progress stepper displaying sequential steps with status indicators, pictograms, and expandable content.

Parameters:
- steps (object, required): Collection of step objects with properties: title, content, status ('done', 'in-progress', ''), url.
- haspicto (boolean, optional): Whether to display step pictogram images. Default: false.
- hasidx (boolean, optional): Whether to display numbered index bullets. Default: false.
- title (boolean, optional): Whether to display step titles with heading style. Default: false.
- showMore (boolean, optional): Whether to display a "show more" button for step content. Default: false.
- labelMore (string, optional): Label for the expand button. Default: i18n("theme.labelShowMore").
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

<#macro cStepper steps=steps haspicto=false hasidx=false title=false showMore=false labelMore=i18n("theme.labelShowMore") a11StatusMsg='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="stepper">
    <ol class="stepper-list <#if hasidx> list-idx</#if><#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> >
    <#if steps?has_content>
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
	                    <figure class="p-3" data-idx="${step?index + 1}" aria-hidden="true">
	                        <img src="${step.url!}" class="img-fluid" title="" alt="${step.title!}" aria-hidden="true">
	                    </figure>
	                <#else>
	                    <div class="idx" data-idx="${step?index + 1}" aria-hidden="true">
	                    	<#if step.status?? && (step.status == 'done')>
	                    		<@parisIcon name='check' title='' class='main-color' params='aria-label="Etape Validée"' />
	                    	<#else>
	                        	<span>${step?index + 1}</span>
	                        </#if>
	                    </div>
	                </#if>
	                <#if step.content??>
	                    <#if showMore>
	                        <#local content>
	                        	<div class="infostep-more">
		                            <span class="extra visually-hidden">${step.content}</span>
		                         </div>
		                        <button type='button' class='btn btn-infostep-more main-color' aria-label="Afficher le contenu">
                                    <@parisIcon name='plus' />
                                    <span class="button-label">${labelMore}</span>
                                </button>
	                        </#local>
	                    </#if>
	                </#if>
	                <#if title>
	                    <div class="stepper-title">
	                        <h2 id="step-title-${step?index + 1}" class="h3 text-center font-weight-bold mt-2">${step.title!}</h2>
	                        <#if step.content??><p class="infostep-more">${content!}</p></#if>
	                    </div>
	                <#else>
	                    <p id="step-title-${step?index + 1}" class="mt-2">${step.title!}<#if step.content??> ${content!}</#if></p>
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