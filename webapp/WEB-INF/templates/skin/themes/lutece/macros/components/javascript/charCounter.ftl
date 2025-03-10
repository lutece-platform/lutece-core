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
<#macro charcounter maxChars=0 selector='.lutece-charcounter' title='' defaultClass='text-normal' warningClass='text-warning' dangerClass='text-danger' id='' class=''  params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local charcounterTitle><#if title=''>#i18n{portal.util.labelCharCount}<#else>${title}</#if></#local>
// Limite la saisie du Titre a n chars
const counterElements = document.querySelectorAll( '${selector}' );
let maxChar = ${maxChars}, titleCount = '${title}',	n = 1
counterElements.forEach( ( counter ) => {
	const charInfo = document.createElement('div');
	charInfo.setAttribute( 'id',<#noparse>`char-info${n}`</#noparse> );
	charInfo.classList.add( 'form-text', 'p-1' );
	if( counter.dataset.luteceCounterTitle != null ) { titleCount = counter.dataset.luteceCounterTitle } else { counter.dataset.luteceCounterTitle = '${charcounterTitle!}' }
	charInfo.textContent = counter.dataset.luteceCounterTitle;
	const charCount = document.createElement('span');
	charCount.classList.add( 'ms-1' );
	charCount.setAttribute( 'id',<#noparse>`char-info${n}-count`</#noparse> );
	charCount.textContent = counter.textContent.trim().length;
	const charMax = document.createElement('span');
	charMax.setAttribute( 'id',<#noparse>`char-info${n}-max`</#noparse> );
	if( counter.dataset.luteceCounterMax != null ) { maxChar = counter.dataset.luteceCounterMax } else { counter.dataset.luteceCounterMax = maxChar }
	charMax.textContent = <#noparse>`/ ${maxChar}`</#noparse>;
	charInfo.appendChild( charCount )
	charInfo.appendChild( charMax )
	counter.after( charInfo )

	const charElement = document.getElementById(<#noparse>`char-info${n}-count`</#noparse>)
	
	counter.addEventListener( 'keydown', (e) => {
		const typedChar = e.currentTarget.textContent.length;
		const maxChar =  parseInt( e.currentTarget.dataset.luteceCounterMax ) + 1
		if ( typedChar >= maxChar ) {
			if ( event.keyCode != 8 )  e.preventDefault()
		} else {
			setCounter( typedChar, maxChar, charElement )
		}
	});
	n++;
})

function setCounter( counter, max, el ){
	el.textContent = counter;
	if ( counter > max ) {
		el.parentElement.classList.toggle('text-danger','fw-bold');
	} else if (counter < max && counter > max * ( 90 / 100 )) {
		el.parentElement.classList.toggle('text-warning', 'fw-semibold');
	} else if (counter < max - ((max * ( 10 / 100) ) - 1) ) {
		el.parentElement.classList.toggle('text-normal');
	}
}
</#macro>