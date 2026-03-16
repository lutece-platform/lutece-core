/**
 * get  FlatPicker lang default local Date short format
 * 
 * @return the Date format pattern
 **/
function getDatePickerDateFormat( lang ) {
  lang = lang.search(/-/) < 0 ? lang + '-' + lang.toUpperCase() : lang
  const dformats = {
    "ar-DZ": "d-M-y",
    "ar-MA": "d-M-y",
    "ar-SA": "d/M/yy",
    "ar-TN": "d-M-y",
    "az-Cyrl-AZ": "d.M.y",
    "az-Latn-AZ": "d.M.y",
    "be-BY": "d.M.y",
    "bg-BG": "d.M.y",
    "bn-BD": "d-M-yy",
    "bn-IN": "d-M-yy",
    "bo-CN": "y/M/d",
    "bs-Cyrl-BA": "d.M.y",
    "bs-Latn-BA": "d.M.y",
    "cs-CZ": "d.M.y",
    "da-DK": "d-M-y",
    "de-AT": "d.M.y",
    "de-CH": "d.M.y",
    "de-DE": "d.M.y",
    "de-LI": "d.M.y",
    "de-LU": "d.M.y",
    "dsb-DE": "d. M. y",
    "dv-MV": "d/M/yy",
    "el-GR": "d/M/y",
    "en-029": "M/d/y",
    "en-AU": "d/M/y",
    "en-IN": "d-M-y",
    "en-MY": "d/M/y",
    "en-NZ": "d/M/y",
    "en-PH": "M/d/y",
    "en-SG": "d/M/y",
    "en-US": "m/d/Y",
    "en-ZA": "y/M/d",
    "en-ZW": "M/d/y",
    "es-CL": "d-M-y",
    "es-PA": "M/d/y",
    "es-US": "M/d/y",
    "et-EE": "d.M.y",
    "fa-IR": "M/d/y",
    "fi-FI": "d.M.y",
    "fil-PH": "M/d/y",
    "fo-FO": "d-M-y",
    "fr-BE": "d/M/y",
    "fr-CA": "y-M-d",
    "fr-CH": "d.M.y",
    "fy-NL": "d-M-y",
    "hi-IN": "d-M-y",
    "hr-BA": "d.M.y.",
    "hr-HR": "d.M.y",
    "hu-HU": "y. M. d.",
    "is-IS": "d.M.y",
    "it-CH": "d.M.y",
    "ja-JP": "y/M/d",
    "ka-GE": "d.M.y",
    "km-KH": "y-M-d",
    "ko-KR": "y. M. d",
    "lt-LT": "y.M.d",
    "lv-LV": "y.M.d.",
    "mk-MK": "d.M.y",
    "mn-MN": "yy.M.d",
    "mn-Mong-CN": "y/M/d",
    "nl-BE": "d/M/y",
    "nl-NL": "d-M-y",
    "nn-NO": "d.M.y",
    "pa-IN": "d-M-yy",
    "pl-PL": "d.M.y",
    "pt-BR": "d/M/y",
    "pt-PT": "d-M-y",
    "ro-RO": "d.M.y",
    "ru-RU": "d.M.y",
    "si-LK": "y-M-d",
    "sk-SK": "d. M. y",
    "sl-SI": "d.M.y",
    "sq-AL": "y-M-d",
    "sr-Cyrl-BA": "d.M.y",
    "sr-Cyrl-CS": "d.M.y",
    "sr-Cyrl-ME": "d.M.y",
    "sr-Cyrl-RS": "d.M.y",
    "sr-Latn-BA": "d.M.y",
    "sr-Latn-CS": "d.M.y",
    "sr-Latn-ME": "d.M.y",
    "sr-Latn-RS": "d.M.y",
    "sv-FI": "d.M.y",
    "sv-SE": "y-M-d",
    "th-TH": "d/M/y",
    "tr-TR": "d.M.y",
    "uk-UA": "d.M.y",
    "uz-Cyrl-UZ": "d.M.y",
    "uz-Latn-UZ": "d/M y",
    "zh-CN": "y/M/d",
    "zh-HK": "d/M/y",
    "zh-MO": "d/M/y",
    "zh-SG": "d/M/y",
    "zh-TW": "y/M/d",
    "zu-ZA": "y/M/d",
  };
  return dformats[lang] || "dd/mm/yyyy";
}

