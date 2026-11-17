<#--
Macro: cBadge

Description: Generates a badge or tag element with an optional dismissible close button.

Parameters:
- label (string, optional): The text displayed in the badge. Default: ''.
- class (string, optional): Additional CSS class(es) for the badge. Default: ''.
- hasp (boolean, optional): If true, wraps the label in a span element. If class contains a 'bg-XXX' class, a 'text-bg-XXX' class is added to this span. Default: true.
- dismissible (boolean, optional): If true, displays a close button to dismiss the badge. Default: false.
- id (string, optional): The unique identifier for the badge. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Badge - @cBadge
- bs: components/badge
- newFeature: false
- updatedFeature: true
- deprecated: false

Snippet:

    Basic badge:

    <@cBadge label='New' class='primary' />

    Dismissible badge:

    <@cBadge label='Filter: Paris' class='secondary' dismissible=true />

-->
<#macro cBadge label='' class='' hasp=true dismissible=false id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local labelClass = 'badge-label' />
<#if hasp><#list class?split(' ') as c><#if c?starts_with('bg-')><#local labelClass = labelClass + ' text-' + c /></#if></#list></#if>
<div class="badge <#if class?has_content> ${class}</#if><#if dismissible> dismissible</#if>"<@commonAttrs id=id params=params />>
<#if dismissible> 
	<div class="badge-content">
		<#if hasp><span class="${labelClass}">${label!}</span><#else>${label!}</#if>
		<#nested>
		<button type="button" class="btn-close" data-lutece-action="hide-parent" aria-label="Fermer"></button>
	</div>	
<#else>
	<#if hasp><span class="${labelClass}">${label!}</span><#else>${label!}</#if>
	<#nested>
</#if>
</div>
</#macro>