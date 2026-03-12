<#--
Macro: copyElementToClipboard

Description: Generates a JavaScript snippet that attaches click-to-copy behavior to matching elements, with optional success and error feedback messages.

Parameters:
- selector (string, optional): CSS selector for the element(s) to make copyable. Default: '.copy-content'.
- class (string, optional): CSS class to add to the target element(s). Default: 'copy-icon'.
- showMsg (boolean, optional): Whether to display success/error messages after the copy operation. Default: true.
- msgDone (string, optional): Success message shown after copying. Default: '#i18n{portal.util.copy.done}'.
- msgError (string, optional): Error message shown if copy fails. Default: '#i18n{portal.util.copy.error}'.

Snippet:

    Basic usage with default selector:

    <@copyElementToClipboard />

    Custom selector with feedback disabled:

    <@copyElementToClipboard selector='.code-block' showMsg=false />

    Custom messages:

    <@copyElementToClipboard selector='.reference-number' msgDone='Copied!' msgError='Copy failed' />

-->
<#macro copyElementToClipboard selector='.copy-content' class='copy-icon' showMsg=true msgDone='#i18n{portal.util.copy.done}' msgError='#i18n{portal.util.copy.error}'>
<script>
const elems = document.querySelectorAll('${selector!}');
elems.forEach(elem => {
	<#if class !=''>elem.classList.add( ${class!} );</#if>
	elem.setAttribute('title', '#i18n{portal.util.labelCopy}');
  	elem.addEventListener('click', () => {
		const selection = window.getSelection();
		const range = document.createRange();
		range.selectNodeContents(elem);
		selection.removeAllRanges();
		selection.addRange(range);
		try {
			document.execCommand('copy');
			selection.removeAllRanges();
		<#if showMsg>
			const original = elem.textContent;
			elem.textContent = '${msgDone}';
			elem.classList.add('msg-success');
			setTimeout(() => {
				elem.textContent = original;
				elem.classList.remove('msg-success');
			}, 1200);
		</#if>
		} catch(e) {
		<#if showMsg>
			const original = elem.textContent;
			elem.textContent = '${msgError}';
			elem.classList.add('msg-error');
			setTimeout(() => {
				elem.textContent = original;
				elem.classList.remove('msg-danger');
			}, 1200);
		</#if>
    	}
  	});
});
</script>
</#macro>