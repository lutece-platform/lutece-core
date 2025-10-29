<#macro manageFeature class='mt-2' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="row row-cards<#if class !=''> ${class}</#if>"<#if id !=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
    <div class="col space-y">
        <ul class="list-unstyled space-y">
            <#nested>
        </ul>
    </div>
</div>
</#macro>
<#macro manageFeatureItem class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li>
    <div class="card<#if class !=''> ${class}</#if>"<#if id !=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
        <div class="card-body">
            <div class="row g-3">
            <#nested>
            </div>
        </div>
    </div>
</li>
</#macro>
<#macro manageFeatureItemColumn auto=false cols='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="col-md <#if cols !=''>-${cols}<#elseif auto> col-md-auto</#if><#if class !=''> ${class}</#if>"<#if id !=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
<#nested>
</div>
</#macro>