<#-- Macro: adminMessagePage

Description: Generates a Bootstrap page for displaying an administrative message.

Parameters:
- title (string, optional): the title of the page.
- message (object): an object containing information about the message to be displayed, including its type and text.
- text (string): the text of the message.
-->
<#macro adminMessagePage title=''>
<#assign logoUrl = (dskey('portal.site.site_property.logo_url')!)?has_content?then(dskey('portal.site.site_property.logo_url'), 'themes/admin/shared/images/logo-header.png')>
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
<body class="lutece-message" data-bs-theme="dark">
<main>
<@pageContainer class="w-100 flex-column align-items-center justify-content-center">
	<@pageRow class='justify-content-center align-items-center w-100'>
		<@pageColumn class="">
			<@box class="p-4 text-center shadow-lg">
				<@div class="text-center mb-4">
					<@img url=logoUrl alt=site_name! params='aria-hidden="true" style="width:33.25px"' />
				</@div>
				<@p class="fs-5 text-center">${text!}</@p>
				<#nested>
			</@box>
		</@pageColumn>
	</@pageRow>
</@pageContainer>
</main>
</#macro>