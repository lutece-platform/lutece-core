<#if portletType.createScriptTemplate != "" ><#include portletType.createScriptTemplate! /></#if>
	
	<@tform method='post' name='form' id='form-portlet' action='jsp/admin/${portletType.doCreateUrl}' params='target="_top"'>  
		<@input type='hidden' name='page_id' value='${portlet_page_id}' />
		<@input type='hidden' name='portlet_type_id' value='${portletType.id}' />
		<@fieldSet legend='#i18n{portal.site.portletType.labelCreate} &nbsp;${portletType.name}'>
				<@row>
					<@columns sm=3 md=3 lg=3>
						<@box color='default box-solid'>
							<@boxHeader title='' />
							<@boxBody>
								<@formGroup labelFor='column' labelKey='#i18n{portal.site.create_portlet.labelColumn}' rows=2>
									<@select name='column' default_value='1' items=portlet_columns_combo />
								</@formGroup>
								<@formGroup labelFor='order' labelKey='#i18n{portal.site.create_portlet.labelOrder}' rows=2>
									<@select name='order' default_value='1' items=portlet_order_combo />
								</@formGroup>
								<#if portletType.id != "ALIAS_PORTLET">
									<@formGroup labelFor='style' labelKey='#i18n{portal.site.create_portlet.labelStyle}' rows=2>
										<@select name='style' default_value='0' items=portlet_style_combo />
									</@formGroup>
								</#if>
								<@formGroup class='pull-right'>
									<@button type='button' id='bt-toggle' buttonIcon='chevron-down' color='secondary' />
								</@formGroup>
										
								<div id="params" style="clear:both;">
									<@fieldSet legend='#i18n{portal.site.admin_page.labelAdvancedParameters}'>
										<@formGroup labelFor='role' labelKey='#i18n{portal.site.create_portlet.labelRole}' rows=2>
											<@select name='role' default_value='none' items=portlet_role_combo />
										</@formGroup>
										<@formGroup labelKey='#i18n{portal.site.create_portlet.labelAlias}' rows=2>
											<@radioButton orientation='horizontal' name='accept_alias' value='1' labelKey='#i18n{portal.util.labelYes}' />
											<@radioButton orientation='horizontal' name='accept_alias' value='0' labelKey='#i18n{portal.util.labelNo}' checked=true />
										</@formGroup>
										<@formGroup labelKey='#i18n{portal.site.create_portlet.labelDisplayOnDevice}' rows=2>
											<@checkBox labelFor='display_on_small_device' name='display_on_small_device' id='display_on_small_device' value='1' checked=true labelKey='#i18n{portal.site.create_portlet.checkboxSmallDevice}' />
											<@checkBox labelFor='display_on_normal_device' name='display_on_normal_device' id='display_on_normal_device' value='1' checked=true labelKey='#i18n{portal.site.create_portlet.checkboxNormalDevice}' />
											<@checkBox labelFor='display_on_large_device' name='display_on_large_device' id='display_on_large_device' value='1' checked=true labelKey='#i18n{portal.site.create_portlet.checkboxLargeDevice}' />
											<@checkBox labelFor='display_on_xlarge_device' name='display_on_xlarge_device' id='display_on_xlarge_device' value='1' checked=true labelKey='#i18n{portal.site.create_portlet.checkboxXLargeDevice}' />
										</@formGroup>
									</@fieldSet>
								</div>
							</@boxBody>
						</@box>
					</@columns>

					<@columns sm=9 md=9 lg=9 id='content'>
						<@formGroup labelKey='#i18n{portal.site.create_portlet.labelPortletTitle}' labelFor='portlet_name' mandatory=true rows=2>
							<@input type='text' name='portlet_name' value='' maxlength=70 />
						</@formGroup>
						<@formGroup rows=2>
							<@radioButton orientation='horizontal' name='display_portlet_title' value='0' checked=true labelKey='#i18n{portal.site.create_portlet.radioButtonDisplayPortletTitleYes}' />
							<@radioButton orientation='horizontal' name='display_portlet_title' value='1' labelKey='#i18n{portal.site.create_portlet.radioButtonDisplayPortletTitleNo}' />
						</@formGroup>
						<#if portletType.createSpecificTemplate != "" >
							<#include portletType.createSpecificTemplate />
						</#if>
						<#if portletType.createSpecificFormTemplate != "" >
							<#include portletType.createSpecificFormTemplate />
						</#if>
						<@formGroup rows=2>
							<@button type='submit' buttonIcon='check' title='#i18n{portal.site.create_portlet.buttonValidate}' size='' />
							<@aButton href='jsp/admin/site/AdminSite.jsp' params='target="_top"' buttonIcon='close' title='#i18n{portal.site.create_portlet.buttonBack}' color='secondary' size='' />
						</@formGroup>
				</@columns> 
			</@row> 
		</@fieldSet>
	</@tform>

<script type="text/javascript">
$(document).ready(function(){
	$("#params").toggle();
	$("#bt-toggle").click(function(){
		$("#params").toggle();
		$("#bt-toggle > i").toggleClass("fa fa-chevron-down");
		$("#bt-toggle > i").toggleClass("fa fa-chevron-up");
	});
});
</script>