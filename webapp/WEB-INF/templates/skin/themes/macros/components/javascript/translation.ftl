<#function getDefaultLang>
<#local defaultLang = dskey('portal.theme.site_property.menu.translate.lang')!'fr'>
<#return defaultLang>
</#function>
<#function getAllowedLangs>
<#local langsConfig = dskey('portal.theme.site_property.menu.translate.langs.textblock')>
<#local defaultLang = getDefaultLang()>
<#local allowedLangs = []>
<#if langsConfig?? && langsConfig?trim != ''>
<#list langsConfig?split(',') as item>
<#local trimmed = item?trim>
<#if trimmed != ''><#local allowedLangs = allowedLangs + [trimmed?lower_case]></#if>
</#list>
</#if>
<#local allowedLangs = allowedLangs + [defaultLang?lower_case] >
<#return allowedLangs>
</#function>
<#macro translationMenu>
<#local langSearch = dskey('portal.theme.site_property.menu.translate.search.checkbox')!'0'>
<#local defaultLang = getDefaultLang()>
<#local allowedLangs = getAllowedLangs()>
<#-- Skip the menu entirely when no language is configured, or only the default one -->
<#if allowedLangs?size == 0 || (allowedLangs?size == 1 && allowedLangs[0]?lower_case == defaultLang?lower_case)><#return></#if>
<#local availableLangs = [
    { 'code': 'fr',    'label': '&#127467;&#127479; Français' },
    { 'code': 'en',    'label': '&#127468;&#127463; English' },
    { 'code': 'de',    'label': '&#127465;&#127466; Deutsch' },
    { 'code': 'nl',    'label': '&#127475;&#127473; Nederlands' },
    { 'code': 'it',    'label': '&#127470;&#127481; Italiano' },
    { 'code': 'es',    'label': '&#127466;&#127480; Español' },
    { 'code': 'pt',    'label': '&#127477;&#127481; Português' },
    { 'code': 'mt',    'label': '&#127474;&#127481; Malti' },
    { 'code': 'ga',    'label': '&#127470;&#127466; Gaeilge' },
    { 'code': 'da',    'label': '&#127465;&#127472; Dansk' },
    { 'code': 'sv',    'label': '&#127480;&#127466; Svenska' },
    { 'code': 'fi',    'label': '&#127467;&#127470; Suomi' },
    { 'code': 'el',    'label': '&#127468;&#127479; Ελληνικά' },
    { 'code': 'ro',    'label': '&#127479;&#127476; Română' },
    { 'code': 'hr',    'label': '&#127469;&#127479; Hrvatski' },
    { 'code': 'sl',    'label': '&#127480;&#127470; Slovenščina' },
    { 'code': 'cs',    'label': '&#127464;&#127487; Čeština' },
    { 'code': 'sk',    'label': '&#127480;&#127472; Slovenčina' },
    { 'code': 'hu',    'label': '&#127469;&#127482; Magyar' },
    { 'code': 'pl',    'label': '&#127477;&#127473; Polski' },
    { 'code': 'bg',    'label': '&#127463;&#127468; Български' },
    { 'code': 'lt',    'label': '&#127473;&#127481; Lietuvių' },
    { 'code': 'lv',    'label': '&#127473;&#127483; Latviešu' },
    { 'code': 'et',    'label': '&#127466;&#127466; Eesti' },
    { 'code': 'tr',    'label': '&#127481;&#127479; Türkçe' },
    { 'code': 'ru',    'label': '&#127479;&#127482; Русский' },
    { 'code': 'ar',    'label': '&#127480;&#127462; العربية' },
    { 'code': 'zh-CN', 'label': '&#127464;&#127475; 中文' },
    { 'code': 'ja',    'label': '&#127471;&#127477; 日本語' },
    { 'code': 'ko',    'label': '&#127472;&#127479; 한국어' }
]>
<#local defaultLangLabel = defaultLang>
<#list availableLangs as l><#if l.code?lower_case == defaultLang><#local defaultLangLabel = l.label><#break></#if>
</#list>
<li class="nav-item dropdown">
    <button class="btn btn-sm btn-outline-primary dropdown-toggle notranslate" translate="no" type="button" data-bs-toggle="dropdown" aria-expanded="false" id="lang-btn">
    ${defaultLangLabel}
    </button>
    <ul class="dropdown-menu dropdown-menu-end w-100 p-0 notranslate" translate="no">
        <#if langSearch == '1'><li class="lang-search-li"><input type="text" class="form-control form-control-sm border-0 border-bottom rounded-0" id="lang-search" placeholder="Search..." autocomplete="off"></li></#if>
        <#list availableLangs as l>
            <#if allowedLangs?seq_contains(l.code?lower_case)>
                <#local itemClass = 'nav-link dropdown-item' + (l.code?lower_case == defaultLang)?then(' active', '')>
                <li class="nav-item"><a class="${itemClass}" href="#" data-lang="${l.code}">${l.label}</a></li>
            </#if>
        </#list>
    </ul>
