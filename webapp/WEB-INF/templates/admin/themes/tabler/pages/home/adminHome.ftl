<#-- Macro: adminHome
Description: Generates the home page for the admin dashboard. It generates a container for three columns of widgets, which are defined in separate macro calls. The macro also includes a script that enables dragging and dropping of the widgets to rearrange their positions.

Snippet:

    Render the admin home page:

    <@adminHome />

-->
<#-- The link of a shortcut, as the menu entry: the plugin name on an internal feature, the URL as it is on an external one -->
<#macro homeShortcutUrl right><#if right.externalFeature>${right.url}<#else>${right.url}${right.url?contains( '?' )?then( '&amp;', '?' )}plugin_name=${right.pluginName!}</#if></#macro>
<#-- The icon of a menu entry, as in the menu (adminFeatureIcon, adminHeader.ftl): a Tabler class, or an image (the icon of a plugin) -->
<#macro homeShortcutIcon right><@adminFeatureIcon icon=right.iconUrl! /></#macro>
<#-- A chosen shortcut in the dialog; without a right, the empty model the script fills when an entry is ticked -->
<#macro homeShortcutItem right=''>
<li class="list-group-item home-shortcuts-item" data-id="<#if right?has_content>${right.id}</#if>">
	<div class="d-flex align-items-center gap-2">
		<span class="home-shortcuts-handle text-secondary" aria-hidden="true"><i class="ti ti-grip-vertical"></i></span>
		<span class="home-shortcuts-name flex-fill text-truncate"><#if right?has_content><@homeShortcutIcon right />${right.name}</#if></span>
		<span class="btn-actions">
			<button type="button" class="btn-action" data-shortcut-move="up" title="#i18n{portal.admin.admin_home.shortcuts.moveUp}"><i class="ti ti-arrow-up" aria-hidden="true"></i><span class="visually-hidden">#i18n{portal.admin.admin_home.shortcuts.moveUp}</span></button>
			<button type="button" class="btn-action" data-shortcut-move="down" title="#i18n{portal.admin.admin_home.shortcuts.moveDown}"><i class="ti ti-arrow-down" aria-hidden="true"></i><span class="visually-hidden">#i18n{portal.admin.admin_home.shortcuts.moveDown}</span></button>
			<button type="button" class="btn-action text-danger" data-shortcut-move="remove" title="#i18n{portal.admin.admin_home.shortcuts.remove}"><i class="ti ti-x" aria-hidden="true"></i><span class="visually-hidden">#i18n{portal.admin.admin_home.shortcuts.remove}</span></button>
		</span>
	</div>
</li>
</#macro>
<#macro adminHome>
<#-- Dashboard widget management (reorder / collapse / hide) is enabled only when the
     'portal.site.site_property.bo.widget.checkbox' site property is set to '1'. When it is
     off, the management button and scripts are not rendered and any layout persisted in the
     browser localStorage is cleared. -->
<#assign manageDashboardWidgets = dskey('portal.site.site_property.bo.widget.checkbox') == '1'>
<#assign head = .get_optional_template('../../../../../admin/user/adminHeader.html')>
<#if head.exists><@head.include /></#if>
<div class="page-header d-print-none" aria-label="Page header">
	<div class="container-xl">
		<div class="row g-2 align-items-center">
			<div class="col">
				<#-- The day, in the language of the user -->
				<div class="page-pretitle">${.now?string[ 'EEEE d MMMM yyyy' ]?cap_first}</div>
				<h2 class="page-title"><#if user?? && user.firstName?has_content>${i18n( 'portal.admin.admin_home.welcomeUser', user.firstName )?html}<#else>#i18n{portal.admin.admin_home.welcome}</#if></h2>
				<div class="text-secondary" id="feature-title">${favourite!}</div>
			</div>
			<div class="page-header-buttons col-auto ms-auto d-print-none">
				<div class="btn-list">
					<#-- The shortcuts: the menu entries chosen by the user, or the default ones (HomeShortcutService), and the button that changes them,
					     for every user when the site property portal.site.site_property.bo.shortcuts.checkbox is on, else for the administrators of the default -->
					<#list home_shortcuts![] as shortcut>
					<a class="btn" href="<@homeShortcutUrl shortcut />" title="${shortcut.description!shortcut.name}"><@homeShortcutIcon shortcut />${shortcut.name}</a>
					</#list>
					<#if home_shortcut_groups?? && ( home_shortcuts_user_enabled || home_shortcuts_can_set_default )>
					<button type="button" class="btn btn-icon" data-bs-toggle="modal" data-bs-target="#home-shortcuts-dialog" title="#i18n{portal.admin.admin_home.shortcuts.edit}"><i class="ti ti-pencil" aria-hidden="true"></i><span class="visually-hidden">#i18n{portal.admin.admin_home.shortcuts.edit}</span></button>
					</#if>
					<!-- Mount point for the admin-user-preferences plugin dashboard tools (restore hidden widgets / reset layout) -->
					<#if manageDashboardWidgets>
					<div id="dashboard-widgets-tools" class="d-inline-flex align-items-center"></div>
					</#if>
					<@adminHeaderDocumentationLink />
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END PAGE HEADER -->
<#--
	The zones of the dashboards, on the layout of the Tabler CRM dashboard:
	- zone 1, a strip of figures: the small boxes of the users, the plugins... side by side, as many a row as the width allows
	- zones 2 and 3, half the width each, the whole width for one alone
	- zones 4 to 6, a third each, the width shared among those that have widgets
	Each zone keeps its id and its widget-col class: the widgets are moved between them by dashboard-widgets.js. A zone without widget
	takes no room, except while a widget is dragged: it is then a place to drop it.
