<#-- Macro: adminLoginPage
Description: Generates the login page for the admin dashboard, with a custom background image and site name. The macro includes a script that randomly selects a background image from a list of images.
Parameters:
- title (string, optional): the title to display on the login page.
- site_name (string, optional): the name of the site to display on the login page.
-->
<#macro adminLoginPage title='' site_name='LUTECE' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local readMode><#if dskey('portal.site.site_property.layout.readmode.checkbox')?trim?starts_with('DS')><#else><#if dskey('portal.site.site_property.layout.readmode.checkbox')?number = 1> dir="rtl"</#if></#if></#local>
<#assign logoUrl = (dskey('portal.site.site_property.logo_url')!)?has_content?then(dskey('portal.site.site_property.logo_url'), 'themes/admin/shared/images/logo-header.svg')>
<#local loginLayoutImg=dskey('portal.site.site_property.login.image')?trim />
</head>
<body class="lutece-login" data-bs-theme="dark"${readMode!}<#if params!=''> ${params}</#if>>
<main>
<#assign pageClass><#if !dskey('portal.site.site_property.bo.showXs.checkbox')?is_number><#if dskey('portal.site.site_property.bo.showXs.checkbox')?number == 0>d-none d-lg-block d-lg-flex<#else>d-block d-lg-flex</#if><#else>d-none d-lg-block d-lg-flex</#if></#assign>
<@pageContainer class=pageClass>
	<@pageColumn id="lutece-login-block" class="border-end p-0" height="full">
		<@div class="d-flex align-items-center justify-content-evenly vh-100 me-md-5">
			<@div class="container-tight">
				<@div class="card rounded-4 py-2 py-md-5 mx-3 mx-md-5 mw-30">
					<@div class="px-2 px-md-5 fs-6">
						<@div class="text-center mb-4">
							<@link href="." params='aria-label="#i18n{portal.admin.admin_login.buttonConnect} ${site_name!}"'>
								<@img url="${logoUrl}" alt="${site_name!}" params='height="35" aria-hidden="true"' />
							</@link>
						</@div>
						<h1 class="mb-5 text-center">#i18n{portal.admin.admin_login.welcome} ${site_name!}</h1>
						<#nested>
					</@div>
				</@div>
				<@p class='text-center text-muted mt-5'>
					<#if version?contains("SNAPSHOT")>
						<@tag color="warning"><i class="ti ti-alert-triangle-filled"></i> Version ${version}</@tag>
					<#else>
						<@tag color="success"><i class="ti ti-discount-check"></i> Version ${version}</@tag>
					</#if>
				</@p>
			</@div>
		</@div>
	</@pageColumn>
	<#if loginLayoutImg?trim != ''>
		<@pageColumn id="lutece-advert" class="p-0 d-none d-xxl-block bg-login-adv">
			<@div class="d-flex align-items-center justify-content-evenly vh-100 fw-bold text-white">
				<@div class="fadeInTop border-0 border-none bg-transparent">
					<h1 class="fs-1 text-center">#i18n{portal.admin.admin_login.title}</h1>
					<p class="text-center">#i18n{portal.admin.admin_login.description}</p>
					<figure class="d-flex justify-content-center">
						<@img url=loginLayoutImg params='aria-hidden="true" style="max-width:1000px;"'/>
					</figure>
				</@div>
			</@div>
		</@pageColumn>
	</#if>
</@pageContainer>
</main>
<script type="module">
import { LutecePassword } from './themes/shared/modules/lutecePassword.js';
const passwordC = new LutecePassword();
document.addEventListener("DOMContentLoaded", function() {
	/* Password Toggler */
	passwordC.initPassToggler();
});
</script>
</#macro>