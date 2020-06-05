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
						<@tabLink active=true href='jsp/admin/user/ModifyUserRoles.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelRoles}' />
						<@tabLink href='jsp/admin/user/ModifyUserWorkgroups.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelWorkgroups}' />
					</@tabList>
					<@tabContent>
						<@tform role='form' method='post' action='jsp/admin/user/DoModifyUserRoles.jsp'>
							<@input type='hidden' name='id_user' value='${user.userId}' />
							<@input type='hidden' name='token' value='${token}' />
							<@row>
								<@columns>
									<@listGroup>
										<#list all_role_list?sort_by('key') as role>
										<#assign checked = false >
										<#list user_role_list as user_role>
										<#if user_role.key = role.key >
										<#assign checked = true>
										<#break>
										</#if>
										</#list>
										<@listGroupItem>
											<@checkBox id='${role.key}' name='roles' value='${role.key}' labelKey='${role.key} - ${role.description}' checked=checked mandatory=false />
										</@listGroupItem>
										</#list>
									</@listGroup> 
								</@columns>
							</@row>
							<@formGroup rows=2>
								<@button type='submit' buttonIcon='check' title='#i18n{portal.users.modify_user_roles.buttonLabelModifyRoles}' size=''  />
								<@aButton size='' href='jsp/admin/user/ManageUsers.jsp' color='secondary' buttonIcon='times' title='#i18n{portal.util.labelBack}'  />
							</@formGroup>
						</@tform>
					</@tabContent>
                </@tabs>
            </@boxBody>
        </@box>
    </@columns>
</@row>