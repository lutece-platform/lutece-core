/* Lutece progress manager - vanilla JS (no jQuery dependency) */

const PROGRESS_FEED_URL = 'servlet/plugins/core/progressManager/progressFeed';

function progressElement(id) {
    return document.getElementById(id);
}

function reportElement(id) {
    return document.getElementById(id + '-report');
}

function isProgressTag(element) {
    return element !== null && element.tagName === 'PROGRESS';
}

function fetchFeed(query) {
    return fetch(PROGRESS_FEED_URL + '?' + query, { method: 'POST' })
        .then(response => {
            if (!response.ok) {
                throw new Error(response.status + ' ' + response.statusText);
            }
            return response.json();
        });
}

function reportError(id, lines) {
    updateProgressReport(id, { lines: lines, lastLine: 0 }, true);
}

function getProgress(id, feedToken, intervalTime) {
    fetchFeed('progress&token=' + encodeURIComponent(feedToken))
        .then(data => {
            if (data.status !== 'ERROR') {
                updateProgressBar(id, parseInt(data.result, 10));
                if (getProgressValue(id) < getProgressMaxValue(id)) {
                    setTimeout(() => getProgress(id, feedToken, intervalTime), intervalTime);
                }
            } else {
                reportError(id, [data.status, data.errorCode]);
            }
        })
        .catch(error => {
            console.log(error);
            reportError(id, [error.message]);
        });
}

function getReport(id, feedToken, nfromLine, intervalTime) {
    fetchFeed('report&fromLine=' + nfromLine + '&token=' + encodeURIComponent(feedToken))
        .then(data => {
            if (data.status !== 'ERROR') {
                updateProgressReport(id, data.result, true);
                if (getProgressValue(id) < getProgressMaxValue(id)) {
                    setTimeout(() => {
                        const report = reportElement(id);
                        const lastLine = report ? parseInt(report.getAttribute('lastline'), 10) || 0 : 0;
                        getReport(id, feedToken, lastLine, intervalTime);
                    }, intervalTime);
                }
            } else {
                reportError(id, [data.status, data.errorCode]);
            }
        })
        .catch(error => {
            console.log(error);
            reportError(id, [error.message]);
        });
}

function updateProgressBar(id, percentage) {
    const element = progressElement(id);
    if (element === null) {
        return;
    }
    if (isProgressTag(element)) {
        element.value = percentage;
    } else {
        element.setAttribute('aria-valuenow', percentage);
        element.style.width = percentage + '%';
    }
    element.textContent = percentage + ' /' + getProgressMaxValue(id);
}

function updateProgressReport(id, report, isAppend) {
    const element = reportElement(id);
    if (element === null || !report || !Array.isArray(report.lines)) {
        return;
    }
    if (!isAppend) {
        element.innerHTML = report.lines.join('<br />');
    } else {
        element.insertAdjacentHTML('beforeend', new Date().toLocaleTimeString(document.documentElement.lang) + '<br />');
        element.insertAdjacentHTML('beforeend', report.lines.join('<br />') + '<br />');
        element.setAttribute('lastline', report.lastLine);
    }
}

function getProgressMaxValue(id) {
    const element = progressElement(id);
    if (element === null) {
        return 0;
    }
    return parseInt(isProgressTag(element) ? element.getAttribute('max') : element.getAttribute('aria-valuemax'), 10);
}

function getProgressValue(id) {
    const element = progressElement(id);
    if (element === null) {
        return 0;
    }
    return parseInt(isProgressTag(element) ? element.value : element.getAttribute('aria-valuenow'), 10);
}

function processError(id, errorMsg) {
    updateProgressReport(id, errorMsg, true);
}

function initProgressManagers() {
    document.querySelectorAll('.progressmanager').forEach(element => {
        const id = element.getAttribute('id');
        const token = element.getAttribute('token');
        const showReport = element.getAttribute('showReport') === 'true';
        const intervalTime = parseInt(element.getAttribute('intervalTime'), 10) || 2000;

        getProgress(id, token, intervalTime);

        if (showReport) {
            getReport(id, token, 0, intervalTime);
        }
    });
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initProgressManagers);
} else {
    initProgressManagers();
}
