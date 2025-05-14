<#--
Macro: input
Description: Generates an input element for various types of form fields, including text, textarea, password, email, file, number, color, range, tel, and datalist. It also supports various attributes such as size, maxlength, placeholder, disabled, readonly, pattern, title, min, max, and mandatory. Additionally, it can generate date and time pickers using the `getDate`, `getDateTime`, `getTime`, `getDateRange`, and `getDateTimeRange` macros.
Parameters:
- name (string): the name of the input field.
- id (string, optional): the ID of the input field.
- type (string, optional): the type of the input field. Default is `text`.
- value (string, optional): the default value of the input field.
- class (string, optional): the CSS class of the input field.
- size (string, optional): the size of the input field.
- inputSize (int, optional): the size of the input field for some input types.
- maxlength (int, optional): the maximum length of the input field.
- placeHolder (string, optional): the placeholder text of the input field.
- rows (int, optional): the number of rows for a textarea input.
- cols (int, optional): the number of columns for a textarea input.
- richtext (boolean, optional): whether to use a rich text editor for a textarea input.
- tabIndex (int, optional): the tab index of the input field.
- disabled (boolean, optional): whether the input field is disabled.
- readonly (boolean, optional): whether the input field is readonly.
- pattern (string, optional): the regex pattern for the input field.
- title (string, optional): the title text of the input field.
- min (int, optional): the minimum value of the input field.
- max (int, optional): the maximum value of the input field.
- params (string, optional): additional parameters for the input field.
- mandatory (boolean, optional): whether the input field is required.
- language (string, optional): the language code for date and time pickers.
- minDate (string, optional): the minimum date for date and datetime pickers.
- maxDate (string, optional): the maximum date for date and datetime pickers.
- defaultDate (string, optional): the default date for date and datetime pickers.
- defaultTime (string, optional): the default time for datetime pickers.
- time_24hr (boolean, optional): whether to use a 24-hour clock for time pickers.
- minTime (string, optional): the minimum time for time and datetime pickers.
- maxTime (string, optional): the maximum time for time and datetime pickers.
- format (string, optional): the format for date and time pickers.
- showFormat (string, optional): the display format for date and time pickers.
- dateRangeEndId (string, optional): the ID of the end date field for date range and datetime range pickers.
- dateParams (list, optional): additional parameters for date and time pickers.
- showFileUrl (boolean, optional): whether to show a link to the uploaded file for file inputs.
- fileURL (string, optional): the URL of the uploaded file for file inputs.
- fileName (string, optional): the name of the uploaded file for file inputs.
- datalist (string, optional): the comma-separated list of options for a datalist input.
- accept (string, optional): the comma-separated list of options for a datalist input.
-->
<#macro input name id='' type='text' value='' class='' size='' inputSize=0 maxlength=0 placeHolder='' autoComplete='' rows=4 cols=40 richtext=false tabIndex='' disabled=false readonly=false pattern='' title='' min=0 max=0 step=0 mandatory=false language=.locale minDate='' maxDate='' defaultDate='' defaultTime='' time_24hr=true minTime='' maxTime='' format='' showFormat='' dateRangeEndId='' dateParams=[] showFileUrl=false showImage=false fileURL='' fileName='' datalist='' accept='' params='' patternValidationRules=[] deprecated...>
<@deprecatedWarning args=deprecated />
<#if propagateMandatory?? && propagateMandatory ><#local mandatory = true /></#if>
<#if type='textarea'>
	<textarea name="${name}" class="form-control<#if size!=''> input-${size}</#if><#if class!=''> ${class}</#if><#if richtext> richtext</#if>" rows="${rows}" cols="${cols}"<#if tabIndex!=''> tabindex="${tabIndex}"</#if><#if placeHolder!=''> placeholder="${placeHolder}"</#if><#if title!=''> title="${title}"</#if><#if disabled> disabled</#if><#if readonly> readonly</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if><#if pattern!=''>pattern=${pattern}</#if><#if maxlength &gt; 0> maxlength="${maxlength}"</#if><#if (mandatory && !richtext)> required</#if><#if labelFor?? && labelFor!='' && helpKey?? && helpKey!=''> aria-describedby="${labelFor}_help"</#if>><#if value!='' >${value}<#else><#nested></#if></textarea>
<#elseif type='text' || type='search' || type='password' || type='email' || type='file' || type='number' || type='color' || type='url'  || type='range' || type='tel' || type='url' || type='datalist' || type='dateHTML5'>
	<input class="form-control<#if size!=''> input-${size}</#if><#if type='color'> input-color</#if><#if class!=''> ${class}</#if>" type="<#if type='dateHTML5'>date<#else>${type}</#if>" name="${name}" value="${value}"<#if autoComplete !=''> autocomplete="${autoComplete}"</#if><#if tabIndex!=''> tabindex="${tabIndex}"</#if><#if placeHolder!=''> placeholder="${placeHolder}"</#if><#if title!=''> title="${title}"</#if><#if maxlength &gt; 0> maxlength="${maxlength}"</#if><#if inputSize!=0> size="${inputSize}"</#if><#if disabled> disabled</#if><#if readonly> readonly</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if><#if pattern!=''>pattern=${pattern}</#if><#if accept!='' && type='file'>accept=${accept}</#if><#if min!=max> min="${min}"</#if><#if max!=0> max="${max}"</#if><#if mandatory> required </#if><#if labelFor?? && labelFor!='' && helpkey?? && helpKey!=''> aria-describedby="${labelFor}_help"</#if><#if type='datalist'> list="${name}_list"</#if>>
	<#if type='file'>
		<input type="hidden" id=${id}Key name="${name}Key" value="${value}" />
		<#if showFileUrl && fileURL?? && fileName??>
			<#if showImage><img src="${fileURL}" alt="${fileName}" class="img-thumbnail thumbnail thumbnail-sm mt-2" ></#if>
			<@link href="${fileURL}">${fileName}</@link>
		</#if>
	</#if>
	<#if type='datalist'>
		<#if id !='' && datalist !='' >
			<datalist id="${name}_list">
			<#assign options = datalist?split(',')>
			<#list options as opt><option value="${opt!}"></#list>
			</datalist>
		<#else>
			<!-- Missing id or params attribute for macro @input for type "datalist" -->
		</#if>
	</#if>
