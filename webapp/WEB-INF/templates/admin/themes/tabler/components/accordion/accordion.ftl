<#--
Macro: accordion

Description: Generates an HTML accordion styled UI element with expandable and collapsible content.

Parameters:
- id (string, optional): the ID of the container element. If not provided, a default ID will be generated.
- icon (string, optional): the icon to display in the header.
- collapsed (boolean, optional): whether the accordion should be collapsed by default.
- params (string, optional): additional HTML attributes to include in the container element.

Snippet:

    Simple accordion with title and content:

    <@accordion id='faq-section' title='Frequently Asked Questions'>
        <p>Here are the answers to common questions about this feature.</p>
    </@accordion>

    Accordion initially collapsed with icon:

    <@accordion id='advanced-options' title='Advanced Options' icon='settings' collapsed=true>
        <p>Configure advanced settings for your application.</p>
    </@accordion>

-->
<#macro accordion id title class='' headerClass='' icon='' collapsed=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@accordionContainer id=id class=class params=params>
   <@accordionPanel collapsed=collapsed childId='${id}Child'>
      <@accordionHeader class=headerClass title=title headerIcon=icon />
      <@accordionBody>
      <#nested>
      </@accordionBody>
   </@accordionPanel>
</@accordionContainer>
</#macro>   