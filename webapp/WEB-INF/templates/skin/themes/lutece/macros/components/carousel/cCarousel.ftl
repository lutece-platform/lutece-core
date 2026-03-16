<#--
Macro: cCarousel

Description: Generates an image or video carousel with optional navigation controls, pagination, and play/pause buttons.

Parameters:
- id (string, optional): The unique identifier for the carousel. Default: 'manege'.
- items (object, optional): List of carousel item objects (with 'img', 'alt', 'title', 'subTitle', 'class'). If empty, nested content is used. Default: ''.
- indicatorSize (number, optional): Number of pagination indicators (auto-calculated from items if provided). Default: 1.
- controls (boolean, optional): If true, shows previous/next navigation arrows. Default: false.
- slideControls (boolean, optional): If true, shows play/pause/stop buttons. Default: false.
- pagination (boolean, optional): If true, shows numbered pagination indicators. Default: false.
- label (string, optional): The aria-label for the carousel. Default: 'Carousel'.
- labelPrev (string, optional): Label for the previous button. Default: ''.
- labelNext (string, optional): Label for the next button. Default: ''.
- class (string, optional): Additional CSS class(es) for the carousel. Default: ''.
- params (string, optional): Additional HTML attributes for the carousel. Default: ''.

Showcase:
- desc: Carrousel - @cCarousel
- bs: components/carousel
- newFeature: false

Snippet:

    Carousel with controls and pagination:

    <@cCarousel id='hero-carousel' controls=true pagination=true>
        <@cCarouselItem img='images/slide1.jpg' active=true idx=1 max=3 alt='Slide 1' title='Welcome' />
        <@cCarouselItem img='images/slide2.jpg' idx=2 max=3 alt='Slide 2' title='Services' />
        <@cCarouselItem img='images/slide3.jpg' idx=3 max=3 alt='Slide 3' title='Contact' />
    </@cCarousel>

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

<script type="module" src="${commonsSharedThemePath}${commonsSiteJsModulePath}theme-carousel.js"></script>
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