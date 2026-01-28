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
- params (string, optional): additional parameters to add to the HTML code.

-->
<#macro tabLink class='' hide=[] id='' active=false href='' title='' tabLabel='' tabIcon='' tabClass='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li class="nav-item<#if tabClass!=''> ${tabClass}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#local tabLinkClass = class + ' nav-link' />
<#if active><#local tabLinkClass += ' active' /></#if>
<#local tabLinkSettings = 'role="tab" aria-selected="${active?c}" aria-controls="${href?remove_beginning("#")}"' />
<#if href?contains('#') && href?contains('.jsp') == false>
	<#local tabLinkSettings += ' data-bs-toggle="tab"' />
	<#local tabLinkId = '${href?remove_beginning("#")}-tab' />
<#else>
	<#local tabLinkId = href?keep_after_last('/')?keep_before('.')?lower_case />
</#if>
<#if href=''>
	<#nested>
<#else>
	<@link class=tabLinkClass?trim href=href id=tabLinkId title=title params=tabLinkSettings>
		<#if tabIcon!=''><@icon style=tabIcon class='mr-1 me-1'/></#if> <#if tabLabel !=''>${tabLabel!}<#else>${title!}</#if>
		<#nested>
	</@link>
</#if>
</li>
</#macro>