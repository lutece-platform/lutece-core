<#-- Macro: paginationPageLinks

Description: Generates the page links for a pagination bar.

Parameters:
- paginator (object): an object that contains information about the current and next/previous pages in a list.

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
		<@link href='${paginator.firstPageLink?xhtml}' class='page-link'>
			${paginator.labelFirst}
		</@link>
	</@li>
</#if>
<#if (paginator.pageCurrent > 1) >
	<@li class='page-item'>
		<@link href='${paginator.previousPageLink?xhtml}' class='page-link'>
			${paginator.labelPrevious}
		</@link>
	</@li>
<#else>
	<@li class='page-item disabled'>
		<@link href='${paginator.firstPageLink?xhtml}' class='page-link'>${paginator.labelPrevious}</@link>
	</@li>
</#if>
<#if ( paginator.pageCurrent - offsetPrev > 1 )>
	<@li class='page-item'>
		<@link href='${(paginator.pagesLinks?first).url?xhtml}' class='page-link'><strong>...</strong></@link>
	</@li>
</#if>
<#list paginator.pagesLinks as pageLink>
	<#if ( pageLink.index == paginator.pageCurrent )>
		<@li class='page-item active'>
			<@link href='${pageLink.url?xhtml}' class='page-link'>${pageLink.name}</@link>
		</@li>
	<#else>
		<@li class='page-item'>
			<@link href='${pageLink.url?xhtml}' class='page-link'>${pageLink.name}</@link>
		</@li>
	</#if>
</#list>
<#if ( paginator.pageCurrent + offsetNext < paginator.pagesCount )>
	<@li class='page-item'>
		<@link href='${(paginator.pagesLinks?last).url?xhtml}' class='page-link'><strong>...</strong></@link>
	</@li>
</#if>
<#if (paginator.pageCurrent < paginator.pagesCount) >
	<@li class='page-item next'>
		<@link href="${paginator.nextPageLink?xhtml}" class='page-link'>
			${paginator.labelNext}
		</@link>
	</@li>
	<#if ( paginator.pageCurrent + offsetNext < paginator.pagesCount )>
		<@li class='page-item next'>
			<@link href='${paginator.lastPageLink?xhtml}' class='page-link'>
				${paginator.labelLast}
			</@link>
		</@li>
	</#if>
<#else>
	<@li class='page-item disabled'>
		<@link href='${paginator.lastPageLink?xhtml}' class='page-link'>${paginator.labelNext}</@link>
	</@li>
</#if>
</@ul>
</#macro>