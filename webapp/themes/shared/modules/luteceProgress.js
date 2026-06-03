/**
 * A class for generating a new password and returning its complexity value
 */
export default class LuteceProgress {
    /**
       * Creates a new instance of LuteceProgress.
       * 
       */
      constructor( progress ) {
        this.progress = progress || '.progressmanager';
        this.init()
      }

      /**
      * Initializes progress manager
      */
      init( ){
        const progressmanagers = document.querySelectorAll( this.progress );
        progressmanagers.forEach( function(progressmanager) {
            let id = progressmanager.getAttribute("id"),
                token = progressmanager.getAttribute("token"),
                showReport = progressmanager.getAttribute("showReport"),
                intervalTime = progressmanager.getAttribute("intervalTime");
            
            getProgress( id, token, intervalTime );
            
            if (showReport) {
                getReport( id, token, 0, intervalTime);
            }
        } );
    }

    getProgress(id, feedToken, intervalTime) {
        const xhr = new XMLHttpRequest();
        xhr.open( 'POST', 'servlet/plugins/core/progressManager/progressFeed?progress&token=' + feedToken, true );
        xhr.onload = function() {
            if ( xhr.status >= 200 && xhr.status < 400 ) {
                let data = JSON.parse(xhr.responseText);
                if (data.status !== "ERROR") {
                    updateProgressBar( id, parseInt(data.result) );
                    if ( getProgressValue(id) < getProgressMaxValue(id) ) {
                        setTimeout(function() {
                            getProgress(id, feedToken, intervalTime);
                        }, intervalTime);
                    }
                } else {
                    updateProgressReport( id, { "lines": [data.status, data.errorCode], "lastLine": 0 }, true );
                }
            } else {
                updateProgressReport( id, { "lines": [xhr.statusText, xhr.status], "lastLine": 0 }, true );
            }
        };
        xhr.onerror = function() {
            updateProgressReport( id, { "lines": [xhr.statusText, xhr.status], "lastLine": 0 }, true );
        };
        xhr.send();
    }

    getReport(id, feedToken, nfromLine, intervalTime) {
        const xhr = new XMLHttpRequest();
        xhr.open( 'POST', 'servlet/plugins/core/progressManager/progressFeed?report&fromLine=' + nfromLine + '&token=' + feedToken, true );
        xhr.onload = function() {
            if ( xhr.status >= 200 && xhr.status < 400 ) {
                let data = JSON.parse(xhr.responseText);
                if (data.status !== "ERROR") {
                    updateProgressReport(id, data.result, true);
                    if ( getProgressValue(id) < getProgressMaxValue(id) ) {
                        setTimeout( function() {
                            getReport( id, feedToken, parseInt(document.getElementById(id + "-report").getAttribute("lastline")), intervalTime );
                        }, intervalTime );
                    }
                } else {
                    updateProgressReport( id, { "lines": [data.status, data.errorCode], "lastLine": 0 }, true );
                }
            } else {
                updateProgressReport( id, { "lines": [xhr.statusText, xhr.status], "lastLine": 0 }, true );
            }
        };
        xhr.onerror = function() {
            updateProgressReport( id, { "lines": [xhr.statusText, xhr.status], "lastLine": 0 }, true );
        };
        xhr.send();
    }

    updateProgressBar( id, percentage ) {
        const element = document.getElementById(id);
        if (element.tagName === "PROGRESS") {
            element.value = percentage;
        } else {
            element.setAttribute('aria-valuenow', percentage);
            element.style.width = percentage + '%';
        }
        element.innerHTML = percentage + ' /' + getProgressMaxValue(id);
    }
    
    updateProgressReport( id, report, isAppend ) {
        const element = document.getElementById(id + "-report");
        if (!isAppend) {
            element.innerHTML = report.lines.join("<br />");
        } else {
            element.innerHTML += new Date().toLocaleTimeString(document.documentElement.lang) + '<br />';
            element.innerHTML += report.lines.join('<br />') + '<br />';
            element.setAttribute( 'lastline', report.lastLine );
        }
    }

    getProgressMaxValue(id) {
        const element = document.getElementById( id );
        if ( element.tagName === "PROGRESS" ) {
            return parseInt( element.max );
        } else {
            return parseInt( element.getAttribute('aria-valuemax') );
        }
    }

    getProgressValue( id ){
        const element = document.getElementById( id );
        if ( element.tagName === "PROGRESS" ) {
            return parseInt( element.value );
        } else {
            return parseInt( element.getAttribute('aria-valuenow') );
        }
    }

}