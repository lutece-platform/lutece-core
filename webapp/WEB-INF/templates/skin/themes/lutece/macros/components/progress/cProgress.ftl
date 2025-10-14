<#-- Macro: cProgress

Description: affiche un tag de status.

Parameters:
@param - label - string - optional - permet de définir le libellé du tag
@param - labelClass - string - optional - permet d'ajouter une classe CSS au libellé du tag
@param - color - string - optional - Couleur par défaut de la barre (par défaut: 'primary')
@param - value - number - optional - Valeur d'initialisation (par défaut: 0)
@param - min - number - optional - valeur min (par défaut: 0)
@param - max - number - optional - valeur max (par défaut: 100)
@param - token - string - optional - the progress manager Feed Token (par défaut: '')
@param - text - string - optional - Texte à ajouter à la barre de progression (par défaut: '')
@param - role - string - optional - role accessibilité (par défaut: 'progressbar')
@param - showReport - boolean - optional - display the messages report (if the feed token is provided), par defaut "false"
@param - intervalTime - number - optional - refresh period in milli seconds (if the feed token is provided) 
@param - id - string - optional - l'ID du tag
@param - class - string - optional - permet d'ajouter une classe CSS au tag
@param - params - string - optional - permet d'ajouter des parametres HTML au tag
-->  
<#macro cProgress label labelClass='' class='' color='primary' id='' params='' value=0 min=0 max=100 text='' progressId='progressbar' token='' role='progressbar' showReport=false intervalTime=2000  deprecated...>
<@deprecatedWarning args=deprecated />
<#if max?number != 100><#assign progPercent=( (value?number / max?number ) * 100) /><#else><#assign progPercent=value /></#if>
<p id="${progressId}-label" class="label-progress<#if labelClass!=''> ${labelClass}</#if>">${label}</p>
<#if role='progressbar'>
<div class="progress<#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
    <div id="${progressId}" aria-labelledby="${progressId}-label" class="progress-bar bg-${color}<#if token!=''> progressmanager</#if>" <#if role!=''>role="${role}"</#if> style="width:${progPercent?replace(',','.')}%;" <#if role='progressbar'>aria-valuenow="${value}"  aria-valuemin="${min}" aria-valuemax="${max}"</#if> <#if token!=''>token="${token}" intervalTime=${intervalTime} showReport=${showReport?c}</#if> >
        <#if text=''>${progPercent}%<#else>${text}</#if>
    </div>
</div>
<#else>
<div class="progress<#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
    <p id="${progressId}" aria-hidden="true" class="progress-bar bg-${color}<#if token!=''> progressmanager</#if>" style="width:${progPercent?replace(',','.')}%;">
        <#if text=''>${progPercent}%<#else>${text}</#if>
    </p>
</div>
</#if>
<#if showReport ><p id="${progressId}-report" class="progress-bar-report" lastline=0></p></#if>
</#macro>