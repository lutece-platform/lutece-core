<#--
Macro: cImg

Description: Generates an HTML img element with responsive styling and optional accessible description. Supports aria-describedby for enhanced accessibility on skin pages.

Parameters:
- src (string, required): Source URL of the image file.
- alt (string, optional): Alternative text for the image. Default: ''.
- id (string, optional): Unique identifier for the image element. Default: ''.
- class (string, optional): CSS class(es) applied to the image element. Default: 'img-fluid'.
- labelDescribedBy (string, optional): Descriptive text added via aria-describedby for accessibility. Default: ''.
- showLabelDescribedBy (boolean, optional): If true, displays the labelDescribedBy text visually; otherwise it is hidden. Default: false.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: 'Image - @cImg'
- bs: content/images
- newFeature: false
- updatedFeature: true

Snippet:

    Basic usage:

    <@cImg src='images/photo.jpg' alt='A beautiful landscape' />

    With accessible description:

    <@cImg src='images/chart.png' alt='Annual statistics chart' labelDescribedBy='This chart shows the yearly growth in public services usage.' showLabelDescribedBy=true />

-->
<#macro cImg src alt='' id='' class='img-fluid' labelDescribedBy='' showLabelDescribedBy=false params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local localId = ( id?has_content )?then( ( id )?string, ( alt?js_string?lower_case?replace(' ','_') )?string ) /> 
<img src="${src!}" alt="${alt!}" class="<#if class?has_content>${class!}</#if>"<#if labelDescribedBy?has_content> aria-descridedby="descridedby_${localId!}"</#if><#if localId?has_content> id="${localId!}"</#if><@commonAttrs params=params />>
<#if labelDescribedBy?has_content><p id="descridedby_${localId!}"<#if !showLabelDescribedBy> class="visually-hidden"</#if>>${labelDescribedBy!}</p></#if>
</#macro>