/**
 * get lang default local time short format
 * 
 * @return the time format pattern
 **/
function getFlatPickerTimeFormat( lang ) {
  const tformats = {
	"es-MX": "H:i", 
	"ar-BH": "h:i K", 
	"ar-DZ": "h:i K", 
	"ar-EG": "h:i K", 
	"ar-IQ": "h:i K", 
	"ar-JO": "h:i K", 
	"ar-KW": "h:i K", 
	"ar-LB": "h:i K", 
	"ar-LY": "h:i K", 
	"ar-OI": "h:i K", 
	"ar-QA": "h:i K", 
	"ar-SA": "h:i K", 
	"ar-SY": "h:i K", 
	"ar-TN": "h:i K", 
	"ar-YE": "h:i K", 
	"bn-IN": "h:i K", 
	"en-AU": "h:i K", 
	"en-IN": "h:i K", 
	"en-JI": "h:i K", 
	"en-IY": "h:i K", 
	"en-NZ": "h:i K", 
	"en-PH": "h:i K", 
	"en-SG": "h:i K", 
	"en-TT": "h:i K", 
	"en-US": "h:i K", 
	"es-CO": "h:i K", 
	"es-DO": "h:i K", 
	"es-PA": "h:i K", 
	"es-PR": "h:i K", 
	"es-US": "h:i K", 
	"es-VE": "h:i K", 
	"is-BN": "h:i K", 
	"fr-CA": "H \\h i"
  };
  return tformats[lang] || "H:i";
}

/**
 * get lang default local datetime short format
 * 
 * @return the datetime format pattern
 **/
function getDefaultDateTimeFormat( lang ) {
   return getDefaultDateFormat( lang ) + " " + getDefaultTimeFormat( lang ) ;
}

/**
 * scriptExists
 * 
 * url    : Url for the script to seek
 * @return exist ?
 **/
function scriptExists(url) {
  return document.querySelectorAll(`script[src="${url}"]`).length > 0;
}

/**
 * loadJS
 * 
 * url    : Url for script
 * async  : false / true load script asynchronously
 * @return Load script at end of page
 **/
function loadScript( url ) {
  let script = document.createElement('script');
  script.src = url;
  if( !scriptExists(url) ){
      document.body.appendChild(script);
  }  
}

/**
 * cssExists
 * 
 * url    : Url for the css to seek
 * @return exist ?
 **/
 function cssExists(url) {
  return document.querySelectorAll(`link[href="${url}"]`).length > 0;
}

/**
 * loadCSS
 * 
 * url    : Url for script
 * async  : false / true load script asynchronously
 * @return Load script at end of page
 **/
 function loadStyle( url ) {
  let link = document.createElement('link');
  link.href = url;
  link.rel = 'stylesheet';
  if( !cssExists(url) ){
      document.head.appendChild(link);
  }  
}

/**
 * getBrowserLocales
 * 
 * option    : Url for script
 * async  : false / true load script asynchronously
 * @return List of locales in browser 
 **/
function getBrowserLocales(options = {}) {
  const defaultOptions = {
      languageCodeOnly: false,
  };

  const opt = {
      ...defaultOptions,
      ...options,
  };

  const browserLocales =
      navigator.languages === undefined
      ? [navigator.language]
      : navigator.languages;

  if (!browserLocales) {
      return undefined;
  }

  return browserLocales.map( locale => {
      const trimmedLocale = locale.trim();

      return opt.languageCodeOnly
          ? trimmedLocale.split(/-|_/)[0]
          : trimmedLocale;
  });
}
