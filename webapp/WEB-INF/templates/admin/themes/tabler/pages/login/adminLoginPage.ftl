<#-- Macro: adminLoginPage
Description: Generates the login page for the admin dashboard, with a custom background image and site name. The macro includes a script that randomly selects a background image from a list of images.
Parameters:
- title (string, optional): the title to display on the login page.
- site_name (string, optional): the name of the site to display on the login page.
- layout (string, optional): the name of the site to display on the login page.
-->
<#macro adminLoginPage title='' site_name='LUTECE' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local readMode><#if dskey('portal.site.site_property.layout.readmode.checkbox')?trim?starts_with('DS')><#else><#if dskey('portal.site.site_property.layout.readmode.checkbox')?number = 1> dir="rtl"</#if></#if></#local>
<#local logoUrl = (dskey('portal.site.site_property.logo_url')!)?has_content?then(dskey('portal.site.site_property.logo_url'), 'themes/admin/shared/images/logo-header-icon.png')>
<#local loginLayoutImg=dskey('portal.site.site_property.layout.login.image')?trim /> 
</head>
<body class="antialiased d-flex flex-column" ${readMode!}<#if params!=''> ${params}</#if>>
<main id="login-page" class="page page-center">
<#if dskey('portal.site.site_property.bo.showXs.checkbox') == '0' >
<@div class="position-fixed top-0 w-100 d-block d-md-block d-lg-none m-0 p-0 overflow-hidden" params='style="z-index: 1050"'>
    <@pageColumn class="p-0 m-0">
	   <@div class="d-flex align-items-center justify-content-center vh-100 ">
	   		<@div class="container">
				<@div class="card shadow-lg rounded-4 p-4 mt-3 mx-2 mw-30">
					<@div class="card-body p-5 fs-6">
						<@div class="text-center mb-4">
							<@link href='/' target='_blank'>
								<span class="visually-hidden">${site_name!'Lutece'}</span>
							</@link>
						</@div>
						<@div class='d-flex flex-column align-items-center'>
							<h2 class="h1 mb-4 text-center">${i18n("portal.admin.admin_login.welcome", site_name!)}</h2>
							<@icon style="device-mobile-off" params=" style='font-size:120px !important'"/>
						</@div>
					</@div>
			   </@div>
			</@div>
		</@div>
	</@pageColumn>
</@div>
</#if>
<#if loginLayoutImg?trim !=''>
	<div class="container py-4">
		<div class="row align-items-center g-4">
			<div class="col-lg">
</#if>
<#--  Content  -->
<#assign containerClass><#if loginLayoutImg?trim =''> container-tight py-4</#if></#assign>
<@div class="container${containerClass}!''">
	<@div class="text-center mb-4">
		<@link href='.' target='_blank' class='admin-logo' >
			<@span class="visually-hidden">#i18n{portal.admin.admin_login.gotoFO} ${site_name!} </@span>
			<@img url='${logoUrl}' alt='${site_name!}' class='logo' params='aria-hidden="true" height="24" width="24"' />
		</@link>
	</@div>
	<@div class="card card-md">
		<@div class="card-body bg-white">
			<h2 class="card-title text-center mb-4 pt-2">
				${i18n("portal.admin.admin_login.welcome", "<span class='d-block'>${site_name!}</span>")}
			</h2>
			<#nested>
		</@div>
	</@div>
</@div>
<p class="text-center text-white"><small>#i18n{portal.site.portal_footer.labelMadeBy} ${version}</small></p>
<#--  End content -->
<#if loginLayoutImg !='' && !loginLayoutImg?starts_with('DS') >
			</div>
			<div class="col-12 col-lg-6 col-xl-8 d-none d-lg-block">
				<div class="bg-cover h-100 min-vh-100 w-100" style="background-image: url(${loginLayoutImg});background-size:70%; "></div>
			</div>
		</div>
	</div>
</div>
</#if>
<#if dskey('portal.site.site_property.bo.showXsWarning.checkbox') == '1' >
<@initToast position='top-0 start-50 translate-middle-x' showAll=true >
   <@addToast title='' content='#i18n{portal.site.message.showXsWarningMsg}' class='text-bg-warning d-block d-sm-block d-md-block d-lg-none' />
</@initToast>
</#if>
</main>
<script type="module">
import {
	LutecePassword
} from './themes/shared/modules/lutecePassword.js';
import {
	LuteceLogin
} from './themes/shared/modules/luteceLogin.js';

const login = new LuteceLogin();
const password = new LutecePassword();

document.addEventListener( "DOMContentLoaded", function(){
	/* backGround image random */
	const aImages = '#dskey{portal.site.site_property.layout.login.image}'.split(',');
	const backImages = '#dskey{portal.site.site_property.back_images}'.split(',');
	login.randomImages = aImages;
	login.randomBgImages = backImages;
	<#if loginLayoutImg != '' >
	login.element = '.bg-cover';
	</#if>	
	login.init( )
	/* Password Toggler */
	password.initPassToggler( );
});
</script>
<@coreAdminJSLinks />
</#macro>
