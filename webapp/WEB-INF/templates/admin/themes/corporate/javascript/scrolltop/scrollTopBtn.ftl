<#-- 
Macro: scrollTopBtn

Description: Generates a button that appears when the user scrolls down the page, allowing them to easily scroll back to the top of the page when clicked.
-->
<#macro scrollTopBtn>
<a href="#" id="scroll" style="display: none;"><span></span></a>
<script>
$( function(){ 
    $(window).scroll(function(){ 
        if ( $(this).scrollTop() > 100) { 
            $('#scroll').fadeIn(); 
        } else { 
            $('#scroll').fadeOut(); 
        } 
    }); 
    $('#scroll').click(function(){ 
        $("html, body").animate({ scrollTop: 0 }, 600); 
        return false; 
    }); 
});
</script>
</#macro>