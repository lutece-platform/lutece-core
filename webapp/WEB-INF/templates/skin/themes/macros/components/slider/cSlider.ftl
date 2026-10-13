<#--
Macro: cSlider

Description: Generates a horizontal card slider/carousel with navigation buttons and optional "view all" link for browsing content on skin pages.

Parameters:
- id (string, required): Unique identifier for the slider.
- sliderLabel (string, required): Accessible ARIA label for the slider.
- class (string, optional): CSS grid classes for slide layout. Default: 'row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4'.
- slides (object, optional): Collection of slide objects with properties: index, id, class, img, imgAlt, link, linkLabel, url, content. Default: {}.
- slideLabel (string, optional): Accessible label prefix for individual slides. Default: 'slide'.
- buttonNext (string, optional): Label for the next button. Default: '#i18n{portal.util.labelNext}'.
- buttonPrev (string, optional): Label for the previous button. Default: '#i18n{portal.util.labelPrevious}'.
- buttonClass (string, optional): CSS classes for navigation buttons. Default: 'light rounded-circle'.
- linkAll (string, optional): URL for a "view all" link. Default: ''.
- linkAllLabel (string, optional): Label for the "view all" link. Default: '#i18n{portal.util.labelMore}'.
- linkAllClass (string, optional): CSS classes for the "view all" link. Default: 'btn btn-link-primary me-sm'.
- params (string, optional): Additional HTML attributes. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Showcase:
- desc: Slider - @cSlider
- bs: forms/range
- newFeature: false

Snippet:

    Basic slider with slides:

    <@cSlider id='newsSlider' sliderLabel='Latest news' slides=newsSlides />

    Slider with "view all" link:

    <@cSlider id='eventSlider' sliderLabel='Upcoming events' slides=eventSlides linkAll='jsp/site/Portal.jsp?page=events' linkAllLabel='View all events' />

    Slider with custom grid and nested content:

    <@cSlider id='serviceSlider' sliderLabel='Our services' slides=serviceSlides class='row-cols-1 row-cols-md-2 row-cols-lg-3'>
        <p class="text-muted mt-3">Swipe or use arrows to browse.</p>
    </@cSlider>

-->
<#macro cSlider id sliderLabel class='row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4' slides={} slideLabel='slide' buttonNext='#i18n{portal.util.labelNext}' buttonPrev='#i18n{portal.util.labelPrevious}' buttonClass='light rounded-circle' linkAll='' linkAllLabel='#i18n{portal.util.labelMore}' linkAllClass='btn btn-link-primary me-sm' params='' attrs={}>
<#assign sliderInnerId = id + '-theme-slider'>
<#assign sliderRowClass = 'theme-slider ' + class!>
<#assign sliderParams>aria-roledescription="slide" aria-label="${sliderLabel!}" ${params?no_esc}</#assign>
<@cSection id=id class='theme-slider-wrapper' params=sliderParams attrs=attrs>
    <@cRow id=sliderInnerId class=sliderRowClass params='aria-live="off"'>
        <#if slides?size gt 0>
            <#assign slideIdx=1 />
            <#list slides as slideItem>
                <@cSlide slide=slideItem currentIndex=slideIdx lastIndex=slides?size />
                <#assign slideIdx++ />
            </#list>
        <#else>
            <@cSlide slide={} />
        </#if>
    </@cRow>
    <#nested>
    <#if slides?size gt 0 && ( buttonNext?has_content || buttonPrev?has_content || linkAll?has_content )>
        <@cBlock class='slider-buttons'>
            <@cContainer>
                <@cBlock class='d-flex justify-content-end pt-4 pb-md-5 mb-md-5'>
                    <#if linkAll?has_content>
                        <@cLink class=linkAllClass label=linkAllLabel href=linkAll />
                    </#if>
                    <@cBlock class='d-flex align-items-center justify-content-end gap-1'>
                        <#if buttonPrev?has_content>
                            <#assign prevBtnClass = buttonClass + ' disabled prev-slide'>
                            <#assign prevBtnParams>tabindex="0" aria-controls="${sliderInnerId}" aria-label="${buttonPrev!} ${slideLabel}"</#assign>
                            <@cBtn label='' class=prevBtnClass type='button' disabled=true params=prevBtnParams>
                                <@cIcon name='arrow-left' />
                            </@cBtn>
                        </#if>
                        <#if buttonNext?has_content>
                            <#assign nextBtnClass = buttonClass + ' ms-sm next-slide'>
                            <#assign nextBtnParams>tabindex="0" aria-controls="${sliderInnerId}" aria-label="${buttonNext!} ${slideLabel}"</#assign>
                            <@cBtn label='' class=nextBtnClass type='button' params=nextBtnParams>
                                <@cIcon name='arrow-right' />
                            </@cBtn>
                        </#if>
                    </@cBlock>
                </@cBlock>
            </@cContainer>
        </@cBlock>
    </#if>
