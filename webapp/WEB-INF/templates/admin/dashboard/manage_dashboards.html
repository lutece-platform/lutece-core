<#macro dashboardTable items=[] list_available_orders_column=[] order=true>
	<#if items?exists && items?size &gt; 0>
      <#list items as dashboard>
        <tr>
          <@td xs=3 sm=3 >${dashboard.name}  (${dashboard.plugin.name})</@td>
          <@td xs=5 sm=5 >${dashboard.description}</@td>
          <#-- name -->
          <@td xs=2 sm=2>
          <#-- column -->
          <@tform action='jsp/admin/dashboard/DoMoveDashboard.jsp' method='post' type='inline'>
            <@input type='hidden' name='dashboard_name' value='${dashboard.name}' />
            <@input type='hidden' name='token' value='${token}' />
            <#if !dashboard.order?exists || dashboard.order==0>
              <#assign dashboard_order=1/>
            <#else>
              <#assign dashboard_order=dashboard.order/>
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
          </@td>
          <#-- /column -->
          <#if order>
            <@td xs=2 sm=2>
              <#-- order -->
              <@tform action='jsp/admin/dashboard/DoMoveDashboard.jsp' method='post' type='inline'>
                <@input type='hidden' name='dashboard_name' value='${dashboard.name}' />
                <@input type='hidden' name='token' value='${token}' />
                <#if !dashboard.zone?exists || dashboard.zone==0>
                  <#assign dashboard_column=1 />
                <#else>
                  <#assign dashboard_column=dashboard.zone />
                </#if>
                <@input type='hidden' name='dashboard_column' value='${dashboard_column}' />
				<@formGroup formStyle='inline' rows=2>
                <@inputGroup>
                    <@select name='dashboard_order' id='dashboard_order-${dashboard.name}' default_value='${dashboard.order!}' items=list_available_orders_column sort=true />
                    <@inputGroupItem>
                        <@button type='submit' buttonIcon='arrows-v' title='#i18n{portal.dashboard.table.action.move}' hideTitle=['all'] />
                    </@inputGroupItem>
                </@inputGroup>
								</@formGroup>
                </@tform>
              </@td>
              <#-- /order -->
            </#if>
          </tr>
      </#list>
   <#else>
        <#if !order>
            <@callOut color='warning'>#i18n{portal.dashboard.message.emptyGroup}</@callOut>
        </#if>    
    </#if>
</#macro>

<@row>
  <@columns>
      	<@accordionContainer id='adminHomePage'>
		<@accordionPanel color='success' collapsed=true childId='adminHomePageManagement'>
			<@accordionHeader title='#i18n{portal.dashboard.title.manage}' headerIcon='columns' >
            </@accordionHeader>
			<@accordionBody>
            <p>
                #i18n{portal.dashboard.featureDescription}
            </p>
				<#-- column lists -->
            <@table>
			<tr>
                <#-- header -->
                <th>#i18n{portal.dashboard.table.title.name}</th>
                <th>#i18n{portal.dashboard.table.title.description}</th>
                <#-- name -->
                <th>#i18n{portal.dashboard.table.title.column}</th>
                <#-- column -->
                <th>#i18n{portal.dashboard.table.title.order}</th>
                <#-- order -->
            </tr>

				<#list list_available_columns as column_refItem>
				<#if column_refItem.code !=''>
					<#assign column_key=column_refItem.code />
					<#if map_column_order_status[column_key]?exists && !map_column_order_status[column_key]>
						<@tform method='post' action='jsp/admin/dashboard/DoReorderColumn.jsp' class='pull-right'>
							<@input type='hidden' name='column' value='${column_key?html}' />
							<@input type='hidden' name='token' value='${token}' />
							<@button type="submit" title='#i18n{portal.dashboard.action.reorder}' buttonIcon='arrows' color='warning' />
						</@tform>
					</#if>
					<@dashboardTable items=map_dashboards[column_key] list_available_orders_column=map_available_orders[column_key] />
					<#-- suggest reorder if the column is not well ordered -->
				</#if>
				</#list>
           		</@table>
 
				<#-- not set list -->
                <#if (not_set_dashboards?size > 0) >
				<h3>#i18n{portal.dashboard.title.notSetDashboard}</h3>
                <@table>
                    <tr>
                        <#-- header -->
                        <th>#i18n{portal.dashboard.table.title.name}</th>
                        <th>#i18n{portal.dashboard.table.title.description}</th>
                        <#-- name -->
                        <th>#i18n{portal.dashboard.table.title.column}</th>
                        <#-- column -->
                    </tr>
        			<@dashboardTable items=not_set_dashboards order=false/>
           		</@table>
                </#if>
            </@accordionBody>    
		</@accordionPanel>
        </@accordionContainer>
	</@columns>
</@row>
