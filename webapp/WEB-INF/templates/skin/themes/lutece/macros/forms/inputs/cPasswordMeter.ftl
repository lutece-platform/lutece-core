<#-- Macro: cPasswordMeter                               
Parameters:
@param - name - string - required - Nom du champ
@param - label - string - Default '#i18n{portal.theme.labelPasswordStrength} #i18n{portal.theme.labelPasswordNoPasswordTyped}' Label asssocié ua message sur la force du mot de passe
@param - labelRefresh - string - Default '#i18n{portal.theme.labelPasswordRandomize}' label associé au bouton de rafraichir le mot de passe
@param - labelSecurity - string - Default '#i18n{portal.theme.labelPasswordSecurity}' label sur les conditions de sécurité du mot de passe
@param - url - string - Default '' Url to generate password
@param - indicator - string - Default '', Id de l'input                           
@param - class - string -  Default 'custom-checkbox', classe css à ajouter à l'input
@param - params - string - Default '', Tous autres paramètres à ajouter à l'input                   

@sample : 

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