/**
 * A class for generating a new password and returning its complexity value
 */
export class LuteceLogin {
  /**
     * Creates a new instance of LuteceLogin.
     * 
     */
    constructor() {
      this.randomImages = "['themes/admin/shared/images/bg_login1.svg','themes/admin/shared/images/bg_login2.svg','themes/admin/shared/images/bg_login3.svg','themes/admin/shared/images/bg_login4.svg']";
      this.element = "#login-page";
    }
    /**
    * Initializes the background images.
    */
    init( ){
      let elem = document.querySelector( this.element );
      let rndNum = Math.floor( Math.random() * ( this.randomImages.length ) );
      let bgImg = 'url(' + this.randomImages[rndNum] +	')';
      elem.style.backgroundImage = bgImg;
    }
}