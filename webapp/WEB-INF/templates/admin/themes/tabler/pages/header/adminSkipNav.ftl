<#-- SkipNav -->
<#-- Params
    - title   : Title shown over the banner
    - logoImg : Default: Empty string, show logo instead of text title, title is set as title html attribute for the logo image.
    - hasMenu : Default: true; 
    Nested content : Shows default page menu, but can other item can be add using @mainNavItem macro.
-->
<#macro adminSkipNav>
<nav aria-label="#i18n{portal.util.skipNavLabel}" role="navigation" >
    <ul class="lutece-skip-links">
        <li><a href="jsp/admin/AdminMenu.jsp#main-menu">#i18n{portal.util.labelSkipNav}</a></li>
        <li><a id="skip-nav" href="jsp/admin/AdminMenu.jsp#lutece-main">#i18n{portal.util.labelSkipContent}</a></li>
    </ul>
    <div id="top" class="visually-hidden visually-hidden-focusable">#i18n{portal.util.labelHome}</div>
</nav>
<script>
    // navfixed
    window.addEventListener('scroll', function () {
        const navigation = document.querySelector('.navigation');
        if (navigation) {
            if (navigation.getBoundingClientRect().top + window.scrollY > 50) {
                navigation.classList.add('nav-bg');
            } else {
                navigation.classList.remove('nav-bg');
            }
        }
    });
</script>
</#macro> 