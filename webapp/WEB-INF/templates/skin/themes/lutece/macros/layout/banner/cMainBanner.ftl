<#-- Macro: cMainBanner

Description: affiche une bannière.

Parameters:
@param - id - string - required - identifiant unique de la bannière - par défaut 'main-banner'
@param - class - string - optional - classe(s) css de la bannière  - par défaut vide
@param - title - string - required - titre de la bannière - par défaut variable 'favourite', nom du site
@param - title Class- string - required - Classe CSS à ajouter au titre de la bannière - par défaut vide
@param - onlyHome - boolean - required - si true, le site affiche la bannière que sur la première page - par défaut false défnie par la variable 'isBannerOnlyHome'
@param - imageSrc - string - required - Chemin vers une image. Attention cette image sera placée en fond et couvrira la totalité de taille de la bannière  - par défaut vide, définie par la variable 'urlDefaultBannerImage'
@param - params - string - optional - permet d'ajouter des paramètres HTML à la bannière
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