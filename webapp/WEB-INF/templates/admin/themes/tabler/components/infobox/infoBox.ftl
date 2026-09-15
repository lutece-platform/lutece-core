<#-- Macro: infoBox

Description: Generates an information box.

Parameters:
- color (string, optional): the color of the info box.
- boxText (string, optional): the text to display in the info box.
- boxIcon (string, optional): the icon to display in the info box.
- boxNumber (string, optional): the number to display in the info box.
- unit (string, optional): the unit to display after the number.
- bgColor (string, optional): the background color of the info box.
- progressBar (string, optional): the value for the progress bar (0-100).
- progressDescription (string, optional): the text to display below the progress bar.
- id (string, optional): the ID attribute of the info box.
- params (string, optional): additional parameters to add to the info box.
-->
<#macro infoBox color='' boxText='' boxIcon='' boxNumber='' unit='' bgColor='' progressBar='' progressDescription='' id='' params='' deprecated...>
<#local boxNumber = boxNumber?is_markup_output?then(boxNumber?markup_string, boxNumber) />
<#local progressBar = progressBar?is_markup_output?then(progressBar?markup_string, progressBar) />
<@deprecatedWarning args=deprecated />
<div class="card m-2 box-widget<#if bgColor?has_content> ${bgColor}</#if>"<#if id?has_content> id="${id}"</#if><#if params?has_content> ${params}</#if>>
	<div class="card-body">
		<span class="info-box-icon<#if color?has_content> ${color}</#if>"><@icon style=boxIcon /></span><#if boxText?has_content><span class="info-box-text ml-2">${boxText}</span></#if>
		<div class="info-box-content">
			<#if boxNumber!='0'><span class="info-box-number">${boxNumber?trim}<#if unit?has_content> <small>${unit}</small></#if></span></#if>
			<#if bgColor?has_content && progressBar?has_content><div class="progress"><div class="progress-bar" style="width: ${(boxNumber?trim?number/progressBar?trim?number*100)?string.computer}%"></div></div></#if>
			<#if progressDescription?has_content><span class="progress-description">${progressDescription}</span></#if>
			<#nested />
		</div>
	</div>
</div>
</#macro>