<#-- Macro: adminLanguage
Description: Generates a language selection form for use in an admin panel. It generates a Bootstrap form that allows the user to select a language from a list of supported languages.
Parameters:
- languages (object): an object that contains information about the supported languages. It should be an array of objects, where each object represents a language and contains a `code` and a `name` property.
- lang (string): the code of the currently selected language.
- action (string, optional): the URL of the form submission handler.
-->
<#macro adminLanguage languages lang action='jsp/admin/DoChangeLanguage.jsp' >
<@tform method='post' action=action class='form-inline clearfix'>
	<@input type='hidden' name='token' value='${token}' />
	<@row>
		<@columns id='lutece-language-menu'>
			<#list languages?filter( language -> language.code == lang  ) as language>
				<@div id="lutece-default-language" class=" ps-1">
					<@icon style='language' /> #i18n{portal.admin.admin_home.language}
					<@span class='ms-2 py-1 ps-4 pe-2 px-2 fw-bolder border border-2 border-muted rounded-pill' params='tabindex="0" style="background: transparent url( ./themes/admin/shared/css/vendor/tabler/img/flags/${lang}.svg ) no-repeat .65rem center; background-size: .75rem"'>
						${language.name!?capitalize}
					</@span>
				</@div>
			</#list>
			<@div id='lutece-languages'>
			<#list languages?filter( language -> language.code != lang  ) as language>
				<@button color='secondary btn-language' type='submit' name='language' value='${language.code}' title='${language.name?capitalize}' hideTitle=['all'] params='style="background-image: url( ./themes/admin/shared/css/vendor/tabler/img/flags/${language.code}.svg );"' />
			</#list>
			</@div>
		</@columns>
	</@row>
</@tform>
</#macro>
<#-- Macro: adminReadMode
Description: Show read direction button
Parameters:
-->
<#macro adminReadMode>
<li class="nav-item dropdown me-2" id="lutece-rtl">
	<div class="nav-link d-none d-lg-flex" tabindex="0" role="button" title="#i18n{portal.site.site_property.layout.readmode.checkbox}">
		<i class="ti ti-text-direction-rtl"></i> <span class="visually-hidden">#i18n{portal.site.site_property.layout.readmode.checkbox}</span>
	</div>
	<div class="nav-link d-flex d-lg-none" tabindex="0" role="button" title="#i18n{portal.site.site_property.layout.readmode.checkbox}">
		<i class="ti ti-text-direction-rtl  me-2"></i> #i18n{portal.site.site_property.layout.readmode.checkbox}
	</div>
</li>
</#macro>