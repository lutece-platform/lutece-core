<#-- 
Macro: scrollTopBtn

Description: Generates a button that appears when the user scrolls down the page, allowing them to easily scroll back to the top of the page when clicked.
-->
<#macro scrollTopBtn>
<a href="#" id="scroll" style="display: none;"><span></span></a>
<script>
document.addEventListener('DOMContentLoaded', function() {
    var scrollBtn = document.getElementById('scroll');
    window.addEventListener('scroll', function() {
        if (window.scrollY > 100) {
            scrollBtn.style.display = 'block';
        } else {
            scrollBtn.style.display = 'none';
        }
    });
    scrollBtn.addEventListener('click', function(e) {
        e.preventDefault();
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });
});
</script>
</#macro>