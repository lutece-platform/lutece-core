<#--
Macro: img

Description: Generates an HTML image element with a specified URL, alternative text, title, class, and ID.

Parameters:
- url (string, required): the URL for the image.
- alt (string, optional): the alternative text to display if the image cannot be loaded or for accessibility purposes.
- title (string, optional): the title for the image.
- class (string, optional): additional classes to add to the image element.
- id (string, optional): the ID for the image element.
- params (string, optional): additional parameters to add to the HTML code.
-->
<#macro img url='' alt='' title='' class='img-fluid' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated /> 
<img src="${url}" alt="<#if alt!=''>${alt!}<#else>${title!}</#if>" title="${title}"<#if class!=''> class=" ${class}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> />
</#macro>