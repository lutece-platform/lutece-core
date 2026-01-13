<#-- 
Macro: th

Description: Generates an HTML <th> element with an optional ID, title, scope, class, and various other features.

Parameters:
- id (string, optional): the ID of the <th> element.
- title (string, optional): the title of the <th> element.
- scope (string, optional): the scope of the <th> element.
- class (string, optional): the class of the <th> element.
- hide (string[], optional): an array of breakpoint names at which to hide the table cell.
- align (string, optional): the horizontal alignment of the table cell contents.
- valign (string, optional): the vertical alignment of the table cell contents.
- cols (int, optional): the number of columns to span.
- xs (int, optional): the number of columns to span on extra-small screens.
- sm (int, optional): the number of columns to span on small screens.
- md (int, optional): the number of columns to span on medium screens.
- lg (int, optional): the number of columns to span on large screens.
- xl (int, optional): the number of columns to span on extra-large screens.
- colspan (int, optional): the number of columns to span.
- rowspan (int, optional): the number of rows to span.
- flex (boolean, optional): whether to use flexbox for layout.
- params (string, optional): additional parameters to add to the HTML code.

-->
<#macro th id='' title='' scope='' class='' align='' valign='' hide=[] cols=0 xs=0 sm=0 md=0 lg=0 xl=0 colspan=0 rowspan=0 flex=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local class += ' ' + displaySettings(hide,'table-cell') + ' ' + alignmentSettings(align,'text') />
<#if cols!=0>
	<#local class += ' col-${cols}' />
</#if>
<#local breakpoints = {'xs':xs, 'sm':sm, 'md':md, 'lg':lg, 'xl':xl}>
<#list breakpoints as breakpointkey,breakpointvalue>
	<#if breakpointvalue!=0>
		<#local class += ' col-${breakpointkey}-${breakpointvalue}' />
	</#if>
</#list>
<#if valign!=''><#local class += ' align-' + valign /></#if>
<#if flex><#local class += ' d-flex' /></#if>
<th<#if class?trim != ''> class="${class?trim}"</#if><#if id!=''> id="${id}"</#if><#if title!=''> title="${title}"</#if><#if scope!=''> scope="${scope}"</#if><#if colspan gt 0> colspan="${colspan}"</#if><#if rowspan gt 0> rowspan="${rowspan}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</th>
</#macro>