-->
<@pageWrapper>
<div id="dashboard-widgets" class="dashboard-widgets lutece-home<#if manageDashboardWidgets> lutece-home-managed</#if>">
	<div class="widget-col lutece-home-strip" id="zone-1">
	${dashboard_zone_1!}
	</div>
	<div class="row row-cards">
		<div class="col-12 col-lg"><div class="widget-col lutece-home-stack" id="zone-2">${dashboard_zone_2!}</div></div>
		<div class="col-12 col-lg"><div class="widget-col lutece-home-stack" id="zone-3">${dashboard_zone_3!}</div></div>
	</div>
	<div class="row row-cards">
		<div class="col-12 col-lg"><div class="widget-col lutece-home-stack" id="zone-4">${dashboard_zone_4!}</div></div>
		<div class="col-12 col-lg"><div class="widget-col lutece-home-stack" id="zone-5">${dashboard_zone_5!}</div></div>
		<div class="col-12 col-lg"><div class="widget-col lutece-home-stack" id="zone-6">${dashboard_zone_6!}</div></div>
	</div>
</div>
</@pageWrapper>
<#if home_shortcut_groups?? && ( home_shortcuts_user_enabled || home_shortcuts_can_set_default )>
<#--
	The dialog that changes the shortcuts: on top the chosen ones, in their order (dragged, or moved with their buttons), under them the
	menu of the user to pick from. The order goes as hidden fields 'shortcut', written from the list when the form is sent; each button
	of the footer carries its action (save, reset, default).
