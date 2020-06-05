<@row>
    <@columns>
        <@box>
            <@boxHeader title='#i18n{portal.system.manage_caches.titleCacheList}' boxTools=true>
                <@btnGroup>
                    <@tform method='post' action='jsp/admin/system/DoReloadProperties.jsp' class='pull-right'>
                        <@input type='hidden' name='token' value='${token}' />
                        <@button type='submit' buttonIcon='sync' title='#i18n{portal.system.manage_caches.titleReloadProperties}' size='sm' hideTitle=['xs','sm'] />
                    </@tform>
                    <@tform method='post' action='jsp/admin/system/DoResetCaches.jsp' class='pull-right spaced'>
                        <@input type='hidden' name='token' value='${token}' />
                        <@button type='submit' buttonIcon='trash' title='#i18n{portal.system.manage_caches.buttonFlush}' size='sm' color='danger' hideTitle=['xs','sm'] />
                    </@tform>
                </@btnGroup>
            </@boxHeader>
            <@boxBody>
            <p>
                <em>#i18n{portal.system.manage_caches.featureDescription}</em>
            </p>
            <@alert color='warning' title='#i18n{portal.system.manage_caches.cautionTitle}' iconTitle='exclamation-triangle' dismissible=true>#i18n{portal.system.manage_caches.cautionMessage}</@alert>
				<@table>
					<tr>
						<th>#i18n{portal.system.manage_caches.columnTitleName}</th>
						<th class="hidden-xs hidden-sm"></th>
						<th class="hidden-xs">#i18n{portal.system.manage_caches.columnTitleCacheSize}</th>
						<th class="hidden-xs">#i18n{portal.system.manage_caches.columnTitleMemorySize}</th>
						<th>#i18n{portal.system.manage_caches.columnTitleActions}</th>
					</tr>
					<#assign id = 0>
					<#list services_list as service>
					<tr>
						<td>
							<#if service.cacheEnable>
							<@tag color='success' title='#i18n{portal.system.manage_caches.imgAltEnable}'>
								<@icon style='check' />
							</@tag>
							<#else>
							<@tag color='danger' title='#i18n{portal.system.manage_caches.imgAltDisable}'>
								<@icon style='times' />
							</@tag>
							</#if>
							&nbsp;<strong>${service.name}</strong>
						</td>
						<td class="hidden-xs hidden-sm">
							[
							<@icon style='info-circle' />&nbsp;#i18n{portal.system.manage_caches.columnTitleMaxElements}: ${service.maxElements!"-"}
							&nbsp;|&nbsp;<@icon style='clock-o' />&nbsp;#i18n{portal.system.manage_caches.columnTitleTimeToLive} : ${service.timeToLive} -> ${ time2string( service.timeToLive )}
							]
						</td>
						<td class="hidden-xs">${service.cacheSize}</td>
						<td class="hidden-xs">${service.memorySize}</td>
						<@td class='d-flex'>
									<@tform method='post' action='jsp/admin/system/ConfirmToggleCache.jsp' class="pull-right spaced">
										<@input type='hidden' name='id_cache' value='${id}' />
										<@input type='hidden' name='token' value='${token}' />
										<#if service.cacheEnable>
												<@button type='submit' buttonIcon='stop' title='#i18n{portal.system.manage_caches.actionDisable}' hideTitle=['all'] color='danger' size='sm' />
										<#else>
												<@button type='submit' buttonIcon='play' title='#i18n{portal.system.manage_caches.actionEnable}' hideTitle=['all'] color='success' size='sm' />
										</#if>
									</@tform>
									<@aButton href='jsp/admin/system/CacheInfos.jsp?id_cache=${id}' buttonIcon='eye' class="pull-right spaced" title='#i18n{portal.system.manage_caches.actionViewKeys}' hideTitle=['all'] size='sm' />
									<@tform method='post' action='jsp/admin/system/DoResetCaches.jsp' class="pull-right spaced">
											<@input type='hidden' name='id_cache' value='${id}' />
											<@input type='hidden' name='token' value='${token}' />
											<@button type='submit' buttonIcon='trash' title='#i18n{portal.system.manage_caches.actionClear}' hideTitle=['all'] size='sm' color='danger' />
									</@tform>
						</@td>
					</tr>
					<#assign id = id + 1>
					</#list>
				</@table>
			</@boxBody>
		</@box>
  </@columns>
</@row>

<#function time2string time>
    <#assign hour =  ( time / 3600 )?floor >
    <#assign mn = ((time - (hour * 3600)) / 60)?floor >
    <#if hour != 0 >
       <#assign result = '' + hour + 'h' >
       <#if mn != 0 >
            <#assign result = result + ' ' + mn + 'mn'>
       </#if>
    <#else>
       <#assign result = '' + mn + 'mn'>
    </#if>
    <#return result >
</#function>

