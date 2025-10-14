<#-- 
Macro: scrollTopBtn

Description: Generates a button that appears when the user scrolls down the page, allowing them to easily scroll back to the top of the page when clicked.
-->
<#macro scrollTopBtn>
<button type="button" class="btn btn-primary btn-icon btn-square-chevron-up" id="scroll" title="#i18n(portal.util.labelScrollTop}" aria-label="#i18n(portal.util.labelScrollTop}">
    <svg aria-hidden="true" xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-square-chevron-up m-0"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M3 3m0 2a2 2 0 0 1 2 -2h14a2 2 0 0 1 2 2v14a2 2 0 0 1 -2 2h-14a2 2 0 0 1 -2 -2z" /><path d="M9 13l3 -3l3 3" /></svg>
</button>
<script>
document.addEventListener('DOMContentLoaded', function () {
    const scrollBtn = document.getElementById('scroll');
    window.addEventListener('scroll', function () {
        if (window.scrollY > 100) {
            scrollBtn.style.display = 'block';
        } else {
            scrollBtn.style.display = 'none';
        }
    });
    scrollBtn.addEventListener('click', function (e) {
        e.preventDefault();
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });
});
</script>
</#macro>