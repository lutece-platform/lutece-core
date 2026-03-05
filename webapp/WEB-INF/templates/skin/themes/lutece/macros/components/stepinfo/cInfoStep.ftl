<#--
Macro: cInfoStepOld

Description: Generates a legacy horizontal info stepper showing numbered procedural steps with optional pictograms and expandable content. Deprecated: use cInfoStep instead.

Parameters:
- steps (object, required): Collection of step objects with properties: url, title, content, status, class.
- haspicto (boolean, optional): Whether to display step pictogram images. Default: false.
- hasidx (boolean, optional): Whether to display index numbers as bullets. Default: false.
- title (boolean, optional): Whether to display step titles with heading style. Default: false.
- showMore (number, optional): Character threshold for truncating step content (0 for no truncation). Default: 0.
- labelMore (string, optional): Label for the "show more" button. Default: i18n("theme.labelShowMore").
- a11StatusMsg (string, optional): Accessible status message for screen readers. Default: ''.
- class (string, optional): Additional CSS classes. Default: ''.
- imgClass (string, optional): CSS class for step pictogram images. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Snippet:

    Basic usage (deprecated, prefer cInfoStep):

    <@cInfoStepOld steps=mySteps haspicto=true title=true />

-->
<#--
Macro: cInfoStep

Description: Generates a responsive info stepper displaying numbered procedural steps with optional pictograms, supporting both horizontal and vertical layouts.

Parameters:
- steps (object, required): Collection of step objects with properties: url, title, content.
- haspicto (boolean, optional): Whether to display step pictogram images. Default: false.
- verticalStepper (boolean, optional): Whether to display the stepper vertically. Default: false.
- title (boolean, optional): Whether to display step titles with heading style. Default: false.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- imgClass (string, optional): CSS class for pictogram images. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Snippet:

    Basic horizontal stepper:

    <@cInfoStep steps=procedureSteps />

    Stepper with pictograms and titles:

    <@cInfoStep steps=procedureSteps haspicto=true title=true />

    Vertical stepper:

    <@cInfoStep steps=procedureSteps verticalStepper=true title=true />

-->

<#macro cInfoStepOld steps haspicto=false hasidx=false title=false showMore=0 labelMore=i18n("theme.labelShowMore") a11StatusMsg='' class='' imgClass='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="info-step">
    <#if steps?has_content>
        <#if haspicto>
        <ol class="d-flex info-step-figure">
        <#list steps as step>
        <li>
            <figure<#if imgClass!=''> class="${imgClass!}"</#if>>
                <img src="${step.url!}" class="img-fluid" title="${step.title!}" alt="${step.title!}" aria-hidden="true">
            </figure>
        </li>
        </#list>
        </ol>
    </#if>
    </#if>
    <ol class="info-step-list<#if hasidx> list-idx</#if><#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> >
    <#if steps?has_content>
        <#list steps as step>
            <li<#if step.status??> class="${step.status}"</#if>>
                <div class="idx" data-idx="${step?index + 1}" aria-hidden="true">
                    <span>${step?index + 1}</span>
                </div>
                <#if step.content??>
                    <#if showMore gt 0>
                        <#local content>
                        <#if step.content?length gt showMore>
                            <#assign truncated=step.content?truncate_w(showMore, '') >
                            ${truncated}<span class="ellipsis">...</span>
                            <span class="extra visually-hidden">${step.content?remove_beginning(truncated)}</span>
                            <span class="d-block text-center mt-2">
                                <@cLink href='#' nestedPos='before' class='btn-infostep-more main-color text-decoration-none h6' label=labelMore title=labelMore >
                                    <@cIcon label=i18n("portal.util.labelBack") class='angle-up hidden' />
                                </@cLink>
                            </span>
                        <#else>
                        ${step.content}
                        </#if>
                        </#local>
                    </#if>
                </#if>
                <#if title>
                    <div class="info-step-title">
                        <h2 id="step-title-${step?index + 1}" class="h3 text-center font-weight-bold mt-2">${step.title!}</h2>
                        <#if step.content??><p class="infostep-more">${content!}</p></#if>
                    </div>
                <#else>
                    <p id="step-title-${step?index + 1}" class="mt-2">${step.title!}<#if step.content??> ${content!}</#if></p>
                </#if>
            </li>
            <li class="bs-infostep-container"><div class="bs-infostep-line"></div></li>
        </#list>
    </#if>
    <#nested>
    </ol>
</div>
</#macro>
<#macro cInfoStep steps haspicto=false verticalStepper=false title=false class='' id='' imgClass='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="c-info-step-wrapper<#if verticalStepper> stepper-vertical<#else> stepper-horizontal c-info-step-default</#if><#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> >
  <#if steps?has_content>
      <#if haspicto && !verticalStepper>
          <ol class="c-info-step-icons<#if !verticalStepper> d-none d-md-flex</#if>">
            <#list steps as step>
                <li class="c-info-step-icons__item">
                    <figure <#if imgClass!=''> class="${imgClass!}"</#if> >
                        <img src="${step.url!}" class="img-fluid" alt="">
                    </figure>
                </li>
            </#list>
          </ol>
      </#if>
  </#if>
  <ol class="c-info-step" >
    <#if steps?has_content>
        <#list steps as step>
            <li class="c-info-step__item">
                <#if haspicto>
                    <div class="c-info-step__icon<#if !verticalStepper> d-md-none</#if>">
                        <figure class="mb-0<#if imgClass!=''> ${imgClass!}</#if>">
                            <img src="${step.url!}" class="img-fluid" alt="">
                        </figure>
                    </div>
                </#if>
                <div class="c-info-step__content">
                    <#if title>
                        <h3 class="c-info-step__title">${step.title!}</h3>
                        <#if step.content??>
                            <p class="c-info-step__desc mb-0">${step.content!}</p>
                        </#if>
                    <#else>
                        <#if step.content??>
                            <p class="c-info-step__desc">${step.content!}</p>
                        </#if>
                    </#if>
                </div>
            </li>
        </#list>
    </#if>
  </ol>
</div>
<#if !verticalStepper>
<script>
    function handleResize() {
        const width = window.innerWidth;
        const elements = document.querySelectorAll('.c-info-step-default');

        elements.forEach(function(element) {
            if (width <= 767) {
                element.classList.remove('stepper-horizontal');
                element.classList.add('stepper-vertical');
            } else {
                element.classList.remove('stepper-vertical');
                element.classList.add('stepper-horizontal');
            }
        });
    }

    window.addEventListener('resize', handleResize);
    handleResize();
</script>
</#if>
</#macro>