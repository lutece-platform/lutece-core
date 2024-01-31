<#-- Macro: adminHome
Description: Generates the home page for the admin dashboard. It generates a container for three columns of widgets, which are defined in separate macro calls. The macro also includes a script that enables dragging and dropping of the widgets to rearrange their positions.
-->
<#macro adminHome>
<#assign head = .get_optional_template('../../../../../admin/user/adminHeader.html')>
<#if head.exists><@head.include /></#if>
<main id="lutece-main" class="lutece-main-content">
<h1 class="visually-hidden">#i18n{portal.admin.admin_login.welcome} ${site_name!}</h1>
<@pageWrapper>
<@div id="dashboard-widgets" class="dashboard-widgets pt-3">
	<@row>
		<@columns sm=12 md=4 class='widget-col' id='zone-1'>
			${dashboard_zone_1!}
		</@columns>
		<@columns sm=12 md=4 class='widget-col' id='zone-2'>
			${dashboard_zone_2!}
		</@columns>
		<@columns sm=12 md=4 class='widget-col' id='zone-3'>
			${dashboard_zone_3!}
		</@columns>
	</@row>
</@div>
</@pageWrapper>
</main>
<#assign foot = .get_optional_template('../../../../../admin/user/adminFooter.html')>
<#if foot.exists><@foot.include /></#if>
<script type="module">
import {
	LuteceDraggable
} from './themes/shared/modules/luteceDraggable.js';

const containers = document.querySelectorAll('#dashboard-widgets .widget-col');
const draggables = Array.from(containers).flatMap(container => [...container.children]);

const AdminHomeDraggable = new LuteceDraggable(draggables, containers);

AdminHomeDraggable.on('dragged', (event) => {
	// should be make a call to user preference to save the position of the widget
});

// Get all the elements that need to have a "move" cursor
const elements = document.querySelectorAll('#dashboard-widgets .widget-col > .card > .card-header, #dashboard-widgets .widget-col > .card .avatar, #dashboard-widgets .widget-col > .card .info-box-icon');
// Loop through each element and set the cursor to "move"
elements.forEach((element) => {
	element.style.cursor = 'move';
});


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

const dashSortables = [].slice.call(document.querySelectorAll('.dashboard-widgets .widget-col'));
// Loop through each nested sortable element
for ( var i = 0; i < dashSortables.length; i++) {
	var sortableDash = new Sortable( dashSortables[i], {
		group: 'widget-dashboard',
		swapThreshold: 0.65,
		draggable: '.box-widget',
		store: {
			get: function (sortable) {
				var order = localStorage.getItem(sortable.options.group.name);
				return order ? order.split('|') : [];
			},
			set: function (sortable) {
				var order = sortable.toArray();
				localStorage.setItem(sortable.options.group.name, order.join('|'));
			}
		}
	});
}

const boxCount = document.querySelectorAll('.box-widget .counter')
setCounters( 200, boxCount );

</script>
</#macro>