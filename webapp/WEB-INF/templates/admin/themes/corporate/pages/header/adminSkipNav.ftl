<#-- SkipNav -->
<#-- Params
    - title   : Title shown over the banner
    - logoImg : Default: Empty string, show logo instead of text title, title is set as title html attribute for the logo image.
    - hasMenu : Default: true; 
    Nested content : Shows default page menu, but can other item can be add using @mainNavItem macro.
-->
<#macro adminSkipNav>
<a href="" class="lutece-skip-links" id="lutece-skip-nav">#i18n{portal.util.labelSkipNav}</a></li>
<script>
document.addEventListener("DOMContentLoaded", function() {
<#noparse>document.querySelector( '#lutece-skip-nav' ).setAttribute('href',`${window.location.pathname}${window.location.search}#lutece-main`)</#noparse>
});
</script>
</#macro> 