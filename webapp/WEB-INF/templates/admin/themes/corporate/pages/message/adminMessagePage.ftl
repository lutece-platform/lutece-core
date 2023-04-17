
<#-- Macro: adminMessagePage

Description: Generates a Bootstrap page for displaying an administrative message.

Parameters:
- title (string, optional): the title of the page.
- message (object): an object containing information about the message to be displayed, including its type and text.
- text (string): the text of the message.
-->
<#macro adminMessagePage title='' >
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
<body class="lutece-message" data-bs-theme="light">
<div class="d-flex align-items-center justify-content-center vh-100">
	<@div class="container-sm">
		<@div class="text-center mb-4">
			<@link href=".">
               <img src="themes/admin/corporate/images/core-corporate.png" height="40" alt="Logo">
			</@link>
		</@div>
		<@row class='justify-content-center align-items-center'>
			<@columns md=6>
				<@box class="p-4 text-center shadow-lg">
					<@p class="fs-5 text-center">${text!}</@p>
					<#nested>
				</@box>	
			</@columns>
		</@row>
	</@div>
	<script>
	   const savedMod = localStorage.getItem('lutece-corporate-theme');
if (savedMod === 'dark') {
	   document.body.setAttribute('data-bs-theme', 'dark');
} else {
	   document.body.setAttribute('data-bs-theme', 'light');
}
	</script>
</#macro>