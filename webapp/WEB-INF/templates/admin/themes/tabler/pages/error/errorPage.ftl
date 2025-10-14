<#-- 
Macro: errorPage

Description: Generates an error page with a custom color, type, and ID, as well as internationalized error messages and a back button.

Parameters:
- color (string, optional): the color of the error message and button, using a Bootstrap color class (e.g. "danger").
- errorType (string, required): the type of error, such as "404" or "500".
- id (string, optional): the ID of the error page container.
- params (string, optional): additional parameters to be added to the error page container.
-->
<#macro errorPage color='' errorType='' id='' params=''>
<@pageContainer>
<@pageColumn>
<div class="error-page"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<h2 class="headline text-${color}">${errorType}</h2>
	<div class="error-content">
		<h3>
			<@icon style='warning' class='text-${color}' />
			<#if errorType=='404'>
				#i18n{portal.util.error404.title}
			<#elseif errorType='500'>
				#i18n{portal.util.error500.title}
			<#else>...
			</#if>
		</h3>
		<p>
		<#if errorType=='404'>
			#i18n{portal.util.error404.text} 
		<#elseif errorType='500'>
			#i18n{portal.util.error500.text} 
		<#else>...
		</#if>
		</p>
		<@aButton href='' size='' color='bg-${color}' style='btn-flat'>
			<@icon style='home' />
			#i18n{portal.util.labelBackHome}
		</@aButton>
	</div>
</div>
<script>
document.documentElement.classList.remove('loading');
document.documentElement.classList.add('loaded');
</script>
</@pageColumn>
</@pageContainer>
</#macro>