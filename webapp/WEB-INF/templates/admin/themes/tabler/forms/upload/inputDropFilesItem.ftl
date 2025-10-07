<#-- Macro: cInputDropFilesItem

Description: Defines a macro that show a checkbox

Parameters:
@param - name - string - required - the name of of the element
@param - label - string - required - the label associated to the input
@param - idx - string - required - the index of the file 
@param - handler - object - required - Handler used to manage files
@param - fileSize - number - optional - File size in octet, default 0
@param - ext - string - optional - the extension of the file, default ''
@param - unit - string - optional - the unit used to show the file size, default ''
@param - urlDl - string - optional - The url to download the file - default ''
@param - maxChars - string - optional - the max numbers of chars to show in the file name, default 24
@param - urlRm - string - optional - Url to remove the file default ''
@param - class - string - optional - the CSS class of the element, default '' 
-->
<#macro inputDropFilesItem name label idx handler image=false fileSize=0 ext='' unit='' maxChars=60 urlDl='' urlRm='' class='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local fileLabel><#if label?length gt maxChars>${label[0..maxChars]}...<#else>${label}</#if></#local>
<#if handler?has_content>
    <#local handlerName>${handler.handlerName}</#local>
    <#local cUrlDl>jsp/site/plugins/asynchronousupload/DoDownloadFile.jsp?fieldname=${name}&field_index=${idx}&fileName=${label}&asynchronousupload.handler=${handler.handlerName}</#local>
    <#local cUrlRm></#local>
    <#local cName>${handler.uploadCheckboxPrefix}${name}${idx}</#local>
    <#local cId>${handler.uploadCheckboxPrefix}${name}${idx}</#local>
<#else>
    <#local cUrlDl>${urlDl}</#local>
    <#local cUrlRm>${urlRm!}</#local>
    <#local cName>_form_upload_checkbox_${name}${idx}</#local>
    <#local cId>_form_upload_checkbox_${name}${idx}</#local>
</#if>
<#if fileSize??>
<#switch unit>
    <#case "Mo">
        <#assign octetUnit="Mo">
        <#assign octetNumber=fileSize/(1024*1024)>
        <#break>
    <#case "Ko">
        <#assign octetUnit="Ko">
        <#assign octetNumber=fileSize/1024>
        <#break>
    <#case "o">
        <#assign octetUnit="o">
        <#assign octetNumber=fileSize>
        <#break>
    <#default>
    <#if fileSize < 1024 >
        <#assign octetUnit="o">
        <#assign octetNumber=fileSize>
    <#elseif fileSize < 1024 * 1024 >
        <#assign octetUnit="Ko">
        <#assign octetNumber=fileSize/1024>
    <#else>
        <#assign octetUnit="Mo">
        <#assign octetNumber=fileSize/(1024*1024)>
    </#if>
</#switch>
</#if>
<#if ext = ''><#local ext=name?keep_after_last('.') /></#if>
<li class="files-item<#if class!=''> ${class}</#if>" id="_file_uploaded_${name}${idx}">
    <label class="files-item-label<#if image=true> image</#if>" for="${cId}">
        <#if image=true><img src="themes/shared/images/none.svg" alt="" width="80" class="img-fluid img-thumbnail"></#if>
        <a href="${cUrlDl}" class="files-item-link" title="#i18n{portal.util.labelDownload} ${label}" data-type="${ext!}" data-img="">
            <span class="file-item-label">${fileLabel}</span>
            <span class="file-item-info"><#if fileSize?has_content>${octetNumber?string["0"]} ${octetUnit}</#if></span>
        </a>
    </label>
    <button type="button" class="btn btn-link main-color deleteSingleFile p-0"<#if cUrlRm !=''> data-url="${cUrlRm!}"</#if> data-item="#_file_uploaded_${name}${idx}" fieldName="${name}" handlerName="${handlerName!}" index="${idx}" title="#i18n{portal.util.labelDelete} ${fileLabel}"> 
        <svg class="paris-icon paris-icon-close" role="img" aria-hidden="true" focusable="false">
            <use xlink:href="#paris-icon-close"></use>
        </svg>
    </button>
</li>
</#macro>