</li>
</#macro>
<#--
    Anti-flash (FOUC) guard, meant to be rendered as early as possible in <head>.
    When a non-default translation is going to be applied (Google Translate cookie,
    previous user choice or browser language), we hide <body> before the first paint
    so the user never sees the untranslated content. The page is revealed as soon as
    Google Translate flags <html> as translated, with a safety timeout as fallback.
-->
<#macro translationHead>
<#local defaultLang = getDefaultLang()>
<#local allowedLangs = getAllowedLangs()>
<#-- Nothing to translate: no extra language configured beyond the default -->
<#if allowedLangs?size == 0 || (allowedLangs?size == 1 && allowedLangs[0]?lower_case == defaultLang?lower_case)><#return></#if>
<style>html.lang-translating body{visibility:hidden!important}</style>
<script blocking="render">
(function() {
    var defaultLang = '${defaultLang}';
    // Returns the language the page is about to be translated into, or null.
    // Mirrors the auto-translate logic in translationInit so we only hide the
    // page when a translation will actually happen.
    function pendingLang() {
        var m = document.cookie.match(new RegExp('googtrans=\\/' + defaultLang + '\\/([^;]+)'));
        if (m && m[1] !== defaultLang) return m[1];
        if (m) return null;
        var choice = localStorage.getItem('userLangChoice');
        if (choice) return choice !== defaultLang ? choice : null;
        var browserLang = (navigator.language || navigator.userLanguage || defaultLang).substring(0, 2);
        return browserLang !== defaultLang ? browserLang : null;
    }
    if (!pendingLang()) return;
    var html = document.documentElement;
    html.classList.add('lang-translating');
    function reveal() { html.classList.remove('lang-translating'); }
    // Google Translate adds `translated-ltr`/`translated-rtl` on <html> once applied.
    var obs = new MutationObserver(function() {
        if (html.classList.contains('translated-ltr') || html.classList.contains('translated-rtl')) {
            obs.disconnect();
            setTimeout(reveal, 80);
        }
    });
    obs.observe(html, { attributes: true, attributeFilter: ['class'] });
    // Safety net: never keep the page hidden for more than 3s.
    setTimeout(function() { obs.disconnect(); reveal(); }, 3000);
})();
</script>
</#macro>
<#macro translationInit>
<#local langSearch = dskey('portal.theme.site_property.menu.translate.search.checkbox')!'0'>
<#local defaultLang = getDefaultLang()>
<#local allowedLangs = getAllowedLangs()>
<#-- Skip the menu entirely when no language is configured, or only the default one -->
<#if allowedLangs?size == 0 || (allowedLangs?size == 1 && allowedLangs[0]?lower_case == defaultLang?lower_case)><#return></#if>
<div id="google_translate_element" style="display:none;"></div>
<script  blocking="render" >
const defaultLang = '${defaultLang}';

function googleTranslateElementInit() {
    new google.translate.TranslateElement({
        pageLanguage: defaultLang,
        autoDisplay: false
    }, 'google_translate_element');
}

