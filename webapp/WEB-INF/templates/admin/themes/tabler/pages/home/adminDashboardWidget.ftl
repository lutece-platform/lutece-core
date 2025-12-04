<#-- Macro: adminDashboardWidget
Description: Generates a widget for use in the admin dashboard. It generates a Bootstrap card that includes a header with a title and control buttons, and a body that can display any content.
Parameters:
- id (string, required): the ID of the widget.
- title (string, required): the title to display in the widget header.
- color (string, optional): the color of the widget header, using a Bootstrap color class (e.g. "primary", "success", "danger").
- url (string, optional): the URL of a settings page for the widget.
- class (string, optional): the class to apply to the widget element.
- bodyClass (string, optional): the class to apply to the widget body element.
- params (string, optional): additional parameters to add to the widget element.
-->
<#macro adminDashboardWidget id title color='primary' url='' class='' bodyClass='table-responsive no-padding' params=''>
	<@box style='solid' color='${color}' id='${id}_dashboard_card' class='box-widget' params=' data-id="${id}" draggable="true"'>
		<@boxHeader titleLevel='h2' title='${title}'>
			<#if url!=''>
				<@aButton color='link' class='card-control' href='${url!}' title='${title!}' size='sm' buttonIcon='cog' hideTitle=['all'] params='aria-label="${title!}"' />
			</#if>
			<@button style='card-control collapse' buttonTargetId='#${id}_dashboard_card_body' buttonIcon='minus' size='sm' params='aria-label="#i18n{portal.util.labelShow}"'/>
			<@button style='card-control remove' buttonTargetId='#${id}_dashboard_card' buttonIcon='times' size='sm' params='aria-label="#i18n{portal.util.labelHide}"' />
		</@boxHeader>
		<@boxBody class=bodyClass id='${id}_dashboard_card_body'>
			<#nested>
		</@boxBody>
	</@box>
</#macro>