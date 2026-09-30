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
<#local isOnlyHome=isBannerOnlyHome  />
<#-- TODO data n'est pas disponible dans ce contexte, à vérifier -->
<#local isHomePage=false />
<#if data??><#local isHomePage=data.homePage! /><#else><!-- NO DATA --></#if>
<#local hasInternalBanner><#if isHomePage>true<#else><#if !isOnlyHome?boolean>false<#else>true</#if></#if></#local>
<#if hasInternalBanner?trim?boolean>
<#local titleStyle='' />
<#local imageStyle='' />
<#local params=params />
<#local bannerClass=class />
<#local dsTitle><#if dskey('portal.theme.site_property.banner.title')?starts_with('DS')><#else>${dskey('portal.theme.site_property.banner.title')}</#if></#local>
<#if dsTitle !=''><#local title=dsTitle /><#else><#local title=title /></#if>
<#local hasBannerTitle><#if !dskey('portal.theme.site_property.banner.title.checkbox')?starts_with('DS')&& dskey('portal.theme.site_property.banner.title.checkbox') == '1'>true<#else>false</#if></#local>
<#local isBannerImage><#if !dskey('portal.theme.site_property.banner.showSiteImg.checkbox')?starts_with('DS') && dskey('portal.theme.site_property.banner.showSiteImg.checkbox') == '1'>true<#else>false</#if></#local>
<#local hasBannerFormTitle>${dskey('portal.theme.site_property.bannerForm.showFormTitle.checkbox')}</#local>
<#local isBannerFormImage><#if dskey('portal.theme.site_property.bannerForm.showBannerImg.checkbox') == '1'>true<#else>false</#if></#local>
<#local bannerCredits=dskey('portal.theme.site_property.banner.credits')>
<#local bannerTitleColor=dskey('portal.theme.site_property.banner.title.color')>
<#local bannerTitleBGColor=dskey('portal.theme.site_property.banner.title.bgcolor')>
<#local bannerTitlePadding=dskey('portal.theme.site_property.banner.title.padding')>
<#local bannerBGImagePosY=dskey('portal.theme.site_property.banner.image.positiony')>
<#local bannerBGImagePosX=dskey('portal.theme.site_property.banner.image.positionx')>
<#if imageSrc !='' && isBannerImage?boolean><#local bannerClass+='bg-banner' /></#if>
<#if imageSrc !='' && isBannerImage?boolean><#local imageStyle +='background-image:url(${imageSrc!});' /></#if>
<#if imageSrc !='' && bannerBGImagePosY !=''><#local imageStyle +='background-position-y: ${bannerBGImagePosY!};' /></#if>
<#if imageSrc !='' && bannerBGImagePosX !=''><#local imageStyle +='background-position-x: ${bannerBGImagePosX!};' /></#if>
<#if bannerTitleColor !=''><#local titleStyle ='color:${bannerTitleColor};' /></#if>
<#if bannerTitleBGColor !=''><#local titleStyle +='background-color:${bannerTitleBGColor};' /></#if>
<#if bannerTitlePadding !=''><#local titleStyle +='padding:${bannerTitlePadding};' /></#if>
<#if imageStyle !='' ><#local params +='style="${imageStyle!}"' /></#if>
<#if isInternal && !isHomePage><#local bannerClass +=' internal' /></#if>
<#if isFixed><div class="banner-wrapper is-fixed<#if bannerClass !='' > ${bannerClass!}</#if>"></#if>
<div class="banner<#if bannerClass !='' > ${bannerClass!}</#if><#if bannerCredits !='' > credits</#if> page-${page_id!}"<#if id !='' > id="${id!}"</#if><#if bannerCredits !='' > data-credits="${bannerCredits!}"</#if><#if params!=''> ${params}</#if> >
<#if hasBannerTitle?boolean && title?trim !=''><h1 id="main-banner-title"<#if titleStyle!=''> style="${titleStyle}"</#if><#if titleClass!=''> class="${titleClass}"</#if>>${title}</h1></#if>
<#nested> 
</div> 
<#if isFixed></div></#if>
</#if>
</#macro>