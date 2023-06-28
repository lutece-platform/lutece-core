<#-- Macro: adminLanguage
Description: Generates a language selection form for use in an admin panel. It generates a Bootstrap form that allows the user to select a language from a list of supported languages.
Parameters:
- languages (object): an object that contains information about the supported languages. It should be an array of objects, where each object represents a language and contains a `code` and a `name` property.
- lang (string): the code of the currently selected language.
- action (string, optional): the URL of the form submission handler.
-->
<#macro adminLanguage languages lang action='jsp/admin/DoChangeLanguage.jsp' >
<@tform method='post' action=action class='form-inline dropdown-item nolink clearfix'>
	<@input type='hidden' name='token' value='${token}' />
	<@p class='mb-0'>#i18n{portal.admin.admin_home.language}</@p>
	<@div class='d-flex flex-wrap'>
	<#list languages as language>
		<#if lang=language.code>
			<#assign isLocale='bg-secondary fw-semibold'>
			<#assign title='<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" stroke-width="1" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"></path><path d="M20.946 12.99a9 9 0 1 0 -9.46 7.995"></path><path d="M3.6 9h16.8"></path><path d="M3.6 15h13.9"></path><path d="M11.5 3a17 17 0 0 0 0 18"></path><path d="M12.5 3a16.997 16.997 0 0 1 2.311 12.001"></path><path stroke="green" d="M15 19l2 2l4 -4" stroke-width="3"></path></svg>'>
		<#else>
			<#assign isLocale=''>
			<#assign title='<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" stroke-width="1" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"></path><path d="M3 12a9 9 0 1 0 18 0a9 9 0 0 0 -18 0"></path><path d="M3.6 9h16.8"></path><path d="M3.6 15h16.8"></path><path d="M11.5 3a17 17 0 0 0 0 18"></path><path d="M12.5 3a17 17 0 0 1 0 18"></path></svg>'>
		</#if>
		<@button color='' class='small p-0 px-2 mt-1 ${isLocale}' type='submit' name='language' value='${language.code}' title='${title} ${language.code} ' tooltip='${language.code}'  />
	</#list>
	</@div>
</@tform>
</#macro>