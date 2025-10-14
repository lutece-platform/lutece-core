<#-- Macro: cTextArea

Description: permet de définir un champs de formulaire textarea.

Parameters:

@param - name - string - required - permet de définir la valeur de l'attribut 'name' du champs du formulaire
@param - class - string - optional - ajoute une classe CSS au champs de formulaire (par défaut: 'form-control')
@param - id - string - optional - l'ID du champs de formulaire
@param - rows - number - optional - permet de modifier le nombre ligne du champs de formulaire, par défaut 0
@param - placeholder - string - optional - permet de définir la valeur de l'attribut 'placeholder' du champs du formulaire
@param - autocomplete- string - Default '' , autocomplete pour le textarea peut être on/off https://developer.mozilla.org/fr/docs/Web/HTML/Attributes/autocomplete  
@param - required - boolean - optional - permet d'indiquer si le champs est obligatoire (par défaut: false)
@param - html5Required - boolean - optional - permet d'indiquer si le champs doit utliser l'attribut html5 required (par défaut: true)
@param - disabled - boolean - optional - permet d'indiquer si le champs est desactivé (par défaut: false)
@param - readonly - boolean - optional - permet d'indiquer si le champs est lecture seule (par défaut: false)
@param - title - string - optional - permet de définir l'attribut 'title' du champs
@param - maxlength - number - optional - permet de saisir une limitation de taille de valeur saisie (par défaut: 0)
@param - helpMsg - string - optional - permet de définir le message d'aide du champs
@param - errorMsg - string - optional - permet de définir le message d'erreur du champs
@param - params - string - optional - permet d'ajouter des parametres HTML au champs de formulaire
-->
<#macro cTextArea name class='form-control' id='' placeholder='' required=false html5Required=true disabled=false readonly=false title='' autocomplete='' maxlength=0 helpMsg='' rows=0 errorMsg='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#assign idMsg><#if id!=''>${id}<#else>${name!}</#if></#assign>
<#if helpMsg !=''><@cFormHelp idMsg helpMsg /></#if>
<textarea class="<#if class!=''> ${class!}</#if><#if errorMsg!=''> is-invalid</#if>" name="${name!}" id="<#if id!=''>${id}<#else>${name!}</#if>" <#if placeholder!=''> placeholder="${placeholder!}"</#if><#if autocomplete!=''> autocomplete="${autocomplete!}"</#if><#if title!=''> title="${title}"</#if><#if maxlength?number gt 0> maxlength="${maxlength!}"</#if><#if disabled> disabled</#if><#if readonly> readonly</#if><#if params!=''> ${params}</#if><#if rows?number!=0>rows="${rows}"</#if><#if required> <#if html5Required>required</#if> aria-required="true"</#if><#if errorMsg!=''> is-invalid aria-invalid="true" aria-describedby="error_<#if id!=''>${id!}<#else>${name}</#if>"<#elseif helpMsg!=''> aria-describedby="help_<#if id!=''>${id!}<#else>${name}</#if>"</#if>>
<#nested>
</textarea>
<#if errorMsg !=''><@cFormError idMsg errorMsg /></#if>
</#macro>