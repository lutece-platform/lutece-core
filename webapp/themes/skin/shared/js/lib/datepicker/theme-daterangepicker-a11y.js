/**
 * Accessible DateRangePicker
 * Connects two themeDatepickerA11y instances into a coordinated date range.
 *
 * Depends on theme-datepicker-a11y.js (must be loaded first).
 *
 * Accessibility features:
 * - Wrapper uses role="group" with aria-label for range context
 * - Live region announces constraint changes to screen readers
 * - Start selection constrains end picker minDate (and vice-versa)
 * - Range cells receive .in-range / .range-start / .range-end for visual feedback
 * - Full keyboard navigation inherited from DatepickerDialog
 *
 * Usage:
 *   const rangepicker = new DateRangePickerA11y(
 *     document.getElementById('myRange'),
 *     { format: 'dd/mm/yyyy', weekStart: 1 }
 *   );
 */
(function (root) {
  'use strict';

  /* ------------------------------------------------------------------ */
  /*  Helpers (self-contained – no dependency on datepicker internals)   */
  /* ------------------------------------------------------------------ */

  /** Day-level date comparison */
  function sameDay(a, b) {
    return (
      a && b &&
      a.getFullYear() === b.getFullYear() &&
      a.getMonth() === b.getMonth() &&
      a.getDate() === b.getDate()
    );
  }

  /** Normalise a Date to midnight for safe comparisons */
  function midnight(d) {
    if (!d) return null;
    return new Date(d.getFullYear(), d.getMonth(), d.getDate());
  }

  /* ------------------------------------------------------------------ */
  /*  DateRangePickerA11y                                               */
  /* ------------------------------------------------------------------ */

  class DateRangePickerA11y {
    /**
     * @param {HTMLElement|string} wrapperEl  Wrapper containing exactly 2 visible inputs
     * @param {Object}            options     Options forwarded to each themeDatepickerA11y
     *        Extra range-specific keys:
     *          labelRange   {string}  aria-label for the group (default: locale-based)
     *          labelStart   {string}  aria-description for start input
     *          labelEnd     {string}  aria-description for end input
     *          allowSameDay {boolean} Allow start === end (default: true)
     */
    constructor(wrapperEl, options) {
      if (typeof wrapperEl === 'string') {
        wrapperEl = document.getElementById(wrapperEl);
      }
      if (!wrapperEl) throw new Error('DateRangePickerA11y: wrapper element not found');

      this.element = wrapperEl;
      this.options = Object.assign({ allowSameDay: true }, options);

      // ---- Collect raw inputs before transformation ----
      const rawInputs = Array.from(
        wrapperEl.querySelectorAll('input:not([type="hidden"])')
      );
      if (rawInputs.length < 2) {
        throw new Error(
          'DateRangePickerA11y: wrapper must contain at least 2 visible <input> elements'
        );
      }

      // ---- ARIA: group role ----
      if (!wrapperEl.getAttribute('role')) {
        wrapperEl.setAttribute('role', 'group');
      }
      const lang = (this.options.language) || navigator.language.split('-')[0];
      const rangeLabelDefault = lang === 'fr' ? 'Plage de dates' : 'Date range';
      wrapperEl.setAttribute(
        'aria-label',
        this.options.labelRange || wrapperEl.getAttribute('aria-label') || rangeLabelDefault
      );

      // ---- Live region for constraint announcements ----
      this.liveRegion = document.createElement('div');
      this.liveRegion.className = 'visually-hidden';
      this.liveRegion.setAttribute('aria-live', 'polite');
      this.liveRegion.setAttribute('aria-atomic', 'true');
      wrapperEl.appendChild(this.liveRegion);

      // ---- Create individual datepickers ----
      var startOpts = Object.assign({}, this.options);
      var endOpts   = Object.assign({}, this.options);
      // Remove range-specific keys before forwarding
      delete startOpts.labelRange;
      delete startOpts.labelStart;
      delete startOpts.labelEnd;
      delete startOpts.allowSameDay;
      delete endOpts.labelRange;
      delete endOpts.labelStart;
      delete endOpts.labelEnd;
      delete endOpts.allowSameDay;

      this.datepickers = [
        new root.themeDatepickerA11y(rawInputs[0], startOpts),
        new root.themeDatepickerA11y(rawInputs[1], endOpts)
      ];

      // Expose display inputs (the visible clones created by themeDatepickerA11y)
      this.inputs = [this.datepickers[0].element, this.datepickers[1].element];

      // Tag each input with its role in the range so it can be targeted in CSS/JS
      this.inputs[0].classList.add('dt-range-start');
      this.inputs[1].classList.add('dt-range-end');

      // ---- ARIA: describe each input's role in the range ----
      this._addDescription(this.inputs[0], this.options.labelStart || (lang === 'fr' ? 'Date de début' : 'Start date'));
      this._addDescription(this.inputs[1], this.options.labelEnd   || (lang === 'fr' ? 'Date de fin'   : 'End date'));

      // ---- Wire synchronisation & highlight ----
      this._setupSync();
      this._setupRangeHighlight();
    }

    /* ---------------------------------------------------------------- */
    /*  ARIA helpers                                                    */
    /* ---------------------------------------------------------------- */

    /** Add an aria-description (or aria-describedby with hidden span) */
    _addDescription(inputEl, text) {
      // Prefer aria-description (ARIA 1.3) with describedby fallback
      var descId = inputEl.id + '_rangedesc';
      var span = document.createElement('span');
      span.id = descId;
      span.className = 'visually-hidden';
      span.textContent = text;
      inputEl.parentNode.appendChild(span);

      var existing = inputEl.getAttribute('aria-describedby');
      inputEl.setAttribute(
        'aria-describedby',
        existing ? existing + ' ' + descId : descId
      );
    }

    /** Announce a message via the live region */
    _announce(msg) {
      var lr = this.liveRegion;
      lr.textContent = '';
      if (msg) {
        setTimeout(function () { lr.textContent = msg; }, 150);
      }
    }

    /* ---------------------------------------------------------------- */
    /*  Synchronisation between start / end pickers                    */
    /* ---------------------------------------------------------------- */

    _setupSync() {
      var self = this;

      // --- Start date changes → constrain end picker ---
      this.inputs[0].addEventListener('changeDate', function (e) {
        var startDate = e.detail.date;
        var endDialog = self.datepickers[1].datepicker;

        endDialog.options.minDate = startDate;

        // Clear end if it violates the new constraint
        if (startDate && endDialog.selectedDate) {
          var ok = self.options.allowSameDay
            ? midnight(endDialog.selectedDate) >= midnight(startDate)
            : midnight(endDialog.selectedDate) > midnight(startDate);
          if (!ok) self.datepickers[1].setDate(null);
        }

        if (endDialog.isOpen) endDialog._updateGrid();

        // Screen reader announcement
        if (startDate) {
          var formatted = startDate.toLocaleDateString(
            self.options.language || navigator.language
          );
          var lang = (self.options.language) || navigator.language.split('-')[0];
          self._announce(
            lang === 'fr'
              ? 'Date de début sélectionnée : ' + formatted
              : 'Start date selected: ' + formatted
          );
        }

        // Dispatch a synthetic range event on the wrapper
        self.element.dispatchEvent(new CustomEvent('rangeDateChange', {
          bubbles: true,
          detail: { dates: self.getDates(), origin: 'start' }
        }));
      });

      // --- End date changes → constrain start picker ---
      this.inputs[1].addEventListener('changeDate', function (e) {
        var endDate = e.detail.date;
        var startDialog = self.datepickers[0].datepicker;

        startDialog.options.maxDate = endDate;

        // Clear start if it violates the new constraint
        if (endDate && startDialog.selectedDate) {
          var ok = self.options.allowSameDay
            ? midnight(startDialog.selectedDate) <= midnight(endDate)
            : midnight(startDialog.selectedDate) < midnight(endDate);
          if (!ok) self.datepickers[0].setDate(null);
        }

        if (startDialog.isOpen) startDialog._updateGrid();

        if (endDate) {
          var formatted = endDate.toLocaleDateString(
            self.options.language || navigator.language
          );
          var lang = (self.options.language) || navigator.language.split('-')[0];
          self._announce(
            lang === 'fr'
              ? 'Date de fin sélectionnée : ' + formatted
              : 'End date selected: ' + formatted
          );
        }

        self.element.dispatchEvent(new CustomEvent('rangeDateChange', {
          bubbles: true,
          detail: { dates: self.getDates(), origin: 'end' }
        }));
      });
    }

    /* ---------------------------------------------------------------- */
    /*  Range visual highlight on grid cells                           */
    /* ---------------------------------------------------------------- */

    _setupRangeHighlight() {
      var self = this;
      for (var i = 0; i < 2; i++) {
        var dialog = this.datepickers[i].datepicker;
        var origUpdate = dialog._updateGrid.bind(dialog);
        // Wrap _updateGrid to apply highlight after each render
        dialog._updateGrid = (function (orig, dlg) {
          return function () {
            orig();
            self._applyHighlight(dlg);
          };
        })(origUpdate, dialog);
      }
    }

    _applyHighlight(dialog) {
      var startDate = this.datepickers[0].datepicker.selectedDate;
      var endDate   = this.datepickers[1].datepicker.selectedDate;

      var s = startDate ? midnight(startDate).getTime() : null;
      var e = endDate   ? midnight(endDate).getTime()   : null;

      dialog.cells.forEach(function (cell) {
        cell.classList.remove('range-start', 'range-end', 'in-range');
        if (!cell._date || s === null || e === null || s >= e) return;

        var t = midnight(cell._date).getTime();
        if (t === s)                cell.classList.add('range-start');
        else if (t === e)           cell.classList.add('range-end');
        else if (t > s && t < e)    cell.classList.add('in-range');
      });
    }

    /* ---------------------------------------------------------------- */
    /*  Public API                                                     */
    /* ---------------------------------------------------------------- */

    /**
     * Get the currently selected dates.
     * @param  {string} [fmt]  Optional format string (forwarded to getDate)
     * @return {Array}         [startDate, endDate] – Date objects or formatted strings
     */
    getDates(fmt) {
      return [
        this.datepickers[0].getDate(fmt),
        this.datepickers[1].getDate(fmt)
      ];
    }

    /**
     * Programmatically set both dates.
     * Pass undefined to leave a date unchanged, null to clear it.
     * @param {Date|string|null|undefined} start
     * @param {Date|string|null|undefined} end
     */
    setDates(start, end) {
      if (start !== undefined) this.datepickers[0].setDate(start);
      if (end   !== undefined) this.datepickers[1].setDate(end);
      // Re-apply constraints after programmatic set
      if (start !== undefined) {
        var s = this.datepickers[0].datepicker.selectedDate;
        this.datepickers[1].datepicker.options.minDate = s;
      }
      if (end !== undefined) {
        var e = this.datepickers[1].datepicker.selectedDate;
        this.datepickers[0].datepicker.options.maxDate = e;
      }
    }

    /** Clean up everything */
    destroy() {
      this.datepickers.forEach(function (dp) { dp.destroy(); });
      if (this.liveRegion.parentNode) {
        this.liveRegion.parentNode.removeChild(this.liveRegion);
      }
      this.element.removeAttribute('role');
      this.element.removeAttribute('aria-label');
    }
  }

  /* ------------------------------------------------------------------ */
  /*  Export                                                            */
  /* ------------------------------------------------------------------ */
  root.DateRangePickerA11y = DateRangePickerA11y;

  // Backward-compat alias: code calling `new DateRangePicker(...)` will
  // transparently use the accessible version.
  root.DateRangePicker = DateRangePickerA11y;

})(typeof window !== 'undefined' ? window : this);
