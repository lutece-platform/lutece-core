/*
 * jQuery Popeye 0.2.1 - http://dev.herr-schuessler.de/jquery/popeye/
 *
 * converts HTML image list in image gallery with inline enlargement
 *
 * Copyright (C) 2008 Christoph Schuessler (schreib@herr-schuessler.de)
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 */
(function ($) {

    
    ////////////////////////////////////////////////////////////////////////////
    //
    // $.fn.popeye
    // popeye definition
    //
    ////////////////////////////////////////////////////////////////////////////
    $.fn.popeye = function (options) {
    
        // set context vars
        //----------------------------------------------------------------------
        var obj = $(this);
        var enPlaceholder = $('<div />');
                   
        
        // build main options before element iteration
        //----------------------------------------------------------------------
        var opts = $.extend({}, $.fn.popeye.defaults, options);
        
        
        // firebug console output
        //----------------------------------------------------------------------
        function debug(text) {
            if (window.console && window.console.log) {
                window.console.log(text);
            }
        };
             

        ////////////////////////////////////////////////////////////////////////
        //
        // -> init
        // apply popeye to all calling instances
        //
        ////////////////////////////////////////////////////////////////////////
        return this.each(function(){
            
            
            ////////////////////////////////////////////////////////////////////////
            //
            // $.fn.popeye.display
            // display thumbnail on stage, update toolbar
            //
            ////////////////////////////////////////////////////////////////////////
            function display(i, transition) {
                                
                // optional parameter transition
                transition = transition || false;
                
                
                // set selected image as background image of stage
                //------------------------------------------------------------------
                var stageIm = {
                    backgroundImage:    'url(' + im.small[i] + ')',
                    backgroundPosition: 'center'
                };
                //if set, show transition on change of image
                if(transition) {
                    ppyStage.fadeTo(100,0,function(){
                        $(this).css(stageIm).fadeTo(100,1);
                    });
                }
                else {
                    ppyStage.css(stageIm);
                }
                
                
                // event handler for click on image
                //------------------------------------------------------------------
                ppyStage.click(function() {
                    ppyStage.unbind();
                    enlarge(cur);
                });
                
                // update image info area
                //------------------------------------------------------------------
                ppyCap.text(im.title[i]);        // caption
                ppyTotal.text(' ' + tot);        // total images
                ppyCur.text((cur + 1) + ' ');    // current image number
            };
            
            
            ////////////////////////////////////////////////////////////////////////
            //
            // $.fn.popeye.enlarge
            // enlarge popeye
            //
            ////////////////////////////////////////////////////////////////////////
            function enlarge(i) {
    
                // get popeye dims and position
                //------------------------------------------------------------------
                var imHeight = obj.outerHeight();
                var imWidth = obj.outerWidth();
                
                var imTop = obj.offset().top - parseInt(obj.css('marginTop'),10);
                var imLeft = obj.offset().left - parseInt(obj.css('marginLeft'),10);     
                var imRight = $(window).width() - (obj.offset().left + imWidth) - parseInt(obj.css('marginRight'),10);
                
                var imFloat = obj.css('float');
                
                var imMarginTop = obj.css('margin-top');
                var imMarginRight = obj.css('margin-right');
                var imMarginBottom = obj.css('margin-bottom');
                var imMarginLeft = obj.css('margin-left');
    
    
                // set css for enlarged box
                //------------------------------------------------------------------
                var cssEnlargedPpy = {
                    position:       'absolute',
                    top:            imTop,
                    zIndex:         '100'
                };
                // css for right or left orientation
                if (opts.direction == 'left') {
                    cssEnlargedPpy.left =     imLeft;
                }
                else if (opts.direction == 'right') {
                    cssEnlargedPpy.right =     imRight;
                }
                var cssPlaceholder = {
                    height:         imHeight,
                    width:          imWidth,
                    float:          imFloat,
                    marginTop:      imMarginTop,
                    marginRight:    imMarginRight,
                    marginBottom:   imMarginBottom,
                    marginLeft:     imMarginLeft
                };
                
                
                // set dom for enlarged box
                //------------------------------------------------------------------
                
                // create placeholder and place it behind popeye
                // to wrap site around it once the popeye' position is absolute
                // this prevents the content that floats around the box 
                // from moving once the box leaves the float context
                
                enPlaceholder.css(cssPlaceholder);
                obj.after(enPlaceholder);
            
                // move popeye to top of body
                // and render it absolute
                obj.prependTo($('body'));
                obj.css(cssEnlargedPpy);
                obj.addClass(opts.eclass);
                
                // hide toolbar
                ppyToolsWrap.hide();
                
                // fade out image so that background shines through
                //background can contain loading grafic
                ppyStageWrap.addClass(opts.lclass);
                ppyStage.fadeTo((opts.duration/2), 0);
                
                
                // preload image and display it with transition
                //------------------------------------------------------------------
               
                // preloading
                var preloader = new Image();
                preloader.src = im.large[i];
                
                // once image has loadded...
                preloader.onload = function() {
                    
                    // get image dimensions
                    var imWidth = preloader.width;
                    var imHeight = preloader.height;
                    
                    // set css
                    var cssStageIm = {
                        backgroundImage:    'url(' + im.large[i] + ')',
                        backgroundPosition: 'left top'
                    };
                    var cssStageTo = {
                        width:              imWidth,
                        height:             imHeight
                    };
    
                    // show transitional animation
                    ppyStage.animate( cssStageTo, {
                        queue:      false,
                        duration:   opts.duration,
                        easing:     opts.easing,
                        complete:   function(){
                        
                            // add close event to box
                            ppyStage.click(function() {
                                ppyStage.unbind();
                                ppyStage.removeAttr('title');
                                compact(cur);
                            });
                            ppyStage.attr('title', opts.clabel);
                            
                            // set image and it fade in
                            $(this).css(cssStageIm).fadeTo((opts.duration/2),1);
                        }
                    });
                };
            };
            
            
            ////////////////////////////////////////////////////////////////////////
            //
            // $.fn.popeye.compact
            // compact popeye
            //
            ////////////////////////////////////////////////////////////////////////
            function compact(i) {
                
                ppyStage.fadeTo((opts.duration/2),0).animate( cssCompactStage, {
                    queue:      false,
                    duration:   opts.duration,
                    easing:     opts.easing,
                    complete:   function() {
                        // return to original state
                        enPlaceholder.after(obj);
                        obj.css(cssCompactPpy);
                        ppyToolsWrap.show();
                        obj.removeClass(opts.eclass);
                        enPlaceholder.remove();
                        // show thumbnail image
                        display(cur);
                        $(this).fadeTo((opts.duration/2),1, function() {
                            ppyStageWrap.removeClass(opts.lclass);
                        });
                        
                    }
                });
            };
            
            
            ////////////////////////////////////////////////////////////////////////
            //
            // do stuff
            //
            ////////////////////////////////////////////////////////////////////////
            
            
            // popeye vars
            //------------------------------------------------------------------
            //define image object arrays
            var im  = {
                small: [],
                title: [],
                large: [],
                width: [],
                height: []
            };
            var maxWidth = 1000;
            var maxHeight = 1000;
            
            obj.find('li').each(function(i){
                im.small[i] = $(this).find('img').attr('src');   // the thumbnail url
                im.title[i] = $(this).find('img').attr('alt');   // the image title
                im.large[i] = $(this).find('a').attr('href');    // the image url
                im.width[i] = $(this).find('img').width();       // the image width
                im.height[i] = $(this).find('img').height();     // the image height
                
                //calculate minimum stage size
                if(maxWidth > im.width[i]) {
                    maxWidth = im.width[i];
                }
                if(maxHeight > im.height[i]) {
                    maxHeight = im.height[i];
                }
            });
            if(opts.stageW) {
                maxWidth = opts.stageW;
            }
            if(opts.stageH) {
                maxHeight = opts.stageH;
            }
            var cur = 0;                 // array index of currently displayed image
            var tot = im.small.length;   // total number of images
            
            
            // popeye dom setup
            //------------------------------------------------------------------
            
            // dispose of original image list
            obj.find('ul').remove();
            
            // crate html nodes
            var ppyStageWrap = $('<div class="popeye-stagewrap" />');
            var ppyStage     = $('<div class="popeye-stage" />');
            var ppyToolsWrap = $('<div class="popeye-tools-wrap" />');
            var ppyTools     = $('<div class="popeye-tools" />');
            var ppyCount     = $('<span class="popeye-count" />');
            var ppyCur       = $('<em class="popeye-cur" />');
            var ppyTotal     = $('<em class="popeye-total" />');
            var ppyPrev      = $('<a href="#" class="popeye-prev">' + opts.plabel + '</a>');
            var ppyNext      = $('<a href="#" class="popeye-next">' + opts.nlabel + '</a>');
            var ppyEnlarge   = $('<a href="#" class="popeye-enlarge">' + opts.blabel + '</a>');
            var ppyCap       = $('<div class="popeye-cap" />');
            
            // build DOM tree
            obj.append(ppyStageWrap);
            ppyStageWrap.append(ppyStage);
            ppyStageWrap.after(ppyToolsWrap);
            ppyToolsWrap.append(ppyTools);
            ppyTools.append(ppyPrev);
            ppyTools.append(ppyCount);
            ppyCount.append(ppyCur);
            ppyCount.append(ppyTotal);
            ppyCur.after(opts.oflabel);
            ppyTools.append(ppyEnlarge);
            ppyTools.append(ppyNext);
            ppyTools.after(ppyCap);
            
    
            // popeye css setup
            //------------------------------------------------------------------
            var cssCompactPpy = {
                position:       'relative',
                overflow:       'hidden',
                height:         'auto',         //overwrite fallback height restrictons
                overflow:       'hidden',       //remove scrolling behaviour
                top:            0
            };
            
            // css for right or left orientation
            if (opts.direction == 'left') {
                cssCompactPpy.left = 0;
            }
            else if (opts.direction == 'right') {
                cssCompactPpy.right = 0;
            }
            
            // set stage dims
            var cssCompactStage = {
                width:          maxWidth,
                height:         maxHeight
            };
            
            // set caption width to stage width
            var cssPpyCap = {
                width:          maxWidth 
            };
            
            // set toolbar width to stage width (IE doesn't recognize css auto width...)
            var ppyToolsWidth = parseInt(maxWidth);
            if( !isNaN( parseInt(ppyTools.css('borderLeftWidth'),10))) {
                ppyToolsWidth = ppyToolsWidth - parseInt(ppyTools.css('borderLeftWidth'), 10);
            }
            if( !isNaN( parseInt(ppyTools.css('borderRightWidth'),10))) {
                ppyToolsWidth = ppyToolsWidth - parseInt(ppyTools.css('borderRightWidth'),10);
            }
            ppyToolsWidth = ppyToolsWidth - parseInt(ppyTools.css('paddingLeft'),10);
            ppyToolsWidth = ppyToolsWidth - parseInt(ppyTools.css('paddingRight'),10);
            if( !isNaN( parseInt(ppyTools.css('marginLeft'),10))) {
                ppyToolsWidth = ppyToolsWidth - parseInt(ppyTools.css('marginLeft'),10);
            }
            if( !isNaN( parseInt(ppyTools.css('marginRight'),10))) {
                ppyToolsWidth = ppyToolsWidth - parseInt(ppyTools.css('marginRight'),10);
            }
            ppyToolsWidth = ppyToolsWidth + 'px';
            
            var cssPpyTools = {
                width:       ppyToolsWidth  
            };

            // style popeye
            obj.css(cssCompactPpy);
            if(opts.jclass) {
                obj.addClass(opts.jclass);
            }
            ppyStage.css(cssCompactStage);
            ppyCap.css(cssPpyCap);
            ppyTools.css(cssPpyTools);
            
            // display first image
            display(cur);
            
            
            // event handlers
            //------------------------------------------------------------------
            
            // previous image button
            ppyPrev.click(function(){
                if( cur <= 0 ) {
                    cur = tot - 1;
                } else {
                    cur--;
                }
                display(cur, true);
                return false;
            });
            // next image button
            ppyNext.click(function(){
                if( cur < ( tot - 1) ) {
                    cur++; 
                } else {
                    cur = 0;
                }
                display(cur, true);
                return false;
            });
            // enlarge image button
            ppyEnlarge.click(function(){
                ppyStage.unbind();
                enlarge(cur);
                return false;
            });
            
            
        });
    };
    
    ////////////////////////////////////////////////////////////////////////////
    //
    // $.fn.popeye.defaults
    // set default  options
    //
    ////////////////////////////////////////////////////////////////////////////
    $.fn.popeye.defaults = {
        jclass:     'popeye-hasjs',    //class to be applied to popeye-box when the browser has activated JavaScript (to overwrite fallback styling)
        eclass:     'popeye-haspopped', //class to be applied to enlarged popeye-box
        lclass:     'popeye-isloading',  //class to be applied to stage while loading image
        direction:  'left',            //direction that popeye-box opens, can be "left" or "right"
        duration:   400,               //duration of transitional effect when enlarging or closing the box
        easing:     'swing',           //easing type, can be 'swing', 'linear' or any of jQuery Easing Plugin types (Plugin required)
        nlabel:     'next',            //label for next button
        plabel:     'previous',        //label for previous button
        oflabel:    'of',              //label for image count text (e.g. 1 of 14)
        blabel:     'enlarge',         //label for enlarge button
        clabel:     'Click to close'   //tooltip on enlarged image (click image to close)

    };
    
// end of closure, bind to jQuery Object
})(jQuery); 
