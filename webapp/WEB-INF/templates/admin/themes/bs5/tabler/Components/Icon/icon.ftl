<#-- Macro: icon

Description: Generates an icon element with customizable attributes.

Parameters:
- prefix (string, optional): the prefix of the icon library.
- style (string, optional): the style of the icon.
- class (string, optional): additional CSS classes to add to the icon.
- title (string, optional): the title attribute of the icon.
- id (string, optional): the ID attribute of the icon.
- params (string, optional): additional parameters to add to the icon.
-->
<#macro icon prefix='fa-' style='fa-lg' class='' title='' id='' params=''>
<#if style='docker' || style = 'github' || style='gitlab' || style='java' || style='jira' || style='jenkins' || style = 'twitter' >
	<#local prefix = 'fab ' + prefix />
<#elseif prefix='fa-'>
	<#local prefix = 'fas ' + prefix />
<#else>
<#local prefix = prefix />
</#if>
<#switch style>
<#case 'arrows'>
	<#local iconStyle = 'arrows-alt' />
<#break>
<#case 'arrows-h'>
	<#local iconStyle = 'arrows-alt-h' />
<#break>
<#case 'arrows-v'>
	<#local iconStyle = 'arrows-alt-v' />
<#break>
<#case 'clock-o'>
	<#local iconStyle = 'clock' />
<#break>
<#case 'close'>
	<#local iconStyle = 'times' />
<#break>
<#case 'external-link'>
	<#local iconStyle = 'external-link-alt' />
<#break>
<#case 'file-pdf-o'>
	<#local iconStyle = 'file-pdf' />
<#break>
<#case 'pencil'>
	<#local iconStyle = 'edit' />
<#break>
<#case 'refresh'>
	<#local iconStyle = 'sync' />
<#break>
<#default>
<#local iconStyle = style />
</#switch>
<i class="${prefix}${iconStyle}<#if class!=''> ${class}</#if>" aria-hidden="true"<#if title!=''> title='${title}'</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>></i>
</#macro>