<@row>
  <@columns>
		<@box color='success'>
			<@boxHeader title='#i18n{portal.role.create_role.boxTitle}' />
			<@boxBody>
				<@tform action='jsp/admin/role/DoCreatePageRole.jsp' name='create_page_role'>
					<@input type='hidden' name='token' value='${token}' />
					<@formGroup labelKey='#i18n{portal.role.create_role.labelName}' mandatory=true>
						<@input type='text' name='role' maxlength=50 />
					</@formGroup>
					<@formGroup labelKey='#i18n{portal.role.create_role.labelDescription}' mandatory=true>
						<@input type='text' name='role_description' maxlength=50 />
					</@formGroup>
					<@formGroup labelKey='#i18n{portal.role.create_role.labelWorkgroupKey}' mandatory=true>
						<@select name='workgroup_key' default_value='${workgroup_key_default_value}' items=workgroup_key_list sort=true />
					</@formGroup>
					<@formGroup>
						<@button type='submit' buttonIcon='check' title='#i18n{portal.role.modify_role.buttonLabelValidate}'  size='' />
						<@aButton buttonIcon='remove' href='jsp/admin/role/ManagePageRole.jsp' title='#i18n{portal.admin.message.buttonCancel}' color='secondary' />
					</@formGroup>
				</@tform>
			</@boxBody>
		</@box>
	</@columns>
</@row>