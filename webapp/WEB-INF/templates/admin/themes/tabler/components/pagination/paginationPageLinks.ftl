<#-- Macro: paginationPageLinks

Description: Generates the page links for a pagination bar.

Parameters:
- paginator (object): an object that contains information about the current and next/previous pages in a list.

Snippet:

    Basic page links:

    <@paginationPageLinks paginator=paginator />

-->
<#macro paginationPageLinks paginator  deprecated...>
<@deprecatedWarning args=deprecated />
<#assign nbLinkPagesToDisplay = 10 />
<#assign offsetPrev = nbLinkPagesToDisplay / 2 />
<#assign offsetNext = nbLinkPagesToDisplay / 2 />
<#if ( paginator.pageCurrent <= nbLinkPagesToDisplay - offsetPrev )>
	<#assign offsetPrev = paginator.pageCurrent - 1 />
	<#assign offsetNext = nbLinkPagesToDisplay - offsetPrev />
<#elseif ( paginator.pageCurrent + offsetNext > paginator.pagesCount )>
	<#assign offsetNext = paginator.pagesCount - paginator.pageCurrent />
	<#assign offsetPrev = nbLinkPagesToDisplay - offsetNext />
</#if>
<@ul class='pagination'>
<#if ( paginator.pageCurrent - offsetPrev > 1 )>
	<@li class='page-item'>
		<#local esc1><#outputformat "HTML">${paginator.firstPageLink}</#outputformat></#local>
		<@link href='${esc1}' class='page-link'>
			${paginator.labelFirst}
		</@link>
	</@li>
</#if>
<#if (paginator.pageCurrent > 1) >
	<@li class='page-item'>
		<#local esc2><#outputformat "HTML">${paginator.previousPageLink}</#outputformat></#local>
		<@link href='${esc2}' class='page-link'>
			${paginator.labelPrevious}
		</@link>
	</@li>
<#else>
	<@li class='page-item disabled'>
		<#local esc3><#outputformat "HTML">${paginator.firstPageLink}</#outputformat></#local>
		<@link href='${esc3}' class='page-link'>${paginator.labelPrevious}</@link>
	</@li>
</#if>
<#if ( paginator.pageCurrent - offsetPrev > 1 )>
	<@li class='page-item'>
		<#local esc4><#outputformat "HTML">${(paginator.pagesLinks?first).url}</#outputformat></#local>
		<@link href='${esc4}' class='page-link'><strong>...</strong></@link>
	</@li>
</#if>
<#list paginator.pagesLinks as pageLink>
	<#if ( pageLink.index == paginator.pageCurrent )>
		<@li class='page-item active'>
			<#local esc5><#outputformat "HTML">${pageLink.url}</#outputformat></#local>
			<@link href='${esc5}' class='page-link'>${pageLink.name}</@link>
		</@li>
	<#else>
		<@li class='page-item'>
			<#local esc6><#outputformat "HTML">${pageLink.url}</#outputformat></#local>
			<@link href='${esc6}' class='page-link'>${pageLink.name}</@link>
		</@li>
	</#if>
</#list>
<#if ( paginator.pageCurrent + offsetNext < paginator.pagesCount )>
	<@li class='page-item'>
		<#local esc7><#outputformat "HTML">${(paginator.pagesLinks?last).url}</#outputformat></#local>
		<@link href='${esc7}' class='page-link'><strong>...</strong></@link>
	</@li>
</#if>
<#if (paginator.pageCurrent < paginator.pagesCount) >
	<@li class='page-item next'>
		<#local esc8><#outputformat "HTML">${paginator.nextPageLink}</#outputformat></#local>
		<@link href="${esc8}" class='page-link'>
			${paginator.labelNext}
		</@link>
	</@li>
	<#if ( paginator.pageCurrent + offsetNext < paginator.pagesCount )>
		<@li class='page-item next'>
			<#local esc9><#outputformat "HTML">${paginator.lastPageLink}</#outputformat></#local>
			<@link href='${esc9}' class='page-link'>
				${paginator.labelLast}
			</@link>
		</@li>
	</#if>
<#else>
	<@li class='page-item disabled'>
		<#local esc10><#outputformat "HTML">${paginator.lastPageLink}</#outputformat></#local>
		<@link href='${esc10}' class='page-link'>${paginator.labelNext}</@link>
	</@li>
</#if>
</@ul>
</#macro>