<#--
Macro: cPaginationLinks

Description: Generates the internal page link list for a pagination component, including previous/next arrows and page number links.

Parameters:
- paginator (object, required): Lutece Paginator object containing page links, current page, and page count.
- fragment (string, optional): URL fragment appended to each pagination link. Default: ''.
- class (string, optional): Additional CSS classes for the pagination list. Default: ''.

Showcase:
- desc: Liens de pagination - @cPaginationLinks
- newFeature: false

Snippet:

    Basic usage (typically called internally by cPagination; paginator is the Paginator object of the model, written here as a hash):

    <#assign paginator = {
        'pageCurrent': 2, 'pagesCount': 3,
        'labelFirst': 'Première page', 'labelLast': 'Dernière page',
        'firstPageLink': 'jsp/site/Portal.jsp?page=search&page_index=1',
        'previousPageLink': 'jsp/site/Portal.jsp?page=search&page_index=1',
        'nextPageLink': 'jsp/site/Portal.jsp?page=search&page_index=3',
        'lastPageLink': 'jsp/site/Portal.jsp?page=search&page_index=3',
        'pagesLinks': [
            { 'index': 1, 'name': '1', 'url': 'jsp/site/Portal.jsp?page=search&page_index=1' },
            { 'index': 2, 'name': '2', 'url': 'jsp/site/Portal.jsp?page=search&page_index=2' },
            { 'index': 3, 'name': '3', 'url': 'jsp/site/Portal.jsp?page=search&page_index=3' }
        ]
    } />
    <@cPaginationLinks paginator=paginator />

    With fragment and custom class:

    <#assign paginator = {
        'pageCurrent': 2, 'pagesCount': 3,
        'labelFirst': 'Première page', 'labelLast': 'Dernière page',
        'firstPageLink': 'jsp/site/Portal.jsp?page=search&page_index=1',
        'previousPageLink': 'jsp/site/Portal.jsp?page=search&page_index=1',
        'nextPageLink': 'jsp/site/Portal.jsp?page=search&page_index=3',
        'lastPageLink': 'jsp/site/Portal.jsp?page=search&page_index=3',
        'pagesLinks': [
            { 'index': 1, 'name': '1', 'url': 'jsp/site/Portal.jsp?page=search&page_index=1' },
            { 'index': 2, 'name': '2', 'url': 'jsp/site/Portal.jsp?page=search&page_index=2' },
            { 'index': 3, 'name': '3', 'url': 'jsp/site/Portal.jsp?page=search&page_index=3' }
        ]
    } />
    <@cPaginationLinks paginator=paginator fragment='listing' class='justify-content-center' />

-->
<#macro cPaginationLinks paginator fragment='' class=''>
<#local nbLinkPagesToDisplay = 10 />
<#local offsetPrev = nbLinkPagesToDisplay / 2 />
<#local offsetNext = nbLinkPagesToDisplay / 2 />
<#if ( paginator.pageCurrent <= nbLinkPagesToDisplay - offsetPrev )>
<#local offsetPrev = paginator.pageCurrent - 1 />
<#local offsetNext = nbLinkPagesToDisplay - offsetPrev />
<#elseif ( paginator.pageCurrent + offsetNext > paginator.pagesCount )>
<#local offsetNext = paginator.pagesCount - paginator.pageCurrent />
<#local offsetPrev = nbLinkPagesToDisplay - offsetNext />
</#if>
<ul class="pagination mb-0<#if class?has_content> ${class}</#if>">
<#if ( paginator.pageCurrent - offsetPrev > 1 )>
	<li class="page-item">
		<a class="page-link" href="${paginator.firstPageLink}<#if fragment?has_content>#${fragment}</#if>" title="${paginator.labelFirst}">
			<svg xmlns="http://www.w3.org/2000/svg" width="9.638" height="15.153" viewBox="0 0 9.638 15.153">
				<g transform="translate(8.279 13.676) rotate(180)">
					<g transform="translate(0 0)">
						<path class="a" d="M1.309,0,0,1.423,4.3,6.1,0,10.776,1.309,12.2,6.92,6.1Z"/>
					</g>
				</g>
			</svg>
		</a>
	</li>
