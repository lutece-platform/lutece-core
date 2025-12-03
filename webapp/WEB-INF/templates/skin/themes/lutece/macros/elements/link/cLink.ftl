<#-- Macro: cLink

Description: permet de créer un lien.

Parameters:

@param - id - string - optional - l'ID du lien
@param - class - string - optional - ajoute une classe CSS au lien. Afin de donner l'illusion d'un bouton, il est possible d'utiliser la classe 'btn' suivi de la classe du bouton souhaité (par exemple: 'btn btn-primary'). Ne pas hésiter à consulter les exemples dans la macro cButton.
@param - href - string - required - permet définir l'url de redirection du lien
@param - label - string - required - permet définir le libellé du lien
@param - title - string - optional - permet définir l'attribut 'title' du lien
@param - nestedPos - string - optional - permet de définir la position du contenu "#nested"(par défaut: 'after')
@param - target - string - optional - permet de définir le type d'ouverture du lien via l'attribut 'target' (valeurs possibles: '_blank', '_top', '_parent' ou 'id')
@param - showTarget - boolean - optional - permet d'afficher un icon si target='_blank' (par défaut: false)
@param - params - string - optional - permet d'ajouter des parametres HTML au lien
-->
<#macro cLink href label title='' nestedPos='after' target='' showTarget=false class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<a href="${href!}" <#if id!=''> id="${id!}"</#if> <#if class !='' >class="${class!}"</#if><#if title!=''> title="${title!}<#if target='_blank'> #i18n{portal.theme.newWindowLink}</#if>"</#if><#if target!=''> target="${target}"</#if><#if params!=''> ${params}</#if> > 
<#if nestedPos!='after'><#nested></#if>
<#if label!=''><span class="link-label">${label!}</span></#if> 
<#if nestedPos='after'> <#nested></#if>
<#if target='_blank' && label=''><span class="visually-hidden visually-hidden-focusable"><#if title!=''>${title!}<#else>${label}</#if> #i18n{portal.theme.newWindowLink}</span></#if>
<#--  <#if target='_blank' && showTarget><svg width="18" height="18" viewBox="0 0 28 28" fill="currentColor" xmlns="http://www.w3.org/2000/svg"><g clip-path="url(#clip0_3081_1959)"><path d="M4.48047 23.3337V4.66699H13.4405V7.2759H6.98501V20.7248H19.8959V14.0003H22.4005V23.3337H4.48047ZM11.6808 17.6429L9.94359 15.8334L18.1588 7.2759H15.3307V4.66699H22.4005V12.0314H19.8959V9.08545L11.6808 17.6429Z" fill="currentColor"/></g><defs><clipPath id="clip0_3081_1959"><rect width="28" height="28" fill="white"/></clipPath></defs></svg></#if>  -->
</a>
</#macro>