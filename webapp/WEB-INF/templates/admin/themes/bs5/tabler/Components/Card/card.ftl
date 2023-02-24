<#-- Macro: card

Description: Generates a Bootstrap card container with customizable attributes.

Parameters:
- header (boolean, optional): whether to include a header in the card.
- headerTitle (string, optional): the title of the card header.
- headerIcon (boolean, optional): whether to include an icon in the card header.
- headerTitleIcon (string, optional): the icon to display in the card header title.
-->
<#macro card header=false headerTitle='' headerIcon=false headerTitleIcon=''>
<div class="card mb-3">
	<#if header><div class="card-header bg-info"><#if headerIcon><@icon style='${headerTitleIcon}' />&#160;</#if>${headerTitle}</div></#if>
	<div class="card-body">
		<#nested>
	</div>
</div>
</#macro>