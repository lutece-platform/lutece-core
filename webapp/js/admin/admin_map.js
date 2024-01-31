/*
 * menuExpandable3.js - implements an expandable menu based on a HTML list
 * Author: Dave Lindquist (http://www.gazingus.org)
 */

if (!document.getElementById)
    document.getElementById = function() { return null; }

function initializeMenu(menuId, actuatorId) {
    var menu = document.getElementById(menuId);
    var actuator = document.getElementById(actuatorId);

    if (menu == null || actuator == null) return;

    actuator.parentNode.style.backgroundImage = "url(images/admin/skin/plus.gif)";
    actuator.onclick = function() {
        var display = menu.style.display;
        this.parentNode.style.backgroundImage =
            (display == "block") ? "url(images/admin/skin/plus.gif)" : "url(images/admin/skin/minus.gif)";
        menu.style.display = (display == "block") ? "none" : "block";

        return false;
    }
}