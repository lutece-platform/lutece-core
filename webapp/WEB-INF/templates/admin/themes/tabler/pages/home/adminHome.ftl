<#-- Macro: adminHome
Description: Generates the home page for the admin dashboard. It generates a container for three columns of widgets, which are defined in separate macro calls. The macro also includes a script that enables dragging and dropping of the widgets to rearrange their positions.

Snippet:

    Render the admin home page:

    <@adminHome />

-->
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
				<!-- Page pre-title -->
				<div class="page-pretitle" id="feature-title">${favourite!}</div>
				<h2 class="page-title">#i18n{portal.admin.admin_home.welcome}</h2>
			</div>
			<div class="page-header-buttons col-auto ms-auto d-print-none">
				<!-- Mount point for the admin-user-preferences plugin dashboard tools (restore hidden widgets / reset layout) -->
				<#if manageDashboardWidgets>
				<div id="dashboard-widgets-tools" class="d-inline-flex align-items-center"></div>
				</#if>
				<@adminHeaderDocumentationLink />
			</div>
		</div>
	</div>
</div>
<!-- END PAGE HEADER -->
<@pageWrapper>
<@div id="dashboard-widgets" class="dashboard-widgets">
	<@div class="row row-cols-1 row-cols-sm-1 row-cols-md-2 row-cols-xl-3">
		<@columns sm=4 class='widget-col' id='zone-1'>
		${dashboard_zone_1!}
		</@columns>
		<@columns sm=4	class='widget-col' id='zone-2'>
		${dashboard_zone_2!}
		</@columns>
		<@columns sm=4 class='widget-col' id='zone-3'>
		${dashboard_zone_3!}
		</@columns>
	</@div>
	<@div class="row">
		<@columns class='widget-col' id='zone-4'></@columns>
		<@columns class='widget-col' id='zone-5'></@columns>
	</@div>
	<@div class="row">
		<@columns class='widget-col' id='zone-6'></@columns>
	</@div>
</@div>
</@pageWrapper>
<#assign foot = .get_optional_template('../../../../../admin/user/adminFooter.html')>
<#if foot.exists><@foot.include /></#if>
<#--
	Widget drag & drop reordering, collapse, hide and per-browser persistence of the
	dashboard layout (mount point: #dashboard-widgets-tools). Menu labels are passed to
	the script through a global, localized from the admin i18n bundle.
-->
<#if manageDashboardWidgets>
<link rel="stylesheet" href="themes/admin/shared/css/dashboard-widgets.css">
<script>
window.LuteceDashboardWidgetsLabels = {
	title: "#i18n{portal.admin.admin_home.dashboard.tools.title}",
	hidden: "#i18n{portal.admin.admin_home.dashboard.tools.hidden}",
	noHidden: "#i18n{portal.admin.admin_home.dashboard.tools.noHidden}",
	showAll: "#i18n{portal.admin.admin_home.dashboard.tools.showAll}",
	reset: "#i18n{portal.admin.admin_home.dashboard.tools.reset}",
	collapse: "#i18n{portal.admin.admin_home.dashboard.widget.collapse}",
	expand: "#i18n{portal.admin.admin_home.dashboard.widget.expand}"
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

const boxCount = document.querySelectorAll('.box-widget .counter')
setCounters( 200, boxCount );
</script>
</#macro>