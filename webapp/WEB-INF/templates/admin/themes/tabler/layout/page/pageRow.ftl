<#--
Macro: pageRow
Description: Generates a row element.
Parameters:
- id (string, optional): the ID of the container element.
- width (string, optional): the width of the container element;
- class (string, optional): the CSS class of the container element.

Snippet:

    Basic page row:

    <@pageRow>
        <@pageColumn>Column content</@pageColumn>
    </@pageRow>

    Page row with custom ID and flex classes:

    <@pageRow id='main-row' class='g-3 align-items-stretch'>
        <@pageColumn width='250px'>Sidebar</@pageColumn>
        <@pageColumn>Main content</@pageColumn>
    </@pageRow>

-->
<#macro pageRow id='' width='' class='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="row<#if class !=''> ${class}</#if>" <#if id !=''> id="${id}"</#if>>
	<#nested>
</div>
</#macro>