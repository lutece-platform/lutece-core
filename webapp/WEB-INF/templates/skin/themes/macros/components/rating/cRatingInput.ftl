<#--
Macro: cRatingInput

Description: Generates a star picker for a form: a fieldset of native radio buttons, one per star, operable with the arrow keys.

Parameters:
- name (string, required): Name of the field, sent with the chosen number of stars (1 to max). Default: ''.
- legend (string, optional): Question of the fieldset. Default: '#i18n{portal.theme.labelRating}'.
- max (number, optional): Number of stars. Default: 5.
- value (number, optional): Stars checked when the form opens. 0 checks none. Default: 0.
- required (boolean, optional): The field is required. Default: false.
- disabled (boolean, optional): The field is disabled. Default: false.
- starLabel (string, optional): Word read after the number of each star. Default: '#i18n{portal.theme.labelRatingStars}'.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): Prefix of the ids of the radio buttons. Default: name.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Saisie de note - @cRatingInput
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Star picker:

    <@cForm action='jsp/site/Portal.jsp?page=myplugin&action=doRate'>
        <@cRatingInput name='rating' legend='Votre avis sur ce service' required=true />
        <@cBtn label='Envoyer' />
    </@cForm>

-->
<#macro cRatingInput name='' legend='#i18n{portal.theme.labelRating}' max=5 value=0 required=false disabled=false starLabel='#i18n{portal.theme.labelRatingStars}' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local idPrefix = (id!='')?then(id, name) />
<fieldset class="lutece-ds-rating-input<#if class!=''> ${class}</#if>"<#if disabled> disabled</#if><@commonAttrs params=params />>
<legend class="form-label">${legend}<#if required> <span class="text-danger" aria-hidden="true">*</span></#if></legend>
<span class="lutece-ds-rating-input-stars">
<#list 1..max as star>
<input type="radio" class="visually-hidden" name="${name}" id="${idPrefix}-${star}" value="${star}"<#if star==value> checked</#if><#if required> required</#if>>
<label class="lutece-ds-rating-input-star" for="${idPrefix}-${star}"><span class="visually-hidden">${star} ${starLabel}</span></label>
</#list>
</span>
<#nested>
</fieldset>
</#macro>
