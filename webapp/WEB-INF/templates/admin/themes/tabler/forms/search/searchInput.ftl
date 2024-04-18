<#macro searchInput name label="" value="" type="">
  <div class="col-md">
    <div class="form-floating">
      <input type="${(type?length > 0)?then(type, 'text')}" class="form-control border-0" id="${name}" placeholder=" " name="${name}" value="${value}" >
      <label for="${name}" class="w-100">${label}<span class="text-danger float-end fw-bold" style="font-size: 1.6em;">*</span></label>
    </div>
  </div>
</#macro>
