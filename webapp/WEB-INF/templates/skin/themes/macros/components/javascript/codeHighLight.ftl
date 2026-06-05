<#--
Macro: codeHighLight

Description: Generates the CSS and JavaScript includes for Prism.js syntax highlighting on `<pre><code>` blocks. Include once per page in the head or before the closing body tag.

Parameters: None.

Snippet:

    Basic usage (include once in the page layout):

    <@codeHighLight />

    Then mark up your code blocks with the appropriate language class:

    <pre><code class="language-javascript">
    const greeting = 'Hello, world!';
    console.log(greeting);
    </code></pre>

-->
<#macro codeHighLight >
<link rel="stylesheet" href="js/admin/lib/prism/prism.css">
<link rel="stylesheet" href="js/admin/lib/prism/prism-live.css">
<script src="js/admin/lib/prism/prism.js"></script>
<script src="js/admin/lib/prism/prism-live.js"></script>
</#macro>