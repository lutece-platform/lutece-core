<#--
Macro: fcBtnImg

Description: Generates a FranceConnect authentication button with the official branded image, either as a button or a link.

Parameters:
- label (string, optional): Text displayed above the FranceConnect brand name. Default: 'Créer mon compte avec'.
- linkClass (string, optional): CSS class applied to the link when type is 'link'. Default: ''.
- type (string, optional): Render mode, either 'button' or 'link'. Default: 'button'.
- btnClass (string, optional): CSS class applied to the button element. Default: 'fr-connect m-0'.
- url (string, optional): URL for the link when type is 'link'. Default: ''.

Snippet:

    Basic button usage:

    <@fcBtnImg />

    As a link with custom label:

    <@fcBtnImg type='link' label='Se connecter avec' url='jsp/site/Portal.jsp?page=mylutece&action=doLogin' linkClass='fc-link' />

-->
<#macro fcBtnImg label='Créer mon compte avec' linkClass='' type='button' btnClass='fr-connect m-0' url='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if type='button'>
<@cBlock class='fr-connect-group m-0'>    
<@cBtn label='' class=btnClass noclass=true>        
    <@cInline class='fr-connect__login'>${label}</@cInline>        
    <@cInline class='fr-connect__brand'>FranceConnect</@cInline>    
</@cBtn>    
</@cBlock>
<#else>    
<@cLink label='' href=url class=linkClass params='aria-label="${label!} FranceConnect"'> 
<@cBlock class='fr-connect-group m-0'>    
<@cInline class=btnClass>        
    <@cInline class='fr-connect__login'>${label}</@cInline>        
    <@cInline class='fr-connect__brand'>FranceConnect</@cInline>    
</@cInline>    
</@cBlock>
</@cLink>
</#if>
</#macro>