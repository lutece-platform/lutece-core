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
<#assign sessionTimeoutEnabled><#if !dskey('portal.theme.site_property.sessiontimeout.enabled.checkbox')?starts_with('DS') && dskey('portal.theme.site_property.sessiontimeout.enabled.checkbox') == '1'>true<#else>false</#if></#assign>
<#if sessionTimeoutEnabled == 'true'>
<#assign sessionTimeoutDuration><#if !dskey('portal.theme.site_property.sessiontimeout.duration')?starts_with('DS') && dskey('portal.theme.site_property.sessiontimeout.duration') != ''>${dskey('portal.theme.site_property.sessiontimeout.duration')}<#else>1800</#if></#assign>
<#assign sessionWarningDelay><#if !dskey('portal.theme.site_property.sessiontimeout.warningDelay')?starts_with('DS') && dskey('portal.theme.site_property.sessiontimeout.warningDelay') != ''>${dskey('portal.theme.site_property.sessiontimeout.warningDelay')}<#else>120</#if></#assign>
<#assign sessionKeepAliveUrl><#if !dskey('portal.theme.site_property.sessiontimeout.keepAliveUrl')?starts_with('DS') && dskey('portal.theme.site_property.sessiontimeout.keepAliveUrl') != ''>${dskey('portal.theme.site_property.sessiontimeout.keepAliveUrl')}<#else>${base_url!}jsp/site/Portal.jsp</#if></#assign>
<#assign sessionMaxExtensions><#if !dskey('portal.theme.site_property.sessiontimeout.maxExtensions')?starts_with('DS') && dskey('portal.theme.site_property.sessiontimeout.maxExtensions') != ''>${dskey('portal.theme.site_property.sessiontimeout.maxExtensions')}<#else>10</#if></#assign>
<#assign sessionPosition><#if !dskey('portal.theme.site_property.sessiontimeout.position')?starts_with('DS') && dskey('portal.theme.site_property.sessiontimeout.position') != ''>${dskey('portal.theme.site_property.sessiontimeout.position')}<#else>top-0 end-0</#if></#assign>
<#assign sessionLoginUrl><#if urlAuth?? && urlAuth != ''>${urlAuth}<#else>jsp/site/Portal.jsp?page=mylutece&action=doLogin</#if></#assign>
<script>
(function() {
    var pluginConfig = window.__sessionTimeoutConfig || {};
    var pluginMessages = pluginConfig.messages || {};
    window.__sessionTimeoutConfig = Object.assign({
        timeoutDuration: ${sessionTimeoutDuration},
        warningDelay: ${sessionWarningDelay},
        keepAliveUrl: "${sessionKeepAliveUrl?js_string}",
        loginUrl: "${sessionLoginUrl?js_string}",
        maxExtensions: ${sessionMaxExtensions},
        position: "${sessionPosition?js_string}"
    }, pluginConfig, {
        messages: Object.assign({
            warningTitle: "#i18n{portal.theme.sessiontimeout.warningTitle}",
            warningMessage: "#i18n{portal.theme.sessiontimeout.warningMessage}",
            btnExtend: "#i18n{portal.theme.sessiontimeout.btnExtend}",
            btnSave: "#i18n{portal.theme.sessiontimeout.btnSave}",
            saveSuccess: "#i18n{portal.theme.sessiontimeout.saveSuccess}",
            saveError: "#i18n{portal.theme.sessiontimeout.saveError}",
            expiredTitle: "#i18n{portal.theme.sessiontimeout.expiredTitle}",
            expiredMessage: "#i18n{portal.theme.sessiontimeout.expiredMessage}",
            btnLogin: "#i18n{portal.theme.sessiontimeout.btnLogin}",
            labelClose: "#i18n{portal.util.labelClose}",
            srCountdown: "#i18n{portal.theme.sessiontimeout.srCountdown}",
            maxExtensionsReached: "#i18n{portal.theme.sessiontimeout.maxExtensionsReached}",
            processWarningTitle: "#i18n{portal.theme.sessiontimeout.processWarningTitle}",
            processWarningMessage: "#i18n{portal.theme.sessiontimeout.processWarningMessage}",
            processExpiredTitle: "#i18n{portal.theme.sessiontimeout.processExpiredTitle}",
            processExpiredMessage: "#i18n{portal.theme.sessiontimeout.processExpiredMessage}"
        }, pluginMessages)
    });
})();
</script>
<script type="module">
import LuteceSessionTimeout from './${commonsSiteSharedPath}modules/luteceSessionTimeout.js';
new LuteceSessionTimeout(window.__sessionTimeoutConfig);
</script>
</#if>
</#macro>
