<#--
Macro: cBtn

Description: Generates an interactive button element, rendered as a link when an href is provided.

Parameters:
- label (string, required): The button text.
- class (string, optional): CSS class(es) for the button ('primary', 'secondary', 'tertiary', 'danger', 'expand'). Default: 'primary'.
- btnClass (string, optional): CSS class(es) applied to the label span. Default: ''.
- noclass (boolean, optional): If true, removes the 'btn-' prefix classes when no href. Default: false.
- href (string, optional): If set, renders the button as an anchor link. Default: ''.
- id (string, optional): The unique identifier for the button. Default: ''.
- params (string, optional): Additional HTML attributes for the button. Default: ''.
- type (string, optional): The button type attribute. Default: 'submit'.
- nestedPos (string, optional): Position of nested content relative to the label ('before', 'after'). Default: 'before'.
- disabled (boolean, optional): If true, disables the button. Default: false.
- size (string, optional): Button size ('mini' for smaller). Default: ''.

Showcase:
- desc: Bouton - @cBtn
- bs: components/buttons
- newFeature: false

Snippet:

    Basic submit button:

    <@cBtn label='Submit' class='primary' />

    Link-style button:

    <@cBtn label='View details' class='secondary' href='jsp/site/Portal.jsp?page=details&id=1' />

    Button with icon (nested content):

    <@cBtn label='Download' class='tertiary'>
        <@cIcon name='download' />
    </@cBtn>

-->
<#macro cBtn label class='primary' btnClass='' noclass=false href='' id='' params='' type='submit' nestedPos='before' disabled=false size='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if href=''>
<button class="<#if !noclass>btn btn-</#if>${class!}<#if size == 'mini'> btn-mini</#if>" type="${type!}"<#if id!=''> id="${id!}"</#if><#if params!=''>${params!}</#if><#if disabled> disabled</#if>>
<#if nestedPos='before'><#nested></#if><#if label!=''><span class="btn-label ${btnClass!}">${label!}</span></#if><#if nestedPos='after'><#nested></#if>
</button>
<#else>
<a href="${href!}" class="btn btn-${class!}" <#if id!=''> id="${id!}"</#if><#if params!=''>${params!}</#if><#if disabled> disabled</#if>> 
<#if nestedPos='before'><#nested></#if><#if label!=''><span class="btn-label ${btnClass!}">${label!}</span></#if><#if nestedPos='after'><#nested></#if>
</a>
</#if>
</#macro>