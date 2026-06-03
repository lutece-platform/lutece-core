/**
 * A draggable class for HTML elements.
 */
export class LuteceDraggable {
    /**
     * Creates a new instance of LuteceDraggable.
     * @param {NodeListOf<Element>} draggables - The list of draggable elements.
     * @param {NodeListOf<Element>} containers - The list of container elements where the draggable elements can be moved to.
     */
    constructor(draggables, containers) {
        this.draggables = draggables;
        this.containers = containers;
        this.setupDraggable();
        this.setupContainers();
    }

    /**
     * Configures the draggable elements to make them draggable.
     */
    setupDraggable() {
        this.draggables.forEach((draggable) => {
            draggable.classList.add("lutece-draggable");
            draggable.addEventListener("dragstart", function () {
                this.classList.add("lutece-dragging");
            });
            draggable.addEventListener("dragend", function () {
                this.classList.remove("lutece-dragging");
                const container = this.parentNode.closest('.lutece-draggable-container');
                const index = Array.from(container.children).indexOf(this);
                const dragEndEvent = new CustomEvent('dragged', {
                    detail: {
                        draggable: draggable,
                        container: container,
                        index: index
                    }
                });
                draggable.dispatchEvent(dragEndEvent);
            });
        });
    }

    /**
     * Configures the container elements to accept draggable elements and handles the drag and drop events.
     */
    setupContainers() {
        this.containers.forEach((container) => {
            container.classList.add('lutece-draggable-container');
            container.addEventListener("dragover", (e) => {
                e.preventDefault();
                const draggedElement = document.querySelector(".lutece-dragging");
                const afterElement = this.getDragTargetElement(container, e.clientY);
                if (afterElement == undefined) {
                    container.appendChild(draggedElement);
                } else {
                    container.insertBefore(draggedElement, afterElement);
                }
            });
        });
    }

    /**
     * Gets the element after which the dragged element should be inserted in the container.
     * @param {Element} container - The container element.
     * @param {number} y - The y coordinate of the pointer.
     * @returns {Element} The element after which the dragged element should be inserted.
     */
    getDragTargetElement(container, y) {
        const notDraggedCards = [
            ...container.querySelectorAll(".lutece-draggable:not(.lutece-dragging)"),
        ];
        return notDraggedCards.reduce(
            (closest, child) => {
                const box = child.getBoundingClientRect();
                const offset = y - box.top - box.height / 2;
                if (offset < 0 && offset > closest.offset) {
                    return {
                        offset,
                        element: child
                    };
                } else {
                    return closest;
                }
            }, {
                offset: Number.NEGATIVE_INFINITY
            }
        ).element;
    }

    /**
     * Moves a draggable element to a new position in a container.
     * @param {Element} draggable - The draggable element to move.
     * @param {number} position - The position to move the draggable element to in the container.
     * @param {Element} container - The container to move the draggable element to.
     */
    moveElement(draggable, position, container) {
        container.insertBefore(draggable, container.children[position]);
    }

    /**
     * Adds an event listener to the draggable elements.
     * @param {string} eventType - The type of the event to listen for.
     * @param {function} callback - The function to call when the event is triggered.
     */
    on(eventType, callback) {
        this.draggables.forEach((draggable) => {
            draggable.addEventListener(eventType, (event) => {
                callback(event);
            });
        });
    }
}