<#elseif type='date' || type='datetime' || type='daterange' || type='datetimerange' || type='time'>
	<input class="form-control<#if size!=''> input-${size}</#if><#if class!=''> ${class}</#if>" type="text" name="${name}" value="${value}"<#if tabIndex!=''> tabindex="${tabIndex}"</#if><#if placeHolder!=''> placeholder="${placeHolder}"</#if><#if title!=''> title="${title}"</#if><#if maxlength &gt; 0> maxlength="${maxlength}"</#if><#if inputSize!=0> size="${inputSize}"</#if><#if disabled> disabled</#if><#if readonly> readonly</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if><#if pattern!=''>pattern=${pattern}</#if><#if min!=max> min="${min}"</#if><#if max!=0> max="${max}"</#if><#if mandatory> required </#if><#if labelFor?? && labelFor!='' && helpkey?? && helpKey!=''> aria-describedby="${labelFor}_help"</#if> />
	<#if type='date'>
		<#if value?has_content>
			<#local defaultDate=value />
		</#if>
		<@getDate idField='${id}' language=language format=format showFormat=showFormat minDate=minDate maxDate=maxDate defaultDate=defaultDate dateOptions=dateParams />
	<#elseif type='datetime'>
		<#if value?has_content>
			<#local defaultDate=value />
		</#if>
		<@getDateTime idField='${id}' language=language format=format showFormat=showFormat minDate=minDate maxDate=maxDate defaultDate=defaultDate defaultTime=defaultTime minTime=minTime maxTime=maxTime dateOptions=dateParams />
	<#elseif type='daterange'>
		<#if dateRangeEndId != ''>
			<#if value?has_content>
				<#local defaultDate=value />
			</#if>
			<@getDateRange idField='${id}' idEndField='${dateRangeEndId}' language=language format=format showFormat=showFormat minDate=minDate maxDate=maxDate defaultDate=defaultDate dateOptions=dateParams />
		<#else>
			<@alert class='danger'>${i18n("portal.util.datepicker.rangeEndId.mandatory")}</@alert>
		</#if>
	<#elseif type='datetimerange'>
		<#if dateRangeEndId != ''>
			<#if value?has_content>
				<#local defaultDate=value />
			</#if>
			<@getDateTimeRange idField='${id}' idEndField='${dateRangeEndId}' language=language format=format showFormat=showFormat minDate=minDate maxDate=maxDate defaultDate=defaultDate defaultTime=defaultTime minTime=minTime maxTime=maxTime dateOptions=dateParams  />
		<#else>
			<@alert class='danger'>${i18n("portal.util.datepicker.rangeEndId.mandatory")}</@alert>
		</#if>
	<#elseif type='time'>
		<#if value?has_content>
			<#local defaultDate=value />
		</#if>
		<@getTime idField='${id}' language=language format='H:i' showFormat='H:i' minTime=minTime maxTime=maxTime time_24hr=time_24hr defaultDate=defaultDate dateOptions=dateParams />
	</#if>
	<#if id=''><@alert class='danger'>${i18n("portal.util.datepicker.id.mandatory")}</@alert></#if>
<#elseif type='html5date' || type='html5datetime' || type='html5time' || type='html5month'>
	<input class="form-control<#if size!=''> input-${size}</#if><#if type='color'> input-color</#if><#if class!=''> ${class}</#if>" type="<#if type='html5date'>date<#elseif type='html5datetime'>datetime-local<#elseif type='html5time'>time<#elseif type='html5month'>month<#else>unsupported date type</#if>" name="${name}" value="${value}"<#if tabIndex!=''> tabindex="${tabIndex}"</#if><#if placeHolder!=''> placeholder="${placeHolder}"</#if><#if title!=''> title="${title}"</#if><#if inputSize!=0> size="${inputSize}"</#if><#if disabled> disabled</#if><#if readonly> readonly</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if><#if min!=max> min="${min}"</#if><#if max!=0> max="${max}"</#if><#if mandatory> required </#if><#if labelFor?? && labelFor!='' && helpkey?? && helpKey!=''> aria-describedby="${labelFor}_help"</#if>>
<#elseif type='hidden'>
	<input type="hidden" name="${name}" <#if id!=''>id="${id}"</#if> value="${value}" />
<#else>
	<@alert class='danger'>${i18n("portal.util.message.unsupportedType")}</@alert>
</#if>
</#macro>