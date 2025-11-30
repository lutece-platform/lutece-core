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
					<@span class='ms-2 py-1 ps-4 pe-2 px-2 fw-bolder border border-2 border-muted rounded-pill' params='tabindex="0" style="background: transparent url( ./themes/admin/shared/css/vendor/tabler/img/flags/${lang}.svg ) no-repeat .65rem center; background-size: .75rem" data-lang="${lang}" data-lang-name="${language.name?capitalize}"'>
						${language.name!?capitalize}
					</@span>
				</@div>
			</#list>
			<@div id='lutece-languages'>
			<#list languages?filter( language -> language.code != lang  ) as language>
				<@button color='secondary btn-language' type='submit' name='language' value='${language.code}' title='${language.name?capitalize}' hideTitle=['all'] params='style="background-image: url( ./themes/admin/shared/css/vendor/tabler/img/flags/${language.code}.svg );" data-lang="${language.code}" data-lang-name="${language.name?capitalize}"' />
			</#list>
			</@div>
		</@columns>
		<@columns id='lutece-sidebar-language-menu'>
			<li class="nav-item dropdown">
				<#list languages?filter( language -> language.code == lang ) as language>
				<a id="btn-lang" class="btn btn-muted btn-rounded" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false" title="#i18n{portal.admin.admin_home.language}" data-lang="${language.code}" data-lang-name="${language.name?capitalize}">
					<div class="position-absolute" style="background:url('themes/admin/shared/css/vendor/tabler/img/flags/${lang}.svg');background-size:contain;background-position: center;height:25px;width:25px;border-radius:25px"></div>
				</a>
				</#list>
				<ul class="dropdown-menu p-2 text-center dropdown-menu-center">
					<#list languages?filter( language -> language.code != lang ) as language>
						<a href='${action}?token=${token}&language=${language.code}' class='border btn btn-light btn-rounded mx-auto mt-2' style="background:url('themes/admin/shared/css/vendor/tabler/img/flags/${language.code}.svg');background-size:contain;background-position: center;background-repeat: no-repeat;" data-lang="${language.code}" data-lang-name="${language.name?capitalize}">
							<span class="visually-hidden">#i18n{portal.admin.admin_home.button.changeLanguage} ${language.name}</span>
						</a>
					</#list>
				</ul>
			</li>
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
		<@icon style="text-direction-rtl"/> <span class="visually-hidden">#i18n{portal.site.site_property.layout.readmode.checkbox}</span>
	</div>
	<div class="nav-link d-flex d-lg-none" tabindex="0" role="button" title="#i18n{portal.site.site_property.layout.readmode.checkbox}">
		<@icon style="text-direction-rtl" class="me-2"/> #i18n{portal.site.site_property.layout.readmode.checkbox}
	</div>
</li>
</#macro>