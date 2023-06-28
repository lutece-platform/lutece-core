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
	<@p class='mb-0 d-flex justify-content-between' >
		#i18n{portal.admin.admin_home.language}
		<#list languages?filter( language -> language.code = lang  ) as language>
			<@span class='py-0 ps-4 pe-2 m-1 fw-bold border border-2 border-dark rounded-pill' params='style="background: transparent url( ./themes/admin/shared/css/vendor/tabler/img/flags/${language.code}.svg ) no-repeat .25rem center; background-size: .75rem"' >${language.code}</@span>
		</#list>
	</@p>
	<@div class='d-flex flex-wrap'>
	<#list languages?filter( language -> language.code != lang  ) as language>
		<@button color='' class='small py-0 ps-4 pe-1 m-1' type='submit' name='language' value='${language.code}' title='${language.code} ' tooltip='${language.code}' params='style="background: transparent url( ./themes/admin/shared/css/vendor/tabler/img/flags/${language.code}.svg ) no-repeat .25rem center; background-size: .75rem"' />
	</#list>
	</@div>
</@tform>
</#macro>