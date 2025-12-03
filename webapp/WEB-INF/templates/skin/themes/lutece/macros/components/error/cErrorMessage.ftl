<#-- Macro: cErrorMessage

Description: permet de créer un message d'erreur.

Parameters:

@param - id - string - optional - l'ID du message d'erreur
@param - class - string - optional - ajoute une classe CSS au message d'erreur
@param - title - string - required - permet de définir le titre du message d'erreur
@param - text - string - required - permet de définir le texte du message d'erreur
@param - linkUrl - string - optional - permet d'ajouter un lien après le texte du message d'erreur
@param - linkLabelUrl - string - optional - permet de définir le label du lien du message d'erreur
@param - params - string - optional - permet d'ajouter des parametres HTML au message d'erreur
-->
<#macro cErrorMessage title text linkUrl='' linkLabelUrl='' id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local linkLabelUrl>${linkLabelUrl}</#local>
<@cContainer class='d-flex align-items-center justify-content-center vh-50'>
  <@cRow>
    <@cCol class=class! id=id params=params >
      <@cAlert title=title class='danger'>
        <#if title?trim != text?trim><@cText>${text}</@cText></#if>
        <#nested>
      </@cAlert>
      <#if linkUrl !=''>
        <@chList class='list-unstyled d-flex justify-content-center gap-2 mt-l'>
          <@chItem>
            <@cLink href='.' label='#i18n{portal.theme.home}' class='btn btn-secondary' />
          </@chItem>
          <@chItem>
            <#if linkLabelUrl=''><#local linkLabelUrl>#i18n{portal.util.labelBackHome}</#local></#if>
            <@cLink href=linkUrl label=linkLabelUrl class='btn btn-primary' />
          </@chItem>
        </@chList>
      <#else>
        <@cText class='text-center mt-l'>
          <@cLink href='.' label='#i18n{portal.theme.home}' class='btn btn-secondary' />
        </@cText>
      </#if>
    </@cCol>
  </@cRow>
</@cContainer>
</#macro>