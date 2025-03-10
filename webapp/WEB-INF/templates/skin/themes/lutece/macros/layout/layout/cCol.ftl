<#-- Macro: cCol

Description: affiche une colonne.

Parameters:
@param - id - string - required - identifiant unique de la colonne
@param - class - string - optional - classe(s) css de la colonne
@param - cols - string - optional -  Si non vide ajoute la classe "col", que vous pouvez compléter avec l'attribut "class" ci-dessous. Si vous ajoutez une valeur, attention le préfix de classe 'col-' est présent dans la macro. Vous pouvez ajouter les "sm-XX", "md-XX", "lg-XX", "xl-XX" ou "XX" est toute valeur permise par Bootstrap. On peut ajouter d'autres paramètres BS séparés par un espace.
@param - default - string - optional - Si col est vide, ajoute le préfix valeur de "default", par défaut -col- qui peut être complété avec l'attribut "class" ci-dessous.
@param - params - string - optional - permet d'ajouter des paramètres HTML à la colonne
-->
<#macro cCol cols='' default='col' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if cols!=''>
    <#local cClass>col-${cols} ${class}</#local> 
<#else>
    <#local cClass>${default!} ${class}</#local>
</#if>
<@cSection type='div' class=cClass id=id params=params >
<#nested>
</@cSection>
</#macro>