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
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

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
<#macro cMainBanner title='${favourite!}' titleClass='' isInternal=hasBannerInternalStyle?boolean isFixed=isBannerFixed?boolean onlyHome=isBannerOnlyHome?boolean imageSrc=urlDefaultBannerImage!'' class='' id='main-banner' params={}  >
<#local isOnlyHome=isBannerOnlyHome  />
<#-- TODO data n'est pas disponible dans ce contexte, à vérifier -->
<#local isHomePage=false />
<#if data??><#local isHomePage=data.homePage! /><#else><!-- NO DATA --></#if>
<#local hasInternalBanner = isHomePage || isOnlyHome?boolean />
<#if hasInternalBanner>
<#local titleStyle='' />
<#local imageStyle='' />
<#local bannerClass=class />
<#local dsTitle = ( dskey('portal.theme.site_property.banner.title')?starts_with('DS') )?then( '', ( dskey('portal.theme.site_property.banner.title') )?string ) />
<#-- the banner title of the site properties wins over the title parameter -->
<#local bannerTitle = dsTitle?has_content?then( dsTitle, title ) />
<#local hasBannerTitle = ( !dskey('portal.theme.site_property.banner.title.checkbox')?starts_with('DS')&& dskey('portal.theme.site_property.banner.title.checkbox') == '1' )?then( 'true', 'false' ) />
<#local isBannerImage = ( !dskey('portal.theme.site_property.banner.showSiteImg.checkbox')?starts_with('DS') && dskey('portal.theme.site_property.banner.showSiteImg.checkbox') == '1' )?then( 'true', 'false' ) />
<#local hasBannerFormTitle = ( dskey('portal.theme.site_property.bannerForm.showFormTitle.checkbox') )?string />
<#local isBannerFormImage = ( dskey('portal.theme.site_property.bannerForm.showBannerImg.checkbox') == '1' )?then( 'true', 'false' ) />
<#local bannerCredits=dskey('portal.theme.site_property.banner.credits')>
<#local bannerTitleColor=dskey('portal.theme.site_property.banner.title.color')?trim?matches( '#[0-9a-fA-F]{3,8}|[a-zA-Z]+|(rgb|rgba|hsl|hsla)\\([0-9., %]+\\)' )?then( dskey('portal.theme.site_property.banner.title.color')?trim, '' )>
<#local bannerTitleBGColor=dskey('portal.theme.site_property.banner.title.bgcolor')?trim?matches( '#[0-9a-fA-F]{3,8}|[a-zA-Z]+|(rgb|rgba|hsl|hsla)\\([0-9., %]+\\)' )?then( dskey('portal.theme.site_property.banner.title.bgcolor')?trim, '' )>
<#local bannerTitlePadding=dskey('portal.theme.site_property.banner.title.padding')?trim?matches( '([0-9.]+(px|rem|em|%)?\\s*){1,4}' )?then( dskey('portal.theme.site_property.banner.title.padding')?trim, '' )>
<#local bannerBGImagePosY=dskey('portal.theme.site_property.banner.image.positiony')?trim?matches( '[0-9.]+(%|px|rem|em)?|top|bottom|left|right|center' )?then( dskey('portal.theme.site_property.banner.image.positiony')?trim, '' )>
<#local bannerBGImagePosX=dskey('portal.theme.site_property.banner.image.positionx')?trim?matches( '[0-9.]+(%|px|rem|em)?|top|bottom|left|right|center' )?then( dskey('portal.theme.site_property.banner.image.positionx')?trim, '' )>
<#if imageSrc?has_content && isBannerImage?boolean><#local bannerClass+=' bg-banner' /></#if>
<#if cssUrl( imageSrc! )?has_content && isBannerImage?boolean><#local imageStyle +='background-image:url(\'' + cssUrl( imageSrc ) + '\');' /></#if>
<#if imageSrc?has_content && bannerBGImagePosY?has_content><#local imageStyle +='background-position-y: ${bannerBGImagePosY!};' /></#if>
<#if imageSrc?has_content && bannerBGImagePosX?has_content><#local imageStyle +='background-position-x: ${bannerBGImagePosX!};' /></#if>
<#if bannerTitleColor?has_content><#local titleStyle ='color:${bannerTitleColor};' /></#if>
<#if bannerTitleBGColor?has_content><#local titleStyle +='background-color:${bannerTitleBGColor};' /></#if>
<#if bannerTitlePadding?has_content><#local titleStyle +='padding:${bannerTitlePadding};' /></#if>
<#local bannerAttrs = imageStyle?has_content?then( { 'style': imageStyle }, { } ) />
<#if isInternal && !isHomePage><#local bannerClass +=' internal' /></#if>
<#if isFixed><div class="banner-wrapper is-fixed<#if bannerClass?has_content > ${bannerClass!}</#if>"></#if>
<div class="banner<#if bannerClass?has_content > ${bannerClass!}</#if><#if bannerCredits?has_content > credits</#if> page-${page_id!}"<#if id?has_content > id="${id!}"</#if><#if bannerCredits?has_content > data-credits="${bannerCredits!}"</#if><@commonAttrs macroParams=bannerAttrs params=params /> >
<#if hasBannerTitle?boolean && bannerTitle?trim !=''><h1 id="main-banner-title"<#if titleStyle?has_content> style="${titleStyle}"</#if><#if titleClass?has_content> class="${titleClass}"</#if>>${bannerTitle}</h1></#if>
<#nested> 
</div> 
<#if isFixed></div></#if>
</#if>
</#macro>