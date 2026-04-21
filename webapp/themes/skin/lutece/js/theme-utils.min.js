function setCookie(cname, cvalue, exdays) {
  var d = new Date();
  d.setTime(d.getTime() + exdays * 24 * 60 * 60 * 1000);
  var expires = "expires=" + d.toGMTString();
  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(";");
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == " ") {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

function checkCookie(cookieName) {
  var c = getCookie(cookieName);
  if (c != "") {
    return true;
  } else {
    return false;
  }
}

function tableXsCollapse() {
  if (window.innerWidth < 580) {
    document.querySelectorAll(".xs-collapsed td:first-child").forEach(function (cell) {
      cell.addEventListener("click", function () {
        let siblings = Array.from(cell.parentNode.children).slice(1);
        siblings.forEach(function (sibling) {
          sibling.classList.toggle("d-block");
        });
        cell.classList.toggle("open");
      });
    });
  }
}
 
function addNoOpener(link) {
  let linkTypes = (link.getAttribute("rel") || "").split(" ");
  if (!linkTypes.includes("noopener")) {
    linkTypes.push("noopener");
  }
  link.setAttribute("rel", linkTypes.join(" ").trim());
}

function addNewTabMessage(link) {
  if (!link.querySelector(".screen-reader-only")) {
    link.insertAdjacentHTML( 'beforeend', '<span class="screen-reader-only">(opens in a new tab)</span>' );
  }
}

function getBrowserInfo(){
	const ua = navigator.userAgent;
	let browserInfo = {};
	if (ua.indexOf("Firefox") > -1) {
		browserInfo.name = "Mozilla Firefox";
		browserInfo.version = ua.split("Firefox/")[1];
	} else if (ua.indexOf("Chrome") > -1) {
		browserInfo.name = "Google Chrome";
		browserInfo.version = ua.split("Chrome/")[1].split(" ")[0];
	} else if (ua.indexOf("Safari") > -1 && ua.indexOf("Version") > -1) {
		browserInfo.name = "Apple Safari";
		browserInfo.version = ua.split("Version/")[1].split(" ")[0];
	} else if (ua.indexOf("MSIE") > -1 || ua.indexOf("Trident/") > -1) {
		browserInfo.name = "Internet Explorer";
		browserInfo.version = ua.indexOf("MSIE") > -1 ? ua.split("MSIE ")[1].split(";")[0] : ua.split("rv:")[1].split(")")[0];
	} else if (ua.indexOf("Opera") > -1 || ua.indexOf("OPR") > -1) {
		browserInfo.name = "Opera";
		browserInfo.version = ua.indexOf("OPR") > -1 ? ua.split("OPR/")[1].split(" ")[0] : ua.split("Version/")[1].split(" ")[0];
	} else {
		browserInfo.name = "Unknown";
		browserInfo.version = "Unknown";
	}
	return browserInfo;
}

function setCharCounter(selCounters) {
  let counters = selCounters === null || selCounters === undefined ? ".counter" : selCounters;
  document.querySelectorAll(counters).forEach(function (counterElement) {
    let nCounterMax = counterElement.getAttribute("maxlength");
    if (!isNaN(nCounterMax) && nCounterMax > 0) {
      let tag = `<span class="ms-2 counter-number text-dark fw-bold">0 /</span><small class="text-dark"> ${nCounterMax} caractères maximum</small>`;
      counterElement.parentElement.querySelector("label").insertAdjacentHTML('beforeend', tag);
      let counter = counterElement.parentElement.querySelector(".counter-number"),
        nChars = 0;

      counterElement.addEventListener("keyup", function () {
        nChars = counterElement.value.length;
        counter.innerHTML = nChars + " /";
        if (nChars >= nCounterMax) {
          counterElement.insertAdjacentHTML('afterend', '<p role="alert" class="visually-hidden">Vous avez atteint le nombre de caractères maximum</p>');
          return false;
        }
      });

      counterElement.addEventListener("paste", function (event) {
        event.preventDefault();
        const content = counterElement.value;
        counterElement.value = "";
        let clip = event.clipboardData.getData("Text");
        let final_clip = content + clip.replace(/\s+/g, " ");
        if (final_clip.length > nCounterMax) {
          final_clip = final_clip.substring(0, nCounterMax);
        }
        counterElement.value = final_clip;
        nChars = counterElement.value.length;
        counter.innerHTML = nChars + " /";
        if (nChars >= nCounterMax) {
          counterElement.insertAdjacentHTML('afterend', '<p role="alert" class="visually-hidden">Vous avez atteint le nombre de caractères maximum</p>');
          return false;
        }
      });
    } else {
      counterElement.parentElement.querySelector("label").insertAdjacentHTML('afterend', "<!-- WARNING no max-length attribute on the input below = no counter shown -->");
    }
  });
}

function initAccessibleTooltips() {
  document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(function(trigger) {
    var hideTimeout = null;
    var tooltip = bootstrap.Tooltip.getOrCreateInstance(trigger, {
      trigger: 'manual',
      html: trigger.getAttribute('data-bs-html') === 'true',
      container: 'body'
    });

    function showTooltip() {
      clearTimeout(hideTimeout);
      tooltip.show();
      var tip = tooltip.tip;
      if (tip) {
        tip.style.pointerEvents = 'auto';
        tip.setAttribute('tabindex', '-1');
        tip.addEventListener('mouseenter', function() { clearTimeout(hideTimeout); });
        tip.addEventListener('mouseleave', function() { scheduleHide(); });
      }
    }

    function scheduleHide() {
      clearTimeout(hideTimeout);
      hideTimeout = setTimeout(function() { tooltip.hide(); }, 200);
    }

    trigger.addEventListener('mouseenter', showTooltip);
    trigger.addEventListener('mouseleave', scheduleHide);
    trigger.addEventListener('focus', showTooltip);
    trigger.addEventListener('blur', scheduleHide);
  });

  document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
      document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(function(el) {
        var tooltip = bootstrap.Tooltip.getInstance(el);
        if (tooltip) { tooltip.hide(); }
      });
      document.querySelectorAll('[data-bs-toggle="popover"]').forEach(function(el) {
        var popover = bootstrap.Popover.getInstance(el);
        if (popover) { popover.hide(); }
      });
    }
  });
}

// Tooltip init
initAccessibleTooltips();
 
function initAccessiblePopovers() {
  document.querySelectorAll('[data-bs-toggle="popover"]').forEach(function(trigger) {
    var hideTimeout = null;
    var popover = bootstrap.Popover.getOrCreateInstance(trigger, {
      trigger: 'manual',
      html: trigger.getAttribute('data-bs-html') === 'true' || true,
      container: 'body'
    });

    function showPopover() {
      clearTimeout(hideTimeout);
      popover.show();
      var tip = popover.tip;
      if (tip) {
        tip.style.pointerEvents = 'auto';
        tip.setAttribute('tabindex', '-1');
        tip.addEventListener('mouseenter', function() { clearTimeout(hideTimeout); });
        tip.addEventListener('mouseleave', function() { scheduleHide(); });
      }
    }

    function scheduleHide() {
      clearTimeout(hideTimeout);
      hideTimeout = setTimeout(function() { popover.hide(); }, 300);
    }

    trigger.addEventListener('mouseenter', showPopover);
    trigger.addEventListener('mouseleave', scheduleHide);
    trigger.addEventListener('focus', showPopover);
    trigger.addEventListener('blur', scheduleHide);
  });
}

// Popover init
initAccessiblePopovers();