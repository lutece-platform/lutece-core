<#-- SkipNav -->
<#-- Params
    - title   : Title shown over the banner
    - logoImg : Default: Empty string, show logo instead of text title, title is set as title html attribute for the logo image.
    - hasMenu : Default: true; 
    Nested content : Shows default page menu, but can other item can be add using @mainNavItem macro.
-->
<#macro adminSkipNav>
<a href="jsp/admin/AdminMenu.jsp#lutece-main" class="lutece-skip-links">#i18n{portal.util.labelSkipNav}</a></li>
</#macro> 