<#--
Macro: cTextArea

Description: Generates a textarea form field with support for validation, help/error messages, and character limits.

Parameters:
- name (string, required): the name attribute of the textarea.
- class (string, optional): CSS class for the textarea. Default: 'form-control'.
- id (string, optional): the ID of the textarea. Default: ''.
- placeholder (string, optional): the placeholder text. Default: ''.
- required (boolean, optional): marks the field as required. Default: false.
- html5Required (boolean, optional): uses the HTML5 required attribute. Default: true.
- disabled (boolean, optional): disables the textarea. Default: false.
- readonly (boolean, optional): sets the textarea as readonly. Default: false.
- title (string, optional): the title attribute. Default: ''.
- autocomplete (string, optional): the autocomplete attribute value (on/off). Default: ''.
- maxlength (number, optional): maximum character length. Default: 0.
- helpMsg (string, optional): help message displayed below the textarea. Default: ''.
- rows (number, optional): number of visible text lines. Default: 0.
- errorMsg (string, optional): error message displayed on validation failure. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: "Zone de texte - @cTextArea"
- bs: "forms/form-control"
- newFeature: false

Snippet:

    Basic textarea:

    <@cTextArea name='message' placeholder='Enter your message...' rows=5 />

    Required textarea with character limit:

    <@cTextArea name='comment' required=true maxlength=500 helpMsg='Maximum 500 characters.' rows=4 />

    Textarea with error message:

    <@cTextArea name='description' rows=3 errorMsg='This field is required.' />

    Textarea with default content:

    <@cTextArea name='notes' rows=6>
        Default content goes here.
    </@cTextArea>

-->
<#macro cTextArea name class='form-control' id='' placeholder='' required=false html5Required=false disabled=false readonly=false title='' autocomplete='' maxlength=0 helpMsg='' rows=0 errorMsg='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#assign idMsg = ( id?has_content )?then( ( id )?string, ( name!'' )?string ) />
<#local textAreaId = id?has_content?then( id, name ) />
<#local describedBy = ( ( errorMsg?has_content && errorMsg != '_error' )?then( 'error_' + textAreaId, '' ) + ' ' + helpMsg?has_content?then( 'help_' + textAreaId, '' ) )?trim />
<textarea class="<#if class?has_content> ${class!}</#if><#if errorMsg?has_content> is-invalid</#if>" name="${name!}" id="<#if id?has_content>${id}<#else>${name!}</#if>" <#if placeholder?has_content> placeholder="${placeholder!}"</#if><#if autocomplete?has_content> autocomplete="${autocomplete!}"</#if><#if title?has_content> title="${title}"</#if><#if maxlength?number gt 0> maxlength="${maxlength!}"</#if><#if disabled> disabled</#if><#if readonly> readonly</#if><@commonAttrs params=params /><#if rows?number!=0> rows="${rows}"</#if><#if required> <#if html5Required>required</#if> aria-required="true"</#if><#if errorMsg?has_content> aria-invalid="true"</#if><#if describedBy?has_content> aria-describedby="${describedBy}"</#if>>
<#nested>
</textarea>
<#if helpMsg?has_content><@cFormHelp idMsg helpMsg /></#if>
<#if errorMsg?has_content><@cFormError idMsg errorMsg /></#if>
</#macro>