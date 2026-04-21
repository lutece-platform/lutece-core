<#--
Macro: cBadge

Description: Generates a badge or tag element with an optional dismissible close button.

Parameters:
- label (string, optional): The text displayed in the badge. Default: ''.
- class (string, optional): Additional CSS class(es) for the badge. Default: ''.
- hasp (boolean, optional): If true, wraps the label in a paragraph element. Default: true.
- dismissible (boolean, optional): If true, displays a close button to dismiss the badge. Default: false.
- id (string, optional): The unique identifier for the badge. Default: ''.
- params (string, optional): Additional HTML attributes for the badge. Default: ''.

Showcase:
- desc: Badge - @cBadge
- bs: components/badge
- newFeature: false

Snippet:

    Basic badge:

    <@cBadge label='New' class='primary' />

    Dismissible badge:

    <@cBadge label='Filter: Paris' class='secondary' dismissible=true />

-->
<#macro cBadge label='' class='' hasp=true dismissible=false id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="badge <#if class !=''> ${class}</#if><#if dismissible> dismissible</#if>"<#if id!=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
<#if dismissible> 
	<div class="badge-content">
		<#if hasp><p class="badge-label">${label!}</p><#else>${label!}</#if>
		<#nested>
		<button type="button" class="btn-close" onclick="this.parentElement.style.display='none';" aria-label="Fermer"></button>
	</div>	
<#else>
	<#if hasp><p class="badge-label">${label!}</p><#else>${label!}</#if>
	<#nested>
</#if>
</div>
</#macro>