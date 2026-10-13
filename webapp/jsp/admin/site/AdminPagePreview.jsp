<%@ page pageEncoding="UTF-8" %>
<%@ page errorPage="../ErrorPage.jsp" %>

<%@ page import="org.apache.commons.text.StringEscapeUtils" %>
<%@ page import="fr.paris.lutece.portal.service.util.*" %>
<%@ page import="fr.paris.lutece.portal.web.LocalVariables" %>
<%@page import="fr.paris.lutece.portal.web.admin.AdminPageJspBean"%>
<%@ page import="fr.paris.lutece.portal.service.i18n.I18nService" %>
<%@ page import="fr.paris.lutece.portal.service.portal.ThemesService" %>
<%@ page import="fr.paris.lutece.portal.business.style.Theme" %>

<%@ page buffer="1024kb"%>
<%@ page autoFlush="false"%>
<%!
	private final static String PROPERTY_LABELPORTLETTYPE_CREATE = "portal.site.portletType.labelCreate";
    private final static String PROPERTY_LABELPORTLETTYPE_CREATECOLUMN = "portal.site.portletType.labelCreateColumn";
    private final static String PROPERTY_LABEL_CANCEL = "portal.util.labelCancel";
    private final static String PROPERTY_LABEL_CLOSE = "portal.util.labelClose";
%>
${ adminPageJspBean.init( pageContext.request, AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE ) }

<%
    try
    {
    	LocalVariables.setLocal( config, request, response );
       

%>
${ adminPageJspBean.getAdminPagePreview( pageContext.request ) }
<!--JSP -->
<!-- Add CSS theme selected theme default file -->
<%
    Theme globalTheme = ThemesService.getGlobalThemeObject( );
    String strThemeCode = globalTheme.getCodeTheme( );
    String strThemeVersion = globalTheme.getThemeVersion( );
%>
<link rel="stylesheet" href="themes/skin/<%= StringEscapeUtils.escapeHtml4( strThemeCode ) %>/css/theme.min.css?version=<%= StringEscapeUtils.escapeHtml4( strThemeVersion ) %>" type="text/css" />
<style>
/* In Iframe Preview scrollbar styles */
html {
  /* Firefox */
  scrollbar-width: thin;
  scrollbar-color: #888 #f0f0f0;
}

/* Chromium */
html::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

html::-webkit-scrollbar-track {
  background: #f0f0f0;
  border-radius: 10px;
}

html::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 10px;
}

html::-webkit-scrollbar-thumb:hover {
  background: #555;
}

/* The place of a portlet type dragged from the "Add" tab: the outline and the fading of a portlet being dragged, with its type */
.lutece-admin-portlet-new {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: .5rem;
  min-height: 3.5rem;
  color: var(--lutece-admin-accent);
  font: 500 14px/1.4 system-ui, -apple-system, "Segoe UI", Roboto, sans-serif;
}   
</style>
<!-- The dialog adding a portlet: the portlet types of the "Add" tab of AdminSite.jsp -->
<dialog id="addPortletDialog" class="lutece-dialog" aria-labelledby="portletModalLabel">
    <div class="lutece-dialog-content">
        <div class="lutece-dialog-header">
            <h2 class="lutece-dialog-title" id="portletModalLabel"><%= I18nService.getLocalizedString(PROPERTY_LABELPORTLETTYPE_CREATE, request.getLocale() ) %></h2>
            <button type="button" class="lutece-dialog-close btn-cancel" title="<%= I18nService.getLocalizedString(PROPERTY_LABEL_CLOSE, request.getLocale() ) %>"><i class="ti ti-x" aria-hidden="true"></i><span class="lutece-dialog-sr"><%= I18nService.getLocalizedString(PROPERTY_LABEL_CLOSE, request.getLocale() ) %></span></button>
        </div>
        <div class="lutece-dialog-body">
            <ul id="portlet_type_id" class="lutece-dialog-types"></ul>
        </div>
        <div class="lutece-dialog-footer">
            <button type="button" class="lutece-dialog-button btn-cancel"><%= I18nService.getLocalizedString(PROPERTY_LABEL_CANCEL, request.getLocale() ) %></button>
        </div>
    </div>
