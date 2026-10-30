/*
 *   File:   theme-nav-overflow.js
 *
 *   Desc:   Keeps the main menu on one line (Priority+), after the Tabler nav overflow component
 *           (https://docs.tabler.io/ui/components/nav-overflow): the items that do not fit move into the More menu,
 *           and come back when there is room again. The front office loads Bootstrap, not Tabler: when a theme
 *           loads Tabler, its own NavOverflow takes the wrapper.
 *
 *   Markup: a wrapper with data-bs-toggle="nav-overflow" around the nav. The items before the More item
 *           (.nav-overflow-item, its button .nav-overflow-toggle and its menu .nav-overflow-menu) may move, except
 *           the ones with .nav-overflow-keep; the items after it always stay.
 */
'use strict';
class ThemeNavOverflow {
  constructor( wrapper ) {
    this.wrapper = wrapper;
    this.nav = wrapper.querySelector( '.navbar-nav, .nav' );
    this.moreItem = this.nav.querySelector( ':scope > .nav-overflow-item' );
    if ( !this.moreItem ) {
      return;
    }
    this.toggle = this.moreItem.querySelector( '.nav-overflow-toggle' );
    this.menu = this.moreItem.querySelector( '.nav-overflow-menu' );
    this.threshold = parseInt( wrapper.dataset.bsThreshold || '0', 10 );
    // The wrapper and its parents up to the navbar: flex items that would grow with the nav instead of keeping the
    // width they are given (the wrapper alone in the core menu, the offcanvas around it in a theme like parisfr)
    this.shrinkable = [ wrapper ];
    if ( wrapper.parentElement?.closest( '.navbar' ) ) {
      for ( let element = wrapper.parentElement; !element.classList.contains( 'navbar' ); element = element.parentElement ) {
        this.shrinkable.push( element );
      }
    }
    this.collect( );
    // The wrapper is what is measured: moving items changes the width of the nav, not of its wrapper
    new ResizeObserver( ( ) => this.update( ) ).observe( wrapper );
    // The widths change once the fonts of the theme are there
    document.fonts?.ready.then( ( ) => this.update( ) );
    // A menu loaded by script (the REST menu of a theme) adds or removes items: they are taken again
    new MutationObserver( ( ) => {
      this.collect( );
      this.update( );
    } ).observe( this.nav, { childList: true } );
    this.update( );
  }

  // The items before the More item that may move, the ones with .nav-overflow-keep left out
  collect( ) {
    this.items = [];
    for ( const item of this.nav.children ) {
      if ( item === this.moreItem ) {
        break;
      }
      if ( !item.classList.contains( 'nav-overflow-keep' ) ) {
        this.items.push( item );
      }
    }
  }

  // The collapsed menu of a phone is vertical: every item has its room there
  isHorizontal( ) {
    return !getComputedStyle( this.nav ).flexDirection.startsWith( 'column' );
  }

  fits( ) {
    return this.nav.scrollWidth <= this.nav.clientWidth + 1;
  }

  update( ) {
    for ( const item of this.items ) {
      item.classList.remove( 'd-none' );
    }
    this.moreItem.classList.add( 'd-none' );
    this.menu.replaceChildren( );
    this.toggle.classList.remove( 'active' );

    const horizontal = this.isHorizontal( );
    // On one line the items keep their width, the nav overflows instead of wrapping their text, and the wrapper
    // keeps the width it is given instead of growing with the nav
    for ( const element of this.shrinkable ) {
      element.style.minWidth = horizontal ? '0' : '';
    }
    this.nav.classList.toggle( 'flex-nowrap', horizontal );
    for ( const item of this.nav.children ) {
      item.classList.toggle( 'flex-shrink-0', horizontal );
      item.classList.toggle( 'text-nowrap', horizontal );
    }
    if ( !horizontal || this.fits( ) ) {
      return;
    }

    this.moreItem.classList.remove( 'd-none' );
    const moved = [];
    for ( let i = this.items.length - 1; i >= this.threshold && !this.fits( ); i-- ) {
      this.items[i].classList.add( 'd-none' );
      moved.unshift( this.items[i] );
    }
    if ( moved.length === 0 ) {
      this.moreItem.classList.add( 'd-none' );
      return;
    }
    moved.forEach( ( item, index ) => {
      // A divider closes the links of a submenu, so the next item is not read as one of them
      if ( index > 0 && moved[index - 1].querySelector( '.dropdown-menu' ) && !item.querySelector( '.dropdown-menu' ) ) {
        this.menu.append( this.divider( ) );
      }
      this.menu.append( ...this.copy( item, index ) );
    } );
    this.toggle.classList.toggle( 'active', moved.some( item => this.isActive( item ) ) );
  }

  isActive( item ) {
    return item.classList.contains( 'active' ) || item.querySelector( '[aria-current="page"], [aria-current="true"]' ) !== null;
  }

  // A plain item becomes a dropdown item; an item with its own dropdown becomes a header followed by its links
  copy( item, index ) {
    const entries = [];
    const submenu = item.querySelector( '.dropdown-menu' );
    if ( submenu ) {
      if ( index > 0 ) {
        entries.push( this.divider( ) );
      }
      const toggle = item.querySelector( '[data-bs-toggle="dropdown"]' );
      entries.push( this.entry( Object.assign( document.createElement( 'span' ), { className: 'dropdown-header', textContent: toggle ? toggle.textContent.trim( ) : '' } ) ) );
      for ( const link of submenu.querySelectorAll( 'a[href]' ) ) {
        entries.push( this.entry( this.link( link ) ) );
      }
      return entries;
    }
    const link = item.querySelector( 'a[href]' );
    if ( link ) {
      const copy = this.link( link );
      copy.classList.toggle( 'active', this.isActive( item ) );
      entries.push( this.entry( copy ) );
    }
    return entries;
  }

  link( link ) {
    const copy = link.cloneNode( true );
    copy.removeAttribute( 'id' );
    // The classes of the links of the submenus (menu_main.html), styled by the themes
    copy.className = 'dropdown-item nav-link';
    if ( link.getAttribute( 'aria-current' ) === 'true' || link.getAttribute( 'aria-current' ) === 'page' ) {
      copy.classList.add( 'active' );
    }
    return copy;
  }

  divider( ) {
    return this.entry( Object.assign( document.createElement( 'hr' ), { className: 'dropdown-divider' } ) );
  }

  entry( element ) {
    const li = document.createElement( 'li' );
    li.append( element );
    return li;
  }
}

if ( !window.tabler?.NavOverflow ) {
  document.querySelectorAll( '[data-bs-toggle="nav-overflow"]' ).forEach( wrapper => new ThemeNavOverflow( wrapper ) );
}
