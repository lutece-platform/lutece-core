<#if portletType.modifyScriptTemplate != "" ><#include portletType.modifyScriptTemplate /></#if>
	<@tform method='post' id='form-portlet' name='form' action='jsp/admin/${portletType.doModifyUrl}' params='target="_top"'>
		<@input type='hidden' name='portlet_id' value='${portlet.id}' />
		<@input type='hidden' name='portlet_type_id' value='${portletType.id}' />
		<@fieldSet legend='${portletType.name} #i18n{portal.site.portletType.labelModify}'>
			<@row>
				<@columns sm=3 md=3 lg=3 id='panel'>
					<@box color='default box-solid'>
						<@boxHeader title='' />
						<@boxBody>
							<@formGroup labelFor='page_id' labelKey='#i18n{portal.site.modify_portlet.labelPortletPageId}' rows=2>
								<@input type='text' name='page_id' value='${portlet.pageId}' />
							</@formGroup>
							<@formGroup labelFor='column' labelKey='#i18n{portal.site.create_portlet.labelColumn}' rows=2>
								<@select name='column' default_value='${portlet.column}' items=portlet_columns_combo />
							</@formGroup>
							<@formGroup labelFor='order' labelKey='#i18n{portal.site.create_portlet.labelOrder}' rows=2>
								<@select name='order' default_value='${portlet.order}' items=portlet_order_combo />
							</@formGroup>
							<#if portletType.id != "ALIAS_PORTLET">
							<@formGroup labelFor='style' labelKey='#i18n{portal.site.create_portlet.labelStyle}' rows=2>
								<@select name='style' default_value='${portlet.styleId}' items=portlet_style_combo />
							</@formGroup>
							</#if>
							<@formGroup rows=2 class='pull-right'>
								<@button type='button' id='bt-toggle' buttonIcon='chevron-down' color='secondary' />
							</@formGroup>
							
							<div id="params" style="clear:both;">
								<@fieldSet legend='#i18n{portal.site.admin_page.labelAdvancedParameters}'>
									<@formGroup labelFor='role' labelKey='#i18n{portal.site.create_portlet.labelRole}' rows=2>
										<@select name='role' default_value='${portlet.role}' items=portlet_role_combo />
									</@formGroup>
									
									<@formGroup formStyle='inline' labelKey='#i18n{portal.site.create_portlet.labelAlias}' rows=2>
										<#if portlet.acceptAlias = 1>
											<#assign checked = true>
										<#else>
											<#assign checked = false>
										</#if>
										<@radioButton name='accept_alias' value='1' checked=checked labelKey='#i18n{portal.util.labelYes}' />
										<#if portlet.acceptAlias = 0>
											<#assign checked = true>
										<#else>
											<#assign checked = false>
										</#if>
										<@radioButton name='accept_alias' value='0' checked=checked labelKey='#i18n{portal.util.labelNo}' />
									</@formGroup>
									
									<@formGroup labelKey='#i18n{portal.site.create_portlet.labelDisplayOnDevice}' rows=2>
										<#if small_checked = 'checked="checked"'>
											<#assign checked = true>
										<#else>
											<#assign checked = false>
										</#if>
										<@checkBox name='display_on_small_device' id='display_on_small_device' value='1' checked=checked labelKey='#i18n{portal.site.create_portlet.checkboxSmallDevice}' />

										<#if normal_checked = 'checked="checked"'>
											<#assign checked = true>
										<#else>
											<#assign checked = false>
										</#if>
										<@checkBox name='display_on_normal_device' id='display_on_normal_device' value='1' checked=checked labelKey='#i18n{portal.site.create_portlet.checkboxNormalDevice}' />

										<#if large_checked = 'checked="checked"'>
											<#assign checked = true>
										<#else>
											<#assign checked = false>
										</#if>
										<@checkBox name='display_on_large_device' id='display_on_large_device' value='1' checked=checked labelKey='#i18n{portal.site.create_portlet.checkboxLargeDevice}' />

										<#if xlarge_checked = 'checked="checked"'>
											<#assign checked = true>
										<#else>
											<#assign checked = false>
										</#if>
										<@checkBox name='display_on_xlarge_device' id='display_on_xlarge_device'value='1' checked=checked labelKey='#i18n{portal.site.create_portlet.checkboxXLargeDevice}' />
									</@formGroup>
								</@fieldSet>
							</div>
						</@boxBody>
					</@box>
				</@columns>
						
				<@columns sm=9 md=9 lg=9 id='content'>
					<@formGroup labelFor='portlet_name' labelKey='#i18n{portal.site.create_portlet.labelPortletTitle}' rows=2>
						<@input type='text' name='portlet_name' value='${portlet.name}' maxlength=70 />
					</@formGroup>
					<@formGroup rows=2>
						<#if portlet.displayPortletTitle = 0>
							<#assign checked = true>
						<#else>
							<#assign checked = false>
						</#if>
						<@radioButton orientation='horizontal' name='display_portlet_title' value='0' checked=checked labelKey='#i18n{portal.site.create_portlet.radioButtonDisplayPortletTitleYes}' />
						<#if portlet.displayPortletTitle = 1>
							<#assign checked = true>
						<#else>
							<#assign checked = false>
						</#if>
						<@radioButton orientation='horizontal' name='display_portlet_title' value='1' checked=checked labelKey='#i18n{portal.site.create_portlet.radioButtonDisplayPortletTitleNo}' />
					</@formGroup>

					<#if portletType.modifySpecificTemplate != "" >
						<#include portletType.modifySpecificTemplate />
					</#if>
					<#if portletType.modifySpecificFormTemplate != "" >
						<#include portletType.modifySpecificFormTemplate />
					</#if>
							
					<@formGroup rows=2>
						<@button type='submit' buttonIcon='check' title='#i18n{portal.site.create_portlet.buttonValidate}' size=''  />
						<@aButton href='jsp/admin/site/AdminSite.jsp?page_id=${portlet.pageId}' params='target="_top"' color='secondary' buttonIcon='close' title='#i18n{portal.site.create_portlet.buttonBack}' size=''  />
					</@formGroup>
				</@columns>
			</@row>
		</@fieldSet>
	</@tform>

<script type="text/javascript">
$(document).ready(function(){
	$("legend").first().prepend('<a id="trigger" class="btn btn-flat btn-sm btn-info" href="#" title="#i18n{portal.site.portlet.labelShowHideProperties}"><i class="fa fa-chevron-left"></i></a>&#160;');
	$("#trigger").click(function(){
		$("#panel").toggle("fast");
		$("#content").toggleClass("col-sm-9 col-md-9 col-lg-9");
		$("#content").toggleClass("col-sm-12 col-md-12 col-lg-12");
		$(this).children().toggleClass("glyphicon-chevron-left");
		$(this).children().toggleClass("glyphicon-chevron-right");
		$(this).toggleClass("active");
		return false;
	});
	$("#params").toggle();
	$("#bt-toggle").click(function(){
		$("#params").toggle();
		$("#bt-toggle > i").toggleClass("fa fa-chevron-down");
		$("#bt-toggle > i").toggleClass("fa fa-chevron-up");
	});
});
</script>
</div>