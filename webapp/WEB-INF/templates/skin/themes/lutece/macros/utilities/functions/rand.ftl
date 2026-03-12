<#-- INTERNAL FUNCTIONS -->
<#--
Function: random

Description: Generates a pseudo-random number based on the current timestamp. This function is deterministic and should only be used for inconsequential purposes such as picking a random image or generating unique IDs.

Parameters:
- None.

Snippet:

    Generate a random-based ID:

    <#assign uniqueId = 'element-' + random() />

    Pick a random CSS class:

    <#assign classes = ['bg-primary', 'bg-secondary', 'bg-info'] />
    <div class="${classes[random()?floor % classes?size]}">Random background</div>

-->
<#assign rnd=turnoverstr(.now?long?string)?number />
<#function random >
<#local h=turnoverstr(.now?long)?string />
<#local r=h?number + rnd />
<#if r gte 1><#local r=r-1 /></#if>
<#assign rnd=r />
<#return r />
</#function>
<#function turnoverstr str >
<#local l = str?length />
<#local r = ""/>
<#list 1..l as i>
<#local r = r+str?substring(l-i,l-i+1) />
</#list>
<#return r/>
</#function>