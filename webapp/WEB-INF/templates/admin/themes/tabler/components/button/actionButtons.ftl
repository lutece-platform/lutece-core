<#-- Macro: actionButtons

Description: Generates a form group with two action buttons, used for validating or canceling a form.

Parameters:
- button1Name (string, optional): the name attribute of the first button.
- button2Name (string, optional): the name attribute of the second button.
- i18nValue1Key (string, optional): the i18n key of the first button's title.
- i18nValue2Key (string, optional): the i18n key of the second button's title.
- url1 (string, optional): the URL to redirect to when the first button is clicked.
- url2 (string, optional): the URL to redirect to when the second button is clicked.
- icon1 (string, optional): the name of the icon for the first button.
- icon2 (string, optional): the name of the icon for the second button.
- offset (int, optional): the number of columns to offset the form group.

Snippet:

    Default validate and cancel buttons using form submission:

    <@actionButtons button1Name='action_create' button2Name='action_cancel' />

    Validate and cancel buttons with custom labels and URL-based navigation:

    <@actionButtons url1='jsp/admin/ManageItems.jsp?action=save' url2='jsp/admin/ManageItems.jsp' i18nValue1Key='module.mymodule.buttonSave' i18nValue2Key='module.mymodule.buttonBack' icon1='check' icon2='arrow-left' />

-->
<#macro actionButtons button1Name='' button2Name='' i18nValue1Key='portal.admin.message.buttonValidate' i18nValue2Key='portal.admin.message.buttonCancel' url1='' url2='' icon1='device-floppy' icon2='x' offset=3 deprecated...>
<@deprecatedWarning args=deprecated />
<@formGroup rows=2>
	<#if url1 != ''>
		<@aButton href='${url1}' buttonIcon='${icon1}' title='#i18n{${i18nValue1Key}}' size='' hideTitle=['xs','sm'] />
	<#else>
		<@button type='submit' name='${button1Name}' buttonIcon='${icon1}' title='#i18n{${i18nValue1Key}}' size='' hideTitle=['xs','sm'] />
	</#if>
	<#if url2 != ''>
		<@aButton href='${url2}' buttonIcon='${icon2}' title='#i18n{${i18nValue2Key}}' color='secondary' size='' hideTitle=['xs','sm']/>
	<#else>
		<#if button2Name != ''>
			<@button type='submit' name='${button2Name}' buttonIcon='${icon2}' title='#i18n{${i18nValue2Key}}' color='secondary' cancel=true size='' hideTitle=['xs','sm'] />
		</#if>
	</#if>
</@formGroup>
</#macro>