/**
 * A class for generating a new password and returning its complexity value
 */
export class LutecePassword {
    constructor() {
      this.lowercase = "abcdefghijklmnopqrstuvwxyz";
      this.uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      this.numbers = "0123456789";
      this.symbols = "!@#$%^&*()_+-=[]{}|;:,.<>?";
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
    getComplexity(password) {
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
  }