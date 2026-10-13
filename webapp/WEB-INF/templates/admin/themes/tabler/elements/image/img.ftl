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
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Basic image with URL and alt text:

    <@img url='images/photo.jpg' alt='A landscape photo' />

    Image with custom class, title, and ID:

    <@img url='images/avatar.png' alt='User avatar' title='Profile picture' class='img-thumbnail rounded-circle' id='user-avatar' />

-->
<#macro img url='' alt='' title='' class='img-fluid' id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated /> 
<img src="${url}" alt="<#if alt!=''>${alt!}<#else>${title!}</#if>" title="${title}"<#if class!=''> class=" ${class}"</#if><@commonAttrs id=id params=params attrs=attrs /> />
</#macro>