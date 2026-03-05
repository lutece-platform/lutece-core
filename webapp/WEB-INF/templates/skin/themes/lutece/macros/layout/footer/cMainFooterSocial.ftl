<#--
Macro: cMainFooterSocial

Description: Generates the main site footer with social media buttons (Facebook, X, Instagram, LinkedIn) and navigation links, following the Paris.fr layout.

Parameters:
- title (string, required): Name of the digital service displayed in the footer.
- nested_pos (string, optional): Position of nested content relative to the footer menu: 'before' or 'after'. Default: 'after'.
- socialNested (string, optional): Additional HTML content displayed above the social links section. Default: ''.
- align (string, optional): Flex alignment for the menu area ('around', 'between', 'start', 'end', 'evenly'). Default: 'around'.
- classColMain (string, optional): Bootstrap column class for the menu zone. Default: 'col-md-7'.
- classColSocial (string, optional): Bootstrap column class for the social zone. Default: 'col-md-5'.
- params (string, optional): Additional HTML attributes for the footer element. Default: ''.

Snippet:

    Basic usage:

    <@cMainFooterSocial title='City Portal' />

    Footer with custom layout and extra links:

    <@cMainFooterSocial title='My Service' align='between' classColMain='col-md-8' classColSocial='col-md-4' nested_pos='before'>
        <li class="nav-item"><a class="nav-link" href="/faq">FAQ</a></li>
    </@cMainFooterSocial>

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
                        <@cImg src=logoFooter! class='d-inline-block' id='footer-img' alt='#i18n{portal.theme.labelGoToSite} ${mainSite!}' />
                    </a>
                    <hr aria-hidden="true">
                    <a class="site" title="#i18n{portal.theme.labelGoToSite} ${title}" href=".">${title}</a>
                </div>
            </div>  
            <div class="main-footer justify-content-${align}">
                <div class="d-flex align-items-center">
                    <ul class="nav d-flex justify-content-center justify-content-md-start" aria-label="#i18n{portal.theme.footerInfo}" >
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
                <h2 class="paris-footer-social-title text-center text-gray">#i18n{portal.theme.labelFollow}</h2>
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
                <p class="paris-footer-social-title text-center">#i18n{portal.theme.footerSocialText}</p>
            </div>
        </div>
    </div>
</footer>
<@parisIconPack />
</#macro>