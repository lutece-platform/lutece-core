<#-- Macro: paginationAdmin

Description: Generates a pagination bar for navigating through pages of a list, with options to display item count and select the number of items per page.

Parameters:
- paginator (object): an object that contains information about the current and next/previous pages in a list.
- combo (boolean, optional): whether to display a combo box for selecting the number of items per page (default is 0).
- form (boolean, optional): whether to wrap the item count and item per page controls in a form element (default is 1).
- nb_items_per_page (number, optional): the number of items to display per page (default is the value of the "nb_items_per_page" variable).
- showcount (boolean, optional): whether to display the item count (default is 1).
- showall (boolean, optional): whether to display a link to show all items on a single page (default is 0).

-->
<#macro paginationAdmin paginator combo=0 form=1 nb_items_per_page=nb_items_per_page showcount=1 showall=0>
<#if paginator??>
<@div class='d-flex is-flex justify-content-between is-justify-content-space-between'>
	<@div align='right'>
		<#if (paginator.pagesCount > 1) >
			<@paginationPageLinks paginator=paginator />
		</#if>
	</@div>
	<@div align='right'>
	<#if form == 1 >
		<@tform type=''>
			<@paginationItemCount paginator=paginator combo=combo nb_items_per_page=nb_items_per_page showcount=showcount showall=showall/>
		</@tform>
	<#else>
		<@paginationItemCount paginator=paginator combo=combo nb_items_per_page=nb_items_per_page showcount=showcount showall=showall/>
	</#if>
	</@div>
</@div>
</#if>
</#macro>