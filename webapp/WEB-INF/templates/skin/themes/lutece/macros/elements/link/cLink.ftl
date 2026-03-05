<#--
Macro: cLink

Description: Generates an HTML anchor element with label, optional title, target behavior, and nested content positioning. Supports styling as a button via CSS classes.

Parameters:
- href (string, required): URL the link points to.
- label (string, required): Text label displayed inside the link.
- title (string, optional): Value of the title attribute for the link. Default: ''.
- nestedPos (string, optional): Position of nested content relative to the label ('before' or 'after'). Default: 'after'.
- target (string, optional): Target attribute for link opening behavior ('_blank', '_top', '_parent', or a frame id). Default: ''.
- showTarget (boolean, optional): If true, displays an icon when target is '_blank'. Default: false.
- class (string, optional): CSS class(es) applied to the link element. Use 'btn btn-primary' to style as a button. Default: ''.
- id (string, optional): Unique identifier for the link element. Default: ''.
- params (string, optional): Additional HTML attributes for the link element. Default: ''.

Snippet:

    Basic usage:

    <@cLink href='/jsp/site/Portal.jsp?page=contact' label='Contact Us' title='Go to contact page' />

    Link styled as a button:

    <@cLink href='/jsp/site/Portal.jsp?page=register' label='Sign Up' class='btn btn-primary' />

    Link opening in a new window:

    <@cLink href='https://www.example.com' label='Visit Example' target='_blank' title='External site' />

    With nested content (icon after label):

    <@cLink href='/jsp/site/Portal.jsp?page=search' label='Search'>
        <i class='ti ti-search'></i>
    </@cLink>

-->
<#macro cLink href label title='' nestedPos='after' target='' showTarget=false class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<a href="${href!}" <#if id!=''> id="${id!}"</#if> <#if class !='' >class="${class!}"</#if><#if title!=''> title="${title!}<#if target='_blank'> #i18n{portal.theme.newWindowLink}</#if>"</#if><#if target!=''> target="${target}"</#if><#if params!=''> ${params}</#if> > 
<#if nestedPos!='after'><#nested></#if>
<#if label!=''><span class="link-label">${label!}</span></#if> 
<#if nestedPos='after'> <#nested></#if>
<#if target='_blank' && label=''><span class="visually-hidden visually-hidden-focusable"><#if title!=''>${title!}<#else>${label}</#if> #i18n{portal.theme.newWindowLink}</span></#if>
<#--  <#if target='_blank' && showTarget><svg width="18" height="18" viewBox="0 0 28 28" fill="currentColor" xmlns="http://www.w3.org/2000/svg"><g clip-path="url(#clip0_3081_1959)"><path d="M4.48047 23.3337V4.66699H13.4405V7.2759H6.98501V20.7248H19.8959V14.0003H22.4005V23.3337H4.48047ZM11.6808 17.6429L9.94359 15.8334L18.1588 7.2759H15.3307V4.66699H22.4005V12.0314H19.8959V9.08545L11.6808 17.6429Z" fill="currentColor"/></g><defs><clipPath id="clip0_3081_1959"><rect width="28" height="28" fill="white"/></clipPath></defs></svg></#if>  -->
</a>
</#macro>