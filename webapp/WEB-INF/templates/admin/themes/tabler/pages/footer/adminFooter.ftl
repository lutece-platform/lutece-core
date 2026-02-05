<#--
Macro: adminFooter

Description: Generates the footer section, including documentation and source code links, a Lutece logo, and a version number.

Parameters:
- version : The version number
-->
<#macro adminFooter closeMain=true deprecated...>
<@deprecatedWarning args=deprecated />
</div>
 <!--  BEGIN FOOTER  -->
<footer class="footer footer-transparent d-print-none">
    <div class="container-xl">
        <div class="row text-center align-items-center flex-row-reverse">
            <div class="col-lg-auto ms-lg-auto">
            <ul class="list-inline list-inline-dots mb-0">
                <li class="list-inline-item"><a href="https://lutece.paris.fr/support/jsp/site/Portal.jsp?page=wiki" class="nav-link">Documentation</a></li>
                <li class="list-inline-item">
                <a href="https://github.com/lutece-platform/" target="_blank" class="link-secondary" rel="noopener">Source code</a>
                </li>
               
            </ul>
            </div>
            <div class="col-12 col-lg-auto mt-3 mt-lg-0">
            <ul class="list-inline list-inline-dots mb-0">
                <li class="list-inline-item">
                    <a class="nav-link d-flex align-items-center" href="https://lutece.paris.fr" target="lutece" title="#i18n{portal.site.portal_footer.labelPortal}">
                        <span class="me-2">${site_name}</span>
                        <img src="themes/admin/shared/images/poweredby.svg" style="height:15px" class="img-fluid theme-invert" alt="#i18n{portal.site.portal_footer.labelMadeBy}">
                        <span class="visually-hidden">LUTECE</span>
                        <span class="text-muted ms-2" rel="noopener">version ${version}</span>
                    </a>
                </li>
            </ul>
            </div>
        </div>
    </div>
</footer>
</div>
<!--  END FOOTER  -->
<!-- Included JS Files 												-->
<!-- Le javascript 													-->
<!-- ============================================================== -->
<!-- Placed at the end of the document so the pages load faster 	-->
<@coreAdminJSLinks />
${javascript_files}
<script>
/* Manage Header */
document.addEventListener( "DOMContentLoaded", function() {
	// Initialize the feature title container
	const adminContentHeader = document.querySelector('#admin-content-header');
    if( adminContentHeader != null ) {
        const adminHeaderTitle = adminContentHeader.querySelector('.page-title');
        const adminHeaderPreTitle = adminContentHeader.querySelector('.page-pretitle');
        const pageHeader = document.getElementById('page-header');
        if( adminHeaderPreTitle != null && pageHeader != null ) {
            const pageHeaderPreTitle = pageHeader.querySelector('.page-pretitle');
            const pageHeaderTitle = pageHeader.querySelector('.page-title');
            pageHeaderPreTitle.innerHTML = adminHeaderPreTitle.innerHTML;
            if( adminHeaderTitle != null && (adminHeaderTitle.textContent.trim() != pageHeaderTitle.textContent.trim() ) && pageHeaderPreTitle.textContent.trim() == '' ) {
                pageHeaderPreTitle.remove()
                adminContentHeader.classList.remove('d-none');
            }
        }
        if( pageHeader === null ) {
            adminContentHeader.classList.remove('d-none');
        }
    }
});
</script>
</div><!-- Close wrapper -->
</#macro>
<#--
Macro: adminSiteFooter

Description: Footer for site Admin
Parameters:
-->
<#macro adminSiteFooter >
<#assign siteFooter = .get_optional_template('../../../../../skin/site/portal_footer.html')>
<#if siteFooter.exists><@siteFooter.include /></#if> 
</#macro>