<#--
Macro: searchInput

Description: Generates a floating-label search input field or select element inside a responsive column, used within a searchBox container.

Parameters:
- name (string, required): the name and ID for the input element.
- label (string, optional): the label displayed for the input field.
- value (string, optional): the default value of the input field.
- type (string, optional): the type of input. Use 'select' for a dropdown, otherwise defaults to 'text'.
- minWidth (string, optional): minimum width for select elements (CSS value).
- mandatory (boolean, optional): whether the field is mandatory, default false.

Snippet:

    Text search input:

    <@searchInput name='keyword' label='Search by keyword' value='' />

    Select search input with nested options:

    <@searchInput name='status' label='Status' type='select' minWidth='150px'>
        <option value=''>All</option>
        <option value='1'>Active</option>
        <option value='0'>Inactive</option>
    </@searchInput>

-->
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