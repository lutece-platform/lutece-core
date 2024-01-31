<#-- 
Macro: copyElementToClipboard

Description: Attaches a click event listener to the specified element(s) that copies its contents to the clipboard when clicked. The macro also adds a tooltip to the element(s) that indicates it can be clicked to copy, and optionally displays a success or error message after the copy operation.

Parameters:
- selector (string, required): the CSS selector for the element(s) to attach the click event listener to.
- class (string, optional): the CSS class to add to the element(s).
- showMsg (boolean, optional): whether to display a success or error message after the copy operation.
- msgDone (string, optional): the success message to display.
- msgError (string, optional): the error message to display.

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