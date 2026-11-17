<#--
Macro: cCountdown

Description: Generates a countdown to a date (days, hours, minutes, seconds), for example the closing of a registration. The remaining time is computed on the server, so the block reads right without JavaScript; themes/skin/shared/js/modules/theme-countdown.min.js, loaded once by the first countdown of the page, keeps it running. Screen readers read the label and the end date, never the seconds. When the date is reached, expiredLabel replaces the units.

Parameters:
- date (string, required): End date and time, ISO 8601 without zone, in the server time zone: 'yyyy-MM-ddTHH:mm:ss'. Default: ''.
- label (string, optional): Text displayed above the units, for example 'Clôture des inscriptions dans'. Default: ''.
- dateLabel (string, optional): End date written for people, read by screen readers after the label, for example 'le 30 novembre 2026 à 18 h'. Default: ''.
- expiredLabel (string, optional): Text displayed once the date is reached. Default: '#i18n{portal.theme.labelCountdownExpired}'.
- showSeconds (boolean, optional): Displays the seconds. Default: true.
- light (boolean, optional): Light variant. Default: false.
- size (string, optional): '' or 'sm'. Default: ''.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Compte à rebours - @cCountdown
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Closing of a registration:

    <@cCountdown date='2026-11-30T18:00:00' label='Clôture des inscriptions dans' dateLabel='le 30 novembre 2026 à 18 h' />

-->
<#macro cCountdown date='' label='' dateLabel='' expiredLabel='#i18n{portal.theme.labelCountdownExpired}' showSeconds=true light=false size='' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local endDate = date?datetime.iso />
<#local remaining = [ ( ( endDate?long - .now?long ) / 1000 )?floor, 0 ]?max />
<#local days = ( remaining / 86400 )?floor />
<#local hours = ( ( remaining % 86400 ) / 3600 )?floor />
<#local minutes = ( ( remaining % 3600 ) / 60 )?floor />
<#local seconds = remaining % 60 />
<div class="lutece-ds-countdown<#if light> lutece-ds-countdown-light</#if><#if size!=''> lutece-ds-countdown-${size}</#if><#if remaining == 0> is-expired</#if><#if class!=''> ${class}</#if>" role="timer" data-countdown-to="${endDate?iso_utc}"<@commonAttrs id=id params=params />>
<#if label!=''><p class="lutece-ds-countdown-label">${label}<#if dateLabel!=''><span class="visually-hidden"> (${dateLabel})</span></#if></p><#elseif dateLabel!=''><p class="visually-hidden">${dateLabel}</p></#if>
<div class="lutece-ds-countdown-units" aria-hidden="true">
<span class="lutece-ds-countdown-unit"><span class="lutece-ds-countdown-number" data-countdown-unit="days">${days?c}</span><span class="lutece-ds-countdown-name">#i18n{portal.theme.labelCountdownDays}</span></span>
<span class="lutece-ds-countdown-unit"><span class="lutece-ds-countdown-number" data-countdown-unit="hours">${hours?string('00')}</span><span class="lutece-ds-countdown-name">#i18n{portal.theme.labelCountdownHours}</span></span>
<span class="lutece-ds-countdown-unit"><span class="lutece-ds-countdown-number" data-countdown-unit="minutes">${minutes?string('00')}</span><span class="lutece-ds-countdown-name">#i18n{portal.theme.labelCountdownMinutes}</span></span>
<#if showSeconds><span class="lutece-ds-countdown-unit"><span class="lutece-ds-countdown-number" data-countdown-unit="seconds">${seconds?string('00')}</span><span class="lutece-ds-countdown-name">#i18n{portal.theme.labelCountdownSeconds}</span></span></#if>
</div>
<p class="lutece-ds-countdown-expired">${expiredLabel}</p>
</div>
<#if !(themeCountdownScriptLoaded!false)>
<#global themeCountdownScriptLoaded = true />
<script src="${commonsSharedThemePath}${commonsSiteJsPath}modules/theme-countdown.min.js?version=${commonsGlobalThemeVersion!}" defer></script>
</#if>
</#macro>
