<#--
Macro: scrollTopBtn

Description: Generates a floating scroll-to-top button (`<a id="scroll">`) that becomes visible when the user scrolls more than 100px down the page, with a smooth scroll-to-top behavior on click. The button is initially hidden and toggled via the embedded vanilla JS scroll listener.

Parameters: None.

Showcase:
- desc: Retour en haut - @scrollTopBtn
- newFeature: false

Snippet:

    Basic usage (include once at the bottom of the page layout, typically in the footer):

    <@scrollTopBtn />

-->
<#macro scrollTopBtn>
<a href="#" id="scroll" style="display: none;"><span></span></a>
<script>
document.addEventListener('DOMContentLoaded', function() {
    // Get the scroll button element
    const scrollButton = document.getElementById('scroll');
    
    // Add scroll event listener to window
    window.addEventListener('scroll', function() {
        if (window.scrollY > 100) {
            scrollButton.style.display = 'block';
        } else {
            scrollButton.style.display = 'none';
        }
    });
    
    // Add click event listener to the scroll button
    scrollButton.addEventListener('click', function(e) {
        e.preventDefault();
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    });
});
</script>
</#macro>