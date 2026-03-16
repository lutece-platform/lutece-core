<#--
Macro: copyElementToClipboard

Description: Attaches a click event listener to the specified element(s) that copies its contents to the clipboard when clicked.
A copy button can be added next to each element. A toast notification is displayed on success or error.

Parameters:
- selector (string, required): the CSS selector for the element(s) to attach the click event listener to.
- class (string, optional): the CSS class to add to the element(s).
- btn (boolean, optional): whether to add a copy button next to each element.
- btnClass (string, optional): the CSS class to add to the copy button.
- btnAbsolute (boolean, optional): whether to position the copy button absolutely within the element.
- btnTop (string, optional): the top offset for the absolute copy button (e.g '5px').
- btnEnd (string, optional): the end offset for the absolute copy button (e.g '5px').
- showMsg (boolean, optional): whether to display a toast notification after the copy operation.
- msgDone (string, optional): the success message to display.
- msgError (string, optional): the error message to display.

Snippet:

    Enable copy-to-clipboard on elements with the default selector:

    <@copyElementToClipboard />

    Enable copy-to-clipboard on custom elements with an absolute button:

    <@copyElementToClipboard selector='.code-block' btn=true btnClass='link' btnAbsolute=true btnTop='5px' btnEnd='5px' />

    Enable copy-to-clipboard without a visible button or toast message:

    <@copyElementToClipboard selector='.secret-field' btn=false showMsg=false />

-->
<#macro copyElementToClipboard selector='.copy-content' class='' btn=true btnClass='link text-bg-light' btnAbsolute=false btnTop='0' btnEnd='0' showMsg=true msgDone='#i18n{portal.util.copy.done}' msgError='#i18n{portal.util.copy.error}' deprecated...>
<@deprecatedWarning args=deprecated />
<script>
document.querySelectorAll('${selector!}').forEach(elem => {
	<#if class != ''>elem.classList.add(...'${class!}'.split(' '));</#if>
	const btnTitle = elem.dataset.title != undefined ? elem.dataset.title : elem.getAttribute('title');
	const btnLabel = btnTitle != undefined ? <#noparse>`#i18n{portal.util.labelCopy} ${btnTitle}`</#noparse> : '#i18n{portal.util.labelCopy}';
	const copyContent = async () => {
		try {
			await navigator.clipboard.writeText(elem.textContent.trim());
			<#if showMsg>showCopyToast('${msgDone}', 'success');</#if>
		} catch {
			<#if showMsg>showCopyToast('${msgError}', 'danger');</#if>
		}
	};

	elem.addEventListener('click', copyContent);

	<#if btn>
	const btn = document.createElement('button');
	btn.type = 'button';
	btn.className = 'btn btn-sm btn-${btnClass} ms-2 px-1';
	btn.title = btnLabel;
	btn.setAttribute('aria-label', btnLabel);
	btn.innerHTML = '<i class="ti ti-copy" aria-hidden="true"></i>';
	<#if btnAbsolute>
	btn.style.position = 'absolute';
	btn.style.top = '${btnTop}';
	btn.style.right = '${btnEnd}';
	</#if>
	btn.addEventListener('click', (e) => {
		e.stopPropagation();
		copyContent();
	});
	elem.after(btn);
	<#if btnAbsolute>btn.parentElement.style.position = 'relative';</#if>
	</#if>
});

<#if showMsg>
function showCopyToast(message, type) {
	let container = document.getElementById('copy-toast-container');
	if (!container) {
		container = document.createElement('div');
		container.id = 'copy-toast-container';
		container.className = 'toast-container position-fixed top-0 end-0 p-3';
		container.setAttribute('aria-live', 'polite');
		container.setAttribute('aria-atomic', 'true');
		container.style.zIndex = '1090';
		document.body.appendChild(container);
	}
	const toastEl = document.createElement('div');
	toastEl.className = 'toast align-items-center text-bg-' + type + ' border-0';
	toastEl.setAttribute('role', 'status');
	toastEl.innerHTML = '<div class="d-flex align-items-center"><div class="toast-body">' + message + '</div>'
		+ '<button type="button" class="btn-close btn-close-white me-2" data-bs-dismiss="toast" aria-label="#i18n{portal.util.labelClose}"></button></div>';
	container.appendChild(toastEl);
	const toast = new bootstrap.Toast(toastEl, { delay: 5000, autohide: true });
	toast.show();
	toastEl.addEventListener('hidden.bs.toast', () => toastEl.remove());
}
</#if>
</script>
</#macro>