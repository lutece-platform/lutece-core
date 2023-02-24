<#-- Macro: jsTreeInit

Description: Initializes a jstree instance for displaying a tree structure.

Parameters:
- id (string, optional): the ID of the jstree element.
- btntreeSearch (string, optional): the ID of the search input field.
- multiple (boolean, optional): whether multiple nodes can be selected.
- plugins (list, optional): a list of jstree plugins to use.
- style (string, optional): the name of the jstree theme to use.
-->
<#macro jsTreeInit id='tree' btntreeSearch='treesearch' multiple=false plugins=[] style='proton/style.min.css'>
<link rel="stylesheet" href="js/admin/lib/jstree/themes/proton/style.min.css" >
<script src="js/admin/lib/jstree/jstree.min.js"></script>
<script>
$(function(){
	let selectedTree=jsTreeId=localStorage.getItem('jsTreeSelectedId' );

	$('#tree').jstree({
		'core': {
			'multiple' : false,
            'themes': {
                'name': 'proton',
                'responsive': true
            }
        },
	 	'plugins' : [  'search' , 'wholerow' ,'changed'  ]
	}).on('click', function( e ){
		window.location.replace( e.target.getAttributeNode('href').value );
	}).on("changed.jstree", function (e, data) {
		localStorage.setItem( 'jsTreeSelectedId', data.changed.selected );
    });

	$('#tree').jstree( 'select_node', jsTreeId );
   
	var to = false;
	$('#treesearch').keyup(function () {
		if(to) { clearTimeout(to); }
		to = setTimeout(function () {
		var v = $('#treesearch').val();
		$('#tree').jstree(true).search(v);
		}, 250);
	});

});
</script>
</#macro>