-->
<@modal id='home-shortcuts-dialog' size='lg' params='tabindex="-1" aria-labelledby="home-shortcuts-title"'>
<form id="home-shortcuts-form" method="post" action="jsp/admin/DoModifyHomeShortcuts.jsp" style="display: contents" data-max="${home_shortcuts_max}">
	<input type="hidden" name="token" value="${token}">
	<div class="modal-header">
		<h2 class="modal-title" id="home-shortcuts-title">#i18n{portal.admin.admin_home.shortcuts.title}</h2>
		<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="#i18n{portal.admin.admin_home.shortcuts.buttonCancel}"></button>
	</div>
	<div class="modal-body">
		<h3 class="h4 mb-2"><#if home_shortcuts_user_enabled>#i18n{portal.admin.admin_home.shortcuts.mine}<#else>#i18n{portal.admin.admin_home.shortcuts.defaults}</#if> <span class="text-secondary fw-normal">(<span class="home-shortcuts-count">${home_shortcuts?size}</span>/${home_shortcuts_max})</span></h3>
		<ul class="list-group mb-3 home-shortcuts-chosen" data-empty="#i18n{portal.admin.admin_home.shortcuts.empty}">
			<#list home_shortcuts as shortcut><@homeShortcutItem shortcut /></#list>
		</ul>
		<h3 class="h4 mb-2">#i18n{portal.admin.admin_home.shortcuts.add}</h3>
		<input type="search" class="form-control mb-2 home-shortcuts-search" placeholder="#i18n{portal.admin.admin_home.shortcuts.search}" aria-label="#i18n{portal.admin.admin_home.shortcuts.search}">
		<p class="text-warning small mb-2 home-shortcuts-limit" hidden>${i18n( 'portal.admin.admin_home.shortcuts.limit', home_shortcuts_max )}</p>
		<div class="home-shortcuts-catalog">
			<#assign chosenIds = home_shortcuts?map( shortcut -> shortcut.id )>
			<#list home_shortcut_groups as group>
			<fieldset class="home-shortcuts-group">
				<legend class="subheader mt-2 mb-1">${group.label}</legend>
				<#list group.features as feature>
				<label class="form-check home-shortcuts-option" data-search="${( feature.name + ' ' + group.label )?lower_case}">
					<input class="form-check-input" type="checkbox" value="${feature.id}"<#if chosenIds?seq_contains( feature.id )> checked</#if>>
					<span class="form-check-label home-shortcuts-label"><@homeShortcutIcon feature />${feature.name}</span>
				</label>
				</#list>
			</fieldset>
			</#list>
		</div>
	</div>
	<div class="modal-footer">
		<#if home_shortcuts_own><button type="submit" class="btn btn-link me-auto px-0" name="shortcut_action" value="reset">#i18n{portal.admin.admin_home.shortcuts.buttonReset}</button></#if>
		<#if home_shortcuts_can_set_default><button type="submit" class="btn btn-outline-primary<#if !home_shortcuts_own> me-auto</#if>" name="shortcut_action" value="default">#i18n{portal.admin.admin_home.shortcuts.buttonDefault}</button></#if>
		<button type="button" class="btn" data-bs-dismiss="modal">#i18n{portal.admin.admin_home.shortcuts.buttonCancel}</button>
		<#if home_shortcuts_user_enabled><button type="submit" class="btn btn-primary" name="shortcut_action" value="save">#i18n{portal.admin.admin_home.shortcuts.buttonSave}</button></#if>
	</div>
