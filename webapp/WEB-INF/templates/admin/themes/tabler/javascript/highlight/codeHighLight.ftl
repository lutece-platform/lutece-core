<#--
Macro: codeHighLight

Description: Adds syntax highlighting to code blocks using the Prism.js library.

Snippet:

    Include Prism.js syntax highlighting resources:

    <@codeHighLight />

-->
<#macro codeHighLight >
<link rel="stylesheet" href="js/admin/lib/prism/prism.css">
<link rel="stylesheet" href="js/admin/lib/prism/prism-live.css">
<script src="js/admin/lib/prism/prism.js"></script>
<script src="js/admin/lib/prism/prism-live.js"></script>
</#macro>