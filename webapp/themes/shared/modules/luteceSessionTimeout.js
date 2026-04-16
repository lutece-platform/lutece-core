/**
 * Manages session timeout warnings and keep-alive for accessibility.
 *
 * Supports two independent timers:
 *   1. Session timeout  – the HTTP session duration (configurable via core_datastore).
 *   2. Process timeout   – an optional shorter deadline set by a plugin (e.g. Forms)
 *      for a specific workflow that may expire before the web session.
 *
 * WCAG compliance:
 *   - 2.2.1 Timing Adjustable (Level A): users can extend at least `maxExtensions` times.
 *   - 2.2.6 Timeouts (Level AAA): users are warned before data loss.
 *
 * Accessibility approach (https://tink.uk/accessible-timeout-notifications/):
 *   - role="group" + aria-labelledby with a heading for semantic structure
 *   - tabindex="-1" + focus() to alert screen reader / keyboard users
 *   - Focus is saved before the notification and restored on dismissal
 *   - Separate aria-live region for polite countdown announcements
 *   - DOM nodes are inserted / removed (never display:none toggled)
 *
 * Plugin integration (e.g. Forms):
 *   Plugins set properties on `window.__sessionTimeoutConfig` **before** this
 *   module is loaded (their templates render before <@cSessionTimeout />).
 *
 *   window.__sessionTimeoutConfig.saveEnabled = true;
 *   window.__sessionTimeoutConfig.saveAction  = function () {
 *       const form = document.getElementById('form-validate');
 *       if (!form) return Promise.resolve();
 *       const fd = new FormData(form);
 *       fd.append('action_doSaveForBackup', '');
 *       return fetch(form.action, { method: 'POST', body: fd });
 *   };
 *   window.__sessionTimeoutConfig.processTimeoutDuration = 600;
 */
export default class LuteceSessionTimeout {
  /**
   * @param {Object} config
   * @param {number}   config.timeoutDuration        – Session timeout (seconds). Default 1800.
   * @param {number}   config.warningDelay            – Seconds before session timeout to warn. Default 120.
   * @param {string}   config.keepAliveUrl             – URL to ping to renew the session.
   * @param {string}   config.loginUrl                 – Redirect URL after expiration.
   * @param {number}   [config.processTimeoutDuration] – Optional shorter process timeout (seconds, 0 = disabled).
   * @param {number}   [config.processWarningDelay]    – Warning delay for process timeout (seconds).
   * @param {boolean}  [config.saveEnabled]            – Show a "Save" button in the warning.
   * @param {Function} [config.saveAction]             – Async callback that saves user data. Must return a Promise.
   * @param {number}   [config.maxExtensions]          – Max times the user can extend (WCAG ≥ 10). Default 10.
   * @param {string}   [config.position]               – Bootstrap position classes for the toast container. Default 'top-0 end-0'.
   * @param {Object}   config.messages                 – Translated UI strings (see below).
   */
  constructor(config) {
    // ── Session timeout ────────────────────────────────────────
    this.timeoutDuration = config.timeoutDuration || 1800;
    this.warningDelay = config.warningDelay || 120;
    this.keepAliveUrl = config.keepAliveUrl || '';
    this.loginUrl = config.loginUrl || '';

    // ── Process timeout (optional, shorter) ────────────────────
    this.processTimeoutDuration = config.processTimeoutDuration || 0;
    this.processWarningDelay = config.processWarningDelay || this.warningDelay;

    // ── Save functionality ─────────────────────────────────────
    this.saveEnabled = Boolean(config.saveEnabled);
    this.saveAction = typeof config.saveAction === 'function' ? config.saveAction : null;

    // ── WCAG 2.2.1 – extend at least 10 times ─────────────────
    this.maxExtensions = config.maxExtensions || 10;
    this._extensionCount = 0;

    // ── Toast container position ───────────────────────────────
    this.position = config.position || 'top-0 end-0';

    // ── Messages ───────────────────────────────────────────────
    this.messages = config.messages || {};

    // ── Internal state ─────────────────────────────────────────
    this._warningTimer = null;
    this._expireTimer = null;
    this._processWarningTimer = null;
    this._processExpireTimer = null;
    this._countdownInterval = null;
    this._remainingSeconds = 0;
    this._container = null;
    this._notificationEl = null;
    this._srLiveRegion = null;
    this._previousFocus = null;
    this._isProcessWarning = false;

    this._activityEvents = ['mousedown', 'keydown', 'touchstart', 'scroll'];
    this._onActivity = this._resetTimers.bind(this);

    this._init();
  }

