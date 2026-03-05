<#--
Macro: cMainFooter

Description: Generates the main site footer with navigation links, social media icons, and copyright information.

Parameters:
- title (string, required): Name of the digital service displayed in the footer. Default: mainSite.
- nested_pos (string, required): Position of nested content relative to the footer menu: 'before' or 'after'. Default: 'after'.
- params (string, optional): Additional HTML attributes for the footer element. Default: ''.

Snippet:

    Basic usage:

    <@cMainFooter title='My City Portal' />

    Footer with additional links before the default menu:

    <@cMainFooter title='City Services' nested_pos='before'>
        <li class="list-inline-item"><a href="/faq">FAQ</a></li>
    </@cMainFooter>

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