<#-- Macro: cMainFooter

Description: affiche un pied de page.

Parameters:
@param - title - string - required - nom du service numérique
@param - nested_pos - string - required - Position du contenu 'nested' passé dans la macro. Valeurs possible avant,'before' et aprés,'after'
@param - params - string - optionnal - permet d'ajouter des parametres HTML au tag footer
-->
<#macro cMainFooter title=mainSite nested_pos='after'  params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if !dskey('theme.site_property.menu.sidebarMenu.checkbox')?starts_with('DS') && dskey('theme.site_property.menu.sidebarMenu.checkbox')?number == 1></div></div></#if>
<footer role="contentinfo"<#if params !=''> ${params!}</#if>>
  <div class="container">
    <div class="row align-items-center border-bottom py-5">
      <div class="col-lg-8">
        <ul class="list-inline footer-menu text-center text-lg-start"  aria-label="#i18n{portal.theme.footerInfo}" >
            <#if nested_pos='before'><#nested></#if>
            <@_footerMenu />
            <#if nested_pos='after'>
                <#nested>
            </#if>  
        </ul>
      </div>
      <div class="col-lg-4">
        <ul class="list-inline social-icons text-lg-end text-center">
            <li class="list-inline-item">
                <a target="_blank" aria-label="#i18n{portal.theme.labelGoToSite} ${footerSocialTitleTw}" href="${footerSocialLinkTw}" class="hide-icon-target">        
                    <i class="ti ti-brand-x"></i>
                </a>
            </li>
            <li class="list-inline-item">
                <a target="_blank" href="#" aria-label="#i18n{portal.theme.labelGoToSite} ${footerSocialTitleGithub}" href="${footerSocialLinkGithub}" class="hide-icon-target">
                    <i class="ti ti-brand-github"></i>
                </a>
            </li>
        </ul>
      </div>
    </div>
    <div class="py-4 text-center">
      <small class="text-light">LUTECE - Copyright © 2025 Ville de Paris</small>
    </div>
  </div>
</footer>
</#macro>