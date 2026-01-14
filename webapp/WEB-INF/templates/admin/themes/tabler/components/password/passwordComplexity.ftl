<#-- 
Macro: passwordComplexity

Description: Generates a progress bar to display the complexity of a password.

Parameters:
- id (string, required): the ID of the progress bar.
- description (string, optional): a description of the progress bar.
- inputId (string, optional): the ID of the input field to use for the progress bar update.
- params (string, optional): additional parameters to add to the HTML code.
-->
<#macro passwordComplexity id description='' params='' inputId='password' value='0' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="progress"<#if params!=''> ${params}</#if>>
    <progress id="${id}" value="0" max="100" style="width: 0%" class="progress-bar progress-bar-striped">0%</div>
</div>
<#if description!=''>
	<span class="progress-description">${description}</span>
</#if>
<#if inputId!=''>
<script type="module">
import { LutecePassword } from './themes/shared/modules/lutecePassword.js';

const password = new LutecePassword();
password.passwordInput = '#${inputId}';
password.progressBar = '#${id}';
password.getComplexity( );
</script>
</#if>
</#macro>