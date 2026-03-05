<#-- Function: responsiveDisplay

Description: Generates Bootstrap responsive display classes based on provided type and breakpoints.

Parameters:
- type (string): the type of display setting to apply. Available values are: block, inline, inline-block, none.
- breakpoints (map, optional): a map of breakpoints and corresponding values to apply the display setting to. Available breakpoints are: xs, sm, md, lg, xl. Value can be any valid Bootstrap display value.

Returns:
- A string of Bootstrap responsive display classes based on the provided type and breakpoints.

Snippet:

    Generate responsive column classes:

    ${responsiveDisplay('col', {'xs': 12, 'sm': 6, 'md': 4})}
    => Returns: "col-12 col-sm-6 col-md-4"
    Generate responsive offset classes:

    ${responsiveDisplay('offset', {'md': 2, 'lg': 3})}
    => Returns: "offset-md-2 offset-lg-3"

-->
<#function responsiveDisplay type='' breakpoints={}>
<#local responsiveDisplayClass = '' />
<#list breakpoints as breakpointkey,breakpointvalue>
	<#if breakpointvalue!=0>
		<#if breakpointkey = 'xs'>
			<#local responsiveDisplayClass += ' ${type}-${breakpointvalue}' />
		<#else>
			<#local responsiveDisplayClass += ' ${type}-${breakpointkey}-${breakpointvalue}' />
		</#if>
	</#if>
</#list>
<#return responsiveDisplayClass?trim>
</#function>