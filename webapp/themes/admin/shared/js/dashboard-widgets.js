/*
 * Lutece admin dashboard widgets - per-browser state persistence.
 *
 * Loaded by the admin home page (adminHome.ftl). It is a no-op unless the dashboard
 * container (#dashboard-widgets) is present.
 *
 * For each box-widget (`.box-widget[data-id]`) it persists, in the browser localStorage:
 *   - visibility state : "shown" | "collapsed" | "hidden"
 *   - the column it lives in (zone-1 / zone-2 / zone-3)
 *   - its order within that column
 *
 * Menu labels are read from the global window.LuteceDashboardWidgetsLabels (set inline
 * by adminHome.ftl from the admin i18n bundle), with built-in English fallbacks.
 */
(function () {
	'use strict';

	// Guard against a double initialization (e.g. script included more than once).
	if (window.__luteceDashboardWidgetsLoaded) {
		return;
	}
	window.__luteceDashboardWidgetsLoaded = true;

	var CONTAINER_ID = 'dashboard-widgets';
	// The layout is stored per admin user (key suffixed with the access code, see storageKey())
	// so that on a shared computer one user's layout never overwrites another's.
	var STORAGE_KEY_BASE = 'lutece.admin.dashboard.widgets.v1';
	var TOOLS_MOUNT_ID = 'dashboard-widgets-tools';
	// Server-side (cross-environment) fallback store, persisted in core_admin_user_preferences.
	// The layout is POSTed here on logout (admin.js) and GET here on login when localStorage is empty.
	var PREFERENCES_URL = 'servlet/plugins/core/dashboard/widgetsPreferences';

	var STATE_SHOWN = 'shown';
	var STATE_COLLAPSED = 'collapsed';
	var STATE_HIDDEN = 'hidden';

	// Dragging is disabled below this breakpoint (Bootstrap md): on touch devices it
	// captures touch events and prevents the page from being scrolled.
	var DRAG_BREAKPOINT = 768;

	document.addEventListener('DOMContentLoaded', function () {
		var container = document.getElementById(CONTAINER_ID);
		if (!container) {
			return;
		}
		new DashboardWidgets(container).init();
	});

	/**
	 * @param {HTMLElement} container the #dashboard-widgets element
	 */
	function DashboardWidgets(container) {
		this.container = container;
		this.columns = toArray(container.querySelectorAll('.widget-col'));
		this.sortables = [];
		this.state = { version: 1, widgets: {} };
		this.storageKey = storageKey();
	}

	// Per-user localStorage key : base + '.' + access code (falls back to the base key when the
	// access code global is unavailable). The access code is exposed by adminHeader.ftl.
	function storageKey() {
		var code = (window.LuteceAdminUser && window.LuteceAdminUser.accessCode)
			? String(window.LuteceAdminUser.accessCode).trim() : '';
		return code ? (STORAGE_KEY_BASE + '.' + code) : STORAGE_KEY_BASE;
	}

	DashboardWidgets.prototype.init = function () {
		var self = this;
		// Everything after the state is known : apply it to the DOM and wire the UI.
		var build = function () {
			self.applyState();
			self.enhanceWidgets();
			self.interceptHide();
			self.setupSortable();
			self.buildToolsMenu();
			// Snapshot the resulting layout (captures defaults for brand-new widgets).
			self.captureLayout();
			// Persist locally : also writes back a layout restored from the server so the
			// next load is instant and offline.
			self.save();
		};
		// localStorage is the per-browser source of truth. Only when it holds no copy
		// (e.g. first login on this browser, or after the logout Clear-Site-Data wipe) do
		// we restore the cross-environment layout from the server.
		if (this.loadState()) {
			build();
		} else {
			this.loadServerState(build);
		}
	};

	/* ------------------------------------------------------------------ *
	 *  Widget helpers
	 * ------------------------------------------------------------------ */

	DashboardWidgets.prototype.widgets = function () {
		return toArray(this.container.querySelectorAll('.box-widget'));
	};

	function widgetId(el) {
		return el.getAttribute('data-id') || el.id || null;
	}

	function widgetBody(el) {
		var id = widgetId(el);
		return (id && document.getElementById(id + '_dashboard_card_body')) || el.querySelector('.card-body');
	}

	function widgetHeader(el) {
		return el.querySelector('.card-header');
	}

	function widgetTitle(el) {
		var titleEl = el.querySelector('.card-title, .card-header .card-title, h3');
		var text = titleEl ? titleEl.textContent : '';
		text = (text || '').replace(/\s+/g, ' ').trim();
		return text || widgetId(el) || '';
	}

	DashboardWidgets.prototype.stateOf = function (id) {
		if (!this.state.widgets[id]) {
			this.state.widgets[id] = { state: STATE_SHOWN, col: null, order: 0 };
		}
		return this.state.widgets[id];
	};

	/* ------------------------------------------------------------------ *
	 *  Load / save (browser localStorage)
	 * ------------------------------------------------------------------ */

	// Load the layout from localStorage. Returns true when a local copy exists (even if
	// corrupted : the key is present, so the server should not be queried), false when
	// there is none and the caller should fall back to the server.
	DashboardWidgets.prototype.loadState = function () {
		var raw;
		try {
			raw = window.localStorage.getItem(this.storageKey);
		} catch (e) {
			return false; // storage unavailable : behave as if nothing is stored
		}
		if (!raw) {
			return false;
		}
		try {
			var data = JSON.parse(raw);
			if (data && data.widgets && typeof data.widgets === 'object') {
				this.state.widgets = data.widgets;
			}
		} catch (e) {
			/* corrupted local value : keep defaults */
		}
		return true;
	};

	// Restore the layout from the server (core_admin_user_preferences) when localStorage
	// has none, then invoke done(). Any failure degrades silently to the default layout.
	DashboardWidgets.prototype.loadServerState = function (done) {
		var self = this;
		try {
			window.fetch(PREFERENCES_URL, {
				method: 'GET',
				credentials: 'same-origin',
				headers: { 'Accept': 'application/json' }
			})
				.then(function (resp) { return resp.ok ? resp.json() : null; })
				.then(function (data) {
					if (data && data.widgets && typeof data.widgets === 'object') {
						self.state.widgets = data.widgets;
					}
				})
				.catch(function () { /* offline / error : fall back to defaults */ })
				.then(function () { done(); });
		} catch (e) {
			done(); // fetch unavailable : proceed with defaults
		}
	};

	DashboardWidgets.prototype.save = function () {
		try {
			window.localStorage.setItem(this.storageKey, JSON.stringify(this.state));
		} catch (e) {
			/* storage full or disabled : ignore */
		}
	};

	/* ------------------------------------------------------------------ *
	 *  Apply persisted state to the DOM
	 * ------------------------------------------------------------------ */

	DashboardWidgets.prototype.applyState = function () {
		this.applyOrder();
		this.applyVisibility();
	};

	DashboardWidgets.prototype.applyOrder = function () {
		var byId = {};
		this.widgets().forEach(function (el) {
			var id = widgetId(el);
			if (id) { byId[id] = el; }
		});

		var groups = {};
		var widgets = this.state.widgets;
		Object.keys(widgets).forEach(function (id) {
			var w = widgets[id];
			if (!byId[id] || !w.col) { return; }
			(groups[w.col] = groups[w.col] || []).push({ id: id, order: w.order || 0 });
		});

		Object.keys(groups).forEach(function (colId) {
			var colEl = document.getElementById(colId);
			if (!colEl) { return; }
			groups[colId].sort(function (a, b) { return a.order - b.order; });
			groups[colId].forEach(function (o) {
				colEl.appendChild(byId[o.id]); // moving an existing node re-parents + reorders it
			});
		});
	};

	DashboardWidgets.prototype.applyVisibility = function () {
		var self = this;
		this.widgets().forEach(function (el) {
			var id = widgetId(el);
			var w = id && self.state.widgets[id];
			if (!w) { return; }
			if (w.state === STATE_HIDDEN) {
				self.setHidden(el, true, false);
			} else if (w.state === STATE_COLLAPSED) {
				self.setCollapsed(el, true, false);
			}
		});
	};

	/* ------------------------------------------------------------------ *
	 *  Collapse
	 * ------------------------------------------------------------------ */

	DashboardWidgets.prototype.enhanceWidgets = function () {
		var self = this;
		this.widgets().forEach(function (el) {
			var header = widgetHeader(el);
			if (!header || header.querySelector('.dw-collapse-toggle')) {
				return; // no header (e.g. smallBox) or already enhanced
			}
			self.neutralizeHideButton(el);
			var btn = document.createElement('button');
			btn.type = 'button';
			btn.className = 'btn btn-icon btn-link dw-collapse-toggle';
			// Default state is expanded, so the action offered is "collapse".
			btn.title = label('collapse');
			btn.setAttribute('aria-label', label('collapse'));
			btn.innerHTML = chevronSvg();
			btn.addEventListener('click', function (e) {
				e.preventDefault();
				e.stopPropagation();
				var collapsed = el.classList.contains('dw-collapsed');
				self.setCollapsed(el, !collapsed, true);
			});
			// Insert the toggle at the very start of the header so it sits before the title.
			header.insertBefore(btn, header.firstChild);
		});
	};

	DashboardWidgets.prototype.setCollapsed = function (el, collapsed, persist) {
		el.classList.toggle('dw-collapsed', collapsed);
		var body = widgetBody(el);
		var toggle = el.querySelector('.dw-collapse-toggle');
		if (toggle) {
			toggle.setAttribute('aria-expanded', collapsed ? 'false' : 'true');
			// Offer the opposite action as the accessible name / tooltip.
			var action = collapsed ? label('expand') : label('collapse');
			toggle.title = action;
			toggle.setAttribute('aria-label', action);
		}
		if (body) {
			body.setAttribute('aria-hidden', collapsed ? 'true' : 'false');
		}
		if (persist) {
			var id = widgetId(el);
			if (id) {
				this.stateOf(id).state = collapsed ? STATE_COLLAPSED : STATE_SHOWN;
				this.save();
			}
		}
	};

	/* ------------------------------------------------------------------ *
	 *  Hide / show
	 * ------------------------------------------------------------------ */

	// The dashboard widget header ships a "Hide" action rendered as a Bootstrap alert
	// dismiss ([data-bs-dismiss="alert"]) that REMOVES the card from the DOM. We strip
	// that behaviour so the widget can be hidden-and-restored instead of destroyed:
	// the dismiss attributes are removed and the button is tagged with its widget id.
	DashboardWidgets.prototype.neutralizeHideButton = function (el) {
		var hideBtn = el.querySelector('[data-bs-dismiss="alert"]');
		if (!hideBtn) {
			return;
		}
		var id = widgetId(el);
		hideBtn.removeAttribute('data-bs-dismiss'); // stop Bootstrap from destroying the card
		hideBtn.removeAttribute('data-bs-target');
		hideBtn.classList.add('dw-hide-btn');
		if (id) {
			hideBtn.setAttribute('data-dw-widget', id);
		}
	};

	// Handle clicks on the (neutralized) Hide buttons. Bound on document so it still
	// works when the Bootstrap dropdown menu holding the button is reparented (Popper
	// moves open menus, which can take the button outside #dashboard-widgets).
	DashboardWidgets.prototype.interceptHide = function () {
		var self = this;
		document.addEventListener('click', function (e) {
			var btn = e.target.closest('.dw-hide-btn');
			if (!btn) {
				return;
			}
			var el = self.widgetById(btn.getAttribute('data-dw-widget'));
			if (!el) {
				return;
			}
			e.preventDefault();
			self.setHidden(el, true, true);
		});
	};

	DashboardWidgets.prototype.setHidden = function (el, hidden, persist) {
		el.classList.toggle('d-none', hidden);
		el.classList.toggle('dw-hidden', hidden);
		if (persist) {
			var id = widgetId(el);
			if (id) {
				this.stateOf(id).state = hidden ? STATE_HIDDEN : STATE_SHOWN;
				this.save();
			}
			this.buildToolsMenu();
		}
	};

	/* ------------------------------------------------------------------ *
	 *  Reorder (SortableJS) + column persistence
	 * ------------------------------------------------------------------ */

	DashboardWidgets.prototype.setupSortable = function () {
		if (typeof Sortable === 'undefined') {
			return; // SortableJS not loaded : reorder simply won't persist
		}
		var self = this;
		// The widget markup hard-codes draggable="true" (previously used by the core
		// native-drag helper). SortableJS manages dragging itself, so neutralize the
		// native HTML5 draggable attribute to avoid a conflicting drag ghost.
		this.widgets().forEach(function (el) { el.setAttribute('draggable', 'false'); });
		this.columns.forEach(function (col) {
			var sortable = new Sortable(col, {
				group: 'dashboard-widgets',
				draggable: '.box-widget',
				handle: '.card-header',
				filter: 'a, button, input, select, textarea, .dropdown-menu, .btn-action',
				preventOnFilter: false,
				swapThreshold: 0.65,
				onEnd: function () {
					self.captureLayout();
					self.save();
				}
			});
			self.sortables.push(sortable);
		});
		this.updateDragState();
		window.addEventListener('resize', function () { self.updateDragState(); });
	};

	DashboardWidgets.prototype.updateDragState = function () {
		var enabled = window.innerWidth >= DRAG_BREAKPOINT;
		this.sortables.forEach(function (s) { s.option('disabled', !enabled); });
	};

	// Read the current DOM layout (column + order for every widget) into the state,
	// preserving each widget's visibility state.
	DashboardWidgets.prototype.captureLayout = function () {
		var self = this;
		this.columns.forEach(function (col) {
			toArray(col.querySelectorAll('.box-widget')).forEach(function (el, idx) {
				var id = widgetId(el);
				if (!id) { return; }
				var w = self.stateOf(id);
				w.col = col.id;
				w.order = idx;
			});
		});
	};

	/* ------------------------------------------------------------------ *
	 *  Tools menu (restore hidden widgets / reset)
	 * ------------------------------------------------------------------ */

	DashboardWidgets.prototype.buildToolsMenu = function () {
		var mount = document.getElementById(TOOLS_MOUNT_ID);
		if (!mount) {
			return; // adminHome.ftl did not provide a mount point
		}

		var hidden = this.widgets().filter(function (el) {
			var id = widgetId(el);
			return id && this.state.widgets[id] && this.state.widgets[id].state === STATE_HIDDEN;
		}, this);

		var items = '';
		if (hidden.length === 0) {
			items += '<span class="dropdown-header">' + i18n('noHidden') + '</span>';
		} else {
			items += '<span class="dropdown-header">' + i18n('hidden') + '</span>';
			hidden.forEach(function (el) {
				items += '<button type="button" class="dropdown-item dw-restore" data-widget="' +
					escapeAttr(widgetId(el)) + '">' + escapeHtml(widgetTitle(el)) + '</button>';
			});
			items += '<div class="dropdown-divider"></div>' +
				'<button type="button" class="dropdown-item dw-restore-all">' + i18n('showAll') + '</button>';
		}
		items += '<div class="dropdown-divider"></div>' +
			'<button type="button" class="dropdown-item text-danger dw-reset">' + i18n('reset') + '</button>';

		mount.innerHTML =
			'<div class="dropdown">' +
			'  <button class="btn btn-icon position-relative" type="button" data-bs-toggle="dropdown" aria-expanded="false" title="' + i18n('title') + '">' +
			gearSvg() +
			(hidden.length ? '<span class="badge bg-primary text-white rounded-pill position-absolute top-0 start-100 translate-middle">' + hidden.length + '</span>' : '') +
			'  </button>' +
			'  <div class="dropdown-menu dropdown-menu-end">' + items + '</div>' +
			'</div>';

		this.bindToolsMenu(mount);
	};

	DashboardWidgets.prototype.bindToolsMenu = function (mount) {
		var self = this;

		toArray(mount.querySelectorAll('.dw-restore')).forEach(function (btn) {
			btn.addEventListener('click', function () {
				var el = self.widgetById(btn.getAttribute('data-widget'));
				if (el) { self.setHidden(el, false, true); }
			});
		});

		var showAll = mount.querySelector('.dw-restore-all');
		if (showAll) {
			showAll.addEventListener('click', function () {
				self.widgets().forEach(function (el) {
					if (el.classList.contains('dw-hidden')) { self.setHidden(el, false, false); }
				});
				self.captureLayout();
				self.save();
				self.buildToolsMenu();
			});
		}

		var reset = mount.querySelector('.dw-reset');
		if (reset) {
			reset.addEventListener('click', function () { self.reset(); });
		}
	};

	DashboardWidgets.prototype.widgetById = function (id) {
		var match = null;
		this.widgets().forEach(function (el) {
			if (widgetId(el) === id) { match = el; }
		});
		return match;
	};

	DashboardWidgets.prototype.reset = function () {
		this.state = { version: 1, widgets: {} };
		try {
			window.localStorage.removeItem(this.storageKey);
		} catch (e) { /* ignore */ }
		window.location.reload();
	};

	/* ------------------------------------------------------------------ *
	 *  Small utilities
	 * ------------------------------------------------------------------ */

	function toArray(nodeList) {
		return Array.prototype.slice.call(nodeList);
	}

	// Built-in English fallbacks; overridden by window.LuteceDashboardWidgetsLabels
	// (set inline by adminHome.ftl from the admin i18n bundle).
	var LABELS = {
		title: 'Widgets',
		hidden: 'Hidden widgets',
		noHidden: 'No hidden widget',
		showAll: 'Show all widgets',
		reset: 'Reset layout',
		collapse: 'Collapse',
		expand: 'Expand'
	};

	// Raw localized label. Use label() for DOM properties/attributes (.title,
	// .setAttribute, .textContent), which handle their own escaping.
	function label(key) {
		var over = window.LuteceDashboardWidgetsLabels;
		if (over && over[key]) { return over[key]; }
		return LABELS[key] || key;
	}

	// HTML-escaped label, for building markup strings (innerHTML).
	function i18n(key) {
		return escapeHtml(label(key));
	}

	function escapeHtml(str) {
		return String(str).replace(/[&<>"']/g, function (c) {
			return { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c];
		});
	}

	function escapeAttr(str) {
		return escapeHtml(str);
	}

	function chevronSvg() {
		return '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" ' +
			'stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon dw-chevron">' +
			'<path d="M6 9l6 6l6 -6"></path></svg>';
	}

	function gearSvg() {
		return '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" ' +
			'stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon">' +
			'<path d="M10.325 4.317c.426 -1.756 2.924 -1.756 3.35 0a1.724 1.724 0 0 0 2.573 1.066c1.543 -.94 3.31 .826 2.37 2.37a1.724 1.724 0 0 0 1.065 2.572c1.756 .426 1.756 2.924 0 3.35a1.724 1.724 0 0 0 -1.066 2.573c.94 1.543 -.826 3.31 -2.37 2.37a1.724 1.724 0 0 0 -2.572 1.065c-.426 1.756 -2.924 1.756 -3.35 0a1.724 1.724 0 0 0 -2.573 -1.066c-1.543 .94 -3.31 -.826 -2.37 -2.37a1.724 1.724 0 0 0 -1.065 -2.572c-1.756 -.426 -1.756 -2.924 0 -3.35a1.724 1.724 0 0 0 1.066 -2.573c-.94 -1.543 .826 -3.31 2.37 -2.37c1 .608 2.296 .07 2.572 -1.065z"></path>' +
			'<path d="M9 12a3 3 0 1 0 6 0a3 3 0 0 0 -6 0"></path></svg>';
	}
})();
