<#--
-- Build the attribute for sorting table
-- @param filter the filter
-- @return the attributes used in the URL
-->
<#function buildSortSearchAttribute filter>
<#assign sortSearchAttribute = "" />
<#if filter??>
<#if filter.name?? && filter.name != "">
<#assign sortSearchAttribute = sortSearchAttribute + "&amp;name=" + filter.name! />
</#if>
<#if filter.description?? && filter.description != "">
<#assign sortSearchAttribute = sortSearchAttribute + "&amp;description=" + filter.description! />
</#if>
<#if filter.workgroup?? && filter.workgroup != "">
<#assign sortSearchAttribute = sortSearchAttribute + "&amp;workgroup=" + filter.workgroup! />
</#if>
</#if>
<#return sortSearchAttribute + "&amp;session=sesssion" />
</#function>

<@row>
	<@columns>
		<@box color='success'>
			<@boxHeader title='#i18n{portal.mailinglist.manage_mailinglists.boxTitle}' boxTools=true>
				<@tform align='right' method='post' name='search_mailinglist' action='jsp/admin/mailinglist/ManageMailingLists.jsp'>
					<@formGroup labelKey='#i18n{portal.mailinglist.manage_mailinglists.labelName}' labelFor='name' hideLabel=['all'] formStyle='inline'>
						<@inputGroup>
							<@input type='text' id='name' name='name' value='${mailinglistFilter.name!}' maxlength=100 size='sm' />
							<@inputGroupItem>
								<@button type='submit' buttonIcon='search' title='#i18n{portal.mailinglist.manage_mailinglists.buttonSearch}' hideTitle=['all'] size='sm'/>
							</@inputGroupItem>
						</@inputGroup>
					</@formGroup>
				</@tform>
				<@tform align='right' method='post' action='jsp/admin/mailinglist/CreateMailingList.jsp'>
					<@button type='submit' buttonIcon='plus' title='#i18n{portal.mailinglist.manage_mailinglists.buttonLabelCreate}' hideTitle=['xs'] size='sm' />
				</@tform>
			</@boxHeader>
			<@boxBody>
				<#assign sortSearchAttribute = buildSortSearchAttribute( mailinglistFilter! ) />
				<@table>
					<tr>
						<th>#i18n{portal.mailinglist.manage_mailinglists.columnTitleName} <@sort jsp_url="jsp/admin/mailinglist/ManageMailingLists.jsp" attribute="name${sortSearchAttribute!}" /> </th>
						<th class="hidden-xs">#i18n{portal.mailinglist.manage_mailinglists.columnTitleDescription} <@sort jsp_url="jsp/admin/mailinglist/ManageMailingLists.jsp" attribute="description${sortSearchAttribute!}" /></th>
						<th class="hidden-xs">#i18n{portal.mailinglist.manage_mailinglists.columnTitleWorkgroup}</th>
            <th>#i18n{portal.mailinglist.manage_mailinglists.columnTitleActions}</th>
					</tr>
					<#list mailinglists_list as mailinglist>
					<tr>
						<td>${mailinglist.name}</td>
						<td class="hidden-xs">${mailinglist.description}</td>
						<td class="hidden-xs">${mailinglist.workgroup}</td>
						<td>
							<@aButton href='jsp/admin/mailinglist/ModifyMailingList.jsp?id_mailinglist=${mailinglist.id}' buttonIcon='edit' size='sm' title='#i18n{portal.mailinglist.modify_mailinglist.buttonModify} (${mailinglist.name?html})' hideTitle=['all'] />
							<@aButton href='jsp/admin/mailinglist/ViewUsers.jsp?id_mailinglist=${mailinglist.id}' buttonIcon='user' size='sm' title='#i18n{portal.mailinglist.modify_mailinglist.linkLabelViewUsers} (${mailinglist.name?html})' hideTitle=['all'] />
							<@aButton href='jsp/admin/mailinglist/RemoveMailingList.jsp?id_mailinglist=${mailinglist.id}' buttonIcon='trash' size='sm' title='#i18n{portal.mailinglist.modify_mailinglist.linkLabelRemove} (${mailinglist.name?html})' hideTitle=['all'] color='danger' />
						</td>
					</tr>
				</#list>
				</@table>
				<@paginationAdmin paginator=paginator combo=1 />
			</@boxBody>
		</@box>
	</@columns>
</@row>