  // ══════════════════════════════════════════════════════════════
  //  Lifecycle
  // ══════════════════════════════════════════════════════════════

  _init() {
    this._createSrLiveRegion();
    this._bindActivityEvents();
    this._startTimers();
  }

  /**
   * Cleans up all timers, events, and DOM elements.
   */
  destroy() {
    this._clearAllTimers();
    this._unbindActivityEvents();
    this._dismissNotification();
    this._restoreFocus();
    if (this._srLiveRegion) {
      this._srLiveRegion.remove();
      this._srLiveRegion = null;
    }
    if (this._container) {
      this._container.remove();
      this._container = null;
    }
  }

  // ══════════════════════════════════════════════════════════════
  //  Accessibility – screen reader live region
  // ══════════════════════════════════════════════════════════════

  _createSrLiveRegion() {
    this._srLiveRegion = document.createElement('div');
    this._srLiveRegion.setAttribute('role', 'status');
    this._srLiveRegion.setAttribute('aria-live', 'polite');
    this._srLiveRegion.setAttribute('aria-atomic', 'true');
    this._srLiveRegion.className = 'visually-hidden';
    document.body.appendChild(this._srLiveRegion);
  }

  _announceSr(text) {
    if (!this._srLiveRegion) return;
    this._srLiveRegion.textContent = '';
    requestAnimationFrame(() => {
      this._srLiveRegion.textContent = text;
    });
  }

  _announceSrCountdown() {
    const minutes = Math.floor(this._remainingSeconds / 60);
    const seconds = this._remainingSeconds % 60;
    this._announceSr(
      this._fmt(this.messages.srCountdown || '', String(minutes), String(seconds))
    );
  }

  // ══════════════════════════════════════════════════════════════
  //  User activity tracking
  // ══════════════════════════════════════════════════════════════

  _bindActivityEvents() {
    this._activityEvents.forEach(e =>
      document.addEventListener(e, this._onActivity, { passive: true })
    );
  }

  _unbindActivityEvents() {
    this._activityEvents.forEach(e =>
      document.removeEventListener(e, this._onActivity)
    );
  }

  // ══════════════════════════════════════════════════════════════
  //  Timers
  // ══════════════════════════════════════════════════════════════

  _startTimers() {
    this._clearAllTimers();

    // Session timers
    const sessionWarningAt = (this.timeoutDuration - this.warningDelay) * 1000;
    this._warningTimer = setTimeout(() => this._showWarning(false), sessionWarningAt);
    this._expireTimer = setTimeout(() => this._onExpired(false), this.timeoutDuration * 1000);

    // Process timers (if shorter than session)
    if (this.processTimeoutDuration > 0 && this.processTimeoutDuration < this.timeoutDuration) {
      const processWarningAt = (this.processTimeoutDuration - this.processWarningDelay) * 1000;
      this._processWarningTimer = setTimeout(() => this._showWarning(true), processWarningAt);
      this._processExpireTimer = setTimeout(() => this._onExpired(true), this.processTimeoutDuration * 1000);
    }
  }

  _clearAllTimers() {
    clearTimeout(this._warningTimer);
    clearTimeout(this._expireTimer);
    clearTimeout(this._processWarningTimer);
    clearTimeout(this._processExpireTimer);
    clearInterval(this._countdownInterval);
    this._warningTimer = null;
    this._expireTimer = null;
    this._processWarningTimer = null;
    this._processExpireTimer = null;
    this._countdownInterval = null;
  }

  _resetTimers() {
    // Don't reset while a notification is visible
    if (this._notificationEl) return;
    this._startTimers();
  }

