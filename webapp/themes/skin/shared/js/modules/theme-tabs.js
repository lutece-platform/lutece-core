/*
 *   This content is inspired by the W3C Software  - License at   https://www.w3.org/Consortium/Legal/2015/copyright-software-and-document
 *
 *   File:   theme-tabs.js
 *
 *   Desc:   Tab list widget that implements ARIA Authoring Practices
 */
'use strict';
class ThemeTabs{
  constructor( listNode ) {
    this.tabList = listNode;

    this.allTabs = [];

    this.firstTab = null;
    this.lastTab = null;

    this.allTabs = Array.from( this.tabList.querySelectorAll('[role=tab]') );
    this.tabPanels = [];

    for ( let tab of this.allTabs) {
      let tabPanel = document.getElementById(tab.getAttribute('aria-controls'));
    
      this.tabPanels.push( tabPanel );

      tab.addEventListener('click', this.onTabClick.bind(this));
      
      if (!this.firstTab) {
        this.firstTab = tab;
      }
      this.lastTab = tab;
    }

    // Listen in the capture phase on the tablist, so the arrow keys never reach the
    // Bootstrap Tab handler of the button, which would show the panel of the focused tab:
    // moving with the arrows only moves the focus, Enter or Space activates the tab
    this.tabList.addEventListener('keydown', this.onTabKeydown.bind(this), true);
  }

  getEnabledTabs( ) {
    return this.allTabs.filter( tab => !tab.disabled && tab.getAttribute('aria-disabled') !== 'true' );
  }

  setActiveTab( activeTab ) {
    for ( let tab of this.allTabs ) {
      if ( activeTab === tab ) {
        tab.setAttribute('aria-selected', 'true');
        tab.removeAttribute('tabindex');
      } else {
        tab.setAttribute('aria-selected', 'false');
        tab.classList.remove('active')
        tab.tabIndex = -1;
      }
    }
  }

  setTab( activeTab ) {
    activeTab.focus();
  }

  setPreviousTab( activeTab ) {
    const tabs = this.getEnabledTabs();
    const index = tabs.indexOf( activeTab );
    this.setTab( tabs[ index <= 0 ? tabs.length - 1 : index - 1 ] );
  }

  setNextTab( activeTab ) {
    const tabs = this.getEnabledTabs();
    const index = tabs.indexOf( activeTab );
    this.setTab( tabs[ index === tabs.length - 1 ? 0 : index + 1 ] );
  }

  /* EVENT HANDLERS */
  onTabKeydown( event ) {
    const tgt = event.target.closest('[role=tab]');
    if ( !tgt || !this.allTabs.includes( tgt ) ) {
      return;
    }
    const tabs = this.getEnabledTabs();
    if ( !tabs.length ) {
      return;
    }
    let flag = false;

    switch (event.key) {
      case 'ArrowLeft':
      case 'ArrowUp':
        this.setPreviousTab(tgt);
        flag = true;
        break;

      case 'ArrowRight':
      case 'ArrowDown':
        this.setNextTab(tgt);
        flag = true;
        break;

      case 'Home':
        this.setTab(tabs[0]);
        flag = true;
        break;

      case 'End':
        this.setTab(tabs[tabs.length - 1]);
        flag = true;
        break;

      default:
        break;
    }

    if ( flag ) {
      event.stopPropagation();
      event.preventDefault();
    }
  }

  onTabClick(event) {
    this.setActiveTab( event.currentTarget );
  }
}

// Initialize tablist
window.addEventListener('load', function () {
  const tabsList = document.querySelectorAll('[role=tablist]');
  for (var i = 0; i < tabsList.length; i++) {
    new ThemeTabs( tabsList[i] );
  }
});