<#--
Macro: page
Description: Contenu principe
-->
<#macro page template='' header=true>
<!-- Begin page content -->
<main role="main" class="lutece-page ${template}" header=true>
<#if header><@pageHeader /></#if>
<#nested>
</main>
</#macro>