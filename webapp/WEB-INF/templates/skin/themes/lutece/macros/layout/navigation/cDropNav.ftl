<#-- cDropNav -->
<#-- Params
    - title   : Title shown over the banner
    - logoImg : Default: Empty string, show logo instead of text title, title is set as title html attribute for the logo image.
    - hasMenu : Default: true; 
    Nested content : Shows default page menu, but can other item can be add using @mainNavItem macro.
-->
<#macro cDropNav title icon='' caret='' expanded=false id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local cId='id-' + random()>
<#if id !=''><#local cId=id /></#if>
<div class="dropdown-group" <#if class !='' > ${class!}</#if>"<#if params!=''> ${params}</#if>>
    <button type="button" class="nav-link dropdown-toggle" href="#" id="dropdownMenu${cId!}" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="${expanded?c}">
        ${icon!} ${title!} ${caret!}
    </button>
    <ul class="dropdown-menu" aria-labelledby="dropdownMenu${cId!}">
        <div class="dropdown-content">
            <#nested>
        </div>
    </ul>
</div>
</#macro>