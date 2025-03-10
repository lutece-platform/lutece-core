var items = document.querySelectorAll('.show-neighbors .carousel-item');

items.forEach(function (item) {
    var next = item.nextElementSibling;
    if (!next) {
        next = item.parentElement.firstElementChild;
    }
    var cloneChild = next.querySelector(':first-child').cloneNode(true);
    item.appendChild(cloneChild);
});

items.forEach(function (item) {
    var prev = item.previousElementSibling;
    if (!prev) {
        prev = item.parentElement.lastElementChild;
    }
    var children = prev.children;
    if (children.length > 1) {
        var cloneChild = children[children.length - 2].cloneNode(true);
        item.insertBefore(cloneChild, item.firstChild);
    }
});