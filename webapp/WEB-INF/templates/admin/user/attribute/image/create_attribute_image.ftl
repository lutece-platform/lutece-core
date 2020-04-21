<@row>
	<@columns>
    <@box color='success'>
      <@boxHeader title='#i18n{portal.users.create_attribute.pageTitleAttributeImage}' />
			<@boxBody>
				<@tform action='jsp/admin/user/attribute/DoCreateAttribute.jsp' method='post'>
					<@input type='hidden' name='token' value='${token}' />
					<@input type='hidden' name='attribute_type_class_name' value='${attribute_type.className}' />
					<@formGroup labelKey='#i18n{portal.users.create_attribute.labelTitle} *' labelFor='attr_title' helpKey='#i18n{portal.users.create_attribute.labelTitleComment}' mandatory=true>
						<@input type='text' name='title' id='attr_title' maxlength=255 />
					</@formGroup>
					<@formGroup labelKey='#i18n{portal.users.create_attribute.labelHelpMessage}' labelFor='help_message' helpKey='#i18n{portal.users.create_attribute.labelHelpMessageComment}'>
						<@input type='textarea' name='help_message' id='help_message' cols=80 rows=5 />
					</@formGroup>
					<@formGroup>
						<@checkBox name='is_shown_in_result_list' id='is_shown_in_result_list' labelKey='#i18n{portal.users.create_attribute.labelIsShownInResultList}' value='1' />
					</@formGroup>
					<@formGroup>
						<@checkBox name='mandatory' id='mandatory' labelKey='#i18n{portal.users.create_attribute.labelMandatory}' value='1' />
					</@formGroup>
					<@formGroup labelKey='#i18n{portal.users.create_attribute.labelWidth}' labelFor='width'>
						<@input type='text' name='width' id='width' value='50' />
					</@formGroup>
					<@formGroup labelKey='#i18n{portal.users.create_attribute.labelHeight}' labelFor='height'>
						<@input type='text' name='height' id='height' value='50' />
					</@formGroup>
					<@formGroup>
						<@button type='submit' name='save' value='save' buttonIcon='check' title='#i18n{portal.users.create_attribute.buttonValidate}'  size='' />
						<@button type='submit' name='apply' value='apply' buttonIcon='check' title='#i18n{portal.users.create_attribute.buttonApply}'  size='' />
						<@button type='submit' name='cancel' value='cancel' buttonIcon='times' title='#i18n{portal.admin.message.buttonCancel}' cancel=true size='' />
					</@formGroup>
					</@tform>
			</@boxBody>
		</@box>
  </@columns>
</@row>