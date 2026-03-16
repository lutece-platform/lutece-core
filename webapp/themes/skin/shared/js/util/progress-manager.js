
function getProgress( id, feedToken, intervalTime ) {
    $.ajax({
        url: 'servlet/plugins/core/progressManager/progressFeed?progress&token=' + feedToken ,
        method: 'POST',
        async: true,
        success: function(data) {
        	if (data.status != "ERROR") {
			updateProgressBar( id, parseInt(data.result) );
		        if ( getProgressValue( id ) < getProgressMaxValue( id ) ) {
		            setTimeout(  function () { getProgress( id, feedToken, intervalTime ) } , intervalTime ) ; 
		        } 
		} else {
			updateProgressReport( id, { "lines" : [ data.status, data.errorCode ], "lastLine" : 0 } , true );
		}
        },
        error : function(jqXHR, textStatus, errorThrown) {
                console.log(textStatus, errorThrown);
                updateProgressReport( id, { "lines" : [ textStatus, errorThrown ], "lastLine" : 0 } , true );
        }
    });
}

function getReport( id, feedToken, nfromLine, intervalTime ) {
    $.ajax({
        url: 'servlet/plugins/core/progressManager/progressFeed?report&fromLine=' + nfromLine + '&token=' + feedToken ,
        method: 'POST',
        async: true,
        success: function(data) {
        	if (data.status != "ERROR") {
		        updateProgressReport( id, data.result, true );
		        if ( getProgressValue( id ) < getProgressMaxValue( id ) ) {
		            setTimeout(  function () { getReport( id, feedToken, parseInt( $('#'+id+"-report").attr( "lastline" ) ), intervalTime ) } , intervalTime );
		        } 
		} else {
			updateProgressReport( id, { "lines" : [ data.status, data.errorCode ], "lastLine" : 0 } , true );
		}
        },
        error : function(jqXHR, textStatus, errorThrown) {
                console.log(textStatus, errorThrown);
                updateProgressReport( id, { "lines" : [ textStatus, errorThrown ], "lastLine" : 0 } , true );
        }
    });
}
 
function updateProgressBar(id, percentage){
    if ( $('#'+id).prop("tagName") == "PROGRESS" ) {
        $('#'+id).attr('value', percentage);
    } else {
        $('#'+id).attr('aria-valuenow', percentage);
        $('#'+id).attr('style', 'width: ' + percentage + '%;');
    }
        
    $('#'+id).html( percentage+' /'+ getProgressMaxValue( id ) );
}

function updateProgressReport(id, report, isAppend) {
    if ( !isAppend ) {
        $('#'+id+"-report").html( report.lines.join("<br />") );
    } else {
    $('#'+id+"-report").append( new Date().toLocaleTimeString( document.documentElement.lang ) + "<br />");
        
        $('#'+id+"-report").append( report.lines.join("<br />") +"<br />" );
        $('#'+id+"-report").attr( "lastline" , report.lastLine );
    }
}

function getProgressMaxValue( id ){
    if ( $('#'+id).prop("tagName") == "PROGRESS" )
    {
	return parseInt($('#'+id).attr('max'));
    }
    else
    {
        return parseInt($('#'+id).attr('aria-valuemax'));
    }
}

function getProgressValue( id ){
    if ( $('#'+id).prop("tagName") == "PROGRESS" )
    {
	return parseInt($('#'+id).attr('value'));
    }
    else
    {
        return parseInt($('#'+id).attr('aria-valuenow'));
    }
}

function processError( id, errorMsg ) {
    updateProgressReport( id, errorMsg, true );
}


$(document).ready( function( ) {
    $('.progressmanager').each( function() {
            var id = $( this ).attr( "id" );
            var token = $( this ).attr( "token" );
            var showReport = $( this ).attr( "showReport" );
            var intervalTime = $( this ).attr( "intervalTime" );
            
            // get progress
            getProgress( id, token, intervalTime );
            
            if ( showReport ) {
                getReport( id, token, 0, intervalTime );
            }
    });
        
});    