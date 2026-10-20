<#-- 
Macro: td

Description: Generates an HTML <td> element with an optional ID, class, and various other features.

Parameters:
- id (string, optional): the ID of the <td> element.
- class (string, optional): the class of the <td> element.
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
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Deprecation:
- args (string[]): the list of deprecated arguments.

Snippet:

    Basic table cell:

    <@td>${user.name}</@td>

    Table cell with alignment and vertical alignment:

    <@td align='center' valign='middle'>
        ${item.price}
    </@td>

    Table cell spanning columns with responsive visibility:

    <@td colspan=2 hide=['xs', 'sm'] class='text-muted'>
        ${item.description}
    </@td>

-->
<#macro td id='' class='' hide=[] align='' valign='' cols=0 xs=0 sm=0 md=0 lg=0 xl=0 colspan=0 rowspan=0 flex=false params={} deprecated...>
<@deprecatedWarning args=deprecated />	
	<#local classExtra = displaySettings( hide, 'table-cell' ) + ' ' + alignmentSettings( align, '' ) />
	<#if cols!=0>
		<#local classExtra += ' col-${cols}' />
	</#if>
	<#local breakpoints = {'xs':xs, 'sm':sm, 'md':md, 'lg':lg, 'xl':xl}>
	<#list breakpoints as breakpointkey,breakpointvalue>
		<#if breakpointvalue!=0>
			<#local classExtra += ' col-${breakpointkey}-${breakpointvalue}' />
		</#if>
	</#list>
	<#if valign?has_content><#local classExtra += ' align-' + valign /></#if>
	<#if flex><#local classExtra += ' d-flex' /></#if>
	<td<@commonAttrs id=id class=class classExtra=classExtra params=params /><#if colspan gt 0> colspan="${colspan}"</#if><#if rowspan gt 0> rowspan="${rowspan}"</#if>>
		<#nested>
	</td>
</#macro>