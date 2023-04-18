<#--
Macro: pageRow
Description: Generates a row element.
Parameters:
- id (string, optional): the ID of the container element.
- width (string, optional): the width of the container element;
- class (string, optional): the CSS class of the container element.
-->
<#macro pageRow id='' width='' class=''>
<div class="row">
	<#nested>
</div>
</#macro>