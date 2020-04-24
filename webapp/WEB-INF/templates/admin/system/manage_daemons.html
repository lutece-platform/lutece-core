<@row>
	<@columns>
		<@box color='danger'>
			<@boxHeader title='#i18n{portal.system.manage_daemons.titleCacheList}' />
			<@boxBody>
				<@table>
				  <tr>
					<th></th>
					<th>#i18n{portal.system.manage_daemons.columnTitleId}</th>
					<@th hide=['xs']>#i18n{portal.system.manage_daemons.columnTitleName}</@th>
					<th>#i18n{portal.system.manage_daemons.columnTitleInterval}</th>
					<th>#i18n{portal.system.manage_daemons.columnTitleLastRun}</th>
					<th>#i18n{portal.system.manage_daemons.columnTitleActions}</th>
				  </tr>
				  <#list daemons_list as daemon>
					<tr>
						<td>
						  <#if daemon.running>
							<@tag color='success' title='#i18n{portal.util.labelEnabled}'>
								<@icon style='check' />
							</@tag>
							<#else>
							<@tag color='danger' title='#i18n{portal.util.labelDisabled}'>
								<@icon style='times' />
							</@tag>
						  </#if>
						</td>
						<@td hide=['xs']>${daemon.id}<br>(${daemon.pluginName})</@td>
						<@td hide=['xs']>#i18n{${daemon.nameKey}}<br>#i18n{${daemon.descriptionKey}}</@td>
						<td>
						  <#if daemon.running>
							${daemon.interval} #i18n{portal.system.manage_daemons.unit.sec} &nbsp;
							<small>
							<#if daemon.interval gte 3600 >
							   <#assign hour = daemon.interval / 3600 />
								 ( ${hour} #i18n{portal.system.manage_daemons.unit.hour} )
							<#else>
								<#if daemon.interval gte 60>
								   <#assign mn = daemon.interval / 60 />
									 ( ${mn} #i18n{portal.system.manage_daemons.unit.mn} )
								</#if>
							</#if>
							</small>
						  <#else>
							  <@tform method='post' action='jsp/admin/system/DoDaemonAction.jsp' type='inline'>
								  <@input type='hidden' name='daemon' value='${daemon.id}' />
								  <@input type='hidden' name='token' value='${token}' />
								  <@formGroup labelFor='interval' hideLabel=['all'] labelKey='#i18n{portal.system.manage_daemons.columnTitleInterval}' rows=2 formStyle='inline'>
								  <@inputGroup>
									<@input type='text' name='interval' id='interval' value='${daemon.interval}' size='sm' />
									<@inputGroupItem>
										<@button type='submit' title='#i18n{portal.system.manage_daemons.buttonUpdateInterval}' name='action' value='UPDATE_INTERVAL' buttonIcon='check' hideTitle=['all'] size='sm' />
									</@inputGroupItem>
								  </@inputGroup>
								  </@formGroup>
							  </@tform>
						  </#if>
						</td>
						<td>
						  <@box color='default box-solid' collapsed=true>
							<#assign title>
								<small>
									<span rel="popover" data-original-title="${daemon.pluginName}" data-content="${daemon.lastRunLogs?html}">
										${daemon.lastRunDate} - Logs ${daemon.id} - ${daemon.pluginName}...
									</span>
								</small>
							</#assign>
							<#if daemon.lastRunLogs?has_content>
								<#assign boxTools=true>
							<#else>
								<#assign boxTools=false>
							</#if>
							<@boxHeader title=title titleLevel='p' boxTools=boxTools>
								<#if daemon.lastRunLogs?has_content>
									<@button style='card-control collapse' buttonIcon='plus' />
								</#if>
							</@boxHeader>
							<#if daemon.lastRunLogs?has_content>
							<@boxBody>
								<@pre>${daemon.lastRunLogs}</@pre>
							</@boxBody>
							</#if>
							</@box>
						</td>
						<td>
						  <div class="text-center">
							<@tform method="post" action="jsp/admin/system/DoDaemonAction.jsp" type='inline'>
							<@input type='hidden' name='daemon' value='${daemon.id}' />
							<@input type='hidden' name='token' value='${token}' />
							<#if daemon.running>
								<@inputGroup>
										<@button type='submit' title='#i18n{portal.system.manage_daemons.buttonStop} ${daemon.id}' name='action' value='STOP' buttonIcon='stop' color='danger' hideTitle=['all'] size='sm' />
										<@button type='submit' title='#i18n{portal.system.manage_daemons.buttonRun} ${daemon.id}' name='action' value='RUN' buttonIcon='sync' color='info' hideTitle=['all'] size='sm' />
								</@inputGroup>
							<#else>
								<@button type='submit' title='#i18n{portal.system.manage_daemons.buttonStart} ${daemon.id}' name='action' value='START' buttonIcon='play' color='success' hideTitle=['all'] size='sm' />
							</#if>
							</@tform>
						  </div>
						</td>
					  </tr>
				  </#list>
				</@table>
			</@boxBody>
        </@box>
    </@columns>
</@row>
<script>
  $(document).ready(function() {
    $("span[rel=popover]").popover({
      trigger: 'hover',
      placement: 'left'
    });
  });
</script>
