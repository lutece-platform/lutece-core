<#-- Macro: adminLoginPage
Description: Generates the login page for the admin dashboard, with a custom background image and site name. The macro includes a script that randomly selects a background image from a list of images.
Parameters:
- title (string, optional): the title to display on the login page.
- site_name (string, optional): the name of the site to display on the login page.
- layout (string, optional): the name of the site to display on the login page.
-->
<#macro adminLoginPage title='' site_name='LUTECE' layout=''  params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local loginLayoutImg=dskey('portal.site.site_property.layout.login.image')?trim /> 
</head>
<body class="antialiased d-flex flex-column"<#if params!=''> ${params}</#if>>
<main id="login-page" class="page page-center">
<#if loginLayoutImg?trim !=''>
	<div class="container container-normal py-4">
		<div class="row align-items-center g-4">
			<div class="col-lg">
				<div class="container-tight">
</#if>
<#--  Content  -->
<@div class="container-tight py-4">
	<@div class="text-center mb-4">
		<@link href='.' target='_blank' >
			<figure>
				<@img url='#dskey{portal.site.site_property.logo_url}' alt='${site_name!}' title='${site_name!}' params=' height="36"' />
				<figcaption class="visually-hidden">#i18n{portal.admin.admin_login.gotoFO} ${site_name!'Lutece'} [ #i18n{portal.site.portal_footer.newWindow} ]</figcaption>
			</figure>
		</@link>
	</@div>
	<@div class="card card-md">
		<@div class="card-body bg-white">
			<h2 class="card-title text-center mb-4 pt-2">
				#i18n{portal.admin.admin_login.welcome}
				<span class="d-block">${site_name!}</span>
			</h2>
			<#nested>
		</@div>
	</@div>
</@div>
<@p class='text-center text-white'><small>#i18n{portal.site.portal_footer.labelMadeBy} ${version}</small></@p>
<#--  End content -->
<#if loginLayoutImg?trim !=''>
				</div>
			</div>
			<div class="col-12 col-lg-6 col-xl-8 d-none d-lg-block">
				<div class="bg-cover h-100 min-vh-100 w-100" style="background-image: url( ${loginLayoutImg} );background-size:70%; "></div>
			</div>
		</div>
	</div>
</div>
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
	const illust = '.bg-cover';
	login.element = illust;
	</#if>	
	login.init( )
	/* Password Toggler */
	<#--  password.passTogglerIconOn = "ti-accessible";
	password.passTogglerIconOff = "ti-accessible-off";  -->
	password.initPassToggler( );
});
</script>
<@coreAdminJSLinks />
</#macro>