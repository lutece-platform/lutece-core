<#--
Macro: cSpinner

Description: Generates a loading indicator announced to screen readers (role status): a spinning ring, a growing dot or three dots, with its label visible or only read.

Parameters:
- label (string, optional): Text announced, and displayed when showLabel is true. Default: '#i18n{portal.theme.labelLoading}'.
- type (string, optional): 'border' (ring), 'grow' (dot) or 'dots'. Default: 'border'.
- size (string, optional): '', 'sm' or 'lg'. Default: ''.
- showLabel (boolean, optional): Displays the label next to the spinner. Default: false.
- vertical (boolean, optional): Label under the spinner. Default: false.
- block (boolean, optional): Centres the spinner in its container, for a block being loaded. Default: false.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Chargement - @cSpinner
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Spinner, label read only:

    <@cSpinner />

    Block being loaded, label displayed:

    <@cSpinner type='dots' showLabel=true block=true label='Chargement des résultats' />

-->
<#macro cSpinner label='#i18n{portal.theme.labelLoading}' type='border' size='' showLabel=false vertical=false block=false class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<div class="lutece-ds-spinner<#if vertical> lutece-ds-spinner-vertical</#if><#if block> lutece-ds-spinner-block</#if><#if class!=''> ${class}</#if>" role="status"<@commonAttrs id=id params=params />>
<#if type=='dots'>
<span class="lutece-ds-spinner-dots" aria-hidden="true"<#if size=='sm'> style="font-size:.75em"<#elseif size=='lg'> style="font-size:1.5em"</#if>><span></span><span></span><span></span></span>
<#else>
<span class="spinner-${type}<#if size=='sm'> spinner-${type}-sm<#elseif size=='lg'> lutece-ds-spinner-lg</#if>" aria-hidden="true"></span>
</#if>
<span<#if !showLabel> class="visually-hidden"</#if>>${label}</span>
</div>
</#macro>
