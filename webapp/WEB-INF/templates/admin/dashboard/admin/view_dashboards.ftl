<@row>	
	<@columns>
		<h1> <@icon style='cog' /> #i18n{portal.admindashboard.view_dashboards.title}</h1>
		<@row>	
			<@columns>
			<@accordionContainer id='technical_settings'>
			<#if column_content_1?has_content>
							${column_content_1}
			</#if>
			<#if column_content_2?has_content>
							${column_content_2}
			</#if>
			</@accordionContainer>
			</@columns>
		</@row>
	</@columns>
</@row>
