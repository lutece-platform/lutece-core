<#--
Macro: cForm

Description: Generates a `<form>` container with optional front-office validation. When `foValidation=true` (default), automatically loads the theme-form-validation and theme-form-observer modules and exposes a global `window.__formValidationConfig` with locale-aware error messages (i18n keys, overridable via datastore site properties).

Parameters:
- class (string, optional): CSS class applied to the form. Default: ''.
- id (string, optional): the ID of the form. Falls back to `name` when set. Default: ''.
- params (string, optional): additional HTML attributes added to the form. Default: ''.
- name (string, optional): value of the `name` attribute of the form. Default: ''.
- method (string, optional): value of the `method` attribute. Accepted values: 'post', 'get'. Default: 'post'.
- role (string, optional): value used as `aria-label` on the form for accessibility. Default: ''.
- action (string, optional): URL of the form action. Default: ''.
- enctype (string, optional): value of the `enctype` attribute (e.g. 'multipart/form-data' for file uploads). Default: ''.
- foValidation (boolean, optional): enables front-office form validation (loads validation modules and config). Default: true.

Snippet:

    Basic POST form:

    <@cForm action='jsp/site/Portal.jsp' method='post' name='contact'>
        <@cField label='#i18n{site.contact.name}'>
            <@cInput type='text' name='username' required=true />
        </@cField>
        <@cBtn type='submit' label='#i18n{site.contact.submit}' />
    </@cForm>

    File upload form with multipart enctype:

    <@cForm action='jsp/site/Portal.jsp?page=upload' method='post' enctype='multipart/form-data'>
        <@cInput type='file' name='attachment' />
        <@cBtn type='submit' label='#i18n{site.upload.send}' />
    </@cForm>

    Form with front-office validation disabled (server-side only):

    <@cForm action='jsp/site/Portal.jsp' method='post' foValidation=false>
        ...
    </@cForm>

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