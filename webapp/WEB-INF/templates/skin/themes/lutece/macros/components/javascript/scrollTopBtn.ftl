<#-- 
Macro: scrollTopBtn

Description: Generates a button that appears when the user scrolls down the page, allowing them to easily scroll back to the top of the page when clicked.
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