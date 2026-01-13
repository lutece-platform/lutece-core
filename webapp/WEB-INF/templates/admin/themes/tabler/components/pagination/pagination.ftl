<#-- Macro: pagination

Description: Generates a pagination bar for navigating through pages of a list.

Parameters:
- paginator (object): an object that contains information about the current and next/previous pages in a list.
-->
<#macro pagination paginator deprecated...>
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
<#if ( paginator.pagesCount > 1 )>
	<#if ( paginator.pageCurrent - offsetPrev > 1 )>
		<@link href='${paginator.firstPageLink?xhtml}'>
			<@icon style='double-left' /> #i18n{portal.util.labelFirst}
		</@link>
	</#if>
	<#if ( paginator.pageCurrent > 1 )>
		<@link href='${paginator.previousPageLink?xhtml}'>
			<@icon style='angle-left' /> #i18n{portal.util.labelPrevious}
		</@link>
	<#else>
	</#if>
	<#if ( paginator.pageCurrent - offsetPrev > 1 )>
		<strong>...</strong>
	</#if>
	<#list paginator.pagesLinks as link>
		<#if link.index == paginator.pageCurrent>
			<strong>${link.name}</strong>
		<#else>
			<@link href='${link.url?xhtml}'>${link.name}</@link>
		</#if>
	</#list>
	<#if ( paginator.pageCurrent + offsetNext < paginator.pagesCount )>
		<strong>...</strong>
	</#if>
	<#if ( paginator.pageCurrent < paginator.pagesCount )>
		<@link href='${paginator.nextPageLink?xhtml}'>
			<@icon style='angle-right' /> #i18n{portal.util.labelNext}
		</@link>
		<#if ( paginator.pageCurrent + offsetNext < paginator.pagesCount )>
			<@link href='${paginator.lastPageLink?xhtml}'>
				<@icon style='angle-double-right' /> #i18n{portal.util.labelLast}
			</@link>
		</#if>
	</#if>
</#if>
</#macro>