<#-- Macro: cAccordion

Description: affiche une liste de titres empilés verticalement. Lorsqu’un titre est cliqué (ou activé par une interaction au clavier ou un lecteur d’écran), il révèle ou masque le contenu associé.

Parameters:
@param - id - string - required - l'ID de l'accordion
@param - title - string - required - le titre de l'accordion
@param - btnTitle - string - optional - (NEW) le titre du bouton de l'accordion
@param - class - string - optional - permet d'ajouter une classe CSS à l'accordion
@param - titleLevel - string - optional - définit le niveau de titre de l'accordion
@param - titleClass - string - optional - l'ajout de classe CSS au titre de l'accordion
@param - subTitle - string - optional - sous titre de l'accordion
@param - subTitleClass - string - optional - nom de classe pour le sous titre de l'accordion.
@param - btnClass - string - optional - nom de classe pour l'icon du bouton "collapse"
@param - btnShowLabel - string - optional - (NEW) gérer le texte du parametre "data-show-label" de l'accordion (par défaut: '#i18n{portal.theme.labelShowDetail}')
@param - btnHideLabel - string - optional - (NEW) gérer le texte du parametre "data-hide-label" de l'accordion (par défaut: '#i18n{portal.theme.labelHideDetail}')
@param - header - string - optional - contenu HTML ajouté au niveau du titre de l'accordion
@param - border - boolean - optional - si true une bordure est ajoutée (par défaut: false)
@param - state - boolean - optional - si true l'accordéon est déplié par défaut (par défaut: true)
@param - hasCollapse - boolean - optional - si true affiche l'icône pour le collapse (par défaut: true)
@param - params - string - optional - permet d'ajouter des parametres HTML à l'accordion
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