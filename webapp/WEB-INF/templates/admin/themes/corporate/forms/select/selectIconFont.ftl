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
- type (string, optional): the type of the icon file (either "yaml" or "json").
- prefix (string, optional): the prefix of the Font icon class.
- iconsUrl (string, optional): the URL of the YAML or JSON file containing the icon data.
- resources (boolean, optional): whether to load the SlimSelect CSS and JS resources.
-->
<#macro selectIconFont id='selectIcon' class='' name='resource-icon' showListLabel=true showListIcon=true searchShow=true searchFocus=false searchHighlight=true type='yaml' prefix='fa-' iconsUrl='css/admin/font-awesome-icons.yml' resources=true defaultValue=''>
<@select name=name id=id class=class />
<#if resources>
<link rel="stylesheet" href="js/admin/lib/slimselect/slimselect.min.css">
<script src="js/admin/lib/slimselect/slimselect.min.js"></script>
<#if type='yaml'>
<!-- js yaml parser -->
<script src="js/admin/lib/slimselect/js-yaml.min.js"></script>
</#if>
</#if>
<script>
$.get('${iconsUrl}', function(data) {
<#if type='yaml'>
	let parsedIcons = jsyaml.load(data)
<#else>
	<#-- Json / Object -->
	let parsedIcons='';
	if( typeof data === 'object' ){
		parsedIcons = data;
	} else { 
		parsedIcons = JSON.parse( data )
	}
</#if>
$.each( parsedIcons, function(index, icon){
	let i=0, prefix='${prefix}';
	let selected = '${defaultValue}' == index ? 'selected' : '';
	<#if type='yaml'>
		<#-- FontAwesome specific / Other lib add another test/treatment -->
		while( i < icon.styles.length ){
			switch ( icon.styles[i] ) {
				case 'regular':
					prefix = 'far'
					break;
				case 'solid':
					prefix = 'fas'
					break;
				case 'light':
					prefix = 'fal'
					break;
				case 'brands':
					prefix = 'fab'
					break;
			}
			$('#${id}').append('<option value="' + index + '"' + selected + '> <#if showListIcon><span class="' + prefix + ' fa-'+ index + ' mr-1" ' + selected + ' ></span>&nbsp;</#if>' <#if showListLabel>+ icon.label + ' [' + icon.styles[i] +  ']'</#if>+ '</option>');
			i++;
		}
	<#else>
		<#-- Json / Object -->
		$('#${id}').append('<option value="' + index + '" ' + selected + ' > <#if showListIcon><span class="' + prefix + ' fa-'+ index + '"></span>&nbsp;</#if>' <#if showListLabel>+ index </#if>+ '</option>');
	</#if>
});
new SlimSelect({ 
	select: '#${id}',
	showSearch: ${searchShow?c},
	searchText: "#i18n{portal.util.labelNoItem}",
	searchPlaceholder: '#i18n{portal.util.labelSearch}',
	searchFocus: ${searchFocus?c}, // Whether or not to focus on the search input field
	searchHighlight: ${searchHighlight?c}
})
});
</script>
</#macro>