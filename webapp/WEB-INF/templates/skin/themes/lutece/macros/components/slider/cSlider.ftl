<#-- Macro: cSlider

Description: affiche un carousel d'images.

Parameters:
@param - id - string - required - l'ID du carousel
@param - sliderLabel - string - required - Aria label du carousel
@param - class - string - optional - permet d'ajouter une classe CSS au slider (par défaut: 'row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4')
@param - slides - string - optional - Objet contenant les  slides ( voir les attributs ci-dessous )
@param - slideLabel - string - optional - Label pour le label slide (par défaut: 'slide')
@param - buttonNext - string - optional - Label pour le bouton 'next' (par défaut: 'i18n{portal.util.labelNext}')
@param - buttonPrev - string - optional - Label pour le bouton 'prev' (par défaut: '#i18n{portal.util.labelPrevious}')
@param - buttonClass - string - optional - permet d'ajouter une classe CSS aux boutons (par défaut: 'light rounded-circle main-info-color')
@param - buttonClass - string - optional - permet d'ajouter une classe CSS aux boutons (par défaut: 'light rounded-circle main-info-color')
@param - linkAll - string - optional - permet d'ajouter un lien de redirection vers une autre page
@param - linkAllLabel - string - optional - permet de définir le label du lien de redirection
@param - linkAllClass - string - optional - permet d'ajouter une classe CSS au lien
@param - params - string - optional - permet d'ajouter des parametres HTML au carousel
-->    
<#macro cSlider id sliderLabel class='row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4'  slides={} slideLabel='slide' buttonNext='#i18n{portal.util.labelNext}' buttonPrev='#i18n{portal.util.labelPrevious}' buttonClass='light rounded-circle' linkAll='' linkAllLabel='#i18n{portal.util.labelMore}' linkAllClass='btn btn-link-primary me-sm' params=''>
<@cSection id=id class='theme-slider-wrapper' params='aria-roledescription="slide" aria-label="${sliderLabel!}" ${params}'>
	<@cRow id='${id}-theme-slider' class='theme-slider ${class!}' params='aria-live="off"'>
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
	<#if slides?size gt 0 && ( buttonNext !='' || buttonPrev !='' || linkAll !='' ) >
	<@cBlock class='slider-buttons'>
		<@cContainer>
			<@cBlock class='d-flex justify-content-end pt-4 pb-md-5 mb-md-5'>
				<#if linkAll !='' >
					<@cLink class=linkAllClass label=linkAllLabel href=linkAll />  
				</#if>
				<@cBlock class="d-flex align-items-center justify-content-end gap-1 test">
				<#if buttonPrev !='' >
					<@cBtn label='' class='${buttonClass} disabled prev-slide' type='button' disabled=true params='tabindex="0" aria-controls="${id}-theme-slider" aria-label="${buttonPrev!} ${slideLabel}"'> 
						<@parisIcon name='arrow-left' />
					</@cBtn> 
				</#if>
				<#if buttonNext !=''>
					<@cBtn label='' class='${buttonClass} ms-sm next-slide' type='button' params='tabindex="0" aria-controls="${id}-theme-slider" aria-label="${buttonNext!} ${slideLabel}"'> 
						<@parisIcon name='arrow-right' />
					</@cBtn>
				</#if>
				</@cBlock>
			</@cBlock>
		</@cContainer>
	</@cBlock>
	</#if>
</@cSection>
<script>
document.addEventListener('DOMContentLoaded', function (){
	const _slides = $('#${id} .theme-slider'),
	btnPrev = $('#${id} .slider-buttons .prev-slide'),
	btnNext = $('#${id} .slider-buttons .next-slide');
	_slides.focus()

	let totalWidth = 0,
	scrollWidth = 0,
	nbSlides = 0;
	_slides.children('.slide').each(function() {
		totalWidth += $(this).width();
		nbSlides++;
	})
	scrollWidth = totalWidth;
	if (nbSlides > 1) {
		btnPrev.on('click', function() {
			_slides.animate({ scrollLeft: '-=305' }, "slow");
			if (scrollWidth + 305 >= totalWidth) {
				$(this).addClass('disabled').attr('disabled', 'disabled')
			} else {
				scrollWidth += 305
				btnNext.removeClass('disabled').removeAttr('disabled')
			}
		}).addClass('disabled').attr('disabled', 'disabled');

		btnNext.on('click', function() {
			_slides.animate({ scrollLeft: '+=305' }, "slow");
			if (scrollWidth <= 305) {
				$(this).addClass('disabled').attr('disabled', 'disabled')
			} else {
				scrollWidth -= 305
				btnPrev.removeClass('disabled').removeAttr('disabled')
			}
		}).attr('tabindex', 0);
		
		btnPrev.attr('tabindex', 0).attr('role', 'button').keydown(function(event) {
			if (event.which == 32) {
				$(this).click();
				event.preventDefault();
			};
		});
		btnNext.attr('tabindex', 0).attr('role', 'button').keydown(function(event) {
			if (event.which == 32) {
				$(this).click();
				event.preventDefault();
			};
		});
	} else {
		_slides.children('.slider-buttons .btn').remove();
	}
});
</script>
</#macro>
<#-- 
Macro cSlide

-->
<#macro cSlide slide={} currentIndex=1 lastIndex=1 params=''>
<#if slide?size gt 0>
	<#assign slideIndex><#if slide.index?? && slide.index!=''>${slide.index!}<#else>${currentIndex!}</#if></#assign>
	<#assign slideId><#if slide.id?? && slide.id!=''>${slide.id!}</#if></#assign>
	<#assign slideClass>slide p-0<#if slide.class?? && slide.class!=''> ${slide.class!}</#if></#assign>
	<#assign slideImg><#if slide.img?? && slide.img!=''>${slide.img!}</#if></#assign>
	<#assign slideImgAlt><#if slide.imgAlt?? && slide.imgAlt!=''>${slide.imgAlt!}</#if></#assign>
	<#assign slideUrl><#if slide.link?? && slide.link!=''>${slide.url!}</#if></#assign>
	<#assign slideLinkLabel><#if slide.linkLabel?? && slide.linkLabel!=''>${slide.linkLabel!}</#if></#assign>
	<#assign slideContent><#if slide.content?? && slide.content!=''>${slide.content!}</#if></#assign>

	<@cCard class=slideClass id=slideId title=slideLinkLabel titleUrl=slideUrl img=slideImg params='role="group" aria-roledescription="slide" aria-label="${slideIndex!} sur ${lastIndex}" ${params}'>
		<p class="card-text">${slideContent}</p>
		<#nested>
	</@cCard>
<#else>
	<@cCard class='slide p-0' title='#i18n{portal.util.labelNoItem}' header='#i18n{portal.util.labelNoItem}' headerLabelClass='d-none' imgAlt='#i18n{portal.util.labelNoItem}' img='images/pexels-picjumbocom-196645.jpg' />
</#if>
</#macro>