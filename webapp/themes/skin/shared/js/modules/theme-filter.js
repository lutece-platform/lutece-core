/**
 * themeSwitcher class provides manage and manipulate menus.
 */
export class themeFilter {
    /**
     * Constructor for the themeSwitcher class.
     * Initializes the DOM elements, gets the saved theme, and calls the init method.
     */
    constructor( filters, options ) {
        this.filters = filters != null ? filters :'filter-wrapper';
        this.options = Object.assign({
            container: 'filters-container',
            toggleBtn: 'filter-toggle-button',
            eraseBtn: 'filter-erase-button',
            btnLabel: 'btn-label',
            labelShowFilter: 'Show filters',
            labelHideFilter: 'Hide filters',
        }, options );
        this.init();
    }
    /**
     * Initializes the themeSwitcher by applying the saved theme, setting the active menu item,
     * adding event listeners for menu items, setting up the menu switcher.
     */
    init() {
        /* Menu Overflow management */
        const filtersWrapper = document.querySelectorAll(`.${this.filters}`);
        filtersWrapper.forEach( filter => {
            let isFiltersVisible = true;
            const filterContainer = filter.querySelector( `.${this.options.container}` );
            const filterToggleButton = filter.querySelector( `.${this.options.toggleBtn}` );
            const filterEraseButton = filter.querySelector( `.${this.options.eraseBtn}` );
            const btnLabelSelector = `.${this.options.btnLabel}`;
            const labelShowFilter = this.options.labelShowFilter;
            const labelHideFilter = this.options.labelHideFilter;
            filterToggleButton.addEventListener("click", function() {
                if (isFiltersVisible) {
                    filterContainer.style.display = "none";
                    filterToggleButton.querySelector(btnLabelSelector).innerHTML = labelShowFilter;
                } else {
                    filterContainer.style.display = "flex";
                    filterToggleButton.querySelector(btnLabelSelector).innerHTML = labelHideFilter;
                }
                isFiltersVisible = !isFiltersVisible;
            });

            filterEraseButton.addEventListener("click", function() {
                const checkboxes = filter.querySelectorAll("input[type='checkbox']:checked");
                checkboxes.forEach(function(checkbox) {
                    checkbox.checked = false;
                });
            });
        });
    }
}