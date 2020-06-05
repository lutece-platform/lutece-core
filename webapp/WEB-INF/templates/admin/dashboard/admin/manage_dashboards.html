<#macro dashboardTable items=[] list_available_orders_column=[] order=true>
  <#if items?exists && items?size &gt; 0>
    <@table>
      <tr>
        <#-- header -->
        <th>#i18n{portal.admindashboard.table.title.name}</th>
        <#-- name -->
        <th>#i18n{portal.admindashboard.table.title.column}</th>
        <#-- column -->
        <#if order>
          <th>#i18n{portal.admindashboard.table.title.order}</th>
        </#if>
        <#-- order -->
      </tr>
      <#list items as dashboard>
        <tr>
          <td>${dashboard.name}</td>
          <#-- name -->
          <td>
          <#-- column -->
            <@tform action='jsp/admin/admindashboard/DoMoveAdminDashboard.jsp' method='post' type='inline'>
						<@input type='hidden' name='dashboard_name' value='${dashboard.name}' />
						<@input type='hidden' name='token' value='${token}' />
						<#if !dashboard.order?exists || dashboard.order==0>
							<#assign dashboard_order=1/>
						<#else>
							<#assign dashboard_order=dashboard.order />
						</#if>
						<@input type='hidden' name='dashboard_order' value='${dashboard_order}' />
						<@formGroup formStyle='inline' rows=2>
							<@inputGroup>
								<@select name='dashboard_column' id='dashboard_column-${dashboard.name}' default_value='${dashboard.zone!}' items=list_available_columns />

								<@inputGroupItem>
									<@button type='submit' buttonIcon='check' title='#i18n{portal.dashboard.table.action.setColumn}' hideTitle=['all'] />
								</@inputGroupItem>
							</@inputGroup>
						</@formGroup>
            </@tform>
          </td>
          <#-- /column -->
            <#if order>
              <td>
              <#-- order -->
                <@tform action='jsp/admin/admindashboard/DoMoveAdminDashboard.jsp' method='post' type='inline'>
					<@input type='hidden' name='dashboard_name' value='${dashboard.name}' />
					<@input type='hidden' name='token' value='${token}' />
					<#if !dashboard.zone?exists || dashboard.zone==0>
					  <#assign dashboard_column=1 />
					<#else>
					  <#assign dashboard_column=dashboard.zone />
					</#if>
					<@formGroup formStyle='inline' rows=2>
						<@input type='hidden' name='dashboard_column' value='${dashboard_column}' />
						<@inputGroup>
							<@select name='dashboard_order' id='dashboard_order-${dashboard.name}' default_value='${dashboard.order!}' items=list_available_orders_column />
							<@inputGroupItem>
								<@button type='submit' buttonIcon='arrows-v' title='#i18n{portal.dashboard.table.action.move}' hideTitle=['all'] />
							</@inputGroupItem>
						</@inputGroup>
					</@formGroup>
                </@tform>
              </td>
              <#-- /order -->
            </#if>
        </tr>
        </#list>
      </@table>
    <#else>
		<@callOut color='warning'>#i18n{portal.dashboard.message.emptyGroup}</@callOut>
  </#if>
</#macro>

<@row>
	<@columns>
		<@box color='danger'>
			<@boxHeader title='#i18n{portal.admindashboard.title.manage}' />
			<@boxBody>
				<#-- column lists -->
				<#list list_available_columns as column_refItem>
				<#if column_refItem.code !=''>
					<#assign column_key=column_refItem.code />
					<h3>#i18n{portal.admindashboard.title.column} ${column_key}
					<#if map_column_order_status[column_key]?exists && !map_column_order_status[column_key]>
						<@tform action='jsp/admin/admindashboard/DoReorderColumn.jsp' method='post' class='pull-right'>
							<@input type='hidden' name='column' value='${column_key?html}' />
							<@input type='hidden' name='token' value='${token}' />
							<@button type='submit' title='#i18n{portal.dashboard.action.reorder}' buttonIcon='arrows' color='warning' />
						</@tform>
					</#if>
					</h3>
					<@dashboardTable items=map_dashboards[column_key] list_available_orders_column=map_available_orders[column_key] />
					<#-- suggest reorder if the column is not well ordered -->
				</#if>
				</#list>
				<#-- not set list -->
				<h3>#i18n{portal.admindashboard.title.notSetDashboard}</h3>
				<@dashboardTable items=not_set_dashboards order=false/>
			</@boxBody>
		</@box>
	</@columns>
</@row>