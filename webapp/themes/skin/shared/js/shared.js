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

    const getStoredTheme = () => localStorage.getItem('theme')
    const setStoredTheme = theme => localStorage.setItem('theme', theme)
    const getDefaultTheme = () => document.documentElement.getAttribute('data-bs-theme')

    const getPreferredTheme = () => {
        const defaultTheme = getDefaultTheme()
        const storedTheme = getStoredTheme()
        
        if ( !storedTheme && defaultTheme && defaultTheme === 'dark') {
            return defaultTheme
        }

        if (storedTheme) {
            return storedTheme
        }

        return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
    }

    const setTheme = theme => {
        if (theme === 'auto') {
            document.documentElement.setAttribute('data-bs-theme', (window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'))
        } else {
            document.documentElement.setAttribute('data-bs-theme', theme)
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
        const storedTheme = getStoredTheme()
        if (storedTheme !== 'light' && storedTheme !== 'dark') {
            setTheme(getPreferredTheme())
        }
    })

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