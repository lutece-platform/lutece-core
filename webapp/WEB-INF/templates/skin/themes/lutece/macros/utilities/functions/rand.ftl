<#-- INTERNAL FUNCTIONS -->
<#--
* Generates a "random" integer between min and max (inclusive)
*
* Note the values this function returns are based on the current
* second the function is called and thus are highly deterministic
* and SHOULD NOT be used for anything other than inconsequential
* purposes, such as picking a random image to display.
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