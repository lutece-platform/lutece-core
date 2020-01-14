<@row>
	<@columns>
		<@box color='success'>
      		<@boxHeader title='#i18n{portal.users.modify_attribute.pageTitleAttributeCheckBox}' boxTools=true>
				<@button style='card-control collapse' buttonIcon='minus' />
			</@boxHeader>
			<@boxBody>
				<@tform action='jsp/admin/user/attribute/DoModifyAttribute.jsp' method='post'>
					<@input type='hidden' name='token' value='${token}' />
					<@formGroup labelKey='#i18n{portal.users.modify_attribute.labelTitle} *' labelFor='attr_title' helpKey='#i18n{portal.users.modify_attribute.labelTitleComment}' mandatory=true>
						<@input type='text' name='title' id='attr_title' value='${attribute.title?html}' maxlength=255 />
					</@formGroup>
					<@formGroup labelKey='#i18n{portal.users.modify_attribute.labelHelpMessage}' labelFor='help_message' helpKey='#i18n{portal.users.modify_attribute.labelHelpMessageComment}'>
						<@input type='textarea' name='help_message' id='help_message' cols=80 rows=5>${attribute.helpMessage?html}</@input>
					</@formGroup>
					<@formGroup>
						<@checkBox name='is_shown_in_search' id='is_shown_in_search' value='1' checked=attribute.shownInSearch labelKey='#i18n{portal.users.modify_attribute.labelIsShownInSearch}' />
					</@formGroup>
					<@formGroup>
						<@checkBox name='is_shown_in_result_list' id='is_shown_in_result_list' value='1' checked=attribute.shownInResultList labelKey='#i18n{portal.users.modify_attribute.labelIsShownInResultList}' />
					</@formGroup>
					<@formGroup>
						<@checkBox name='mandatory' id='mandatory' value='1' checked=attribute.mandatory labelKey='#i18n{portal.users.modify_attribute.labelMandatory}' />
					</@formGroup>
					<@formGroup helpKey='#i18n{portal.users.create_attribute.labelIsFieldInLineComment}'>
						<@checkBox name='is_field_in_line' id='is_field_in_line' value='1' checked=attribute.fieldInLine labelKey='#i18n{portal.users.create_attribute.labelIsFieldInLine}' />
					</@formGroup>
					<@formGroup>
						<@input type='hidden' name='id_attribute' value='${attribute.idAttribute}' />
						<@input type='hidden' name='position' value='${attribute.position}' />
						<@button type='submit' name='validate' value='#i18n{portal.users.modify_attribute.buttonValidate}' buttonIcon='check' title='#i18n{portal.users.modify_attribute.buttonValidate}' size='' hideTitle=['xs'] />
						<@button type='submit' name='apply' value='#i18n{portal.users.modify_attribute.buttonApply}' buttonIcon='check-circle-o' title='#i18n{portal.users.modify_attribute.buttonApply}' size='' hideTitle=['xs'] />
						<@button type='submit' name='cancel' value='#i18n{portal.admin.message.buttonCancel}' buttonIcon='times' title='#i18n{portal.admin.message.buttonCancel}' size='' hideTitle=['xs'] cancel=true />
					</@formGroup>
				</@tform>
			</@boxBody>
		</@box>
	
		<@box color='success'>
			<#assign anchor>
				<@link name='list' />
			</#assign>
      <@boxHeader title='${anchor} #i18n{portal.users.modify_attribute.listAssociatedFields}' boxTools=true>
				<@tform method='post' action='jsp/admin/user/attribute/CreateAttributeField.jsp' type='inline' align='right'>
					<@input type='hidden' value='${attribute.idAttribute}' name='id_attribute' />
					<@button type='submit' name='create' value='#i18n{portal.users.modify_attribute.buttonCreateField}' buttonIcon='plus' title='#i18n{portal.users.modify_attribute.buttonCreateField}' size='' hideTitle=['xs','sm'] />
					<@button style='card-control collapse' buttonIcon='minus' />
				</@tform>
			</@boxHeader>
			<@boxBody>
				<@table id='list'>
					<tr>
						<th>#i18n{portal.users.modify_attribute.columnLabelTitle}</th>
						<th>#i18n{portal.users.modify_attribute.columnLabelValue}</th>
						<th>#i18n{portal.users.modify_attribute.columnLabelDefaultValue}</th>
						<th>#i18n{portal.users.modify_attribute.columnLabelId}</th>
						<th>#i18n{portal.users.modify_attribute.columnLabelActions}</th>
					</tr>
					<#assign cpt = 0>
					<#list attribute_fields_list as attribute_field>
						<#if attribute_field.title?exists>
						<#assign cpt = cpt + 1>
						<tr>
							<td>${attribute_field.title}</td>
							<td>${attribute_field.value}</td>
							<td>
								<#if attribute_field.defaultValue>
									<@tag color='success'>#i18n{portal.users.modify_attribute.defaultValue}</@tag>
								</#if>
							</td>
							<td>${attribute_field.idField}</td>
							<td>
								<@aButton href='jsp/admin/user/attribute/ModifyAttributeField.jsp?id_attribute=${attribute.idAttribute}&amp;id_field=${attribute_field.idField}' title='#i18n{portal.users.modify_attribute.buttonLabelModify}' buttonIcon='edit' hideTitle=['all'] class='pull-left spaced'/>
								
								<#if cpt != 1>
									<@tform method='post' action='jsp/admin/user/attribute/DoMoveUpAttributeField.jsp#list' class='pull-left spaced'>
										<@input type='hidden' name='id_attribute' value='${attribute.idAttribute?html}' />
										<@input type='hidden' name='id_field' value='${attribute_field.idField?html}' />
										<@input type='hidden' name='token' value='${token}' />
										<@button type='submit' title='#i18n{portal.users.modify_attribute.buttonLabelMoveUp}' buttonIcon='chevron-up' hideTitle=['all'] />
									</@tform>
								</#if>
								
								<#if attribute_field_has_next>
									<@tform method='post' action='jsp/admin/user/attribute/DoMoveDownAttributeField.jsp#list' class='pull-left spaced'>
										<@input type='hidden' name='id_attribute' value='${attribute.idAttribute?html}' />
										<@input type='hidden' name='id_field' value='${attribute_field.idField?html}' />
										<@input type='hidden' name='token' value='${token}' />
										<@button type='submit' title='#i18n{portal.users.modify_attribute.buttonLabelMoveDown}' buttonIcon='chevron-down' hideTitle=['all'] />
									</@tform>
								</#if>
								
								<@aButton href='jsp/admin/user/attribute/RemoveAttributeField.jsp?id_attribute=${attribute.idAttribute}&amp;id_field=${attribute_field.idField}' title='#i18n{portal.users.modify_attribute.buttonLabelDelete}' buttonIcon='trash' hideTitle=['all'] color='danger'  class='pull-left spaced'/>
							</td>
						</tr>
						</#if>
					</#list>
				</@table>
			</@boxBody>
    </@box>
  </@columns>
</@row>