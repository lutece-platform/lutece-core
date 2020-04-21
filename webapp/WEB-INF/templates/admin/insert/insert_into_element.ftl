<!--  Insert service -->
<script type="text/javascript">
$( function(){
	var isIt = '${.data_model.input}', insertCode='${insert}';
	switch ( isIt ) {
		case 'slider-file':
			sessionStorage.setItem('slidercode', insertCode );
			window.parent.closeModal( );
			break;
		case 'photo-file':
			var photoUrl = insertCode.match(/("|')(document)((?:\\\1|(?:(?!\1).))*)\1/g);
			sessionStorage.setItem('photourl', photoUrl );
			window.parent.closeModal( );
			break;
		case 'undefined':
			parent.tinymce.activeEditor.insertContent( insertCode );
			parent.tinymce.activeEditor.windowManager.close();
			break;
		case 'null':
			parent.tinymce.activeEditor.insertContent( insertCode );
			parent.tinymce.activeEditor.windowManager.close();
			break;
		case 'tinymce':
			parent.tinymce.activeEditor.insertContent( insertCode );
			parent.tinymce.activeEditor.windowManager.close();
			break;
		default:
			var elementInput = opener.document.getElementById("${.data_model.input}");
			elementInput.value = insertCode;
			window.close();
	}
});
</script>
