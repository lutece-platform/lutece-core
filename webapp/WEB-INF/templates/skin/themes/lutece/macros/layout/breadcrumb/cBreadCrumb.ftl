<#-- Macro: cBreadCrumb

Description: affiche un fil d'ariane.

Parameters:
@param - home - string - required - Titre de la page Home. Par défaut clé Fil d'ariane disponible dans le fichier "theme_messages_fr.properties"
@param - items - list - required - Objet JSON list avec liste d'item de page. L'objet contient un attribut 'titre' et un attribut 'url'.
@param - class - string - optional - classe(s) css de la bannière
@param - type - string - optional - Si fluid ajoute une classe pour un affichage en largeur 100%    
@param - params - string - optional - permet d'ajouter des paramètres HTML au fil d'ariane
-->
<#macro cBreadCrumb home='#i18n{theme.home}' items='' class='' type='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<nav aria-label="#i18n{theme.breadcrumb}" class="breadcrumb-nav<#if class!=''> ${class!}</#if>"<#if params!=''> ${params!}</#if>>
	<div class="container<#if type='fluid'>-fluid</#if>">
		<ol class="breadcrumb">
            <#if home!=''>
                <li class="breadcrumb-item">
                    <a target="_top" href=".">${home!}</a>
                </li>
            </#if>
            <#if items?has_content>
                <#assign iMax=items?size>
                <#assign idx=1>
                <#list items as i>
                    <li class="breadcrumb-item <#if iMax==idx>active</#if>"<#if iMax==idx> aria-current="page"</#if>>
                        <#if idx lt iMax>
                        <a href="${i.url!}" title="${i.title!}" target="_top">
                        </#if>
                        ${i.title!}
                        <#if idx lt iMax>
                        </a>
                        </#if>
                    </li>
                    <#assign idx+=1>
                </#list>
            </#if>
           <#nested> 
        </ol>
	</div>
</nav>
</#macro>