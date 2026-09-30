<#-- Main Colors Vars               -->
<#-- Only override defined vars     -->
<#-- Update ME IN BO                -->
<#assign lightColors = dskey('theme.site_property.layout.colors.light.textblock')!''>
<#assign darkColors = dskey('theme.site_property.layout.colors.dark.textblock')!''>
<style>
/*** 										***/
/*** LIGHT Theme - Default					***/
/*** 										***/
[data-bs-theme=light] {
<#if !lightColors?starts_with('DS Value')>${lightColors}</#if>
}

/*** 										***/
/*** DARK Theme 							***/
/*** 										***/
[data-bs-theme=dark] {
<#if !darkColors?starts_with('DS Value')>${darkColors}</#if>
}
</style>
<#-- End Update ME -->