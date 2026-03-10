<#--
Macro: scrollTopBtn

Description: Generates a scroll-to-top button that appears when the user scrolls down the page, enabling smooth scrolling back to the top on click.

Showcase:
- desc: Retour en haut - @scrollTopBtn
- newFeature: false

Snippet:

    Basic usage (include once at the bottom of the page layout):

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