</@cSection>
<script>
document.addEventListener('DOMContentLoaded', function () {
    const slider = document.querySelector('#${id?js_string?no_esc} .theme-slider');
    const btnPrev = document.querySelector('#${id?js_string?no_esc} .slider-buttons .prev-slide');
    const btnNext = document.querySelector('#${id?js_string?no_esc} .slider-buttons .next-slide');
    if (!slider) return;
    slider.focus();

    const STEP = 305;
    const slides = slider.querySelectorAll(':scope > .slide');
    let totalWidth = 0;
    slides.forEach(function (slide) {
        totalWidth += slide.offsetWidth;
    });
    let scrollWidth = totalWidth;
    const nbSlides = slides.length;

    function setDisabled(btn, value) {
        if (!btn) return;
        if (value) {
            btn.classList.add('disabled');
            btn.setAttribute('disabled', 'disabled');
        } else {
            btn.classList.remove('disabled');
            btn.removeAttribute('disabled');
        }
    }

    function bindKeySpace(btn) {
        if (!btn) return;
        btn.setAttribute('tabindex', '0');
        btn.setAttribute('role', 'button');
        btn.addEventListener('keydown', function (event) {
            if (event.key === ' ') {
                btn.click();
                event.preventDefault();
            }
        });
    }

    if (nbSlides > 1) {
        setDisabled(btnPrev, true);
        bindKeySpace(btnPrev);
        bindKeySpace(btnNext);

        if (btnPrev) {
            btnPrev.addEventListener('click', function () {
                slider.scrollBy({ left: -STEP, behavior: 'smooth' });
                if (scrollWidth + STEP >= totalWidth) {
                    setDisabled(btnPrev, true);
                } else {
                    scrollWidth += STEP;
                    setDisabled(btnNext, false);
                }
            });
        }

        if (btnNext) {
            btnNext.addEventListener('click', function () {
                slider.scrollBy({ left: STEP, behavior: 'smooth' });
                if (scrollWidth <= STEP) {
                    setDisabled(btnNext, true);
                } else {
                    scrollWidth -= STEP;
                    setDisabled(btnPrev, false);
                }
            });
        }
    } else {
        // Single slide: hide the navigation buttons block
        const sliderButtons = document.querySelector('#${id?js_string?no_esc} .slider-buttons');
        if (sliderButtons) sliderButtons.remove();
    }
});
</script>
</#macro>
<#--
Macro: cSlide

Description: Generates a single slide card element for use inside a cSlider component.

Parameters:
- slide (object, optional): Slide data object with properties: index, id, class, img, imgAlt, link, linkLabel, url, content. Default: {}.
- currentIndex (number, optional): Current slide index for accessibility labeling. Default: 1.
- lastIndex (number, optional): Total number of slides for accessibility labeling. Default: 1.
- params (string, optional): Additional HTML attributes. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Basic usage (typically called internally by cSlider):

    <@cSlide slide=mySlideData currentIndex=1 lastIndex=5 />

-->
<#macro cSlide slide={} currentIndex=1 lastIndex=1 params='' attrs={}>
<#if slide?size gt 0>
    <#assign slideIndex = ( slide.index?? && slide.index!='' )?then( ( slide.index!'' )?string, ( currentIndex!'' )?string ) />
    <#assign slideId = ( slide.id?? && slide.id!='' )?then( ( slide.id!'' )?string, '' ) />
    <#assign slideClass>slide p-0<#if slide.class?? && slide.class!=''> ${slide.class!}</#if></#assign>
    <#assign slideImg = ( slide.img?? && slide.img!='' )?then( ( slide.img!'' )?string, '' ) />
    <#assign slideImgAlt = ( slide.imgAlt?? && slide.imgAlt!='' )?then( ( slide.imgAlt!'' )?string, '' ) />
    <#assign slideUrl = ( slide.link?? && slide.link!='' )?then( ( slide.url!'' )?string, '' ) />
    <#assign slideLinkLabel = ( slide.linkLabel?? && slide.linkLabel!='' )?then( ( slide.linkLabel!'' )?string, '' ) />
    <#assign slideContent = ( slide.content?? && slide.content!='' )?then( ( slide.content!'' )?string, '' ) />
    <#assign slideParams>role="group" aria-roledescription="slide" aria-label="${slideIndex!} / ${lastIndex}" ${params?no_esc}</#assign>
    <@cCard class=slideClass id=slideId title=slideLinkLabel titleUrl=slideUrl img=slideImg params=slideParams attrs=attrs>
        <@cText class='card-text'>${slideContent}</@cText>
        <#nested>
    </@cCard>
<#else>
    <@cCard class='slide p-0' title='#i18n{portal.util.labelNoItem}' header='#i18n{portal.util.labelNoItem}' headerLabelClass='d-none' imgAlt='#i18n{portal.util.labelNoItem}' img='images/pexels-picjumbocom-196645.jpg' />
</#if>
</#macro>
