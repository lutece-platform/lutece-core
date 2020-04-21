<#assign boxTitle>#i18n{portal.admin.admin_login.boxTitle}</#assign>
<@adminLoginPage title=boxTitle site_name=site_name>
    <@tform name='login' id='login' action='${do_admin_login_url}'>
    <@input type='hidden' name='token' value='${token}' />
    <#list params_list as param><@input type='hidden' name='${param.code}' value='${param.name}' /></#list>
    <@formGroup labelFor='access_code' labelKey='${i18n("portal.admin.admin_login.loginLabel")}' mandatory=true rows=2>
        <@inputGroup>
            <@inputGroupItem type='text'><@icon style='user' /></@inputGroupItem>
            <@input name='access_code' id='access_code' placeHolder='${i18n("portal.admin.admin_login.loginLabel")}' params=' autofocus' />
        </@inputGroup>
    </@formGroup>
    <@formGroup labelFor='password' labelKey='${i18n("portal.admin.admin_login.passwordLabel")}' helpKey='${i18n("portal.admin.admin_login.helpText")}' mandatory=true rows=2>
        <@inputGroup>
            <@inputGroupItem type='text'><@icon style='lock' /></@inputGroupItem>
            <@input type='password' name='password' id='password' placeHolder='${i18n("portal.admin.admin_login.passwordLabel")}' />
        </@inputGroup>
    </@formGroup>
    <@formGroup class='text-center' rows=2>
			<@button type='submit' class='btn-block' buttonIcon='check' title='#i18n{portal.admin.admin_login.buttonConnect}' />
		</@formGroup>
    <p class="text-right mt-3">
        <@link href='${forgot_password_url?if_exists}' title='#i18n{portal.admin.admin_login.forgotPassword}'>#i18n{portal.admin.admin_login.forgotPassword} ?</@link>&nbsp;
        <@link href='${forgot_login_url?if_exists}' title='#i18n{portal.admin.admin_login.forgotLogin}'>#i18n{portal.admin.admin_login.forgotLogin} ?</@link>
    </p>
    </@tform>
</@adminLoginPage>
