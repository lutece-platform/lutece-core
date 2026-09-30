<#-- Macro: noScriptMessage
Description: Generates a warning message when JavaScript is disabled in the user's browser.
Parameters:
- id (string, required): The ID of the main element to hide when JavaScript is disabled.
- styles (string, optional): Additional CSS styles for the noscript wrapper, default is 'height: 50vh;width: 100vw;'.
- wrapperClass (string, optional): The CSS class for the noscript wrapper (default is 'd-flex justify-content-center align-items-center').
- alertType (string, optional): The type of alert to display (default is 'warning').
- alertTitle (string, optional): The title of the alert message.
- linkUrl (string, optional): The URL for the link to reload the page (default is '.').
- linkClass (string, optional): The CSS class for the link button (default is 'btn btn-primary').
- linkLabel (string, optional): The label for the link button (default is 'Reload the page').
- linkIcon (string, optional): The HTML for the icon to display in the link button.

Snippet:
	Default sample : 
	<html>
		<head>
			<title>JavaScript Disabled</title>
		</head>
		<body>
			<@noScriptMessage />
			<main id="main">
				....
			</main>
		</body>
	</html>

	Plugin sample with custom parameters and main not hidden : 
	<html>
		<head>
			<title>JavaScript Disabled</title>
		</head>
		<body>
			<main id="main">
				<@noScriptMessage id='myForm' linkUrl='jsp/site/Portal.jsp?page=forms&view=stepView&id_form=${form.id}&init=true' />
				<form id="myForm">
					...
				</form>
				....
			</main>
		</body>
	</html>

-->
<#macro noScriptMessage id='main' styles='height:50vh;width:100vw;' wrapperClass='d-flex justify-content-center align-items-center' alertType='warning' alertTitle='#i18n{portal.util.noscript.alert.message}' linkUrl='.' linkClass='btn btn-primary' linkLabel='#i18n{portal.util.noscript.link.label}' linkIcon='<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-tabler icons-tabler-outline icon-tabler-reload ms-2"><path stroke="none" d="M0 0h24v24H0z" fill="none" /><path d="M19.933 13.041a8 8 0 1 1 -9.925 -8.788c3.899 -1 7.935 1.007 9.425 4.747" /><path d="M20 4v5h-5" /></svg> '>
<noscript>
<style>#${id}{ display : none };.breadcrumb-nav{ display : none };</style>
<@cBlock id='no-script-wrapper' class=wrapperClass params='style="${styles}"' >
    <@cAlert type=alertType title=alertTitle >
		<@cText class='text-center'><@cLink href=linkUrl class=linkClass label=''>${linkIcon} ${linkLabel}</@cLink></@cText>
	</@cAlert> 
</@cBlock>
</noscript>
</#macro>