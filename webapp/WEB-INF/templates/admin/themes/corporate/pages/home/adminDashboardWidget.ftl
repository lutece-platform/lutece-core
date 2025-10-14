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
<#macro adminDashboardWidget id title hasHeader=false sm=12 md=4 color='primary' url='' class='' bodyClass='' actions=true actionMenu='' params=''>
<@columns sm=sm! md=md! class='widget-col' id='zone-${id!}'>
<@box style='solid' color='${color}' id='${id}_dashboard_card' class='box-widget' params=' data-id="${id}" draggable="true"'>
<@boxHeader titleLevel='h2' title='${title}' class='h5 align-items-center pt-0 pb-3'>
<#if url!=''><@aButton color="link" class='card-control' href='${url!}' title='${title!}' size='sm' buttonIcon='cog' hideTitle=['all'] params='aria-label="${title!}"' /></#if>
</@boxHeader>
<@boxBody class=bodyClass id='${id}_dashboard_card_body'>
<#nested>
</@boxBody>
</@box>
</@columns>
</#macro>