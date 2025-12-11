<#-- Macro: jsTreeInit

DEPRECATED 

Description: Initializes a jstree instance for displaying a tree structure.

Parameters:
- id (string, optional): the ID of the jstree element.
- btntreeSearch (string, optional): the ID of the search input field.
- multiple (boolean, optional): whether multiple nodes can be selected.
- plugins (list, optional): a list of jstree plugins to use.
- style (string, optional): the name of the jstree theme to use.
-->
<#macro jsTreeInit id='tree' btntreeSearch='treesearch' multiple=false plugins=[] style='proton/style.min.css' deprecated...>
<@deprecatedWarning args=deprecated />>
<!-- JSTREE is depecated use admin module "themes/shared/modules/luteceTree.js" -->
<script>
console.log(' // JSTREE is depecated use admin module "themes/shared/modules/luteceTree.js"')
</script>
</#macro>