</form>
</@modal>
<template id="home-shortcut-item-template"><@homeShortcutItem /></template>
</#if>
<style>
/* The dialog of the shortcuts */
.home-shortcuts-chosen:not( :has( .home-shortcuts-item ) )::before { content: attr( data-empty ); display: block; padding: .75rem 1rem; border: 1px dashed var( --tblr-border-color ); border-radius: var( --tblr-border-radius ); color: var( --tblr-secondary ); font-size: .875rem; text-align: center; }
.home-shortcuts-item { padding: .375rem .75rem; }
.home-shortcuts-item .btn-actions { display: flex; gap: .125rem; }
.home-shortcuts-item .btn-action { width: 1.75rem; height: 1.75rem; display: inline-flex; align-items: center; justify-content: center; border: 0; background: transparent; box-shadow: none; }
.home-shortcuts-item .btn-action:hover { background: var( --tblr-active-bg ); }
.home-shortcuts-handle { cursor: grab; }
.home-shortcuts-catalog { max-height: 40vh; overflow-y: auto; padding: 0 .25rem; }
.home-shortcuts-catalog fieldset { margin: 0; }
.home-shortcuts-option { display: flex; align-items: center; gap: .5rem; margin: 0; padding: .25rem 0 .25rem 1.75rem; }
.home-shortcuts-label img, .home-shortcuts-item img, .page-header-buttons img { width: 1rem; height: 1rem; object-fit: contain; }
/* The icon of an entry at a steady distance from its name, whatever its kind (Tabler class or image) */
.home-shortcuts-label, .home-shortcuts-name { display: inline-flex; align-items: center; gap: .5rem; min-width: 0; }
.page-header-buttons .btn { gap: .375rem; }
.home-shortcuts-chosen .sortable-ghost { opacity: .4; }
.lutece-home { --lutece-home-gap: .75rem; display: grid; gap: var( --lutece-home-gap ); }
.lutece-home > .row { --tblr-gutter-x: var( --lutece-home-gap ); --tblr-gutter-y: var( --lutece-home-gap ); margin-top: 0; }
.lutece-home > .row > * { margin-top: 0; }
.lutece-home > .row { row-gap: var( --lutece-home-gap ); }
/* The strip of figures: one card a cell, all on one row on a desktop */
.lutece-home-strip { display: grid; grid-template-columns: repeat( auto-fit, minmax( 10.5rem, 1fr ) ); gap: var( --lutece-home-gap ); }
.lutece-home .lutece-kpi { margin-bottom: 0 !important; }
.lutece-home .lutece-kpi > .card-body { padding: .625rem .875rem; }
.lutece-kpi .subheader { font-size: .6875rem; }
.lutece-kpi .lutece-kpi-value { font-size: 1.375rem; font-weight: 700; line-height: 1.2; }
.lutece-kpi .avatar { --tblr-avatar-size: 1.75rem; font-size: 1rem; flex-shrink: 0; }
.lutece-kpi .progress { height: .25rem; }
/* The hide button of a figure: over its icon, shown when the card is pointed or focused, above the link that covers the card */
.lutece-kpi > .card-body { position: relative; }
.lutece-kpi .lutece-kpi-hide { position: absolute; top: .5rem; right: .625rem; z-index: 2; width: 1.75rem; height: 1.75rem; display: inline-flex; align-items: center; justify-content: center; background: var( --tblr-bg-surface ); opacity: 0; transition: opacity .15s; }
.lutece-kpi:hover .lutece-kpi-hide, .lutece-kpi:focus-within .lutece-kpi-hide { opacity: 1; }
/* The other zones: their cards one under the other */
.lutece-home-stack { display: grid; gap: var( --lutece-home-gap ); align-content: start; }
.lutece-home-stack > .card { margin-bottom: 0 !important; }
/* A zone without widget takes no room, nor the column that holds it; while a widget is dragged, it shows as a place to drop it */
.lutece-home:not( .lutece-home-dragging ) .widget-col:not( :has( .card ) ) { display: none; }
.lutece-home:not( .lutece-home-dragging ) > .row > [class*="col-"]:not( :has( .card ) ) { display: none; }
.lutece-home-dragging .widget-col:not( :has( .card:not( .sortable-ghost ) ) ) { min-height: 4rem; border: 1px dashed var( --tblr-border-color ); border-radius: var( --tblr-border-radius-lg, .5rem ); }
/* The cards of the widgets, as the Tabler "card actions": a compact header, the icon, the title and its subtitle on the left, the actions on the right */
.lutece-home .box-widget > .card-header { min-height: 0; padding: .5rem .75rem .5rem 1rem; gap: .625rem; background: transparent; flex-wrap: nowrap; }
.lutece-home .box-widget > .card-header .card-title { font-size: .9375rem; font-weight: 600; margin: 0; }
.lutece-home .box-widget > .card-header .card-subtitle { font-size: .75rem; margin: 0; }
.lutece-home .box-widget > .card-header .avatar { --tblr-avatar-size: 1.75rem; font-size: 1rem; flex-shrink: 0; }
.lutece-home .box-widget > .card-header .card-actions { margin-left: auto; flex-shrink: 0; }
.lutece-home .box-widget > .card-header .dw-collapse-toggle { width: auto; min-width: 0; height: auto; margin: 0 -.25rem 0 -.25rem; }
.lutece-home .box-widget .btn-actions { display: flex; align-items: center; gap: .125rem; }
.lutece-home .box-widget .btn-action { width: 1.875rem; height: 1.875rem; display: inline-flex; align-items: center; justify-content: center; }
.lutece-home .box-widget .btn-action .ti { font-size: 1.125rem; }
/* The rows of a flush list: their actions dimmed until the row is pointed or focused */
.lutece-home .box-widget .list-group-item { padding: .5rem 1rem; }
.lutece-home .box-widget .list-group-item .btn-actions { opacity: .6; transition: opacity .15s; }
.lutece-home .box-widget .list-group-item:hover .btn-actions,
.lutece-home .box-widget .list-group-item:focus-within .btn-actions { opacity: 1; }
/* The widgets that still put a card in the card (plugins not yet on the flush list): the inner card loses its frame */
.lutece-home .box-widget > .card-body { padding: .75rem 1rem; }
.lutece-home .box-widget > .card-body .card { border: 0; box-shadow: none; background: transparent; margin: 0 !important; }
.lutece-home .box-widget > .card-body .card > .card-body { padding: 0; }
.lutece-home .box-widget > .card-body .card + .card { border-top: 1px solid var( --tblr-border-color ) !important; padding-top: .75rem; margin-top: .75rem !important; }
.lutece-home .box-widget > .card-body p { margin-bottom: .25rem; }
.lutece-home .box-widget > .card-body p:last-child { margin-bottom: 0; }
/* The empty states: a short block, not half a screen */
.lutece-home .box-widget .empty { padding: 1rem; height: auto; }
.lutece-home .box-widget .empty .empty-img, .lutece-home .box-widget .empty .empty-icon { display: none; }
.lutece-home .box-widget .empty .empty-title { font-size: 1rem; margin-bottom: .25rem; }
.lutece-home .box-widget .empty .empty-action { margin-top: .75rem; }
</style>
<#assign foot = .get_optional_template('../../../../../admin/user/adminFooter.html')>
<#if foot.exists><@foot.include /></#if>
<#--
	Widget drag & drop reordering, collapse, hide and per-browser persistence of the
	dashboard layout (mount point: #dashboard-widgets-tools). Menu labels are passed to
	the script through a global, localized from the admin i18n bundle.
-->
<#if manageDashboardWidgets>
<link rel="stylesheet" href="themes/admin/shared/css/dashboard-widgets.min.css">
<script>
window.LuteceDashboardWidgetsLabels = {
	title: "${i18n('portal.admin.admin_home.dashboard.tools.title')?js_string?no_esc}",
	hidden: "${i18n('portal.admin.admin_home.dashboard.tools.hidden')?js_string?no_esc}",
	noHidden: "${i18n('portal.admin.admin_home.dashboard.tools.noHidden')?js_string?no_esc}",
	showAll: "${i18n('portal.admin.admin_home.dashboard.tools.showAll')?js_string?no_esc}",
	reset: "${i18n('portal.admin.admin_home.dashboard.tools.reset')?js_string?no_esc}",
	collapse: "${i18n('portal.admin.admin_home.dashboard.widget.collapse')?js_string?no_esc}",
	expand: "${i18n('portal.admin.admin_home.dashboard.widget.expand')?js_string?no_esc}"
};
</script>
<script src="themes/admin/shared/js/dashboard-widgets.js"></script>
<#else>
<#-- Widget management is disabled: purge any dashboard layout kept in this browser.
     The key mirrors STORAGE_KEY_BASE in dashboard-widgets.js (base, optionally suffixed
     per user with '.<accessCode>'), so every matching entry is removed. -->
<script>
(function () {
	try {
		var base = 'lutece.admin.dashboard.widgets.v1';
		for (var i = window.localStorage.length - 1; i >= 0; i--) {
			var key = window.localStorage.key(i);
			if (key && key.indexOf(base) === 0) {
				window.localStorage.removeItem(key);
			}
		}
	} catch (e) { /* storage unavailable : nothing to clear */ }
})();
</script>
</#if>
<script>
function setCounters( speed, counters  ){
	counters.forEach( counter => {
		const animate = () => {
			let nCounter = counter.innerText;
			let sVal = '';
			let thisTXT = counter.innerText.split( '/' );
			if ( thisTXT.length > 1 ){
				nCounter = thisTXT[0];
				sVal = ' / ' + thisTXT[1];
			}
			if ( typeof nCounter === 'number' ) {
				const time = nCounter / speed;
				if( data < value) {
					counter.innerText = Math.ceil( data + time );
					setTimeout(animate, 1);
				} else {
					counter.innerText = value;
				}
			}
		}
		animate();
	});
}

/*
	The body of a widget scrolls (overflow-y-auto), which cuts a dropdown opened in it. Placed by Popper with the fixed strategy, the menu
	is positioned on the window instead: no box cuts it any more, and it opens above its button when there is no room below. Set before
	the first click, when Bootstrap reads the data attributes of the toggle.
*/
document.querySelectorAll( '#dashboard-widgets .box-widget [data-bs-toggle="dropdown"]' ).forEach( toggle => {
	if ( !toggle.hasAttribute( 'data-bs-popper-config' ) ) {
		toggle.setAttribute( 'data-bs-popper-config', '{"strategy":"fixed"}' );
	}
});

const boxCount = document.querySelectorAll('.box-widget .counter')
setCounters( 200, boxCount );
</script>
<#if home_shortcut_groups?? && ( home_shortcuts_user_enabled || home_shortcuts_can_set_default )>
<#noparse>
<script>
// The icon image of a menu entry may be gone (a plugin declaring a file that does not exist): a generic icon takes its place
const homeShortcutIconFallback = img => {
	const icon = document.createElement( 'i' );
	icon.className = 'ti ti-apps';
	icon.setAttribute( 'aria-hidden', 'true' );
	img.replaceWith( icon );
};
document.querySelectorAll( '.page-header-buttons .btn img, #home-shortcuts-dialog img' ).forEach( img => {
	if ( img.complete && img.naturalWidth === 0 ) {
		homeShortcutIconFallback( img );
	} else {
		img.addEventListener( 'error', ( ) => homeShortcutIconFallback( img ), { once: true } );
	}
});

// The dialog of the shortcuts. Started once the page is read: SortableJS is loaded at its end
document.addEventListener( 'DOMContentLoaded', ( ) => {
	const form = document.getElementById( 'home-shortcuts-form' );
	if ( !form ) {
		return;
	}
	const max = parseInt( form.dataset.max, 10 );
	const chosen = form.querySelector( '.home-shortcuts-chosen' );
	const options = Array.from( form.querySelectorAll( '.home-shortcuts-option input' ) );
	const count = form.querySelector( '.home-shortcuts-count' );
	const limit = form.querySelector( '.home-shortcuts-limit' );
	const model = document.getElementById( 'home-shortcut-item-template' );
	const items = ( ) => Array.from( chosen.querySelectorAll( '.home-shortcuts-item' ) );

	// The count, the boxes ticked as the list, and the others disabled once the list is full
	const refresh = ( ) => {
		const ids = items( ).map( item => item.dataset.id );
		const full = ids.length >= max;
		count.textContent = ids.length;
		limit.hidden = !full;
		options.forEach( option => {
			option.checked = ids.includes( option.value );
			option.disabled = full && !option.checked;
		});
	};

	const add = option => {
		const item = model.content.firstElementChild.cloneNode( true );
		item.dataset.id = option.value;
		item.querySelector( '.home-shortcuts-name' ).innerHTML = option.closest( 'label' ).querySelector( '.home-shortcuts-label' ).innerHTML;
		chosen.appendChild( item );
	};

	options.forEach( option => option.addEventListener( 'change', ( ) => {
		if ( option.checked ) {
			add( option );
		} else {
			const item = items( ).find( i => i.dataset.id === option.value );
			if ( item ) {
				item.remove( );
			}
		}
		refresh( );
	}));

	// Up, down, remove: the focus stays on the moved shortcut
	chosen.addEventListener( 'click', event => {
		const button = event.target.closest( '[data-shortcut-move]' );
		if ( !button ) {
			return;
		}
		const item = button.closest( '.home-shortcuts-item' );
		const move = button.dataset.shortcutMove;
		if ( move === 'up' && item.previousElementSibling ) {
			chosen.insertBefore( item, item.previousElementSibling );
			button.focus( );
		} else if ( move === 'down' && item.nextElementSibling ) {
			chosen.insertBefore( item.nextElementSibling, item );
			button.focus( );
		} else if ( move === 'remove' ) {
			const next = item.nextElementSibling || item.previousElementSibling;
			item.remove( );
			const focus = next ? next.querySelector( '[data-shortcut-move="remove"]' ) : form.querySelector( '.home-shortcuts-search' );
			focus.focus( );
		}
		refresh( );
	});

	if ( typeof Sortable !== 'undefined' ) {
		new Sortable( chosen, { draggable: '.home-shortcuts-item', handle: '.home-shortcuts-handle', animation: 150 } );
	}

	// The search keeps the entries whose name or group holds the text, and the groups that keep one
	const search = form.querySelector( '.home-shortcuts-search' );
	search.addEventListener( 'input', ( ) => {
		const text = search.value.trim( ).toLowerCase( );
		form.querySelectorAll( '.home-shortcuts-group' ).forEach( group => {
			let visible = 0;
			group.querySelectorAll( '.home-shortcuts-option' ).forEach( option => {
				const match = !text || option.dataset.search.includes( text );
				option.hidden = !match;
				visible += match ? 1 : 0;
			});
			group.hidden = visible === 0;
		});
	});

	// The order goes as hidden fields, written from the list when the form is sent
	form.addEventListener( 'submit', ( ) => {
		form.querySelectorAll( 'input[name="shortcut"]' ).forEach( input => input.remove( ) );
		items( ).forEach( item => {
			const input = document.createElement( 'input' );
			input.type = 'hidden';
			input.name = 'shortcut';
			input.value = item.dataset.id;
			form.appendChild( input );
		});
	});

	refresh( );
});
</script>
</#noparse>
</#if>
</#macro>