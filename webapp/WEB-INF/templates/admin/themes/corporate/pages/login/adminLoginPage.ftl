<#-- Macro: adminLoginPage
Description: Generates the login page for the admin dashboard, with a custom background image and site name. The macro includes a script that randomly selects a background image from a list of images.
Parameters:
- title (string, optional): the title to display on the login page.
- site_name (string, optional): the name of the site to display on the login page.
-->
<#macro adminLoginPage title='' site_name='LUTECE'>
<#local loginLayoutImg=dskey('portal.site.site_property.layout.login.image')?trim /> 
</head>
<body class="lutece-login"  data-bs-theme="light">
<main>
<#assign pageClass><#if dskey('portal.site.site_property.bo.showXs.checkbox')?number == 0>d-none d-lg-block d-lg-flex<#else>d-block d-lg-flex</#if></#assign>
<@pageContainer class=pageClass >
	<@pageColumn id="lutece-login-block" class="border-end p-0" height="full">
		<@div class="d-flex align-items-center justify-content-evenly vh-100 me-5">
			<@div class="container-tight">
				<@div class="card shadow-lg rounded-4 p-4 mx-5 mw-30 ">
					<@div class="card-body p-5 fs-6">
						<@div class="text-center mb-4">
							<@link href='.' target='_blank'>
								<@img url=dskey('portal.site.site_property.logo_url') alt="Logo" params='height="40"' />
								<span class="visually-hidden">${site_name!'Lutece'}</span>
							</@link>
						</@div>
						<h1 class="mb-4 text-center">#i18n{portal.admin.admin_login.welcome} ${site_name!}</h1>
						<#nested>
					</@div>
				</@div>
				<@p class='text-center text-muted'><small>#i18n{portal.site.portal_footer.labelMadeBy} ${version}</small></@p>
			</@div>
		</@div>
	</@pageColumn>
	<#if loginLayoutImg?trim !=''>
	<@pageColumn id="lutece-advert" class="p-0 d-none d-xxl-block bg-login-adv">
		<@div class="d-flex align-items-center justify-content-evenly vh-100 fw-bold text-white">
			<@div class="fadeInTop border-0 border-none bg-transparent">
				<h1 class="text-center">#i18n{portal.admin.admin_login.title}</h1>
			 	<p class="text-center"><small>#i18n{portal.admin.admin_login.description}</small></p>
				<figure class="d-flex justify-content-center">
					<@img url=loginLayoutImg params='width="70%" style="max-width:1000px;"' />
				</figure>
			</@div>
		</@div>
    </@pageColumn>
	</#if>
</@pageContainer>
</main>
<script type="module">
import {
	LutecePassword
} from './themes/shared/modules/lutecePassword.js';

const passwordC = new LutecePassword();

document.addEventListener( "DOMContentLoaded", function(){
	/* Password Toggler */
	passwordC.initPassToggler( );
});
</script>
</#macro>