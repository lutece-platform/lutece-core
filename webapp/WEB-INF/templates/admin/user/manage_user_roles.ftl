<@row>
	<@columns>
		<@box color='success'>
			<#assign headerIcon>
				<@icon style='user' />
			</#assign>
			<@boxHeader title='#i18n{portal.users.manage_user_roles.titleRoleList} : ${headerIcon}&nbsp;${user.lastName} ${user.firstName}' boxTools=true>
				<div class="pull-right spaced"><@item_navigation item_navigator=item_navigator /></div>
				<@tform align='right' method='post' name='modifyform' action='jsp/admin/user/ModifyUserRoles.jsp'>
					<@input type='hidden' name='id_user' value='${user.userId}' />
					<@input type='hidden' name='delegate_rights' value='false' />
					<#if can_modify>
						<@button type='submit' buttonIcon='edit' title='#i18n{portal.users.manage_user_roles.buttonLabelModifyRoles}' size='' hideTitle=['xs','sm'] />
					</#if>
					<#if can_delegate>
						<@button type='submit' buttonIcon='share' title='#i18n{portal.users.manage_user_roles.buttonLabelDelegateRoles}' size='' hideTitle=['xs','sm'] />
					</#if>
      			</@tform>
			</@boxHeader>
			<@boxBody>
				<@tabs>
					<@tabList>
						<@tabLink href='jsp/admin/user/ModifyUser.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelUser}' />
						<@tabLink href='jsp/admin/user/ManageUserRights.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelRights}' />
						<@tabLink active=true href='jsp/admin/user/ModifyUserRoles.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelRoles}' />
						<@tabLink href='jsp/admin/user/ModifyUserWorkgroups.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelWorkgroups}' />
					</@tabList>
					<@tabContent>
						<@table>
							<tr>
								<th>#i18n{portal.users.manage_user_roles.columnTitleKey}</th>
								<th>#i18n{portal.users.manage_user_roles.columnTitleDescription}</th>
							</tr>
							<#list user_role_list?sort_by('key') as role>
							<tr>
								<td>${role.key}</td>
								<td>${role.description}</td>            
							</tr>
							</#list>
						</@table>
					</@tabContent>
				</@tabs>
			</@boxBody>
    	</@box>
	</@columns>
</@row>