<#--
Macro: boxHeader
Description: Generates an HTML element for a box header with an optional title, ID, class, and box tools.
Parameters:
- title (string, optional): the title of the box header.
- i18nTitleKey (string, optional): the i18n key of the title of the box header.
- titleLevel (string, optional): the HTML heading level for the title of the box header, h2 by default so that it follows the h1 of the page header (the card-title class keeps its size).
- titleClass (string, optional): the CSS class of the box title element.
- titleParams (map, optional): additional HTML attributes of the box title element as name/value pairs, e.g. {'data-x': 'y'}. Default: {}.
- titleActions (boolean, optional): whether to display title actions in the header.
- titleAuto (boolean, optional): whether to automatically align title content to the right.
- hideTitle (list, optional): a list of screen sizes to hide the title of the box header on.
- showTitle (boolean, optional): whether to show the title of the box header.
- id (string, optional): the ID of the box header element. If not provided, a default ID will be generated.
- class (string, optional): the CSS class of the box header element.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.
- boxTools (boolean, optional): whether to display box tools in the header.
- skipHeader (boolean, optional): whether to skip the box header element entirely.

Snippet:

    Simple box header with title:

    <@boxHeader title='Dashboard' />

    Box header with i18n title key and action buttons:

    <@boxHeader i18nTitleKey='portal.users.pageTitle' titleActions=true>
        <@aButton href='jsp/admin/user/CreateUser.jsp' title='Add User' buttonIcon='plus' />
    </@boxHeader>

    Box header with custom heading level and box tools:

    <@boxHeader title='Recent Activity' titleLevel='h2' titleClass='fw-bold' boxTools=true>
        <@aButton href='#' buttonIcon='refresh-cw' size='sm' />
    </@boxHeader>

-->
<#macro boxHeader title='' i18nTitleKey='' titleClass='' titleLevel='h2' titleParams={} titleActions=false titleAuto=true hideTitle=[] showTitle=true collapsed=false id='' class='' params={} boxTools=false skipHeader=false deprecated...>
<@deprecatedWarning args=deprecated />
<div class="card-header<#if class?has_content> ${class}</#if><#if skipHeader> skip-header</#if>"<@commonAttrs id=id params=params />>
	<${titleLevel} class="card-title<#if titleClass?has_content> ${titleClass}</#if><#if showTitle=false> visually-hidden</#if>"<@commonAttrs params=titleParams />><#if title?has_content>${title}</#if><#if i18nTitleKey?has_content>#i18n{${i18nTitleKey}}</#if></${titleLevel}>
	<#local nested><#nested></#local>
	<#if nested?has_content>
	<#if titleActions>
		<div class="card-actions btn-actions">
			<#nested>
		</div>
	<#elseif titleAuto>
		<div class="ms-auto">
			<#nested>
		</div>
	<#else>
		<#nested>
	</#if>
	</#if>
</div>
</#macro>