  // ══════════════════════════════════════════════════════════════
  //  Warning notification
  // ══════════════════════════════════════════════════════════════

  /**
   * @param {boolean} isProcess – true when triggered by the process timer
   */
  _showWarning(isProcess) {
    // If a notification is already showing, don't replace it
    if (this._notificationEl) return;

    this._isProcessWarning = isProcess;
    this._unbindActivityEvents();
    this._previousFocus = document.activeElement;

    this._remainingSeconds = isProcess ? this.processWarningDelay : this.warningDelay;

    const canExtend = this._extensionCount < this.maxExtensions;

    this._getContainer();

    const headingId = 'session-timeout-heading';
    this._notificationEl = document.createElement('div');
    this._notificationEl.className = 'toast fade show';
    this._notificationEl.setAttribute('role', 'group');
    this._notificationEl.setAttribute('aria-labelledby', headingId);
    this._notificationEl.setAttribute('tabindex', '-1');

    const title = isProcess
      ? (this.messages.processWarningTitle || this.messages.warningTitle || '')
      : (this.messages.warningTitle || '');

    const messageTpl = isProcess
      ? (this.messages.processWarningMessage || this.messages.warningMessage || '')
      : (this.messages.warningMessage || '');

    this._notificationEl.innerHTML = this._buildWarningHtml(
      headingId, title, messageTpl, canExtend
    );
    this._container.appendChild(this._notificationEl);
    this._notificationEl.focus();

    // Bind buttons
    this._notificationEl.querySelectorAll('[data-action="extend"]').forEach(btn =>
      btn.addEventListener('click', () => this._extendSession())
    );
    this._notificationEl.querySelectorAll('[data-action="save"]').forEach(btn =>
      btn.addEventListener('click', () => this._saveData())
    );

    this._startCountdown(messageTpl);
    this._announceSr(this._fmt(messageTpl, this._fmtTime(this._remainingSeconds)));
  }

  _buildWarningHtml(headingId, title, messageTpl, canExtend) {
    const countdownText = this._fmt(messageTpl, this._fmtTime(this._remainingSeconds));

    const showSave = this.saveEnabled && this.saveAction;
    const showExtend = canExtend;

    let buttons = '';
    if (showExtend) {
      buttons += `<button type="button" class="btn btn-secondaryy" data-action="extend">${this.messages.btnExtend || ''}</button> `;
    }
    if (showSave) {
      buttons += `<button type="button" class="btn btn-primary" data-action="save">${this.messages.btnSave || ''}</button>`;
    }
    if (!showExtend) {
      buttons += `<span class="small">${this.messages.maxExtensionsReached || ''}</span>`;
    }

    return `<div class="toast-header">
        <h2 class="h6 me-auto my-0" id="${headingId}">${title}</h2>
        ${canExtend ? `<button type="button" class="btn-close" data-action="extend" aria-label="${this.messages.labelClose || ''}"></button>` : ''}
      </div>
      <div class="toast-body">
        <p class="mb-2" data-countdown>${countdownText}</p>
        <div class="d-flex gap-1 align-items-center flex-wrap">${buttons}</div>
        <div data-save-feedback class="mt-1"></div>
      </div>`;
  }

  // ══════════════════════════════════════════════════════════════
  //  Countdown
  // ══════════════════════════════════════════════════════════════

  _startCountdown(messageTpl) {
    this._countdownInterval = setInterval(() => {
      this._remainingSeconds--;
      if (this._remainingSeconds <= 0) {
        clearInterval(this._countdownInterval);
        return;
      }
      const el = this._notificationEl?.querySelector('[data-countdown]');
      if (el) {
        el.textContent = this._fmt(messageTpl, this._fmtTime(this._remainingSeconds));
      }
      // Announce every 30 s, and every second under 10 s
      if (this._remainingSeconds % 30 === 0 || this._remainingSeconds <= 10) {
        this._announceSrCountdown();
      }
    }, 1000);
  }

  // ══════════════════════════════════════════════════════════════
  //  User actions
  // ══════════════════════════════════════════════════════════════

