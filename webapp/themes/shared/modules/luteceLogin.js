/**
 * A class for generating a new password and returning its complexity value
 */
export class LuteceLogin {
  /**
     * Creates a new instance of LuteceLogin.
     * 
     */
    constructor() {
      this.randomBgImages = "['themes/admin/shared/images/bg_login1.svg','themes/admin/shared/images/bg_login2.svg','themes/admin/shared/images/bg_login3.svg','themes/admin/shared/images/bg_login4.svg']";
      this.randomImages = "['themes/admin/shared/images/login-illustration-1.svg','themes/admin/shared/images/login-illustration-2.svg']";
      this.bgElement = "#login-page";
      this.element = "#illustration";
    }
    /**
    * Initializes the background images.
    */
    init( ){
      let elem = document.querySelector( this.element );
      let bg = document.querySelector( this.bgElement );
      let rndNum = Math.floor( Math.random() * ( this.randomImages.length ) );
      let rndBgNum = Math.floor( Math.random() * ( this.randomBgImages.length ) );
      let img = 'url(' + this.randomImages[rndNum] +	')';
      let bgImg = 'url(' + this.randomBgImages[rndBgNum] +	')';
      if( bg != null ){
        bg.style.backgroundImage = bgImg;
      }
      if( elem != null ){
        elem.style.backgroundImage = img;
      }
    }
}