function translatePage(lang) {
    let attempts = 0;
    let tryTranslate = setInterval(function() {
        let combo = document.querySelector('.goog-te-combo');
        if (combo) {
            combo.value = lang;
            combo.dispatchEvent(new Event('change'));
            clearInterval(tryTranslate);
        }
        if (++attempts > 50) clearInterval(tryTranslate);
    }, 100);
}

function getStoredLang() {
    let re = new RegExp('googtrans=\\/' + defaultLang + '\\/([^;]+)');
    let match = document.cookie.match(re);
    return match ? match[1] : null;
}

function syncDropdownToLang(lang) {
    let item = document.querySelector('[data-lang="' + lang + '"]');
    if (!item) return;
    let btn = document.getElementById('lang-btn');
    btn.textContent = item.textContent;
    document.querySelectorAll('[data-lang]').forEach(function(el) { el.classList.remove('active'); });
    item.classList.add('active');
}

// Google Translate forces `body { top: 40px }` inline after each translation
// to make room for its banner. We keep the banner hidden via CSS, so we also
// clear any inline top/position that Google sets on <body>.
function resetBodyOffset() {
    if (document.body.style.top) document.body.style.top = '';
    if (document.body.style.position) document.body.style.position = '';
}

document.addEventListener('DOMContentLoaded', function() {
    resetBodyOffset();
    new MutationObserver(resetBodyOffset).observe(document.body, {
        attributes: true,
        attributeFilter: ['style']
    });

    let themeSwitch = document.getElementById('theme-switch');
    let isDark = document.documentElement.getAttribute('data-bs-theme') === 'dark';
    if (isDark) {
        themeSwitch.checked = true;
    }

    // Restore dropdown state from cookie
    let storedLang = getStoredLang();
    if (storedLang) {
        syncDropdownToLang(storedLang);
    }

    document.querySelectorAll('[data-lang]').forEach(function(item) {
        item.addEventListener('click', function(e) {
            e.preventDefault();
            let lang = this.getAttribute('data-lang');
            let btn = document.getElementById('lang-btn');
            btn.textContent = this.textContent;
            document.querySelectorAll('[data-lang]').forEach(function(el) { el.classList.remove('active'); });
            this.classList.add('active');
            bootstrap.Dropdown.getOrCreateInstance(btn).hide();
            localStorage.setItem('userLangChoice', lang);
            if (lang === defaultLang) {
                // Reset to original: remove Google Translate cookie on all domain levels and reload
                let expiry = 'googtrans=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/';
                document.cookie = expiry + ';';
                let parts = location.hostname.split('.');
                for (let i = 0; i < parts.length - 1; i++) {
                    document.cookie = expiry + '; domain=.' + parts.slice(i).join('.');
                }
                location.reload();
            } else {
                translatePage(lang);
            }
        });
    });
<#if langSearch == '1'>
    // Language search filter
    let langSearch = document.getElementById('lang-search');
    langSearch.addEventListener('input', function() {
        let query = this.value.toLowerCase();
        document.querySelectorAll('[data-lang]').forEach(function(item) {
            let text = item.textContent.toLowerCase();
            item.closest('li').style.display = text.includes(query) ? '' : 'none';
        });
    });
    document.querySelector('.dropdown:has(#lang-btn)').addEventListener('shown.bs.dropdown', function() {
        langSearch.value = '';
        langSearch.dispatchEvent(new Event('input'));
        langSearch.focus();
    });
</#if>
    // Auto-translate only if user has never made an explicit choice and browser is not the default lang
    let userChoice = localStorage.getItem('userLangChoice');
    if (!storedLang && userChoice !== defaultLang) {
        if (userChoice) {
            // User previously chose a non-default language (e.g. on another page)
            translatePage(userChoice);
            syncDropdownToLang(userChoice);
        } else {
            // No explicit choice: fall back to browser language
            let browserLang = (navigator.language || navigator.userLanguage || defaultLang).substring(0, 2);
            if (browserLang !== defaultLang) {
                translatePage(browserLang);
                syncDropdownToLang(browserLang);
            }
        }
    }
});
</script>
<script src="https://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
</#macro>