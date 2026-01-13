<#-- cList                          -->
<#-- items: Mandatory               -->
<#-- type: default / more / files   -->
<#-- itemsShown: 0                  -->
<#-- labelMore: 'VOIR PLUS'         -->
<#-- class:                         -->
<#-- id:                            -->
<#-- params:                        -->
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