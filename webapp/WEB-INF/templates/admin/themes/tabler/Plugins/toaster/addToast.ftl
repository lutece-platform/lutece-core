<#-- Macro: addToast

Description: Adds a Bootstrap toast notification to the page.

Parameters:
- title (string): the title of the notification.
- content (string): the content of the notification.
- type (string, optional): the type of the notification
-->
<#macro addToast title content type='default' >
lutecepolipop.add({
    content: '${content}',
    title: '${title}',
    type: '${type}',
});
</#macro>