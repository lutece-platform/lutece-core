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
<@deprecatedWarning args=deprecated />
<div class="card m-2 box-widget<#if bgColor!=''> ${bgColor}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<div class="card-body">
		<span class="info-box-icon<#if color!=''> ${color}</#if>"><@icon style=boxIcon /></span><#if boxText !=''><span class="info-box-text ml-2">${boxText}</span></#if>
		<div class="info-box-content">
			<#if boxNumber!='0'><span class="info-box-number">${boxNumber?trim}<#if unit!=''> <small>${unit}</small></#if></span></#if>
			<#if bgColor!='' && progressBar!=''><div class="progress"><div class="progress-bar" style="width: ${(boxNumber?trim?number/progressBar?trim?number*100)?string.computer}%"></div></div></#if>
			<#if progressDescription!=''><span class="progress-description">${progressDescription}</span></#if>
			<#nested />
		</div>
	</div>
</div>
</#macro>