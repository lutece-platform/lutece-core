/**
 * BackportTemplateStyle class
 */
export class BackportTemplateStyle {
    constructor() {
        document.addEventListener('DOMContentLoaded', () => {
            this.backportTemplateStyle();
        });
    }    
    backportTemplateStyle() {
        const tables = document.querySelectorAll('table');
        tables.forEach(table => {
            const tdElements = Array.from(table.querySelectorAll('td'));
            const tdWithForm = tdElements.filter(td => td.querySelector('form') !== null);
            tdWithForm.forEach(td => {
                td.querySelectorAll('form').forEach(form => {
                    form.classList.add('d-flex');
                });
            });
        });
        const lutecePage = document.querySelector('.lutece-page');
        if( !lutecePage ) {
            const lutecePageWrappers = document.querySelectorAll('.lutece-page-wrapper');
            lutecePageWrappers.forEach(wrapper => {
                if (!wrapper.querySelector('main')) {
                    wrapper.classList.add('no-main', 'overflow-auto');
                }
            });
        }
        this.adjustPageHeight();
        window.addEventListener('resize', this.adjustPageHeight.bind(this));
        const columns = document.querySelectorAll('.lutece-page > .lutece-column');

        document.addEventListener('shown.bs.offcanvas', () => {
            columns && columns.forEach(column => {
                column.classList.add("overflow-hidden");
                lutecePage && lutecePage.classList.add("overflow-hidden");
            })
            window.dispatchEvent(new Event('resize'));
        })

        document.addEventListener('hidden.bs.offcanvas', () => {
            columns && columns.forEach(column => {
                column.classList.remove("overflow-hidden");
                lutecePage && lutecePage.classList.remove("overflow-hidden");
            })
            window.dispatchEvent(new Event('resize'));
        })
        document.querySelectorAll('[data-toggle="modal"]').forEach(element => {
            element.setAttribute('data-bs-toggle', 'modal');
            element.setAttribute('data-bs-target', element.getAttribute('data-target'));
        });
        document.querySelectorAll('[data-toggle="dropdown"]').forEach(element => {
            element.setAttribute('data-bs-toggle', 'dropdown');
            element.setAttribute('data-bs-target', element.getAttribute('data-target'));
        });
        document.querySelectorAll('[data-toggle="collapse"]').forEach(element => {
            element.setAttribute('data-bs-toggle', 'collapse');
            element.setAttribute('data-bs-target', element.getAttribute('data-target'));
        });

        document.querySelectorAll('[data-toggle="collapse"]').forEach(element => {
            element.addEventListener('click', function () {
                const icon = element.querySelector('i');
                if (icon.classList.contains('fa-minus')) {
                    icon.classList.add('fa-plus');
                    icon.classList.remove('fa-minus');
                } else if (icon.classList.contains('fa-plus')) {
                    icon.classList.add('fa-minus');
                    icon.classList.remove('fa-plus');
                }
            });
        });
        document.querySelectorAll(".modal").forEach((e => {
            document.body.appendChild(e)
        }))
    }
    adjustPageHeight() {
        const lutecePage = document.querySelector('.lutece-page');
        const luteceColumns = document.querySelectorAll('.lutece-column');
        this.adjustElementHeight(lutecePage);
        luteceColumns && luteceColumns.forEach((col) => this.adjustElementHeight(col));
    }
    adjustElementHeight(element) {
        const elementHeight = window.innerHeight - element.getBoundingClientRect().top;
        element.style.maxHeight = `${elementHeight}px`;
        element.style.height = `${elementHeight}px`;
    }
}