<#-- Macro: adminLanguage
Description: Generates a language selection form for use in an admin panel. It generates a Bootstrap form that allows the user to select a language from a list of supported languages.
Parameters:
- languages (object): an object that contains information about the supported languages. It should be an array of objects, where each object represents a language and contains a `code` and a `name` property.
- lang (string): the code of the currently selected language.
- action (string, optional): the URL of the form submission handler.
-->
<#macro adminLanguage languages lang action='jsp/admin/DoChangeLanguage.jsp'>
	<#assign flagCodeSelected=lang>
	<#if flagCodeSelected=='en'>
		<#assign flagCodeSelected='gb'>
	</#if>
	<li class="d-none d-sm-block nav-item dropdown">
		<a id="btn-lang" class="border btn btn-light btn-rounded" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false" title="#i18n{portal.admin.admin_home.language}">
			<div class="position-absolute" style="background:url('themes/admin/shared/css/vendor/tabler/img/flags/${flagCodeSelected}.svg');background-size:contain;background-position: center;height:25px;width:25px;border-radius:25px"></div>
		</a>
		<ul class="dropdown-menu p-3 text-center dropdown-menu-center">
			<span class="text-muted">#i18n{portal.admin.admin_home.language}s</span>
			<#list languages as language>
				<#assign flagCode=language.code>
				<#if flagCode=='en'>
					<#assign flagCode='gb'>
				</#if>
				<#if flagCodeSelected=flagCode>
					<#assign isLocale='border-primary border-3'>
					<#assign title='#i18n{portal.admin_home.button.selectedLanguage}'>
				<#else>
					<#assign isLocale=''>
					<#assign title=''>
				</#if>
				<a href='${action}?token=${token}&language=${language.code}' class='border btn btn-light btn-rounded ${isLocale} mx-auto mt-2' style="background:url('themes/admin/shared/css/vendor/tabler/img/flags/${flagCode}.svg');background-size:contain;background-position: center;"></a>
			</#list>
		</ul>
	</li>
</#macro>