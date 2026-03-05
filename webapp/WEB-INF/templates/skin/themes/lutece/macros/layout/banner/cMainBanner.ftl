<#--
Macro: cMainBanner

Description: Generates a main banner section for the site, with optional background image, title, and home-only display mode.

Parameters:
- title (string, required): Banner title text. Default: '${favourite!}'.
- titleClass (string, optional): CSS class added to the banner title. Default: ''.
- onlyHome (boolean, required): If true, the banner is only displayed on the home page. Default: isBannerOnlyHome.
- imageSrc (string, required): Path to a background image covering the full banner area. Default: urlDefaultBannerImage.
- class (string, optional): Additional CSS class(es) for the banner. Default: ''.
- id (string, optional): Unique identifier for the banner element. Default: 'main-banner'.
- params (string, optional): Additional HTML attributes for the banner element. Default: ''.

Snippet:

    Basic usage:

    <@cMainBanner title='My Portal'>
        <p>Welcome to our portal</p>
    </@cMainBanner>

    Banner with background image, displayed only on home page:

    <@cMainBanner title='City Services' titleClass='text-white' onlyHome=true imageSrc='images/banner.jpg' class='banner-lg'>
        <p>Discover our digital services</p>
    </@cMainBanner>

-->
<#macro cMainBanner title='${favourite!}' titleClass='' onlyHome=isBannerOnlyHome imageSrc=urlDefaultBannerImage class='' id='main-banner' params=''  >
<#local onlyHome=isBannerOnlyHome!onlyHome />
<#local titleStyle='' />
<#local imageStyle='' />
<#local params=params />
<#local bannerClass=class />
<#local isBannerImage><#if dskey('theme.site_property.bannerForm.showBannerImg.checkbox') == '1'>true<#else>false</#if></#local>
<#local hasBannerTitle>${dskey('theme.site_property.bannerForm.showFormTitle.checkbox')}</#local>
<#local bannerCredits=dskey('theme.site_property.banner.credits')>
<#local bannerTitleColor=dskey('theme.site_property.banner.title.color')>
<#local bannerTitleBGColor=dskey('theme.site_property.banner.title.bgcolor')>
<#local bannerTitlePadding=dskey('theme.site_property.banner.title.padding')>
<#local bannerBGImagePosY=dskey('theme.site_property.banner.image.positiony')>
<#if imageSrc !='' && isBannerImage?boolean><#local bannerClass+='bg-banner' /></#if>
<#if imageSrc !='' && isBannerImage?boolean><#local imageStyle +='background-image:url(${imageSrc!});' /></#if>
<#if imageSrc !='' && bannerBGImagePosY !=''><#local imageStyle +='background-position-y: ${bannerBGImagePosY!};' /></#if>
<#if bannerTitleColor !=''><#local titleStyle ='color:${bannerTitleColor};' /></#if>
<#if bannerTitleBGColor !=''><#local titleStyle +='background-color:${bannerTitleBGColor};' /></#if>
<#if bannerTitlePadding !=''><#local titleStyle +='padding:${bannerTitlePadding};' /></#if>
<#if imageStyle !='' ><#local params +='style="${imageStyle!}"' /></#if>
<#if page_id??><#if onlyHome><#if page_id = 1><#else><#return></#if><#else><#if page_id gt 1><#local bannerClass +=' internal' /> </#if></#if></#if>
<div class="banner<#if bannerClass !='' > ${bannerClass!}</#if><#if bannerCredits !='' > credits</#if> page-${page_id!}"<#if id !='' > id="${id!}"</#if><#if bannerCredits !='' > data-credits="${bannerCredits!}"</#if><#if params!=''> ${params}</#if> >
<#if hasBannerTitle?number=1 && title?trim !=''><h1<#if titleStyle!=''> style="${titleStyle}"</#if><#if titleClass!=''> class="${titleClass}"</#if>>${title}</h1></#if>
<#nested>
</div>
</#macro>