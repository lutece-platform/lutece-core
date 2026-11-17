/****************************************************/
/* SPECIFIC THEME CUSTOM SITE SCRIPTS OVERRIDES     */
/****************************************************/
(() => {
  'use strict'

    // Preloader js    
    window.addEventListener('load', function () {
        const preloader = document.querySelector('.preloader');
        if (preloader) {
            preloader.style.opacity = 0;
            setTimeout(() => {
                preloader.style.display = 'none';
            }, 100);
        }
    });

    // navfixed
    window.addEventListener('scroll', function () {
        const navigation = document.querySelector('.navigation');
        if (navigation) {
            if (navigation.getBoundingClientRect().top + window.scrollY > 50) {
                navigation.classList.add('nav-bg');
            } else {
                navigation.classList.remove('nav-bg');
            }
        }
    });

    // clipboard
    document.addEventListener('DOMContentLoaded', function() {
        let clipInit = false;
        document.querySelectorAll('code').forEach(function(codeBlock) {
            const text = codeBlock.textContent;
            if (text.length > 2) {
                if (!clipInit) {
                    new ClipboardJS('.copy-to-clipboard', {
                        text: function(trigger) {
                            return trigger.previousElementSibling.textContent.replace(/^\$\s/gm, '');
                        }
                    });
                    clipInit = true;
                }
                
                const copyButton = document.createElement('span');
                copyButton.className = 'copy-to-clipboard';
                copyButton.textContent = 'copy';
                codeBlock.after(copyButton);
            }
        });
        
        document.addEventListener('click', function(e) {
            if (e.target && e.target.classList.contains('copy-to-clipboard')) {
                e.target.textContent = 'copied';
            }
        });
    });

    // Paris.fr sprite icons in contributed content ( forms comments, html... ) :
    // when the theme does not include the sprite, the <use> points to nothing.
    // Replace those icons with the Tabler icon cAlert renders for the same type.
    document.addEventListener('DOMContentLoaded', function() {
        const tablerIcons = {
            'alert-info': 'info-circle',
            'alert-warning': 'alert-triangle',
            'alert-triangle': 'alert-triangle',
            'alert-danger': 'ban',
            'alert-error': 'ban',
            'alert-success': 'circle-check',
            'alert-check': 'circle-check',
            'alert-close': 'x'
        };
        document.querySelectorAll('svg.paris-icon').forEach(function(svg) {
            const use = svg.querySelector('use');
            const ref = use ? ( use.getAttribute('href') || use.getAttribute('xlink:href') ) : null;
            if (!ref || !ref.startsWith('#') || document.getElementById(ref.slice(1))) {
                return;
            }
            const name = Array.from(svg.classList).find(c => c.startsWith('paris-icon-'));
            const tablerName = name ? tablerIcons[name.slice('paris-icon-'.length)] : null;
            if (!tablerName) {
                return;
            }
            const icon = document.createElement('span');
            icon.className = 'ti ti-' + tablerName;
            const title = svg.querySelector('title');
            if (title && title.textContent.trim() !== '') {
                icon.title = title.textContent.trim();
            } else {
                icon.setAttribute('aria-hidden', 'true');
            }
            svg.replaceWith(icon);
        });
    });

    // search
    document.addEventListener('DOMContentLoaded', function() {
        const searchBy = document.getElementById('search-by');
        if (searchBy) {
            searchBy.addEventListener('input', function() {
                if (this.value) {
                    this.classList.add('active');
                } else {
                    this.classList.remove('active');
                }
            });
        }
    });

    // Accordions with Bootstrap 5
    document.addEventListener('DOMContentLoaded', function() {
        const collapseElementList = [].slice.call(document.querySelectorAll('.collapse'));
        collapseElementList.map(function(collapseEl) {
            collapseEl.addEventListener('show.bs.collapse', function() {
                const parent = this.parentElement;
                const icon = parent.querySelector('.ti-plus');
                if (icon) {
                    icon.classList.remove('ti-plus');
                    icon.classList.add('ti-minus');
                }
            });
            
            collapseEl.addEventListener('hide.bs.collapse', function() {
                const parent = this.parentElement;
                const icon = parent.querySelector('.ti-minus');
                if (icon) {
                    icon.classList.remove('ti-minus');
                    icon.classList.add('ti-plus');
                }
            });
        });
    });

    /*!
    * Color mode toggler for Bootstrap's docs (https://getbootstrap.com/)
    * Copyright 2011-2025 The Bootstrap Authors
    * Licensed under the Creative Commons Attribution 3.0 Unported License.
    */

    // Storage can be blocked (private browsing, site data disabled) and the
    // stored value tampered with: both fall back to the default theme.
    const THEMES = ['light', 'dark', 'auto']
    const getStoredTheme = () => {
        try {
            const theme = localStorage.getItem('theme')
            return THEMES.includes(theme) ? theme : null
        } catch (e) {
            return null
        }
    }
    const setStoredTheme = theme => {
        try {
            localStorage.setItem('theme', theme)
        } catch (e) {
            // storage blocked: the choice lasts for this page only
        }
    }
    // Default theme rendered by the server. A FO theme that applies the mode
    // before the first paint keeps it in data-bs-theme-default.
    const defaultThemeAtLoad = document.documentElement.getAttribute('data-bs-theme-default') || document.documentElement.getAttribute('data-bs-theme')
    const getDefaultTheme = () => defaultThemeAtLoad

    // Back office right "the user can manage the light / dark mode" (theme_frameset_header_js.html).
    // Without it the mode set in the back office stays: no stored choice, no system preference.
    const canSwitchTheme = () => typeof hasUserThemeSwitch === 'undefined' || hasUserThemeSwitch === true

    const getPreferredTheme = () => {
        const defaultTheme = getDefaultTheme()
        if (!canSwitchTheme()) {
            return defaultTheme === 'dark' ? 'dark' : 'light'
        }
        const storedTheme = getStoredTheme()

        if ( !storedTheme && defaultTheme && defaultTheme === 'dark') {
            return defaultTheme
        }

        if (storedTheme) {
            return storedTheme
        }

        return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
    }

    const getSystemTheme = () => window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'

    const setTheme = theme => {
        const rendered = theme === 'auto' ? getSystemTheme() : theme
        document.documentElement.setAttribute('data-bs-theme', rendered)
        // Hint for the browser UI drawn before the CSS (page canvas, scrollbars)
        const meta = document.querySelector('meta[name="color-scheme"]')
        if (meta) {
            meta.content = rendered
        }
    }

    setTheme(getPreferredTheme())

    const showActiveTheme = (theme, focus = false) => {
        const themeSwitcher = document.querySelector('#bd-theme')

        if (!themeSwitcher) {
            return
        }

        const themeSwitcherText = document.querySelector('#bd-theme-text')
        const btnToActive = document.querySelector(`[data-bs-theme-value="${theme}"]`)

        if (!btnToActive) {
            return
        }
        
        document.querySelectorAll('[data-bs-theme-value]').forEach(element => {
            element.classList.remove('active')
            element.setAttribute('aria-pressed', 'false')
        })

        btnToActive.classList.add('active')
        btnToActive.setAttribute('aria-pressed', 'true')
        const themeSwitcherLabel = `${themeSwitcherText.textContent} (${btnToActive.dataset.bsThemeValue})`
        themeSwitcher.setAttribute('aria-label', themeSwitcherLabel)

        if (focus) {
            themeSwitcher.focus()
        }
    }

    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', () => {
        if (!canSwitchTheme()) {
            return
        }
        const storedTheme = getStoredTheme()
        if (storedTheme !== 'light' && storedTheme !== 'dark') {
            setTheme(getPreferredTheme())
        }
    })

    // A choice made in another tab applies here too
    window.addEventListener('storage', event => {
        if (event.key === 'theme' && canSwitchTheme()) {
            const theme = getPreferredTheme()
            setTheme(theme)
            showActiveTheme(theme)
        }
    })

    // Two-state toggle (a single button in the page header): switch to the opposite of the
    // rendered mode. Going back to the system's mode stores 'auto' rather than pinning it,
    // so the page keeps following the system afterwards.
    window.toggleColorMode = () => {
        const target = document.documentElement.getAttribute('data-bs-theme') === 'dark' ? 'light' : 'dark'
        const theme = target === getSystemTheme() ? 'auto' : target
        setStoredTheme(theme)
        setTheme(theme)
        showActiveTheme(theme)
        return target
    }

    window.addEventListener('DOMContentLoaded', () => {
        showActiveTheme(getPreferredTheme())

        document.querySelectorAll('[data-bs-theme-value]')
        .forEach(toggle => {
            toggle.addEventListener('click', () => {
            const theme = toggle.getAttribute('data-bs-theme-value')
            setStoredTheme(theme)
            setTheme(theme)
            showActiveTheme(theme, true)
            })
        })
    })
})();