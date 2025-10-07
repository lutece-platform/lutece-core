/**
 * BackportTemplateStyle class
 */
export class themeInit {
    constructor() {
        document.addEventListener('DOMContentLoaded', () => {
            this.themeInit();
        });
    }  

    themeInit() {
        /* Responsive */
        // tableXsCollapse();
        
        window.onresize = (event) => {
            // tableXsCollapse();
        };

        /* Components           */
        /* Accordion with border */
        const cardWBHeaders = document.querySelectorAll('.card.with-border .card-header')
        if( cardWBHeaders.length > 0 ){
            const accordionHeaders = [].slice.call( document.querySelectorAll('.card.with-border .card-header'));
            accordionHeaders.forEach( acc => {
                acc.addEventListener('click', (e) => {
                    if( e.currentTarget.children('.btn-accordion').classList.contains('collapsed') ){
                        e.currentTarget.closest('.card.with-border').classList.contains('open');
                    } else {
                        e.currentTarget.closest('.card.with-border').classList.remove('open');
                    }
                });
            });
        }
        
        /* List with More  */
        // $('.btn-more').click( function(e){
        //     e.preventDefault();
        //     $(".list-more > .extra").toggleClass('hidden');
        // });

        /* Infostep with More  */
        // $('.btn-infostep-more').click( function(e){
        //     e.preventDefault();
        //     $(this).children('.link-label').toggle();
        //     $(this).children('.fa-angle-up').toggleClass('hidden');
        //     $(this).closest('.infostep-more').children('.extra').toggleClass('visually-hidden');
        //     $(this).closest('.infostep-more').children('.ellipsis').toggleClass('visually-hidden');
        // });
    }

    // tableXsCollapse(  ){
    //     if( window.innerWidth < 580 ){
    //         document.querySelectorAll( '.xs-collapsed td:first-child' ).forEach( cell => {
    //             cell.addEventListener('click', (e) => {
    //                 // var nextAll = false;
    //                 // nextAll = [].filter.call(<htmlElement>.parentNode.children, function (htmlElement) {
    //                 //     return (htmlElement.previousElementSibling === <htmlElement>) ? nextAll = true : nextAll;
    //                 // });
                  
    //                 e.currentTarget.nextAll('td').classList.toggle('d-block');
    //                 e.currentTarget.classList.toggle('open');
    //             });   
    //         })
    //     }
    // }
    

    setCookie( cname, cvalue, exdays ) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays*24*60*60*1000));
        var expires = "expires=" + d.toGMTString();
        document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    }
  
    getCookie(cname) {
        var name = cname + "=";
        var decodedCookie = decodeURIComponent(document.cookie);
        var ca = decodedCookie.split(';');
        for(var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
        }
        return "";
    }
  
    checkCookie( cookieName ) {
        var c=getCookie( cookieName );
        if ( c != "") {
        return true;
        } else {
        return false;
        }
    }

    addNoOpener( link ) {
        let linkTypes = ( link.getAttribute('rel') || '' ).split(' ');
        if (!linkTypes.includes('noopener')) {
            linkTypes.push('noopener');
        }
        link.setAttribute('rel', linkTypes.join(' ').trim());
    }

    addNewTabMessage( link ) {
        if (!link.querySelector('.screen-reader-only')) {
            link.insertAdjacentHTML('beforeend', '<span class="screen-reader-only">(opens in a new tab)</span>');
        }
    }

    setCharCounter( selCounters ){
        let counters='';
        if( selCounters === null || selCounters === undefined ){
            counters='.counter';
        } else {
            counters=selCounters;
        }

        document.querySelectorAll( counters ).forEach( function( counter ){
        let nCounterMax = counter.attr('maxlength');
        if( nCounterMax != NaN && nCounterMax > 0 ){
            let tag = '<span class="ml-2 counter-number dark-gray-color font-weight-bold">0 /</span><small class="dark-gray-color">' + nCounterMax + ' caractères maximum</small>';
            counter.parent().children('label').append( tag );
            let counter=counter.parent().find('.counter-number'), nChars=0;
            counter.on( 'keyup', function(){
                nChars = counter.val().length;
                counter.html( nChars + ' /' );
                if( nChars >= nCounterMax  ){ 
                    counter.after('<p role="alert" class="visually-hidden">Vous avez atteint le nombre de caractères maximum</p>');
                    return false ;
                }
            });
            counter.on('paste', function(event) {
                event.preventDefault();
                const content=$(this).val();
                $(this).val('');
                let clip = event.originalEvent.clipboardData.getData('Text');
                let final_clip = content + clip.replace(/\s+/g, ' ');
                if( final_clip.length > nCounterMax ){
                    final_clip = final_clip.substring(0, nCounterMax)
                }
                $(this).val(final_clip);
                nChars = $(this).val().length;
                counter.html( nChars + ' /' );
                if( nChars >= nCounterMax  ){ 
                    $(this).after('<p role="alert" class="visually-hidden">Vous avez atteint le nombre de caractères maximum</p>');
                    return false 
                }
            });
        } else {
            counter.parent().children('label').after( '<!-- WARNING no max-length attribute on the input below = no oounter shown -->' );
        }
        });

    }
    
}