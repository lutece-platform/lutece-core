<#-- 
Macro: tableHeadBodySeparator

Parameters:
- id (string, optional): the ID of the <tbody> element.
- class (string, optional): the class of the <tbody> element.
- params (string, optional): additional parameters to add to the HTML code.

-->
<#macro tableHeadBodySeparator id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
</thead>
<tbody<#if id!=''> id="${id}"</#if><#if class!=''> class="${class?trim}"</#if><#if params!=''> ${params}</#if>>
</#macro>