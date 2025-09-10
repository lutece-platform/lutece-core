
<#-- Macro: adminMessagePage

Description: Generates a Bootstrap page for displaying an administrative message.

Parameters:
- title (string, optional): the title of the page.
- message (object): an object containing information about the message to be displayed, including its type and text.
- text (string): the text of the message.
-->
<#macro adminMessagePage title=''>
<#local logoUrl = (dskey('portal.site.site_property.logo_url')!)?has_content?then(dskey('portal.site.site_property.logo_url')?trim, '')>
<#local logoSvg = (dskey('portal.site.site_property.logo_svg.textblock')!)?has_content?then(dskey('portal.site.site_property.logo_svg.textblock'), '')>
<#local title=title />
<#local alerttype='primary' />
<#local iconcolor='primary' />
<#local icontype='<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-exclamation-circle" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M12 12m-9 0a9 9 0 1 0 18 0a9 9 0 1 0 -18 0" /> <path d="M12 9v4" /><path d="M12 16v.01" /></svg>' />
<#if title??><#if title?trim='' ><#local title='Information' /></#if></#if>		
<#switch message.type >
	<#case 2 >
		<#local alerttype='danger' />
		<#local iconcolor='danger' />
   		<#local icontype='<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-question-mark" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M8 8a3.5 3 0 0 1 3.5 -3h1a3.5 3 0 0 1 3.5 3a3 3 0 0 1 -2 3a3 4 0 0 0 -2 4" /><path d="M12 19l0 .01" /></svg>' />
		<#break>
	<#case 3 >
   		<#local alerttype='warning' />
		<#local iconcolor='warning' />
   		<#local icontype='<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-exclamation-circle" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M12 12m-9 0a9 9 0 1 0 18 0a9 9 0 1 0 -18 0" /> <path d="M12 9v4" /><path d="M12 16v.01" /></svg>' />
		<#break>
	<#case 4 >
		<#local alerttype='warning' />
		<#local iconcolor='warning' />
   		<#local icontype='<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-question-mark" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M8 8a3.5 3 0 0 1 3.5 -3h1a3.5 3 0 0 1 3.5 3a3 3 0 0 1 -2 3a3 4 0 0 0 -2 4" /><path d="M12 19l0 .01" /></svg>' />
		<#break>
	<#case 5 >
   		<#local alerttype='danger' />
   		<#local iconcolor='danger' />
   		<#local icontype='<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-ban" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M12 12m-9 0a9 9 0 1 0 18 0a9 9 0 1 0 -18 0" /><path d="M5.7 5.7l12.6 12.6" /></svg>' />
		<#break>
</#switch>
</head>
<body >
<@div class="page page-center">
	<@div class="container">
		<@div class="text-center mb-4">
			<@link href=".">
				<#if logoUrl==''>
						${logoSvg!}
					<#else>
            			<img src="${logoUrl}" width="32" height="32" alt="Logo du site" aria-hidden="true">
					</#if>
			</@link>
		</@div>
		<@row class='justify-content-center align-items-center'>
			<@columns md=3>
				<@card status=alerttype stamp=icontype stampColor=iconcolor >
					<@p class="text-center">${text!}</@p>
					<#nested>
				</@card>	
			</@columns>
		</@row>
	</@div>
</@div>
<script>
document.documentElement.classList.remove('loading');
</script>
</#macro>