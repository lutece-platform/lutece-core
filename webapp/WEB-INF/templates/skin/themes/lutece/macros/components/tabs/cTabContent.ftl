<#--
Macro: cTabContent

Description: Generates the tab content container that wraps all tab panes and includes the required tabs JavaScript.

Parameters:
- id (string, required): HTML id attribute for the tab content container.
- class (string, optional): Additional CSS classes. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Snippet:

    Basic tab content container:

    <@cTabContent id='myTabContent'>
        <@cTabPane id='overview' active=true>
            <p>Overview content here.</p>
        </@cTabPane>
        <@cTabPane id='details'>
            <p>Details content here.</p>
        </@cTabPane>
    </@cTabContent>

-->
<#macro cTabContent id class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@cBlock class='tab-content ${class!}' id=id params=params>
<#nested>
</@cBlock>
<#if themeTabsIsLoaded?? && themeTabsIsLoaded>
<#else>
<script src="${commonsSiteThemePath}${commonsSiteJsPath}theme-tabs.min.js"></script>
<#assign themeTabsIsLoaded = true />
</#if>
</#macro>