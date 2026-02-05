<#macro manageFeature class='mt-2' colClass='space-y' listClass='space-y' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="row row-cards<#if class !=''> ${class}</#if>"<#if id !=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
    <div class="col ${colClass}">
        <ul class="list-unstyled ${listClass}">
            <#nested>
        </ul>
    </div>
</div>
</#macro>
<#macro manageFeatureItem class='' align='start' valign='center' liClass='' bodyClass='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li class="<#if liClass !=''> ${liClass}</#if>">
    <div class="card<#if class !=''> ${class}</#if>"<#if id !=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
        <div class="card-body<#if bodyClass !=''> ${bodyClass}</#if>">
            <div class="row g-3 justify-content-${align} align-items-${valign}">
            <#nested>
            </div>
        </div>
    </div>
</li>
</#macro>
<#macro manageFeatureItemColumn auto=false flex=true cols='' valign='center' align='start' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="col-md<#if flex> d-flex align-items-${valign} justify-content-${align}</#if><#if cols !=''> col-${cols}<#elseif auto> col-md-auto</#if><#if class !=''> ${class}</#if>"<#if id !=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
<#nested>
</div>
</#macro>