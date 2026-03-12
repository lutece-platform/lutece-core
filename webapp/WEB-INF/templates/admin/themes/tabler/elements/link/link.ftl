<#--
Macro: link
Description: Generates an HTML anchor link element with a specified URL, class, ID, name, title, alt text, target, and additional parameters.
Parameters:
- href (string, required): the URL for the link.
- class (string, optional): additional classes to add to the link element.
- id (string, optional): the ID for the link element.
- name (string, optional): the name for the link element.
- title (string, optional): the title for the link element.
- alt (string, optional): the alternative text for the link element.
- target (string, optional): the target for the link element, e.g., "_blank".
- params (string, optional): additional parameters to add to the HTML code.

Snippet:

    Simple link with nested content:

    <@link href='jsp/admin/ManageUsers.jsp'>
        Manage users
    </@link>

    Link with label, opening in a new tab:

    <@link href='https://lutece.paris.fr' label='Visit Lutece website' target='_blank' title='Opens in a new window' />

    Styled button link with custom class and ID:

    <@link href='jsp/admin/CreateUser.jsp' class='btn btn-primary' id='btn-create-user'>
        <i class="ti ti-plus"></i> Create User
    </@link>

-->
<#macro link href='' class='' id='' name='' label='' title='' alt='' target='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<a href="${href}"<#if class!=''> class="${class}"</#if><#if id!=''> id="${id}"</#if><#if name!=''> name="${name}"</#if><#if target!=''> target="${target}"</#if><#if title!=''> title="${title}"</#if><#if alt!=''> alt="${alt}"</#if><#if params!=''> ${params}</#if>>
<#if label !=''>${label}<#else><#nested></#if>
</a>
</#macro>