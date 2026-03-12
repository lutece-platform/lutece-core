<#--
Macro: initThemeDatePicker

Description: Generates the CSS and JavaScript includes required to initialize the theme datepicker library on the page.

Parameters:
None.

Snippet:

    Initialize datepicker resources before using getThemeDatePicker:

    <@initThemeDatePicker />
    <@getThemeDatePicker idField='appointmentDate' />

-->
<#macro initThemeDatePicker>
<!-- DatePicker css -->
<link rel="stylesheet" href="${commonsSiteThemePath}${commonsSiteJsPath}vendor/datepicker/datepicker-bs5.min.css">
<link rel="stylesheet" href="${commonsSiteThemePath}${commonsSiteJsPath}vendor/datepicker/style/theme-datepicker.css">
<!-- DatePicker js -->
<script src="${commonsSiteThemePath}${commonsSiteJsPath}util/lutece.js" charset="utf-8"></script>
<script src="${commonsSiteThemePath}${commonsSiteJsPath}vendor/datepicker/datepicker-full.min.js" charset="utf-8"></script>
<script src="${commonsSiteThemePath}${commonsSiteJsPath}vendor/datepicker/datepicker-theme.js" charset="utf-8"></script>
<script src="${commonsSiteThemePath}${commonsSiteJsPath}vendor/datepicker/locales/fr.js" charset="utf-8"></script>
<script type="module">
import { themeUtils } from './${commonsSiteThemePath}${commonsSiteJsPath}/modules/theme-utils.js';
const uploadUtils = new themeUtils;
// Get current browser lang
window.addEventListener('DOMContentLoaded', (event) => {
  if( !navigator.language.startsWith('fr') ){
      uploadUtils.addResourceLinksFromArray( [{"name": <#noparse>`${navigator.language.split('-')[0]}.js`</#noparse>, "pathtoadd" : "${commonsSiteThemePath}${commonsSiteJsPath}vendor/datepicker/"}] )
  }
});
</script>
</#macro>