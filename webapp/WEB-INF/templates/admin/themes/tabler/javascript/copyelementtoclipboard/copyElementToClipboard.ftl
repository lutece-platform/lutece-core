<#--
Macro: copyElementToClipboard

Description: Attaches a click event listener to the specified element(s) that copies its contents to the clipboard when clicked.
A copy button can be added next to each element. A toast notification is displayed on success or error.

Parameters:
- selector (string, required): the CSS selector for the element(s) to attach the click event listener to.
- class (string, optional): the CSS class to add to the element(s).
- copyBtn (boolean, optional): whether to add a copy button next to each element.
- showMsg (boolean, optional): whether to display a toast notification after the copy operation.
- msgDone (string, optional): the success message to display.
- msgError (string, optional): the error message to display.

-->
<#macro copyElementToClipboard selector='.copy-content' class='' copyBtn=true showMsg=true msgDone='#i18n{portal.util.copy.done}' msgError='#i18n{portal.util.copy.error}' deprecated...>
<@deprecatedWarning args=deprecated />
<script>
document.querySelectorAll('${selector!}').forEach(elem => {
	<#if class != ''>elem.classList.add(...'${class!}'.split(' '));</#if>
	elem.setAttribute('title', '#i18n{portal.util.labelCopy}');

	const copyContent = async () => {
		try {
			await navigator.clipboard.writeText(elem.textContent.trim());
			<#if showMsg>showCopyToast('${msgDone}', 'success');</#if>
		} catch {
			<#if showMsg>showCopyToast('${msgError}', 'danger');</#if>
		}
	};

	elem.addEventListener('click', copyContent);

	<#if copyBtn>
	const btn = document.createElement('button');
	btn.type = 'button';
	btn.className = 'btn btn-sm btn-icon btn-link-primary ms-2';
	btn.title = '#i18n{portal.util.labelCopy}';
	btn.innerHTML = '<i class="ti ti-copy"></i>';
	btn.addEventListener('click', (e) => {
		e.stopPropagation();
		copyContent();
	});
	elem.after(btn);
	</#if>
});

<#if showMsg>
function showCopyToast(message, type) {
	let container = document.getElementById('copy-toast-container');
	if (!container) {
		container = document.createElement('div');
		container.id = 'copy-toast-container';
		container.className = 'toast-container position-fixed top-0 end-0 p-3';
		container.style.zIndex = '1090';
		document.body.appendChild(container);
	}
	const toastEl = document.createElement('div');
	toastEl.className = 'toast align-items-center text-bg-' + type + ' border-0';
	toastEl.setAttribute('role', 'alert');
	toastEl.innerHTML = '<div class="d-flex align-items-center"><div class="toast-body">' + message + '</div>'
		+ '<button type="button" class="btn-close btn-close-white me-2" data-bs-dismiss="toast" aria-label="#i18n{portal.util.labelClose}"></button></div>';
	container.appendChild(toastEl);
	const toast = new bootstrap.Toast(toastEl, { delay: 2000, autohide: true });
	toast.show();
	toastEl.addEventListener('hidden.bs.toast', () => toastEl.remove());
}
</#if>
</script>
</#macro>