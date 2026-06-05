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
<#macro cStepper steps=steps haspicto=false hasidx=true title=false titleLevel=3 showMore=false labelMore=i18n("portal.theme.labelShowMore") a11StatusMsg='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<ul class="steps">
    <#if steps?has_content>
		<#assign nbSteps = steps?size >
    	<#list steps as step>
			<li class="step <#if hasidx> list-idx</#if><#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> >
			<#if hasidx><div class="step_num<#if step.status?? && step.status != ''> ${step.status}</#if>" >${step?index + 1}</div></#if>
			<#if haspicto>
				<figure class="p-3 mb-0" data-idx="${step?index + 1}">
					<img src="${step.url!}" class="img-fluid" alt="${step.title!}">
				</figure>
			</#if>
			<h${titleLevel}>${step.title!}</h${titleLevel}>
			<#if step.content??><p class="stepper-content<#if showMore> truncate</#if>">${step.content!}</p></#if>
			<#if step.content?? && showMore>
			<p class="step-footer">
				<button type='button' class='btn btn-stepper-more main-color btn-mini'>
					<@cIcon name='plus' />
					<span class="button-label">${labelMore}</span>
				</button>
				<button type="button" class="btn btn-stepper-less main-color btn-mini">
					<@cIcon name='minus' />
					<span class="button-label">#i18n{portal.util.labelClose}</span>
				</button>
			</p></#if>
		</li>
        </#list>
    </#if>
    <#nested>
</ul>
</#macro>