<#--
Macro: cPagination

Description: Generates a navigation pagination component from a Lutece Paginator object for browsing paged content on skin pages.

Parameters:
- paginator (object, required): Lutece Paginator object containing page links, current page, and page count.
- label (string, optional): Accessible label for the nav element. Default: 'Pagination'.
- fragment (string, optional): URL fragment appended to pagination links (e.g., anchor id). Default: ''.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Showcase:
- desc: Pagination - @cPagination
- bs: components/pagination
- newFeature: false

Snippet:

    Basic pagination (paginator is the Paginator object put in the model by the Java code; written here as a hash with the same properties):

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
    <@cPagination paginator=paginator />

    Pagination with anchor fragment:

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
    <@cPagination paginator=paginator fragment='results' label='Search results pagination' />

-->
<#macro cPagination paginator label='Pagination' fragment='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<nav aria-label="${label!}" <#if id!=''> id="${id}"</#if><#if class!=''> class="${class}"</#if><#if params!=''> ${params}</#if> >
<#if (paginator.pagesCount > 1) >
	<@cPaginationLinks paginator=paginator class=class fragment=fragment />
</#if>
</nav>
</#macro>