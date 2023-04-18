<#-- Macro intiTooltip 
-- Options
allowList	object	Default value	Object which contains allowed attributes and tags.
animation	boolean	true	Apply a CSS fade transition to the tooltip.
boundary	string, element	'clippingParents'	Overflow constraint boundary of the tooltip (applies only to Popper’s preventOverflow modifier). By default, it’s 'clippingParents' and can accept an HTMLElement reference (via JavaScript only). For more information refer to Popper’s detectOverflow docs.
container	string, element, false	false	Appends the tooltip to a specific element. Example: container: 'body'. This option is particularly useful in that it allows you to position the tooltip in the flow of the document near the triggering element - which will prevent the tooltip from floating away from the triggering element during a window resize.
customClass	string, function	''	Add classes to the tooltip when it is shown. Note that these classes will be added in addition to any classes specified in the template. To add multiple classes, separate them with spaces: 'class-1 class-2'. You can also pass a function that should return a single string containing additional class names.
delay	number, object	0	Delay showing and hiding the tooltip (ms)—doesn’t apply to manual trigger type. If a number is supplied, delay is applied to both hide/show. Object structure is: delay: { "show": 500, "hide": 100 }.
fallbackPlacements	array	['top', 'right', 'bottom', 'left']	Define fallback placements by providing a list of placements in array (in order of preference). For more information refer to Popper’s behavior docs.
html	boolean	false	Allow HTML in the tooltip. If true, HTML tags in the tooltip’s title will be rendered in the tooltip. If false, innerText property will be used to insert content into the DOM. Use text if you’re worried about XSS attacks.
offset	array, string, function	[0, 0]	Offset of the tooltip relative to its target. You can pass a string in data attributes with comma separated values like: data-bs-offset="10,20". When a function is used to determine the offset, it is called with an object containing the popper placement, the reference, and popper rects as its first argument. The triggering element DOM node is passed as the second argument. The function must return an array with two numbers: skidding, distance. For more information refer to Popper’s offset docs.
placement	string, function	'top'	How to position the tooltip: auto, top, bottom, left, right. When auto is specified, it will dynamically reorient the tooltip. When a function is used to determine the placement, it is called with the tooltip DOM node as its first argument and the triggering element DOM node as its second. The this context is set to the tooltip instance.
popperConfig	null, object, function	null	To change Bootstrap’s default Popper config, see Popper’s configuration. When a function is used to create the Popper configuration, it’s called with an object that contains the Bootstrap’s default Popper configuration. It helps you use and merge the default with your own configuration. The function must return a configuration object for Popper.
sanitize	boolean	true	Enable or disable the sanitization. If activated 'template', 'content' and 'title' options will be sanitized.
sanitizeFn	null, function	null	Here you can supply your own sanitize function. This can be useful if you prefer to use a dedicated library to perform sanitization.
selector	string, false	false	If a selector is provided, tooltip objects will be delegated to the specified targets. In practice, this is used to also apply tooltips to dynamically added DOM elements (jQuery.on support). See this issue and an informative example. Note: title attribute must not be used as a selector.
template	string	'<div class="tooltip" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>'	Base HTML to use when creating the tooltip. The tooltip’s title will be injected into the .tooltip-inner. .tooltip-arrow will become the tooltip’s arrow. The outermost wrapper element should have the .tooltip class and role="tooltip".
title	string, element, function	''	The tooltip title. If a function is given, it will be called with its this reference set to the element that the popover is attached to.
trigger	string	'hover focus'	How tooltip is triggered: click, hover, focus, manual. You may pass multiple triggers; separate them with a space. 'manual' indicates that the tooltip will be triggered programmatically via the .tooltip('show'), .tooltip('hide') and .tooltip('toggle') methods; this value cannot be combined with any other trigger. 'hover' on its own will result in tooltips that cannot be triggered via the keyboard, and should only be used if alternative methods for conveying the same information for keyboard users is present.
-->
<#macro initToolTip class='' id='' options='' autohide=false animation=true duration=2000 params='' deprecated...>
<script>
document.addEventListener("DOMContentLoaded", function() {
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
    <#if options !=''>const tooltipOptions = {${options}}</#if>
    const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl<#if options !=''>, tooltipOptions</#if> ))
});
</script>
</#macro>