<#-- Macro code 
Description :  Generates a code block with the provided content. The macro accepts an optional ID and class for styling purposes. The content is wrapped in a <pre> tag for preserving whitespace and a <code> tag for semantic meaning. 
Parameters :
- id (string, optional): the ID to assign to the <pre> element.
- class (string, optional): additional classes to assign to the <pre> element.  

-->
<#macro code id='' class='w-100 m-0' codeClass='text-wrap' deprecated...> 
<@pre class=class id=id >
<code class="${codeClass}">
<#nested>
</code>
</@pre>
</#macro>