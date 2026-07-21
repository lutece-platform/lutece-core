<#-- INIT THEME DATE PICKER COMPONENT                       -->
<#-- initThemeDatePicker                                    -->
<#-- ------------------------------------------------------ -->
<#macro initThemeDatePicker>
<!-- DatePicker css -->
<link rel="stylesheet" href="${commonsSharedThemePath}${commonsSiteJsPath}lib/datepicker/style/theme-datepicker.min.css">
<!-- Accessible DatePicker js (W3C APG pattern) -->
<script src="${commonsSharedThemePath}${commonsSiteJsPath}util/lutece.js" charset="utf-8"></script>
<script src="${commonsSharedThemePath}${commonsSiteJsPath}lib/datepicker/theme-datepicker-a11y.min.js" charset="utf-8"></script>
<script src="${commonsSharedThemePath}${commonsSiteJsPath}lib/datepicker/theme-daterangepicker-a11y.min.js" charset="utf-8"></script>
<!-- DatePicker locales (must load after theme-datepicker-a11y.js which exposes Datepicker.locales) -->
<#if !dskey('theme.site_property.config.locales')?starts_with('DS')>
<#local localesList=dskey('theme.site_property.config.locales') />
<#list localesList?split(",") as loc>
<script src="${commonsSharedThemePath}${commonsSiteJsPath}lib/datepicker/locales/${loc}.js" charset="utf-8"></script>
</#list>
<#else>
<script src="${commonsSharedThemePath}${commonsSiteJsPath}lib/datepicker/locales/fr.js" charset="utf-8"></script>
</#if>
<script>
// Function to set date picker options (compat shim)
function setDatePickerOptions( themeOption, customOptions, defaultOptions ) {
  const options = {};
  for ( const key in defaultOptions ) {
    options[key] = defaultOptions[key];
    if ( themeOption.hasOwnProperty(key) ) {
        options[key] = themeOption[key];
    }
    if ( customOptions.hasOwnProperty(key) ) {
        options[key] = customOptions[key];
    }
  }
  return options;
}
// Compat alias so existing code using `new themeDatepicker(...)` keeps working
if (typeof themeDatepicker === 'undefined') { window.themeDatepicker = window.themeDatepickerA11y; }

