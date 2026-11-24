<#--
Macro: codeHighLight

Description: Adds syntax highlighting to code blocks using the Prism.js library and, optionally, Prism Live: editable code areas
(a textarea with the class prism-live and a language class, highlighted as you type, with indentation and snippets).

Parameters:
- live (boolean, optional): also load Prism Live; every textarea.prism-live of the page becomes a code editor. Default: false.
- liveLanguages (string, optional): comma-separated Prism Live language helpers to load (css, javascript, markup). Default: 'css,javascript,markup'.

Snippet:

    Include Prism.js syntax highlighting resources:

    <@codeHighLight />

    Code editor on a textarea:

    <@codeHighLight live=true />
    <@input type='textarea' name='content' class='prism-live language-html' rows=10>${content!}</@input>

-->
<#macro codeHighLight live=false liveLanguages='css,javascript,markup'>
<link rel="stylesheet" href="themes/admin/shared/js/lib/prism/prism.css">
<script src="themes/admin/shared/js/lib/prism/prism.js"></script>
<#if live>
<link rel="stylesheet" href="themes/admin/shared/js/lib/prism/live/prism-live-lutece.css">
<script src="themes/admin/shared/js/lib/prism/live/src/prism-live.js?load=${liveLanguages?url}"></script>
</#if>
</#macro>
