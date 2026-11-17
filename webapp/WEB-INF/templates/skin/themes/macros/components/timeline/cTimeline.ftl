<#--
Macro: cTimeline

Description: Generates a vertical timeline: an ordered list of dated events (cTimelineItem), for example the stages of a project or the history of a request.

Parameters:
- label (string, optional): Accessible name of the list, read by screen readers (aria-label). Default: ''.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Frise chronologique - @cTimeline
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Stages of a project:

    <@cTimeline label='Étapes du projet'>
        <@cTimelineItem date='Mars 2025' datetime='2025-03' title='Concertation' status='done' />
        <@cTimelineItem date='Juin 2026' datetime='2026-06' title='Travaux' status='current'>
            <@cText>Les travaux de voirie ont commencé.</@cText>
        </@cTimelineItem>
        <@cTimelineItem date='2027' datetime='2027' title='Livraison' status='upcoming' />
    </@cTimeline>

-->
<#macro cTimeline label='' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<ol class="lutece-ds-timeline<#if class!=''> ${class}</#if>"<#if label!=''> aria-label="${label}"</#if><@commonAttrs id=id params=params />>
<#nested>
</ol>
</#macro>
