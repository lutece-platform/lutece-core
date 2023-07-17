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
        this.mainMenu = document.getElementById('main-menu');
        this.mainMenuItems = this.mainMenu.querySelectorAll('#main-menu li a');
        this.body = document.querySelector('body');
        this.savedTheme = localStorage.getItem('lutece-corporate-theme');
        this.readMode = localStorage.getItem('lutece-bo-readmode');
        this.readModeToggle = document.getElementById('lutece-rtl');
        this.savedThemeMenu = localStorage.getItem('lutece-corporate-theme-menu');
        this.menu = document.getElementById('menu');
        this.childMenu = document.getElementById('child-menu');
        this.switcherMenu = document.getElementById('menu-switcher');
        this.mobileMenu = document.getElementById('menu-mobile');
        this.mobileMenuClose = document.getElementById('menu-mobile-close');
        this.mobileMenuBack = document.getElementById('menu-mobile-back');
        this.iconMenu = document.getElementById('menu-icon');
        this.rotateMenu = document.getElementById('menu-rotate');
        this.init();
    }
    /**
     * Initializes the MenuManager by applying the saved theme, setting the active menu item,
     * adding event listeners for menu items, setting up the menu switcher, and adding hover listeners.
     */
    init() {
        if ( this.readModeToggle != null ) {
            this.applyReadMode();
        }    
        if( this.toggleBtn != null ){
            this.applySavedTheme();
        }
        if( this.rotateMenu != null ){
            this.applySavedThemeMenu();
        }
        this.setupSwitcherMenu();
        this.setActive();
        this.addMenuEventListeners();
        this.setupMobileMenu();
        this.closeChildMenuOnClickOutside();
        this.searchMenu();
    }
    /**
     * Applies the saved theme to the body and adds an event listener for theme toggle button.
     */
    applyReadMode() {
        if ( this.readMode === null ) {
            this.body.removeAttribute('dir');
        } else {
            this.body.setAttribute('dir', this.readMode );
        }
        this.readModeToggle.addEventListener('click', (e) => {
            e.preventDefault()
            if ( this.body.getAttribute('dir') === null ) {
                this.body.setAttribute('dir', 'rtl');
                localStorage.setItem('lutece-bo-readmode', 'rtl');
            } else {
                this.body.removeAttribute('dir');
                localStorage.removeItem('lutece-bo-readmode');
            }
        });
    }
    /**
     * Applies the saved theme to the body and adds an event listener for theme toggle button.
     */
    applySavedTheme() {
        if ( this.savedTheme ) {
            this.body.setAttribute('data-bs-theme', this.savedTheme);
            if (this.savedTheme === 'dark') {
                this.toggleBtn.checked = true;
            }
        }
        
        this.toggleBtn.addEventListener('click', () => {

            this.toggleTheme()
        });

        this.toggleBtn.addEventListener('keydown', ( keyboardEvent ) => {
            switch (keyboardEvent.key) {
                case 'Enter':
                case 'Space':
                    keyboardEvent.preventDefault();
                    this.toggleTheme()
                    break;
            }
        }); 
    }
    /**
     * Applies the saved theme to the body and adds an event listener for theme toggle button.
     */
    applySavedThemeMenu() {
        if (this.savedThemeMenu) {
            this.body.setAttribute('data-bs-theme-menu', this.savedThemeMenu);
        }
        
        ( !this.savedThemeMenu || this.savedThemeMenu === 'top' ) && setTimeout(() => {
            this.menuTooltips(false);
        }, 0);

        this.rotateMenu.addEventListener('click',() => {
            if (this.body.getAttribute('data-bs-theme-menu') === 'left') {
                this.body.setAttribute('data-bs-theme-menu', 'top');
                localStorage.setItem('lutece-corporate-theme-menu', 'top');
                this.menuTooltips(false);
            } else {
                this.body.setAttribute('data-bs-theme-menu', 'left');
                localStorage.setItem('lutece-corporate-theme-menu', 'left');
                this.menuTooltips(true);
            }
        });

    }
    /**
     *  Theme toggle button function.
     */
    toggleTheme(){
        if (this.body.getAttribute('data-bs-theme') === 'light') {
            this.body.setAttribute('data-bs-theme', 'dark');
            this.toggleBtn.querySelector( '.darkmode-sun' ).style.animationName = 'spin-fast';
            localStorage.setItem('lutece-corporate-theme', 'dark');
        } else {
            this.body.setAttribute('data-bs-theme', 'light');
            this.toggleBtn.querySelector('.darkmode-moon').style.animationName = 'spin-backwards';
            localStorage.setItem('lutece-corporate-theme', 'light');
        }
    }
    /**
     *  Tooltips
     */
    menuTooltips(init) {
        let tooltipTriggerList = [].slice.call(this.mainMenu.querySelectorAll('a[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            let tooltipInstance = bootstrap.Tooltip.getInstance(tooltipTriggerEl);
            if (init) {
                // Initialize tooltips if they don't already exist
                if (!tooltipInstance) {
                    new bootstrap.Tooltip(tooltipTriggerEl);
                }
            } else {
                // Dispose of tooltips if they exist
                if (tooltipInstance) {
                    tooltipInstance.dispose();
                }
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
            if ( !activeGroup.toLowerCase().includes( '.jsp' ) ) {
                this.childMenu.classList.remove('d-none');
                let nEl = 0;
                let activeElement = null;
                let firstGroupElement = null;
                
                rightListItems.forEach(element => {
                    if ( element.getAttribute('feature-group') != activeGroup ) {
                        element.classList.add('d-none');
                    } else {
                        if (!firstGroupElement) {
                            firstGroupElement = element;
                        }
                        if ( element.classList.contains( 'active' ) ) {
                            activeElement = element;
                        }
                        element.classList.remove('d-none');
                        this.childMenu.classList.add( 'child-menu-found' );
                        this.childMenu.setAttribute( 'data-featuregroup', activeGroup);
                    }
                });
                
                if (activeElement) {
                    activeElement.focus();
                    activeElement.classList.add('menu-selected');
                    activeElement.setAttribute('aria-current','page');

                } else if (firstGroupElement) {
                    firstGroupElement.classList.add('menu-selected');
                    firstGroupElement.focus();
                }
               
                featureGroupListItems.forEach(featureGroup => {
                    if (featureGroup.getAttribute('feature-group') === activeGroup) {
                        featureGroup.classList.remove('d-none');
                        this.childMenu.classList.add('child-menu-found');
                    } else {
                        featureGroup.classList.add('d-none');
                    }
                });
                featureGroupMenuItems.forEach(featureGroup => {
                    if (featureGroup.getAttribute( 'feature-group' ) === activeGroup) {
                        featureGroup.classList.add( 'active', 'bg-body-tertiary' );
                    } else {
                        featureGroup.classList.remove( 'active', 'bg-body-tertiary' );
                    }
                });
            } else {
                let parts = activeGroup.split( '/' );
                if (parts.length >= 3) {
                    let role = parts[2];
                    featureGroupMenuItems.forEach(featureGroup => {
                        if (featureGroup.getAttribute('href').includes( `/${role}/` ) ) {
                            featureGroup.classList.add('active', 'bg-body-tertiary');
                        }
                    });
                }
            }
        } else {
            featureGroupMenuItems.forEach(featureGroup => {
                if (featureGroup.getAttribute( 'href' ).includes(window.location.href)) {
                    featureGroup.classList.add( 'active', 'bg-body-tertiary' );
                }
            });
        }
    }

    /**
     * Adds click event listeners for the menu.
     */
    addMenuEventListeners() {
        const menus = this.menu.querySelectorAll('a:not(.feature-link)');
        menus.forEach(element => {
            element.addEventListener( 'click', e => {
                e.preventDefault();
                this.setActive( element.getAttribute('feature-group') );
                this.childMenu.classList.add('child-menu-show');
                menus.forEach(element => {
                    element.ariaExpanded = "false";
                });
                if( e.target.tagName === 'I' ){
                    if( e.target.parentElement.ariaExpanded === "true" ){
                        e.target.parentElement.ariaExpanded = "false" ;
                    } else {
                        e.target.parentElement.ariaExpanded = "true" ;
                    }
                } else {
                    if( e.target.ariaExpanded === "true" ){
                        e.target.ariaExpanded === "false";
                    } else {
                        e.target.ariaExpanded === "true";
                    }
                 }
            });
        });
        
        this.menu.addEventListener( "keydown", ( keyboardEvent ) => {
            const activeMenuItem = this.menu.querySelector('.active');
            switch (keyboardEvent.key) {
                case 'Escape':
                    keyboardEvent.preventDefault();
                    break;
                case 'ArrowUp':
                case 'ArrowLeft':
                    this.selectMenu('up', menus[0], 'active', activeMenuItem );
                    break;
                case 'ArrowDown':
                case 'ArrowRight':
                    keyboardEvent.preventDefault();
                    this.selectMenu('down', menus[0], 'active', activeMenuItem );
                    break;
                case 'Home':
                    keyboardEvent.preventDefault();
                    menus[0].focus();
                    break;
                case 'End':
                    keyboardEvent.preventDefault();
                    menus[menus.length - 1].focus();
                    break;
            }
            
        });

        this.childMenu.addEventListener( "keydown", (keyboardEvent) => {
            const featureGroup = `[feature-group="${this.childMenu.dataset.featuregroup}"]`;
            const rightListItems = this.childMenu.querySelectorAll(`#right-list .list-group-item${featureGroup}`);
            const activeMenuItem = this.childMenu.querySelector(`#right-list .list-group-item${featureGroup}.menu-selected`);
            switch (keyboardEvent.key) {
                case 'Escape':
                    this.childMenu.classList.remove('child-menu-show');
                    this.menu.querySelector(featureGroup).setAttribute('aria-expanded','false');
                    this.childMenu.classList.add('d-none');
                    this.menu.querySelector( featureGroup ).focus()
                    break;
                case 'ArrowUp':
                    this.selectMenu( 'up', rightListItems[0], 'menu-selected', activeMenuItem );
                    break;
                case 'ArrowDown':
                    keyboardEvent.preventDefault();
                    this.selectMenu( 'down', rightListItems[0], 'menu-selected', activeMenuItem );
                    break;
                case 'Home':
                    keyboardEvent.preventDefault();
                    rightListItems[0].focus();
                    break;
                case 'End':
                    keyboardEvent.preventDefault();
                    rightListItems[ rightListItems.length - 1 ].focus();
                    break;
            }
        });
    }
    
    /**
     * Sets up the menu switcher to toggle the pin state of the child menu.
     */
    selectMenu( dir, item, sel, activeItem ){
        if( activeItem != undefined && activeItem != null ){
            activeItem.classList.remove( sel );
            activeItem.removeAttribute( 'aria-current' );
            let elem = null;
            if( dir === 'down' ){
                elem = activeItem.nextElementSibling
                if( sel === 'active' ){
                    elem = activeItem.parentElement.nextElementSibling.firstElementChild;
                }
            } else {
                elem = activeItem.previousElementSibling;
                if( sel === 'active' ){
                    elem = activeItem.parentElement.previousElementSibling.firstElementChild;
                }
            }
            elem.classList.add( sel );
            activeItem.setAttribute( 'aria-current', 'page' );
            elem.focus()
        } else {
            item.classList.add( sel )
            item.setAttribute( 'aria-current', 'page' );
            item.focus( )
        }
    }

    /**
     * Sets up the mobile menu events
     */    
    setupMobileMenu() {
        this.mobileMenu.addEventListener('click', () => {
           document.body.classList.toggle('menu-mobile-show');
           this.menuTooltips(false);
        });
        this.mobileMenuClose.addEventListener('click', () => {
            document.body.classList.remove('menu-mobile-show','menu-mobile-sub-show');
            this.childMenu.classList.remove('child-menu-show');
            this.menuTooltips(true);
        });
        this.mobileMenuBack.addEventListener('click', () => {
            document.body.classList.remove('menu-mobile-sub-show');
            this.mobileMenuBack.classList.add('d-none');
            this.childMenu.classList.remove('child-menu-show');
        });
        this.mainMenuItems.forEach(element => {
            element.addEventListener('click', e => {
              if (document.body.classList.contains('menu-mobile-show')) {
                document.body.classList.add('menu-mobile-sub-show');
                this.mobileMenuBack.classList.remove('d-none');
              }
            });
          });
    }
    
    /**
     * Sets up the menu switcher to toggle the pin state of the child menu.
     */
    setupSwitcherMenu() {
        let pin = localStorage.getItem('lutece-corporate-menu-pin');
        if (pin === null) {
            pin = false;
            localStorage.setItem('lutece-corporate-menu-pin', pin);
        }
        if( pin === 'true' ) {
            this.childMenu.classList.remove('no-pin');
            this.iconMenu.classList.add('ti-lock');
        }
        else {
            this.iconMenu.classList.add('ti-lock-open');
        }
        this.switcherMenu.addEventListener('click', () => {
            this.childMenu.classList.toggle('no-pin');
            this.iconMenu.classList.toggle('ti-lock-open');
            this.iconMenu.classList.toggle('ti-lock');
            localStorage.setItem('lutece-corporate-menu-pin', !this.childMenu.classList.contains('no-pin'));
        });
    }
    /**
     * Adds a click event listener to the document to close the child menu when clicking outside of the menu elements.
     */
    closeChildMenuOnClickOutside() {
        document.addEventListener('click', (event) => {
            if (!this.menu.contains(event.target) && !this.childMenu.contains(event.target) && this.childMenu.classList.contains('child-menu-show')) {
                this.childMenu.classList.remove('child-menu-show');
                this.menu.querySelectorAll('a:not(.feature-link)').forEach(element => {
                    element.setAttribute('aria-expanded','false')
                });
            }
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