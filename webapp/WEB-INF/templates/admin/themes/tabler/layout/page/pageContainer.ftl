<#--
Macro: pageContainer

Description: Generates the main page container element. Wraps the page content in a <main> tag with optional full-height layout and header action buttons injection.

Parameters:
- id (string, optional): the ID of the main element. Default is 'lutece-main'.
- template (string, optional): additional template CSS class.
- height (string, optional): set to 'full' to make the container fill the viewport height and hide the footer.
- class (string, optional): additional CSS classes for the main element.
- actions (string, optional): HTML string of action buttons to inject into the page header buttons area.
- params (string, optional): additional HTML attributes for the main element.

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
<#macro pageContainer id='lutece-main' template='' height='' class='' actions='' params='' deprecated...> 
<@deprecatedWarning args=deprecated />
<!-- Begin page content -->
<main role="main" <#if id!=''> id="${id}"</#if>  class="lutece-page ${template} ${class} d-flex" style="<#if height='full'>height:calc(100%-64px);max-height:calc(100%-64px)</#if>" ${params!}>
<#nested>
</main>
<#if height='full'>
<style>
footer {display: none;}
</style>
</#if>
<#if actions!=''>
<script>
document.addEventListener( "DOMContentLoaded", function() {
    const actions = `${actions}`;
    document.querySelector('.page-header-buttons').innerHTML = actions;
});
</script>
</#if>
</#macro>