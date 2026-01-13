<#-- Macro: item_navigation

Description: Generates a Bootstrap navigation bar with links to the previous and next items in a list.

Parameters:
- item_navigator (object): an object that contains information about the current and next/previous items in a list.
- id (string, optional): the ID of the navigation bar.
- align (string, optional): the alignment of the navigation bar (left, right, or center).
- hideButtonTitle (list, optional): a list of button titles to hide (e.g. "title", "i18nTitle").
- buttonColor (string, optional): the color of the navigation buttons, using a Bootstrap color class (e.g. "primary").
- buttonSize (string, optional): the size of the navigation buttons, using a Bootstrap size class (e.g. "lg").
-->
<#macro item_navigation item_navigator id='item-navigator' display='' align='' hideButtonTitle=[] buttonColor='info' buttonSize='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local class = alignmentSettings(align,'') />
<nav id="${id}" class="${class} <#if display!=''>d-inline</#if>">
<#if (item_navigator.currentItemId > 0)>
	<@aButton href='${item_navigator.previousPageLink?xhtml}' title='#i18n{portal.util.labelPrevious}' buttonIcon='arrow-left' color='${buttonColor}' hideTitle=hideButtonTitle size='${buttonSize}' />
</#if>
<#if (item_navigator.currentItemId < item_navigator.listItemSize - 1) >
	<@aButton href='${item_navigator.nextPageLink?xhtml}' title='#i18n{portal.util.labelNext}' buttonIcon='arrow-right' color='${buttonColor}' hideTitle=hideButtonTitle size='${buttonSize}' />
</#if>
</nav>
</#macro>