<#macro autocomplete id name suggestionsUrl class="" suggestionsPath="" itemValueFieldName="value" btnColor="light" btnSize="" itemLabelFieldNames="[]" itemTitleFieldNames=itemLabelFieldNames itemDescriptionFieldNames="[]" itemTagsFieldNames="[]" copyFields="[]" currentValue="" currentLabel="" required=false minimumInputLength=1 minimumInputLenghtLabel="#i18n{portal.util.labelMinimumSearchLenght}" searchLabel="#i18n{portal.util.labelSearch}" emptyLabel="#i18n{portal.util.labelNoItem}" additionnalRequestParamInputId="" formStyle="">
  <div id="${id}" class="lutece-autocomplete dropdown ${class}" data-itemValueFieldName=${itemValueFieldName} data-itemTitleFieldNames=${itemTitleFieldNames} data-suggestionsUrl="${suggestionsUrl}" data-suggestionsPath="${suggestionsPath}" data-minimumInputLength=${minimumInputLength} data-itemDescriptionFieldNames=${itemDescriptionFieldNames} data-itemTagsFieldNames=${itemTagsFieldNames} data-copyFields=${copyFields} data-emptyLabel="${emptyLabel}" data-currentValue="${currentValue}" data-currentLabel="${currentLabel}" data-suggestionItemClass='["list-group-item", "p-3", "list-group-item-action", "cursor-pointer"]' data-titleClass='["mb-0", "fw-bolder"]' data-descriptionClass='["text-muted", "mb-0"]' data-tagClass='["badge", "bg-blue-lt", "me-1"]' data-loaderIconClasses='{"loading": ["ti-loader-2", "icon-rotate"], "error": ["ti-zoom-exclamation", "text-danger"], "search": ["ti-search"]}' data-emptyClass='["list-group-item", "p-3", "text-muted", "text-center"]' data-searchLabel="${searchLabel}">
    <#if formStyle == "floating">
      <div class="input-group">
    </#if>
    <@formGroup labelFor='${id}' labelKey='${searchLabel}' mandatory=mandatory formStyle="${formStyle}">
      <#if formStyle != "floating">
        <div class="input-group">
      </#if>
      <@input type='text' name="${name}" id="${id}" placeHolder="${searchLabel}" class="lutece-autocomplete-search-input" value="${currentValue}" />
      <div id="${id}-dropdown" class="lutece-autocomplete-dropdown dropdown-menu p-0" aria-labelledby="dropdownMenuButton">
        <ul id="${id}-list-container" class="lutece-autocomplete-result-container list-group list-group-flush overflow-auto bg-white" role="listbox" id="suggestions-list" aria-labelledby="${id}-input" style="max-height:15rem;"></ul>
        <span id="${id}-lutece-autocomplete-default-assistive" class="sr-only visually-hidden">#i18n{portal.util.message.autocomplete.avalailbleResults}</span>
        <div class="sr-only lutece-autocomplete-status" role="status" aria-atomic="true" aria-live="polite"></div>
      </div>
      <#if formStyle != "floating">
        <span role="button" class="input-group-text lutece-autocomplete-remove <#if currentValue=''>d-none</#if> text-danger">
          <i class="ti ti-x fs-5"></i>
        </span>
        <span role="button" class="input-group-text">
          <i id="${id}-search-icon" class="lutece-autocomplete-search-icon ti ti-search"></i>
        </span>
        </div>
      </#if>
    </@formGroup>
    <#if formStyle == "floating">
      <span role="button" class="input-group-text lutece-autocomplete-remove <#if currentValue=''>d-none</#if> text-danger">
        <i class="ti ti-x fs-5"></i>
      </span>
      <span role="button" class="input-group-text">
        <i id="${id}-search-icon" class="lutece-autocomplete-search-icon ti ti-search"></i>
      </span>
      </div>
    </#if>
  </div>
  <script type="module">
    import LuteceAutoComplete from "./themes/shared/modules/luteceAutoComplete.js";
    <#if additionnalRequestParamInputId??>
      new LuteceAutoComplete(document.getElementById(`${id}`), document.getElementById("${additionnalRequestParamInputId}"));
    <#else>
      new LuteceAutoComplete(document.getElementById(`${id}`));
    </#if>
  </script>
</#macro>