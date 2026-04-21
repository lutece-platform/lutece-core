<#--
Macro: cDropNav

Description: Generates a dropdown navigation group with a toggle button and a list of nested menu items.

Parameters:
- title (string, required): Label text displayed on the dropdown toggle button.
- icon (string, optional): HTML or icon markup displayed before the title. Default: ''.
- caret (string, optional): HTML or icon markup displayed after the title as a caret indicator. Default: ''.
- expanded (boolean, optional): Initial aria-expanded state of the dropdown. Default: false.
- id (string, optional): Unique identifier for the dropdown; auto-generated if empty. Default: ''.
- class (string, optional): Additional CSS class(es) for the dropdown wrapper. Default: ''.
- params (string, optional): Additional HTML attributes for the dropdown wrapper. Default: ''.

Snippet:

    Basic dropdown navigation:

    <@cDropNav title='Services'>
        <li><a class="dropdown-item" href="/service1">Service 1</a></li>
        <li><a class="dropdown-item" href="/service2">Service 2</a></li>
    </@cDropNav>

    Dropdown with icon and custom id:

    <@cDropNav title='My Account' icon='<i class="ti ti-user"></i>' id='account-menu'>
        <li><a class="dropdown-item" href="/profile">Profile</a></li>
        <li><a class="dropdown-item" href="/settings">Settings</a></li>
    </@cDropNav>

-->
<#macro cDropNav title icon='' caret='' expanded=false id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local cId='id-' + random()>
<#if id !=''><#local cId=id /></#if>
<div class="dropdown-group" <#if class !='' > ${class!}</#if>"<#if params!=''> ${params}</#if>>
    <button type="button" class="nav-link dropdown-toggle" href="#" id="dropdownMenu${cId!}" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="${expanded?c}">
        ${icon!} ${title!} ${caret!}
    </button>
    <ul class="dropdown-menu" aria-labelledby="dropdownMenu${cId!}">
        <div class="dropdown-content">
            <#nested>
        </div>
    </ul>
</div>
</#macro>