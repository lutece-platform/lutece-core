
<#-- Macro: adminMessagePage

Description: Generates a Bootstrap page for displaying an administrative message.

Parameters:
- title (string, optional): the title of the page.
- message (object): an object containing information about the message to be displayed, including its type and text.
- text (string): the text of the message.
-->
<#macro adminMessagePage title=''>
<#assign logoUrl = (dskey('portal.site.site_property.logo_url')!)?has_content?then(dskey('portal.site.site_property.logo_url'), 'themes/admin/shared/images/logo-header-icon.svg')>
<#assign title=title />
<#assign alerttype="primary" />
<#assign icontype="fa-info-circle" />
<#if title??>
	<#if title?trim='' >
		<#assign title="Information" />
	</#if>		
</#if>		
<#if message.type == 2 >
	 <#assign alerttype="danger" />
   <#assign icontype="fa-question-circle" />
<#elseif message.type == 3 >
   <#assign alerttype="warning" />
   <#assign icontype="fa-exclamation-circle" />
<#elseif message.type == 4 >
	 <#assign alerttype="warning" />
   <#assign icontype="fa-question-circle" />
<#elseif message.type == 5 >
   <#assign alerttype="danger" />
   <#assign icontype="fa-ban" />
</#if>
</head>
<body class="antialiased border-top-wide border-primary d-flex flex-column">
<@div class="page page-center">
	<@div class="text-center mb-4">
		<@link href=".">
			<@img url="${logoUrl}" alt='LUTECE' title='Lutece' params=' width="36"' />
		</@link>
	</@div>
	<@row class='justify-content-center align-items-center'>
		<@columns md=3>
			<@alert color=alerttype titleLevel='h1' title=title iconTitle=icontype params='flex-column'>
				<@p class="text-center">${text!}</@p>
				<#nested>
			</@alert>	
		</@columns>
	</@row>
</@div>
</#macro>