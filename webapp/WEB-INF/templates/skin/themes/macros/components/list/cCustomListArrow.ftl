<#--
Macro: cCustomListArrow

Description: Generates a custom arrow-styled list. Deprecated: use cList instead.

Parameters:
- items (list, optional): List of items to display. Default: ''.

Showcase:
- desc: Liste flèche - @cCustomListArrow
- newFeature: false

Snippet:

    Basic usage (deprecated, prefer cList):

    <@cCustomListArrow items=myItems />

-->
<#macro cCustomListArrow items=''>
<@cList items />
</#macro>