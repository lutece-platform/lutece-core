<#-- Macro: cCarouselItem

Description: affiche un carousel d'image ou de vidéo.

Parameters:

@param - id - string - optional - l'ID de la slide du carousel (par défaut: 'manege')
@param - class - string - optional - permet d'ajouter une classe CSS à la slide du carousel
@param - img - string - required - permet de définir la source de l'image de la slide du carousel
@param - active - boolean - optional - permet de définir que la slide du carousel est celle active au chargement de la page (par défaut: false)
@param - idx - number - optional - permet de définir l'index de la slide du carousel utilisé par l'attribut 'id' et 'aria-label' (par défaut: 1)
@param - max - number - optional - permet de définir le nombre maximum de slides dans le carousel pour l'attribut 'aria-label' (par défaut: 1)
@param - alt - string - optional - permet de définir l'attribut 'alt' de l'image
@param - title - string - optional - permet de définir le titre de la slide du carousel
@param - titleLevel - number - optional - permet de définir le niveau de titre de la slide du carousel (par défaut: 3)
@param - subtitle - string - optional - permet de définir le sous-titre de la slide du carousel
@param - params - string - optional - permet d'ajouter des parametres HTML la slide du carousel
 -->
<#macro cCarouselItem img active=false id='manege' idx=1 max=1 alt='' title='' titleLevel=3 subtitle='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="carousel-item<#if active> active</#if><#if class !=''> ${class}</#if>"  id="${id}-item-${idx}" role="group" aria-roledescription="slide" aria-label="${idx} of ${max}"<#if params !=''> ${params}</#if>>
    <div class="item__third">
	    <img src="${img!}" class="d-block w-100" alt="${alt!'...'}">
	    <div class="carousel-caption">
	        <@cTitle level=titleLevel>${title}</@cTitle >
	        <p>${subtitle}</p>
	    </div>
    </div>
</div>
</#macro>