<#-- Macro: initToast
Description: Initializes a Polipop instance for displaying Bootstrap toast notifications.
Parameters:
- layout (string, optional): the layout of the notifications ("popups" or "list").
- position (string, optional): the position of the notifications on the screen ("top-left", "top-right", "bottom-left", or "bottom-right").
- closer (boolean, optional): whether to show a close button on the notifications.
- sticky (boolean, optional): whether the notifications should be sticky (i.e., remain on the screen until closed by the user).
- progressbar (boolean, optional): whether to show a progress bar for the notifications.
- insert (string, optional): the position in the DOM where the notifications should be inserted ("before" or "after").
- theme (string, optional): the theme of the notifications (a string corresponding to a Bootstrap color class).
- duration (integer, optional): the duration in milliseconds that the notifications should be displayed before disappearing.
- pool (integer, optional): the maximum number of notifications that can be displayed at the same time.
-->
<#macro initToast class='' id='' position='top-0 end-0' showAll=true autohide=false animation=true duration=2000 params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div aria-live="polite" aria-atomic="true" class="position-static z-3">
	<div<#if id !=''> id="${id}"</#if> class="toast-container ${position} p-3<#if class !=''> ${class}</#if>"<#if params !=''> ${params}"</#if>>
	<#nested>
	</div>
</div>
<script>
document.addEventListener("DOMContentLoaded", function() {
    const toastElList = [].slice.call(document.querySelectorAll('.toast'))
    const option = { delay: ${duration}, animation: ${animation?c}, autohide: ${autohide?c} }
    const toastList = toastElList.map( function( toastEl, option ) {
        return new bootstrap.Toast( toastEl )
    })
    <#if showAll>toastList.forEach( toast => toast.show())</#if>
});
</script>
</#macro>