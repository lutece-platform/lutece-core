<#-- Macro fcMessages 

Description: Message franceconnect avec image

Parameters:
@param - label - string - optionnal - Texte par défaut 'Créer mon compte avec' 
@param - url - string - optional - Url pour le lien
@param - class - string - optional - Classe CSS pour le lien
@param - pclass - string - optional - Classe CSS pour le paragraphe
@param - brlink - boolean - optional - Ajoute un saut de ligne après le lien
-->
<#macro fcMessage label='#i18n{portal.theme.labelFranceConnect}' url='https://franceconnect.gouv.fr/' class='' pclass='' brlink=true deprecated...>
<@cText class=pclass>
<#if brlink><span class="d-block"></#if>
<#nested>
<#if brlink></span></#if>
<@cLink target='_blank' href=url label=label class=class showTarget=true params='rel="noopener"' />
</@cText>
</#macro>