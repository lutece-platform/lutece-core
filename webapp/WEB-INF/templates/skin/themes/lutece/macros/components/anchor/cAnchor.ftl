<#--
Macro: cAnchor

Description: Generates an anchor navigation list for in-page linking, built from a list of anchor objects.

Parameters:
- anchors (object, required): List of anchor items, each containing 'href' and 'label' attributes.
- id (string, optional): The unique identifier for the anchor container. Default: ''.
- class (string, optional): CSS class(es) for the outer container. Default: ''.
- listClass (string, optional): CSS class(es) for the list element. Default: ''.
- listItemClass (string, optional): CSS class(es) for each list item. Default: ''.
- anchorsClass (string, optional): CSS class(es) for each anchor link. Default: ''.
- params (string, optional): Additional HTML attributes for the anchor container. Default: ''.

Snippet:

    Basic anchor navigation:

    <@cAnchor anchors=[
        {'href': '#section-about', 'label': 'About us'},
        {'href': '#section-services', 'label': 'Our services'},
        {'href': '#section-contact', 'label': 'Contact'}
    ] />

-->
<#macro cAnchor anchors id='' class='' listClass='' listItemClass='' anchorsClass='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@cBlock id=id! class='anchors ${class}'>
	<@chList class=listClass >
		<#list anchors as anchor>
			<@chItem class=listItemClass>
				<@cLink href=anchor.href! label=anchor.label! class=anchorsClass! />
			</@chItem>
		</#list>
	</@chList>
</@cBlock>
</#macro>