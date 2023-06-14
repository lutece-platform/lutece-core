<#--
Macro: adminAccessibilityMode

Description: Generates a form to activate or deactivate accessibility mode for an administrative user.

Parameters:
- token (string, required): the security token used to prevent cross-site request forgery (CSRF) attacks.
- user.accessibilityMode (boolean, required): a boolean value indicating whether accessibility mode is currently active.
-->
<#macro adminAccessibilityMode>
<@tform method='post' action='jsp/admin/DoModifyAccessibilityMode.jsp' class='form-inline ms-0'>
	<@input type='hidden' name='token' value='${token}' />
	<#local btnIcon='eye-slash' > 
	<#local btnTitle='#i18n{portal.users.admin_header.labelActivateAccessibilityMode}' >
	<#if user.accessibilityMode>
		<#local btnIcon='eye' > 
		<#local btnTitle='#i18n{portal.users.admin_header.labelDeactivateAccessibilityMode}' >
	<#else>
	</#if>
	<@button color='link text-dark ps-2' type='submit' buttonIcon=btnIcon title=btnTitle />
</@tform>
</#macro>