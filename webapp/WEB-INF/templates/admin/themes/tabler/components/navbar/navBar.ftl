<#-- Macro: navBar
Description: Generates a  navigation bar with the specified tag, class, ID, and parameters.
Parameters:
- tag (string, optional): the HTML tag to use for the navigation bar (default is "nav").
- class (string, optional): the class to use for the navigation bar.
- id (string, optional): the ID to use for the navigation bar.
- params (string, optional): additional parameters to include in the navigation bar.

Snippet:

    Basic navigation bar:

    <@navBar class='navbar-expand-lg navbar-light bg-light'>
        <a class="navbar-brand" href="#">My App</a>
        <@nav type='tab'>
            <@navItem href='#home' name='Home' active=true />
            <@navItem href='#about' name='About' />
        </@nav>
    </@navBar>

    Navigation bar with custom ID:

    <@navBar id='adminNavbar' class='navbar-dark bg-dark'>
        <a class="navbar-brand" href="#">Admin</a>
    </@navBar>

-->
<#macro navBar tag='nav' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<${tag} class="navbar<#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</${tag}>
</#macro>