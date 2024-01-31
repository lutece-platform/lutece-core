<#-- Macro: adminLanguage
Description: Generates a language selection form for use in an admin panel. It generates a Bootstrap form that allows the user to select a language from a list of supported languages.
Parameters:
- languages (object): an object that contains information about the supported languages. It should be an array of objects, where each object represents a language and contains a `code` and a `name` property.
- lang (string): the code of the currently selected language.
- action (string, optional): the URL of the form submission handler.
-->
<#macro adminLanguage languages lang action='jsp/admin/DoChangeLanguage.jsp'>
<li class="nav-item dropdown" id="lutece-language-menu">
	<#list languages?filter( language -> language.code == lang ) as language>
	<a id="btn-lang" class="border btn btn-light btn-rounded" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false" title="#i18n{portal.admin.admin_home.language}" data-lang="${language.code}" data-lang-name="${language.name?capitalize}">
		<div class="position-absolute" style="background:url('themes/admin/shared/css/vendor/tabler/img/flags/${lang}.svg');background-size:contain;background-position: center;height:25px;width:25px;border-radius:25px"></div>
	</a>
	</#list>
	<ul class="dropdown-menu p-3 text-center dropdown-menu-center">
		<span class="text-muted">#i18n{portal.admin.admin_home.language}s</span>
		<#list languages?filter( language -> language.code != lang ) as language>
			<a href='${action}?token=${token}&language=${language.code}' class='border btn btn-light btn-rounded mx-auto mt-2' style="background:url('themes/admin/shared/css/vendor/tabler/img/flags/${language.code}.svg');background-size:contain;background-position: center;" data-lang="${language.code}" data-lang-name="${language.name?capitalize}">
				<span class="visually-hidden">#i18n{portal.admin.admin_home.button.changeLanguage} ${language.name}</span>
			</a>
		</#list>
	</ul>
</li>
</#macro>
<#-- Macro: adminReadMode
Description: Show read direction button
Parameters:
-->
<#macro adminReadMode>
<li class="nav-item d-none d-xl-flex">
     <a class="border btn btn-light btn-rounded btn-readmode" href="" id="lutece-rtl" title="#i18n{portal.site.site_property.layout.readmode.checkbox}" data-bs-toggle="tooltip" data-bs-placement="right" data-bs-animation="false" data-bs-original-title="#i18n{portal.site.site_property.layout.readmode.checkbox}" aria-label="#i18n{portal.site.site_property.layout.readmode.checkbox}"> 
	 	<i class="fs-5 ti ti-text-direction-rtl mx-auto align-self-center"></i>
        <span class="visually-hidden">#i18n{portal.site.site_property.layout.readmode.checkbox}</span>
	 </a>
</li>
</#macro>