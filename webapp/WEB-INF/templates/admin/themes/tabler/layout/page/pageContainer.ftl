<#--
Macro: pageContainer

Description: Generates the main page container element. Wraps the page content in a <main> tag with optional full-height layout and header action buttons injection.

Parameters:
- id (string, optional): the ID of the main element. Default is 'lutece-main'.
- template (string, optional): additional template CSS class.
- height (string, optional): set to 'full' to make the container fill the viewport height and hide the footer.
- class (string, optional): additional CSS classes for the main element.
- actions (string, optional): HTML string of action buttons to inject into the page header buttons area.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Basic page container:

    <@pageContainer>
        <p>Page content goes here</p>
    </@pageContainer>

    Full-height page container with custom class and header actions:

    <@pageContainer height='full' class='my-custom-page' actions='<a class="btn btn-primary" href="#">New Item</a>'>
        <p>Full-height page content</p>
    </@pageContainer>

-->
<#macro pageContainer id='lutece-main' template='' height='' class='' actions='' params={} deprecated...> 
<@deprecatedWarning args=deprecated />
<!-- Begin page content -->
<main role="main" <@commonAttrs id=id params=params />  class="lutece-page ${template} ${class} d-flex" style="<#if height='full'>height:calc(100%-64px);max-height:calc(100%-64px)</#if>">
<#nested>
</main>
<#if height='full'>
<style>
footer {display: none;}
</style>
</#if>
<#if actions?has_content>
<script>
document.addEventListener( "DOMContentLoaded", function() {
    const actions = '${actions?is_markup_output?then( actions?markup_string, actions )?js_string?no_esc}';
    document.querySelector('.page-header-buttons').innerHTML = actions;
});
</script>
</#if>
</#macro>