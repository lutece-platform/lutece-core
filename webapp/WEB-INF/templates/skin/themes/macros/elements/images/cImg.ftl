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
- params (string, optional): Additional HTML attributes for the image element. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

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
<#macro cImg src alt='' id='' class='img-fluid' labelDescribedBy='' showLabelDescribedBy=false params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local localId><#if id !=''>${id}<#else>${alt?js_string?lower_case?replace(' ','_')}</#if></#local> 
<img src="${src!}" alt="${alt!}" class="<#if class!=''>${class!}</#if>"<#if labelDescribedBy !=''> aria-descridedby="descridedby_${localId!}"</#if><#if localId!=''> id="${localId!}"</#if><@commonAttrs params=params attrs=attrs />>
<#if labelDescribedBy !=''><p id="descridedby_${localId!}"<#if !showLabelDescribedBy> class="visually-hidden"</#if>>${labelDescribedBy!}</p></#if>
</#macro>