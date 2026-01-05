<%@ page pageEncoding="UTF-8" %>
<%@ page errorPage="../ErrorPage.jsp" %>

<%@ page import="fr.paris.lutece.portal.service.util.*" %>
<%@ page import="fr.paris.lutece.portal.web.LocalVariables" %>
<%@page import="fr.paris.lutece.portal.web.admin.AdminPageJspBean"%>
<%@ page import="fr.paris.lutece.portal.service.i18n.I18nService" %>

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
<!-- A modal dialog containing a form -->
<dialog id="addPortletDialog" class="lutece-dialog" aria-labelledby="portletModalLabel" aria-hidden="true" tabindex="-1">
    <div class="lutece-dialog lutece-dialog-fullscreen">
        <div class="lutece-dialog-content">
            <div class="lutece-dialog-header">
                <h2 class="lutece-dialog-title h4 text-dark" id="portletModalLabel"><%= I18nService.getLocalizedString(PROPERTY_LABELPORTLETTYPE_CREATE, request.getLocale() ) %></h2>
                <button type="button" class="btn btn-link btn-cancel text-dark" aria-label="<%= I18nService.getLocalizedString(PROPERTY_LABEL_CANCEL, request.getLocale() ) %>"><i class="ti ti-x"></i></button>
            </div>
            <div class="lutece-dialog-body">
                <div class="container">
                    <div id='portlet_type_id' class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4"></div>
                </div>
                <div class="d-flex justify-content-center">
                    <button type="button" class="btn btn-secondary btn-cancel" value="cancel" >
                    <svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-x"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M18 6l-12 12" /><path d="M6 6l12 12" /></svg><%= I18nService.getLocalizedString(PROPERTY_LABEL_CLOSE, request.getLocale() ) %></button>
                </div>
            </div>
        </div>
    </div>
</div>
</dialog>
<script type="module">
document.addEventListener( "DOMContentLoaded", function(){
    const parentPortletTypeNodes = window.parent.document.querySelectorAll( '#offcanvas-body-portlet-type-wrapper ul li' );
    const dialogPortletTypes = document.getElementById( 'portlet_type_id' );
    const portletDialog = document.querySelector( '#addPortletDialog' );

    parentPortletTypeNodes.forEach( ( item ) => {
        const divType = document.createElement('div')
        divType.classList.add('col')
        const aType = document.createElement('a')
        aType.classList.add('btn', 'btn-secondary', 'btn-lg', 'btn-block', 'btn-new-portlet', 'py-5', 'px-0' , 'my-3', 'd-flex', 'align-items-center' )
        aType.setAttribute( 'href', item.dataset.portletTypeHref )
        const spanType = document.createElement('span')
        spanType.classList.add( 'px-2', 'text-left', 'truncate' )
        spanType.innerText = item.dataset.portletTypeName
        const iconType = document.createElement('i')
        iconType.classList.add('ti', 'ti-' + item.dataset.portletTypeIcon ,'fs-1','d-block','ps-2','pl-2')
        aType.appendChild( iconType );
        aType.appendChild( spanType );
        divType.appendChild( aType );
        dialogPortletTypes.appendChild( divType );
    })

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
    const btnPortletList = document.querySelectorAll( '[data-bs-toggle="modal"]' );
    btnPortletList.forEach( ( btnPortlet ) => {
        btnPortlet.addEventListener( 'click', ( event ) => {
            const portlet = event.currentTarget
            const portletColumn = portlet.dataset.portletColumn
            let portletOrder = portlet.dataset.portletOrder
            if( portletOrder === '' ){
                const lastPortlet = document.querySelector(`#lutece-column-${portletColumn} .lutece-admin-column-outline .lutece-admin-portlet:last-child .lutece-admin-toolbar`)
                if( lastPortlet != null ){
                    portletOrder = parseInt( document.querySelector('#lutece-column-' + portletColumn + ' .lutece-admin-column-outline .lutece-admin-portlet:last-child .lutece-admin-toolbar').dataset.portletOrder ) + 1
                } else {
                    portletOrder = 1
                }
            }
            dialogPortletTypes.childNodes.forEach( ( item ) => {
                let itemLnk = item.firstElementChild;
                let itemHref = itemLnk.getAttribute('href');
                itemLnk.setAttribute('href', itemHref + '&portlet_column=' + portletColumn + '&portlet_order=' + portletOrder)
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

async function updatePortlet( portletId, col, order ){
const response = await fetch( `jsp/admin/site/DoModifyPortletPosition.jsp?portlet_id=${portletId}&column=${col}&order=${order}` );
}
 
AdminHomeDraggable.on('dragstart', (event) => {
   root.style.setProperty( "--column-empty-text", `"Drop here !"` );
});
 
AdminHomeDraggable.on('dragover', (event) => {
    //event.currentTarget.style.setProperty( "opacity", ".5" );
});
 
AdminHomeDraggable.on( 'dragend', (event) => {
    root.style.setProperty("--column-empty-text", `"<%= I18nService.getLocalizedString(PROPERTY_LABELPORTLETTYPE_CREATECOLUMN, request.getLocale() ) %>"`);
    const dragContainer = event.target.closest( '.lutece-draggable-container' );
    const newCol = dragContainer.dataset.portletColumn;
    const portletId = event.currentTarget.firstElementChild.dataset.portletId
    let newOrder= 1
	if ( event.currentTarget.nextSibling != null ){
		newOrder = parseInt( event.currentTarget.nextSibling.firstElementChild.dataset.portletOrder )
		if( newOrder > 1 ){ newOrder-- }
	} else if ( event.currentTarget.previousSibling != null ){
		newOrder = parseInt( event.currentTarget.previousSibling.previousSibling.firstElementChild.dataset.portletOrder )
		newOrder++
    }

	event.currentTarget.firstElementChild.firstElementChild.textContent = newOrder
	event.currentTarget.firstElementChild.firstElementChild.dataset.portletOrder = newOrder
	event.currentTarget.firstElementChild.firstElementChild.dataset.portletColumn = newCol

    if( dragContainer.textContent.trim() === '' ){ dragContainer.textContent = '' }

	updatePortlet( portletId, newCol, newOrder )
}); 
</script> 
<%
    }
    finally
    {
        LocalVariables.setLocal( null, null, null );
    }
%>
