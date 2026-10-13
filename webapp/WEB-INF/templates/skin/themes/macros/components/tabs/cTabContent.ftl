<#--
Macro: cTabContent

Description: Generates the tab content container that wraps all tab panes and includes the required tabs JavaScript.

Parameters:
- id (string, required): HTML id attribute for the tab content container.
- class (string, optional): Additional CSS classes. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

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
<#macro cTabContent id class='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<@cBlock class='tab-content ${class!}' id=id params=params attrs=attrs>
<#nested>
</@cBlock>
</#macro>