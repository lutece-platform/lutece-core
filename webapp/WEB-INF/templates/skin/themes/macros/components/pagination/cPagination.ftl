<#--
Macro: cPagination

Description: Generates a navigation pagination component from a Lutece Paginator object for browsing paged content on skin pages.

Parameters:
- paginator (object, required): Lutece Paginator object containing page links, current page, and page count.
- label (string, optional): Accessible label for the nav element. Default: 'Pagination'.
- fragment (string, optional): URL fragment appended to pagination links (e.g., anchor id). Default: ''.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Pagination - @cPagination
- bs: components/pagination
- newFeature: false

Snippet:

    Basic pagination:

    <@cPagination paginator=paginator />

    Pagination with anchor fragment:

    <@cPagination paginator=paginator fragment='results' label='Search results pagination' />

-->
<#macro cPagination paginator label='Pagination' fragment='' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<nav aria-label="${label!}" <@commonAttrs id=id class=class params=params /> >
<#if (paginator.pagesCount > 1) >
	<@cPaginationLinks paginator=paginator class=class fragment=fragment />
</#if>
</nav>
</#macro>