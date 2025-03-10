<#-- Macro: cCarousel

Description: affiche un carousel d'image ou de vidéo.

Parameters:

@param - id - string - optional - l'ID du carousel (par défaut: 'manege')
@param - class - string - optional - permet d'ajouter une classe CSS au carousel
@param - items - string - optional - définit la liste des images du carousel. Si vide le contenu "#nested" sera utilisé.
@param - indicatorSize - number - optional -  Si supérieur à 1 affiche des puces égales au nombre d'image -il sera toujours égal au nombre d'image !- Si items n'est pas vide le calcul se fera sur la longueur de la séquence (par défaut: 1)
@param - controls - boolean - optional -  permet d'afficher les boutons 'Previous' et 'Next' pour naviguer dans le carousel (par défaut: false)
@param - slideControls - boolean - optional -  permet d'afficher les boutons 'pause', 'play' et 'stop' dans le carousel (par défaut: false)
@param - pagination - boolean - optional -  permet d'afficher la pagination pour naviguer dans le carousel (par défaut: false)
@param - label - string - optional -  permet de définir l'aria-label du carousel (par défaut: 'Carousel')
@param - labelPrev - string - optional -  permet de définir le titre du bouton 'Previous'
@param - labelNext - string - optional -  permet de définir le titre du bouton 'Next'
@param - params - string - optional - permet d'ajouter des parametres HTML au carousel
 -->
<#macro cCarousel id='manege' items='' indicatorSize=1 controls=false slideControls=false pagination=false label='Carousel' labelPrev='' labelNext='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local itemSize = indicatorSize />
<#if items != ''><#local itemSize = items?size - 1 /></#if>
<#if slideControls>
<div id="carouselButtons-${id}" class="d-flex justify-content-center py-2">
    <button id="playButton-${id}" type="button" class="btn btn-tertiary" size="mini" aria-label="Démarrer la lecture du carrousel">
        <@parisIcon name='play' />
        </button>
    <button id="pauseButton-${id}" type="button" class="btn btn-tertiary" size="mini" aria-label="Mettre le carrousel en pause">
        <@parisIcon name='refresh' />
    </button>
    <button id="stopButton-${id}" type="button" class="btn btn-tertiary" size="mini" aria-label="Arrêter le carrousel et revenir à la première diapositive">
        <@parisIcon name='close' />
    </button>
</div>
</#if>
<div id="${id!}" class="carousel slide carousel-themed show-neighbors" data-bs-ride="carousel" aria-roledescription="carousel" aria-label="${label}">
	<#if pagination>
		<#if itemSize &gt; 1>
		    <ol class="carousel-indicators pagination">
		    <#list 0..( itemSize - 1) as idx>
		        <li class="page-item<#if idx=0> active</#if>" data-bs-target="#${id!}" data-bs-slide-to="${idx}" aria-label="Slide ${idx}" aria-selected="false" aria-controls="carousel-item-${idx}" tabindex="0"><span class="page-link">${idx+1}</span></li>
		    </#list>
		    </ol>
		</#if>
	</#if>
    <div class="carousel-inner">
    <#if items != ''>
        <#local itemIdx = 1>
        <#local isActive = true>
        <#list items as cItem>
            <@cCarouselItem img=cItem.img active=isActive idx=itemIdx max=itemSize alt=cItem.alt title=cItem.title subtitle=cItem.subTitle class=cItem.class params=cItem.class />
            <#local itemIdx = itemIdx + 1 />
            <#local isActive = false />
        </#list>
    <#else>
        <#nested>
    </#if>
    </div>
    <#if controls>
    <button class="carousel-control-prev" data-bs-target="#${id!}" data-bs-slide="prev" aria-label="élément précédent">
        <span class="carousel-bg-control">
            <@parisIcon name='arrow-left' />
            <span class="visually-hidden">${labelPrev}</span>
        </span>
    </button>
    <button class="carousel-control-next" data-bs-target="#${id!}" data-bs-slide="next" aria-label="élément suivant">
        <span class="carousel-bg-control">
            <@parisIcon name='arrow-right' />
            <span class="visually-hidden">${labelNext}</span>
        </span>
    </button>

    </#if>
</div>

<script type="module" src="${commonsSiteThemePath}${commonsSiteJsPath}/modules/theme-carousel.js"></script>
<#if slideControls>
    <script>
        document.addEventListener("DOMContentLoaded", function (event) {
            var carouselElement = document.getElementById('${id}');
            var carousel = bootstrap.Carousel.getOrCreateInstance(carouselElement);

            document.getElementById('playButton-${id}').addEventListener('click', function () {
                carousel.cycle();
            });

            document.getElementById('pauseButton-${id}').addEventListener('click', function () {
                carousel.pause();
            });

            document.getElementById('stopButton-${id}').addEventListener('click', function () {
                carousel.pause();
                carousel.to(0);
            });
        });
    </script>
</#if>
</#macro>