  /**
   * Extends the session (and process timer) by pinging the keep-alive URL.
   */
  async _extendSession() {
    this._extensionCount++;
    try {
      await fetch(this.keepAliveUrl, { method: 'HEAD', cache: 'no-store' });
    } catch {
      // best-effort
    }
    this._dismissNotification();
    this._restoreFocus();
    this._bindActivityEvents();
    this._startTimers();
  }

  /**
   * Calls the plugin-provided saveAction callback and shows feedback.
   */
  async _saveData() {
    if (!this.saveAction) return;

    const feedback = this._notificationEl?.querySelector('[data-save-feedback]');
    const btn = this._notificationEl?.querySelector('[data-action="save"]');
    if (btn) btn.disabled = true;

    try {
      await this.saveAction();
      if (feedback) {
        feedback.innerHTML = `<span class="small">${this.messages.saveSuccess || ''}</span>`;
      }
      this._announceSr(this.messages.saveSuccess || '');
    } catch {
      if (feedback) {
        feedback.innerHTML = `<span class="text-danger small">${this.messages.saveError || ''}</span>`;
      }
      this._announceSr(this.messages.saveError || '');
    }
  }

  // ══════════════════════════════════════════════════════════════
  //  Expiration
  // ══════════════════════════════════════════════════════════════

  /**
   * @param {boolean} isProcess – true when the process timer expired
   */
  _onExpired(isProcess) {
    this._clearAllTimers();
    this._unbindActivityEvents();
    this._dismissNotification();
    this._getContainer();

    const headingId = 'session-expired-heading';
    const title = isProcess
      ? (this.messages.processExpiredTitle || this.messages.expiredTitle || '')
      : (this.messages.expiredTitle || '');
    const message = isProcess
      ? (this.messages.processExpiredMessage || this.messages.expiredMessage || '')
      : (this.messages.expiredMessage || '');

    const el = document.createElement('div');
    el.className = 'toast fade error show';
    el.setAttribute('role', 'group');
    el.setAttribute('aria-labelledby', headingId);
    el.setAttribute('tabindex', '-1');

    el.innerHTML = `<div class="toast-header">
          <h2 class="h6 me-auto my-0" id="${headingId}">${title}</h2>
        </div>
        <div class="toast-body">
        <p class="mb-2">${message}</p>
        <p><a href="${this.loginUrl}" class="btn btn-secondary">${this.messages.btnLogin || ''}</a></p>
    </div>`;

    this._container.appendChild(el);
    this._notificationEl = el;
    el.focus();
    this._announceSr(message);
  }

  // ══════════════════════════════════════════════════════════════
  //  Notification helpers
  // ══════════════════════════════════════════════════════════════

  _dismissNotification() {
    if (this._notificationEl) {
      this._notificationEl.remove();
      this._notificationEl = null;
    }
    clearInterval(this._countdownInterval);
    this._countdownInterval = null;
  }

  _restoreFocus() {
    if (this._previousFocus && typeof this._previousFocus.focus === 'function') {
      this._previousFocus.focus();
    }
    this._previousFocus = null;
  }

  _getContainer() {
    if (!this._container) {
      this._container = document.getElementById('session-timeout-container');
    }
    if (!this._container) {
      this._container = document.createElement('div');
      this._container.id = 'session-timeout-container';
      this._container.className = `toast-container position-fixed ${this.position} p-3`;
      this._container.style.zIndex = '1090';
      document.body.appendChild(this._container);
    }
    return this._container;
  }

  // ══════════════════════════════════════════════════════════════
  //  Formatting utilities
  // ══════════════════════════════════════════════════════════════

  /** Replace {0}, {1}, … placeholders. */
  _fmt(template, ...args) {
    if (!template) return '';
    return args.reduce((msg, arg, i) => msg.replace(`{${i}}`, arg), template);
  }

  /** Seconds → mm:ss */
  _fmtTime(totalSeconds) {
    const m = Math.floor(totalSeconds / 60);
    const s = totalSeconds % 60;
    return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`;
  }
}