</dialog>
<script type="module">
document.addEventListener( "DOMContentLoaded", function(){
    const dialogPortletTypes = document.getElementById( 'portlet_type_id' );
    const portletDialog = document.querySelector( '#addPortletDialog' );

    // The portlet types of the "Add" tab of AdminSite.jsp, the page holding this preview, read when the dialog opens first
    const fillPortletTypes = () => {
        if ( dialogPortletTypes.childElementCount > 0 ) { return }
        const parentPortletTypeNodes = window.parent.document.querySelectorAll( '#lutece-site-types > li[data-portlet-type-href]' );
        parentPortletTypeNodes.forEach( ( item ) => {
            const liType = document.createElement( 'li' )
            const aType = document.createElement( 'a' )
            aType.classList.add( 'lutece-dialog-type' )
            aType.dataset.baseHref = item.dataset.portletTypeHref
            aType.setAttribute( 'href', item.dataset.portletTypeHref )
            // The icon keeps the tint it has in the "Add" tab
            const iconBox = document.createElement( 'span' )
            const parentIcon = item.querySelector( '.lutece-site-type-icon' )
            iconBox.className = parentIcon ? parentIcon.className.replace( /lutece-site-/g, 'lutece-dialog-' ) : 'lutece-dialog-type-icon lutece-dialog-tint-5'
            iconBox.setAttribute( 'aria-hidden', 'true' )
            const iconType = document.createElement( 'i' )
            iconType.classList.add( 'ti', 'ti-' + item.dataset.portletTypeIcon )
            iconBox.appendChild( iconType )
            const spanType = document.createElement( 'span' )
            spanType.classList.add( 'lutece-dialog-type-name' )
            spanType.textContent = item.dataset.portletTypeName
            aType.append( iconBox, spanType )
            liType.appendChild( aType )
            dialogPortletTypes.appendChild( liType )
        })
    }

    // 
    const columnList = document.querySelectorAll( '.lutece-admin-column' );
    columnList.forEach( ( col ) => {
        const colOutList = col.querySelectorAll( '.lutece-admin-column-outline' );
        colOutList.forEach( ( colOut ) => { 
            if( colOut.textContent.trim() === '' ){
                colOut.textContent= '';
            }
        })
    })

    // "Show the dialog" button opens the <dialog> modally
    const btnPortletList = document.querySelectorAll( '[data-lutece-add-portlet]' );
    btnPortletList.forEach( ( btnPortlet ) => {
        btnPortlet.addEventListener( 'click', ( event ) => {
            const portlet = event.currentTarget
            const portletColumn = portlet.dataset.portletColumn
            let portletOrder = portlet.dataset.portletOrder
            if( portletOrder === '' ){
                const lastPortlet = document.querySelector(`#lutece-column-\${portletColumn} .lutece-admin-column-outline .lutece-admin-portlet:last-child .lutece-admin-toolbar`)
                if( lastPortlet != null ){
                    portletOrder = parseInt( lastPortlet.dataset.portletPlace ) + 1
                } else {
                    portletOrder = 1
                }
            }
            fillPortletTypes()
            dialogPortletTypes.childNodes.forEach( ( item ) => {
                let itemLnk = item.firstElementChild;
                itemLnk.setAttribute('href', itemLnk.dataset.baseHref + '&portlet_column=' + portletColumn + '&portlet_order=' + portletOrder)
            })  
            portletDialog.showModal()
        })
    })

    const btnCloseDialogList = document.querySelectorAll( '.btn-cancel' );
    btnCloseDialogList.forEach( ( btnCloseDialog ) => {
        btnCloseDialog.addEventListener( 'click', ( event ) => {
            portletDialog.close()
        })
    })

}); 

