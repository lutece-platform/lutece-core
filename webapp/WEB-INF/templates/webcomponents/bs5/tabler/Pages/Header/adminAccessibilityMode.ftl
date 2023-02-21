<#--
Macro: adminAccessibilityMode

Description: Generates a form to activate or deactivate accessibility mode for an administrative user.

Parameters:
- token (string, required): the security token used to prevent cross-site request forgery (CSRF) attacks.
- user.accessibilityMode (boolean, required): a boolean value indicating whether accessibility mode is currently active.
-->
<#macro adminAccessibilityMode>
<@tform method='post' action='jsp/admin/DoModifyAccessibilityMode.jsp' class="ml-2">
	<@input type='hidden' name='token' value='${token}' />
	<#if user.accessibilityMode>
		<@button color='link ' type='submit' buttonIcon='eye' title='#i18n{portal.users.admin_header.labelDeactivateAccessibilityMode}'/>
	<#else>
		<@button color='link text-dark' type='submit' buttonIcon='eye-slash' title='#i18n{portal.users.admin_header.labelActivateAccessibilityMode}'/>
	</#if>
</@tform>
</#macro>