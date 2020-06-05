<@row>
	<@columns>
		<@box color='success'>
			<@boxHeader title='#i18n{portal.users.import_users_from_file.labelImportUsers}, #i18n{portal.users.export_users.labelExportUsers}' />
		</@box>
		
		<@tform params='enctype="multipart/form-data"' method='post' name='import_users_from_file' 	action='jsp/admin/user/DoImportUsersFromFile.jsp'>
			<@input type='hidden' name='token' value='${token}' />
			<@tabs>
				<@tabList>
					<@tabLink href='jsp/admin/user/ExportUsers.jsp' title='#i18n{portal.users.export_users.labelExportUsers}' />
					<@tabLink active=true href='jsp/admin/user/ImportUsersFromFile.jsp' title='#i18n{portal.users.import_users_from_file.labelImportUsers}' />
				</@tabList>
				<@tabContent>
					<@formGroup labelKey='#i18n{portal.users.import_users_from_file.labelImportFile}' labelFor='import_file' helpKey='#i18n{portal.users.import_users_from_file.labelCSVSeparator} "<strong>${csv_separator}</strong>".<br />#i18n{portal.users.import_users_from_file.labelListColumns}<br />#i18n{portal.users.import_users_from_file.labelComplementaryAttributes} "<strong>${attributes_separator}</strong>".<br />#i18n{portal.users.import_users_from_file.labelRightsRolesWorkgroup} <br />#i18n{portal.users.import_users_from_file.labelFileEncoding}' mandatory=true>
						<@input type='file' id='import_file' name='import_file' />
					</@formGroup>
							
					<@formGroup labelFor='ignore_first_line' helpKey='#i18n{portal.users.import_users_from_file.labelHelpIgnoreFirstLine}'>
						<@checkBox id='ignore_first_line' name='ignore_first_line' labelKey='#i18n{portal.users.import_users_from_file.labelIgnoreFirstLine}' />
					</@formGroup>
							
					<@formGroup labelFor='update_existing_users' helpKey='#i18n{portal.users.import_users_from_file.labelHelpUpdateExistingUsers}'>
						<@checkBox id='update_existing_users' name='update_existing_users' labelKey='#i18n{portal.users.import_users_from_file.labelUpdateExistingUsers}'/>
					</@formGroup>

					<#if csv_messages?? && csv_messages?has_content>
						<#list csv_messages as csv_message>
							<#if csv_message.messageLevel?string == "ERROR">
								<#assign alertClass="alert-danger">
							</#if>
							<@alert class=alertClass>
								<#if 0 < csv_message.lineNumber >#i18n{portal.users.import_users_from_file.lineNumber} ${csv_message.lineNumber} - </#if>${csv_message.messageContent}
							</@alert>
						</#list>
					</#if>
				
					<@formGroup>
						<@button type='submit' buttonIcon='check' title='#i18n{portal.util.labelValidate}'  size='' />
						<@aButton color='secondary' href='jsp/admin/user/ManageUsers.jsp' buttonIcon='times' title='#i18n{portal.util.labelBack}'  size='' />
					</@formGroup>
				</@tabContent>
			</@tabs>
		</@tform>
	</@columns>
</@row>