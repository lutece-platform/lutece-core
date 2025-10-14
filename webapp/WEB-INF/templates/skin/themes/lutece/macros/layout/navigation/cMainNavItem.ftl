<#-- Macro: cMainNavItem

Description: affiche un élément de navigation.

Parameters:
@param - id - string - optional - identifiant unique de l'élément de navigation
@param - class - string - optional - classe(s) css de l'élément de navigation
@param - title - string - required - titre de l'élément de navigation
@param - url - string - required - url de redirection de l'élément de navigation, si vide n'ajoute pas la balise a autour du contenu #nested
@param - urlClass - string - optional - classe(s) css de l'élément lien de navigation
@param - target - string - optional - les valeurs possibles sont '', _top, _blank, _parent
@param - role - string - optional - les valeurs possibles sont '', navitem
@param - showTitle - boolean - required -  ajoute l'attribute title avec le libellé du paramètre "title"
@param - params - string - optional - permet d'ajouter des paramètres HTML à l'élément de navigation
-->
<#macro cMainNavItem title url urlClass='' target='' role='navitem' showTitle=false id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li class="nav-item<#if class !='' > ${class!}</#if>"<#if id !='' > id="${id!}"</#if><#if params!=''> ${params}</#if>>
<#if url !=''>
    <a class="nav-link<#if urlClass !='' > ${urlClass!}</#if>"<#if role !=''> role="${role!}"</#if> href="${url}" <#if showTitle>title="${title!}"</#if><#if target!=''> target="${target}"</#if>>
        ${title!}<#if target='_blank'> <span class="visually-hidden">#i18n{theme.newWindowLink}</span></#if>
        <#nested>
    </a>
<#else>
    <#nested>
</#if>
</li> 
</#macro>