/*
 *   This content is inspired by the W3C Software  - License at   https://www.w3.org/Consortium/Legal/2015/copyright-software-and-document
 *
 *   File:   themeparisfr-tabs.js.js
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

      tab.addEventListener('keydown', this.onTabKeydown.bind(this));
      tab.addEventListener('click', this.onTabClick.bind(this));
      
      if (!this.firstTab) {
        this.firstTab = tab;
      }
      this.lastTab = tab;
    }
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
    if (activeTab === this.firstTab) {
      this.setTab(this.lastTab);
    } else {
      const index = this.allTabs.indexOf( activeTab );
      this.setTab( this.allTabs[index - 1]);
    }
  }

  setNextTab(activeTab) {
    if ( activeTab === this.lastTab ) {
      this.setTab( this.firstTab );
    } else {
      const index = this.allTabs.indexOf( activeTab );
      this.setTab( this.allTabs[index + 1]) ;
    }
  }

  /* EVENT HANDLERS */
  onTabKeydown( event ) {
    let tgt = event.currentTarget
    let flag = false;

    switch (event.key) {
      case 'ArrowLeft':
        this.setPreviousTab(tgt);
        flag = true;
        break;

      case 'ArrowRight':
        this.setNextTab(tgt);
        flag = true;
        break;

      case 'Home':
        this.setTab(this.firstTab);
        flag = true;
        break;

      case 'End':
        this.setTab(this.lastTab);
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