<#-- Macro: cMainNavUser

Description: affiche le menu utilisateur.

Parameters:
@param - id - string - optional - identifiant unique du menu
@param - class - string - optional - classe(s) css du menu
@param - connected - boolean - required - indique si une connexion utilisateur existe
@param - userName - boolean - required - nom de l'utilisateur si connecté
@param - urlConnect - string - required - url de connexion ou de déconnexion
@param - btnToggle - boolean - required -  ouvre le menu dans un dropdown
@param - userFullName - string - optional - nom de l'utilisateur à afficher
@param - userEmail - string - optional - email de l'utilisateur à afficher
@param - userInitials - string - optional - initiales de l'utilisateur à afficher
@param - hasIcon - boolean - required - permet d'afficher ou non l'icone utilisateur
@param - title - string - required - libellé pour les icônes
@param - params - string - optional - permet d'ajouter des paramètres HTML au menu
-->
<#macro cMainNavUser connected userName urlConnect btnToggle=true userFullName='' userEmail='' userInitials='' hasIcon=false title='#i18n{theme.labelConnect}' id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<ul id="auth-wrapper" class="navbar-nav mon-auth">
    <li class="nav-item mt-0 dropdown<#if class !='' > ${class!}</#if>"<#if id !='' > id="${id!}"</#if><#if params!=''> ${params}</#if>>
    <#if !connected>
        <#if btnToggle>
        <button type="button" class="nav-link" id="dropdownAuthUser" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" aria-label="#i18n{theme.titleConnect}">
        <#else>
        <button type="button" class="nav-link" href="${urlConnect!'jsp/site/Portal.jsp?page=mylutece&amp;action=login'}" aria-label="#i18n{theme.titleConnect}">
        </#if>
            <span>#i18n{theme.labelConnect}</span>
        </button>
    <#else>
        <button type="button" class="nav-link" href="#" role="button" id="dropdownAuthUser" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" >
            ${userName}
        </button>
    </#if>
        <div class="dropdown-menu<#if class ='show' > ${class!}</#if>" aria-labelledby="dropdownAuthUser">
            <div class="dropdown-content d-flex flex-column align-items-center">
                <#if !connected>
                    <h2 class="h3">#i18n{theme.labelMonAuthAccount}</h2>
                    <p class="mt-xl pb-l">#i18n{theme.labelMonAuthAccountExists}</p>
                    <a class="btn btn-primary btn-user btn-default-user" href="${urlConnect!'jsp/site/Portal.jsp?page=mylutece&amp;action=login'}" aria-label="#i18n{theme.titleConnect}">
                        <span>#i18n{theme.labelBtnConnect}</span>
                    </a>
                    <p class="py-m lh-sm">#i18n{theme.labelMonAuthHelp}</p>
                    <a class="btn btn-primary btn-user btn-outline-user" href="jsp/site/Portal.jsp?page=myluteceusergu&view=createAccount" aria-label="#i18n{theme.titleConnect}">
                        <span>#i18n{theme.labelMonAuthCreateAccount} #i18n{theme.labelMonAuth}</span>
                    </a>
                <#else>    
                    <h2 class="h3" id="myluteceusername">${userFullName!}</h2>
                    <p class="font-bold">${userEmail!}</p>
                    <p>
                        <a class="btn btn-primary" href="jsp/site/Portal.jsp?page=mydashboard" aria-label="#i18n{theme.titleConnect}" title="#i18n{theme.titleConnect}">
                            #i18n{theme.labelMonAuthHome}
                        </a>
                    </p>
                    <p class="border-bottom border-b-md main-info-border-color pb-1">
                        <a href="jsp/site/Portal.jsp?page=mydashboard&panel=datas#datas" class="fs-sm font-bold main-info-color">#i18n{theme.labelMonAuthManagement}</a>
                    </p>
                    <#nested>
                    <hr>
                    <p class="services show main-color font-bold">#i18n{theme.labelMonAuthFavorites}</p>
                    <div id="auth-favs" class="d-flex flex-wrap justify-content-center align-items-center"></div>
                    <div id="auth-apps" class="d-flex flex-wrap justify-content-center align-items-center"></div>
                    <p class="show">
                        <a class="btn btn-link-primary" href="${urlConnect!}" title="#i18n{theme.titleDisconnect}">
                            <span>#i18n{theme.labelDisconnect}</span>  
                        </a>
                    </p>
                </#if>    
                <@cText class='mt-5 text-center'><@cLink href=footerLinkLegal! target='_blank' title='#i18n{theme.site_property.Url.legalURL}' label='#i18n{theme.site_property.Url.legalURL}' /></@cText>
            </div>
        </div>
    </li>
</ul>
</#macro>