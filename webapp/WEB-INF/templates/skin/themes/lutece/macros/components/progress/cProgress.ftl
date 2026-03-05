<#--
Macro: cProgress

Description: Generates a Bootstrap progress bar with optional Lutece progress manager integration for tracking long-running operations.

Parameters:
- label (string, required): Accessible label displayed above the progress bar.
- labelClass (string, optional): CSS class for the label element. Default: ''.
- class (string, optional): Additional CSS classes for the progress container. Default: ''.
- color (string, optional): Bootstrap color for the progress bar. Default: 'primary'.
- id (string, optional): HTML id attribute for the container. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.
- value (number, optional): Current progress value. Default: 0.
- min (number, optional): Minimum value. Default: 0.
- max (number, optional): Maximum value. Default: 100.
- text (string, optional): Custom text displayed inside the progress bar. Default: ''.
- progressId (string, optional): HTML id for the progress bar element. Default: 'progressbar'.
- token (string, optional): Lutece progress manager feed token for live updates. Default: ''.
- role (string, optional): ARIA role attribute. Default: 'progressbar'.
- showReport (boolean, optional): Whether to display a progress report below the bar (requires token). Default: false.
- intervalTime (number, optional): Refresh interval in milliseconds for progress updates (requires token). Default: 2000.

Snippet:

    Basic progress bar:

    <@cProgress label='Upload progress' value=45 />

    Progress bar with custom max and color:

    <@cProgress label='Steps completed' value=3 max=10 color='success' />

    Progress bar with Lutece progress manager:

    <@cProgress label='Import in progress' token=progressToken showReport=true intervalTime=3000 />

-->  
<#macro cProgress label labelClass='' class='' color='primary' id='' params='' value=0 min=0 max=100 text='' progressId='progressbar' token='' role='progressbar' showReport=false intervalTime=2000  deprecated...>
<@deprecatedWarning args=deprecated />
<#if max?number != 100><#assign progPercent=( (value?number / max?number ) * 100) /><#else><#assign progPercent=value /></#if>
<p id="${progressId}-label" class="label-progress<#if labelClass!=''> ${labelClass}</#if>">${label}</p>
<#if role='progressbar'>
<div class="progress<#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
    <div id="${progressId}" aria-labelledby="${progressId}-label" class="progress-bar bg-${color}<#if token!=''> progressmanager</#if>" <#if role!=''>role="${role}"</#if> style="width:${progPercent?replace(',','.')}%;" <#if role='progressbar'>aria-valuenow="${value}"  aria-valuemin="${min}" aria-valuemax="${max}"</#if> <#if token!=''>token="${token}" intervalTime=${intervalTime} showReport=${showReport?c}</#if> >
        <#if text=''>${progPercent}%<#else>${text}</#if>
    </div>
</div>
<#else>
<div class="progress<#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
    <p id="${progressId}" aria-hidden="true" class="progress-bar bg-${color}<#if token!=''> progressmanager</#if>" style="width:${progPercent?replace(',','.')}%;">
        <#if text=''>${progPercent}%<#else>${text}</#if>
    </p>
</div>
</#if>
<#if showReport ><p id="${progressId}-report" class="progress-bar-report" lastline=0></p></#if>
</#macro>