<#-- Macro: jsTreeSearch

Description: Generates a form group with a search input and a button to search the jstree.

-->
<#macro jsTreeSearch >
<@formGroup formStyle='inline' labelKey='#i18n{portal.site.admin_page.buttonSearchPage}' hideLabel=['all']>
	<@inputGroup size='sm'>
		<@input type='text' id='tree_search' name='tree_search' placeHolder='#i18n{portal.site.admin_page.buttonSearchPage}' />
		<@inputGroupItem>
			<@button id='btn-tree_search' color='link' type='button' title='#i18n{portal.site.admin_page.buttonSearchPage}' buttonIcon='search' hideTitle=['all'] />
		</@inputGroupItem>
	</@inputGroup>
</@formGroup>
</#macro>