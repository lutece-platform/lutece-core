<#-- Macro: cInputDate                                
Parameters:
@param - name - string - required - Nom du champ
@param - label - string - required - Label asssocié au champs                   
@param - id - string - Default '', Id de l'input                           
@param - class - string -  Default 'custom-checkbox', classe css à ajouter à l'input
@param - type - string - Default '', type de l'input par default 'datepicker', sinon cela peut être un champ de type date HTML5.
@param - icon - boolean - Default true, affiche l'icone "agenda" à droite de l'input
@param - options - object - Default {} Voir les paramètres possibles disponible pour Vanilla JS Datepicker -https://mymth.github.io/vanillajs-datepicker/#/ - 
@param - value : - string - Default '', Valeur par défaut de l'input         
@param - placeholder- string - Default '' , placeholder de l'input                        
@param - autocomplete- string - Default '' , autocomplete pour l'input https://developer.mozilla.org/fr/docs/Web/HTML/Attributes/autocomplete       
@param - required - boolean - Default false, champ obligatoire ou non                 
@param - disabled - boolean - Default false, champ désactivé ou non
@param - readonly - boolean - Default false, champ en lecture seule ou non                
@param - helpMsg - string - Default '', Message d'aide pour l'input                                   
@param - errorMsg - string - Default '', Message d'erreur pour l'input                      
@param - params - string - Default '', Tous autres paramètres à ajouter à l'input                   
@param - #nested - string - Default '', Contenu textuel a ajouter après l'input

@sample : <@cFormRow>
    <@cCol cols='3'>
        <@cLabel label='Date picker' for='datepicker' />
        <@cInputDate id='datepicker_1' label='' name='date1' value=.now?date?iso_utc />
    </@cCol>
    <@cCol cols='4'>
        <@cLabel label='Date picker sans icone' for='datepicker' />
        <@cInputDate id='datepicker_2' label='' name='date2' icon=false />
    </@cCol>
    <@cCol cols='3'>
        <@cLabel label='Date picker HTML' for='datepicker' />
        <@cInputDate id='datepicker_3' label='' name='date3' type='date' />
    </@cCol>
</@cFormRow>
 -->
<#macro cInputDate name label id='' class='' type='datepicker' icon=true options={} value='' placeholder='' autocomplete='' required=false disabled=false readonly=false helpMsg='' errorMsg='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local idLocal><#if id!=''>${id}<#else>${name!}</#if></#local>
<#local typeLocal><#if type='date'>date<#else>text</#if></#local>
<#local valLocal><#if value !=''>${value}<#elseif value='now'>.now?date?iso_utc</#if></#local>
<#local errorInput><#if errorMsg !=''>_error</#if></#local>
<@cLabel label=label for='${idLocal}' required=required />
<#if helpMsg !=''><@cFormHelp idLocal helpMsg /></#if>
<@cInputGroup>
  <@cInput id=idLocal type=typeLocal name=name value=valLocal placeholder=placeholder autocomplete=autocomplete required=required disabled=disabled readonly=readonly errorMsg=errorInput params=params />
  <#if icon && type='datepicker'>
  <@cInputGroupAddon>
      <@parisIcon 'agenda' idLocal />
  </@cInputGroupAddon>
  </#if>
  <#nested>
  <#if errorMsg !=''><@cFormError idMsg errorMsg /></#if>
</@cInputGroup>
<#if type='datepicker'><@getThemeDatePicker idField=idLocal options=options /></#if>
</#macro>
<#-- Macro: cInputDateRange                                
Parameters:
@param name  : required                                
@param label : required                                
@param id : default 'dtRange'                           
@param class : default ''                
@param type  : default 'datepicker' : datepicker / date 
@param icon : boolean default true                      
@param options : object default {}                      
@param value : default ''                               
@param placeholder : Array default ['','']              
@param required : Array default [false,false]           
@param disabled : Array default [false,false]           
@param readonly : Array default [false,false]           
@param helpMsg : default ''                             
@param errorMsg : default ''                            
@param params : default ''                              
@param #nested                                          
-->  
<#macro cInputDateRange name label id='dtRange' class='' type='datepicker' icon=true options={} value='' placeholder=['',''] required=[false,false] disabled=[false,false] readonly=[false,false] helpMsg='' errorMsg='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local idLocal><#if id!=''>${id}<#else>${name!}</#if></#local>
<#local typeLocal><#if type='date'>date<#else>text</#if></#local>
<#local valLocal><#if value !=''>${value}<#elseif value='now'>.now?date?iso_utc</#if></#local>
<@cBlock class='daterange ${class!}' id='${idLocal}' params=params >
  <@cLabel label=label for='${idLocal}_range_start' />
  <#if helpMsg !=''><@cFormHelp idLocal helpMsg /></#if>
  <@cInputGroup>
    <@cInput id='${idLocal}_range_start' type=typeLocal name=name value=valLocal placeholder=placeholder[0] required=required[0] disabled=disabled[0] readonly=readonly[0]  />
    <@cLabel label=label for='${idLocal}_range_end' class='visually-hidden' />
    <@cInput id='${idLocal}_range_end'type=typeLocal name='${name}_range_end' placeholder=placeholder[1] required=required[1] disabled=disabled[1] readonly=readonly[1] />
    <#if icon>
    <@cInputGroupAddon>
        <@cInputGroupAddonText tag='div'>
            <@parisIcon 'agenda' '${idLocal}' />
        </@cInputGroupAddonText>   
    </@cInputGroupAddon> 
    </#if>
    <#nested>
    <#if errorMsg !=''><@cFormError idMsg errorMsg /></#if>
  </@cInputGroup>
</@cBlock>
<#local optionsLocal><#if options?size = 0>{inputs:["${idLocal}_range_start","${idLocal}_range_start"]}</#if></#local>
<#if type='datepicker'><@getThemeDatePicker idField='' range=true rangeIdWrapper='${idLocal}' options=options /></#if>
</#macro>