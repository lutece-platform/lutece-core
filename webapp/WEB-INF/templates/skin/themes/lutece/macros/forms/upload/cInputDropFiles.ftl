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
@param - label - string - optional - label associated to the upload input default '#i18n{theme.labelUploadFiles}' 
@param - showLabel - number - optional - Show label if equal to one, otherwise hide it , default 1
@param - labelPos - number - optional - Set label before input if equal to one, otherwise set after input , default 1
@param - labelSelect - string - optional - label associated to the upload input default '#i18n{theme.labelSelect}' - deprecated
@param - labelSubmit - string - optional - label associated to the upload input default '#i18n{theme.labelSubmit}' - deprecated
@param - formSubmitButtonName - string - optional - name of the submit "button" of the parent form, used in js to prevent validation of mandatory fields, default 'action_doSaveStep' 
@param - labelDelete - string - optional - label of the label button default '#i18n{theme.labelDelete}' - deprecated
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
<#macro cInputDropFiles name handler={} type='dropzone' nbFiles=0 nbUplodadedFiles=0 maxFileSize=0 unit='' accept='' label='#i18n{theme.labelUploadFiles}' showLabel=1 labelPos=1 labelSelect='#i18n{theme.labelSelect}' labelSubmit='#i18n{theme.labelSubmit}' formSubmitButtonName='action_doSaveStep' labelDelete='#i18n{theme.labelDelete}' required=false disabled=false multiple=true noJs=false helpMsg='' hasFiles=false errorMsg='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local cId><#if id!=''>${id!}<#else>${name!}</#if></#local>
<#local nbFiles = nbFiles?number >
<#local maxFileSize = maxFileSize?number >
<#local isDisabled><#if nbFiles?number = nbUplodadedFiles?number >true<#elseif disabled>true<#else>false</#if></#local>
<#if handler?has_content>
<#local deleteBtnName='_form_upload_delete_${cId}' >
<@cInput type='hidden' name='asynchronousupload.handler' value=handler.handlerName />
<div class="row group-files ${type} <#if !multiple> one-file</#if><#if nbUplodadedFiles?number=nbFiles?number> no-file</#if>">
	<div class="col">
        <#if labelPos == 1>
        <label id="lb${cId!}" class="<#if showLabel=0>visually-hidden visually-hidden-focusable</#if><#if errorMsg !=''> main-danger-color</#if>" for="${cId!}">${label}<#if required>&nbsp;<span class="main-danger-color" tabindex="0" title="#i18n{theme.labelMandatory}">*</span></#if></label>
        </#if>
        <#if helpMsg !=''><@cFormHelp idMsg helpMsg /></#if>	
        <#if type="dropzone">
            <div id="group-${cId!}" class="d-flex align-items-center file-input<#if errorMsg!=''> is-invalid</#if>">
                <input type="file" class="form-control my-xs ${cssClass!}<#if isDisabled?boolean> disabled</#if><#if required> is-required</#if><#if handler?has_content> ${handler.handlerName}</#if><#if class!=''> ${class}</#if>"<#if required>aria-required="true"</#if> data-nbuploadedfiles="${nbUplodadedFiles}" name="${name}" id="${cId!}" <#if multiple>multiple="multiple"</#if><#if nbFiles gt 0> data-nof="${nbFiles}"</#if><#if maxFileSize gt 0> data-mfs="${maxFileSize}"</#if><#if accept !=''> accept="${accept}" data-atf="${accept}"</#if>>
                 <svg class="paris-icon paris-icon-upload white-color" role="img" aria-hidden="true" focusable="false">
                    <use xlink:href="#paris-icon-upload"></use>
                </svg>
                <p class="flex-1 text-left ms-sm mb-0">#i18n{theme.labelDropFiles} <span class="text-primary text-underline">#i18n{asynchronousupload.action.browse.name}</span></p>
            </div>
            <#if errorMsg !=''><@cFormError idMsg errorMsg /></#if>
        <#elseif type="button">
            <div id="group-${cId!}" class="<#if errorMsg!=''> is-invalid</#if>">
                <#--  <label id="lb${cId!}" class="form-label" for="${cId}" data-browse="#i18n{asynchronousupload.action.browse.name}">#i18n{asynchronousupload.action.browse.name}</label>  -->
                <input type="file" <#if required>aria-required="true" required</#if><#if helpMsg!=''> aria-labelledby="${cId}Help"</#if> name="${name}" id="${cId}" class="form-control<#if isDisabled?boolean> disabled</#if><#if required> is-required</#if><#if handler?has_content> ${handler.handlerName}</#if><#if class!=''> ${class}</#if>"<#if multiple> multiple</#if><#if nbFiles gt 0> data-nof="${nbFiles}"</#if><#if maxFileSize gt 0> data-mfs="${maxFileSize}"</#if><#if accept !=''> accept="${accept}" data-atf="${accept}"</#if>>
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
<script>
window.addEventListener( 'DOMContentLoaded', (event) => {
    const fieldName = $('#${cId!}');
    const groupFieldName = $('#group-${cId!}');
    const labelFieldName = $('#lb${cId!}');
    let hasChecked = false, countFiles=${nbUplodadedFiles!};
    mapFilesNumber.set( "${cId!}", ${nbFiles!} );
    <#if errorMsg!=''>mapFileErrors.set( "${cId!}", "${errorMsg!}" );</#if>
    <#-- Add FileUpload option to control number of files max to upload -->
    <#if nbFiles?number &gt; 0>fieldName.fileupload( { dropZone: fieldName<#if accept !=''>, acceptFileTypes: /(\.|\/)(${accept?replace('.','')?replace(',','|')})$/i</#if>});</#if>
	const isInputFileRequired = fieldName.hasClass('is-required');	
    
	if( isInputFileRequired ){
		if( countFiles > 0 ){ fieldName.attr('data-nbuploadedfiles', countFiles ); }
		const validateButton = document.querySelector('#${formSubmitButtonName!}');
		validateButton.addEventListener('click', (e) => {
            const fileItemsList = $('#_file_deletion_${cId} .files-item');
			const fileUploaded = fileItemsList.length;
            /* TODO : Rewrite as Vanilla JS  */
            $('.form-control:user-invalid').addClass('is-invalid');
            $('.form-control:invalid').addClass('is-invalid');
            const hasErrors = $(".is-invalid").length;
			if( fileUploaded == 0 ){
				groupFieldName.addClass('is-invalid');
                labelFieldName.addClass('main-danger-color');
				if( groupFieldName.next().hasClass('invalid-feedback') ){
					groupFieldName.next().text('Le champs est obligatoire')
				} else {
					groupFieldName.after('<div class="invalid-feedback" role="status">Le champs est obligatoire</div>')
				}
                if( hasErrors == 0 ){
                    e.preventDefault();
                    $('html, body').scrollTop( groupFieldName.offset().top - 50 );
                }
            } else {
                groupFieldName.removeClass('is-invalid');
                labelFieldName.removeClass('main-danger-color');
            }
		})
       
	} 
});
</script>
<#else>
<div class="custom-file">
    <input type="file" <#if required>aria-required="true"</#if><#if helpMsg!=''> aria-labelledby="${name}Help"</#if> name="${name}" id="${cId}" class="custom-file-input ${cssClass!}<#if class!=''> ${class}</#if>"<#if multiple> multiple</#if>>
    <label class="custom-file-label" for="${name}" data-browse="#i18n{asynchronousupload.action.browse.name}"></label>
    <#if helpMsg!=''><small id="${name}Help" class="form-text text-muted">${helpMsg}</small></#if>	
</div>	
<script src="js/plugins/forms/bs-custom-file-input.min.js"></script>
<script>
window.addEventListener('DOMContentLoaded', (event) => {
    bsCustomFileInput.init()
});
</script>
</#if>
</#macro>