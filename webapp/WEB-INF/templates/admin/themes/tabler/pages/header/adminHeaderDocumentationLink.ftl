<#--
Macro: adminHeaderDocumentationLink
Description: Generates a link to the documentation for a specific administrative feature.
Parameters:
- feature_documentation (string, optional): the URL of the feature's documentation.
-->
<#macro adminHeaderDocumentationLink >
<#if feature_documentation?has_content >
<#if feature_documentation?exists>
<div class="position-fixed bottom-0 end-0 mb-1 me-1">
	<@link target="_blank" href="${feature_documentation}" title="#i18n{portal.features.documentation.help} [Nouvelle fenêtre]">
		<@icon class="fa-life-ring" /> <small>#i18n{portal.features.documentation.help}</small> 
	</@link>
</div>
</#if>
</#if>
</#macro>