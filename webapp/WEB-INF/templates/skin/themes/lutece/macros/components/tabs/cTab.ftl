<#--
Macro: cTab

Description: Generates a single Bootstrap tab navigation item, either as a button or a list-item link, for use inside a cTabs container.

Parameters:
- url (string, required): Hash reference to the target tab pane (e.g., '#myPane'), also used to derive the tab id.
- id (string, optional): Custom HTML id for the tab. Default: '' (auto-generated from url).
- active (boolean, optional): Whether this tab is active on page load. Default: false.
- navigation (boolean, optional): Whether to render as a list item (li > a) instead of a button. Default: false.
- disabled (boolean, optional): Whether the tab is disabled. Default: false.
- class (string, optional): Additional CSS classes. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Snippet:

    Button tab (default):

    <@cTab url='#overview' active=true>
        Overview
    </@cTab>

    Navigation tab as list item:

    <@cTab url='#details' navigation=true>
        Details
    </@cTab>

    Disabled tab:

    <@cTab url='#settings' disabled=true>
        Settings
    </@cTab>

-->
<#macro cTab url id='' active=false navigation=false disabled=false class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if navigation>
<li class="nav-item" role="presentation">
	<a id="<#if id=''>tab_${url!?remove_beginning("#")}<#else>${id}</#if>" aria-controls="<#if id=''>${url!?remove_beginning("#")}<#else>${id}</#if>" class="nav-link<#if active> active</#if><#if disabled> disabled</#if><#if class!=''> ${class!}</#if>"<#if disabled> disabled tabindex="-1" aria-disabled="true"</#if><#if !active && !disabled> tabindex="-1"</#if> data-bs-toggle="tab" role="tab"<#if active> aria-selected="true"<#else> aria-selected="false"</#if> href="${url!}">
		<#nested>
	</a>
</li>
<#else>
<button type="button" id="<#if id=''>tab_${url!?remove_beginning("#")}<#else>${id}</#if>" aria-controls="<#if id=''>${url!?remove_beginning("#")}<#else>${id}</#if>" data-bs-toggle="tab" role="tab" data-bs-target="#<#if id=''>${url!?remove_beginning("#")}<#else>${id}</#if>"  class="nav-link<#if active> active</#if><#if disabled> disabled</#if><#if class!=''> ${class!}</#if>" <#if disabled> disabled tabindex="-1" aria-disabled="true"</#if><#if !active && !disabled> tabindex="-1"</#if><#if active> aria-selected="true"<#else> aria-selected="false"</#if>>
	<#nested>
</button>
</#if>
</#macro>