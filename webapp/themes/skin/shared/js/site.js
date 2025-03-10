/****************************************************/
/* SPECIFIC THEME CUSTOM SITE SCRIPTS OVERRIDES     */
/****************************************************/
/**
 * WEBSITE: https://themefisher.com
 * TWITTER: https://twitter.com/themefisher
 * FACEBOOK: https://www.facebook.com/themefisher
 * GITHUB: https://github.com/themefisher/
 */
(function () {
    'use strict';

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
})();