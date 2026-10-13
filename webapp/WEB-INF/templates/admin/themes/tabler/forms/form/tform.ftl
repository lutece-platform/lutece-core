<#-- Macro: tform

Description: Generates a Bootstrap form element with specified properties.

Parameters:
- type (string, optional): the type of form (horizontal, inline, or blank).
- class (string, optional): additional CSS classes to add to the form element.
- align (string, optional): the alignment of the form element (left, right, or center).
- hide (list, optional): a list of form properties to hide.
- action (string, optional): the URL to submit the form to.
- method (string, optional): the HTTP method to use when submitting the form (get or post).
- name (string, optional): the name of the form element.
- id (string, optional): the ID of the form element.
- role (string, optional): the role of the form element.
- collapsed (boolean, optional): whether to collapse the form element.
- enctype (string, optional): the encoding type for the form element.
- params (string, optional): additional parameters to add to the form element.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Basic form with POST method:

    <@tform action='jsp/admin/DoAction.jsp' method='post' name='myForm' id='myForm'>
        <@formGroup labelKey='#i18n{portal.users.label.name}' labelFor='name' formStyle='horizontal'>
            <@input type='text' name='name' id='name' />
        </@formGroup>
        <@button type='submit' buttonIcon='check' color='primary'>Save</@button>
    </@tform>

    Inline form with alignment:

    <@tform type='inline' action='jsp/admin/DoSearch.jsp' method='post' id='searchForm'>
        <@input type='text' name='query' id='query' placeHolder='Search...' />
        <@button type='submit' color='primary' buttonIcon='search' />
    </@tform>

    Form with file upload and required fields indicator:

    <@tform action='jsp/admin/DoUpload.jsp' method='post' enctype='multipart/form-data' required=true id='uploadForm'>
        <@formGroup labelKey='#i18n{portal.upload.label.file}' labelFor='file' formStyle='horizontal'>
            <@input type='file' name='file' id='file' mandatory=true />
        </@formGroup>
        <@button type='submit' color='primary'>Upload</@button>
    </@tform>

-->
<#macro tform type='' class='' align='' hide=[] required=false action='' method='post' name='' id='' role='' collapsed=false enctype='' boxed=false boxClass='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />	
<#local classExtra = alignmentSettings( align, '' ) + ' ' + displaySettings( hide, ( type == 'inline' )?then( 'inline-flex', 'block' ) ) />
<#if collapsed><#local classExtra += ' collapse' /></#if>
<#switch type>
	<#case 'inline'>
		<#local classExtra += ' d-inline-flex align-items-center'>
		<#break>
	<#case 'flex'>
		<#local classExtra += ' d-flex align-items-center'>
		<#break>
</#switch>
<#if align == 'middle'><#local classExtra += ' align-middle' /></#if>
<#if boxed>
<div class="card<#if boxClass?has_content> ${boxClass!}</#if>">
<div class="card-body"></#if>
<form<@commonAttrs id=id class=class classExtra=classExtra params=params attrs=attrs /><#if action?has_content> action="${action}"</#if><#if method?has_content> method="${method}"</#if><#if name?has_content> name="${name}"</#if><#if role?has_content> role="${role}"</#if><#if method='post' && enctype?has_content> enctype='${enctype}'</#if>>
<#if required><@staticText>#i18n{portal.util.message.titleRequiredFields}</@staticText></#if>
<#nested>
</form>
<#if boxed></div></div></#if>
</#macro>