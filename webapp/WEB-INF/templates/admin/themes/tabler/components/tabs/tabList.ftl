<#-- 
Macro: tabList

Description: Generates an HTML <ul> element containing tabs, with an optional ID, style, and various other features.

Parameters:
- style (string, optional): the style of the tab list.
- vertical (boolean, optional): whether or not the tab list is vertical.
- id (string, optional): the ID of the <ul> element containing the tab list.
- params (string, optional): additional parameters to add to the HTML code.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.
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
<#macro tabList style='tabs' vertical=false id='' class='' params='' attrs={} color='' deprecated...>
<@deprecatedWarning args=deprecated />
<#-- the style of the enclosing @tabs wins over the style parameter -->
<#local tabStyle = propagateTabStyle!style />
<div class="card-header">
<ul<@commonAttrs id=id classBase='nav nav-' + tabStyle + ' card-header-tabs' + vertical?then( ' flex-column mb-3', '' ) class=class params=params attrs=attrs /> data-bs-toggle="tabs" role="tablist">
<#nested>
</ul>
</div>
</#macro>