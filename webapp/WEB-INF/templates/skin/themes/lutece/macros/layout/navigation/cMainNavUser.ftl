<#--
Macro: cMainNavUser

Description: Generates the user authentication menu with login/register dropdown for anonymous users and account dashboard dropdown for connected users.

Parameters:
- connected (boolean, required): Indicates whether a user session exists.
- userName (string, required): Display name of the connected user.
- urlConnect (string, required): URL for the login or logout action.
- btnToggle (boolean, optional): If true, opens the user menu in a dropdown. Default: true.
- userFullName (string, optional): Full name of the connected user. Default: ''.
- userEmail (string, optional): Email address of the connected user. Default: ''.
- userInitials (string, optional): Initials of the connected user for avatar display. Default: ''.
- hasIcon (boolean, optional): If true, displays a user icon. Default: false.
- title (string, optional): Accessible label for the connection button/icon. Default: '#i18n{portal.theme.labelConnect}'.
- id (string, optional): Unique identifier for the menu item. Default: ''.
- class (string, optional): Additional CSS class(es) for the menu item. Default: ''.
- params (string, optional): Additional HTML attributes for the menu item. Default: ''.

Snippet:

    User menu for anonymous visitors:

    <@cMainNavUser connected=false userName='' urlConnect='jsp/site/Portal.jsp?page=mylutece&action=login' />

    User menu for connected user:

    <@cMainNavUser connected=true userName='John D.' urlConnect='jsp/site/Portal.jsp?page=mylutece&action=logout' userFullName='John Doe' userEmail='john.doe@example.com'>
        <p><a href="jsp/site/Portal.jsp?page=myservices">My Services</a></p>
    </@cMainNavUser>

-->
<#macro cMainNavUser connected userName urlConnect btnToggle=true userFullName='' userEmail='' userInitials='' hasIcon=false title='#i18n{portal.theme.labelConnect}' id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<ul id="auth-wrapper" class="navbar-nav mon-auth">
    <li class="nav-item mt-0 dropdown<#if class !='' > ${class!}</#if>"<#if id !='' > id="${id!}"</#if><#if params!=''> ${params}</#if>>
    <#if !connected>
        <#if btnToggle>
        <button type="button" class="nav-link" id="dropdownAuthUser" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" aria-label="#i18n{portal.theme.titleConnect}">
        <#else>
        <button type="button" class="nav-link" href="${urlConnect!'jsp/site/Portal.jsp?page=mylutece&amp;action=login'}" aria-label="#i18n{portal.theme.titleConnect}">
        </#if>
            <span>#i18n{portal.theme.labelConnect}</span>
        </button>
    <#else>
        <button type="button" class="nav-link" href="#" role="button" id="dropdownAuthUser" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" >
            ${userName}
        </button>
    </#if>
        <div class="dropdown-menu<#if class ='show' > ${class!}</#if>" aria-labelledby="dropdownAuthUser">
            <div class="dropdown-content d-flex flex-column align-items-center">
                <#if !connected>
                    <h2 class="h3">#i18n{portal.theme.labelMonAuthAccount}</h2>
                    <p class="mt-xl pb-l">#i18n{portal.theme.labelMonAuthAccountExists}</p>
                    <a class="btn btn-primary btn-user btn-default-user" href="${urlConnect!'jsp/site/Portal.jsp?page=mylutece&amp;action=login'}" aria-label="#i18n{portal.theme.titleConnect}">
                        <span>#i18n{portal.theme.labelBtnConnect}</span>
                    </a>
                    <p class="py-m lh-sm">#i18n{portal.theme.labelMonAuthHelp}</p>
                    <a class="btn btn-primary btn-user btn-outline-user" href="jsp/site/Portal.jsp?page=myluteceusergu&view=createAccount" aria-label="#i18n{portal.theme.titleConnect}">
                        <span>#i18n{portal.theme.labelMonAuthCreateAccount} #i18n{portal.theme.labelMonAuth}</span>
                    </a>
                <#else>    
                    <h2 class="h3" id="myluteceusername">${userFullName!}</h2>
                    <p class="font-bold">${userEmail!}</p>
                    <p>
                        <a class="btn btn-primary" href="jsp/site/Portal.jsp?page=mydashboard" aria-label="#i18n{portal.theme.titleConnect}" title="#i18n{portal.theme.titleConnect}">
                            #i18n{portal.theme.labelMonAuthHome}
                        </a>
                    </p>
                    <p class="border-bottom border-b-md main-info-border-color pb-1">
                        <a href="jsp/site/Portal.jsp?page=mydashboard&panel=datas#datas" class="fs-sm font-bold main-info-color">#i18n{portal.theme.labelMonAuthManagement}</a>
                    </p>
                    <#nested>
                    <hr>
                    <p class="services show main-color font-bold">#i18n{portal.theme.labelMonAuthFavorites}</p>
                    <div id="auth-favs" class="d-flex flex-wrap justify-content-center align-items-center"></div>
                    <div id="auth-apps" class="d-flex flex-wrap justify-content-center align-items-center"></div>
                    <p class="show">
                        <a class="btn btn-link-primary" href="${urlConnect!}" title="#i18n{portal.theme.titleDisconnect}">
                            <span>#i18n{portal.theme.labelDisconnect}</span>  
                        </a>
                    </p>
                </#if>    
                <@cText class='mt-5 text-center'><@cLink href=footerLinkLegal! target='_blank' title='#i18n{portal.theme.site_property.Url.legalURL}' label='#i18n{portal.theme.site_property.Url.legalURL}' /></@cText>
            </div>
        </div>
    </li>
</ul>
</#macro>