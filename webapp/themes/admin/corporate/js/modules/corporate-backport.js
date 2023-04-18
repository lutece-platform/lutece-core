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
        const page = document.getElementById('corporate-page');
        if (!page) {
            const lutecePageWrappers = document.querySelectorAll('.lutece-page-wrapper');
            lutecePageWrappers.forEach(wrapper => {
                if (!wrapper.querySelector('main')) {
                    wrapper.classList.add('no-main', 'overflow-auto');
                }
            });
        }
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
}