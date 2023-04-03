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
			<ul class="list-inline list-inline-dots mb-0">
				<li class="list-inline-item"><a href="https://lutece.paris.fr/support/jsp/site/Portal.jsp?page=wiki" class="link-secondary">Documentation</a></li>
				<li class="list-inline-item"><a href="https://github.com/lutece-platform/" target="_blank" class="link-secondary" rel="noopener">Source code</a></li>
			</ul>
		</div>
		<div class="col-12 col-lg-auto mt-3 mt-lg-0">
			<ul class="list-inline list-inline-dots mb-0">
				<li class="list-inline-item">
					<a class="link-secondary" href="https://lutece.paris.fr" target="lutece" title="#i18n{portal.site.portal_footer.labelPortal}">
						<img src="images/poweredby.png" alt="#i18n{portal.site.portal_footer.labelMadeBy}">
						<small></small>
					</a>
				</li>
				<li class="list-inline-item">
					<span class="text-muted" rel="noopener">version ${version}</span>
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
<script>
document.addEventListener( "DOMContentLoaded", function(){
	let nCounter = "";
	const luteceTheme=localStorage.getItem('theme-bo-lutece');
	if( luteceTheme != undefined ){
		themeMode( luteceTheme );
	} else {
		localStorage.setItem('theme-bo-lutece','default');
	}
	
	const switchMode = document.querySelector('#switch-darkmode');
	switchMode.addEventListener( 'click', function(){
		const boTheme=localStorage.getItem('theme-bo-lutece');
		if( boTheme === 'dark'){
			localStorage.setItem('theme-bo-lutece','default	');
			themeMode('default');
		} else{
			localStorage.setItem('theme-bo-lutece','dark');
			themeMode('dark');
		}
	})
});
</script>
</div><!-- Close wrapper -->
</#macro>