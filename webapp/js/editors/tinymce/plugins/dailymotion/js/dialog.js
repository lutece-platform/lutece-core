tinyMCEPopup.requireLangPack();

var DailyMotionDialog = {
	init : function() {
	},

	insert : function() {
		// Insert the contents from the input into the document
		var embedCode = '<object width="'+document.forms[0].dailymotionWidth.value+'" height="'+document.forms[0].dailymotionHeight.value+'"><param name="movie" value="http://www.dailymotion.com/swf/'+document.forms[0].dailymotionID.value+'&v3=1&related=1"></param><param name="wmode" value="transparent"></param><param name="allowFullScreen" value="true"></param><param name="allowScriptAccess" value="always"></param><embed src="http://www.dailymotion.com/swf/'+document.forms[0].dailymotionID.value+'&v3=1&related=1" type="application/x-shockwave-flash" wmode="transparent" allowFullScreen="true" allowScriptAccess="always" width="'+document.forms[0].dailymotionWidth.value+'" height="'+document.forms[0].dailymotionHeight.value+'"></embed></object>';
		tinyMCEPopup.editor.execCommand('mceInsertRawHTML', false, embedCode);
		tinyMCEPopup.close();
	}
};

tinyMCEPopup.onInit.add(DailyMotionDialog.init, DailyMotionDialog);

/*** MODEL DAILYMOTION
<object width="420" height="365">
<param name="movie" value="http://www.dailymotion.com/swf/x4qc2h&v3=1&related=1"></param>
<param name="allowFullScreen" value="true"></param>
<param name="allowScriptAccess" value="always"></param>
<embed src="http://www.dailymotion.com/swf/x4qc2h&v3=1&related=1" type="application/x-shockwave-flash" width="420" height="365" allowFullScreen="true" allowScriptAccess="always"></embed>
</object>
***/