/* ------------------------------------------------------------------ */
/*  Front-end validation of manually typed dates                      */
/*  - triggered on blur and on form submit                            */
/*  - a value that does not match the picker format is invalid        */
/*  - a non-existent date (31/02, month 14, ...) is invalid           */
/*  - the error message clears as soon as the value becomes valid     */
/* ------------------------------------------------------------------ */
(function () {
  if (typeof DatepickerDialog === 'undefined') return;
  if (DatepickerDialog.prototype._dpValidationPatched) return;
  DatepickerDialog.prototype._dpValidationPatched = true;

  function isLeapYear(y) { return (y % 4 === 0 && y % 100 !== 0) || y % 400 === 0; }
  var DAYS_IN_MONTH = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
  function daysInMonth(y, m) { return (m === 1 && isLeapYear(y)) ? 29 : DAYS_IN_MONTH[m]; }

  // Strict check: value must follow the format AND be an existing date
  function isValidDate(str, fmt) {
    var sep = fmt.replace(/[a-zA-Z]/g, '').charAt(0) || '/';
    var fmtParts = fmt.toLowerCase().split(sep);
    var valParts = str.split(sep);
    if (valParts.length !== fmtParts.length) return false;
    var d = null, m = null, y = null;
    for (var i = 0; i < fmtParts.length; i++) {
      var tok = fmtParts[i];
      var v = valParts[i];
      if (!/^[0-9]+$/.test(v)) return false;            // digits only, no spaces/letters
      if (tok === 'dd' || tok === 'mm' || tok === 'yy') { if (v.length !== 2) return false; }
      else if (tok === 'yyyy') { if (v.length !== 4) return false; }
      else if (tok === 'd' || tok === 'm') { if (v.length < 1 || v.length > 2) return false; }
      var n = parseInt(v, 10);
      if (tok.charAt(0) === 'd') d = n;
      else if (tok.charAt(0) === 'm') m = n;
      else if (tok.charAt(0) === 'y') y = (v.length <= 2) ? 2000 + n : n;
    }
    if (d === null || m === null || y === null) return false;
    if (m < 1 || m > 12) return false;
    if (d < 1 || d > daysInMonth(y, m - 1)) return false;
    return true;
  }

  // Build the format hint from the localized Day/Month/Year initials provided by
  // the theme bundle (Lutece core locale): "JMA" (fr) / "DMY" (en).
  // e.g. dd/mm/yyyy -> JJ/MM/AAAA (fr) or DD/MM/YYYY (en), separators preserved.
  function formatHint(fmt, letters) {
    return fmt.toLowerCase()
      .replace(/d/g, letters.charAt(0) || 'D')
      .replace(/m/g, letters.charAt(1) || 'M')
      .replace(/y/g, letters.charAt(2) || 'Y');
  }

  // The message text and the format letters are resolved server-side by the
  // Lutece i18n system (#i18n), so the displayed language follows the core locale.
  function buildMessage(fmt, custom) {
    if (custom) return custom;
    var hint = formatHint(fmt, '#i18n{portal.theme.error.invalidDateFormat.letters}');
    var example = DatepickerDialog.formatDate(new Date(2025, 5, 30), fmt); // 30 June 2025
    return '#i18n{portal.theme.error.invalidDateFormat}'.replace('{0}', hint).replace('{1}', example);
  }

  function getGroup(input) { return input.closest('.input-group') || input; }

  function showError(input, msg) {
    input.classList.add('is-invalid');
    input.setAttribute('aria-invalid', 'true');
    var fb = input._dpFeedback;
    if (!fb || !fb.parentNode) {
      fb = document.createElement('p');
      fb.className = 'invalid-feedback';
      fb.id = 'error_' + (input.id || '');
      fb.innerHTML = '<svg class="paris-icon paris-icon-alert-error main-danger-color" aria-hidden="true" focusable="false" role="img"><use href="#paris-icon-alert-error"></use></svg> ';
      fb.appendChild(document.createTextNode(''));
      var group = getGroup(input);
      group.parentNode.insertBefore(fb, group);
      input._dpFeedback = fb;
    }
    fb.lastChild.nodeValue = msg;
    if (fb.id) input.setAttribute('aria-describedby', fb.id);
  }

  function clearError(input) {
    input.classList.remove('is-invalid');
    input.removeAttribute('aria-invalid');
    input.removeAttribute('aria-describedby');
    if (input._dpFeedback && input._dpFeedback.parentNode) {
      input._dpFeedback.parentNode.removeChild(input._dpFeedback);
    }
    input._dpFeedback = null;
  }

  var origAttach = DatepickerDialog.prototype._attachInputEvents;
  DatepickerDialog.prototype._attachInputEvents = function () {
    origAttach.apply(this, arguments);

    var self = this;
    var input = this.inputEl;
    var fmt = this.options.format || 'dd/mm/yyyy';
    var msg = buildMessage(fmt, input.getAttribute('data-date-error'));
    var touched = false;

    var run = function () {
      var v = (input.value || '').trim();
      if (v === '' || isValidDate(v, fmt)) { clearError(input); return true; }
      showError(input, msg);
      return false;
    };
    this._dpValidate = run;

    // Validate when the field loses focus (but not when focus moves into the
    // open calendar dialog — selecting a date will re-validate via changeDate).
    // While the typed value is invalid, the user is kept in the field.
    input.addEventListener('focusout', function (e) {
      if (e.relatedTarget && self.dialog && self.dialog.contains(e.relatedTarget)) return;
      touched = true;
      setTimeout(function () {
        var active = document.activeElement;
        if (active && self.dialog && self.dialog.contains(active)) return;
        if (run()) {
          // Valid or empty: let the focus go and close the calendar behind
          if (self.isOpen && active !== input) self.close(false);
          return;
        }
        self._suppressOpen = true;
        input.focus();
        if (input.setSelectionRange) input.setSelectionRange(0, input.value.length);
        setTimeout(function () { self._suppressOpen = false; }, 100);
      }, 0);
    });
    // Re-validate while typing once the field has been touched, so the
    // message disappears as soon as the value becomes valid again
    input.addEventListener('input', function () { if (touched) run(); });
    // A date chosen in the calendar (or cleared) always dispatches changeDate
    input.addEventListener('changeDate', function () { run(); });

    // Validate on submit and block the submission if the date is invalid
    var form = input.closest('form');
    if (form) {
      form.addEventListener('submit', function (e) {
        touched = true;
        if (input.offsetParent !== null && !run()) {
          e.preventDefault();
          input.focus();
        }
      });
    }
  };
})();
</script>
</#macro>