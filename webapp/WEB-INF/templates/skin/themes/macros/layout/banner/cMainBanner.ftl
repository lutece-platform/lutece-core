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

Showcase:
- desc: "Bandeau principal - @cMainBanner"
- guide: page-accueil
- newFeature: false

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
<#macro cMainBanner title='${favourite!}' titleClass='' isInternal=hasBannerInternalStyle?boolean isFixed=isBannerFixed?boolean onlyHome=isBannerOnlyHome?boolean imageSrc=urlDefaultBannerImage!'' class='' id='main-banner' params=''  >
<#local params = params?is_markup_output?then(params?markup_string, params) />
<#local isOnlyHome=isBannerOnlyHome  />
<#-- TODO data n'est pas disponible dans ce contexte, à vérifier -->
<#local isHomePage=false />
<#if data??><#local isHomePage=data.homePage! /><#else><!-- NO DATA --></#if>
<#local hasInternalBanner><#if isHomePage>true<#else><#if !isOnlyHome?boolean>false<#else>true</#if></#if></#local>
<#local hasInternalBanner = hasInternalBanner?is_markup_output?then(hasInternalBanner?markup_string, hasInternalBanner) />
<#if hasInternalBanner?trim?boolean>
<#local titleStyle='' />
<#local imageStyle='' />
<#local params=params />
<#local bannerClass=class />
<#local dsTitle><#if dskey('portal.theme.site_property.banner.title')?starts_with('DS')><#else>${dskey('portal.theme.site_property.banner.title')}</#if></#local>
<#local dsTitle = dsTitle?is_markup_output?then(dsTitle?markup_string, dsTitle) />
<#if dsTitle?has_content><#local title=dsTitle /><#else><#local title=title /></#if>
<#local hasBannerTitle><#if !dskey('portal.theme.site_property.banner.title.checkbox')?starts_with('DS')&& dskey('portal.theme.site_property.banner.title.checkbox') == '1'>true<#else>false</#if></#local>
<#local hasBannerTitle = hasBannerTitle?is_markup_output?then(hasBannerTitle?markup_string, hasBannerTitle) />
<#local isBannerImage><#if !dskey('portal.theme.site_property.banner.showSiteImg.checkbox')?starts_with('DS') && dskey('portal.theme.site_property.banner.showSiteImg.checkbox') == '1'>true<#else>false</#if></#local>
<#local isBannerImage = isBannerImage?is_markup_output?then(isBannerImage?markup_string, isBannerImage) />
<#local hasBannerFormTitle>${dskey('portal.theme.site_property.bannerForm.showFormTitle.checkbox')}</#local>
<#local hasBannerFormTitle = hasBannerFormTitle?is_markup_output?then(hasBannerFormTitle?markup_string, hasBannerFormTitle) />
<#local isBannerFormImage><#if dskey('portal.theme.site_property.bannerForm.showBannerImg.checkbox') == '1'>true<#else>false</#if></#local>
<#local isBannerFormImage = isBannerFormImage?is_markup_output?then(isBannerFormImage?markup_string, isBannerFormImage) />
<#local bannerCredits=dskey('portal.theme.site_property.banner.credits')>
<#local bannerTitleColor=dskey('portal.theme.site_property.banner.title.color')>
<#local bannerTitleBGColor=dskey('portal.theme.site_property.banner.title.bgcolor')>
<#local bannerTitlePadding=dskey('portal.theme.site_property.banner.title.padding')>
<#local bannerBGImagePosY=dskey('portal.theme.site_property.banner.image.positiony')>
<#local bannerBGImagePosX=dskey('portal.theme.site_property.banner.image.positionx')>
<#if imageSrc?has_content && isBannerImage?boolean><#local bannerClass+='bg-banner' /></#if>
<#if imageSrc?has_content && isBannerImage?boolean><#local imageStyle +='background-image:url(${imageSrc!});' /></#if>
<#if imageSrc?has_content && bannerBGImagePosY?has_content><#local imageStyle +='background-position-y: ${bannerBGImagePosY!};' /></#if>
<#if imageSrc?has_content && bannerBGImagePosX?has_content><#local imageStyle +='background-position-x: ${bannerBGImagePosX!};' /></#if>
<#if bannerTitleColor?has_content><#local titleStyle ='color:${bannerTitleColor};' /></#if>
<#if bannerTitleBGColor?has_content><#local titleStyle +='background-color:${bannerTitleBGColor};' /></#if>
<#if bannerTitlePadding?has_content><#local titleStyle +='padding:${bannerTitlePadding};' /></#if>
<#if imageStyle?has_content ><#local params +='style="${imageStyle!}"' /></#if>
<#if isInternal && !isHomePage><#local bannerClass +=' internal' /></#if>
<#if isFixed><div class="banner-wrapper is-fixed<#if bannerClass?has_content > ${bannerClass!}</#if>"></#if>
<div class="banner<#if bannerClass?has_content > ${bannerClass!}</#if><#if bannerCredits?has_content > credits</#if> page-${page_id!}"<#if id?has_content > id="${id!}"</#if><#if bannerCredits?has_content > data-credits="${bannerCredits!}"</#if><#if params?has_content> ${params}</#if> >
<#if hasBannerTitle?boolean && title?trim?has_content><h1 id="main-banner-title"<#if titleStyle?has_content> style="${titleStyle}"</#if><#if titleClass?has_content> class="${titleClass}"</#if>>${title}</h1></#if>
<#nested> 
</div> 
<#if isFixed></div></#if>
</#if>
</#macro>