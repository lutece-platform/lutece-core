<#--
Macro: codeHighLight

Description: Generates the CSS and JavaScript includes for Prism.js syntax highlighting on code blocks.

Snippet:

    Basic usage (include once in the page):

    <@codeHighLight />

-->
<#macro codeHighLight >
<link rel="stylesheet" href="js/admin/lib/prism/prism.css">
<link rel="stylesheet" href="js/admin/lib/prism/prism-live.css">
<script src="js/admin/lib/prism/prism.js"></script>
<script src="js/admin/lib/prism/prism-live.js"></script>
</#macro>