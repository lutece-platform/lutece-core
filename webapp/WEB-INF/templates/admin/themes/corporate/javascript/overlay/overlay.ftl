<#-- 
Macro: overlay

Description: Add overlay 

Parameters:
- label (string, optionnal): the label for the element(s), default : #i18n{portal.util.labelLoading}.
- id (string, optionnal): the ID selector for the element(s), default : 'loading-overlay'.
- class (string, optional): the CSS class to add to the element(s), default : 'position-fixed top-0 start-0 w-100 h-100 d-flex justify-content-center align-items-center'.
- params (string, optional): any attributes to add to the element(s) default 'style="background-color: rgba(255, 255, 255, 0.9); z-index: 9999;"'.

-->
<#macro overlay label='#i18n{portal.util.labelLoading}' id='loading-overlay' class='position-fixed top-0 start-0 w-100 h-100 d-flex justify-content-center align-items-center ' params='style="background-color: rgba(255, 255, 255, 0.9); z-index: 9999;"' deprecated...>
<@deprecatedWarning args=deprecated />
<!-- Loading overlay -->
<div id="${id}" class="${class}" ${params}>
    <div class="text-center">
        <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">#i18n{portal.util.labelLoading}</span>
        </div>
        <div class="mt-2">${label}</div>
        <#nested>
    </div>
</div>
<script>
window.addEventListener( "load", function(){
    // Show loading overlay initially
    const loadingOverlay = document.getElementById("loading-overlay");
    
    // Hide loading overlay after sessionStorage processing is complete
    setTimeout(function() {
        if (loadingOverlay) {
            loadingOverlay.style.opacity = '0';
            loadingOverlay.style.transition = 'opacity 0.3s ease';
            setTimeout(function() {
                loadingOverlay.style.display = 'none';
                loadingOverlay.classList.remove('position-fixed');
            }, 300);
        }
    }, 100);
});
</script>
</#macro>