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
<link rel="stylesheet" href="${commonsSharedThemePath}${commonsSiteJsPath}vendor/datepicker/datepicker-bs5.min.css">
<link rel="stylesheet" href="${commonsSharedThemePath}${commonsSiteJsPath}vendor/datepicker/style/theme-datepicker.css">
<!-- DatePicker js -->
<script src="${commonsSharedThemePath}${commonsSiteJsPath}util/lutece.js" charset="utf-8"></script>
<script src="${commonsSharedThemePath}${commonsSiteJsPath}vendor/datepicker/datepicker-full.min.js" charset="utf-8"></script>
<script src="${commonsSharedThemePath}${commonsSiteJsPath}vendor/datepicker/datepicker-theme.js" charset="utf-8"></script>
<script src="${commonsSharedThemePath}${commonsSiteJsPath}vendor/datepicker/locales/fr.js" charset="utf-8"></script>
<script type="module">
import { themeUtils } from './${commonsSiteSharedPath}${commonsSiteJsModulesPath}theme-utils.min.js';
const uploadUtils = new themeUtils;
// Get current browser lang
window.addEventListener('DOMContentLoaded', (event) => {
  if( !navigator.language.startsWith('fr') ){
      uploadUtils.addResourceLinksFromArray( [{"name": <#noparse>`${navigator.language.split('-')[0]}.js`</#noparse>, "pathtoadd" : "${commonsSharedThemePath}${commonsSiteJsPath}vendor/datepicker/"}] )
  }
});
</script>
</#macro>