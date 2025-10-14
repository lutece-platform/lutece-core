// Function to set date picker options
function setDatePickerOptions( themeOption, customOptions, defaultOptions ) {
  let options = {};
  for ( let key in defaultOptions ) {
    options[key] = defaultOptions[key];
    if ( themeOption.hasOwnProperty(key) ) {
        options[key] = themeOption[key];
    } 
    if ( customOptions.hasOwnProperty(key) ) {
        options[key] = customOptions[key];
    }
  }
  return options;
}

// Extend DatePicker Format management
class themeParisFrDatepicker extends Datepicker {
    constructor(element, options = {}) {
      const originalType = element.type;
      const dateInput = element.cloneNode(true);
      dateInput.removeAttribute('name');
      if (element.value) {
        dateInput.value = Datepicker.formatDate(
          new Date(`${element.value} 00:00:00`), // not to be parsed as UTC date
          options.format || 'dd/mm/yyyy'
        );
      }
      element.type = 'hidden';
      element.id = element.id + '_hidden';
      element.after(dateInput);
      super( dateInput, options );
      this.originalElement = element;
      this.originalElementType = originalType;
      if (element.value) element.value = this.getDate( options.dataFormat ) || '';
      dateInput.addEventListener( 'changeDate', () => {
        element.value = this.getDate( options.dataFormat ) || '';
        element.dispatchEvent( new Event( 'change' ) );
      });
    }
}
