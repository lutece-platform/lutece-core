/* BEWARE THIS UTIL IS JQUERY dependant */
function getProgress(id, feedToken, intervalTime) {
    fetch('servlet/plugins/core/progressManager/progressFeed?progress&token=' + feedToken, {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        if (data.status != "ERROR") {
            updateProgressBar(id, parseInt(data.result));
            if (getProgressValue(id) < getProgressMaxValue(id)) {
                setTimeout(() => getProgress(id, feedToken, intervalTime), intervalTime);
            }
        } else {
            updateProgressReport(id, { "lines": [data.status, data.errorCode], "lastLine": 0 }, true);
        }
    })
    .catch(error => {
        console.log(error);
        updateProgressReport(id, { "lines": [error.message], "lastLine": 0 }, true);
    });
}

function getReport(id, feedToken, nfromLine, intervalTime) {
    fetch('servlet/plugins/core/progressManager/progressFeed?report&fromLine=' + nfromLine + '&token=' + feedToken, {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        if (data.status != "ERROR") {
            updateProgressReport(id, data.result, true);
            if (getProgressValue(id) < getProgressMaxValue(id)) {
                setTimeout(() => getReport(id, feedToken, parseInt(document.getElementById(id + "-report").getAttribute("lastline"))), intervalTime);
            }
        } else {
            updateProgressReport(id, { "lines": [data.status, data.errorCode], "lastLine": 0 }, true);
        }
    })
    .catch(error => {
        console.log(error);
        updateProgressReport(id, { "lines": [error.message], "lastLine": 0 }, true);
    });
}

function updateProgressBar(id, percentage) {
    const element = document.getElementById(id);
    
    if (element.tagName === "PROGRESS") {
        element.value = percentage;
    } else {
        element.setAttribute('aria-valuenow', percentage);
        element.style.width = percentage + '%';
    }
    
    element.innerHTML = percentage + ' /' + getProgressMaxValue(id);
}

function updateProgressReport(id, report, isAppend) {
    const reportElement = document.getElementById(id + "-report");
    
    if (!isAppend) {
        reportElement.innerHTML = report.lines.join("<br />");
    } else {
        reportElement.innerHTML += new Date().toLocaleTimeString(document.documentElement.lang) + "<br />";
        reportElement.innerHTML += report.lines.join("<br />") + "<br />";
        reportElement.setAttribute("lastline", report.lastLine);
    }
}

function getProgressMaxValue(id) {
    const element = document.getElementById(id);
    
    if (element.tagName === "PROGRESS") {
        return parseInt(element.getAttribute('max'));
    } else {
        return parseInt(element.getAttribute('aria-valuemax'));
    }
}

function getProgressValue(id) {
    const element = document.getElementById(id);
    
    if (element.tagName === "PROGRESS") {
        return parseInt(element.value);
    } else {
        return parseInt(element.getAttribute('aria-valuenow'));
    }
}

function processError(id, errorMsg) {
    updateProgressReport(id, errorMsg, true);
}

document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.progressmanager').forEach(element => {
        const id = element.getAttribute("id");
        const token = element.getAttribute("token");
        const showReport = element.getAttribute("showReport");
        const intervalTime = element.getAttribute("intervalTime");
        
        // get progress
        getProgress(id, token, intervalTime);
        
        if (showReport) {
            getReport(id, token, 0, intervalTime);
        }
    });
});
