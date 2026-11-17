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
<@cBtn type='button' class='${scrollClass!} ${scrollBtnClass!} z-2' label=scrollLabel nestedPos='before'><@cInline class='position-relative white fw-bold' params={"aria-hidden": "true"}>&#8593;</@cInline></@cBtn>
<script>
(() => {
    const gototop = document.querySelector('.${( scrollBtnClass! )?js_string?no_esc}');
    if ( !gototop ) return;

    // Marks the first ${scrollTop?c}px of the page, as the first child of <body> so it sits at the very top:
    // the button shows once this band has scrolled out of view
    const sentinel = document.createElement( 'div' );
    sentinel.setAttribute( 'aria-hidden', 'true' );
    sentinel.style.cssText = 'position:absolute;inset-block-start:0;inset-inline-start:0;inline-size:1px;block-size:${scrollTop?c}px;pointer-events:none';
    document.body.prepend( sentinel );

    // An observer instead of a scroll listener: nothing runs while the page scrolls
    new IntersectionObserver( ( [ entry ] ) => {
        gototop.classList.toggle( 'active', !entry.isIntersecting );
    }).observe( sentinel );

    gototop.addEventListener( 'click', () => {
        const reduce = window.matchMedia( '(prefers-reduced-motion: reduce)' ).matches;
        window.scrollTo( { top: 0, left: 0, behavior: reduce ? 'auto' : 'smooth' } );
        // The button hides at the top: keyboard focus moves to the start of the page instead of being lost
        const top = document.getElementById( 'top' );
        if ( top ) {
            top.setAttribute( 'tabindex', '-1' );
            top.addEventListener( 'blur', () => top.removeAttribute( 'tabindex' ), { once: true } );
            top.focus( { preventScroll: true } );
        }
    });
})();
</script>
</#macro>