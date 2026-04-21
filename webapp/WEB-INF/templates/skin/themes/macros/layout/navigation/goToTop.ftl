<#--
Macro: cGoToTop

Description: Generates a "back to top" button that appears after scrolling down and smoothly scrolls the page back to the top when clicked.

Parameters:
- scrollLabel (string, optional): Accessible label text for the button. Default: '#i18n{portal.theme.gohome}'.
- scrollClass (string, optional): CSS class(es) for the scroll animation effect. Default: 'primary fadein'.
- scrollBtnClass (string, optional): CSS class for the button element. Default: 'btn-gototop'.
- scrollTop (number, optional): Scroll offset in pixels before the button becomes visible. Default: 100.

Showcase:
- desc: "Retour en haut - @goToTop"
- newFeature: false

Snippet:

    Basic usage:

    <@cGoToTop />

    Custom scroll offset and styling:

    <@cGoToTop scrollLabel='Back to top' scrollClass='secondary fadein' scrollTop=200 />

-->
<#macro cGoToTop scrollLabel='#i18n{portal.theme.gohome}' scrollClass='primary fadein' scrollBtnClass='btn-gototop' scrollTop=100 deprecated...>
<@deprecatedWarning args=deprecated />
<@cBtn type='button' class='${scrollClass!} ${scrollBtnClass!}' label=scrollLabel nestedPos='before'><@cInline class='position-relative white fw-bold pb-3' params='style="bottom:2px;"'>&#8593;</@cInline></@cBtn>
<script>
const gototop = document.querySelector('.${scrollBtnClass!}');
document.addEventListener( "DOMContentLoaded", () => {
    document.addEventListener("scroll", (e) => {
        if ( document.documentElement.scrollTop  > ${scrollTop}) {
            gototop.classList.add('active');
        } else {
            gototop.classList.remove('active');
        }
    });
  
    gototop.addEventListener( 'click', (e) =>  {
        e.preventDefault();
       window.scrollTo({
            top: 0,
            left: 0,
        });
    });
});
</script>
</#macro>