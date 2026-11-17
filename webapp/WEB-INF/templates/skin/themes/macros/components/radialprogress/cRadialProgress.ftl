<#--
Macro: cRadialProgress

Description: Generates a circular progress ring with its percentage in the centre, exposed to screen readers as a progressbar.

Parameters:
- value (number, required): Progress, from 0 to max. Default: 0.
- max (number, optional): Value of a complete ring. Default: 100.
- label (string, required): Accessible name of the progress bar, for example 'Avancement des travaux'. Default: ''.
- showLabel (boolean, optional): Displays the label next to the ring. Default: false.
- size (string, optional): '', 'sm' or 'lg'. Default: ''.
- color (string, optional): '' (secondary), 'success' or 'danger'. Default: ''.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Progression circulaire - @cRadialProgress
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Progress of a project:

    <@cRadialProgress value=72 label='Avancement des travaux' showLabel=true />

-->
<#macro cRadialProgress value=0 max=100 label='' showLabel=false size='' color='' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local progressValue = [ [ value, 0 ]?max, max ]?min />
<#local percent = ( progressValue / max * 100 )?round />
<#if showLabel><div class="lutece-ds-radial-progress-wrapper"></#if>
<div class="lutece-ds-radial-progress<#if size!=''> lutece-ds-radial-progress-${size}</#if><#if color!=''> lutece-ds-radial-progress-${color}</#if><#if class!=''> ${class}</#if>" role="progressbar" aria-valuenow="${progressValue?c}" aria-valuemin="0" aria-valuemax="${max?c}" aria-valuetext="${percent?c} %"<#if label!=''> aria-label="${label}"</#if> style="--radial-value: ${percent?c}"<@commonAttrs id=id params=params />>
<span class="lutece-ds-radial-progress-value">${percent?c}&nbsp;%</span>
</div>
<#if showLabel><p class="lutece-ds-radial-progress-label" aria-hidden="true">${label}</p></div></#if>
</#macro>
