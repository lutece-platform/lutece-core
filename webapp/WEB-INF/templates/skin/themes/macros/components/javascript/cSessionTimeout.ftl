<#--
Macro: cSessionTimeout

Description: Displays a non-disruptive notification before session expiration to warn users
about timeouts (WCAG 2.2.1 Level A / 2.2.6 Level AAA). Includes a countdown timer, a button
to extend the session, optional data saving, and screen reader announcements.

Core configuration is read from core_datastore keys.
Plugins can pre-set additional properties on window.__sessionTimeoutConfig BEFORE this macro
renders (their templates are included earlier in the page). Supported plugin properties:
  - processTimeoutDuration (number): shorter process-specific timeout in seconds.
  - processWarningDelay   (number): warning delay for the process timeout.
  - saveEnabled           (boolean): show a "Save" button in the warning.
  - saveAction            (function): async callback returning a Promise to save user data.
  - messages.*            (strings): plugin-specific message overrides.

Parameters:
- none: All core configuration is read from the core_datastore via dskey().

Datastore keys:
- portal.theme.site_property.sessiontimeout.enabled      (string): '1' to enable, '0' to disable. Default: '0'.
- portal.theme.site_property.sessiontimeout.duration     (string): Session timeout in seconds. Default: '1800'.
- portal.theme.site_property.sessiontimeout.warningDelay (string): Seconds before timeout to show warning. Default: '120'.
- portal.theme.site_property.sessiontimeout.keepAliveUrl (string): URL to ping for session renewal. Default: base_url + jsp/site/Portal.jsp.
- portal.theme.site_property.sessiontimeout.maxExtensions(string): Max times the user can extend (WCAG >= 10). Default: '10'.
- portal.theme.site_property.sessiontimeout.position     (string): Bootstrap position classes for the toast container. Default: 'top-0 end-0'.

Snippet:

    Add session timeout warning (typically in page footer):

    <@cSessionTimeout />

    Plugin integration (e.g. Forms step_view.html) — add BEFORE the macro renders:

    <script>
    window.__sessionTimeoutConfig = window.__sessionTimeoutConfig || {};
    window.__sessionTimeoutConfig.processTimeoutDuration = 600;
    window.__sessionTimeoutConfig.saveEnabled = true;
    window.__sessionTimeoutConfig.saveAction = function() {
        var form = document.getElementById('form-validate');
        if (!form) return Promise.resolve();
        var fd = new FormData(form);
        fd.append('action_doSaveForBackup', '');
        return fetch(form.action, { method: 'POST', body: fd });
    };
    </script>

-->
<#macro cSessionTimeout deprecated...>
<@deprecatedWarning args=deprecated />
<#assign sessionTimeoutEnabled = ( !dskey('portal.theme.site_property.sessiontimeout.enabled.checkbox')?starts_with('DS') && dskey('portal.theme.site_property.sessiontimeout.enabled.checkbox') == '1' )?then( 'true', 'false' ) />
<#if sessionTimeoutEnabled == 'true'>
<#assign sessionTimeoutDuration = ( dskey('portal.theme.site_property.sessiontimeout.duration')?trim?matches( '[0-9]+' ) )?then( dskey('portal.theme.site_property.sessiontimeout.duration')?trim, '1800' ) />
<#assign sessionWarningDelay = ( dskey('portal.theme.site_property.sessiontimeout.warningDelay')?trim?matches( '[0-9]+' ) )?then( dskey('portal.theme.site_property.sessiontimeout.warningDelay')?trim, '120' ) />
<#assign sessionKeepAliveUrl = ( !dskey('portal.theme.site_property.sessiontimeout.keepAliveUrl')?starts_with('DS') && dskey('portal.theme.site_property.sessiontimeout.keepAliveUrl') != '' )?then( ( dskey('portal.theme.site_property.sessiontimeout.keepAliveUrl') )?string, ( base_url!'' )?string + 'jsp/site/Portal.jsp' ) />
<#assign sessionMaxExtensions = ( dskey('portal.theme.site_property.sessiontimeout.maxExtensions')?trim?matches( '[0-9]+' ) )?then( dskey('portal.theme.site_property.sessiontimeout.maxExtensions')?trim, '10' ) />
<#assign sessionPosition = ( !dskey('portal.theme.site_property.sessiontimeout.position')?starts_with('DS') && dskey('portal.theme.site_property.sessiontimeout.position') != '' )?then( ( dskey('portal.theme.site_property.sessiontimeout.position') )?string, 'top-0 end-0' ) />
<#assign sessionLoginUrl = safeUrl( ( urlAuth?? && urlAuth?has_content )?then( ( urlAuth )?string, 'jsp/site/Portal.jsp?page=mylutece&action=doLogin' ), 'jsp/site/Portal.jsp?page=mylutece&action=doLogin' ) />
<script>
(function() {
    var pluginConfig = window.__sessionTimeoutConfig || {};
    var pluginMessages = pluginConfig.messages || {};
    window.__sessionTimeoutConfig = Object.assign({
        timeoutDuration: ${sessionTimeoutDuration},
        warningDelay: ${sessionWarningDelay},
        keepAliveUrl: "${sessionKeepAliveUrl?js_string?no_esc}",
        loginUrl: "${sessionLoginUrl?js_string?no_esc}",
        maxExtensions: ${sessionMaxExtensions},
        position: "${sessionPosition?js_string?no_esc}"
    }, pluginConfig, {
        messages: Object.assign({
            warningTitle: "${i18n('portal.theme.sessiontimeout.warningTitle')?js_string?no_esc}",
            warningMessage: "${i18n('portal.theme.sessiontimeout.warningMessage')?js_string?no_esc}",
            btnExtend: "${i18n('portal.theme.sessiontimeout.btnExtend')?js_string?no_esc}",
            btnSave: "${i18n('portal.theme.sessiontimeout.btnSave')?js_string?no_esc}",
            saveSuccess: "${i18n('portal.theme.sessiontimeout.saveSuccess')?js_string?no_esc}",
            saveError: "${i18n('portal.theme.sessiontimeout.saveError')?js_string?no_esc}",
            expiredTitle: "${i18n('portal.theme.sessiontimeout.expiredTitle')?js_string?no_esc}",
            expiredMessage: "${i18n('portal.theme.sessiontimeout.expiredMessage')?js_string?no_esc}",
            btnLogin: "${i18n('portal.theme.sessiontimeout.btnLogin')?js_string?no_esc}",
            labelClose: "${i18n('portal.util.labelClose')?js_string?no_esc}",
            srCountdown: "${i18n('portal.theme.sessiontimeout.srCountdown')?js_string?no_esc}",
            maxExtensionsReached: "${i18n('portal.theme.sessiontimeout.maxExtensionsReached')?js_string?no_esc}",
            processWarningTitle: "${i18n('portal.theme.sessiontimeout.processWarningTitle')?js_string?no_esc}",
            processWarningMessage: "${i18n('portal.theme.sessiontimeout.processWarningMessage')?js_string?no_esc}",
            processExpiredTitle: "${i18n('portal.theme.sessiontimeout.processExpiredTitle')?js_string?no_esc}",
            processExpiredMessage: "${i18n('portal.theme.sessiontimeout.processExpiredMessage')?js_string?no_esc}"
        }, pluginMessages)
    });
})();
</script>
<script type="module">
import LuteceSessionTimeout from './${commonsSiteSharedPath?js_string?no_esc}modules/luteceSessionTimeout.js';
new LuteceSessionTimeout(window.__sessionTimeoutConfig);
</script>
</#if>
</#macro>
