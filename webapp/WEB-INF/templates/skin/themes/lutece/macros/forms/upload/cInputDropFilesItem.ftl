<#--
Macro: cInputDropFilesItem

Description: Generates a single file item in the uploaded files list, with download link, file size display, and a delete button.

Parameters:
- name (string, required): the name of the file input element.
- label (string, required): the file name to display.
- idx (string, required): the index of the file in the upload list.
- handler (object, required): handler object used to manage file operations.
- fileSize (number, optional): file size in bytes. Default: 0.
- ext (string, optional): the file extension. Default: ''.
- unit (string, optional): the unit used to display file size ('o', 'Ko', 'Mo'). Default: ''.
- urlDl (string, optional): URL to download the file. Default: ''.
- maxChars (number, optional): maximum characters shown in the file name before truncation. Default: 60.
- urlRm (string, optional): URL to remove the file. Default: ''.
- class (string, optional): CSS class for the file item. Default: ''.

Snippet:

    File item in upload list:

    <@cInputDropFiles name='documents' handler=handler hasFiles=true>
        <@cInputDropFilesItem name='documents' label='report-2026.pdf' idx='0' handler=handler fileSize=245760 />
        <@cInputDropFilesItem name='documents' label='photo.jpg' idx='1' handler=handler fileSize=1048576 />
    </@cInputDropFiles>

-->
<#macro cInputDropFilesItem name label idx handler fileSize=0 ext='' unit='' maxChars=60 urlDl='' urlRm='' class='' deprecated...>
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
    <label class="files-item-label" for="${cId}">
        <input type="checkbox" name="${cName}" id="${cId}" tabindex="-1" aria-hidden="true">
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