<#--
Macro: adminAccessibilityMode

Description: Generates a form to activate or deactivate accessibility mode for an administrative user.

Parameters:
- token (string, required): the security token used to prevent cross-site request forgery (CSRF) attacks.
- user.accessibilityMode (boolean, required): a boolean value indicating whether accessibility mode is currently active.
-->
<#macro adminAccessibilityMode>
	<#local btnTitle='#i18n{portal.users.admin_header.labelActivateAccessibilityMode}' >
	<#if user.accessibilityMode>
		<#local btnTitle='#i18n{portal.users.admin_header.labelDeactivateAccessibilityMode}' >
	<#else>
	</#if>
	<@aButton style='dropdown-item disabled truncate' color='' href='jsp/admin/DoModifyAccessibilityMode.jsp?token=${token}'  title=btnTitle />
</#macro>