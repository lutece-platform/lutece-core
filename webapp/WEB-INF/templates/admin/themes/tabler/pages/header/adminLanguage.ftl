<#-- Macro: adminLanguage
Description: Generates a language selection form for use in an admin panel. It generates a Bootstrap form that allows the user to select a language from a list of supported languages.
Parameters:
- languages (object): an object that contains information about the supported languages. It should be an array of objects, where each object represents a language and contains a `code` and a `name` property.
- lang (string): the code of the currently selected language.
- action (string, optional): the URL of the form submission handler.
-->
<#macro adminLanguage languages lang action='jsp/admin/DoChangeLanguage.jsp' >
<#list languages?filter( language -> language.code == lang ) as language>
<div class="dropdown-item justify-content-center">
	<span title="#i18n{portal.admin.admin_home.language} ${language.name!}" data-lang="${language.code}" data-lang-name="${language.name?capitalize}">
		<div style="background:url('themes/admin/shared/css/vendor/tabler/img/flags/${lang}.svg');background-size:contain;background-position:center;height:25px;width:25px;border-radius:25px"></div>
	</span>
</div>	
</#list>
<#list languages?filter( language -> language.code != lang ) as language>
<div class="dropdown-item justify-content-center">
	<a href="${action}?token=${token}&language=${language.code}" class="nav-link" data-lang="${language.code}" data-lang-name="${language.name?capitalize}">
		<div style="background:url('themes/admin/shared/css/vendor/tabler/img/flags/${language.code}.svg');background-size:contain;background-position:center;height:25px;width:25px;border-radius:25px"></div>
		<span class="visually-hidden">#i18n{portal.admin.admin_home.button.changeLanguage} ${language.name}</span>
	</a>
</div>
</#list>
</#macro>
<#-- Macro: adminReadMode
Description: Show read direction button
Parameters:
-->
<#macro adminReadMode>
<div class="nav-item dropdown me-2" id="lutece-rtl">
	<div class="nav-link d-none d-lg-flex" tabindex="0" role="button" title="#i18n{portal.site.site_property.layout.readmode.checkbox}">
		<i class="ti ti-text-direction-rtl"></i> <span class="visually-hidden">#i18n{portal.site.site_property.layout.readmode.checkbox}</span>
	</div>
	<div class="nav-link d-flex d-lg-none" tabindex="0" role="button" title="#i18n{portal.site.site_property.layout.readmode.checkbox}">
		<i class="ti ti-text-direction-rtl  me-2"></i> #i18n{portal.site.site_property.layout.readmode.checkbox}
	</div>
</div>
</#macro>