<#--
Macro: cInputDate

Description: Generates a date input field with optional Vanilla JS Datepicker integration, calendar icon, and HTML5 date fallback.

Parameters:
- name (string, required): the name attribute of the input.
- id (string, optional): the ID of the input. Default: ''.
- class (string, optional): CSS class for the input. Default: ''.
- type (string, optional): the input type, 'datepicker' for JS datepicker or 'date' for HTML5 date. Default: 'datepicker'.
- icon (boolean, optional): displays a calendar icon to the right of the input. Default: true.
- options (object, optional): Vanilla JS Datepicker options (see https://mymth.github.io/vanillajs-datepicker/#/). Default: {}.
- value (string, optional): the default value. Default: ''.
- placeholder (string, optional): the placeholder text. Default: ''.
- autocomplete (string, optional): the autocomplete attribute value. Default: ''.
- html5Required (boolean, optional): whether to add the HTML5 required attribute. Default: true.
- required (boolean, optional): marks the field as required. Default: false.
- disabled (boolean, optional): disables the input. Default: false.
- readonly (boolean, optional): sets the input as readonly. Default: false.
- helpMsg (string, optional): help message displayed below the input. Default: ''.
- errorMsg (string, optional): error message displayed on validation failure. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.

Showcase:
- desc: "Champ date - @cInputDate"
- bs: "forms/form-control"
- newFeature: false

Snippet:

    Basic datepicker:

    <@cInputDate name='birthdate' label='Date of birth' id='birthdate' />

    Datepicker with default value and no icon:

    <@cInputDate name='event_date' label='Event date' id='event_date' value='2026-01-15' icon=false />

    HTML5 date input:

    <@cInputDate name='start_date' label='Start date' id='start_date' type='date' />

    Datepicker in a form row:

    <@cFormRow>
        <@cCol cols='4'>
            <@cInputDate id='date_start' label='From' name='date_start' />
        </@cCol>
        <@cCol cols='4'>
            <@cInputDate id='date_end' label='To' name='date_end' />
        </@cCol>
    </@cFormRow>

-->
<#macro cInputDate name id='' class='' type='datepicker' icon=true options={} value='' placeholder='' autocomplete='' html5Required=true required=false disabled=false readonly=false helpMsg='' errorMsg='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local idLocal><#if id!=''>${id}<#else>${name!}</#if></#local>
<#local typeLocal><#if type='date'>date<#else>text</#if></#local>
<#local valLocal><#if value !=''>${value}<#elseif value='now'>.now?date?iso_utc</#if></#local>
<#local errorInput><#if errorMsg !=''>_error</#if></#local>
<#local inputClass><#if errorMsg !=''>is-invalid</#if></#local>
<#if helpMsg !=''><@cFormHelp idLocal helpMsg /></#if>
<#if errorMsg !='' && errorMsg !='_error'><@cFormError idLocal errorMsg /></#if>
<@cInputGroup>
  <@cInput id=idLocal type=typeLocal name=name value=valLocal placeholder=placeholder autocomplete=autocomplete required=required html5Required=html5Required disabled=disabled readonly=readonly errorMsg=errorInput params=params />
  <#if icon && type='datepicker'>
  <@cInputGroupAddonText>
      <@cIcon name='agenda' id=idLocal />
  </@cInputGroupAddonText>
  </#if>
  <#nested>
</@cInputGroup>
<#if type='datepicker'><@getThemeDatePicker idField=idLocal options=options /></#if>
</#macro>
<#--
Macro: cInputDateRange

Description: Generates a date range picker with two date inputs (start and end) inside an input group, with optional datepicker integration.

Parameters:
- name (string, required): the name attribute prefix for the date inputs.
- label (array, required): the labels associated to the start and end date inputs. Default: ['#i8n{theme.labelDateStart}','#i8n{theme.labelDateEnd}'].
- showLabel (array, optional): flags to show or hide labels for start and end date inputs. Default: [false,false].
- id (string, optional): the ID of the date range container. Default: 'dtRange'.
- class (string, optional): CSS class for the container. Default: ''.
- type (string, optional): the input type, 'datepicker' for JS datepicker or 'date' for HTML5 date. Default: 'datepicker'.
- icon (boolean, optional): displays a calendar icon. Default: true.
- options (object, optional): Vanilla JS Datepicker options. Default: {}.
- value (string, optional): the default value. Default: ''.
- placeholder (array, optional): placeholder texts for start and end inputs. Default: ['',''].
- required (array, optional): required flags for start and end inputs. Default: [false,false].
- disabled (array, optional): disabled flags for start and end inputs. Default: [false,false].
- readonly (array, optional): readonly flags for start and end inputs. Default: [false,false].
- helpMsg (string, optional): help message displayed below the inputs. Default: ''.
- errorMsg (string, optional): error message displayed on validation failure. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.

Snippet:

    Basic date range picker:

    <@cInputDateRange name='period' label='Select a period' />

    Date range with placeholders and required start date:

    <@cInputDateRange name='stay' label='Stay dates' placeholder=['Check-in','Check-out'] required=[true,true] />

-->  
<#macro cInputDateRange name label=['#i8n{theme.labelDateStart}','#i8n{theme.labelDateEnd}'] showLabel=[false,false] id='dtRange' class='' type='datepicker' icon=true options={} value='' placeholder=['',''] required=[false,false] disabled=[false,false] readonly=[false,false] helpMsg='' errorMsg='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local idLocal><#if id!=''>${id}<#else>${name!}</#if></#local>
<#local typeLocal><#if type='date'>date<#else>text</#if></#local>
<#local valLocal><#if value !=''>${value}<#elseif value='now'>.now?date?iso_utc</#if></#local>
<@cBlock class='daterange ${class!}' id='${idLocal}' params=params >
  <#if showLabel[0]><@cLabel label=label[0] for='${idLocal}_range_start' /><#else><@cLabel label=label[0] for='${idLocal}_range_start' class='visually-hidden' /></#if>
  <#if showLabel[1]><@cLabel label=label[1] for='${idLocal}_range_end' class='ms-xl ps-xxs'/><#else><@cLabel label=label[1] for='${idLocal}_range_end' class='visually-hidden' /></#if>
  <#if helpMsg !=''><@cFormHelp idLocal helpMsg /></#if>
  <#if errorMsg !=''><@cFormError idMsg errorMsg /></#if>
  <@cInputGroup>
    <@cInput id='${idLocal}_range_start' type=typeLocal name=name value=valLocal placeholder=placeholder[0] required=required[0] disabled=disabled[0] readonly=readonly[0]  />
    <@cInput id='${idLocal}_range_end'type=typeLocal name='${name}_range_end' placeholder=placeholder[1] required=required[1] disabled=disabled[1] readonly=readonly[1] />
    <#if icon>
    <@cInputGroupAddon>
        <@cInputGroupAddonText tag='div'>
            <@cIcon name='agenda' id=idLocal />
        </@cInputGroupAddonText>   
    </@cInputGroupAddon> 
    </#if>
    <#nested>
  </@cInputGroup>
</@cBlock>
<#local optionsLocal><#if options?size = 0>{inputs:["${idLocal}_range_start","${idLocal}_range_start"]}</#if></#local>
<#if type='datepicker'><@getThemeDatePicker idField='' range=true rangeIdWrapper='${idLocal}' options=options /></#if>
</#macro>