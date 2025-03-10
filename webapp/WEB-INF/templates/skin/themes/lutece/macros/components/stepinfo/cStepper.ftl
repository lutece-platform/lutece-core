<#-- Macro: cStepper

Description: permet d'afficher les différentes étapes d'une procédure à suivre.

Parameters:

@param - id - string - optional - l'ID du stepper
@param - class - string - optional - ajoute une classe CSS au stepper
@param - steps - object - required - définit l'objet comprenant les informations pour chaque étape
@param - haspicto - boolean - optional - permet de définir si le stepper doit afficher les images comprises dans l'objet 'steps' (par défaut: false)
@param - hasidx - boolean - optional - affiche un index sous forme de puce sur une étape (par défaut: false)
@param - title - boolean - optional - affiche le contenu de 'title' de l'objet 'steps' avec un style de titre (par défaut: false)
@param - showMore - boolean - optional - permet d'afficher un bouton pour afficher le contenu de steps (par defaut: false)
@param - labelMore - string - optional - Libellé du bouton pour afficher le contenu (par défaut: i18n("theme.labelShowMore"))
@param - a11StatusMsg - string - optional - Libellé pour accessibilité inclu un texte de statut masqué
@param - params - string - optional - permet d'ajouter des parametres HTML au stepper
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