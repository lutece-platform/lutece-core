/**
 * MenuManager class provides manage and manipulate menus.
 */
export class themeMenuManager {
    /**
     * Constructor for the MenuManager class.
     * Initializes the DOM elements, gets the saved theme, and calls the init method.
     */
    constructor( ) {
        this.body = document.querySelector( 'body' );
        this.mainMenu = document.getElementById( 'navbarMainMenu' );
        this.longMenu = this.mainMenu.dataset.longMenu;
        this.mainMenuBtn = this.mainMenu.querySelector( 'button' );
        this.mainMenuVisible = document.querySelector( '#navbarMainMenu > .navbar-main > .navbar-main' );
        this.mainMenuHiddenLinks = document.getElementById( '#navbar-hidden-links' );
        this.mainMenuItems = this.mainMenu.querySelectorAll( '#main-menu li a' );
        this.breakWidths = [];
        this.init();
    }
    /**
     * Initializes the MenuManager by applying the saved theme, setting the active menu item,
     * adding event listeners for menu items, setting up the menu switcher, and adding hover listeners.
     */
    init() {
        /* Menu Overflow management */
        if ( this.longMenu == 1 ){
            let totalSpace = 0;
            let numOfItems = 0;

            // Get initial state
            const visibleChilds = this.mainMenuVisible.children
            Array.from( visibleChilds ).forEach( visibleChild => {
                let w = this.outerWidth( visibleChild )
                totalSpace += w;
                numOfItems += 1;
                this.breakWidths.push( totalSpace );
            });

            this.mainMenuBtn.addEventListener( 'click', () => {
                this.mainMenuHiddenLinks.classList.toggle('hidden');
            });

            window.onresize = (event) => {
                this.menuCheck( this.mainMenuVisible.childElementCount );
            }
            this.menuCheck( this.mainMenuVisible.childElementCount);
        }
    }
    
    menuCheck( numOfVisibleItems ){
        let requiredSpace;
        // Get instant state
        requiredSpace = this.breakWidths[ numOfVisibleItems  - 1 ];
        const availableSpace = this.mainMenu.style.width - 180;
        // There is not enought space
        if ( requiredSpace > availableSpace ) {
            this.mainMenuHiddenLinks.prepend( this.mainMenuVisible.lastElementChild );
            numOfVisibleItems -= 1;
            menuCheck( numOfVisibleItems );
        // There is more than enough space
        } else if ( availableSpace > breakWidths[ numOfVisibleItems ] ) {
            this.mainMenuVisible.append( this.mainMenuHiddenLinks.firstElementChild );
            numOfVisibleItems += 1;
        }

        // Update the button accordingly
        const nHLinks= this.mainMenuHiddenLinks.childElementCount 
        this.mainMenuBtn.dataset.count = nHLinks;
        if ( nHLinks === 0 ) {
            this.mainMenuBtn.classList.add( 'hidden' );
            this.mainMenu.classList.remove( 'count' );
        } else {
            this.mainMenu.classList.add( 'count' );
            this.mainMenuBtn.classList.remove( 'hidden' );
        }
    }

    outerWidth(el) {
        const style = getComputedStyle(el);
        return ( el.getBoundingClientRect().width + parseFloat(style.marginLeft) +  parseFloat(style.marginRight ) );
    }

}