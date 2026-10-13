<#--
Macro: accordionContainer

Description: Generates an HTML container element for a set of accordion-style UI elements with expandable and collapsible content.

Parameters:
- id (string, optional): the ID of the container element. If not provided, a default ID will be generated.
- params (string, optional): additional HTML attributes to include in the container element.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

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
<#macro accordionContainer id='' class='' params='' attrs={}>
<#local containerId = id />
<#if !id?has_content>
   <#if accordionContainerId?? == false><#assign accordionContainerId = 1 ><#else><#assign accordionContainerId = accordionContainerId + 1 ></#if>
   <#local containerId = 'accCont_'+ accordionContainerId >
</#if>
<div<@commonAttrs id=containerId classBase='accordion' class=class params=params attrs=attrs />>
<#assign parentId = containerId>
<#nested>
</div>
</#macro>