import {
    LuteceDraggable
} from './themes/shared/modules/luteceDraggable.js';

const root = document.querySelector(":root");
root.style.setProperty("--column-empty-text", `"<%= I18nService.getLocalizedString(PROPERTY_LABELPORTLETTYPE_CREATECOLUMN, request.getLocale() ) %>"`);

const containers = document.querySelectorAll('#main .lutece-admin-column-outline');
const draggables = Array.from(containers).flatMap(container => [...container.children]);
const AdminHomeDraggable = new LuteceDraggable( draggables, containers);

// The stored orders of a column may have gaps, left by portlets removed before the columns kept the orders 1 to n, and the server
// takes an order as a place in the column: the up, down and "add after" buttons and the drag use the place of the portlet
containers.forEach( ( container ) => {
    [ ...container.querySelectorAll( ':scope > .lutece-admin-portlet' ) ].forEach( ( portletBox, i ) => {
        const toolbar = portletBox.querySelector( ':scope > .lutece-admin-toolbar' );
        if ( !toolbar ) {
            return;
        }
        toolbar.dataset.portletPlace = i + 1;
        toolbar.querySelectorAll( 'form[action$="DoModifyPortletPosition.jsp"]' ).forEach( ( form ) => {
            const order = form.elements.order;
            const up = parseInt( order.value, 10 ) < parseInt( toolbar.dataset.portletOrder, 10 );
            if ( up && i === 0 ) {
                form.remove( );
            } else {
                order.value = up ? i : i + 2;
            }
        } );
        toolbar.querySelectorAll( '[data-lutece-add-portlet]' ).forEach( ( button ) => { button.dataset.portletOrder = i + 2; } );
    } );
} );

// The page holding this preview veils it from the moment it starts to leave, then covers it once its page has gone, until the new one
// has loaded with its styles: no flash of an unstyled page. Without that page (the preview opened alone), nothing happens
const previewBusy = ( state ) => {
    try { window.parent.luteceSitePreviewBusy?.( state ); } catch ( e ) { /* a parent of another origin */ }
};
window.addEventListener( 'beforeunload', ( ) => previewBusy( 'veil' ) );
window.addEventListener( 'pagehide', ( ) => previewBusy( 'cover' ) );

// A portlet type dragged from the "Add" tab of the page holding this preview, which calls luteceSitePortletTypeDrag with the type
// when the drag starts and with null when it ends. A placeholder follows the pointer in the column under it, and the drop opens the
// creation form of the type with that column and that order
const newPortlet = document.createElement( 'div' );
newPortlet.className = 'lutece-admin-portlet lutece-dragging lutece-admin-portlet-new';
let draggedType = null;
window.luteceSitePortletTypeDrag = ( type ) => {
    draggedType = type;
    newPortlet.remove( );
    if ( type ) {
        const icon = document.createElement( 'i' );
        icon.className = 'ti ti-' + type.icon;
        icon.setAttribute( 'aria-hidden', 'true' );
        const name = document.createElement( 'span' );
        name.textContent = type.name;
        newPortlet.replaceChildren( icon, name );
    }
};
// The outline of the column under the pointer, also when the pointer is on its toolbar or its padding
const columnUnder = ( target ) => target.closest?.( '.lutece-admin-column' )?.querySelector( '.lutece-draggable-container' );
// Capture phase, and kept from the columns: their own handler moves the portlets of the page
document.addEventListener( 'dragover', ( event ) => {
    if ( !draggedType ) {
        return;
    }
    event.stopPropagation( );
    const container = columnUnder( event.target );
    if ( !container ) {
        // Out of the columns, no place and no drop
        newPortlet.remove( );
        return;
    }
    event.preventDefault( );
    event.dataTransfer.dropEffect = 'copy';
    const next = AdminHomeDraggable.getDragTargetElement( container, event.clientY );
    if ( next ) {
        if ( newPortlet.nextElementSibling !== next ) {
            container.insertBefore( newPortlet, next );
        }
    } else if ( container.lastElementChild !== newPortlet ) {
        container.appendChild( newPortlet );
    }
}, true );
document.addEventListener( 'drop', ( event ) => {
    if ( !draggedType ) {
        return;
    }
    event.stopPropagation( );
    event.preventDefault( );
    const container = newPortlet.parentElement;
    if ( !container ) {
        return;
    }
    // The order is the place among the portlets of the column: the creation moves the portlets below one step down
    const order = [ ...container.querySelectorAll( ':scope > .lutece-admin-portlet' ) ].indexOf( newPortlet ) + 1;
    window.location.href = draggedType.href + '&portlet_column=' + container.dataset.portletColumn + '&portlet_order=' + order;
}, true );

