<#-- Macro: cImg

Description: affiche une image.

Parameters:
@param - id - string - optional - identifiant unique de l'image
@param - class - string - optional - classe(s) css de l'image
@param - src - string - required - source du fichier image
@param - alt - string - optional - texte alternatif de l'image
@param - labelDescribedBy - string - optional - aria label de l'image
@param - showLabelDescribedBy - boolean - required - si true, affiche le label saisi dans le paramètre "labelDescribedBy"
@param - params - string - optional - permet d'ajouter des paramètres HTML à l'image
-->
<#macro cImg src alt='' id='' class='img-fluid' labelDescribedBy='' showLabelDescribedBy=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local localId><#if id !=''>${id}<#else>${alt?js_string?lower_case?replace(' ','_')}</#if></#local> 
<img src="${src!}" alt="${alt!}" class="<#if class!=''>${class!}</#if>"<#if labelDescribedBy !=''> aria-descridedby="descridedby_${localId!}"</#if><#if localId!=''> id="${localId!}"</#if><#if params!=''> ${params!}</#if>>
<#if labelDescribedBy !=''><p id="descridedby_${localId!}"<#if !showLabelDescribedBy> class="visually-hidden"</#if>>${labelDescribedBy!}</p></#if>
</#macro>