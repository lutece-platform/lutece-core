/**
 * Theme Form Observer module
 * Handles removal of invalid state and feedback messages on user interaction
 */
export class themeFormObserver {
    constructor() {
        document.addEventListener('DOMContentLoaded', () => {
            this.init();
        });
    }

    /**
     * Initialize the form Observer handlers
     */
    init() {
        this.bindInvalidInputHandlers();
        this.observeNewElements();
    }

    /**
     * Bind event handlers to all invalid inputs
     */
    bindInvalidInputHandlers() {
        const invalidInputs = document.querySelectorAll('.is-invalid');
        invalidInputs.forEach(input => this.attachClearHandler(input));
    }

    /**
     * Attach clear handler to a single element
     * @param {HTMLElement} element - The element to attach handlers to
     */
    attachClearHandler(element) {
        // Skip if already has handler attached
        if (element.dataset.invalidHandlerAttached) {
            return;
        }

        const clearInvalid = () => this.clearInvalidState(element);

        // Handle different input types
        const tagName = element.tagName.toLowerCase();
        const inputType = element.getAttribute('type');

        if (tagName === 'select') {
            element.addEventListener('change', clearInvalid, { once: true });
        } else if (tagName === 'textarea') {
            element.addEventListener('input', clearInvalid, { once: true });
        } else if (tagName === 'legend') {
            element.parentElement.querySelectorAll('input[type="radio"], input[type="checkbox"]').forEach(input => {
                input.addEventListener('change', clearInvalid, { once: true });
            });
        } else if (tagName === 'input') {
            if (inputType === 'checkbox' || inputType === 'radio') {
                element.addEventListener('change', clearInvalid, { once: true });
            } else if (inputType === 'file') {
                element.addEventListener('input', clearInvalid, { once: true });
            } else {
                // Text, email, number, etc.
                if( element.classList.contains('datepicker-input') ) {
                    // Special handling for datepicker inputs
                    element.addEventListener('click', clearInvalid, { once: true });
                } else {

                    element.addEventListener('input', clearInvalid, { once: true });
                }
            }
        } else {
            // For other elements (divs with is-invalid class, etc.)
            element.addEventListener('click', clearInvalid, { once: true });
            element.addEventListener('input', clearInvalid, { once: true });
        }

        // Mark as having handler attached
        element.dataset.invalidHandlerAttached = 'true';
    }

    /**
     * Clear the invalid state from an element
     * @param {HTMLElement} element - The element to clear invalid state from
     */
    clearInvalidState(element) {
        // Remove is-invalid class
        if( element.tagName.toLowerCase() === 'div' ) {
            // Special handling for divs (e.g. custom file inputs)
            const colGroup = element.closest('.col');
            if (colGroup) {
                const invalidChildren = colGroup.querySelectorAll('.is-invalid');
                invalidChildren.forEach(child => {
                    child.classList.remove('is-invalid');
                    child.removeAttribute('aria-invalid');
                    this.removeInvalidFeedback(child);
                    delete child.dataset.invalidHandlerAttached;
                });
            }
        } else {
            element.classList.remove('is-invalid');
            element.removeAttribute('aria-invalid');
            
            // Remove the closest invalid-feedback element
            this.removeInvalidFeedback(element);
    
            // Clean up the handler marker
            delete element.dataset.invalidHandlerAttached;
        }
    }

    /**
     * Remove the invalid-feedback element associated with an input
     * @param {HTMLElement} element - The input element
     */
    removeInvalidFeedback(element) {
        // Strategy 0: the message the field declares (aria-describedby)
        const describedBy = (element.getAttribute('aria-describedby') || '').split(/\s+/).filter(Boolean);
        for (const id of describedBy) {
            const describedFeedback = document.getElementById(id);
            if (describedFeedback && describedFeedback.classList.contains('invalid-feedback')) {
                describedFeedback.remove();
                return;
            }
        }

        // Strategy 1: Check for sibling invalid-feedback (next sibling)
        let feedback = element.nextElementSibling;
        while (feedback) {
            if (feedback.classList.contains('invalid-feedback')) {
                if (!this.belongsToAnotherField(feedback, element)) {
                    feedback.remove();
                }
                return;
            }
            // Stop if we hit another form control
            if (feedback.classList.contains('form-control') || 
                feedback.tagName.toLowerCase() === 'input' ||
                feedback.tagName.toLowerCase() === 'select' ||
                feedback.tagName.toLowerCase() === 'textarea') {
                break;
            }
            feedback = feedback.nextElementSibling;
        }

        // Strategy 2: Check parent for invalid-feedback child
        const parent = element.parentElement;
        if (parent && this.removeOwnFeedback(parent, element)) {
            return;
        }

        // Strategy 3: Use closest to find containing form-group and look for feedback there
        const colGroup = element.closest('.col');
        if (colGroup) {
            this.removeOwnFeedback(colGroup, element);
        }
    }

    /**
     * Remove the first invalid-feedback of a container that is not the message of another field
     * @param {HTMLElement} container - The container to search
     * @param {HTMLElement} element - The input element
     * @returns {boolean} true if a message has been removed
     */
    removeOwnFeedback(container, element) {
        const feedback = Array.from(container.querySelectorAll('.invalid-feedback'))
            .find(candidate => !this.belongsToAnotherField(candidate, element));
        if (feedback) {
            feedback.remove();
            return true;
        }
        return false;
    }

    /**
     * Tell if an invalid-feedback is the message of another field (referenced by its aria-describedby):
     * fields without wrapper share their parent, the message of a field must not be removed by another one
     * @param {HTMLElement} feedback - The invalid-feedback element
     * @param {HTMLElement} element - The input element
     * @returns {boolean} true if another field declares this message
     */
    belongsToAnotherField(feedback, element) {
        if (!feedback.id) {
            return false;
        }
        return Array.from(document.querySelectorAll('[aria-describedby]'))
            .some(other => other !== element && other.getAttribute('aria-describedby').split(/\s+/).includes(feedback.id));
    }

    /**
     * Observe DOM for new elements with is-invalid class
     * Useful when Observer errors are added dynamically
     */
    observeNewElements() {
        const observer = new MutationObserver((mutations) => {
            mutations.forEach((mutation) => {
                // Check added nodes
                mutation.addedNodes.forEach((node) => {
                    if (node.nodeType === Node.ELEMENT_NODE) {
                        // Check if the added node itself has is-invalid
                        if (node.classList && node.classList.contains('is-invalid')) {
                            this.attachClearHandler(node);
                        }
                        // Check children of added node
                        const invalidChildren = node.querySelectorAll && node.querySelectorAll('.is-invalid');
                        if (invalidChildren) {
                            invalidChildren.forEach(child => this.attachClearHandler(child));
                        }
                    }
                });

                // Check for class changes (when is-invalid is added to existing elements)
                if (mutation.type === 'attributes' && 
                    mutation.attributeName === 'class' && 
                    mutation.target.classList.contains('is-invalid')) {
                    this.attachClearHandler(mutation.target);
                }
            });
        });

        observer.observe(document.body, {
            childList: true,
            subtree: true,
            attributes: true,
            attributeFilter: ['class']
        });
    }
}

// Auto-initialize if loaded as module
const formObserver = new themeFormObserver();

export default themeFormObserver;
