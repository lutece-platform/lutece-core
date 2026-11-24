<#--
Macro: codeHighLight

Description: Generates the CSS and JavaScript includes for Prism.js syntax highlighting on `<pre><code>` blocks. Include once per page in the head or before the closing body tag.

Parameters:
- live (boolean, optional): also load Prism Live; every textarea.prism-live of the page becomes a code editor highlighted as you type. Default: false.
- liveLanguages (string, optional): comma-separated Prism Live language helpers to load (css, javascript, markup). Default: 'css,javascript,markup'.

Snippet:

    Basic usage (include once in the page layout):

    <@codeHighLight />

    Then mark up your code blocks with the appropriate language class:

    <pre><code class="language-javascript">
    const greeting = 'Hello, world!';
    console.log(greeting);
    </code></pre>

    Code editor on a textarea:

    <@codeHighLight live=true />
    <textarea name="content" class="prism-live language-html" rows="10"></textarea>

-->
<#macro codeHighLight live=false liveLanguages='css,javascript,markup'>
<link rel="stylesheet" href="themes/admin/shared/js/lib/prism/prism.css">
<script src="themes/admin/shared/js/lib/prism/prism.js"></script>
<#if live>
<link rel="stylesheet" href="themes/admin/shared/js/lib/prism/live/prism-live-lutece.css">
<script src="themes/admin/shared/js/lib/prism/live/src/prism-live.js?load=${liveLanguages?url}"></script>
</#if>
</#macro>