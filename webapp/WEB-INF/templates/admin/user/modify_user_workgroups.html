<@row>
	<@columns>
		<@box color='success'>
			<#assign headerIcon>
				<@icon style='user' />
			</#assign>
			<@boxHeader title='${headerIcon}&nbsp;${user.lastName} ${user.firstName}' boxTools=true>
					<@item_navigation item_navigator=item_navigator />
			</@boxHeader>
			<@boxBody>
					<@tabs>
		<@tabList>
			<@tabLink href='jsp/admin/user/ModifyUser.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelUser}' />
			<#if defaultModeUsed><@tabLink href='jsp/admin/user/ModifyUserPassword.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelPassword}' /></#if>
			<@tabLink href='jsp/admin/user/ModifyUserRights.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelRights}' />
			<@tabLink href='jsp/admin/user/ModifyUserRoles.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelRoles}' />
			<@tabLink active=true href='jsp/admin/user/ModifyUserWorkgroups.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelWorkgroups}' />
		</@tabList>
		<@tabContent>
			<@tform method='post' action='jsp/admin/user/DoModifyUserWorkgroups.jsp'>
				<@input type='hidden' name='delegate_rights' value='${can_delegate}' />
				<@input type='hidden' name='id_user' value='${user.userId}' />
				<@input type='hidden' name='token' value='${token}' />
				<@row>
					<@columns>
						<@listGroup>
							<#list all_workgroup_list?sort_by('code') as workgroup>
							<@listGroupItem>
								<@checkBox name='workgroup' id='workgroup' value='${workgroup.code}' checked=workgroup.checked labelKey='${workgroup.name}' mandatory=false />
							</@listGroupItem>
							</#list>        
						</@listGroup>	
					</@columns>	
				</@row>	
				<@formGroup rows=2>
					<@button type='submit' buttonIcon='check'  title='#i18n{portal.users.modify_user_workgroups.buttonLabelModifyWorkgroups}'  size='' />
					<@aButton size='' href='jsp/admin/user/ManageUsers.jsp' color='secondary' buttonIcon='times' title='#i18n{portal.util.labelBack}'  />
				</@formGroup>
			</@tform>
		</@tabContent>
					</@tabs>
			</@boxBody>
		</@box>	
	</@columns>
</@row>