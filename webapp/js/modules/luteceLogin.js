/**
 * A class for generating a new password and returning its complexity value
 */
export class LuteceLogin {
    /**
       * Creates a new instance of LutecePassword.
       * 
       */
      /**
       * Generates a background image
       */
      setRandomBackground( randomImages, elem ) {
        const rndNum = Math.floor( Math.random() * ( randomImages.length ) );
        const bgImg = 'url(' + randomImages[rndNum] +	')';
        elem.style.backgroundImage = bgImg;
      };
  }