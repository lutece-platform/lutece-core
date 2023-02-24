<#-- Function: responsiveDisplay

Description: Generates Bootstrap responsive display classes based on provided type and breakpoints.

Parameters:
- type (string): the type of display setting to apply. Available values are: block, inline, inline-block, none.
- breakpoints (map, optional): a map of breakpoints and corresponding values to apply the display setting to. Available breakpoints are: xs, sm, md, lg, xl. Value can be any valid Bootstrap display value.

Returns:
- A string of Bootstrap responsive display classes based on the provided type and breakpoints.
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