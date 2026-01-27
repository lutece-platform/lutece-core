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
<#local logoSvg><#attempt>${dskey('portal.site.site_property.logo_svg.textblock')}<#recover>${dskey('portal.site.site_property.logo_svg.textblock')}!=''?then(${dskey('portal.site.site_property.logo_svg.textblock')}, '')></#attempt></#local>
<#local logoUrl><#attempt>${dskey('portal.site.site_property.logo_url')}<#recover>${dskey('portal.site.site_property.logo_url')}!=''?then(${dskey('portal.site.site_property.logo_url')},'themes/admin/shared/images/logo-header-icon.min.svg')</#attempt></#local>
<#local loginIsCover><#attempt>${dskey('portal.site.site_property.layout.login.cover.checkbox')?number}<#recover>0</#attempt></#local>
<#local loginIsCoverContain><#attempt>${dskey('portal.site.site_property.layout.login.cover.contain.checkbox')?number}<#recover>0</#attempt></#local>
<#local loginLayoutImg=dskey('portal.site.site_property.layout.login.image')?trim /> 
</head>
<body class="loaded<#if loginIsCover?number == 1> d-flex flex-column</#if>" ${readMode!}<#if params!=''> ${params}</#if>>
<main class="<#if loginIsCover?number == 1>row g-0 flex-fill<#else>page page-center"</#if>">
<#if loginIsCover?number == 0 && loginLayoutImg==''><div class="container container-tight py-4"> </#if>
<#if loginIsCover?number == 1>
<!-- COVER -->
	<div class="col-12 col-lg-6 col-xl-4 border-top-wide border-primary d-flex flex-column justify-content-center">
		<div class="container container-tight my-5 px-lg-5">
</#if>	
<#if loginIsCover?number == 0 && loginLayoutImg!='' >		 
	<!-- Illustration -->
	<div class="container container-normal py-4">
		<div class="row align-items-center g-4">
			<div class="col-lg">
				<div class="container-tight">
</#if>
					<div class="text-center mb-4">
						<!-- BEGIN NAVBAR LOGO -->
						<a href="." aria-label="#i18n{portal.admin.admin_login.gotoFO} ${site_name!}" target="_blank" class="navbar-brand navbar-brand-autodark">
							<#if logoUrl !=''>
							<@img url='${logoUrl}' alt='${site_name!}' class='logo' params='aria-hidden="true" height="24" width="24"' />
							<span class="ms-1 fs-2 d-inline-block">${site_name!''}</span>
							<#else>
							${logoSvg!}
							</#if>
						</a>
						<!-- END NAVBAR LOGO -->
					</div>
					<div class="card card-md">
						<div class="card-body">
						<#nested>
						</div>
					</div>
				</div>
<#if loginIsCover?number == 0 && loginLayoutImg!='' >	
		</div>
		<div class="col-lg d-none d-lg-block">
			<img src="${loginLayoutImg!}" class="img d-block mx-auto" />
		</div>
	</div>
</#if>
<#if loginIsCover?number == 1>
		<!-- Cover --->
		</div>
		<div class="col-12 col-lg-6 col-xl-8 d-none d-lg-block">
			<!-- Photo -->
			<div class="bg-cover h-100 min-vh-100" style="background-image: url(${loginLayoutImg!});<#if loginIsCoverContain?number == 1>background-size: contain;</#if>"></div>
		</div>
	</div>
<#else>
	</div>
</#if>
<!--  End content -->
<p class="text-center text-white"><small>#i18n{portal.site.portal_footer.labelMadeBy} ${version}</small></p>
</main>
<#if dskey('portal.site.site_property.bo.showXsWarning.checkbox') == '1' >
<@initToast position='top-0 start-50 translate-middle-x' showAll=true >
<@addToast title='' content='#i18n{portal.site.message.showXsWarningMsg}' class='text-bg-warning d-block d-sm-block d-md-block d-lg-none' />
</@initToast>
</#if>
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