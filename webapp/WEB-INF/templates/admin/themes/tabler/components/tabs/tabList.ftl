<#-- 
Macro: tabList

Description: Generates an HTML <ul> element containing tabs, with an optional ID, style, and various other features.

Parameters:
- style (string, optional): the style of the tab list.
- vertical (boolean, optional): whether or not the tab list is vertical.
- id (string, optional): the ID of the <ul> element containing the tab list.
- params (string, optional): additional parameters to add to the HTML code.
- color (string, optional): the color of the tab list.

Snippet:

    Basic horizontal tab list:

    <@tabList>
        <@tabLink href='#tab1' title='Overview' active=true />
        <@tabLink href='#tab2' title='Details' />
        <@tabLink href='#tab3' title='History' />
    </@tabList>

    Vertical tab list with pills style:

    <@tabList style='pills' vertical=true id='verticalTabs'>
        <@tabLink href='#profile' title='Profile' active=true />
        <@tabLink href='#account' title='Account' />
        <@tabLink href='#notifications' title='Notifications' />
    </@tabList>

-->
<#macro tabList style='tabs' vertical=false id='' class='' params='' color='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if propagateTabStyle?? ><#local style = propagateTabStyle /></#if>
<div class="card-header">
<ul class="nav nav-${style} card-header-tabs<#if vertical> flex-column mb-3</#if><#if class!=''> ${class}</#if>" data-bs-toggle="tabs"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> role="tablist">
<#nested>
</ul>
</div>
</#macro>