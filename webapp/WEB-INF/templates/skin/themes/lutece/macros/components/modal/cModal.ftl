<#-- Macro: cModal

Description: affiche une boite de dialogue.

Parameters:

@param - id - string - required - l'ID de la modal
@param - title - string - required - permet de définir le titre du header de la modal
@param - class - string - optional - permet d'ajouter une classe CSS à la modal
@param - size - string - optional - permet d'ajouter une classe CSS pour gérer la taille de la modal (pas de valeur pré-définie)
@param - static - boolean - optional - permet d'ajouter un attribut HTML 'data-backdrop' (par défaut: false)
@param - pos - string - optional - permet de gérer la position de la modal (par défaut et seule valeur existante: 'centered')
@param - role - string - optional - permet de définir le role de la modal avec un attribut HTML
@param - scrollable - boolean - optional - permet d'ajouter une classe CSS 'modal-dialog-scrollable' (par défaut: false)
@param - dismissible - boolean - optional - permet d'activer la fermeture de la modal avec l'affichage de la croix et le bouton de fermeture (par défaut: true)
@param - dismissLabel - string - optional - permet de définir le label du bouton de fermerture de la modal (par défaut: '#i18n{portal.theme.labelClose}')
@param - levelTitle - number - optional - permet de définir le niveau de titre de la modale
@param - footer - string - optional - permet de définir le HTML du footer de la modal (pour y ajouter des boutons de submit par exemple)
@param - params - string - optional - permet d'ajouter des parametres HTML à la modal
 -->
<#macro cModal title id size='lg' static=false pos='centered' role='' scrollable=false dismissible=true dismissLabel='#i18n{portal.theme.labelClose}' footer='' titleLevel=1 class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="modal cmodal fade<#if class!=''> ${class}</#if>" id="${id}Modal"<#if static> data-backdrop="static"</#if> role="dialog" aria-labelledby="modal${id}Title" aria-hidden="true" <#if params!=''> ${params}</#if>>
    <div class="modal-dialog <#if pos!=''>modal-dialog-${pos} </#if> <#if scrollable>modal-dialog-scrollable</#if><#if size!=''>modal-${size}</#if>"<#if role !=''> role="${role}"</#if>>
        <div class="modal-content">
            <div class="modal-header">
                <@cTitle level=titleLevel class="modal-title h5 main-color" id="modal${id}Title">${title}</@cTitle>
                <#if dismissible>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="#i18n{portal.theme.labelClose}">
                    </button> 
                </#if>
            </div>
            <div class="modal-body">
                <#nested>
            </div>
            <div class="modal-footer">
                <#if dismissible>
	                <@cBtn label='${dismissLabel}' class='tertiary m-1' params='data-bs-dismiss="modal"'/>
                </#if>
                ${footer!}
            </div>
        </div>
    </div>
</div>
</#macro>