<#--
Macro: autocomplete
Description: Generates an autocomplete dropdown
Parameters:
- id (string): the ID of the autocomplete search input element.
- name (string): the name attribute of the hidden input element.
- suggestionsUrl (string): the URL to fetch the suggestions from.
- suggestionsPath (string, optional): the path to the suggestions list in the response object.
- itemValueFieldName (string, optional): the value property of the suggestion object.
- btnColor (string, optional): the color of the dropdown button.
- btnSize (string, optional): the size of the dropdown button.
- itemLabelFieldNames (array, optional): an array of suggestion object property names to display as the title.
- itemTitleFieldNames (array, optional): an array of suggestion object property names to display as the description.
- itemDescriptionFieldNames (array, optional): an array of suggestion object property names to display as the description.
- itemTagsFieldNames (array, optional): an array of suggestion object property names to display as tags.
- currentValue (string, optional): the current value of the autocomplete input.
- currentLabel (string, optional): the current label displayed on the dropdown button.
- required (boolean, optional): whether the input is required or not.
- minimumInputLength (integer, optional): the minimum number of characters needed to trigger the search.
- minimumInputLenghtLabel (string, optional): the label for the minimum input length information.
- searchLabel (string, optional): the label displayed on the dropdown button.
- emptyLabel (string, optional): the label displayed when there are no suggestions.
-->
<#macro autocomplete id name suggestionsUrl suggestionsPath="" itemValueFieldName="value" btnColor="light" btnSize="" itemLabelFieldNames="[]" itemTitleFieldNames=itemLabelFieldNames itemDescriptionFieldNames="[]" itemTagsFieldNames="[]" copyFields="[]" currentValue="" currentLabel="" required=false minimumInputLength=1 minimumInputLenghtLabel="#i18n{portal.util.labelMinimumSearchLenght}" searchLabel="#i18n{portal.util.labelSearch}" emptyLabel="#i18n{portal.util.labelNoItem}">
  <div id="${id}" class="lutece-autocomplete dropdown form-group form-floating " data-itemTitleFieldNames=${itemTitleFieldNames} data-suggestionsUrl="${suggestionsUrl}" data-suggestionsPath="${suggestionsPath}" data-minimumInputLength=${minimumInputLength} data-itemDescriptionFieldNames=${itemDescriptionFieldNames} data-itemTagsFieldNames=${itemTagsFieldNames} data-copyFields=${copyFields} data-emptyLabel="${emptyLabel}" data-currentValue="${currentValue}" data-currentLabel="${currentLabel}" data-suggestionItemClass='["list-group-item", "p-3"]' data-titleClass='["mb-0", "fw-bolder"]' data-descriptionClass='["text-muted", "mb-0"]' data-tagClass='["badge", "bg-blue-lt", "me-1"]' data-loaderIconClasses='{"loading": ["ti-loader-2", "icon-rotate"], "error": ["ti-zoom-exclamation", "text-danger"], "search": ["ti-search"]}' data-emptyClass='["list-group-item", "p-3", "text-muted", "text-center"]' data-searchLabel="${searchLabel}">
    <div class="input-group p-0 m-0">
      <@formGroup formStyle='floating' class='' labelFor='${id}' labelKey='${searchLabel}' mandatory=mandatory>
        <@input type='text' name="${name}" id="${id}" placeHolder="${searchLabel}" class="lutece-autocomplete-search-input" value="${currentValue}" />
        <ul id="${id}-dropdown" class="lutece-autocomplete-dropdown dropdown-menu p-0" aria-labelledby="dropdownMenuButton">
          <ul id="${id}-list-container" class="lutece-autocomplete-result-container list-group list-group-flush overflow-auto bg-white" id="suggestions-list" style="max-height:15rem;">
          </ul>
        </ul>
      </@formGroup>
      <span role="button" class="input-group-text lutece-autocomplete-remove <#if currentValue=''>d-none</#if> text-danger">
        <i class="ti ti-x fs-5"></i>
      </span>
      <span role="button" class="input-group-text">
        <i id="${id}-search-icon" class="lutece-autocomplete-search-icon ti ti-search"></i>
      </span>
    </div>
  </div>
  <script type="module">
    import LuteceAutoComplete from "./themes/shared/modules/luteceAutoComplete.js";
    new LuteceAutoComplete(document.getElementById(`${id}`));
  </script>
</#macro>
