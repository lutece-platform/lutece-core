<#--
Macro: cTile

Description: Generates an interactive tile component (linked card with optional icon, badge and detail text) that directs the user to a content page or downloadable resource. Both `title` and `url` are required — the macro renders nothing if either is missing.

Parameters:
- title (string, required): tile title displayed inside the link.
- url (string, required): destination URL of the tile link.
- level (number, optional): heading level for the title (2 to 6). Default: 3.
- target (string, optional): link target attribute (e.g. '_blank'). Ignored when `download` is true. Default: ''.
- detail (string, optional): supplementary text displayed below the title. Default: ''.
- imgName (string, optional): name of the SVG icon rendered in the tile header (resolved by `parisIcon`). Default: ''.
- badge (string, optional): label of an optional badge displayed in the tile body. Default: ''.
- badgeClass (string, optional): CSS classes applied to the badge (e.g. 'bg-primary', 'bg-secondary'). Default: ''.
- horizontal (boolean, optional): renders the tile in horizontal layout when true. Default: false.
- download (boolean, optional): adds the `download` attribute on the link to force file download. Default: false.
- disabled (boolean, optional): renders the tile and its link as disabled when true. Default: false.
- tooltip (boolean, optional): displays a Bootstrap tooltip combining title and detail when true. Default: false.
- tooltipPos (string, optional): tooltip placement when `tooltip` is true. Accepted values: 'top', 'bottom', 'left', 'right'. Default: 'top'.
- class (string, optional): additional CSS classes applied to the tile container. Default: ''.
- id (string, optional): unique identifier for the tile container. Default: ''.
- params (string, optional): additional HTML attributes for the tile container. Default: ''.

Showcase:
- desc: Tuile - @cTile
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Basic tile with icon, badge and detail:

    <@cTile title='Paris.fr' url='https://paris.fr' detail='Accéder au site Paris.fr' badge='Tag' imgName='louvre' />

    External link tile (opens in new tab):

    <@cTile title='Voir la page [lien externe]' url='https://paris.fr' detail='Accéder au site Paris.fr' target='_blank' />

    Download tile (forces file download):

    <@cTile title='Logo Paris' url='themes/skin/parisfr/images/header-logo-paris.svg' detail='Télécharger le logo' imgName='louvre' download=true />

    Horizontal layout with tooltip:

    <@cTile title='Hotel de Ville' url='https://paris.fr' detail='Découvrir les services' imgName='hoteldeville' horizontal=true tooltip=true tooltipPos='bottom' />

    Disabled tile:

    <@cTile title='Service indisponible' url='#' detail='Bientôt disponible' imgName='louvre' disabled=true />

    Tiles in a responsive grid:

    <@cRow>
        <@cCol>
            <@cTile title='Hotel de Ville' url='jsp/site/Portal.jsp?page=hotel-de-ville' detail='Découvrir les services' imgName='hoteldeville' />
        </@cCol>
        <@cCol>
            <@cTile title='Opéra' url='jsp/site/Portal.jsp?page=opera' detail='Programmation et billetterie' imgName='opera' />
        </@cCol>
        <@cCol>
            <@cTile title='Sacré Coeur' url='jsp/site/Portal.jsp?page=sacre-coeur' detail='Visiter Montmartre' imgName='sacre-coeur' />
        </@cCol>
    </@cRow>

-->
<#macro cTile title url level=3 target='' detail='' imgName='' badge='' badgeClass='' horizontal=false download=false tooltip=false tooltipPos='top' disabled=false class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if title?has_content && url?has_content>
<#local r=random() />
<#local tileLevel><#if level=1>2<#else>${level!}</#if></#local>
<#local tileLinkClass>tile-link<#if disabled> disabled</#if></#local>
<#local tileClass>tile<#if class !=''> ${class!}</#if><#if horizontal> horizontal</#if><#if download> download</#if><#if disabled> disabled</#if></#local>
<#local tileDownload><#if download> download</#if></#local>
<#local tileTarget><#if download><#else>${target!}</#if></#local>
<#local tileparams><#if params !=''>${params}</#if><#if tooltip> data-bs-toggle="tooltip" data-bs-custom-class="custom-tooltip" data-bs-placement="${tooltipPos}" data-bs-title="${title?js_string} - ${detail?js_string}"}</#if></#local>
<@cBlock class=tileClass id=id params=tileparams>
	<#if imgName!=''>
	<@cBlock class='tile-header'>
		<@cBlock class='tile-img'>
			<@parisIcon name=imgName />
		</@cBlock>
	</@cBlock>
	</#if>
	<@cBlock class="tile-body">
		<#if badge !=''><@cBlock class="tile-badge"><@cBadge label=badge class=badgeClass /></@cBlock ></#if>
		<@cLink href=url! class=tileLinkClass label='' target=tileTarget params=tileDownload>
			<@cTitle level=tileLevel class='tile-title truncate'>${title}</@cTitle>
		</@cLink>
		<#if detail !=''><@cText class="tile-detail truncate">${detail}</@cText></#if>
		<#nested>
	</@cBlock>
</@cBlock>
<#else>
<!-- Erreur Tile : les paramètres title et url sont obligatoires ! -->
</#if>
</#macro>