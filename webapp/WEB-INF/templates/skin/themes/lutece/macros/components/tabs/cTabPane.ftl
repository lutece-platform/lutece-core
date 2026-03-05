<#--
Macro: cTabPane

Description: Generates a single tab pane panel for use inside a cTabContent container, displaying content associated with a cTab.

Parameters:
- id (string, required): Unique identifier matching the corresponding cTab url (without '#').
- active (boolean, optional): Whether this pane is visible on page load. Default: false.
- disabled (boolean, optional): Whether this pane is disabled. Default: false.
- class (string, optional): Additional CSS classes for the pane. Default: ''.
- bodyClass (string, optional): Additional CSS classes for the card body. Default: ''.
- title (string, optional): Title displayed in mobile view. Default: ''.
- titleLevel (number, optional): Heading level for the mobile title. Default: 3.
- params (string, optional): Additional HTML attributes. Default: ''.

Snippet:

    Active tab pane:

    <@cTabPane id='overview' active=true>
        <p>This is the overview content.</p>
    </@cTabPane>

    Tab pane with custom body class:

    <@cTabPane id='details' bodyClass='p-4'>
        <p>Detailed information here.</p>
    </@cTabPane>

-->
<#macro cTabPane id active=false disabled=false class='' bodyClass='' title='' titleLevel=3 params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="card tab-pane fade<#if active> show active</#if><#if disabled> disabled</#if><#if class!=''> ${class!}</#if>"<#if disabled> disabled aria-disabled="true"</#if> id="${id}" tabindex="0" role="tabpanel" aria-labelledby="tab_${id}" ${params!}>    
	<div class="card-body<#if bodyClass!=''> ${bodyClass!}</#if>">
		<#nested>
	</div>
</div>
</#macro>