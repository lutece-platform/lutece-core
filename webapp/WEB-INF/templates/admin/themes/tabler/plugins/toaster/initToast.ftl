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
<#macro initToast layout='popups' position='top-right' closer=true sticky=false progressbar=true insert='before' theme='default' duration=3000 pool=5 >
<script>
document.addEventListener( "DOMContentLoaded", function(){
	var lutecepolipop = new Polipop('lutecepop', {
		layout: '${layout}',
		position: '${position}',
		theme: '${theme}',
		life: ${duration?c},
		insert: '${insert}',
		closer: ${closer?c},
		closeText : '#i18n{portal.util.labelClose}',
		sticky: ${sticky?c},
		progressbar: ${progressbar?c},
		pool: ${pool},
	});
	<#-- Add addToast macro in your template -->
	<#nested>
});
</script>
</#macro>