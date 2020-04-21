<div class="row">
	<div class="col-xs-12 col-sm-12 col-md-12">

		<#if message.type = 0 >
			<#assign messageClass = 'info' />
			<#assign messageIcon = 'info-circle' />
		<#elseif message.type = 1 > 
			<#assign messageClass = 'info' />
			<#assign messageIcon = 'question-circle' />
		<#elseif message.type = 2 > 
			<#assign messageClass = 'danger' />
			<#assign messageIcon = 'times-circle' />
		<#elseif message.type = 3 > 
			<#assign messageClass = 'warning' />
			<#assign messageIcon = 'exclamation-circle' />
		<#elseif message.type = 4 > 
			<#assign messageClass = 'success' />
			<#assign messageIcon = 'check-circle' />
		<#elseif message.type = 5 > 
			<#assign messageClass = 'danger' />
			<#assign messageIcon = 'stop-circle' />
		<#else> 
			<#assign messageClass = 'info' />
			<#assign messageIcon = 'question-circle' />
		</#if>
		
		<div class="alert alert-${messageClass}">
			<h1><i class="fa fa-${messageIcon}"></i> ${title}</h1>
			<br>
			${text}
			<br><br>
		</div>
			
		<div class="row">
			<div class="col-xs-12">
			<#if list_parameters?exists>
				<#assign keys = list_parameters?keys>
			</#if>
			<#if url?has_content> 
				<form method="post" action="${url!}" target="${target!}" class="form-inline">							
				<#if list_parameters?exists>
				<#list keys as key>
				<input type="hidden" name="${key}" value="${list_parameters[key]}" />
				</#list>
				</#if>														
				<button type="submit"class="btn btn-lg btn-info"><i class="glyphicon glyphicon-ok"></i>&nbsp;#i18n{portal.util.labelValidate}</button>
				</form>	
			</#if>
			<#if back_url?has_content>
				<form method="post" action="${back_url}" class="form-inline" >
				<#if list_parameters?exists>
				<#list keys as key>
				<input type="hidden" name="${key}" value="${list_parameters[key]}" />
				</#list>	
				</#if>
				<#if cancel_button = 1> 
					<button type="submit" name="cancel" class="btn btn-lg btn-info"><i class="fa fa-times-circle"></i>&nbsp;#i18n{portal.util.labelBack}</button>
				<#elseif cancel_button = 2>
					<button type="submit" name="cancel" class="btn btn-lg btn-info"><i class="fa fa-times-circle"></i>&nbsp;#i18n{portal.util.labelCancel}</button>
				</#if>
				</form>		
			<#else>		
				<#if cancel_button = 1> 
					<button type="button" class="btn btn-lg btn-info pagination-centered" onclick="javascript:window.history.back();"><i class="fa fa-times-circle"></i>&nbsp;#i18n{portal.util.labelBack}</button>
				<#elseif cancel_button = 2>
					<button type="button" class="btn btn-lg btn-info pagination-centered" onclick="javascript:window.history.back();"><i class="fa fa-times-circle"></i>&nbsp;#i18n{portal.util.labelCancel}</button>
				</#if>
			</#if>
			</div>
		</div>
	</div>
</div>
