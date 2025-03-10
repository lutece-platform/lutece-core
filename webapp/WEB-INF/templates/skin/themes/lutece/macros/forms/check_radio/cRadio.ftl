<#-- Macro: cRadio

Description: Defines a macro that show a radio button

Parameters:
@param - name - string - optional - required - the name of of the element
@param - label - string - optional - required - the label associated to the input
@param - labelClass - string - optional - the CSS class of the label, can be set to "visually-hidden" class to be hided default '' 
@param - type - string - optional - the type by default 'radio' can be 'button'
@param - helpMsg - string - optional - Content of the help message for radio, default ''
@param - errorMsg - string - optional - Content of the error message for radio, default ''
@param - value - string - optional - the value of the element, default ''
@param - selectionButton - boolean - optional - Add box to the radio, default false 
@param - selectionLabel - string - optional - Add label to the "selection" box default ''
@param - btnClass - string - optional - Only use if type is 'button' - default ''
@param - textCenter - boolean - optional - Center text on all select default false
@param - params - optional - additional HTML attributes to include in the radio element default ''
@param - disabled - boolean - optional - Disable element, default false
@param - readonly - boolean - optional - Set element readonly, default false
@param - checked - boolean - optional - Check the element, default false
@param - required - boolean - optional - Set element as required, default false
@param - html5Required - boolean - optional - permet d'indiquer si le champs doit utliser l'attribut html5 required (par défaut: true)
@param - showRequiredLabel - boolean - optional - indique si l'affichage de l'étoile pour "required" s'affiche sur le label du radio (false)  ou le label englobant les radio (true) (par défaut: true)
@param - class - string - optional - the CSS class of the element, default '' 
@param - id - string - optional - the ID of the element, default ''
@param - inline - boolean - optional - Set inline radio default false
@param - #nested - String - Any text to add un label
-->
<#macro cRadio name label type='radio' class='form-check' labelClass='' btnClass='' id='' value='' errorMsg='' helpMsg='' params='' inline=false selectionButton=false selectionLabel='' textCenter=false disabled=false readonly=false checked=false required=false html5Required=true showRequiredLabel=true deprecated...>
<@deprecatedWarning args=deprecated />
<#assign nestedContent><#nested></#assign>
<@cFormCheck name=name label=label type=type class=class labelClass=labelClass btnClass=btnClass id=id value=value errorMsg=errorMsg helpMsg=helpMsg params=params inline=inline selectionButton=selectionButton selectionLabel=selectionLabel textCenter=textCenter disabled=disabled readonly=readonly checked=checked required=required html5Required=html5Required showRequiredLabel=showRequiredLabel nestedContent=nestedContent />
</#macro>