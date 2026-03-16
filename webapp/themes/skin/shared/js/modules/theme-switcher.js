/**
 * themeSwitcher class provides manage and manipulate menus.
 */
export class themeSwitcher {
    /**
     * Constructor for the themeSwitcher class.
     * Initializes the DOM elements, gets the saved theme, and calls the init method.
     */
    constructor( defaultTheme ) {
        this.defaultTheme = defaultTheme || 'light';
        this.body = document.querySelector( 'body' );
        this.themeSwitcher = document.querySelectorAll('[data-bs-theme-value]')
        this.themeIconActive = document.querySelector('.theme-icon-active > use')
        this.init();
    }
    /**
     * Initializes the themeSwitcher by applying the saved theme, setting the active menu item,
     * adding event listeners for menu items, setting up the menu switcher.
     */
    init() {
        /* Menu Overflow management */
        this.themeSwitcher.forEach( (theme) => {
            theme.addEventListener('click', () => {
              this.resetSwitch( this.themeSwitcher )
              const activeTheme = theme.dataset.bsThemeValue
              theme.setAttribute('aria-pressed','true')
              theme.classList.add('active')
              this.body.setAttribute('data-bs-theme', activeTheme )
              this.setIcon( theme )
            })
          })
          this.resetSwitch( this.themeSwitcher )
          const defaultSwitch = this.body.querySelector(`[data-bs-theme-value="${this.defaultTheme}"]`)
          defaultSwitch.setAttribute('aria-pressed','true')
          defaultSwitch.classList.add('active')
          this.setIcon( defaultSwitch )
    }
    
    resetSwitch( switcher ){
        switcher.forEach( (sw) => {
          sw.setAttribute('aria-pressed','false')
          sw.classList.remove('active')
        })
    }
    
    setIcon( theme ){
      const themeIcon = theme.querySelector(`.theme-icon > use`).getAttribute('href')
      this.themeIconActive.setAttribute('href', themeIcon )
    }

}