/**
 * Lutece XSS Bypass - Base64 encoding for form fields
 *
 * Fields with data-xss-bypass="true" will have their value
 * Base64-encoded before form submission so that the global
 * XSS sanitizer filter does not alter their content.
 *
 * Server-side, use StringUtil.decodeXssBypass() to decode.
 */
(function() {
    document.addEventListener('DOMContentLoaded', function() {
        document.querySelectorAll('form').forEach(function(form) {
            form.addEventListener('submit', function() {
                form.querySelectorAll('[data-xss-bypass="true"]').forEach(function(field) {
                    var value = field.value;
                    if (value) {
                        // Encode to Base64 using UTF-8 safe method
                        field.value = btoa(unescape(encodeURIComponent(value)));
                    }
                });
            });
        });
    });
})();
