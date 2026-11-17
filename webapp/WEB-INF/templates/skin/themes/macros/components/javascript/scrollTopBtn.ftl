<#--
Macro: scrollTopBtn

Description: Kept for compatibility: renders @cGoToTop with its default settings, the floating back-to-top button that appears once the page is scrolled more than 100px down. Use @cGoToTop directly in new templates.

Parameters: None.

Showcase:
- desc: Retour en haut - @scrollTopBtn
- newFeature: false

Snippet:

    Basic usage (include once at the bottom of the page layout, typically in the footer):

    <@scrollTopBtn />

-->
<#macro scrollTopBtn>
<@cGoToTop />
</#macro>