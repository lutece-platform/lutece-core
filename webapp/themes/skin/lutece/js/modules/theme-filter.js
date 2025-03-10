const filtersContainer = document.getElementById("filters-container");
const filterToggleButton = document.getElementById("filter-toggle-button");
const filterEraseButton = document.getElementById("filter-erase-button");

let isFiltersVisible = true;

filterToggleButton.addEventListener("click", function() {
    if (isFiltersVisible) {
        filtersContainer.style.display = "none";
        filterToggleButton.querySelector('.btn-label').innerHTML = 'Afficher les filtres'
    } else {
        filtersContainer.style.display = "flex";
        filterToggleButton.querySelector('.btn-label').innerHTML = 'Masquer les filtres'
    }
    isFiltersVisible = !isFiltersVisible;
});


filterEraseButton.addEventListener("click", function() {
    const checkboxes = filtersContainer.querySelectorAll("input[type='checkbox']:checked");
    checkboxes.forEach(function(checkbox) {
        checkbox.checked = false;
    });
});