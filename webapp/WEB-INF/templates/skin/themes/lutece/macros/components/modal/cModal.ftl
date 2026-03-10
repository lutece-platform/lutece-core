<#--
Macro: cModal

Description: Generates a Bootstrap modal dialog box with a header, body, and footer section for displaying content overlays on skin pages.

Parameters:
- title (string, required): Title displayed in the modal header.
- id (string, required): Unique identifier for the modal (used for targeting with data-bs-target).
- size (string, optional): Modal size class suffix ('sm', 'lg', 'xl'). Default: 'lg'.
- static (boolean, optional): Whether the backdrop is static (prevents closing on outside click). Default: false.
- pos (string, optional): Modal dialog position ('centered'). Default: 'centered'.
- role (string, optional): ARIA role attribute for the modal dialog. Default: ''.
- scrollable (boolean, optional): Whether the modal body is scrollable. Default: false.
- dismissible (boolean, optional): Whether to show close button in header and footer. Default: true.
- dismissLabel (string, optional): Label for the dismiss button. Default: '#i18n{portal.theme.labelClose}'.
- footer (string, optional): HTML content for the modal footer (e.g., submit buttons). Default: ''.
- titleLevel (number, optional): Heading level for the title element. Default: 1.
- class (string, optional): Additional CSS classes for the modal. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Showcase:
- desc: Fenêtre modale - @cModal
- bs: components/modal
- newFeature: false

Snippet:

    Basic modal:

    <@cModal title='Confirm action' id='confirmAction'>
        <p>Are you sure you want to proceed?</p>
    </@cModal>

    Modal with footer buttons and scrollable body:

    <@cModal title='Terms and conditions' id='terms' size='xl' scrollable=true footer='<button type="submit" class="btn btn-primary">Accept</button>'>
        <p>Long content here...</p>
    </@cModal>

    Static backdrop modal (non-dismissible on outside click):

    <@cModal title='Important notice' id='notice' static=true dismissible=false>
        <p>You must complete this step before continuing.</p>
    </@cModal>

-->
<#macro cModal title id size='lg' static=false pos='centered' role='' scrollable=false dismissible=true dismissLabel='#i18n{portal.theme.labelClose}' footer='' titleLevel=1 class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="modal cmodal fade<#if class!=''> ${class}</#if>" id="${id}Modal"<#if static> data-backdrop="static"</#if> role="dialog" aria-labelledby="modal${id}Title" aria-hidden="true" <#if params!=''> ${params}</#if>>
    <div class="modal-dialog <#if pos!=''>modal-dialog-${pos} </#if> <#if scrollable>modal-dialog-scrollable</#if><#if size!=''>modal-${size}</#if>"<#if role !=''> role="${role}"</#if>>
        <div class="modal-content">
            <div class="modal-header">
                <@cTitle level=titleLevel class="modal-title h5 main-color" id="modal${id}Title">${title}</@cTitle>
                <#if dismissible>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="#i18n{portal.theme.labelClose}">
                    </button> 
                </#if>
            </div>
            <div class="modal-body">
                <#nested>
            </div>
            <div class="modal-footer">
                <#if dismissible>
	                <@cBtn label='${dismissLabel}' class='tertiary m-1' params='data-bs-dismiss="modal"'/>
                </#if>
                ${footer!}
            </div>
        </div>
    </div>
</div>
</#macro>