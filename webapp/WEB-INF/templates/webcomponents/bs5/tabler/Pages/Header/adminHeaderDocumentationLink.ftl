<#--
Macro: adminHeaderDocumentationLink

Description: Generates a link to the documentation for a specific administrative feature.

Parameters:
- feature_documentation (string, optional): the URL of the feature's documentation.

-->

<#macro adminHeaderDocumentationLink >
  <#if feature_documentation?has_content >
		<#if feature_documentation?exists>
			<@div id="lutece-doc-link" class="bg-light p-1">
				<@link target="_blank" href="${feature_documentation}" title="#i18n{portal.features.documentation.help} [Nouvelle fenêtre]">
					<@icon class="fa-life-ring" /> <small>#i18n{portal.features.documentation.help}</small> 
				</@link>
			</@div>
		</#if>
	</#if>
</#macro>