// A double click on a portlet opens its form, as its pencil button does (the form of its toolbar posting to DoModifyPortlet.jsp).
// Its toolbar, and the links, buttons and fields of its content, keep their own double click
document.addEventListener( 'dblclick', ( event ) => {
    if ( event.target.closest( '.lutece-admin-toolbar, a, button, input, textarea, select, label, [contenteditable]' ) ) {
        return;
    }
    const portletBox = event.target.closest( '.lutece-admin-portlet' );
    const toolbar = portletBox && portletBox.querySelector( ':scope > .lutece-admin-toolbar' );
    const modifyForm = toolbar && [ ...toolbar.querySelectorAll( 'form' ) ].find( ( form ) => /DoModifyPortlet\.jsp$/.test( form.getAttribute( 'action' ) ) );
    if ( !modifyForm ) {
        return;
    }
    event.preventDefault( );
    window.getSelection( )?.removeAllRanges( );
    modifyForm.requestSubmit( );
} );

// The position is saved, then the preview reloads: the badges, the up and down buttons and the "add after" buttons carry the stored orders
// The security token comes from the toolbar of the portlet, used once: the preview reloads with new ones
async function updatePortlet( portletId, col, order, token ){
    // The veil comes before the save, which takes a moment too
    previewBusy( 'veil' );
    try {
        await fetch( 'jsp/admin/site/DoModifyPortletPosition.jsp', { method: 'POST', redirect: 'manual',
            body: new URLSearchParams( { portlet_id: portletId, column: col, order: order, token: token } ) } );
    } finally {
        window.location.reload();
    }
}

AdminHomeDraggable.on('dragstart', (event) => {
   root.style.setProperty( "--column-empty-text", `"Drop here !"` );
});

AdminHomeDraggable.on( 'dragend', (event) => {
    root.style.setProperty("--column-empty-text", `"<%= I18nService.getLocalizedString(PROPERTY_LABELPORTLETTYPE_CREATECOLUMN, request.getLocale() ) %>"`);
    const portletBox = event.currentTarget;
    const toolbar = portletBox.querySelector( ':scope > .lutece-admin-toolbar' );
    const dragContainer = portletBox.closest( '.lutece-draggable-container' );
    if ( !toolbar || !dragContainer ) {
        return;
    }
    const newCol = dragContainer.dataset.portletColumn;
    // The order is the place of the portlet among the portlets of its column, whatever sits between them
    const newOrder = [ ...dragContainer.querySelectorAll( ':scope > .lutece-admin-portlet' ) ].indexOf( portletBox ) + 1;
    // Columns left empty lose their blank text, so that they show as empty
    containers.forEach( ( container ) => { if ( container.textContent.trim() === '' ) { container.textContent = '' } } );
    if ( newCol === toolbar.dataset.portletColumn && String( newOrder ) === toolbar.dataset.portletPlace ) {
        return;
    }
    updatePortlet( toolbar.dataset.portletId, newCol, newOrder, toolbar.dataset.positionToken );
});
</script> 
<%
    }
    finally
    {
        LocalVariables.setLocal( null, null, null );
    }
%>
