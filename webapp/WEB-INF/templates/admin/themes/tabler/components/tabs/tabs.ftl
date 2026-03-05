<#-- 
Macro: tabs

Description: Generates an HTML <div> element containing tab panels, with an optional ID, style, color, and various other features.

Parameters:
- color (string, optional): the color of the tabs.
- style (string, optional): the style of the tabs.
- class (string, optional): additional classes to add to the HTML code.
- id (string, optional): the ID of the <div> element containing the tabs.
- hide (list, optional): a list of breakpoint-specific display settings to hide the element at certain screen sizes.
- collapsed (boolean, optional): whether or not the tabs are collapsed by default.
- params (string, optional): additional parameters to add to the HTML code.

Snippet:

    Basic tabs container:

    <@tabs id='myTabs'>
        <@tabList>
            <@tabLink href='#tab1' title='Tab 1' active=true />
            <@tabLink href='#tab2' title='Tab 2' />
        </@tabList>
        <@tabContent>
            <@tabPanel id='tab1' active=true>Content of tab 1</@tabPanel>
            <@tabPanel id='tab2'>Content of tab 2</@tabPanel>
        </@tabContent>
    </@tabs>

    Tabs with color and custom style:

    <@tabs id='styledTabs' color='bg-primary-lt' style='pills'>
        <@tabList>
            <@tabLink href='#info' title='Info' active=true />
            <@tabLink href='#settings' title='Settings' />
        </@tabList>
        <@tabContent>
            <@tabPanel id='info' active=true>Information content</@tabPanel>
            <@tabPanel id='settings'>Settings content</@tabPanel>
        </@tabContent>
    </@tabs>

    Collapsed tabs hidden on small screens:

    <@tabs id='responsiveTabs' collapsed=true hide=['sm']>
        <@tabList>
            <@tabLink href='#details' title='Details' active=true />
        </@tabList>
        <@tabContent>
            <@tabPanel id='details' active=true>Details content</@tabPanel>
        </@tabContent>
    </@tabs>

-->
<#macro tabs color='' style='tabs' class='' id='' hide=[] collapsed=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if collapsed><#local class += ' ' + 'collapse' /></#if>
<#if hide??><#local class += ' ' + displaySettings(hide,'block') /></#if>
<#assign propagateTabStyle = style />
<div class="card<#if color!=''> ${color}</#if><#if class?trim!=''> ${class?trim}</#if>" <#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</div>
</#macro>