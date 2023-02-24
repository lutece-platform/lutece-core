<#-- Macro: adminHome
Description: Generates the home page for the admin dashboard. It generates a container for three columns of widgets, which are defined in separate macro calls. The macro also includes a script that enables dragging and dropping of the widgets to rearrange their positions.
-->
<#macro adminHome>
<#assign head = .get_optional_template('../../../../../admin/user/adminHeader.html')>
<#if head.exists><@head.include /></#if>
<@pageWrapper>
	<@div class="dashboard-widgets pt-3">
		<@row>
			<@columns sm=12 md=4 class='widget-col' id='zone-1'>
				${dashboard_zone_1!}
			</@columns>
			<@columns sm=12 md=4 class='widget-col' id='zone-2'>
				${dashboard_zone_2!}
			</@columns>
			<@columns sm=12 md=4 class='widget-col' id='zone-3'>
				${dashboard_zone_3!}
			</@columns>
		</@row>
	</@div>
</@pageWrapper>
<#assign foot = .get_optional_template('../../../../../admin/user/adminFooter.html')>
<#if foot.exists><@foot.include /></#if>
</#macro>