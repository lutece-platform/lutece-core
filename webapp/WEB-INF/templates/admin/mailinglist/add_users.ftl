<@row>
	<@columns>
		<@box color='success'>
			<@boxHeader title='#i18n{portal.mailinglist.add_users.title}' />
			<@boxBody>
				<@tform method='post' action='jsp/admin/mailinglist/DoAddUsers.jsp'>
					<@input type='hidden' name='id_mailinglist' value='${mailinglist.id}' />
					<@input type='hidden' name='token' value='${token}' />
					<@formGroup labelKey='#i18n{portal.mailinglist.add_users.labelWorkgroup}' labelFor='workgroup' mandatory=true>
						<@select name='workgroup' default_value='all' items=workgroups_list sort=true />
					</@formGroup>
					
					<@formGroup labelKey='#i18n{portal.mailinglist.add_users.labelRole}' labelFor='role' mandatory=true>
						<@select name='role' default_value='none' items=roles_list sort=true />
					</@formGroup>
					
					<@formGroup>
						<@button type='submit' buttonIcon='check' title='#i18n{portal.mailinglist.add_users.buttonAdd}' size='' />
						<@aButton href='jsp/admin/mailinglist/ManageMailingLists.jsp' buttonIcon='remove' title='#i18n{portal.util.labelCancel}' size='' color='secondary' />
					</@formGroup>
				</@tform>
			</@boxBody>
		</@box>
	</@columns>
</@row>