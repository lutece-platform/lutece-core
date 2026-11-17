<#--
Macro: cTimelineItem

Description: Generates an event of a cTimeline: a date, a title and an optional content (nested).

Parameters:
- date (string, optional): Date displayed. Default: ''.
- datetime (string, optional): Machine-readable date of the time element (yyyy, yyyy-MM or yyyy-MM-dd). Without it the date is a plain text. Default: ''.
- title (string, optional): Title of the event. Default: ''.
- titleLevel (number, optional): Heading level of the title. 0 makes it a paragraph. Default: 3.
- status (string, optional): '', 'done', 'current' or 'upcoming'. The current event carries aria-current="step". Default: ''.
- statusLabel (string, optional): Text read by screen readers after the title to give the status, for example 'étape terminée'. Default: ''.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Événement de frise chronologique - @cTimelineItem
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Event with a content:

    <@cTimelineItem date='12 mai 2026' datetime='2026-05-12' title='Demande reçue' status='done' statusLabel='étape terminée'>
        <@cText>Votre demande a été enregistrée.</@cText>
    </@cTimelineItem>

-->
<#macro cTimelineItem date='' datetime='' title='' titleLevel=3 status='' statusLabel='' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<li class="lutece-ds-timeline-item<#if status!=''> ${status}</#if><#if class!=''> ${class}</#if>"<#if status=='current'> aria-current="step"</#if><@commonAttrs id=id params=params />>
<span class="lutece-ds-timeline-marker" aria-hidden="true"></span>
<#if date!=''><#if datetime!=''><time class="lutece-ds-timeline-date" datetime="${datetime}">${date}</time><#else><span class="lutece-ds-timeline-date">${date}</span></#if></#if>
<#if title!=''><#if titleLevel gt 0><h${titleLevel} class="lutece-ds-timeline-title"><#else><p class="lutece-ds-timeline-title"></#if>${title}<#if statusLabel!=''><span class="visually-hidden"> (${statusLabel})</span></#if><#if titleLevel gt 0></h${titleLevel}><#else></p></#if></#if>
<#local content><#nested></#local>
<#if content?has_content><div class="lutece-ds-timeline-content">${content}</div></#if>
</li>
</#macro>
