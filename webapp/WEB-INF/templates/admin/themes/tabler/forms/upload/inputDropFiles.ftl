<#-- Macro: cInputDropFiles

Description: Defines a macro that show a checkbox

Parameters:
@param - name - string - required - the name of of the element
@param - handler - object - optional - Handler used to manage files, default {}
@param - type - string - optional - the type default "dropzone" can be "file" or button. Change the look of the input.
@param - nbFiles - number - optional - Number of files, default 0
@param - nbUplodadedFiles - number - optional - Number of files uploaded by the user, default 0
@param - maxFileSize - number - optional - Max file size that can be uploaded, default 0
@param - unit - string - optional - the unit used to show the file size, default ''
@param - accept - string - optional - Mimit the extensions the user can upload, default ''
@param - label - string - optional - label associated to the upload input default '#i18n{themeparisfr.labelUploadFiles}' 
@param - showLabel - number - optional - Show label if equal to one, otherwise hide it , default 1
@param - labelPos - number - optional - Set label before input if equal to one, otherwise set after input , default 1
@param - labelSelect - string - optional - label associated to the upload input default '#i18n{themeparisfr.labelSelect}' - deprecated
@param - labelSubmit - string - optional - label associated to the upload input default '#i18n{themeparisfr.labelSubmit}' - deprecated
@param - formSubmitButtonName - string - optional - name of the submit "button" of the parent form, used in js to prevent validation of mandatory fields, default 'action_doSaveStep' 
@param - labelDelete - string - optional - label of the label button default '#i18n{themeparisfr.labelDelete}' - deprecated
@param - required - boolean - optional - Set element as required, default false
@param - disabled - boolean - optional - Disable element, default false
@param - multiple - boolean - optional - Set multiple attribute to select default false
@param - noJs - boolean - optional - Add nojs class to remove all button, default false
@param - hasFiles - boolean - optional - If true show the files, default false
@param - helpMsg - string - optional - Content of the help message for upload, default ''
@param - errorMsg - string - optional - Content of the error message for upload, default ''
@param - id - string - optional - the ID of the element, default ''
@param - class - string - optional - the CSS class of the element, default '' 
@param - params - optional - additional HTML attributes to include in the ckeckbox element default ''
-->
<#macro inputDropFiles name handler={} type='dropzone' image=false nbFiles=0 nbUplodadedFiles=0 maxFileSize=0 unit='' accept='' label='#i18n{themeparisfr.labelUploadFiles}' showLabel=1 labelPos=1 labelSelect='#i18n{themeparisfr.labelSelect}' labelSubmit='#i18n{themeparisfr.labelSubmit}' formSubmitButtonName='action_doSaveStep' labelDelete='#i18n{themeparisfr.labelDelete}' required=false disabled=false multiple=true noJs=false helpMsg='' hasFiles=false errorMsg='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local cId><#if id!=''>${id!}<#else>${name!}</#if></#local>
<#local nbFiles = nbFiles?number >
<#local maxFileSize = maxFileSize?number >
<#local isDisabled><#if nbFiles?number = nbUplodadedFiles?number >true<#elseif disabled>true<#else>false</#if></#local>
<#if handler?has_content>
<#local deleteBtnName='_form_upload_delete_${cId}' >
<@cInput type='hidden' name='asynchronousupload.handler' value=handler.handlerName />
<div class="row group-files ${type} <#if image> image-file</#if><#if !multiple> one-file</#if><#if nbUplodadedFiles?number=nbFiles?number> no-file</#if><#if errorMsg!=''> is-invalid</#if>">
	<div class="col px-0">
        <#if labelPos == 1>
        <label id="lb${cId!}" class="<#if showLabel=0>visually-hidden visually-hidden-focusable</#if><#if errorMsg !=''> main-danger-color</#if>" for="${cId!}">${label}<#if required>&nbsp;<span class="main-danger-color" tabindex="0" title="#i18n{themeparisfr.labelMandatory}">*</span></#if></label>
        </#if>
        <#if helpMsg !=''><@cFormHelp idMsg helpMsg /></#if>	
        <#if type="dropzone">
            <div id="group-${cId!}" class="d-flex align-items-center file-input<#if errorMsg!=''> is-invalid</#if>">
                <input type="file" class="form-control my-xs ${cssClass!}<#if required> is-required</#if><#if handler?has_content> ${handler.handlerName}</#if><#if class!=''> ${class}</#if>"<#if required>aria-required="true"</#if> data-nbuploadedfiles="${nbUplodadedFiles}" name="${name}" id="${cId!}" <#if multiple>multiple="multiple"</#if><#if nbFiles gt 0> data-nof="${nbFiles}"</#if><#if maxFileSize gt 0> data-mfs="${maxFileSize}"</#if><#if accept !=''> accept="${accept}" data-atf="${accept}"</#if>>
                <svg  xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                    <path d="M4 17v2a2 2 0 0 0 2 2h12a2 2 0 0 0 2 -2v-2" /><path d="M7 9l5 -5l5 5" /><path d="M12 4l0 12" />
                </svg>
                <p class="flex-1 text-left ms-sm mb-0">#i18n{themeparisfr.labelDropFiles} <span class="main-info-color text-underline">#i18n{asynchronousupload.action.browse.name}</span></p>
            </div>
            <#if errorMsg !=''><@cFormError idMsg errorMsg /></#if>
        <#elseif type="button">
            <div id="group-${cId!}" class="">
                <input type="file" <#if required>aria-required="true"</#if><#if helpMsg!=''> aria-labelledby="${cId}Help"</#if> name="${name}" id="${cId}" class="form-control<#if isDisabled?boolean> disabled</#if><#if required> is-required</#if><#if handler?has_content> ${handler.handlerName}</#if><#if class!=''> ${class}</#if>"<#if multiple> multiple</#if><#if nbFiles gt 0> data-nof="${nbFiles}"</#if><#if maxFileSize gt 0> data-mfs="${maxFileSize}"</#if><#if accept !=''> accept="${accept}" data-atf="${accept}"</#if>>
                <button hidden class="btn btn-link-primary" name="<#if handler.uploadSubmitPrefix?has_content>${handler.uploadSubmitPrefix}<#else>handler_</#if>${cId}" id="<#if handler.uploadSubmitPrefix?has_content>${handler.uploadSubmitPrefix}<#else>handler_</#if>${cId}" value="<#if handler.uploadSubmitPrefix?has_content>${handler.uploadSubmitPrefix}<#else>handler_</#if>${cId}" type="submit" >
                    <span class="file-input-text-noscript">#i18n{asynchronousupload.action.send.name}</span>
                    <span class="file-input-text-js" style="display:none;">#i18n{asynchronousupload.action.browse.name}</span>
                </button>
            </div>	
            <#if errorMsg !=''><@cFormError idMsg errorMsg /></#if>
        </#if>	
        <#if labelPos != 1>
        <label id="lb${cId!}" class="<#if showLabel=0>visually-hidden visually-hidden-focusable<#else>mt-m</#if><#if errorMsg !=''> main-danger-color</#if>" for="${cId!}">${label}</label>
        </#if>
    <#if !multiple>
    </div>
    <div class="col">
    </#if>
        <div id="_file_error_box_${cId}"></div>
        <div id="progress_${cId}" class="progress" style="display:none;">
            <div id="progress-bar_${cId}" class="progress-bar progress-bar-success progress-bar-striped"></div>
        </div>
        <div class="form-files-group" id="_file_deletion_label_${cId!}"<#if !hasFiles> style="display:none;"</#if>>
            <ul id="_file_deletion_${cId}" class="files-group">
            <#nested>
            </ul>
        </div>
    </div>
</div>
</#if>
</#macro>