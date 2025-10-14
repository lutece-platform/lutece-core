<#-- Macro fcBtnImg 

Description: Bouton franceconnect avec image

Parameters:
@param - label - string - optionnal - Texte par défaut 'Créer mon compte avec' 
@param - linkClass - string - optional - Classe CSS pour les liens
@param - type - string - optional - 'button' par défaut peut être 'link'
@param - btnClass - string - optional - 'fr-connect m-0' classe CSS pour le bouton
@param - url - string - optional - Url pour le type 'link'
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