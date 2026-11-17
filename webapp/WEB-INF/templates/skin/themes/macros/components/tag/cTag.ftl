<#--
Macro: cTag

Description: Generates a tag (chip): a short label, optionally a link, optionally removable. A removable tag carries a link to removeUrl (the page without this filter) or, without removeUrl, a button that removes the tag from the page and moves the focus to the next tag.

Parameters:
- label (string, required): Text of the tag. Default: ''.
- href (string, optional): Makes the whole tag a link. Ignored when the tag is removable. Default: ''.
- removable (boolean, optional): Displays a remove control. Default: false, true when removeUrl is set.
- removeUrl (string, optional): Link of the remove control, typically the current page without this filter. Default: ''.
- removeLabel (string, optional): Accessible label of the remove control, followed by the tag label. Default: '#i18n{portal.theme.labelDelete}'.
- color (string, optional): '' for the light tag, 'primary' for the solid one. Default: ''.
- size (string, optional): '' or 'sm'. Default: ''.
- listItem (boolean, optional): Wraps the tag in a li, to use inside cTagGroup. Default: false.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Étiquette - @cTag
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Simple tag:

    <@cTag label='Culture' />

    Tag as a link:

    <@cTag label='Culture' href='jsp/site/Portal.jsp?page=search&tag=culture' />

    Solid removable tag, removed in the page:

    <@cTag label='Paris 11e' color='primary' removable=true />

-->
<#macro cTag label='' href='' removable=false removeUrl='' removeLabel='#i18n{portal.theme.labelDelete}' color='' size='' listItem=false class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local isRemovable = removable || removeUrl != '' />
<#local tagClass>lutece-ds-tag<#if color!=''> lutece-ds-tag-${color}</#if><#if size!=''> lutece-ds-tag-${size}</#if><#if class!=''> ${class}</#if></#local>
<#local tagAttrs><@commonAttrs id=id params=params /></#local>
<#if listItem><li></#if>
<#if href!='' && !isRemovable>
<a class="${tagClass}" href="${href}"${tagAttrs}><span class="lutece-ds-tag-label">${label}</span><#nested></a>
<#else>
<span class="${tagClass}"${tagAttrs}><span class="lutece-ds-tag-label">${label}</span><#nested><#if isRemovable><#if removeUrl!=''><a class="btn-close lutece-ds-tag-remove" href="${removeUrl}" aria-label="${removeLabel} : ${label}"></a><#else><button type="button" class="btn-close lutece-ds-tag-remove" aria-label="${removeLabel} : ${label}" data-lutece-action="remove-tag"></button></#if></#if></span>
</#if>
<#if listItem></li></#if>
</#macro>