</#if>
<#if (paginator.pageCurrent > 1) >
	<li class="page-item">
		<a class="page-link" href="${paginator.previousPageLink}<#if fragment?has_content>#${fragment}</#if>">
			<svg xmlns="http://www.w3.org/2000/svg" width="9.638" height="15.153" viewBox="0 0 9.638 15.153" aria-hidden="true" focusable="false">
				<g transform="translate(8.279 13.676) rotate(180)">
					<g transform="translate(0 0)">
						<path class="a" d="M1.309,0,0,1.423,4.3,6.1,0,10.776,1.309,12.2,6.92,6.1Z"/>
					</g>
				</g>
			</svg>
			<span class="visually-hidden">#i18n{portal.theme.labelPreviousPage}</span>
		</a>
	</li>
<#else>
	<li class="page-item disabled">
		<span class="page-link">
			<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 9.638 15.153" aria-hidden="true" focusable="false">
				<g transform="translate(8.279 13.676) rotate(180)">
					<path class="a" d="M1.309,0,0,1.423,4.3,6.1,0,10.776,1.309,12.2,6.92,6.1Z"/>
				</g>
			</svg>
			<span class="visually-hidden">#i18n{portal.theme.labelPreviousPage}, #i18n{portal.theme.labelPageUnavailable}</span>
		</span>
	</li>
</#if>
<#if ( paginator.pageCurrent - offsetPrev > 1 )>
	<li class="page-item group">
		<a class="page-link cesure" href="${(paginator.pagesLinks?first).url}<#if fragment?has_content>#${fragment}</#if>" title="#i18n{portal.theme.labelNextGroup}"><strong>...</strong></a>
	</li>
</#if>
<#list paginator.pagesLinks as link>
	<#if ( link.index == paginator.pageCurrent )>
		<li class="page-item active" aria-current="page">
			<span class="page-link" title="page ${link.name} sur ${paginator.pagesCount}">${link.name}</span>
		</li>
	<#else>
		<li class="page-item">
			<a class="page-link" title="page ${link.name} sur ${paginator.pagesCount}" href="${link.url}<#if fragment?has_content>#${fragment}</#if>">${link.name}</a>
		</li>
	</#if>
</#list>
<#if ( paginator.pageCurrent + offsetNext < paginator.pagesCount) >
	<li class="page-item group">
		<a class="page-link cesure" href="${(paginator.pagesLinks?last).url}<#if fragment?has_content>#${fragment}</#if>" title="#i18n{portal.theme.labelNextGroup}" >
			<strong>...</strong>
		</a>
	</li>
</#if>
<#if (paginator.pageCurrent < paginator.pagesCount) >
	<li class="page-item">
		<a class="page-link" href="${paginator.nextPageLink}<#if fragment?has_content>#${fragment}</#if>">
			<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 9.638 15.153" aria-hidden="true" focusable="false">
				<g transform="translate(1.359 1.477)">
					<path class="a" d="M1.309,0,0,1.423,4.3,6.1,0,10.776,1.309,12.2,6.92,6.1Z"/>
				</g>
			</svg>
			<span class="visually-hidden">#i18n{portal.theme.labelNextPage}</span>
		</a>
	</li>
	<#if ( paginator.pageCurrent + offsetNext < paginator.pagesCount) >
		<li class="page-item">
			<a class="page-link" href="${paginator.lastPageLink}<#if fragment?has_content>#${fragment}</#if>" title="${paginator.labelLast}">
				${paginator.labelLast}
			</a>
		</li>
	</#if>
<#else>
	<li class="page-item disabled">
		<span class="page-link">
			<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 9.638 15.153" aria-hidden="true" focusable="false">
				<g transform="translate(1.359 1.477)">
					<path class="a" d="M1.309,0,0,1.423,4.3,6.1,0,10.776,1.309,12.2,6.92,6.1Z"/>
				</g>
			</svg>
			<span class="visually-hidden">#i18n{portal.theme.labelNextPage}, #i18n{portal.theme.labelPageUnavailable}</span>
		</span>
	</li>
</#if>
</ul>
</#macro>