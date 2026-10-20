<#-- 
Macro: passwordComplexity

Description: Generates a progress bar to display the complexity of a password.

Parameters:
- id (string, required): the ID of the progress bar.
- description (string, optional): a description of the progress bar.
- inputId (string, optional): the ID of the input field to use for the progress bar update.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Basic password complexity indicator:

    <@passwordComplexity id='pwdStrength' />

    Password complexity with custom input and description:

    <@passwordComplexity id='pwdComplexity' inputId='new_password' description='Password must contain at least 8 characters' />

-->
<#macro passwordComplexity id description='' params={} inputId='password' value='0' >
<@deprecatedWarning args=deprecated />
<div class="lutece-progress-wrapper "<@commonAttrs params=params />>
    <progress id="${id}" value="0" max="100" class="lutece-progress lutece-progress-danger"></progress>
    <span id="${id}-text" class="lutece-progress-text">0%</span>
    <div aria-live="polite" class="visually-hidden" id="${id}-status"></div>
    <#if description?has_content>
        <span class="lutece-progress-description">${description}</span>
    </#if>
</div>
<#if inputId?has_content>
<script type="module">
import { LutecePassword } from './themes/shared/modules/lutecePassword.js';

const password = new LutecePassword();
password.passwordInput = '#${inputId?js_string?no_esc}';
password.progressBar = '#${id?js_string?no_esc}';
password.getComplexity( );
</script>
</#if>
</#macro>