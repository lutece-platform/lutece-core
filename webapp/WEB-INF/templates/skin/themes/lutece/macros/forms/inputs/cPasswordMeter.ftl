<#--
Macro: cPasswordMeter

Description: Generates a password strength meter with a generate-password button and a visual indicator showing password quality (weak, medium, excellent).

Parameters:
- id (string, required): the ID of the associated password input.
- label (string, optional): label for the password strength message. Default: '#i18n{portal.theme.labelPasswordStrength} #i18n{portal.theme.labelPasswordNoPasswordTyped}'.
- labelRefresh (string, optional): label for the generate password button. Default: '#i18n{portal.theme.labelPasswordRandomize}'.
- labelSecurity (string, optional): label for the security conditions. Default: '#i18n{portal.theme.labelPasswordSecurity} :'.
- url (string, optional): URL to generate a password via AJAX. Default: ''.
- indicator (string, optional): custom indicator HTML content. Default: ''.
- class (string, optional): CSS class for the meter container. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.

Snippet:

    Password meter for a password field:

    <@cPasswordMeter id='new_password' />

    Password meter with custom labels:

    <@cPasswordMeter id='user_password' labelRefresh='Generate a new password' labelSecurity='Security requirements:' />

-->
<#macro cPasswordMeter id label='#i18n{portal.theme.labelPasswordStrength} #i18n{portal.theme.labelPasswordNoPasswordTyped}' labelRefresh='#i18n{portal.theme.labelPasswordRandomize}' labelSecurity='#i18n{portal.theme.labelPasswordSecurity} :' url='' indicator='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@cRow id='password-${id}-wrapper' params=params>
    <@cCol cols='12 col-md-7'>
        <@cBtn id='password-${id}-generator' type='button' label='' class='pass font-xs text-underline p-0 mt-xs font-medium-bold cursor-pointer' >
            <@parisIcon name='refresh' class='main-info-color password-refresh' /> ${labelRefresh!}
        </@cBtn>
    </@cCol>
    <@cCol cols='12 col-md-5'>
        <@cBlock params='aria-valuemax="100" aria-valuemin="0" aria-valuetext="${label!}" aria-label="#i18n{portal.theme.labelPasswordStrength}" aria-valuenow="0" role="meter" id="aria-${id}-meter"'>
            <@cBlock class="indicator visually-hidden d-flex align-items-center justify-content-end">
                <@cInline>${labelSecurity!}</@cInline><#if indicator=''><@cInline class='ml-2'><@cInline class='dot'></@cInline></@cInline><#else>${indicator}</#if><@cInline class='meter-text ml-1'></@cInline>
            </@cBlock>    
        </@cBlock>
        <output class="visually-hidden" for="aria-${id}-meter"></output>
    </@cCol>
</@cRow>
</#macro>