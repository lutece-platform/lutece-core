<#macro searchSeparator type='or' color='primary' content='' deprecated...>
<@deprecatedWarning args=deprecated />
  <div class="col-md-auto d-flex align-items-center justify-content-center position-relative align-self-stretch">
    <div class="d-flex flex-column align-items-center justify-content-center h-100">
      <div class="vr position-absolute start-50 top-0 bottom-0"></div>
      <div class="btn btn-sm position-relative <#if type="or">btn-rounded<#else>p-0 px-1 shadow</#if> btn-${color} d-flex align-items-center justify-content-center shadow-lg" style="z-index: 1;">${content}</div>
    </div>
  </div>
</#macro>