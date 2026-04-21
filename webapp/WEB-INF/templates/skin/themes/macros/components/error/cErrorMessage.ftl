<#--
Macro: cErrorMessage

Description: Generates a full-page error message with a danger alert, optional navigation links, and a home button.

Parameters:
- title (string, required): The error title displayed in the alert.
- text (string, required): The error description text.
- linkUrl (string, optional): URL for an additional navigation link. Default: ''.
- linkLabelUrl (string, optional): Label text for the navigation link. Default: ''.
- id (string, optional): The unique identifier for the error container. Default: ''.
- class (string, optional): Additional CSS class(es) for the error container. Default: ''.
- params (string, optional): Additional HTML attributes for the error container. Default: ''.

Showcase:
- desc: Message d'erreur - @cErrorMessage
- newFeature: false

Snippet:

    Basic error page:

    <@cErrorMessage title='Page not found' text='The page you are looking for does not exist.' />

    Error page with redirect link:

    <@cErrorMessage title='Access denied' text='You do not have permission to view this page.' linkUrl='jsp/site/Portal.jsp?page=login' linkLabelUrl='Sign in' />

-->
<#macro cErrorMessage title text linkUrl='' linkLabelUrl='' id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local linkLabelUrl>${linkLabelUrl}</#local>
<@cContainer class='d-flex align-items-center justify-content-center vh-50'>
  <@cRow>
    <@cCol class=class! id=id params=params >
      <@cAlert title=title type='danger'>
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