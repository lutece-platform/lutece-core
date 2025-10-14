function getTemplateUploadedFile( fieldName, index, checkboxPrefix, jsonData, imgTag, handler, baseUrl, deleteLabel, unit='' ) {
	let strCode='', sizeDisplay='', sizeTemp='', octetUnit='', octetNumber='', fileName='', fileDisplayName='', mimeType='', mimeTypeDisplay='';

	if ( (typeof jsonData.files[index] != 'undefined' && jsonData.files[index].size != 'undefined' ) || (jsonData.files.size != 'undefined' ) ) {

		sizeTemp = (jsonData.fileCount == 1) ? jsonData.files.size : jsonData.files[index].size;
		fileName = (jsonData.fileCount == 1) ? jsonData.files.name : jsonData.files[index].name;
		mimeType = (jsonData.fileCount == 1) ? jsonData.files.preview : jsonData.files[index].preview;
		
		fileDisplayName=fileName;

		mimeTypeDisplay = mimeType !='' ? mimeType.match(/[^:/]\w+(?=;|,)/)[0] : fileName.substr( ( fileName.lastIndexOf(".") + 1 ), ( fileName.length - fileName.lastIndexOf(".") ) );
		switch ( unit ) {
			case 'Mo':
				octetUnit = 'Mo';
				octetNumber = sizeTemp/(1024*1024);
			  	break;
			case 'Ko':
				octetUnit = 'Ko';
				octetNumber = sizeTemp/1024;
				break;
			case 'o':
				octetUnit = "o";
				octetNumber = sizeTemp;
			  break;
			default:
			  if ( sizeTemp < 1024 ) {
				  octetUnit = "o";
				  octetNumber = sizeTemp;
			  }
			  else if (sizeTemp < 1024 * 1024) {
				  octetUnit = "Ko";
				  octetNumber = sizeTemp/1024;
			  }
			  else {
				  octetUnit = "Mo";
				  octetNumber = sizeTemp/(1024*1024);
			  }
		}
		// sizeDisplay = "" + Math.floor(octetNumber) + " " + octetUnit;
		sizeDisplay = "" + Number.parseFloat(octetNumber).toFixed(2) + " " + octetUnit;
		
   }

   if( fileDisplayName.length > 60 ){
	   fileDisplayName = fileName.substr(0,55) + '...';
   }

   strCode = `<li class="files-item" id="_file_uploaded_${fieldName}${index}">
   <label class="files-item-label" for="${checkboxPrefix}${index}">
	   <input type="checkbox" name="${checkboxPrefix}${index}" id="${checkboxPrefix}${index}"> 
	   <a class="files-item-link" title="${fileName}" href="jsp/site/plugins/asynchronousupload/DoDownloadFile.jsp?fieldname=${fieldName}&field_index=${index}&fileName=${fileName}&asynchronousupload.handler=${handler}" data-type="${mimeTypeDisplay}" data-img="">
		   <span class="file-item-label">${fileDisplayName}</span>
		   <span class="file-item-info">${sizeDisplay}</span>
	   </a>
   </label>
   <button type="button" class="btn btn-link deleteSingleFile main-color p-0" data-item="#_file_uploaded_${fieldName}${index}" fieldname="${fieldName}" handlername="${handler}" index="${index}" title="${deleteLabel} ${fileDisplayName}" aria-label="${deleteLabel} ${fileDisplayName}"> 
	   <svg class="paris-icon paris-icon-close" role="img" aria-labelledby="paris-icon-title-group" focusable="false">
		   <title id="paris-icon-title-group"></title>
		   <use xlink:href="#paris-icon-close"></use>
	   </svg> 
   </button>
</li>`

   return strCode;
}

function prettySize( bytes, separator=' ', postFix=''){
if (bytes) {
	const sizes = ['Octets', 'Ko', 'Mo', 'Go', 'To'];
	const i = Math.min(parseInt(Math.floor(Math.log(bytes) / Math.log(1024)).toString(), 10), sizes.length - 1);
	return `${(bytes / (1024 ** i)).toFixed(i ? 1 : 0)}${separator}${sizes[i]}${postFix}`;
}
return 'n/a';
}