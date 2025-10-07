<#-- Macro: cMainFooterSocial

Description: affiche un pied de page principal avec les boutons réseaux sociaux de Paris.fr.

Parameters:
@param - title - string - required - nom du service numérique
@param - nested_pos - string - required - position du contenu 'nested' passé dans la macro. Valeurs possible avant,'before' et aprés,'after'
@param - socialNested - string - optional - position du contenu 'nested' passé dans la macro. Valeurs possible avant,'before' et après,'after'
@param - align - string - required - alignement de la partie "menu" dans le pied de page. Valeurs possibles : 'arround' | 'between' | 'start' | 'end' | 'evenly'
@param - classColMain - string - required - taille de la zone "menu"
@param - classColSocial - string - required - taille de la zone "social"
@param - params - string - optionnal - permet d'ajouter des parametres HTML au tag footer
-->
<#macro cMainFooterSocial title nested_pos='after' socialNested='' align='around' classColMain='col-md-7' classColSocial='col-md-5' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if !dskey('theme.site_property.menu.sidebarMenu.checkbox')?starts_with('DS') && dskey('theme.site_property.menu.sidebarMenu.checkbox')?number == 1></div></div></#if>
<footer class="social z-1" role="contentinfo"<#if params !=''> ${params!}</#if>>
    <div class="row m-0">
        <div class="col-12 <#if classColMain !=''> ${classColMain}</#if>">
            <div class="main-footer justify-content-${align} py-4">
                <div class="d-flex align-items-center justify-content-center justify-content-lg-start w-100">
                    <a class="logo hide-icon-target" href="${urlMainSite}" target="_blank">
                        <@cImg src=logoFooter! class='d-inline-block' id='footer-img' alt='#i18n{theme.labelGoToSite} ${mainSite!}' />
                    </a>
                    <hr aria-hidden="true">
                    <a class="site" title="#i18n{theme.labelGoToSite} ${title}" href=".">${title}</a>
                </div>
            </div>  
            <div class="main-footer justify-content-${align}">
                <div class="d-flex align-items-center">
                    <ul class="nav d-flex justify-content-center justify-content-md-start" aria-label="#i18n{theme.footerInfo}" >
                        <#if nested_pos='before'><#nested></#if>
                        <@_footerMenu />
                        <#if nested_pos='after'>
                            <#nested>
                        </#if>    
                    </ul>    
                </div>
            </div>
        </div>
        <div class="col-12 main-bg-color<#if classColSocial !=''> ${classColSocial}</#if> d-flex justify-align-items">
            <div class="social-links">
                <#if socialNested !=''><div class="d-flex justify-content-center align-items-center">${socialNested}</div></#if>
                <h2 class="paris-footer-social-title text-center text-gray">#i18n{theme.labelFollow}</h2>
                <ul class="list-inline text-center">
                    <li class="list-inline-item">
                        <a class="social-link has-icon hide-icon-target" target="_blank" aria-label="Aller vers le site de ${footerSocialTitleFb}" href="${footerSocialLinkFb}">        
                            <svg class="paris-icon paris-icon-facebook" role="img" aria-labelledby="paris-icon-title-fb" focusable="false">
                                <title id="paris-icon-title-fb">${footerSocialTitleFb}</title>
                                <use xlink:href="#paris-icon-facebook"></use>
                            </svg>
                        </a>    
                    </li>
                    <li class="list-inline-item">
                        <a class="social-link has-icon hide-icon-target" target="_blank" aria-label="Aller vers le site de ${footerSocialTitleTw}" href="${footerSocialLinkTw}">        
                            <svg class="paris-icon paris-icon-x" role="img" aria-labelledby="paris-icon-title-tw" focusable="false">
                                <title id="paris-icon-title-x">${footerSocialTitleTw}</title>
                                <use xlink:href="#paris-icon-x"></use>
                            </svg>
                        </a>    
                    </li>
                    <li class="list-inline-item">
                        <a class="social-link has-icon hide-icon-target" target="_blank" aria-label="Aller vers le site de ${footerSocialTitleIn}" href="${footerSocialLinkIn}">        
                            <svg class="paris-icon paris-icon-instagram" role="img" aria-labelledby="paris-icon-title-in" focusable="false">
                                <title id="paris-icon-title-in">${footerSocialTitleIn}</title>
                                <use xlink:href="#paris-icon-instagram"></use>
                            </svg>
                        </a>    
                    </li>
                    <li class="list-inline-item">
                        <a class="social-link has-icon hide-icon-target" target="_blank" aria-label="Aller vers le site de ${footerSocialTitleLi}" href="${footerSocialLinkLi}">        
                            <svg class="paris-icon paris-icon-linkedin" role="img" aria-labelledby="paris-icon-title-li" focusable="false">
                                <title id="paris-icon-title-li">${footerSocialTitleLi}</title>
                                <use xlink:href="#paris-icon-linkedin"></use>
                            </svg>
                        </a>    
                    </li>
                </ul>
                <p class="paris-footer-social-title text-center">#i18n{theme.footerSocialText}</p>
            </div>
        </div>
    </div>
</footer>
<@parisIconPack />
</#macro>