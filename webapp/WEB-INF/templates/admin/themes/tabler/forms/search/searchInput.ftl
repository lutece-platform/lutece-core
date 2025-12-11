<#macro searchInput name label="" value="" type="" minWidth="" mandatory=false deprecated...>
<@deprecatedWarning args=deprecated />
  <div class="col-md">
    <div class="form-floating">
      <#if type !="select">
        <input type="${(type?length > 0)?then(type, 'text')}" class="form-control border-0  shadow-none" id="${name}" placeholder=" " name="${name}" value="${value}">
        <#else>
          <select class="form-select border-0 shadow-none" style="min-width:${minWidth}" id="${name}" name="${name}">
            <#nested>
          </select>
      </#if>
      <label for="${name}" class="w-100">
        ${label}
        <span class="text-danger float-end fw-bold" style="font-size: 1.6em;">*</span></label>
    </div>
  </div>
</#macro>