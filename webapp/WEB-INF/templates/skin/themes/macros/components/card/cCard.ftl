<#--
Macro: cCard

Description: Generates a card component with optional image, title, subtitle, header, footer, and support for vertical or horizontal orientation.

Parameters:
- title (string, optional): The card title text. Default: ''.
- titleClass (string, optional): CSS class(es) for the card title. Default: ''.
- titleLevel (number, optional): Heading level for the title. Default: 3.
- titleUrl (string, optional): URL to make the title a link. Default: ''.
- titleUrlTitle (string, optional): Title attribute for the title link. Default: ''.
- subtitle (string, optional): Subtitle text for the card. Default: ''.
- subtitleClass (string, optional): CSS class(es) for the subtitle. Default: ''.
- subtitleLevel (number, optional): Heading level for the subtitle. Default: 4.
- class (string, optional): Additional CSS class(es) for the card container. Default: ''.
- id (string, optional): The unique identifier for the card. Default: ''.
- img (string, optional): Image source URL for the card. Default: ''.
- imgType (string, optional): Image type ('svg' for inline SVG). Default: ''.
- imgClass (string, optional): CSS class(es) for the image container. Default: ''.
- imgAlt (string, optional): Alt text for the card image. Default: ''.
- header (string, optional): Header text displayed above the image. Default: ''.
- headerLevel (number, optional): Heading level for the header. Default: 0.
- headerClass (string, optional): CSS class(es) for the header container. Default: ''.
- headerLabelClass (string, optional): CSS class(es) for the header text. Default: ''.
- headerImg (string, optional): Background image URL for the header. Default: ''.
- subHeader (string, optional): Sub-header text for the card. Default: ''.
- subHeaderClass (string, optional): CSS class(es) for the sub-header. Default: ''.
- footer (string, optional): Footer text for the card. Default: ''.
- footerClass (string, optional): CSS class(es) for the footer. Default: ''.
- orientation (string, optional): Card layout orientation ('v' for vertical, other for horizontal). Default: 'v'.
- vcolsInit (number, optional): Initial column size for smallest screens (0 disables responsive). Default: 12.
- vcols (object, optional): Column ratio between image and content for horizontal layout. Default: [4,8].
- hoverEffect (boolean, optional): If true, adds a hover effect on the card image. Default: true.
- params (string, optional): Additional HTML attributes for the card. Default: ''.

Showcase:
- desc: Carte - @cCard
- bs: components/card
- newFeature: false

Snippet:

    Basic vertical card with image:

    <@cCard title='City services' img='images/services.jpg' imgAlt='City services illustration'>
        <p>Discover all public services available in your neighborhood.</p>
    </@cCard>

    Horizontal card with linked title:

    <@cCard title='Latest news' titleUrl='jsp/site/Portal.jsp?page=news&id=42' img='images/news.jpg' imgAlt='News' orientation='h' vcols=[3,9]>
        <p>Read about recent updates to the portal.</p>
    </@cCard>

-->
<#macro cCard title='' titleClass='' titleLevel=3 titleUrl='' titleUrlTitle='' subtitle='' subtitleClass='' subtitleLevel=4 class='' id='' img='' imgType='' imgClass='' imgAlt='' header='' headerLevel=0 headerClass='' headerLabelClass='' headerImg='' subHeader='' subHeaderClass='' footer='' footerClass='' orientation='v' vcolsInit=12 vcols=[4,8] hoverEffect=true params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if orientation='v'>
<div class="card ${class!}"<#if id!=''> id="${id}"</#if><#if params!=''> ${params!}</#if>>
    <#if header!=''>
    <div class="card-header<#if headerClass !=''> ${headerClass}</#if><#if headerImg!=''> card-header-img</#if>"<#if headerImg!=''>style="background-image:url(${headerImg});"</#if>>
    <#if headerLabelClass!=''><#if headerLevel gt 0><@cTitle level=headerLevel class="${headerLabelClass!}">${header!}</@cTitle><#else><span class="${headerLabelClass!}">${header!}</span></#if></#if>
    </div>
    </#if>
    <#if subHeader!=''><div class="card-sub-header<#if subHeaderClass !=''> ${subHeaderClass}</#if>">${subHeader!}</div>
    </#if>
    <#if img!=''>
    <figure<#if hoverEffect> class="card-figure ${imgClass}"</#if>>
    <#if imgType !='svg'>
        <@cImg src=img! class='card-img-top' alt=imgAlt! />
    <#else>
        ${img!}
    </#if>
    </figure>
    </#if>
    <@cBlock class='card-body'>
        <#if title!=''><@cTitle level=titleLevel class='card-title ${titleClass}'><#if titleUrl!=''><a href="${titleUrl}" class="card-title-link"<#if titleUrlTitle!=''> title="${titleUrlTitle!}"</#if>></#if>${title}<#if titleUrl!=''></a></#if></@cTitle></#if>
        <#if subtitle!=''><@cTitle level=subtitleLevel class='card-subtitle mb-2 text-muted ${subtitleClass}'>${subtitle}</@cTitle></#if>
        <#nested>
    </@cBlock>
    <#if footer!=''><div class="card-footer text-muted<#if footerClass !=''> ${footerClass}</#if>">${footer!}</div></#if>
</div>
<#else>
<div class="card ${class!}"<#if id!=''> id="${id}"</#if><#if params!=''> ${params!}</#if>>
	<@cRow class='m-0'>
		<#if img!=''>
			<div class="<#if vcolsInit gt 0>col-${vcolsInit} col-md-${vcols[0]}<#else>col-${vcols[0]}</#if> ${imgClass} p-0">
			    <figure<#if hoverEffect> class="card-figure ${imgClass}"</#if>>
				    <#if imgType !='svg'>
				        <@cImg src=img! class='card-img-top' alt=imgAlt! />
				    <#else>
				        ${img!}
				    </#if>
			    </figure>
			</div>
		</#if>
		<div class="<#if img!=''><#if vcolsInit gt 0>col-${vcolsInit} col-md-${vcols[1]}<#else>col-${vcols[1]}</#if><#else>col</#if> p-0">
			<@cBlock  class='card-body'>
				<#if title!=''>
                    <@cTitle level=titleLevel class='card-title'>
                        <#if titleUrl!=''><a href="${titleUrl}" class="card-title-link"<#if titleUrlTitle!=''> title="${titleUrlTitle!}"</#if>></#if>${title}<#if titleUrl!=''></a></#if>
                    </@cTitle>
                </#if>
                    <#if subtitle!=''><@cTitle level=subtitleLevel class="card-subtitle mb-2 text-muted">${subtitle}</@cTitle></#if>
                    <#nested>
                <#if footer!=''><@cBlock class='card-footer text-muted'>${footer!}</@cBlock></#if>
			</@cBlock >
		</div>
	</@cRow>
</div>
</#if>    
</#macro>