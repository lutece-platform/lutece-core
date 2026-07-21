/**
 * Accessible Datepicker Dialog
 * Based on W3C WAI-ARIA APG pattern:
 * https://www.w3.org/WAI/ARIA/apg/patterns/dialog-modal/examples/datepicker-dialog/
 *
 * Drop-in replacement for vanillajs-datepicker, compatible with themeDatepicker API.
 */
(function (root) {
  'use strict';

  /* ------------------------------------------------------------------ */
  /*  Helpers                                                           */
  /* ------------------------------------------------------------------ */
  const DAYS_IN_MONTH = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
  function isLeapYear(y) { return (y % 4 === 0 && y % 100 !== 0) || y % 400 === 0; }
  function daysInMonth(y, m) { return m === 1 && isLeapYear(y) ? 29 : DAYS_IN_MONTH[m]; }

  /** Parse locale-formatted date string to Date using a format pattern (dd/mm/yyyy etc.) */
  function parseLocaleDate(str, fmt) {
    if (!str) return null;
    const sep = fmt.replace(/[a-zA-Z]/g, '').charAt(0) || '/';
    const fmtParts = fmt.toLowerCase().split(sep);
    const valParts = str.split(sep);
    if (valParts.length !== fmtParts.length) return null;
    let d = 1, m = 0, y = 2000;
    fmtParts.forEach(function (p, i) {
      const v = parseInt(valParts[i], 10);
      if (isNaN(v)) return;
      if (p.startsWith('d')) d = v;
      else if (p.startsWith('m')) m = v - 1;
      else if (p.startsWith('y')) y = v < 100 ? 2000 + v : v;
    });
    const dt = new Date(y, m, d);
    return isNaN(dt.getTime()) ? null : dt;
  }

  /** Format Date to locale string using format pattern */
  function formatDate(dt, fmt) {
    if (!dt) return '';
    const sep = fmt.replace(/[a-zA-Z]/g, '').charAt(0) || '/';
    const parts = fmt.toLowerCase().split(sep);
    return parts.map(function (p) {
      if (p.startsWith('dd')) return String(dt.getDate()).padStart(2, '0');
      if (p.startsWith('d')) return String(dt.getDate());
      if (p.startsWith('mm')) return String(dt.getMonth() + 1).padStart(2, '0');
      if (p.startsWith('m')) return String(dt.getMonth() + 1);
      if (p.startsWith('yyyy') || p === 'y') return String(dt.getFullYear());
      if (p.startsWith('yy')) return String(dt.getFullYear()).slice(-2);
      return p;
    }).join(sep);
  }

  /** Format Date for the hidden server field (ISO-like) */
  function formatISO(dt, dataFmt) {
    if (!dt) return '';
    if (!dataFmt) return dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate() + ' 00:00:00';
    // Extract the date-only pattern and any literal suffix (e.g. "yyyy-m-d 00:00:00" → pattern "yyyy-m-d", suffix " 00:00:00")
    const match = dataFmt.match(/^([a-zA-Z\-\/.]+)(.*)/);
    if (!match) return formatDate(dt, dataFmt);
    return formatDate(dt, match[1]) + match[2];
  }

  /** Compare two dates (day-level) */
  function sameDay(a, b) {
    return a && b && a.getFullYear() === b.getFullYear() && a.getMonth() === b.getMonth() && a.getDate() === b.getDate();
  }

  /** Get locale day/month names via Intl */
  function getLocaleData(lang) {
    const loc = lang || navigator.language || 'fr';
    const dayNarrow = [], dayShort = [], dayLong = [];
    const monthLong = [], monthShort = [];
    for (let i = 0; i < 7; i++) {
      // 2024-01-07 is a Sunday
      const d = new Date(2024, 0, 7 + i);
      dayNarrow.push(d.toLocaleDateString(loc, { weekday: 'narrow' }));
      dayShort.push(d.toLocaleDateString(loc, { weekday: 'short' }));
      dayLong.push(d.toLocaleDateString(loc, { weekday: 'long' }));
    }
    for (let i = 0; i < 12; i++) {
      const d = new Date(2024, i, 1);
      monthLong.push(d.toLocaleDateString(loc, { month: 'long' }));
      monthShort.push(d.toLocaleDateString(loc, { month: 'short' }));
    }
    return { dayNarrow, dayShort, dayLong, monthLong, monthShort };
  }

  /* ------------------------------------------------------------------ */
  /*  DatepickerDialog                                                  */
  /* ------------------------------------------------------------------ */
  class DatepickerDialog {
    constructor(inputEl, options) {
      const lang = (options && options.language) || navigator.language.split('-')[0];
      const loc = resolveLocale(lang);

      this.options = Object.assign({
        format: loc.format || 'dd/mm/yyyy',
        dataFormat: 'yyyy-m-d 00:00:00',
        language: lang,
        weekStart: loc.weekStart != null ? loc.weekStart : 1,
        minDate: null,
        maxDate: null,
        todayHighlight: true,
        autohide: true,
        clearButton: true,
        todayButton: false,
        orientation: 'auto',
        enableReadOnly: true,
        prevArrow: '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="15 6 9 12 15 18"/></svg>',
        nextArrow: '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="9 6 15 12 9 18"/></svg>',
        title: '',
        labelToday: loc.today,
        labelClear: loc.clear,
        labelChooseDate: loc.chooseDate,
        labelPrevYear: loc.prevYear,
        labelPrevMonth: loc.prevMonth,
        labelNextMonth: loc.nextMonth,
        labelNextYear: loc.nextYear,
        labelOk: loc.ok,
        labelKeyboardHelp: loc.keyboardHelp,
      }, options);

      // Use locale day/month names if available, otherwise fallback to Intl
      this.locale = (loc.days && loc.months)
        ? { dayLong: loc.days, dayShort: loc.daysShort || loc.days, dayNarrow: loc.daysMin || loc.days, monthLong: loc.months, monthShort: loc.monthsShort || loc.months }
        : getLocaleData(this.options.language);
      this.inputEl = inputEl;
      this.selectedDate = null;
      this.focusedDate = null;
      this.lastDayOfMonth = 0; // remember day across month changes
      this.isOpen = false;

      // Parse initial value
      if (inputEl.value) {
        this.selectedDate = parseLocaleDate(inputEl.value, this.options.format);
      }
      this.focusedDate = this.selectedDate ? new Date(this.selectedDate) : new Date();

      this._buildDOM();
      this._attachInputEvents();
    }

    /* ---- DOM Construction ---- */
    _buildDOM() {
      const o = this.options;

      // Wrapper
      this.dialog = document.createElement('div');
      this.dialog.className = 'datepicker datepicker-dropdown';
      this.dialog.setAttribute('role', 'dialog');
      this.dialog.setAttribute('aria-modal', 'true');
      this.dialog.setAttribute('aria-label', o.title || o.labelChooseDate);
      this.dialog.style.display = 'none';
      // Fixed + portalled to <body> so the open calendar always renders above
      // every other element (sticky/fixed banners, overlays) and is never
      // clipped by an ancestor stacking context or overflow.
      this.dialog.style.position = 'fixed';
      this.dialog.style.zIndex = '1055';

      const picker = document.createElement('div');
      picker.className = 'datepicker-picker';
      this.dialog.appendChild(picker);

      // --- Header ---
      const header = document.createElement('div');
      header.className = 'datepicker-header';
      picker.appendChild(header);

      const controls = document.createElement('div');
      controls.className = 'datepicker-controls';
      header.appendChild(controls);

      this.prevYearBtn = this._createNavBtn('prev-button prev-year', o.prevArrow, o.labelPrevYear);
      controls.appendChild(this.prevYearBtn);

      this.prevMonthBtn = this._createNavBtn('prev-button', o.prevArrow, o.labelPrevMonth);
      controls.appendChild(this.prevMonthBtn);

      this.viewSwitch = document.createElement('span');
      this.viewSwitch.className = 'btn view-switch';
      this.viewSwitch.setAttribute('aria-live', 'polite');
      this.viewSwitch.setAttribute('role', 'heading');
      this.viewSwitch.setAttribute('aria-level', '2');
      controls.appendChild(this.viewSwitch);

      this.nextMonthBtn = this._createNavBtn('next-button', o.nextArrow, o.labelNextMonth);
      controls.appendChild(this.nextMonthBtn);

      this.nextYearBtn = this._createNavBtn('next-button next-year', o.nextArrow, o.labelNextYear);
      controls.appendChild(this.nextYearBtn);

      // --- Main (grid) ---
      const main = document.createElement('div');
      main.className = 'datepicker-main';
      picker.appendChild(main);

      const view = document.createElement('div');
      view.className = 'datepicker-view days';
      main.appendChild(view);

      // Days of week header
      const dowRow = document.createElement('div');
      dowRow.className = 'days-of-week';
      view.appendChild(dowRow);
      const ws = this.options.weekStart;
      for (let i = 0; i < 7; i++) {
        const di = (ws + i) % 7;
        const span = document.createElement('span');
        span.className = 'dow';
        span.setAttribute('aria-label', this.locale.dayLong[di]);
        span.textContent = this.locale.dayShort[di];
        dowRow.appendChild(span);
      }

      // Grid
      this.grid = document.createElement('div');
      this.grid.className = 'datepicker-grid';
      this.grid.setAttribute('role', 'grid');
      view.appendChild(this.grid);

      // Create 42 cells (6 rows x 7 cols)
      this.cells = [];
      for (let r = 0; r < 6; r++) {
        const row = document.createElement('span');
        row.className = 'datepicker-row';
        row.setAttribute('role', 'row');
        this.grid.appendChild(row);
        for (let c = 0; c < 7; c++) {
          const cell = document.createElement('span');
          cell.className = 'datepicker-cell day';
          cell.setAttribute('role', 'gridcell');
          cell.setAttribute('tabindex', '-1');
          cell.dataset.row = r;
          cell.dataset.col = c;
          row.appendChild(cell);
          this.cells.push(cell);
        }
      }

      // --- Footer ---
      const footer = document.createElement('div');
      footer.className = 'datepicker-footer';
      picker.appendChild(footer);

      const footControls = document.createElement('div');
      footControls.className = 'datepicker-controls';
      footer.appendChild(footControls);

      if (o.todayButton) {
        this.todayBtn = document.createElement('button');
        this.todayBtn.type = 'button';
        this.todayBtn.className = 'btn today-button';
        this.todayBtn.textContent = o.labelToday;
        footControls.appendChild(this.todayBtn);
      }

      // OK button — always present
      this.okBtn = document.createElement('button');
      this.okBtn.type = 'button';
      this.okBtn.className = 'btn btn-sm ok-button';
      this.okBtn.textContent = o.labelOk;
      footControls.appendChild(this.okBtn);

      if (o.clearButton) {
        this.clearBtn = document.createElement('button');
        this.clearBtn.type = 'button';
        this.clearBtn.className = 'btn btn-sm clear-button';
        this.clearBtn.textContent = o.labelClear;
        footControls.appendChild(this.clearBtn);
      }

      // Keyboard help text
      const helpText = document.createElement('p');
      helpText.className = 'datepicker-help';
      helpText.textContent = o.labelKeyboardHelp;
      footer.appendChild(helpText);

      // Live region for screen reader messages
      this.liveRegion = document.createElement('div');
      this.liveRegion.className = 'visually-hidden';
      this.liveRegion.setAttribute('aria-live', 'polite');
      picker.appendChild(this.liveRegion);

      // Portal the dialog to <body>: keeps it out of any ancestor stacking
      // context / overflow so it can be positioned freely against the viewport
      // and always painted on top. The input's parent stays relative for the
      // input-group add-on layout.
      this.inputEl.parentNode.style.position = 'relative';
      document.body.appendChild(this.dialog);

      this._attachDialogEvents();
    }

    _createNavBtn(className, innerHTML, ariaLabel) {
      const btn = document.createElement('button');
      btn.type = 'button';
      btn.className = 'btn ' + className;
      btn.innerHTML = innerHTML;
      btn.setAttribute('aria-label', ariaLabel);
      return btn;
    }

    /* ---- Event Binding ---- */
    _attachInputEvents() {
      // Restrict manual typing to digits and date separators (/ - .) and space.
      // beforeinput covers typing, paste, drop and autocomplete in one place.
      const allowedChar = /^[0-9/\-.\s]*$/;
      this.inputEl.addEventListener('beforeinput', (e) => {
        // insertText / insertFromPaste / insertFromDrop all carry the text in e.data
        if (e.data != null && !allowedChar.test(e.data)) {
          e.preventDefault();
        }
      });
      // Fallback for browsers/inputs that don't fire beforeinput data: filter keypress
      this.inputEl.addEventListener('keypress', (e) => {
        if (e.ctrlKey || e.metaKey || e.altKey || e.key.length > 1) return; // let control keys through
        if (!allowedChar.test(e.key)) e.preventDefault();
      });

      // Click on input or icon opens dialog. Focus stays in the input so the
      // user can type the date manually; ArrowDown moves focus to the grid.
      const openHandler = (e) => {
        if (this._suppressOpen) return;
        if (this.inputEl.readOnly && !this.options.enableReadOnly) return;
        if (!this.isOpen) this.open(false);
      };
      this.inputEl.addEventListener('click', openHandler);
      this.inputEl.addEventListener('focus', openHandler);

      // Arrow Down on input opens the dialog and moves focus into the grid
      this.inputEl.addEventListener('keydown', (e) => {
        if (e.key === 'ArrowDown') {
          e.preventDefault();
          if (this.isOpen) this._setGridFocus(); else this.open();
        }
      });

      // Click on the addon icon
      const addon = this.inputEl.parentNode.querySelector('.input-group-text');
      if (addon) {
        addon.style.cursor = 'pointer';
        addon.addEventListener('click', (e) => {
          e.preventDefault();
          if (this.isOpen) this.close(); else this.open();
        });
      }

      // Close on outside click
      this._onDocClick = (e) => {
        if (!this.isOpen) return;
        if (this.dialog.contains(e.target) || this.inputEl.contains(e.target)) return;
        if (addon && addon.contains(e.target)) return;
        this.close();
      };
      document.addEventListener('pointerdown', this._onDocClick);

      // Close on Escape from anywhere (input, document)
      this._onDocKeydown = (e) => {
        if (this.isOpen && e.key === 'Escape') {
          e.preventDefault();
          this.close();
        }
      };
      document.addEventListener('keydown', this._onDocKeydown);
    }

    _attachDialogEvents() {
      // Navigation buttons
      this.prevYearBtn.addEventListener('click', () => this._changeMonth(-12));
      this.prevMonthBtn.addEventListener('click', () => this._changeMonth(-1));
      this.nextMonthBtn.addEventListener('click', () => this._changeMonth(1));
      this.nextYearBtn.addEventListener('click', () => this._changeMonth(12));

      // Clicking a non-interactive area of the dialog must not blur the input
      this.dialog.addEventListener('mousedown', (e) => {
        if (!e.target.closest('button, .datepicker-cell')) e.preventDefault();
      });

      // Grid clicks
      this.grid.addEventListener('click', (e) => {
        const cell = e.target.closest('.datepicker-cell');
        if (!cell || cell.classList.contains('disabled')) return;
        this._selectCell(cell);
      });

      // Grid keyboard
      this.grid.addEventListener('keydown', (e) => this._handleGridKeydown(e));

      // Focus trap keyboard on the whole dialog
      this.dialog.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') { e.preventDefault(); this.close(); return; }
        if (e.key === 'Tab') this._handleTabTrap(e);
      });

      // Footer buttons
      if (this.todayBtn) {
        this.todayBtn.addEventListener('click', () => {
          this.focusedDate = new Date();
          this.selectedDate = new Date();
          this._updateInput();
          if (this.options.autohide) this.close(); else this._updateGrid();
        });
      }
      // OK button — validate focused date and close
      this.okBtn.addEventListener('click', () => {
        this._selectFocused();
        this.close();
      });

      if (this.clearBtn) {
        this.clearBtn.addEventListener('click', () => {
          this.selectedDate = null;
          this.inputEl.value = '';
          this.inputEl.dispatchEvent(new Event('change', { bubbles: true }));
          this.inputEl.dispatchEvent(new CustomEvent('changeDate', { detail: { date: null } }));
          this.close();
        });
      }
    }

    /* ---- Tab trapping ---- */
    _getFocusableElements() {
      const els = [];
      els.push(this.prevYearBtn, this.prevMonthBtn, this.nextMonthBtn, this.nextYearBtn);
      // The focused grid cell
      const focusedCell = this.grid.querySelector('[tabindex="0"]');
      if (focusedCell) els.push(focusedCell);
      if (this.todayBtn) els.push(this.todayBtn);
      els.push(this.okBtn);
      if (this.clearBtn) els.push(this.clearBtn);
      return els;
    }

    _handleTabTrap(e) {
      const focusable = this._getFocusableElements();
      if (focusable.length === 0) return;
      const first = focusable[0];
      const last = focusable[focusable.length - 1];

      if (e.shiftKey) {
        if (document.activeElement === first || !this.dialog.contains(document.activeElement)) {
          e.preventDefault();
          last.focus();
        }
      } else {
        if (document.activeElement === last) {
          e.preventDefault();
          first.focus();
        }
      }
    }

    /* ---- Grid Keyboard Navigation ---- */
    _handleGridKeydown(e) {
      const fd = this.focusedDate;
      let handled = true;

      switch (e.key) {
        case 'ArrowRight': this._moveFocus(1); break;
        case 'ArrowLeft':  this._moveFocus(-1); break;
        case 'ArrowDown':  this._moveFocus(7); break;
        case 'ArrowUp':    this._moveFocus(-7); break;
        case 'Home':       this._moveFocusToWeekEdge(true); break;
        case 'End':        this._moveFocusToWeekEdge(false); break;
        case 'PageUp':
          if (e.shiftKey) this._changeMonth(-12, true);
          else this._changeMonth(-1, true);
          break;
        case 'PageDown':
          if (e.shiftKey) this._changeMonth(12, true);
          else this._changeMonth(1, true);
          break;
        case 'Enter':
        case ' ':
          e.preventDefault();
          this._selectFocused();
          return;
        default:
          handled = false;
      }
      if (handled) e.preventDefault();
    }

    _moveFocus(days) {
      const d = new Date(this.focusedDate);
      d.setDate(d.getDate() + days);
      if (this._isDisabled(d)) return;
      this.focusedDate = d;
      this._updateGrid();
      this._setGridFocus();
    }

    _moveFocusToWeekEdge(start) {
      const d = new Date(this.focusedDate);
      const day = d.getDay();
      const ws = this.options.weekStart;
      if (start) {
        const diff = (day - ws + 7) % 7;
        d.setDate(d.getDate() - diff);
      } else {
        const diff = (6 - (day - ws + 7) % 7);
        d.setDate(d.getDate() + diff);
      }
      if (!this._isDisabled(d)) {
        this.focusedDate = d;
        this._updateGrid();
        this._setGridFocus();
      }
    }

    /* ---- Month Navigation ---- */
    _changeMonth(offset, keepDay) {
      const d = this.focusedDate;
      if (keepDay && this.lastDayOfMonth === 0) {
        this.lastDayOfMonth = d.getDate();
      }
      const newMonth = d.getMonth() + offset;
      const newDate = new Date(d.getFullYear(), newMonth, 1);
      const targetDay = keepDay ? this.lastDayOfMonth : d.getDate();
      const maxDay = daysInMonth(newDate.getFullYear(), newDate.getMonth());
      newDate.setDate(Math.min(targetDay, maxDay));
      this.focusedDate = newDate;
      this._updateGrid();
      if (keepDay) this._setGridFocus();
    }

    /* ---- Grid Rendering ---- */
    _updateGrid() {
      const fd = this.focusedDate;
      const y = fd.getFullYear();
      const m = fd.getMonth();

      // Update header
      const monthName = this.locale.monthLong[m];
      this.viewSwitch.textContent = monthName.charAt(0).toUpperCase() + monthName.slice(1) + ' ' + y;

      // Compute first cell date
      const firstOfMonth = new Date(y, m, 1);
      const ws = this.options.weekStart;
      let startDay = (firstOfMonth.getDay() - ws + 7) % 7;
      const startDate = new Date(y, m, 1 - startDay);

      const today = new Date();
      const minDate = this.options.minDate ? new Date(this.options.minDate) : null;
      const maxDate = this.options.maxDate ? new Date(this.options.maxDate) : null;

      let hideSixthRow = true;

      for (let i = 0; i < 42; i++) {
        const cell = this.cells[i];
        const cellDate = new Date(startDate);
        cellDate.setDate(startDate.getDate() + i);

        cell.textContent = cellDate.getDate();
        cell._date = new Date(cellDate);

        // Reset classes
        cell.className = 'datepicker-cell day';

        // Outside current month
        if (cellDate.getMonth() < m || (cellDate.getFullYear() < y)) {
          cell.classList.add('prev');
        } else if (cellDate.getMonth() > m || (cellDate.getFullYear() > y)) {
          cell.classList.add('next');
        }

        // Today
        if (this.options.todayHighlight && sameDay(cellDate, today)) {
          cell.classList.add('today');
        }

        // Selected
        if (sameDay(cellDate, this.selectedDate)) {
          cell.classList.add('selected');
          cell.setAttribute('aria-selected', 'true');
        } else {
          cell.removeAttribute('aria-selected');
        }

        // Focused
        if (sameDay(cellDate, fd)) {
          cell.classList.add('focused');
          cell.setAttribute('tabindex', '0');
        } else {
          cell.setAttribute('tabindex', '-1');
        }

        // Disabled (min/max)
        const disabled = (minDate && cellDate < minDate && !sameDay(cellDate, minDate))
                      || (maxDate && cellDate > maxDate && !sameDay(cellDate, maxDate));
        if (disabled) {
          cell.classList.add('disabled');
        }

        // Check if 6th row needed
        if (i >= 35 && cellDate.getMonth() === m) {
          hideSixthRow = false;
        }
      }

      // Show/hide 6th row
      const rows = this.grid.querySelectorAll('.datepicker-row');
      if (rows[5]) {
        rows[5].style.display = hideSixthRow ? 'none' : '';
      }
    }

    _setGridFocus() {
      const focused = this.grid.querySelector('[tabindex="0"]');
      if (focused) focused.focus();
      this._announceDate();
    }

    _announceDate() {
      const d = this.focusedDate;
      const dayName = this.locale.dayLong[d.getDay()];
      const monthName = this.locale.monthLong[d.getMonth()];
      this.liveRegion.textContent = '';
      setTimeout(() => {
        this.liveRegion.textContent = dayName + ' ' + d.getDate() + ' ' + monthName + ' ' + d.getFullYear();
      }, 150);
    }

    _isDisabled(d) {
      const min = this.options.minDate ? new Date(this.options.minDate) : null;
      const max = this.options.maxDate ? new Date(this.options.maxDate) : null;
      if (min && d < min && !sameDay(d, min)) return true;
      if (max && d > max && !sameDay(d, max)) return true;
      return false;
    }

    /* ---- Selection ---- */
    _selectCell(cell) {
      if (!cell._date) return;
      this.selectedDate = new Date(cell._date);
      this.focusedDate = new Date(cell._date);
      this.lastDayOfMonth = 0;
      this._updateInput();
      if (this.options.autohide) this.close(); else this._updateGrid();
    }

    _selectFocused() {
      this.selectedDate = new Date(this.focusedDate);
      this.lastDayOfMonth = 0;
      this._updateInput();
      if (this.options.autohide) this.close();
    }

    _updateInput() {
      this.inputEl.value = formatDate(this.selectedDate, this.options.format);
      this.inputEl.dispatchEvent(new Event('change', { bubbles: true }));
      this.inputEl.dispatchEvent(new CustomEvent('changeDate', {
        detail: { date: this.selectedDate }
      }));
      this._updateGrid();
    }

    /* ---- Open / Close ---- */
    open(focusGrid) {
      if (this.isOpen) return;
      this.isOpen = true;

      // Re-parse input value
      if (this.inputEl.value) {
        const parsed = parseLocaleDate(this.inputEl.value, this.options.format);
        if (parsed) {
          this.selectedDate = parsed;
          this.focusedDate = new Date(parsed);
        }
      }
      if (!this.focusedDate || isNaN(this.focusedDate.getTime())) {
        this.focusedDate = new Date();
      }
      this.lastDayOfMonth = 0;

      this._updateGrid();
      this.dialog.style.display = '';
      this._positionDialog();

      // The dialog is position:fixed, so it does not follow the field on scroll.
      // Close it on scroll/resize to avoid it detaching from the input.
      if (!this._onViewportChange) {
        this._onViewportChange = (e) => {
          if (e && e.target && this.dialog.contains(e.target)) return; // ignore inner scroll
          this.close();
        };
      }
      window.addEventListener('scroll', this._onViewportChange, true);
      window.addEventListener('resize', this._onViewportChange);

      // Focus the selected/focused cell after a frame, unless the dialog was
      // opened from the input itself (the user may want to type the date)
      if (focusGrid !== false) requestAnimationFrame(() => this._setGridFocus());
    }

    close(refocusInput) {
      if (!this.isOpen) return;
      this.isOpen = false;
      this.dialog.style.display = 'none';
      if (this._onViewportChange) {
        window.removeEventListener('scroll', this._onViewportChange, true);
        window.removeEventListener('resize', this._onViewportChange);
      }
      if (refocusInput === false) return;
      // Temporarily suppress re-open when focus returns to input
      this._suppressOpen = true;
      this.inputEl.focus();
      setTimeout(() => { this._suppressOpen = false; }, 100);
    }

    _positionDialog() {
      // The dialog is position:fixed and portalled to <body>, so it is placed
      // in viewport coordinates against the input group's on-screen rectangle.
      const rect = this.inputEl.parentNode.getBoundingClientRect();
      const margin = 4;
      // Dialog is already displayed here, so this measures its real size.
      const dRect = this.dialog.getBoundingClientRect();

      // Horizontal: align to the input group's left edge, clamped to the viewport.
      let left = rect.left;
      const maxLeft = window.innerWidth - dRect.width - margin;
      if (left > maxLeft) left = maxLeft;
      if (left < margin) left = margin;
      this.dialog.style.left = left + 'px';

      // Vertical: always open below the field, unless it does not fit below AND
      // it fully fits above. This keeps the calendar under the field even when
      // the space below is not totally sufficient. Since the dialog is painted
      // above every other element, it is never hidden behind a banner.
      const spaceBelow = window.innerHeight - rect.bottom;
      const spaceAbove = rect.top;
      const fitsBelow = dRect.height + margin <= spaceBelow;
      const fitsAbove = dRect.height + margin <= spaceAbove;
      if (!fitsBelow && fitsAbove) {
        this.dialog.style.top = rect.top - dRect.height - margin + 'px';
      } else {
        this.dialog.style.top = rect.bottom + 'px';
      }
    }

    /* ---- Public API (compat with vanillajs-datepicker) ---- */
    getDate(fmt) {
      if (!this.selectedDate) return '';
      if (fmt) return formatDate(this.selectedDate, fmt);
      return this.selectedDate;
    }

    setDate(dateOrStr, triggerChange) {
      if (typeof dateOrStr === 'string') {
        this.selectedDate = parseLocaleDate(dateOrStr, this.options.format) || new Date(dateOrStr);
      } else if (dateOrStr instanceof Date) {
        this.selectedDate = new Date(dateOrStr);
      } else {
        this.selectedDate = null;
      }
      if (this.selectedDate) {
        this.focusedDate = new Date(this.selectedDate);
        this.inputEl.value = formatDate(this.selectedDate, this.options.format);
      } else {
        this.inputEl.value = '';
      }
      if (triggerChange !== false) {
        this.inputEl.dispatchEvent(new Event('change', { bubbles: true }));
        this.inputEl.dispatchEvent(new CustomEvent('changeDate', { detail: { date: this.selectedDate } }));
      }
      if (this.isOpen) this._updateGrid();
    }

    destroy() {
      document.removeEventListener('pointerdown', this._onDocClick);
      document.removeEventListener('keydown', this._onDocKeydown);
      if (this.dialog.parentNode) this.dialog.parentNode.removeChild(this.dialog);
    }

    /** Static format helper (compat with Datepicker.formatDate) */
    static formatDate(date, fmt, lang) {
      return formatDate(date, fmt);
    }
  }

  /* ------------------------------------------------------------------ */
  /*  themeDatepicker (drop-in replacement)                             */
  /* ------------------------------------------------------------------ */
  class themeDatepickerA11y {
    constructor(element, options) {
      this.originalElement = element;
      this.originalElementType = element.type;
      const fmt = options.format || 'dd/mm/yyyy';

      // Clone input for display, make original hidden (stores ISO value)
      const dateInput = element.cloneNode(true);
      dateInput.removeAttribute('name');

      if (element.value) {
        const parsed = parseLocaleDate(element.value, fmt);
        if (parsed) {
          dateInput.value = formatDate(parsed, fmt);
        } else {
          // Try ISO parse
          const d = new Date(element.value);
          if (!isNaN(d.getTime())) dateInput.value = formatDate(d, fmt);
        }
      }

      element.type = 'hidden';
      element.id = element.id + '_hidden';
      element.after(dateInput);

      // Create the accessible datepicker on the display input
      this.datepicker = new DatepickerDialog(dateInput, options);
      this.element = dateInput;

      // Sync display -> hidden (ISO)
      if (element.value) {
        const parsed = parseLocaleDate(dateInput.value, fmt);
        if (parsed) element.value = formatISO(parsed, options.dataFormat);
      }

      dateInput.addEventListener('changeDate', () => {
        const d = this.datepicker.selectedDate;
        element.value = d ? formatISO(d, options.dataFormat) : '';
        element.dispatchEvent(new Event('change', { bubbles: true }));
      });

      // Sync manually typed dates too: only a strictly valid value (format
      // round-trip unchanged) is synced, otherwise the hidden value is cleared
      dateInput.addEventListener('input', () => {
        const parsed = parseLocaleDate(dateInput.value, fmt);
        const ok = parsed && formatDate(parsed, fmt) === dateInput.value;
        this.datepicker.selectedDate = ok ? parsed : null;
        element.value = ok ? formatISO(parsed, options.dataFormat) : '';
      });
    }

    getDate(fmt) { return this.datepicker.getDate(fmt); }
    setDate(v, t) { this.datepicker.setDate(v, t); }

    destroy() {
      this.datepicker.destroy();
      this.element.remove();
      this.originalElement.type = this.originalElementType;
      this.originalElement.id = this.originalElement.id.replace(/_hidden$/, '');
      return this;
    }
  }

  /* ------------------------------------------------------------------ */
  /*  Locale registry (compatible with vanillajs-datepicker locale files)*/
  /* ------------------------------------------------------------------ */
  const locales = {
    en: {
      days: ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'],
      daysShort: ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'],
      daysMin: ['Su','Mo','Tu','We','Th','Fr','Sa'],
      months: ['January','February','March','April','May','June','July','August','September','October','November','December'],
      monthsShort: ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'],
      today: 'Today',
      clear: 'Clear',
      ok: 'OK',
      chooseDate: 'Choose Date',
      prevYear: 'Previous year',
      prevMonth: 'Previous month',
      nextMonth: 'Next month',
      nextYear: 'Next year',
      keyboardHelp: 'Arrows: navigate days/weeks. Page Up/Down: month. Shift+Page: year. Enter: select. Escape: close.',
      weekStart: 0,
      format: 'mm/dd/yyyy'
    }
  };

  /** Resolve locale object: merge registered locale with English fallback */
  function resolveLocale(lang) {
    const base = locales.en;
    const loc = locales[lang] || locales[lang.split('-')[0]] || {};
    return Object.assign({}, base, loc);
  }

  /* ------------------------------------------------------------------ */
  /*  Export                                                            */
  /* ------------------------------------------------------------------ */
  root.DatepickerDialog = DatepickerDialog;
  root.themeDatepickerA11y = themeDatepickerA11y;

  // Expose Datepicker.locales so existing locale files (fr.js, de.js, etc.) can register
  if (!root.Datepicker) root.Datepicker = {};
  root.Datepicker.locales = locales;
  // Static format helper for compat
  root.Datepicker.formatDate = DatepickerDialog.formatDate;

})(typeof window !== 'undefined' ? window : this);
