<#--
Macro: pageColumnBtn
Description: Generates a button that toggles the visibility of a pageColumn with an optional custom CSS class.
Parameters:
- idPageColumn (string, required): The ID of the container element to be toggled.
- class (string, optional): The CSS class of the button element.
- hideSize (string, optional): The breakpoint at which the button should be hidden. Default is 'xl'. Accepts standard breakpoints (xs, sm, md, lg, xl, xxl).
- title (string, optional): The title attribute for the button element.
- btnColor (string, optional): The color variant of the button. Default is 'primary'.
-->
<#macro pageColumnBtn idPageColumn class='' hideSize='' title='' btnColor='primary' deprecated...>
<@deprecatedWarning args=deprecated />
<#assign hideSizebtn><#if hideSize!=''>d-${hideSize}-none</#if></#assign>
<@button color='primary' class='${hideSizebtn} ${class}' title='${title}' buttonIcon='menu-2'  params='data-bs-toggle="offcanvas" data-bs-target="#${idPageColumn}" aria-controls="${idPageColumn}"' />
</#macro>
