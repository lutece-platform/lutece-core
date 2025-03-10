<#-- Macro: cCard

Description: affiche une carte pouvant comprendre une image et du texte.

Parameters:

@param - id - string - optional - l'ID de la carte
@param - title - string - optional - définit le titre de la carte
@param - titleClass - string - optional - permet d'ajouter une classe CSS au titre de la carte
@param - titleLevel - string - optional - défaut 3, permet de modifier le niveau du titre de la carte si besoin, pour respecter les normes accessibliité
@param - titleUrl - string - optional - permet d'ajouter un lien au titre de la carte
@param - titleUrlTitle - string - optional - permet d'ajouter un lien un attribut "title" au lien sur le titre de la carte
@param - subtitle - string - optional - permet d'ajouter un sous-titre à la carte
@param - subtitleClass - string - optional - permet d'ajouter une classe CSS au sous-titre à la carte
@param - subtitleLevel - string - optional - défaut 4, permet de modifier le niveau du sous titre de la carte si besoin, pour respecter les normes accessibliité
@param - class - string - optional - permet d'ajouter une classe CSS à la carte
@param - img - string - optional - permet de définir la source de l'image de la carte
@param - imgType - string - optional - permet de définir le type de l'image de la carte (valeur possible: 'svg')
@param - imgClass - string - optional - permet d'ajouter une classe CSS à la source de l'image de la carte
@param - imgClass - string - optional - permet de définir la valeur de l'attribut alt de la carte
@param - header - string - optional - permet de définir un text au dessus de l'image de la carte
@param - headerClass - string - optional - permet d'ajouter une classe CSS au container du header de la carte
@param - headerLabelClass - string - optional - permet d'ajouter une classe CSS au texte du header de la carte
@param - headerImg - string - optional - permet d'ajouter une image au header de la carte
@param - subHeader - string - optional - permet d'ajouter un sous header à la carte
@param - subHeaderClass - string - optional - permet d'ajouter une classe CSS au sous header de la carte
@param - footer - string - optional - permet d'ajouter un footer à la carte
@param - footerClass - string - optional - permet d'ajouter une classe CSS au footer de la carte
@param - orientation - string - optional - permet de gérer l'orientation de la carte (valeur possible: 'v')
@param - vcolsInit - number - optional - Par défault égal à 12, taille par défaut pour l'écran le plus petit de l'image et du contenu de la carte (12 = toute la largeur). Si la valeur est 0, la taille de l'image et du contenu de la carte ne seront pas modifié selon la media query -taille de l'écran- mais conservea les même largeurs définies par le paramètre vcols.
@param - vcols - objet - optional - permet de gérer le ration de largeur entre l'image et le texte dans une orientation horizontale (par défaut: [4,8])
@param - hoverEffect - boolean - optional - permet d'ajouter un effet d'hover à l'image de la carte (par défaut: true)
@param - params - string - optional - permet d'ajouter des parametres HTML à la carte
 -->
<#macro cCard title='' titleClass='' titleLevel=3 titleUrl='' titleUrlTitle='' subtitle='' subtitleClass='' subtitleLevel=4 class='' id='' img='' imgType='' imgClass='' imgAlt='' header='' headerClass='' headerLabelClass='' headerImg='' subHeader='' subHeaderClass='' footer='' footerClass='' orientation='v' vcolsInit=12 vcols=[4,8] hoverEffect=true params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if orientation='v'>
<div class="card ${class!}"<#if id!=''> id="${id}"</#if><#if params!=''> ${params!}</#if>>
    <#if header!=''><div class="card-header<#if headerClass !=''> ${headerClass}</#if><#if headerImg!=''> card-header-img</#if>"<#if headerImg!=''>style="background-image:url(${headerImg});"</#if>><#if headerLabelClass!=''><span class="${headerLabelClass!}">${header!}</span><#else>${header!}</#if></div></#if>
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