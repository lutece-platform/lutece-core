<#--
Macro: cDropdown

Description: Generates a dropdown button with a toggleable menu, populated from an items list or nested content.

Parameters:
- label (string, required): The button label text.
- items (object, optional): List of menu items with 'label', 'action', 'active', and 'disabled' attributes. Default: [].
- itemType (string, optional): Item rendering type ('link' for anchors, other for buttons). Default: 'link'.
- type (string, optional): Dropdown type ('split' for split button). Default: ''.
- centered (boolean, optional): If true, centers the dropdown in a button group. Default: false.
- dropup (boolean, optional): If true, opens the menu upward. Default: false.
- autoclose (string, optional): Auto-close behavior ('inside', 'outside', 'true', 'false'). Default: ''.
- nobutton (boolean, optional): If true, hides the toggle button. Default: false.
- header (string, optional): Header text for the dropdown menu. Default: ''.
- dark (boolean, optional): If true, uses a dark-themed menu. Default: false.
- dropDownMenuType (string, optional): HTML element type for the menu ('ul' or 'div'). Default: 'ul'.
- class (string, optional): Additional CSS class(es) for the dropdown container. Default: ''.
- btnClass (string, optional): CSS class for the button style ('primary', 'secondary', 'tertiary', 'danger'). Default: 'secondary'.
- noclass (boolean, optional): If true, removes default button classes. Default: false.
- id (string, optional): The unique identifier for the dropdown. Default: ''.
- disabled (boolean, optional): If true, disables the dropdown button. Default: false.
- params (string, optional): Additional HTML attributes for the dropdown. Default: ''.

Snippet:

    Dropdown with items list:

    <@cDropdown label='Actions' items=[
        {'label': 'Edit', 'action': 'jsp/site/Portal.jsp?page=edit', 'active': 'false', 'disabled': 'false'},
        {'label': 'Archive', 'action': 'jsp/site/Portal.jsp?page=archive', 'active': 'false', 'disabled': 'false'}
    ] />

    Dropdown with nested content:

    <@cDropdown label='More options' btnClass='primary'>
        <@cDropdownItem label='Profile' action='jsp/site/Portal.jsp?page=profile' />
        <@cDropdownItemDivider />
        <@cDropdownItem label='Logout' action='jsp/site/Portal.jsp?page=logout' />
    </@cDropdown>

-->
<#macro cDropdown label items=[] itemType='link' type='' centered=false dropup=false autoclose='' nobutton=false header='' dark=false dropDownMenuType='ul' class='' btnClass='secondary' noclass=false id='' disabled=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if centered>
<div class="btn-group ${direction}">
</#if>
<div class="dropdown<#if class!=''> ${class}</#if>"<#if id!=''> id="${id!}"</#if><#if params!=''>${params!}</#if>>
<#if !nobutton>
    <#if type !='split'>
    <button class="btn btn-${btnClass!} dropdown-toggle" type="button" data-bs-toggle="dropdown"<#if autoclose !=''> data-bs-auto-close="${autoclose!}" </#if> aria-expanded="false"<#if disabled> disabled</#if>>
        ${label!}
    </button>
    <#else>
    <button type="button" class="btn btn-${btnClass!}">${label!}</button>
    <button type="button" class="btn btn-${btnClass!} dropdown-toggle dropdown-toggle-split" data-bs-toggle="dropdown" aria-expanded="false">
        <span class="visually-hidden">#i18n{themeparisfr.labelDropdownToggle}</span>
    </button>
    </#if>
</#if>
    <${dropDownMenuType} class="dropdown-menu<#if dark> dropdown-menu-dark</#if>">
        <#if header !=''><li><h4 class="dropdown-header">${header!}</h6></li></#if>
        <#if items?has_content && items?size gt 0>
            <#list items as item>
                <li><#if itemType='link'><a class="dropdown-item<#if item.active='true'> active</#if><#if item.disabled='true'> disabled</#if>"<#if item.disabled='true'> aria-disabled="true"</#if> href="${item.action!}">${item.label!}</a><#else><button class="dropdown-item<#if item.active='true'> active</#if><#if item.disabled='true'> disabled</#if>"<#if item.disabled='true'> aria-disabled="true"</#if> type="${item.action}">${item.label!}</button></#if></li>
            </#list>
        </#if>
        <#nested>
    </${dropDownMenuType}>
</div>
<#if centered>
</div>
</#if>
</#macro>
<#--
Macro: cDropdownItem

Description: Generates a single item within a dropdown menu, rendered as a link or button.

Parameters:
- label (string, required): The menu item text.
- action (string, optional): URL for link items or button type ('button', 'submit') for button items. Default: ''.
- type (string, optional): Item type ('link' for anchor, other for button). Default: 'link'.
- active (boolean, optional): If true, marks the item as active. Default: false.
- disabled (boolean, optional): If true, disables the item. Default: false.
- header (boolean, optional): If true, renders the item as a dropdown header. Default: false.
- class (string, optional): Additional CSS class(es) for the item. Default: ''.
- nestedPos (string, optional): Position of nested content ('before', 'after'). Default: 'before'.
- id (string, optional): The unique identifier for the item. Default: ''.
- params (string, optional): Additional HTML attributes for the item. Default: ''.

Snippet:

    Link item:

    <@cDropdownItem label='My account' action='jsp/site/Portal.jsp?page=account' />

    Active button item:

    <@cDropdownItem label='Current page' type='button' action='button' active=true />

-->
<#macro cDropdownItem label action='' type='link' active=false disabled=false header=false class='' nestedPos='before' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li>
<#if type='link'>
<a class="dropdown-<#if !header>item<#else>header</#if><#if class!=''> ${class}</#if><#if active> active</#if><#if disabled> disabled</#if>"<#if disabled> aria-disabled="true"</#if><#if id!=''> id="${id!}"</#if><#if params!=''>${params!}</#if> href="${href!}">
<#if header><h4></#if><#if nestedPos='before'><#nested></#if><#if label!=''>${label!}</#if><#if nestedPos='after'><#nested></#if><#if header></h4></#if>
</a>
<#else>
<button class="dropdown-<#if !header>item<#else>header</#if><#if active> active</#if><#if disabled> disabled</#if>"<#if disabled> aria-disabled="true"</#if> type="${type}">
<#if header><h4></#if><#if nestedPos='before'><#nested></#if><#if label!=''>${label!}</#if><#if nestedPos='after'><#nested></#if><#if header></h4></#if>
</button>
</#if>
</li>
</#macro>
<#--
Macro: cDropdownItemDivider

Description: Generates a horizontal divider line between dropdown menu items.

Parameters:
None.

Snippet:

    Divider between dropdown items:

    <@cDropdownItem label='Edit' action='#' />
    <@cDropdownItemDivider />
    <@cDropdownItem label='Delete' action='#' />

-->
<#macro cDropdownItemDivider>
<li><hr class="dropdown-divider"></li>
</#macro>
