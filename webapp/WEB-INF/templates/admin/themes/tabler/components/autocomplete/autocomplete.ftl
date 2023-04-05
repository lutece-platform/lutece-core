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
- formLabel (string, optional): the label displayed on the dropdown button as default.
- searchLabel (string, optional): the label displayed on the dropdown button.
- emptyLabel (string, optional): the label displayed when there are no suggestions.
-->
<#macro autocomplete id name suggestionsUrl suggestionsPath="" itemValueFieldName="value" btnColor="light" btnSize="" itemLabelFieldNames="[]" itemTitleFieldNames=itemLabelFieldNames itemDescriptionFieldNames="[]" itemTagsFieldNames="[]" currentValue="" currentLabel="" required=false minimumInputLength=1 minimumInputLenghtLabel="#i18n{portal.util.labelMinimumSearchLenght}" formLabel=searchLabel searchLabel="#i18n{portal.util.labelSearch}" emptyLabel="#i18n{portal.util.labelNoItem}">
    <div class="dropdown">
        <input type="text" id="${id}-form-input" name="${name}" style="opacity: 0;width: 0;margin-left:20px;position:absolute;" aria-required="true" value="${currentValue}" <#if required>required=required</#if>>
        <button class="btn btn-${btnColor} border <#if btnSize!=''>btn-${btnSize}</#if>" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-expanded="false">
            <span id="${id}-dropdown-btn"><#if currentLabel!="">${currentLabel}<#else>${formLabel}</#if></span>
            <span id="${id}-remove" class="d-none badge bg-indigo ms-2 rounded-5 p-1 py-0"><i class="ti ti-x fs-5"></i></span>
            <span><i class="ti ti-chevron-down ps-1"></i></span>
        </button>
        <ul id="${id}-dropdown"class="dropdown-menu p-0" aria-labelledby="dropdownMenuButton">
            <li class="p-3 border-bottom">
                <div class="input-icon mb-0">
                    <input type="text" placeholder=${searchLabel} class="form-control" id="${id}-search" <#if currentValue!="">${currentValue}</#if>>
                    <span class="input-icon-addon">
                        <i id="${id}-search-icon" class="ti ti-search"></i>
                    </span>
                </div>
                <#if minimumInputLength gt 1>
                    <div class="d-flex justify-content-end">
                        <small class="text-muted fst-italic">
                            <strong>${minimumInputLength}</strong> ${minimumInputLenghtLabel}
                        </small>
                    </div>
                </#if>
            </li>
            <li>
                <ul id="${id}-list-container" class="list-group list-group-flush overflow-auto bg-white" id="suggestions-list" style="max-height:15rem;">
                </ul>
            </li>
        </ul>
    </div>
    <script type="module">
        import LuteceAutoComplete from './themes/shared/modules/luteceAutoComplete.js';
        //util functions
        const updateLoader = (add, remove) => (loader.classList.add(...add), loader.classList.remove(...remove));
        const createEl = (type, classNames = [], textContent = '') => (el => (el.classList.add(...classNames), el.textContent = textContent, el))(document.createElement(type));

        // Autocomplete
        
        // elements;
        const [formInput, dropdownBtn, removeBtn, loader, dropdown, resultList, searchInput] = ['form-input', 'dropdown-btn', 'remove', 'search-icon','dropdown','list-container','search'].map(suffix => document.getElementById(`${id}-`+suffix));

        // customise the template to display the suggestions
        const itemTemplate = suggestion => {
            const item = createEl('li', ['list-group-item', 'p-3']);
            item.setAttribute('data-value', suggestion.${itemValueFieldName});
            item.setAttribute('data-label', ${itemTitleFieldNames}.map(field => suggestion[field]).join(" "));
            if (suggestion.${itemValueFieldName} == formInput.value) item.classList.add('active');
            item.addEventListener('click', ({ currentTarget }) => {
                dropdown.querySelectorAll(`.list-group-item.active`).forEach(el => el.classList.remove('active'));
                currentTarget.classList.add('active');
                formInput.value = currentTarget.getAttribute('data-value');
                dropdownBtn.textContent = currentTarget.getAttribute('data-label');
                removeBtn.classList.remove('d-none');
            });
            item.append(
                createEl('h4', ['mb-0', 'fw-bolder'], ${itemTitleFieldNames}.map(field => suggestion[field]).join(" ")),
                createEl('p', ['text-muted', 'mb-0'], ${itemDescriptionFieldNames}.map(field => suggestion[field]).join(" ")),
                ${itemTagsFieldNames}.reduce((tags, field) => (tags.appendChild(createEl('span', ['badge', 'bg-blue-lt', 'me-1'], suggestion[field])), tags), createEl('span'))
            );
            return item;
        };

        // init the autocomplete
        const autoComplete = new LuteceAutoComplete(searchInput, resultList, "${suggestionsUrl}", "${suggestionsPath}", itemTemplate, ${minimumInputLength});
        // event listeners
        removeBtn.addEventListener('click', () => (dropdown.querySelectorAll(`.list-group-item.active`).forEach(el => el.classList.remove('active')), formInput.value = '', dropdownBtn.textContent = '${formLabel}', removeBtn.classList.add('d-none')));
        dropdownBtn.addEventListener('click', () => searchInput.focus());
        autoComplete.addEventListener('loading-error', () => updateLoader(['ti-zoom-exclamation', 'text-danger'], ['ti-loader-2', 'icon-rotate', 'ti-search']));
        autoComplete.addEventListener('loading-start', () => updateLoader(['ti-loader-2', 'icon-rotate'], ['ti-zoom-exclamation', 'text-danger', 'ti-search']));
        autoComplete.addEventListener('loading-end', () => {
            updateLoader(['ti-search'], ['ti-loader-2', 'icon-rotate']);
            if (resultList.childElementCount === 0) {
                const emptyItem = createEl('li', ['list-group-item', 'p-3', 'text-muted','text-center'], `${emptyLabel}`);
                resultList.appendChild(emptyItem);
            }
        });
    </script>
</#macro>
