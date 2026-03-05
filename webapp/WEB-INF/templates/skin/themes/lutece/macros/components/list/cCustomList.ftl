<#--
Macro: cCustomList

Description: Generates a custom list component. Deprecated: use cList instead.

Parameters:
- items (list, required): List of items to display.
- type (string, optional): List style type. Default: 'arrow'.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Snippet:

    Basic usage (deprecated, prefer cList):

    <@cCustomList items=myItems type='arrow'>
        <li>Additional item</li>
    </@cCustomList>

-->
<#macro cCustomList items type='arrow' class='' id='' params=''>
<@cList items type class id params >
    <#nested>    
</@cList>
</#macro>