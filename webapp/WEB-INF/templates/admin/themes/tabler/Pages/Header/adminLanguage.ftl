<#-- Macro: adminLanguage
Description: Generates a language selection form for use in an admin panel. It generates a Bootstrap form that allows the user to select a language from a list of supported languages.
Parameters:
- languages (object): an object that contains information about the supported languages. It should be an array of objects, where each object represents a language and contains a `code` and a `name` property.
- lang (string): the code of the currently selected language.
- action (string, optional): the URL of the form submission handler.
-->
<#macro adminLanguage languages lang action='jsp/admin/DoChangeLanguage.jsp' >
<@tform method='post' action=action class='form-inline'>
	<@input type='hidden' name='token' value='${token}' />
	<@row>
		<@columns class='py-1' params='style="padding-left:1.15rem"' >
			<@icon style='language' /> #i18n{portal.admin.admin_home.language}
			<#list languages as language>
				<#if lang=language.code>
					<#assign islocale='check-circle text-success pl-4'>
					<#assign title=' Selectionné '>
				<#else>
					<#assign islocale=''>
					<#assign title=''>
				</#if>
				<@button color='' class='btn-language ${language.code}' type='submit' name='language' value='${language.code}' title='${language.name?capitalize}${title}' buttonIcon='${islocale}' hideTitle=['all'] />
			</#list>
		</@columns>
	</@row>
</@tform>
</#macro>