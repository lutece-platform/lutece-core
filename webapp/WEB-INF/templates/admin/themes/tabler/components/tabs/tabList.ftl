<#-- 
Macro: tabList

Description: Generates an HTML <ul> element containing tabs, with an optional ID, style, and various other features.

Parameters:
- style (string, optional): the style of the tab list.
- vertical (boolean, optional): whether or not the tab list is vertical.
- id (string, optional): the ID of the <ul> element containing the tab list.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.
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
<#macro tabList style='tabs' vertical=false id='' class='' params={} color='' deprecated...>
<@deprecatedWarning args=deprecated />
<#-- the style of the enclosing @tabs wins over the style parameter -->
<#local tabStyle = propagateTabStyle!style />
<#-- a list of in-page tabs is a tablist; a list of links to other pages (tabLink with a .jsp href) stays a list of links -->
<#local tabsContent><#nested></#local>
<#local inPageTabs = tabsContent?markup_string?contains( 'role="tab"' ) />
<div class="card-header">
<ul<@commonAttrs id=id classBase='nav nav-' + tabStyle + ' card-header-tabs' + vertical?then( ' flex-column mb-3', '' ) class=class params=params /><#if inPageTabs> data-bs-toggle="tabs" role="tablist"</#if>>
${tabsContent}
</ul>
</div>
</#macro>