<#--
Macro: boxHeader
Description: Generates an HTML element for a box header with an optional title, ID, class, and box tools.
Parameters:
- title (string, optional): the title of the box header.
- i18nTitleKey (string, optional): the i18n key of the title of the box header.
- hideTitle (list, optional): a list of screen sizes to hide the title of the box header on.
- showTitle (boolean, optional): whether to show the title of the box header.
- id (string, optional): the ID of the box header element. If not provided, a default ID will be generated.
- class (string, optional): the CSS class of the box header element.
- params (string, optional): additional HTML attributes to include in the box header element.
- boxTools (boolean, optional): whether to display box tools in the header.
- titleLevel (string, optional): the HTML heading level for the title of the box header.
- skipHeader (boolean, optional): whether to skip the box header element entirely.
-->
<#macro boxHeader title='' i18nTitleKey='' titleClass='' hideTitle=[] showTitle=true id='' collapsed=false class='' params='' boxTools=false titleLevel='h3' skipHeader=false>
<div class="card-header<#if class!=''> ${class}</#if><#if skipHeader> skip-header</#if>"<#if id!=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
	<${titleLevel} class="card-title<#if titleClass!=''> ${titleClass}</#if><#if showTitle=false> visually-hidden</#if>"><#if title!=''>${title}</#if><#if i18nTitleKey!=''>#i18n{${i18nTitleKey}}</#if></${titleLevel}>
	<#local nested><#nested></#local>
	<#if nested?has_content>
	<div class="ms-auto">
		<#nested>
	</div>
	</#if>
</div>
</#macro>