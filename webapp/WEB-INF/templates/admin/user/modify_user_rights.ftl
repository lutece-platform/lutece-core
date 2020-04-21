<@row>
	<@columns>
		<@box color='success'>
			<#assign headerIcon>
				<@icon style='user' />
			</#assign>
			<@boxHeader title='${headerIcon}&nbsp;${user.lastName} ${user.firstName}' boxTools=true>
				<#if can_delegate = 'true'><p>#i18n{portal.users.modify_user_rights.informationRightModification}</p></#if>
				<@item_navigation item_navigator=item_navigator />
				<@tform align='right' method='post' action='jsp/admin/user/ModifyUserRights.jsp'>	
					<@input type='hidden' name='delegate_rights' value='${can_delegate}' />
					<@input type='hidden' name='id_user' value='${user.userId}' />
					<@input type='hidden' name='select' value='all' />
					<@button type='submit' size='' buttonIcon='filter' size='sm' title='#i18n{portal.users.modify_user_rights.buttonLabelSelectAll}' hideTitle=['xs','sm']   />
				</@tform>
			</@boxHeader>
		<@boxBody>
			<@tabs>
				<@tabList>
					<@tabLink href='jsp/admin/user/ModifyUser.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelUser}' />
					<#if defaultModeUsed><@tabLink href='jsp/admin/user/ModifyUserPassword.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelPassword}' /></#if>
					<@tabLink active=true href='jsp/admin/user/ModifyUserRights.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelRights}' />
					<@tabLink href='jsp/admin/user/ModifyUserRoles.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelRoles}' />
					<@tabLink href='jsp/admin/user/ModifyUserWorkgroups.jsp?id_user=${user.userId}' title='#i18n{portal.users.actions.labelWorkgroups}' />
				</@tabList>
				<@tabContent>
					<@tform method='post' action='jsp/admin/user/DoModifyUserRights.jsp'>
						<@input type='hidden' name='delegate_rights' value='${can_delegate}' />
						<@input type='hidden' name='id_user' value='${user.userId}' />
						<@input type='hidden' name='token' value='${token}' />
							<@row>
								<@columns>
									<@listGroup>
										<#list all_right_list?sort_by('name') as right>
										<#if select_all = true>
										<#assign checked = true>
										<#else>
										<#assign checked = false>
										<#list user_right_list as user_right>
										<#if user_right.id = right.id >
										<#assign checked = true>
										<#break>
										</#if>
										</#list>
										</#if>
										<@listGroupItem>
											<@checkBox id='${right.name}' name='right' labelKey='${right.name} (#i18n{portal.users.modify_user_rights.labelLevel} ${right.level}) - ${right.description}' value='${right.id}' checked=checked mandatory=false />
										</@listGroupItem>
										</#list>
									</@listGroup>
								</@columns>
							</@row>
							<@formGroup rows=2>
								<@button type='submit' buttonIcon='check' title='#i18n{portal.users.modify_user_rights.buttonLabelModifyRights}'  size='' />
								<@aButton size='' href='jsp/admin/user/ManageUsers.jsp' color='secondary' buttonIcon='times' title='#i18n{portal.util.labelBack}'  />
							</@formGroup>
						</@tform>
					</@tabContent>
				</@tabs>
            </@boxBody>
        </@box>
    </@columns>
</@row>