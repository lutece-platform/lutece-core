<#-- Macro: adminMessagePage

Description: Generates a Bootstrap page for displaying an administrative message.

Parameters:
- title (string, optional): the title of the page.
- message (object): an object containing information about the message to be displayed, including its type and text.
- text (string): the text of the message.
-->
<#macro adminMessagePage title=''>
<#assign logoUrl = (dskey('portal.site.site_property.logo_url')!)?has_content?then(dskey('portal.site.site_property.logo_url'), 'themes/admin/shared/images/logo-header.svg')>
	<#assign title=title />
	<#assign alerttype="primary" />
	<#assign icontype="fa-info-circle" />
	<#if title??>
		<#if title?trim=''>
			<#assign title="Information" />
		</#if>
	</#if>
	<#if message.type == 2>
		<#assign alerttype="danger" />
		<#assign icontype="fa-question-circle" />
	<#elseif message.type == 3>
		<#assign alerttype="warning" />
		<#assign icontype="fa-exclamation-circle" />
	<#elseif message.type == 4>
		<#assign alerttype="warning" />
		<#assign icontype="fa-question-circle" />
	<#elseif message.type == 5>
		<#assign alerttype="danger" />
		<#assign icontype="fa-ban" />
	</#if>
</head>
<body class="lutece-message"  data-bs-theme="light">
	<div class="d-flex align-items-center justify-content-center vh-100 flex-column">
		<@div class="container-sm w-100 d-flex flex-column align-items-center justify-content-end">
			<@row class='justify-content-center align-items-center w-100'>
				<@columns md=6>
					<@box class="p-4 text-center shadow-lg">
						<@div class="text-center mb-4">
							<@img url="${logoUrl}" alt="${site_name!}" params='height="35" aria-hidden="true"' />
						</@div>
						<@p class="fs-5 text-center">${text!}</@p>
						<#nested>
					</@box>
				</@columns>
			</@row>
		</@div>
	</div>
<script>
const savedTheme = localStorage.getItem('lutece-corporate-theme');
if ( savedTheme ) {
    document.body.setAttribute('data-bs-theme', savedTheme);
}
</script>
</body>
</#macro>