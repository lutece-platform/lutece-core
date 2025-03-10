<#-- THEME DATE PICKER COMPONENT                                  -->
<#-- getThemeDatePicker                                           -->
<#-- ------------------------------------------------------------ -->
<#-- @tag     | date, datepicker, flatpikr                        -->
<#-- ------------------------------------------------------------ -->
<#-- @summary | Add datepicker with flatpickr lib                 -->
<#-- ------------------------------------------------------------ -->
<#-- @param idField       | mandatory |                           -->
<#-- @param showFormat    | ''        |                           -->
<#-- @param minDate       | ''        |                           -->
<#-- @param maxDate       | ''        |                           -->
<#-- @param defaultDate   | ''        |                           -->
<#-- @param mode          | 'single'  |                           -->
<#-- @param time          | false     |                           --> 
<#-- @param range         | false     |                           --> 
<#-- @param rangeIdField  | ''        |                           -->
<#-- @param customArrows  | ['<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-chevron-left" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"></path><polyline points="15 6 9 12 15 18"></polyline></svg>','<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-chevron-right" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"></path><polyline points="9 6 15 12 9 18"></polyline></svg>'] |  -->
<#-- ------------------------------------------------------------ -->
<#-- @nested              | true      | Shows default page menu, but can other item can be add using @mainNavItem macro. -->
<#-- ------------------------------------------------------------ -->
<#macro getThemeDatePicker idField format='' showFormat='' minDate='' maxDate='' defaultDate='' title='' time=false range=false rangeIdWrapper='dtRange' customArrows=['<svg width="28" height="28" viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg"><g clip-path="url(#clip0_6399_4241)"><path d="M12.4849 14.5271L17.8516 19.8938L15.6907 22.0547L8.16309 14.5271L15.6907 6.99955L17.8516 9.16045L12.4849 14.5271Z" fill="#071F32"/></g><defs><clipPath id="clip0_6399_4241"><rect width="28" height="28" fill="white"/></clipPath></defs></svg>','<svg width="28" height="28" viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg"><g clip-path="url(#clip0_6399_3183)"><path d="M15.5151 13.4729L10.1484 8.10621L12.3093 5.94531L19.8369 13.4729L12.3093 21.0004L10.1484 18.8395L15.5151 13.4729Z" fill="#071F32"/></g><defs><clipPath id="clip0_6399_3183"><rect width="28" height="28" fill="white" transform="translate(28 28) rotate(180)"/></clipPath></defs></svg>'] options={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local dtThemeOptions>${dskey('theme.site_property.config.datepicker.textblock')!}</#local>
<script>
document.addEventListener('DOMContentLoaded', (e) => {
  const customOptions = {<#if options?size gt 0><#list options as opt, val>${opt} : <#if val?is_boolean || val?is_number>${val?c}<#elseif val?is_string>'${val}'</#if>,</#list></#if> };
  const themeOptions = {<#if !dtThemeOptions?starts_with('DS Value')>${dtThemeOptions}</#if>};
  let showFormat=<#if showFormat == ''>getDatePickerDateFormat( navigator.language )<#else>'${showFormat}'</#if>;
  <#-- The server parses dates on a per locale basis. The formats here much match those specified by lutece.format.date.short -->
  let dataFormat=<#if format == ''>'yyyy-m-d 00:00:00' // Default for most DB<#else>'${format}'</#if>;
  const defaultOptions = {
      autohide: true,
      buttonClass: 'btn',
      dataFormat: dataFormat,
      dateDelimiter: '|', // Delimiter for mutlidate
      daysOfWeekDisabled: [],
      defaultViewDate: 'today',
      clearButton: true, 
      enableReadOnly: true,
      format: showFormat,
      language: <#noparse>`${navigator.language.split('-')[0]}`</#noparse>,
      minDate: null,     
      maxDate: null,     
      maxNumberOfDate: 1,
      nextArrow: '${customArrows[1]}',
      orientation: 'auto',
      prevArrow: '${customArrows[0]}',
      picklevel: 0, // 0 - Days 1 - Month, 2 - Year, 3 - Decade
      showDaysOfWeek: true,
      showOnClick: true,
      showOnFocus: true,
      startView: 0, // 0 - Days 1 - Month, 2 - Year, 3 - Decade
      title: '',
      todayButton: false,
      todayButtonMode: 0, // 0 - Focus , 1 - Select
      todayHighlight: true,
      updateOnBlur: true,
      weekNumbers: 0, // 0 – None, 1 – ISO 8601, 2 – Western traditional, 3 – Middle Eastern, 4 – Guess from weekStart
      weekStart: 1,
  }
<#if !range>
  const elem${idField} = document.querySelector('#${idField}')
  const dtOptions${idField} = setDatePickerOptions( themeOptions, customOptions, defaultOptions )
  let datepicker${idField} = new themeDatepicker( elem${idField}, dtOptions${idField} );
<#else>
  const dtRangeDefaultOptions = {
    title: '',
    format: showFormat,
    dateDelimiter: '|', // Delimiter for mutlidate
    language: <#noparse>`${navigator.language.split('-')[0]}`</#noparse>,
    dataFormat: dataFormat,
    daysOfWeekDisabled: [],
    buttonClass: 'btn',
    nextArrow: '${customArrows[1]}',
    prevArrow: '${customArrows[0]}',
    defaultViewDate: 'today',
    todayButton: false,
    clearButton: true, 
    picklevel: 0, // 0 - Days 1 - Month, 2 - Year, 3 - Decade
    startView: 0, // 0 - Days 1 - Month, 2 - Year, 3 - Decade
    todayHighlight: true,
    minDate: null,     
    maxDate: null,   
    weekNumbers: 0, // 0 – None, 1 – ISO 8601, 2 – Western traditional, 3 – Middle Eastern, 4 – Guess from weekStart
    weekStart: 1,
  }

  const dtRangeOptions${rangeIdWrapper} = setDatePickerOptions( themeOptions, customOptions, dtRangeDefaultOptions );
  const elDtPickerWrapper${rangeIdWrapper} = document.getElementById( '${rangeIdWrapper}' );
  const rangepicker${rangeIdWrapper} = new DateRangePicker( elDtPickerWrapper${rangeIdWrapper}, dtRangeOptions${rangeIdWrapper}); 
  
  rangepicker${rangeIdWrapper}.inputs.forEach((input) => {
    let isoInput = null;
    input.addEventListener('changeDate', (ev) => {
      const {date} = ev.detail;
      <#noparse>const dtIso = `${date.toISOString()}`;</#noparse>
      if( isoInput === null ){
        isoInput = input.cloneNode(true);
        input.removeAttribute('name');
        isoInput.type = 'hidden';
        isoInput.id = input.id + '_hidden';
        elDtPickerWrapper${rangeIdWrapper}.append( isoInput ); 
      }
      isoInput.value = dtIso ;
    });
  });
</#if>
});
</script>
</#macro>