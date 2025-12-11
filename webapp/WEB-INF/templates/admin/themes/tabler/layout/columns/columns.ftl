<#--
Macro: columns

Description: Generates a grid column using the Bootstrap grid system with responsive options for offset, push, pull, and order.

Parameters:
- tag (string, optional): the HTML tag to use for the column element (e.g. "div", "section", "article", etc.).
- offset* (integer, optional): the number of columns to offset the column on extra small screens.
- offsetSm (integer, optional): the number of columns to offset the column on small screens.
- offsetMd (integer, optional): the number of columns to offset the column on medium screens.
- offsetLg (integer, optional): the number of columns to offset the column on large screens .
- offsetXl (integer, optional): the number of columns to offset the column on extra large screens.
- offset (map, optional): a map of screen sizes and corresponding offset values. Overrides offsetXs, offsetSm, offsetMd, offsetLg, offsetXl if provided.
- pushXs (integer, optional): the number of columns to push the column on extra small screens.
- pushSm (integer, optional): the number of columns to push the column on small screens.
- pushMd (integer, optional): the number of columns to push the column on medium screens.
- pushLg (integer, optional): the number of columns to push the column on large screens .
- pushXl (integer, optional): the number of columns to push the column on extra large screens.
- pullXs (integer, optional): the number of columns to pull the column on extra small screens.
- pullSm (integer, optional): the number of columns to pull the column on small screens.
- pullMd (integer, optional): the number of columns to pull the column on medium screens.
- pullLg (integer, optional): the number of columns to pull the column on large screens .
- pullXl (integer, optional): the number of columns to pull the column on extra large screens.
- xs (integer, optional): the number of columns the column should span on extra small screens.
- sm (integer, optional): the number of columns the column should span on small screens.
- md (integer, optional): the number of columns the column should span on medium screens.
- lg (integer, optional): the number of columns the column should span on large screens .
- xl (integer, optional): the number of columns the column should span on extra large screens.
- order (map or integer, optional): a map of screen sizes and corresponding order values, or a single order value that applies to all screen sizes.
- id (string, optional): the ID of the column element.
- class (string, optional): the CSS class of the column element.
- align (string, optional): the text alignment of the column element (left, right, center, justify).
- collapsed (boolean, optional): whether to add the "collapse" class to the column element (useful for hiding/showing columns).
- params (string, optional): any additional attributes to include in the column elemen
-->
<#macro columns tag='div' offsetXs=0 offsetSm=0 offsetMd=0 offsetLg=0 offsetXl=0 offset={} pushXs=0 pushSm=0 pushMd=0 pushLg=0 pushXl=0 pullXs=0 pullSm=0 pullMd=0 pullLg=0 pullXl=0 xs=0 sm=0 md=0 lg=0 xl=0 order={} id='' class='' align='' collapsed=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if collapsed><#local class += ' ' + 'collapse' /></#if>
<#if align!=''><#local class+= ' ' + alignmentSettings(align,'') /></#if>
<#local class += ' ' + responsiveDisplay('col',{'xs':xs, 'sm':sm, 'md':md, 'lg':lg, 'xl':xl}) />
<#if offset?has_content>
	<#local class += ' ' + responsiveDisplay('offset',offset) />
<#elseif offsetXs &gt; 0 || offsetSm &gt; 0 || offsetMd &gt; 0 || offsetLg &gt; 0 || offsetXl &gt; 0>
	<#local class += ' ' + responsiveDisplay('offset',{'xs':offsetXs, 'sm':offsetSm, 'md':offsetMd, 'lg':offsetLg, 'xl':offsetXl}) />
</#if>
<#if order?has_content>
	<#if order?is_number>
		<#local class += ' order-${order}' />
	<#elseif order?is_hash>
		<#local class += ' ' + responsiveDisplay('order',order) />
	</#if>
</#if>
<${tag} class="<#if class?trim!=''>${class?trim}<#else>col</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</${tag}>
</#macro>