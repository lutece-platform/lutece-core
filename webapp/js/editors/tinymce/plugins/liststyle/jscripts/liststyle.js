tinyMCEPopup.requireLangPack();

function init() {
	tinyMCEPopup.resizeToInnerSize();

	var formObj = document.forms[0];
	var list = tinyMCEPopup.getWindowArg('list');
	var isIE = tinyMCEPopup.getWindowArg('isIE');
	switch (tinyMCEPopup.getWindowArg('listStyleType')) {
		case "decimal":
			formObj.decimalId.checked = true;
			break;
		case "lower-alpha":
			formObj.lowerAlphaId.checked = true;
			break;
		case "upper-alpha":
			formObj.upperAlphaId.checked = true;
			break;
		case "lower-roman":
			formObj.lowerRomanId.checked = true;
			break;
		case "upper-roman":
			formObj.upperRomanId.checked = true;
			break;
		case "disc":
			formObj.discId.checked = true;
			break;
		case "circle":
			formObj.circleId.checked = true;
			break;
		case "square":
			formObj.squareId.checked = true;
			break;
		case "none":
			formObj.noneId.checked = true;
			break;
		default:
			if (list == "ol") {
				formObj.decimalId.checked = true;
			}
			else {
				formObj.discId.checked = true;
			}
	}
	//alert("list = " + list);
	if (list == "ol") {
		document.getElementById("discRow").style.display = "none";
		document.getElementById("circleRow").style.display = "none";
		document.getElementById("squareRow").style.display = "none";
		document.getElementById("startId").value = parseInt(tinyMCEPopup.getWindowArg('start') || 1);
	}
	else {
		document.getElementById("decimalRow").style.display = "none";
		document.getElementById("laRow").style.display = "none";
		document.getElementById("uaRow").style.display = "none";
		document.getElementById("lrRow").style.display = "none";
		document.getElementById("urRow").style.display = "none";
		document.getElementById("startRow").style.display = "none";
	}
	if (!isIE && list != "ol")
		document.getElementById("listStartRow").style.display = "none";
	if (isIE) {
		document.getElementById("classAttrId").value = tinyMCEPopup.getWindowArg('classAttr') || '';
	} else {
		document.getElementById("classNameRow").style.display = "none";
	}
}

function setListStyleType(listStyleType) {
	document.forms[0].listStyleTypeId.value = listStyleType;
}

function styleList() {
	var formObj = document.forms[0];
	var listStyleType = formObj.listStyleType.value;
	var listStart = parseInt(formObj.start.value);
	var ed = tinyMCEPopup.editor;
	var se = ed.selection.getNode(); // selectedElement
	var p = ed.dom.getParent(se, 'ol,ul'); // parent
	if (p) {
		tinyMCEPopup.execCommand('mceBeginUndoLevel');
		p.style.listStyleType = listStyleType;
		p.start = listStart || 1;
		if (tinyMCEPopup.getWindowArg('isIE'))
			p.className = formObj.classAttr.value;
		tinyMCEPopup.execCommand('mceEndUndoLevel');
	}
	tinyMCEPopup.close();
}

function cancelAction() {
	tinyMCEPopup.close();
}

tinyMCEPopup.onInit.add(init);
