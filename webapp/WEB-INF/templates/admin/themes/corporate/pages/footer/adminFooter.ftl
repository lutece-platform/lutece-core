<#--
Macro: adminFooter

Description: Generates the footer section, including documentation and source code links, a Lutece logo, and a version number.

Parameters:
- version : The version number
-->
<#macro adminFooter closeMain=true >
<footer id='main-lutece-footer' class="d-flex justify-content-end align-items-end border-top d-none">
<@p class='me-5'>${site_name} - #i18n{portal.site.portal_footer.labelMadeBy} ${version}</@p>
</footer>
<!-- footer menu                                                     -->
<!-- Included JS Files 												            -->
<!-- Le javascript 													            -->
<!-- =============================================================== -->
<!-- Placed at the end of the document so the pages load faster 	   -->
<@coreAdminJSLinks />
${javascript_files}
</div><!-- Close wrapper -->
</div>
<#if dskey('portal.site.site_property.bo.showXs.checkbox') == '0' >
<@pageContainer class="position-fixed top-0 w-100 d-block d-md-block d-lg-none">
    <@pageColumn id="lutece-login-block" class="border-end p-0" height="full">
	   <@div class="d-flex align-items-center justify-content-evenly vh-100">
			<@div class="container-tight">
				<@div class="card shadow-lg rounded-4 p-4 mx-5 mw-30">
					<@div class="card-body p-5 fs-6">
                  <@div class="text-center mb-4">
                     <@link href='/' target='_blank'>
                        <img src="${dskey('portal.site.site_property.logo_url')}" height="40" alt="Logo" aria-hidden="true" >
                        <span class="visually-hidden">${site_name!'Lutece'}</span>
                     </@link>
                  </@div>
                  <@div class='d-flex flex-column align-items-center'>
                     <h2 class="h1 mb-4 text-center">#i18n{portal.admin.admin_login.welcome} ${site_name!}</h2>
                     <i class="ti ti-device-mobile-off" style="font-size:120px !important"></i>
                  </@div>
               </@div>
			   </@div>
			</@div>
		</@div>
	</@pageColumn>
</@pageContainer>
</#if>
<#if dskey('portal.site.site_property.bo.showXsWarning.checkbox')?number == 1 >
<@initToast position='top-0 start-50 translate-middle-x' showAll=true >
   <@addToast title='' content='#i18n{portal.site.message.showXsWarningMsg}' class='text-bg-warning d-block d-sm-block d-md-block d-lg-none' />
</@initToast>
</#if>
</#macro>
<#--
Macro: adminSiteFooter

Description: Footer for site Admin
Parameters:
- 
-->
<#macro adminSiteFooter >
<#assign siteFooter = .get_optional_template('../../../../../skin/site/portal_footer.html')>
<#if siteFooter.exists><@siteFooter.include /></#if>
<!-- A modal dialog containing a form -->
<dialog id="addPortletDialog" class="lutece-dialog" aria-labelledby="portletModalLabel" aria-hidden="true" tabindex="-1">
    <div class="lutece-dialog lutece-dialog-fullscreen">
        <div class="lutece-dialog-content">
            <div class="lutece-dialog-header">
                <h2 class="lutece-dialog-title h4 text-dark" id="portletModalLabel">#i18n{portal.site.portletType.labelCreate}</h2>
                <button type="button" class="btn btn-link btn-cancel text-dark" aria-label="#i18n{portal.util.labelCancel}"><i class="ti ti-x"></i></button>
            </div>
            <div class="lutece-dialog-body">
                    <form action="jsp/admin/DoCreatePortlet.jsp" type="get">
                    <div class="container">
                        <div id='portlet_type_id' class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4"></div>
                    </div>
                    <div class="d-flex justify-content-center">
                        <button type="button" class="btn btn-secondary btn-cancel" value="cancel" >
                        <svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-x"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M18 6l-12 12" /><path d="M6 6l12 12" /></svg> #i18n{portal.util.labelClose}</button>
                    </div>
                    </form>
                </div>
            <div>
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
        aType.classList.add('btn', 'btn-outline-primary', 'btn-lg','btn-block', 'btn-new-portlet', 'py-5', 'px-0' , 'my-3', 'd-flex', 'align-items-center' )
        aType.setAttribute( 'href', item.dataset.portletTypeHref )
        const spanType = document.createElement('span')
        spanType.classList.add( 'px-2', 'text-left', 'truncate' )
        spanType.innerText = item.dataset.portletTypeName
        const iconType = document.createElement('i')
        iconType.classList.add('ti', <#noparse>`ti-${item.dataset.portletTypeIcon}`</#noparse> ,'fs-0','d-block','me-4','mr-4','ps-2','pl-2')
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
                const lastPortlet = document.querySelector(<#noparse>`#lutece-column-${portletColumn} .lutece-admin-column-outline .lutece-admin-portlet:last-child .lutece-admin-toolbar</#noparse>`)
                if( lastPortlet != null ){
                    portletOrder = parseInt( document.querySelector(<#noparse>`#lutece-column-${portletColumn} .lutece-admin-column-outline .lutece-admin-portlet:last-child .lutece-admin-toolbar</#noparse>`).dataset.portletOrder ) + 1
                } else {
                    portletOrder = 1
                }
            }
            dialogPortletTypes.childNodes.forEach( ( item ) => {
                let itemLnk = item.firstElementChild;
                let itemHref = itemLnk.getAttribute('href');
                <#noparse>itemLnk.setAttribute('href',`${itemHref}&portlet_column=${portletColumn}&portlet_order=${portletOrder}`)</#noparse>
            })  
            portletDialog.showModal()
        })
    })

    const btnCloseDialogList = document.querySelectorAll( '.btn-close' );
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
root.style.setProperty("--column-empty-text", `"#i18n{portal.site.portletType.labelCreateColumn}"`);

const containers = document.querySelectorAll('#main .lutece-admin-column-outline');
const draggables = Array.from(containers).flatMap(container => [...container.children]);
const AdminHomeDraggable = new LuteceDraggable( draggables, containers);

async function updatePortlet( portletId, col, order ){
<#noparse>const response = await fetch( `jsp/admin/site/DoModifyPortletPosition.jsp?portlet_id=${portletId}&column=${col}&order=${order}` );</#noparse>
}
 
AdminHomeDraggable.on('dragstart', (event) => {
   root.style.setProperty( "--column-empty-text", `"Drop moi ici !"` );
});
 
AdminHomeDraggable.on('dragover', (event) => {
    //event.currentTarget.style.setProperty( "opacity", ".5" );
});
 
AdminHomeDraggable.on( 'dragend', (event) => {
    root.style.setProperty("--column-empty-text", `"#i18n{portal.site.portletType.labelCreateColumn}"`);
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
</#macro>