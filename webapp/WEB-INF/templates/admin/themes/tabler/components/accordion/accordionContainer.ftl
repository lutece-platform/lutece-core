<#--
Macro: accordionContainer

Description: Generates an HTML container element for a set of accordion-style UI elements with expandable and collapsible content.

Parameters:
- id (string, optional): the ID of the container element. If not provided, a default ID will be generated.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Accordion container with multiple panels:

    <@accordionContainer id='myAccordion'>
        <@accordionPanel childId='panel1' collapsed=false>
            <@accordionHeader title='First Section' />
            <@accordionBody>
                <p>Content of the first section.</p>
            </@accordionBody>
        </@accordionPanel>
        <@accordionPanel childId='panel2' collapsed=true>
            <@accordionHeader title='Second Section' />
            <@accordionBody>
                <p>Content of the second section.</p>
            </@accordionBody>
        </@accordionPanel>
    </@accordionContainer>

-->
<#macro accordionContainer id='' class='' params={}>
<#local containerId = id />
<#if !id?has_content>
   <#if accordionContainerId?? == false><#assign accordionContainerId = 1 ><#else><#assign accordionContainerId = accordionContainerId + 1 ></#if>
   <#local containerId = 'accCont_'+ accordionContainerId >
</#if>
<div<@commonAttrs id=containerId classBase='accordion' class=class params=params />>
<#assign parentId = containerId>
<#nested>
</div>
</#macro>