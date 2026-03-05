<#--
Macro: pageWrapper
Description: Generates a main container element for a page with a flexible width that fills the available space.

Parameters:
- class (string, optional): additional CSS classes for the page body wrapper.
- template (string, optional): additional CSS class for the inner container.

Snippet:

    Basic page wrapper:

    <@pageWrapper>
        <@pageHeader title='My Page' />
        <@pageRow>
            <@pageColumn>Content</@pageColumn>
        </@pageRow>
    </@pageWrapper>

    Page wrapper with custom class:

    <@pageWrapper class='py-4' template='template-dashboard'>
        <@pageHeader title='Dashboard' />
        <p>Dashboard content</p>
    </@pageWrapper>

-->
<#macro pageWrapper class='' template='' deprecated...>
<@deprecatedWarning args=deprecated />
<!-- BEGIN PAGE BODY -->
<div class="page-body<#if class!=''> ${class}</#if>">
    <div class="container-xl ${template}">
    <#nested>
    </div>
</div>
</#macro>