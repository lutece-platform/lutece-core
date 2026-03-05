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

Snippet:

    Basic pagination:

    <@cPagination paginator=paginator />

    Pagination with anchor fragment:

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