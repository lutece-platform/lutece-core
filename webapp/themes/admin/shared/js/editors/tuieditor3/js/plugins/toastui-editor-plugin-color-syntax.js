/*!
 * TOAST UI Editor : Color Syntax Plugin
 * @version 3.1.0 | Fri Apr 21 2023
 * @author NHN Cloud FE Development Lab <dl_javascript@nhn.com>
 * @license MIT
 */
(function webpackUniversalModuleDefinition(root, factory) {
	if(typeof exports === 'object' && typeof module === 'object')
		module.exports = factory(require("tui-color-picker"));
	else if(typeof define === 'function' && define.amd)
		define(["tui-color-picker"], factory);
	else if(typeof exports === 'object')
		exports["toastui"] = factory(require("tui-color-picker"));
	else
		root["toastui"] = root["toastui"] || {}, root["toastui"]["Editor"] = root["toastui"]["Editor"] || {}, root["toastui"]["Editor"]["plugin"] = root["toastui"]["Editor"]["plugin"] || {}, root["toastui"]["Editor"]["plugin"]["colorSyntax"] = factory(root["tui"]["colorPicker"]);
})(self, function(__WEBPACK_EXTERNAL_MODULE__858__) {
return /******/ (function() { // webpackBootstrap
/******/ 	"use strict";
/******/ 	var __webpack_modules__ = ({

/***/ 858:
/***/ (function(module) {

module.exports = __WEBPACK_EXTERNAL_MODULE__858__;

/***/ })

/******/ 	});
/************************************************************************/
/******/ 	// The module cache
/******/ 	var __webpack_module_cache__ = {};
/******/ 	
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/ 		// Check if module is in cache
/******/ 		var cachedModule = __webpack_module_cache__[moduleId];
/******/ 		if (cachedModule !== undefined) {
/******/ 			return cachedModule.exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = __webpack_module_cache__[moduleId] = {
/******/ 			// no module.id needed
/******/ 			// no module.loaded needed
/******/ 			exports: {}
/******/ 		};
/******/ 	
/******/ 		// Execute the module function
/******/ 		__webpack_modules__[moduleId](module, module.exports, __webpack_require__);
/******/ 	
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/ 	
/************************************************************************/
/******/ 	/* webpack/runtime/compat get default export */
/******/ 	!function() {
/******/ 		// getDefaultExport function for compatibility with non-harmony modules
/******/ 		__webpack_require__.n = function(module) {
/******/ 			var getter = module && module.__esModule ?
/******/ 				function() { return module['default']; } :
/******/ 				function() { return module; };
/******/ 			__webpack_require__.d(getter, { a: getter });
/******/ 			return getter;
/******/ 		};
/******/ 	}();
/******/ 	
/******/ 	/* webpack/runtime/define property getters */
/******/ 	!function() {
/******/ 		// define getter functions for harmony exports
/******/ 		__webpack_require__.d = function(exports, definition) {
/******/ 			for(var key in definition) {
/******/ 				if(__webpack_require__.o(definition, key) && !__webpack_require__.o(exports, key)) {
/******/ 					Object.defineProperty(exports, key, { enumerable: true, get: definition[key] });
/******/ 				}
/******/ 			}
/******/ 		};
/******/ 	}();
/******/ 	
/******/ 	/* webpack/runtime/hasOwnProperty shorthand */
/******/ 	!function() {
/******/ 		__webpack_require__.o = function(obj, prop) { return Object.prototype.hasOwnProperty.call(obj, prop); }
/******/ 	}();
/******/ 	
/************************************************************************/
var __webpack_exports__ = {};
// This entry need to be wrapped in an IIFE because it need to be isolated against other modules in the chunk.
!function() {

// EXPORTS
__webpack_require__.d(__webpack_exports__, {
  "default": function() { return /* binding */ colorSyntaxPlugin; }
});

// EXTERNAL MODULE: external {"commonjs":"tui-color-picker","commonjs2":"tui-color-picker","amd":"tui-color-picker","root":["tui","colorPicker"]}
var external_commonjs_tui_color_picker_commonjs2_tui_color_picker_amd_tui_color_picker_root_tui_colorPicker_ = __webpack_require__(858);
var external_commonjs_tui_color_picker_commonjs2_tui_color_picker_amd_tui_color_picker_root_tui_colorPicker_default = /*#__PURE__*/__webpack_require__.n(external_commonjs_tui_color_picker_commonjs2_tui_color_picker_amd_tui_color_picker_root_tui_colorPicker_);
;// CONCATENATED MODULE: ./src/i18n/langs.ts
function addLangs(i18n) {
    i18n.setLanguage('ar', {
        'Text color': 'لون النص',
    });
    i18n.setLanguage(['cs', 'cs-CZ'], {
        'Text color': 'Barva textu',
    });
    i18n.setLanguage(['de', 'de-DE'], {
        'Text color': 'Textfarbe',
    });
    i18n.setLanguage(['en', 'en-US'], {
        'Text color': 'Text color',
    });
    i18n.setLanguage(['es', 'es-ES'], {
        'Text color': 'Color del texto',
    });
    i18n.setLanguage(['fi', 'fi-FI'], {
        'Text color': 'Tekstin väri',
    });
    i18n.setLanguage(['fr', 'fr-FR'], {
        'Text color': 'Couleur du texte',
    });
    i18n.setLanguage(['gl', 'gl-ES'], {
        'Text color': 'Cor do texto',
    });
    i18n.setLanguage(['hr', 'hr-HR'], {
        'Text color': 'Boja teksta',
    });
    i18n.setLanguage(['it', 'it-IT'], {
        'Text color': 'Colore del testo',
    });
    i18n.setLanguage(['ja', 'ja-JP'], {
        'Text color': '文字色相',
    });
    i18n.setLanguage(['ko', 'ko-KR'], {
        'Text color': '글자 색상',
    });
    i18n.setLanguage(['nb', 'nb-NO'], {
        'Text color': 'Tekstfarge',
    });
    i18n.setLanguage(['nl', 'nl-NL'], {
        'Text color': 'Tekstkleur',
    });
    i18n.setLanguage(['pl', 'pl-PL'], {
        'Text color': 'Kolor tekstu',
    });
    i18n.setLanguage(['pt', 'pt-BR'], {
        'Text color': 'Cor do texto',
    });
    i18n.setLanguage(['ru', 'ru-RU'], {
        'Text color': 'Цвет текста',
    });
    i18n.setLanguage(['sv', 'sv-SE'], {
        'Text color': 'Textfärg',
    });
    i18n.setLanguage(['tr', 'tr-TR'], {
        'Text color': 'Metin rengi',
    });
    i18n.setLanguage(['uk', 'uk-UA'], {
        'Text color': 'Колір тексту',
    });
    i18n.setLanguage('zh-CN', {
        'Text color': '文字颜色',
    });
    i18n.setLanguage('zh-TW', {
        'Text color': '文字顏色',
    });
}

;// CONCATENATED MODULE: ./src/utils/dom.ts
function hasClass(element, className) {
    return element.classList.contains(className);
}
function findParentByClassName(el, className) {
    var currentEl = el;
    while (currentEl && !hasClass(currentEl, className)) {
        currentEl = currentEl.parentElement;
    }
    return currentEl;
}
function removeProseMirrorHackNodes(html) {
    var reProseMirrorImage = /<img class="ProseMirror-separator" alt="">/g;
    var reProseMirrorTrailingBreak = / class="ProseMirror-trailingBreak"/g;
    var resultHTML = html;
    resultHTML = resultHTML.replace(reProseMirrorImage, '');
    resultHTML = resultHTML.replace(reProseMirrorTrailingBreak, '');
    return resultHTML;
}

;// CONCATENATED MODULE: ./src/index.ts




var PREFIX = 'toastui-editor-';
function createApplyButton(text) {
    var button = document.createElement('button');
    button.setAttribute('type', 'button');
    button.textContent = text;
    return button;
}
function createToolbarItemOption(colorPickerContainer, i18n) {
    return {
        name: 'color',
        tooltip: i18n.get('Text color'),
        className: "color-toolbar-icons-color",
        text: 'C',
        popup: {
            className: PREFIX + "popup-color",
            body: colorPickerContainer,
            style: { width: 'auto' },
        },
    };
}
function createSelection(tr, selection, SelectionClass, openTag, closeTag) {
    var mapping = tr.mapping, doc = tr.doc;
    var from = selection.from, to = selection.to, empty = selection.empty;
    var mappedFrom = mapping.map(from) + openTag.length;
    var mappedTo = mapping.map(to) - closeTag.length;
    return empty
        ? SelectionClass.create(doc, mappedTo, mappedTo)
        : SelectionClass.create(doc, mappedFrom, mappedTo);
}
function getCurrentEditorEl(colorPickerEl, containerClassName) {
    var editorDefaultEl = findParentByClassName(colorPickerEl, PREFIX + "defaultUI");
    return editorDefaultEl.querySelector("." + containerClassName + " .ProseMirror");
}
var containerClassName;
var currentEditorEl;
// @TODO: add custom syntax for plugin
/**
 * Color syntax plugin
 * @param {Object} context - plugin context for communicating with editor
 * @param {Object} options - options for plugin
 * @param {Array.<string>} [options.preset] - preset for color palette (ex: ['#181818', '#292929'])
 * @param {boolean} [options.useCustomSyntax=false] - whether use custom syntax or not
 */
function colorSyntaxPlugin(context, options) {
    if (options === void 0) { options = {}; }
    var eventEmitter = context.eventEmitter, i18n = context.i18n, _a = context.usageStatistics, usageStatistics = _a === void 0 ? true : _a, pmState = context.pmState;
    var preset = options.preset;
    var container = document.createElement('div');
    var colorPickerOption = { container: container, usageStatistics: usageStatistics };
    addLangs(i18n);
    if (preset) {
        colorPickerOption.preset = preset;
    }
    var colorPicker = external_commonjs_tui_color_picker_commonjs2_tui_color_picker_amd_tui_color_picker_root_tui_colorPicker_default().create(colorPickerOption);
    var button = createApplyButton(i18n.get('OK'));
    eventEmitter.listen('focus', function (editType) {
        containerClassName = "" + PREFIX + (editType === 'markdown' ? 'md' : 'ww') + "-container";
    });
    container.addEventListener('click', function (ev) {
        if (ev.target.getAttribute('type') === 'button') {
            var selectedColor = colorPicker.getColor();
            currentEditorEl = getCurrentEditorEl(container, containerClassName);
            eventEmitter.emit('command', 'color', { selectedColor: selectedColor });
            eventEmitter.emit('closePopup');
            // force the current editor to focus for preventing to lose focus
            currentEditorEl.focus();
        }
    });
    colorPicker.slider.toggle(true);
    container.appendChild(button);
    var toolbarItem = createToolbarItemOption(container, i18n);
    return {
        markdownCommands: {
            color: function (_a, _b, dispatch) {
                var selectedColor = _a.selectedColor;
                var tr = _b.tr, selection = _b.selection, schema = _b.schema;
                if (selectedColor) {
                    var slice = selection.content();
                    var textContent = slice.content.textBetween(0, slice.content.size, '\n');
                    if(textContent === null || textContent === undefined || textContent === "" || textContent.size === 0){
                        textContent = "My text is colored";
                    }
                    textContent = "color={{"+ selectedColor +"}} message={{"+textContent+"}}"
                    var openTagInMD = "\n$$cl\n";
                    var closeTagInMD = "\n$$\n";
                    var colored = openTagInMD + textContent + closeTagInMD;
                    var openTag = "\n"+"<span style=\"color: " + selectedColor + "\">";
                    var closeTag = "</span>"+"\n";
                    tr.replaceSelectionWith(schema.text(colored)).setSelection(createSelection(tr, selection, pmState.TextSelection, openTag, closeTag));
                    dispatch(tr);
                    return true;
                }
                return false;
            },
        },
        wysiwygCommands: {
            color: function (_a, _b, dispatch) {
                var selectedColor = _a.selectedColor;
                var tr = _b.tr, selection = _b.selection, schema = _b.schema;
                if (selectedColor) {
                    var from = selection.from, to = selection.to;
                    var attrs = { htmlAttrs: { style: "color: " + selectedColor } };
                    var mark = schema.marks.span.create(attrs);
                    tr.addMark(from, to, mark);
                    dispatch(tr);
                    return true;
                }
                return false;
            },
        },
        toolbarItems: [
            {
                groupIndex: 0,
                itemIndex: 3,
                item: toolbarItem,
            },
        ],
        toHTMLRenderers: {
            htmlInline: {
                span: function (node, _a) {
                    var entering = _a.entering;
                    return entering
                        ? { type: 'openTag', tagName: 'span', attributes: node.attrs }
                        : { type: 'closeTag', tagName: 'span' };
                },
            },
        },
    };
}

}();
__webpack_exports__ = __webpack_exports__["default"];
/******/ 	return __webpack_exports__;
/******/ })()
;
});
