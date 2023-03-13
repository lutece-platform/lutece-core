<#-- Macro: adminLoginPage

Description: Generates the login page for the admin dashboard, with a custom background image and site name. The macro includes a script that randomly selects a background image from a list of images.

Parameters:
- title (string, optional): the title to display on the login page.
- site_name (string, optional): the name of the site to display on the login page.
-->
<#macro adminLoginPage title='' site_name='LUTECE'>
</head>
<body class="antialiased border-top-wide border-primary d-flex flex-column">
<@div id="login-page" class="page page-center">
	<@div class="container-tight py-4">
		<@div class="text-center mb-4">
			<@link href='.'>
				<@img url='#dskey{portal.site.site_property.logo_url}' alt='${site_name!}' title='${site_name!}' params=' height="36"' />
				<span class="sr-only">
					${site_name!'Lutece'}
				</span>
			</@link>
		</@div>
		<@div class="card-body bg-white">
			<h2 class="card-title text-center mb-4">#i18n{portal.admin.admin_login.welcome}
				<br>
				${site_name!}
			</h2>
			<#nested>
		</@div>
	</@div>
</@div>
<script>
document.addEventListener( "DOMContentLoaded", function(){
	const randomImages = [#dskey{portal.site.site_property.back_images}];
	let rndNum = Math.floor(Math.random() * (randomImages.length));
	let bgImg = 'url(' + randomImages[rndNum] +	')';
	const lp = document.querySelector( '#login-page' );
	lp.style.backgroundImage = bgImg;
});
</script>
</#macro>