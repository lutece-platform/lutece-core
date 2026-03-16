<#--
Macro: cForm

Description: Generates an HTML form container with configurable action, method, and ARIA attributes for front-office pages.

Parameters:
- class (string, optional): CSS class for the form. Default: ''.
- id (string, optional): the ID of the form. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.
- name (string, optional): the name attribute of the form. Default: ''.
- method (string, optional): the HTTP method. Default: 'post'.
- role (string, optional): ARIA label for the form. Default: ''.
- action (string, optional): the form action URL. Default: 'jsp/site/Portal.jsp'.

Showcase:
- desc: "Formulaire - @cForm"
- bs: "forms/overview"
- newFeature: false

Snippet:

    Basic form:

    <@cForm action='jsp/site/Portal.jsp'>
        <@cField label='Email' for='email' required=true>
            <@cInput name='email' id='email' type='email' />
        </@cField>
        <button type="submit" class="btn btn-primary">Submit</button>
    </@cForm>

    Named form with ARIA label:

    <@cForm name='contact_form' role='Contact form' action='jsp/site/Portal.jsp' params='enctype="multipart/form-data"'>
        <@cField label='Message' for='message'>
            <@cTextArea name='message' id='message' rows=4 />
        </@cField>
        <button type="submit" class="btn btn-primary">Send</button>
    </@cForm>

-->
<#macro cForm class='' id='' params='' name='' method='post' role='' action='jsp/site/Portal.jsp' deprecated...>
<@deprecatedWarning args=deprecated />
<form <#if class!=''>class="${class}"</#if> <#if id!=''> id="${id!name}"</#if><#if action!=''> action="${action}"</#if><#if method!=''> method="${method}"</#if><#if name!=''> name="${name}"</#if><#if role!=''> aria-label="${role}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</form>
<!-- Form validation config from datastore (loaded before modules) -->
<script>
window.__formValidationConfig = {
    errorClass: '${dskey("portal.theme.site_property.formvalidation.errorClass")!}',
    validClass: '${dskey("portal.theme.site_property.formvalidation.validClass")!}',
    errorFeedbackClass: '${dskey("portal.theme.site_property.formvalidation.errorFeedbackClass")!}',
    helpClass: '${dskey("portal.theme.site_property.formvalidation.helpClass")!}',
    errorIconSvg: '${dskey("portal.theme.site_property.formvalidation.errorIconSvg")?js_string!}',
    messages: {
        required: '${dskey("portal.theme.site_property.formvalidation.msg.required")!}',
        email: '${dskey("portal.theme.site_property.formvalidation.msg.email")!}',
        url: '${dskey("portal.theme.site_property.formvalidation.msg.url")!}',
        number: '${dskey("portal.theme.site_property.formvalidation.msg.number")!}',
        min: '${dskey("portal.theme.site_property.formvalidation.msg.min")!}',
        max: '${dskey("portal.theme.site_property.formvalidation.msg.max")!}',
        minlength: '${dskey("portal.theme.site_property.formvalidation.msg.minlength")!}',
        maxlength: '${dskey("portal.theme.site_property.formvalidation.msg.maxlength")!}',
        pattern: '${dskey("portal.theme.site_property.formvalidation.msg.pattern")!}',
        step: '${dskey("portal.theme.site_property.formvalidation.msg.step")!}',
        tel: '${dskey("portal.theme.site_property.formvalidation.msg.tel")!}',
        date: '${dskey("portal.theme.site_property.formvalidation.msg.date")!}',
        time: '${dskey("portal.theme.site_property.formvalidation.msg.time")!}',
        file: '${dskey("portal.theme.site_property.formvalidation.msg.file")!}',
        filetype: '${dskey("portal.theme.site_property.formvalidation.msg.filetype")!}',
        filesize: '${dskey("portal.theme.site_property.formvalidation.msg.filesize")!}',
        mismatch: '${dskey("portal.theme.site_property.formvalidation.msg.mismatch")!}',
        custom: '${dskey("portal.theme.site_property.formvalidation.msg.custom")!}'
    }
};
</script>
<#-- Form validation messages from datastore -->
<script type="module" src="${commonsSharedThemePath}${commonsSiteJsModulesPath}theme-form-validation.js"></script>
<script type="module" src="${commonsSharedThemePath}${commonsSiteJsModulesPath}theme-form-observer.js"></script>
</#macro>