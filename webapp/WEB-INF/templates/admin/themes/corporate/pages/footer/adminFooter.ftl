<#--
Macro: adminFooter

Description: Generates the footer section, including documentation and source code links, a Lutece logo, and a version number.

Parameters:
- version : The version number
-->
<#macro adminFooter closeMain=true >
<footer id='main-lutece-footer' class="d-flex justify-content-end align-items-end border-top d-none">
<@p class='me-5'>${site_name} - #i18n{portal.site.portal_footer.labelMadeBy} ${version}</@p>
</footer>
<!-- footer menu                                                     -->
<!-- Included JS Files 												            -->
<!-- Le javascript 													            -->
<!-- =============================================================== -->
<!-- Placed at the end of the document so the pages load faster 	   -->
<@coreAdminJSLinks />
${javascript_files}
</div><!-- Close wrapper -->
</div>
<#if dskey('portal.site.site_property.bo.showXs.checkbox')?number == 0 >
<@pageContainer class="position-fixed top-0 w-100 d-block d-md-block d-lg-none">
    <@pageColumn id="lutece-login-block" class="border-end p-0" height="full">
	   <@div class="d-flex align-items-center justify-content-evenly vh-100">
			<@div class="container-tight">
				<@div class="card shadow-lg rounded-4 p-4 mx-5 mw-30">
					<@div class="card-body p-5 fs-6">
                  <@div class="text-center mb-4">
                     <@link href='/' target='_blank'>
                        <img src="${dskey('portal.site.site_property.logo_url')}" height="40" alt="Logo" aria-hidden="true" >
                        <span class="visually-hidden">${site_name!'Lutece'}</span>
                     </@link>
                  </@div>
                  <@div class='d-flex flex-column align-items-center'>
                     <h2 class="h1 mb-4 text-center">#i18n{portal.admin.admin_login.welcome} ${site_name!}</h2>
                     <i class="ti ti-device-mobile-off" style="font-size:120px !important"></i>
                  </@div>
               </@div>
			   </@div>
			</@div>
		</@div>
	</@pageColumn>
</@pageContainer>
</#if>
<#if dskey('portal.site.site_property.bo.showXsWarning.checkbox')?number == 1 >
<@initToast position='top-0 start-50 translate-middle-x' showAll=true >
   <@addToast title='' content='#i18n{portal.site.message.showXsWarningMsg}' class='text-bg-warning d-block d-sm-block d-md-block d-lg-none' />
</@initToast>
</#if>
</#macro>