<#--
Macro: cCarouselItem

Description: Generates a single slide within a carousel, containing an image with optional title and subtitle caption.

Parameters:
- img (string, required): The image source URL for the slide.
- active (boolean, optional): If true, this slide is displayed on page load. Default: false.
- id (string, optional): The base identifier for the slide. Default: 'manege'.
- idx (number, optional): The index of this slide, used for id and aria-label. Default: 1.
- max (number, optional): The total number of slides, used in aria-label. Default: 1.
- alt (string, optional): Alt text for the slide image. Default: ''.
- title (string, optional): Title caption for the slide. Default: ''.
- titleLevel (number, optional): Heading level for the title. Default: 3.
- subtitle (string, optional): Subtitle caption for the slide. Default: ''.
- class (string, optional): Additional CSS class(es) for the slide. Default: ''.
- params (string, optional): Additional HTML attributes for the slide. Default: ''.

Snippet:

    Active carousel slide:

    <@cCarouselItem img='images/banner.jpg' active=true idx=1 max=3 alt='Main banner' title='Welcome to our portal' subtitle='Discover our services' />

    Secondary slide:

    <@cCarouselItem img='images/events.jpg' idx=2 max=3 alt='Events' title='Upcoming events' />

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