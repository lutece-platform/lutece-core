<#--
Macro: cTabs

Description: Generates a Bootstrap tab navigation container wrapping individual cTab elements, with optional nav element semantics.

Parameters:
- navigation (boolean, optional): Whether to wrap tabs in a nav > ul structure (true) or a div structure (false). Default: false.
- id (string, optional): HTML id attribute. Default: ''.
- class (string, optional): Additional CSS classes. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Snippet:

    Basic tabs with button style:

    <@cTabs>
        <@cTab url='#overview' active=true>Overview</@cTab>
        <@cTab url='#details'>Details</@cTab>
    </@cTabs>

    Navigation tabs with nav element:

    <@cTabs navigation=true id='mainTabs'>
        <@cTab url='#overview' navigation=true active=true>Overview</@cTab>
        <@cTab url='#details' navigation=true>Details</@cTab>
    </@cTabs>

    Complete tabs example:

    <@cTabs>
        <@cTab url='#tab1' active=true>Tab 1</@cTab>
        <@cTab url='#tab2'>Tab 2</@cTab>
    </@cTabs>
    <@cTabContent id='tabContent'>
        <@cTabPane id='tab1' active=true>
            <p>Content for tab 1.</p>
        </@cTabPane>
        <@cTabPane id='tab2'>
            <p>Content for tab 2.</p>
        </@cTabPane>
    </@cTabContent>

-->
<#macro cTabs navigation=false id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if navigation>
	<nav>
		<div class="nav-tabs-container">
			<ul class="nav nav-tabs<#if class!=''> ${class!}</#if>"<#if id !=''> id="${id!}"</#if> role="tablist" <#if params!=''>${params!}</#if>>
				<#nested>
			</ul>
		</div>
	</nav>
<#else>
	<div class="nav-tabs-container">
		<div class="nav nav-tabs<#if class!=''> ${class!}</#if>"<#if id !=''> id="${id!}"</#if> role="tablist" <#if params!=''>${params!}</#if>>
			<#nested>
		</div>
	</div>
</#if>
</#macro>