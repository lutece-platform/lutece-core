<#--
Macro: cList

Description: Generates a customizable list component with support for default, expandable ('more'), and file download list styles.

Parameters:
- items (list, required): List of item objects with properties: url, title, target, class, size (for files).
- type (string, optional): List display type ('default', 'more', 'files'). Default: 'default'.
- itemsShown (number, optional): Number of items visible before requiring 'show more' (used with type='more'). Default: 0.
- indexShown (boolean, optional): Whether to display the item index. Default: false.
- labelMore (string, optional): Label for the 'show more' button. Default: i18n("theme.labelShowMore").
- labelClose (string, optional): Label for the 'close' button. Default: i18n("theme.labelClose").
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Showcase:
- desc: Liste - @cList
- bs: components/list-group
- newFeature: false

Snippet:

    Basic default list:

    <@cList items=myItems />

    Expandable list showing first 3 items:

    <@cList items=myItems type='more' itemsShown=3 />

    File download list:

    <@cList items=fileItems type='files' />

    List with additional nested content:

    <@cList items=myItems type='default' class='my-custom-list'>
        <@cListItem url='#' label='Extra item' title='Extra item title' />
    </@cList>

-->
<#--
Macro: cListItem

Description: Generates a single list item for use inside a cList component, either as a link or with nested content.

Parameters:
- url (string, optional): URL for the list item link. Default: ''.
- label (string, optional): Text label displayed in the list item. Default: ''.
- title (string, optional): Title attribute for the link. Default: ''.
- target (string, optional): Link target attribute. Default: ''.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Snippet:

    Link list item:

    <@cListItem url='/my-page' label='My page' title='Go to my page' />

    List item with nested content:

    <@cListItem class='custom-item'>
        <span>Custom content inside the list item</span>
    </@cListItem>

-->
<#macro cList items type='default' itemsShown=0 indexShown=false labelMore=i18n("theme.labelShowMore") labelClose=i18n("theme.labelClose") class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />        
<ul class="custom-list list-${type!}<#if class!=''> ${class}</#if><#if type='more'> more-list</#if>" <#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> >
<#if items?has_content>
    <#list items as i>
        <li class="custom-list-item <#if i.class?? && i.class !=''>${i.class}</#if><#if type='more' && i?index &gt; itemsShown>extra hidden</#if>">
            <a class="list-info" href="${i.url!'return false;'}" title="${i.title}" <#if i.target !=''> target="${i.target}"</#if><#if type='files'> download="${i.title!?replace(' ','_')?replace('"','_')?replace('\'','_')}"</#if>>
                <span>${i.title!} <#if indexShown>${i?index}</#if></span>
                <#if type='files'>
                <div class="files-info">
                    <small class='fw-bold'>${i.size!}</small>
                    <@parisIcon name='file-text' class='ms-m main-color' />
                </div>    
                </#if>
            </a>
        </li>
    </#list>
</#if>
<#nested>
<#if type ='more'>
    <li class="more">
        <@cLink href='#' nestedPos='before' class='btn-more' label=labelMore title=labelMore params='data-plus="${labelMore}" data-minus="${labelClose}"'>
            <svg xmlns="http://www.w3.org/2000/svg" class="plus" width="24" height="24" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M12 5l0 14" /><path d="M5 12l14 0" /></svg> 
            <svg xmlns="http://www.w3.org/2000/svg" class="minus" width="24" height="24" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M5 12l14 0" /></svg>
        </@cLink>
    </li>
</#if> 
</ul>
</#macro>
<#macro cListItem url='' label='' title='' target='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li class="custom-list-item<#if class !=''> ${class}</#if>">
<#if url !=''>
<a class="list-info" href="${url!}" title="${title!}"<#if target !=''> target="${target}"</#if>>
    <span>${label}</span>
</a>
<#else>
<#nested>
</#if>    
</li>
</#macro>