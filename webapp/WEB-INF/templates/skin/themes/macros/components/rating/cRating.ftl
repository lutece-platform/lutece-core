<#--
Macro: cRating

Description: Displays a score as stars, filled to the value (half stars and tenths included), read by screen readers as "Rating: 3.5 out of 5". Drawn like the scores of module-extend-rating.

Parameters:
- value (number, required): The score, from 0 to max. Default: 0.
- max (number, optional): Number of stars. Default: 5.
- showValue (boolean, optional): Displays the score as text next to the stars. Default: true.
- count (number, optional): Number of votes, displayed in brackets after the score. -1 hides it. Default: -1.
- label (string, optional): Word read before the score. Default: '#i18n{portal.theme.labelRating}'.
- outOfLabel (string, optional): Word between the score and the max. Default: '#i18n{portal.theme.labelRatingOutOf}'.
- size (string, optional): '', 'sm' or 'lg'. Default: ''.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Note - @cRating
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Score with its number of votes:

    <@cRating value=3.5 count=12 />

    Small score out of 4, stars only:

    <@cRating value=2 max=4 showValue=false size='sm' />

-->
<#macro cRating value=0 max=5 showValue=true count=-1 label='#i18n{portal.theme.labelRating}' outOfLabel='#i18n{portal.theme.labelRatingOutOf}' size='' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local ratingValue = [ [ value, 0 ]?max, max ]?min />
<#local ratingText = ratingValue?string('0.#') />
<span class="lutece-ds-rating<#if size!=''> lutece-ds-rating-${size}</#if><#if class!=''> ${class}</#if>" role="img" aria-label="${label} : ${ratingText} ${outOfLabel} ${max}"<@commonAttrs id=id params=params />>
<span class="lutece-ds-rating-stars" style="--rating-max: ${max?c}; --rating-fill: ${(ratingValue / max * 100)?c}%"></span>
<#if showValue || count gte 0><span class="lutece-ds-rating-value"><#if showValue>${ratingText}/${max}</#if><#if count gte 0> (${count})</#if></span></#if>
</span>
</#macro>
