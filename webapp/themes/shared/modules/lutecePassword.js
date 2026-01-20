/**
 * A class for generating a new password and returning its complexity value
 */
export class LutecePassword {
  /**
     * Creates a new instance of LutecePassword.
     * @param {NodeListOf<Element>} passwords - The list of password's input elements.
     */
    constructor() {
      this.lowercase = "abcdefghijklmnopqrstuvwxyz";
      this.uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      this.numbers = "0123456789";
      this.symbols = "!@#$%^&*()_+-=[]{}|;:,.<>?";
      this.passwordInput = '#password';
      this.progressBar = '#progress';
      this.passTogglerBtn = '#lutece-password-toggler';
      this.passTogglerIconOn = "ti-eye";
      this.passTogglerIconOff = "ti-eye-off";
      this.weakPasswords = ["123456", "123456789", "12345", "12345678", "1234", "111111", "1234567", "password"
      , "123123", "qwerty", "abc123", "password1", "000000", "iloveyou", "123321", "qwertyuiop", "monkey", "dragon"
      , "letmein", "football", "admin", "adminadmin", "welcome", "login", "princess", "solo", "passw0rd", "master"
      , "sunshine", "shadow", "superman", "azerty", "azertyuiop", "qwerty123", "1q2w3e4r", "654321", "pokemon"
      , "starwars", "batman", "trustno1", "hello", "freedom", "whatever", "baseball"];
    }
    /**
     * Generates a new password that meets the following criteria:
     * - Between 12 and 15 characters long.
     * - Contains at least one lowercase letter, one uppercase letter, one number, and one symbol.
     * - Uses the remaining characters from any of the character sets.
     * @returns {string} The new password.
     */
    generatePassword() {
      let password = "";
      let passwordLength = Math.floor(Math.random() * 4) + 12;
      // Use at least one character from each character set
      password += this.lowercase.charAt(Math.floor(Math.random() * this.lowercase.length));
      password += this.uppercase.charAt(Math.floor(Math.random() * this.uppercase.length));
      password += this.numbers.charAt(Math.floor(Math.random() * this.numbers.length));
      password += this.symbols.charAt(Math.floor(Math.random() * this.symbols.length));
      // Use remaining characters from any character set
      let remainingLength = passwordLength - 4;
      for (let i = 0; i < remainingLength; i++) {
        let characterSet = Math.floor(Math.random() * 4);
        switch (characterSet) {
          case 0:
            password += this.lowercase.charAt(Math.floor(Math.random() * this.lowercase.length));
            break;
          case 1:
            password += this.uppercase.charAt(Math.floor(Math.random() * this.uppercase.length));
            break;
          case 2:
            password += this.numbers.charAt(Math.floor(Math.random() * this.numbers.length));
            break;
          case 3:
            password += this.symbols.charAt(Math.floor(Math.random() * this.symbols.length));
            break;
        }
      }
      return password;
    }
    /**
     * Calculates the complexity of the given password and returns it as a percentage of the maximum score (100).
     * The complexity is based on the following criteria:
     * - The length of the password.
     * - Whether the password contains at least one lowercase letter, one uppercase letter, one number, and one symbol.
     * @param {string} password The password to check.
     * @returns {number} The complexity value of the password, as a percentage of the maximum score (100).
     */
    calcComplexity(password) {

      if (this.weakPasswords.includes(password))
      {
        return 5;
      }

      let hasLowercase = /[a-z]/.test(password);
      let hasUppercase = /[A-Z]/.test(password);
      let hasNumber = /\d/.test(password);
      let hasSymbol = /[^A-Za-z0-9]/.test(password);
      let lengthScore = Math.min(password.length / 20 * 100, 50);
      let complexityScore = 0;
      if (hasLowercase && hasUppercase && hasNumber && hasSymbol) {
        complexityScore = 50;
      } else if ((hasLowercase || hasUppercase) && hasNumber && hasSymbol) {
        complexityScore = 25;
      } else if (hasLowercase && hasUppercase && hasNumber) {
        complexityScore = 20;
      } else if ((hasLowercase || hasUppercase) && hasNumber) {
        complexityScore = 10;
      } else if (hasLowercase && hasUppercase) {
        complexityScore = 5;
      }
      return Math.round(lengthScore + complexityScore);
    }
    /**
     * Set the complexity bar
     * Show the complexity result in progress bar.
     */
    getComplexity( ) {
        const input = document.querySelector( this.passwordInput )
        const progressBar = document.querySelector( this.progressBar )
        const progressBarText = document.querySelector( this.progressBar + "-text" )
        const progressBarStatus =  document.querySelector( this.progressBar + "-status" )
        const label = progressBar.labels?.[0] || null;

        input.addEventListener('input', () => {
          const complexityScore = this.calcComplexity(input.value);
          progressBarText.textContent = Math.round(complexityScore) + "%";
          progressBarStatus.textContent = (label?.textContent ?? '') + ' ' + Math.round(complexityScore) + "%";
          progressBar.value = complexityScore;
          progressBar.classList.remove('lutece-progress-danger', 'lutece-progress-warning', 'lutece-progress-success');
          if( complexityScore < 50 ) {
            progressBar.classList.add('lutece-progress-danger')
          } else if ( complexityScore >= 50 && complexityScore < 100 ) {
            progressBar.classList.add('lutece-progress-warning')
          } else {
            progressBar.classList.add('lutece-progress-success')
          }
        });
    }
   
    /**
     * Initializes the password toggler.
     */
    initPassToggler( ) {
      const _passDefaultTogglerIcon = "ti-eye";
      const btnToggler = document.querySelector( this.passTogglerBtn )
      const inputToggler = document.querySelector( this.passwordInput )
      btnToggler.firstElementChild.classList.remove( _passDefaultTogglerIcon );
      btnToggler.firstElementChild.classList.add( this.passTogglerIconOn );
      btnToggler.addEventListener(
        "click", ( event) => {
          event.preventDefault();
          this.showHidePassword( btnToggler, inputToggler );
      });
    }
    
    /**
     * Show / Hide password.
     *  @param {string} btn The button id that old the event.
     *  @param {string} field The password input to toggle.
     */
    showHidePassword( btn, field ){
      if ( field.type == 'password') {
        field.setAttribute( 'type', 'text' );
        btn.firstElementChild.classList.add( this.passTogglerIconOff  );
        btn.firstElementChild.classList.remove( this.passTogglerIconOn  );
      } else {
        btn.firstElementChild.classList.remove( this.passTogglerIconOff  );
        btn.firstElementChild.classList.add( this.passTogglerIconOn  );
        field.setAttribute('type', 'password');
      }
    };
}