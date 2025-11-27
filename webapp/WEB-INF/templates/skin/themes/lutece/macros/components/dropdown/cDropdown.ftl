<#-- Macro: cDropdown

Description: Affiche un bouton avec menu "dropdown".

Parameters:
@param - label - string - required - le titre du bouton
@param - items - json - optional - si valeur, permet d'ajouter la liste d'item dans le bouton. La forme doit être {"label": "Item 1","action": "#","active": "false"}, {"label": "Item 2","action": "#","active": "false"}
@param - itemType - string - optional - si la valeur est link, permet d'ajouter la liste d'item spus forme de lien, sinon ce seront des boutons dans ce cas il faut préciser le paramètre "action" a "button" ou "submit".
@param - type - string - optional - 
@param - direction - string - optional - valeur par défaut '', les valeurs possibles sont : 'inside'
@param - autoclose - string - optional - valeur par défaut '' ( = 'false' ), les valeurs possibles sont : 'inside', 'outside', 'true' ou 'false' 
@param - dark - boolean - optional - par défaut false, si true passe le menu en mode "dark"
@param - nobutton - boolean - optional - permet de supprimer le bouton . A utiliser si le contenu n'est pas une liste par exemple.
@param - header - string - optional - 
@param - dropDownMenuType - string - optional - 
@param - class - string - optional - permet d'ajouter une classe CSS au bouton (valeur existantes dans le CSS: 'primary', 'secondary', 'tertiary', 'danger', 'expand')
@param - btnClass - string - optional - permet d'ajouter une classe CSS au label du bouton
@param - id - string - optional - l'ID du bouton
@param - disabled - boolean - optional - permet de désactiver le bouton (par défaut: false)
@param - params - string - optional - permet d'ajouter des parametres HTML au bouton
#nested : optionnal - permet d'ajouter des éléments dans le menu déroulant
 -->
<#macro cDropdown label items=[] itemType='link' type='' centered=false dropup=false autoclose='' nobutton=false header='' dark=false dropDownMenuType='ul' class='' btnClass='secondary' noclass=false id='' disabled=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if centered>
<div class="btn-group ${direction}">
</#if>
<div class="dropdown<#if class!=''> ${class}</#if>"<#if id!=''> id="${id!}"</#if><#if params!=''>${params!}</#if>>
<#if !nobutton>
    <#if type !='split'>
    <button class="btn btn-${btnClass!} dropdown-toggle" type="button" data-bs-toggle="dropdown"<#if autoclose !=''> data-bs-auto-close="${autoclose!}" </#if> aria-expanded="false"<#if disabled> disabled</#if>>
        ${label!}
    </button>
    <#else>
    <button type="button" class="btn btn-${btnClass!}">${label!}</button>
    <button type="button" class="btn btn-${btnClass!} dropdown-toggle dropdown-toggle-split" data-bs-toggle="dropdown" aria-expanded="false">
        <span class="visually-hidden">#i18n{themeparisfr.labelDropdownToggle}</span>
    </button>
    </#if>
</#if>
    <${dropDownMenuType} class="dropdown-menu<#if dark> dropdown-menu-dark</#if>">
        <#if header !=''><li><h4 class="dropdown-header">${header!}</h6></li></#if>
        <#if items?has_content && items?size gt 0>
            <#list items as item>
                <li><#if itemType='link'><a class="dropdown-item<#if item.active='true'> active</#if><#if item.disabled='true'> disabled</#if>"<#if item.disabled='true'> aria-disabled="true"</#if> href="${item.action!}">${item.label!}</a><#else><button class="dropdown-item<#if item.active='true'> active</#if><#if item.disabled='true'> disabled</#if>"<#if item.disabled='true'> aria-disabled="true"</#if> type="${item.action}">${item.label!}</button></#if></li>
            </#list>
        </#if>
        <#nested>
    </${dropDownMenuType}>
</div>
<#if centered>
</div>
</#if>
</#macro>
<#-- Macro: cDropdownItem

Description: Affiche un item de menu "dropdown".

Parameters:
@param - label - string - required - le titre du bouton
@param - action - string - optional - Url du lien pour l'item de menu pour le type 'link' sinon 'button' ou 'submit' pour le type 'button'
@param - type - string - optional - 'link' par défaut  sinon 'button'.
@param - active - boolean - optional - False par défaut, si true ajoute la classe "active" sur l'item.
@param - disabled - boolean - optional - False par défaut, si true ajoute la classe "active" sur l'item.
@param - header - boolean - optional - False par défaut, si true ajoute la classe "disabled" sur l'item.
@param - nestedPos - string - optional - permet de gérer la position du contenu "nested" avant ou après le label (par défaut: 'before', valeurs possibles: 'before', 'after')
@param - id - string - optional - l'ID du bouton
@param - class - string - optional - permet d'ajouter une classe CSS au bouton (valeur existantes dans le CSS: 'primary', 'secondary', 'tertiary', 'danger', 'expand')
@param - params - string - optional - permet d'ajouter des parametres HTML au bouton
#nested : optionnal - permet d'ajouter des éléments après le label de l'item
 -->
<#macro cDropdownItem label action='' type='link' active=false disabled=false header=false class='' nestedPos='before' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li>
<#if type='link'>
<a class="dropdown-<#if !header>item<#else>header</#if><#if class!=''> ${class}</#if><#if active> active</#if><#if disabled> disabled</#if>"<#if disabled> aria-disabled="true"</#if><#if id!=''> id="${id!}"</#if><#if params!=''>${params!}</#if> href="${href!}">
<#if header><h4></#if><#if nestedPos='before'><#nested></#if><#if label!=''>${label!}</#if><#if nestedPos='after'><#nested></#if><#if header></h4></#if>
</a>
<#else>
<button class="dropdown-<#if !header>item<#else>header</#if><#if active> active</#if><#if disabled> disabled</#if>"<#if disabled> aria-disabled="true"</#if> type="${type}">
<#if header><h4></#if><#if nestedPos='before'><#nested></#if><#if label!=''>${label!}</#if><#if nestedPos='after'><#nested></#if><#if header></h4></#if>
</button>
</#if>
</li>
</#macro>
<#-- Macro: cDropdownItemDivider

Description: Affiche une séparation entre deux items.

Parameters:
 -->
<#macro cDropdownItemDivider>
<li><hr class="dropdown-divider"></li>
</#macro>
