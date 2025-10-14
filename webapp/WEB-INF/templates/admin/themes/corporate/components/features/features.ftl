<#macro manageFeature class='' id='' params=''>
<div class="row row-cards mt-2<#if class !=''> ${class}</#if>"<#if id !=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
    <div class="col space-y">
        <ul class="list-unstyled space-y">
            <#nested>
        </ul>
    </div>
</div>
</#macro>
<#macro manageFeatureItem class='' id='' params=''>
<li>
    <div class="card<#if class !=''> ${class}</#if>"<#if id !=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
        <div class="row g-0">
            <#nested>
        </div>
    </div>
</li>
</#macro>
<#macro manageFeatureItemColumn auto=false cols='' class='' id='' params='' >
<div class="col<#if cols !=''>-${cols}<#elseif auto>-auto</#if><#if class !=''> ${class}</#if>"<#if id !=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
    <div class="card-body">
     <#nested>
    </div>
</div>
</#macro>