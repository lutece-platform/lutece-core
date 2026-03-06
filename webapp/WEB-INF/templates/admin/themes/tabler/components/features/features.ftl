<#--
Macro: manageFeature

Description: Generates a container for listing manageable feature items as cards. Replaces the @table macro for CRUD entity lists, rendering items as a vertical list of cards instead of table rows.

Parameters:
- class (string, optional): additional classes to add to the row container. Default: 'mt-2'.
- colClass (string, optional): classes for the inner column wrapper. Default: 'space-y'.
- listClass (string, optional): classes for the unordered list element. Default: 'space-y'.
- id (string, optional): the ID of the container element.
- params (string, optional): additional HTML attributes to add to the container.

Snippet:

    Basic usage:

    <@manageFeature>
        <#list item_list as item>
            <@manageFeatureItem>
                ...
            </@manageFeatureItem>
        </#list>
    </@manageFeature>

    With custom ID and classes:

    <@manageFeature id='my-list' class='mt-4' colClass='gap-3'>
        ...
    </@manageFeature>

-->
<#macro manageFeature class='mt-2' colClass='space-y' listClass='space-y' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="row row-cards<#if class !=''> ${class}</#if>"<#if id !=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
    <div class="col ${colClass}">
        <ul class="list-unstyled ${listClass}">
            <#nested>
        </ul>
    </div>
</div>
</#macro>
<#--
Macro: manageFeatureItem

Description: Generates a single feature item rendered as a card within a manageFeature list. Each item is displayed as a list item containing a card with a row layout for its columns.

Parameters:
- class (string, optional): additional classes to add to the card element.
- align (string, optional): horizontal alignment of the row content. Default: 'start'.
- valign (string, optional): vertical alignment of the row content. Default: 'center'.
- liClass (string, optional): additional classes to add to the list item element.
- bodyClass (string, optional): additional classes to add to the card body element.
- id (string, optional): the ID of the card element.
- params (string, optional): additional HTML attributes to add to the card element.

Snippet:

    Basic usage with columns:

    <@manageFeatureItem>
        <@manageFeatureItemColumn>
            <strong>${item.name}</strong>
        </@manageFeatureItemColumn>
        <@manageFeatureItemColumn auto=true align='end'>
            <@aButton href='action?id=${item.id}' title='Edit' buttonIcon='pencil' />
        </@manageFeatureItemColumn>
    </@manageFeatureItem>

    With liClass for client-side search filtering:

    <@manageFeatureItem liClass='item-labels'>
        <@manageFeatureItemColumn class='searchable'>
            ${item.name}
        </@manageFeatureItemColumn>
    </@manageFeatureItem>

    With top-aligned content and custom alignment:

    <@manageFeatureItem valign='top' align='between'>
        ...
    </@manageFeatureItem>

-->
<#macro manageFeatureItem class='' align='start' valign='center' liClass='' bodyClass='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li class="<#if liClass !=''> ${liClass}</#if>">
    <div class="card<#if class !=''> ${class}</#if>"<#if id !=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
        <div class="card-body<#if bodyClass !=''> ${bodyClass}</#if>">
            <div class="row g-3 justify-content-${align} align-items-${valign}">
            <#nested>
            </div>
        </div>
    </div>
</li>
</#macro>
<#--
Macro: manageFeatureItemColumn

Description: Generates a column within a manageFeatureItem row. Supports flexible sizing with auto-width, fixed column spans, and configurable flex direction and alignment.

Parameters:
- auto (boolean, optional): whether the column should auto-size to its content. Default: false.
- flex (boolean, optional): whether to apply flexbox layout to the column. Default: true.
- bp (string, optional): the breakpoint for responsive column sizing when using auto or fixed spans. Default: 'md'.
- dir (string, optional): the flex direction of the column content. Default: 'row'.
- cols (string, optional): the column span size (e.g. '3', '6', '12').
- valign (string, optional): vertical alignment of the column content. Default: 'center'.
- align (string, optional): horizontal alignment of the column content. Default: 'start'.
- class (string, optional): additional classes to add to the column element.
- id (string, optional): the ID of the column element.
- params (string, optional): additional HTML attributes to add to the column element.

Snippet:

    Default fluid column (takes remaining space):

    <@manageFeatureItemColumn>
        <strong>${item.name}</strong>
    </@manageFeatureItemColumn>

    Auto-sized column aligned to end (for action buttons):

    <@manageFeatureItemColumn auto=true align='end'>
        <@aButton href='modify?id=${item.id}' title='Edit' buttonIcon='pencil' hideTitle=['all'] />
        <@aButton href='delete?id=${item.id}' title='Delete' buttonIcon='trash' color='danger' hideTitle=['all'] />
    </@manageFeatureItemColumn>

    Fixed column span with vertical stacking:

    <@manageFeatureItemColumn cols='4' dir='column'>
        <@p class='fw-bold'>${item.label}</@p>
        <@p>${item.description}</@p>
    </@manageFeatureItemColumn>

    Without flexbox layout:

    <@manageFeatureItemColumn flex=false>
        ${item.htmlContent}
    </@manageFeatureItemColumn>

-->
<#macro manageFeatureItemColumn bp='md' auto=false flex=true dir='row' cols='' valign='center' align='start' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="col-${bp}<#if flex> d-${bp}-flex align-self-${valign} justify-content-${align} flex-${dir}</#if><#if cols !=''> col-${cols}</#if><#if auto> col-${bp}-auto</#if><#if class !=''> ${class}</#if>"<#if id !=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
<#nested>
</div>
</#macro>