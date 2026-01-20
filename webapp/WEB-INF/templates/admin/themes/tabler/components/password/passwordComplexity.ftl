<#-- 
Macro: passwordComplexity

Description: Generates a progress bar to display the complexity of a password.

Parameters:
- id (string, required): the ID of the progress bar.
- description (string, optional): a description of the progress bar.
- inputId (string, optional): the ID of the input field to use for the progress bar update.
- params (string, optional): additional parameters to add to the HTML code.
-->
<#macro passwordComplexity id description='' params='' inputId='password' value='0' >
<div class="lutece-progress-wrapper "<#if params!=''> ${params}</#if>>
    <progress id="${id}" value="0" max="100" class="lutece-progress lutece-progress-danger"></progress>
    <span id="${id}-text" class="lutece-progress-text">0%</span>
    <div aria-live="polite" class="visually-hidden" id="${id}-status"></div>
    <#if description!=''>
    	<span class="lutece-progress-description">${description}</span>
    </#if>
</div>
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