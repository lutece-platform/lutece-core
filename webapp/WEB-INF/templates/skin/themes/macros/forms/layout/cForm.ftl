<#-- Macro: cForm

Description: permet de définir le container d'un formulaire.

Parameters:

@param - id - string - optional - l'ID du formulaire
@param - class - string - optional - ajoute une classe CSS au formulaire
@param - method - string - optional - permet de définir la valeur de l'attribut 'method' du formulaire (par défaut: 'post')
@param - name - string - optional - permet de définir la valeur de l'attribut 'name' du formulaire
@param - role - string - optional - permet de définir la valeur de l'attribut 'aria-label' du formulaire
@param - action - string - optional - permet de définir l'url de l'action du formulaire
@param - enctype - string - optional - permet de définir la valeur de l'attribut 'enctype' du formulaire
@param - params - string - optional - permet d'ajouter des parametres HTML au formulaire
@param - foValidation - boolean - optional - permet d'activer la validation du formulaire côté front-office. Par défaut: true.
-->
<#macro cForm class='' id='' params='' name='' method='post' role='' action='' enctype='' foValidation=true deprecated...>
<@deprecatedWarning args=deprecated />
<form <#if class!=''>class="${class}"</#if> <#if id!=''> id="${id!name}"</#if><#if action!=''> action="${action}"</#if><#if method!=''> method="${method}"</#if><#if name!=''> name="${name}"</#if><#if role!=''> aria-label="${role}"</#if> data-form-theme-validation="${foValidation?c}"<#if params!=''> ${params}</#if>>
<#nested>
</form>
<#if foValidation>
<#-- Form validation config: i18n messages (locale-aware), with optional datastore override -->
<#-- Helper macro: use dskey value if non-empty, otherwise fall back to i18n key -->
<script>
window.__formValidationConfig = {
    errorClass: '${dskey("portal.theme.site_property.formvalidation.errorClass")!}',
    validClass: '${dskey("portal.theme.site_property.formvalidation.validClass")!}',
    errorFeedbackClass: '${dskey("portal.theme.site_property.formvalidation.errorFeedbackClass")!}',
    helpClass: '${dskey("portal.theme.site_property.formvalidation.helpClass")!}',
    errorIconSvg: '${(dskey("portal.theme.site_property.formvalidation.errorIconSvg")!"")?js_string}',
    messages: {
        required: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.required" "portal.theme.formvalidation.msg.required" />',
        email: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.email" "portal.theme.formvalidation.msg.email" />',
        url: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.url" "portal.theme.formvalidation.msg.url" />',
        number: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.number" "portal.theme.formvalidation.msg.number" />',
        min: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.min" "portal.theme.formvalidation.msg.min" />',
        max: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.max" "portal.theme.formvalidation.msg.max" />',
        minlength: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.minlength" "portal.theme.formvalidation.msg.minlength" />',
        maxlength: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.maxlength" "portal.theme.formvalidation.msg.maxlength" />',
        pattern: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.pattern" "portal.theme.formvalidation.msg.pattern" />',
        step: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.step" "portal.theme.formvalidation.msg.step" />',
        tel: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.tel" "portal.theme.formvalidation.msg.tel" />',
        date: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.date" "portal.theme.formvalidation.msg.date" />',
        time: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.time" "portal.theme.formvalidation.msg.time" />',
        file: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.file" "portal.theme.formvalidation.msg.file" />',
        filetype: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.filetype" "portal.theme.formvalidation.msg.filetype" />',
        filesize: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.filesize" "portal.theme.formvalidation.msg.filesize" />',
        mismatch: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.mismatch" "portal.theme.formvalidation.msg.mismatch" />',
        custom: '<@_fvMsg "portal.theme.site_property.formvalidation.msg.custom" "portal.theme.formvalidation.msg.custom" />'
    }
};
</script>
<script type="module" src="${commonsSharedThemePath}${commonsSiteJsModulesPath}theme-form-validation.js"></script>
<script type="module" src="${commonsSharedThemePath}${commonsSiteJsModulesPath}theme-form-observer.js"></script>
</#if>
</#macro>
<#macro _fvMsg dsKey i18nKey><#assign _ds = dskey(dsKey)!''><#if _ds?has_content && !_ds?starts_with('DS Value')>${_ds?js_string}<#else>#i18n{${i18nKey}}</#if></#macro>