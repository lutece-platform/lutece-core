<#-- Macro: adminLoginPage
Description: Generates the login page for the admin dashboard, with a custom background image and site name. The macro includes a script that randomly selects a background image from a list of images.
Parameters:
- title (string, optional): the title to display on the login page.
- site_name (string, optional): the name of the site to display on the login page.
-->
<#macro adminLoginPage title='' site_name='LUTECE'>
	</head>
	<body class="lutece-login"  data-bs-theme="light">
		<@pageContainer class="d-md-block d-lg-none">
    <@pageColumn id="lutece-login-block" class="border-end p-0" height="full">
	<@div class="d-flex align-items-center justify-content-evenly vh-100">
			<@div class="container-tight">
					<@div class="card shadow-lg rounded-4 p-4 mx-5 mw-30 ">
					<@div class="card-body p-5 fs-6">
					<@div class="text-center mb-4">
				<@link href='.'>
               <img src="themes/admin/corporate/images/core-corporate.png" height="40" alt="Logo">
						<span class="sr-only">
							${site_name!'Lutece'}
						</span>
					</@link>
				</@div>
				<h1 class="mb-4"> <center>#i18n{portal.admin.admin_login.welcome} ${site_name!}</center></h1>
				<center><i class="ti ti-device-mobile-off" style="font-size:120px !important"></i></center>
				</@div>
			</@div>
			</@div>
		</@div>
	</@pageColumn>
	</@pageContainer>
	<@pageContainer class="d-xs-none d-sm-none d-md-none d-lg-flex">
    <@pageColumn id="lutece-login-block" class="border-end p-0" height="full">
		<@div class="d-flex align-items-center justify-content-evenly vh-100 me-5">
			<@div class="container-tight">
					<@div class="card shadow-lg rounded-4 p-4 mx-5 mw-30 ">
					<@div class="card-body p-5 fs-6">
					<@div class="text-center mb-4">
				<@link href='.'>
               <img src="themes/admin/corporate/images/core-corporate.png" height="40" alt="Logo">
						<span class="sr-only">
							${site_name!'Lutece'}
						</span>
					</@link>
				</@div>
					<h1 class="mb-4"> <center>#i18n{portal.admin.admin_login.welcome} ${site_name!}</center></h1>
					<#nested>
				</@div>
			</@div>
			</@div>
		</@div>
		    </@pageColumn>
			    <@pageColumn  id="lutece-advert" class="p-0 d-none d-xxl-block bg-login-adv">
		<@div class="d-flex align-items-center justify-content-evenly vh-100 fw-bold text-white">
		<div class="fadeInTop border-0 border-none bg-transparent">
			<h1>  <center>#i18n{portal.admin.admin_login.title}</center></h1>
			<small><center>#i18n{portal.admin.admin_login.description}</center></small>
			<center><img src="themes/admin/corporate/images/miniroom.png" width="70%" style="max-width:1000px;"></center>
					   </div>
		</@div>
		    </@pageColumn>
		</@pageContainer>
</#macro>