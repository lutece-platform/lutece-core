<#--
Macro: cIndicator

Description: Places a count or a dot on the top corner of an element (nested), for example the unread notifications on a button. The text of the indicator is read by screen readers after the element: when the element is a button or a link, also write the count in its own accessible name.

Parameters:
- value (string, optional): Text of the indicator, usually a number. Empty: a dot. Default: ''.
- label (string, optional): Text read by screen readers after the value, for example 'notifications non lues'. Default: ''.
- color (string, optional): '' (danger red), 'primary' or 'success'. Default: ''.
- position (string, optional): 'top' or 'bottom' (end corner). Default: 'top'.
- class (string, optional): Additional CSS classes of the indicator. Default: ''.
- id (string, optional): HTML id attribute of the indicator. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Indicateur - @cIndicator
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Count on a button:

    <@cIndicator value='3' label='notifications non lues' id='notif-count'>
        <@cBtn label='Notifications' class='secondary' params={"aria-describedby": "notif-count"} />
    </@cIndicator>

    Dot on an avatar:

    <@cIndicator color='success' position='bottom' label='en ligne'><@cAvatar name='Anne Martin' decorative=false /></@cIndicator>

-->
<#macro cIndicator value='' label='' color='' position='top' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<span class="lutece-ds-indicator">
<#nested>
<span class="lutece-ds-indicator-badge<#if value==''> lutece-ds-indicator-dot</#if><#if color!=''> lutece-ds-indicator-${color}</#if><#if position=='bottom'> lutece-ds-indicator-bottom</#if><#if class!=''> ${class}</#if>"<@commonAttrs id=id params=params />><#if value!=''><span aria-hidden="true">${value}</span></#if><#if label!='' || value!=''><span class="visually-hidden">${value}<#if label!=''> ${label}</#if></span></#if></span>
</span>
</#macro>
