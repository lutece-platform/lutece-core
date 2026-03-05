<#--
Macro: cStatus

Description: Generates a status tag badge indicating the current state of a process or event on skin pages.

Parameters:
- level (string, optional): Status level key ('forthcoming', 'tobecompleted', 'inprogress', 'over', 'bygone'). Default: 'forthcoming'.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- label (string, optional): Custom label text (overrides i18n default). Default: ''.
- labelClass (string, optional): CSS class for the label span. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Snippet:

    Basic forthcoming status:

    <@cStatus level='forthcoming' />

    In-progress status with custom label:

    <@cStatus level='inprogress' label='In progress' class='mt-2' />

    Completed status:

    <@cStatus level='over' label='Completed' labelClass='text-success' />

-->  
<#macro cStatus level='forthcoming' class='' id='' label='' labelClass='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="status ${level}<#if class !=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
<#if level='tobecompleted'><svg width="10" height="10" viewBox="0 0 10 10" fill="none" xmlns="http://www.w3.org/2000/svg"><circle cx="5" cy="5" r="5" fill="currentColor"/></svg></#if>
<span class="status-label fw-bold<#if labelClass !=''> ${labelClass}</#if>"><#if label!=''>${label!}<#else>#i18n{portal.theme.status.level.${level}.label}</#if></span>
<#nested>
</div>
</#macro>