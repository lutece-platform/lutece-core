<#-- Macro: paginationItemCount

Description: Generates the item count and item per page controls for a pagination bar.

Parameters:
- paginator (object): an object that contains information about the current and next/previous pages in a list.
- combo (boolean, optional): whether to display a combo box for selecting the number of items per page (default is 0).
- nb_items_per_page (number, optional): the number of items to display per page (default is the value of the "nb_items_per_page" variable).
- showcount (boolean, optional): whether to display the item count (default is 1).
- showall (boolean, optional): whether to display a link to show all items on a single page (default is 0).

-->
<#macro paginationItemCount paginator combo=0 nb_items_per_page=nb_items_per_page showcount=1 showall=0>
<#-- Display item count -->
<#if showcount == 1 >
<@p class='text-start mb-0'>
<#if (paginator.labelItemCount)?? && paginator.labelItemCount?has_content>&#160;${paginator.labelItemCount} : <#else>#i18n{portal.util.labelItemCount} : </#if> ${paginator.itemsCount}
</@p>
</#if>
<#-- Display combo -->
<#if combo == 1 >
<@paginationCombo paginator=paginator nb_items_per_page=nb_items_per_page showall=showall />
</#if>
</#macro>