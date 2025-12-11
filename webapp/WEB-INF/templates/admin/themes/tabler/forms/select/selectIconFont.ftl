<#-- 
Macro: selectIconFont

Description: Generates a select box with options for Font icons. The options are loaded from a YAML or JSON file and include labels and styles.

Parameters:
- id (string, optional): the ID of the select box.
- class (string, optional): the CSS class of the select box.
- name (string, optional): the name of the select box.
- showListLabel (boolean, optional): whether to display the label of each option in the select box.
- showListIcon (boolean, optional): whether to display the icon of each option in the select box.
- searchShow (boolean, optional): whether to show the search box for the select box.
- searchFocus (boolean, optional): whether to focus on the search box when it is displayed.
- searchHighlight (boolean, optional): whether to highlight the matched search terms in the options.
- type (string, optional): the type of the icon file ("json" by default).
- prefix (string, optional): the prefix of the Font icon class.
- iconsUrl (string, optional): the URL of the YAML or JSON file containing the icon data.
- resources (boolean, optional): whether to load the SlimSelect CSS and JS resources.
-->
<#-- TODO Check accessibility of slim-select lib -->
<#macro selectIconFont id='selectIcon' class='' name='resource-icon' showListLabel=true showListIcon=true searchShow=true searchFocus=false searchHighlight=true type='json' prefix='ti' iconsUrl='themes/admin/shared/css/vendor/tabler/tabler-icons.json' resources=true defaultValue='' deprecated...>
<@deprecatedWarning args=deprecated />
<@select name=name id=id class=class />
<#if resources>
<link rel="stylesheet" href="js/admin/lib/slimselect/slimselect.min.css">
<script src="js/admin/lib/slimselect/slimselect.min.js"></script>
</#if>
<script>
const request = new XMLHttpRequest();
request.open( "GET", "${iconsUrl}", false); // `false` makes the request synchronous
request.send( null );

if (request.status === 200) {
	const data = request.response;
	let parsedIcons='';
	if( typeof data === 'object' ){
		parsedIcons = data;
	} else { 
		parsedIcons = JSON.parse( data )
	}
	const keyIcons = Object.keys( parsedIcons ), iconList = document.getElementById('${id}'), prefix='${prefix}';
			
	keyIcons.forEach( (icon , idx ) => {
		let selected = '${defaultValue!}' === icon ? 'selected' : '';
		const optionIcon = document.createElement("option");
		optionIcon.value = icon;
		if ( '${defaultValue!}' === icon ){
			optionIcon.setAttribute( 'selected', '' )
		}
		<#if showListIcon>
		const optionSpanIcon = document.createElement("span");
		optionSpanIcon.classList.add( prefix )
		<#noparse>optionSpanIcon.classList.add( `ti-${icon}` )</#noparse>
		optionIcon.append( optionSpanIcon )
		</#if>
		<#if showListLabel>
		const optionSpanLabel = document.createElement("span");
		optionSpanLabel.classList.add( 'ms-2' )
		optionSpanLabel.textContent = icon
		optionIcon.append( optionSpanLabel )
		</#if>
		iconList.append( optionIcon );
	})

	new SlimSelect({ 
		select: '#${id}',
		showSearch: ${searchShow?c},
		searchText: "#i18n{portal.util.labelNoItem}",
		searchPlaceholder: '#i18n{portal.util.labelSearch}',
		searchFocus: ${searchFocus?c}, // Whether or not to focus on the search input field
		searchHighlight: ${searchHighlight?c}
	})
}
</script>
</#macro>