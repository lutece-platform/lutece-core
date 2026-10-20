<#-- 
Macro: tabLink

Description: Generates a tab link with an optional ID, class, and various other features.

Parameters:
- id (string, optional): the ID of the <li> element containing the tab link.
- class (string, optional): the class of the <li> element containing the tab link.
- hide (string[], optional): an array of breakpoint names at which to hide the tab link.
- active (boolean, optional): whether or not the tab link is active.
- href (string, required): the href of the tab link.
- title (string, optional): the title of the tab link.
- tabLabel (string, optional): the label of the tab.
- tabIcon (string, optional): the icon of the tab.
- tabClass (string, optional): the class of the tab content.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Basic active tab link:

    <@tabLink href='#overview' title='Overview' active=true />

    Tab link with icon and label:

    <@tabLink href='#users' title='Users' tabIcon='users' tabLabel='User List' />

    Tab link with custom class and external URL:

    <@tabLink href='jsp/admin/ManageUsers.jsp' title='Manage Users' tabIcon='settings' tabClass='ms-auto' />

-->
<#macro tabLink class='' hide=[] id='' active=false href='' title='' tabLabel='' tabIcon='' tabClass='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local inPageTab = href?contains('#') && !href?contains('.jsp') />
<li class="nav-item<#if tabClass?has_content> ${tabClass}</#if>"<#if inPageTab> role="presentation"</#if><@commonAttrs id=id params=params />>
<#local tabLinkClass = class + ' nav-link' />
<#if active><#local tabLinkClass += ' active' /></#if>
<#if inPageTab>
	<#local tabLinkAttrs = { 'role': 'tab', 'aria-selected': active?c, 'aria-controls': href?remove_beginning('#'), 'data-bs-toggle': 'tab' } />
	<#local tabLinkId = '${href?remove_beginning("#")}-tab' />
<#else>
	<#local tabLinkAttrs = active?then( { 'aria-current': 'page' }, {} ) />
	<#local tabLinkId = href?keep_after_last('/')?keep_before('.')?lower_case />
</#if>
<#if !href?has_content>
	<#nested>
<#else>
	<@link class=tabLinkClass?trim href=href id=tabLinkId title=title params=tabLinkAttrs>
		<#if tabIcon?has_content><@icon style=tabIcon class='mr-1 me-1'/></#if> <#if tabLabel?has_content>${tabLabel!}<#else>${title!}</#if>
		<#nested>
	</@link>
</#if>
</li>
</#macro>