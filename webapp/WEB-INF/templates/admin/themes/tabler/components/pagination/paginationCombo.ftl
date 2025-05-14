<#-- Macro: paginationCombo

Description: Generates a combo box for selecting the number of items per page in a pagination bar.

Parameters:
- paginator (object): an object that contains information about the current and next/previous pages in a list.
- nb_items_per_page (number, optional): the number of items to display per page (default is the value of the "nb_items_per_page" variable).
- showall (boolean, optional): whether to display an option to show all items on a single page (default is 0).
-->
<#macro paginationCombo paginator nb_items_per_page=nb_items_per_page showall=0>
<@formGroup labelFor='${paginator.itemsPerPageParameterName}' labelKey='#i18n{portal.util.labelItemCountPerPage}' formStyle='inline' class='align-items-center'>
<@inputGroup size='sm'>
	<@select params='data-max-item="${paginator.itemsCount}"' size='sm' name='${paginator.itemsPerPageParameterName}' id='${paginator.itemsPerPageParameterName}' title='${paginator.labelItemCountPerPage}'>
  		<#list [ "10" , "20" , "50" , "100" ] as nb>
  			<#if nb_items_per_page = nb >
  				<@option selected=true value=nb label=nb />
  			<#else>
  				<@option value=nb label=nb />
  			</#if>
  		</#list>
  		<#if showall ==1>
  			<#if paginator.itemsCount &gt; 100 >
				<#assign isItemSelected><#if nb_items_per_page?number = paginator.itemsCount?number >true<#else>false</#if></#assign>
  				<@option selected=isItemSelected?boolean value=paginator.itemsCount class=nb_items_per_page label='#i18n{portal.util.labelAll}' />
  			</#if>
  		</#if>
	</@select>
	<@inputGroupItem type='btn'>
		<@button type='submit' color='oultine' size='xs' title='#i18n{portal.util.labelRefresh}' buttonIcon='check' hideTitle=['all'] />
	</@inputGroupItem>
</@inputGroup>
</@formGroup>
</#macro>