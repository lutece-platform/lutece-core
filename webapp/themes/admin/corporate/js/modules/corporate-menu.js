/**
 * MenuManager class provides manage and manipulate menus.
 */
import LuteceSearchList from '../../../../shared/modules/luteceSearchList.js';
export class MenuManager {
    /**
     * Constructor for the MenuManager class.
     * Initializes the DOM elements, gets the saved theme, and calls the init method.
     */
    constructor() {
        this.toggleBtn = document.getElementById('toggle-theme');
        this.body = document.querySelector('body');
        this.savedTheme = localStorage.getItem('lutece-corporate-theme');
        this.menu = document.getElementById('menu');
        this.childMenu = document.getElementById('child-menu');
        this.switcherMenu = document.getElementById('menu-switcher');
        this.iconMenu = document.getElementById('menu-icon');
        this.init();
    }
    /**
     * Initializes the MenuManager by applying the saved theme, setting the active menu item,
     * adding event listeners for menu items, setting up the menu switcher, and adding hover listeners.
     */
    init() {
        this.applySavedTheme();
        this.setActive();
        this.addMenuEventListeners();
        this.setupSwitcherMenu();
        this.addMenuHoverListeners();
        this.addChildMenuHoverListeners();
        this.searchMenu();
    }
    /**
     * Applies the saved theme to the body and adds an event listener for theme toggle button.
     */
    applySavedTheme() {
        if (this.savedTheme) {
            this.body.setAttribute('data-bs-theme', this.savedTheme);
            if (this.savedTheme === 'dark') {
                this.toggleBtn.checked = true;
            }
        }
        this.toggleBtn.addEventListener('change', () => {
            if (this.body.getAttribute('data-bs-theme') === 'light') {
                this.body.setAttribute('data-bs-theme', 'dark');
                this.toggleBtn.checked = true;
                localStorage.setItem('lutece-corporate-theme', 'dark');
            } else {
                this.body.setAttribute('data-bs-theme', 'light');
                this.toggleBtn.checked = false;
                localStorage.setItem('lutece-corporate-theme', 'light');
            }
        });
    }
    /**
     * Sets the active menu item and updates the child menu based on the active group.
     * @param {string} activeGroup - The feature group of the active menu item.
     */
    setActive(activeGroup) {
        const active = document.querySelector('#feature-title a') && document.querySelector('#feature-title a').getAttribute('href');
        const rightListItems = this.childMenu.querySelectorAll('#right-list .list-group-item');
        const featureGroupListItems = this.childMenu.querySelectorAll('#feature-list .feature-group');
        const featureGroupMenuItems = this.menu.querySelectorAll('a');
        this.childMenu.classList.remove('child-menu-found');
        if (!activeGroup && active) {
            activeGroup = active.replace('#', '');
            rightListItems.forEach(element => {
                if (element.href.includes(active)) {
                    element.classList.add('active', 'bg-body-tertiary');
                    activeGroup = element.getAttribute('feature-group');
                    return;
                }
            });
        }
        if (activeGroup) {
            if (!activeGroup.toLowerCase().includes(".jsp")) {
                this.childMenu.classList.remove('d-none');
                rightListItems.forEach(element => {
                    if (element.getAttribute('feature-group') != activeGroup) {
                        element.classList.add('d-none');
                    } else {
                        element.classList.remove('d-none');
                        this.childMenu.classList.add('child-menu-found');
                    }
                });
                featureGroupListItems.forEach(featureGroup => {
                    if (featureGroup.getAttribute('feature-group') === activeGroup) {
                        featureGroup.classList.remove('d-none');
                        this.childMenu.classList.add('child-menu-found');
                    } else {
                        featureGroup.classList.add('d-none');
                    }
                });
                featureGroupMenuItems.forEach(featureGroup => {
                    if (featureGroup.getAttribute('feature-group') === activeGroup) {
                        featureGroup.classList.add('active', 'bg-body-tertiary');
                    } else {
                        featureGroup.classList.remove('active', 'bg-body-tertiary');
                    }
                });
            } else {
                let parts = activeGroup.split("/");
                if (parts.length >= 3) {
                    let role = parts[2];
                    featureGroupMenuItems.forEach(featureGroup => {
                        if (featureGroup.getAttribute('href').includes(`/${role}/`)) {
                            featureGroup.classList.add('active', 'bg-body-tertiary');
                        }
                    });
                }
            }
        } else {
            featureGroupMenuItems.forEach(featureGroup => {
                if (featureGroup.getAttribute('href').includes(window.location.href)) {
                    featureGroup.classList.add('active', 'bg-body-tertiary');
                }
            });
        }
    }
    /**
     * Adds click event listeners for the menu items.
     */
    addMenuEventListeners() {
        this.menu.querySelectorAll('a:not(.feature-link)').forEach(element => {
            element.addEventListener('click', e => {
                e.preventDefault();
                this.setActive(element.getAttribute('feature-group'));
            });
        });
    }
    /**
     * Sets up the menu switcher to toggle the pin state of the child menu.
     */
    setupSwitcherMenu() {
        let noPinState = localStorage.getItem('noPinState');
        if (noPinState === null) {
            noPinState = false;
            localStorage.setItem('noPinState', false);
        } else {
            this.childMenu.classList.toggle('no-pin', noPinState === 'true');
            this.iconMenu.classList.toggle('ti-lock-open', noPinState === 'true');
            this.iconMenu.classList.toggle('ti-lock', noPinState === 'false');
        }
        this.switcherMenu.addEventListener('click', () => {
            this.childMenu.classList.toggle('no-pin');
            this.iconMenu.classList.toggle('ti-lock-open');
            this.iconMenu.classList.toggle('ti-lock');
            localStorage.setItem('noPinState', this.childMenu.classList.contains('no-pin'));
        });
    }
    /**
     * Adds hover event listeners to the menu to show and hide the child menu.
     */
    addMenuHoverListeners() {
        this.menu.addEventListener('mouseover', () => {
            if (!this.childMenu.matches(':hover')) {
                this.childMenu.classList.add('child-menu-show');
            }
        });
        this.menu.addEventListener('mouseout', (event) => {
            if (!this.childMenu.matches(':hover')) {
                this.childMenu.classList.remove('child-menu-show');
            }
        });
    }
    /**
     * Adds hover event listeners to the child menu to maintain its visibility state.
     */
    addChildMenuHoverListeners() {
        this.childMenu.addEventListener('mouseover', () => {
            this.childMenu.classList.add('child-menu-show');
        });
        this.childMenu.addEventListener('mouseout', () => {
            this.childMenu.classList.remove('child-menu-show');
        });
    }
    searchMenu() {
        const searchInput = document.querySelector("#search-menu");
        const searchElementList = document.querySelectorAll("#right-list a");
        new LuteceSearchList(searchInput, searchElementList, {
            searchableChild: [".title", ".text-muted"],
            highlight: true,
            emptyMessageElement: document.querySelector("#empty-message"),
            hideClass: "d-none",
            extraSearchFunction: () => {
                const featureList = document.querySelector("#feature-list");
                if (searchInput.value.length > 0) {
                    featureList.classList.add("d-none");
                } else {
                    featureList.classList.remove("d-none");
                    searchElementList.forEach(element => {
                        element.classList.add("d-none");
                    });
                    const featureGroup = document.querySelector("#feature-list .feature-group:not(.d-none)").getAttribute("feature-group");
                    if (featureGroup && featureGroup !== "home") {
                        this.setActive(featureGroup);
                    }
                }
            }
        });
    }
}