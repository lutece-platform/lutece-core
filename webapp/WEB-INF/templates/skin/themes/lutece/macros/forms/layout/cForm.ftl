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
</#macro>