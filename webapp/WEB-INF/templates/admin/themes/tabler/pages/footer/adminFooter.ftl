<#--
Macro: adminFooter

Description: Generates the footer section, including documentation and source code links, a Lutece logo, and a version number.

Parameters:
- version : The version number
-->
<#macro adminFooter closeMain=true >
<!-- footer menu -->
<footer class="lutece-main-footer footer footer-transparent d-print-none">
	<div class="container-fluid">
		<div class="row text-center align-items-center flex-row-reverse">
			<div class="col-lg-auto ms-lg-auto">
				<ul class="list-inline mb-0">
					<li class="list-inline-item nav-item "><a href="https://lutece.paris.fr/support/jsp/site/Portal.jsp?page=wiki" class="nav-link">Documentation</a></li>
					<li class="list-inline-item nav-item "><a href="https://github.com/lutece-platform/" target="_blank" class="nav-link" rel="noopener">Source code</a></li>
				</ul>
			</div>
			<div class="col-12 col-lg-auto mt-3 mt-lg-0">
				<ul class="nav list-inline mb-0">
					<li class="list-inline-item nav-item">
						<a class="nav-link d-flex align-items-center" href="https://lutece.paris.fr" target="lutece" title="#i18n{portal.site.portal_footer.labelPortal}">
							<span class="me-2">${site_name}</span>
							<img src="images/poweredby.svg" style="height:15px" class="img-fluid theme-invert" alt="#i18n{portal.site.portal_footer.labelMadeBy}">
							<span class="visually-hidden">LUTECE</span>
							<span class="text-muted ms-2" rel="noopener">version ${version}</span>
						</a>
					</li>
				</ul>
			</div>
		</div>
	</div>
</footer>
<!-- Included JS Files 												-->
<!-- Le javascript 													-->
<!-- ============================================================== -->
<!-- Placed at the end of the document so the pages load faster 	-->
<@coreAdminJSLinks />
${javascript_files}
</div><!-- Close wrapper -->
</#macro>