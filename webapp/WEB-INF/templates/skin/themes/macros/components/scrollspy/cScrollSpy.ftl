<#--
Macro: cScrollSpy

Description: Generates a two-column layout with a list of anchors on one side and the nested content on the other, the anchor of the section in view being highlighted while scrolling (Bootstrap scrollspy).

Parameters:
- anchorId (string, required): id of the list of anchors, target of the scrollspy.
- anchors (list, required): anchors of the list, each item with a 'label' and an 'href' (the id of a section of the nested content, prefixed with #).
- anchorClass (string, optional): CSS class(es) of the list of anchors. Default: 'position-sticky'.
- anchorListClass (string, optional): CSS class(es) of the ul element. Default: 'list-unstyled'.
- anchorListItemClass (string, optional): CSS class(es) of the li elements. Default: 'ps-m py-s'.
- anchorsLinkClass (string, optional): CSS class(es) of the anchor links. Default: ''.
- anchorsParams (string, optional): additional HTML attributes of the list of anchors. Default: ''.
- anchorColClass (string, optional): column classes of the list of anchors. Default: 'col-12 col-md-3'.
- contentColClass (string, optional): column classes of the nested content. Default: 'col-12 col-md-9'.
- id (string, optional): id of the row wrapper. Default: ''.
- class (string, optional): CSS class(es) of the row wrapper. Default: ''.
- params (string, optional): additional HTML attributes of the row wrapper. Default: ''.

Showcase:
- desc: Scrollspy - @cScrollSpy
- bs: components/scrollspy
- newFeature: false

Snippet:

    Page sections with a list of anchors (anchorsList usually comes from the model, written here by hand):

    <#assign anchorsList = [
        { 'label': 'Présentation', 'href': '#scrollspy-intro' },
        { 'label': 'Démarches', 'href': '#scrollspy-steps' },
        { 'label': 'Contact', 'href': '#scrollspy-contact' }
    ] />
    <@cScrollSpy anchorId='scrollspy-sample' anchors=anchorsList anchorsParams='style="top:100px;"'>
        <@cSection id='scrollspy-intro'>
            <@cTitle level=2>Présentation</@cTitle>
            <@cText>Le service vous permet de suivre vos demandes en ligne.</@cText>
        </@cSection>
        <@cSection id='scrollspy-steps'>
            <@cTitle level=2>Démarches</@cTitle>
            <@cText>Préparez vos pièces justificatives puis déposez votre demande.</@cText>
        </@cSection>
        <@cSection id='scrollspy-contact'>
            <@cTitle level=2>Contact</@cTitle>
            <@cText>Une question ? Écrivez-nous depuis le formulaire de contact.</@cText>
        </@cSection>
    </@cScrollSpy>

-->
<#macro cScrollSpy anchorId anchors anchorClass='position-sticky' anchorListClass='list-unstyled' anchorListItemClass='ps-m py-s' anchorsLinkClass='' anchorsParams='' anchorColClass='col-12 col-md-3' contentColClass='col-12 col-md-9' id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="row<#if class!=''> ${class}</#if>"<#if id!=''> id="${class}"</#if><#if params!=''> ${params}</#if>>
	<div class="${anchorColClass}">
		<@cAnchor id=anchorId class=anchorClass anchors=anchors listClass=anchorListClass listItemClass=anchorListItemClass anchorsClass=anchorsLinkClass params=anchorsParams />
	</div>
	<div class="${contentColClass}">
		<div data-bs-spy="scroll" data-bs-target="#${anchorId}" tabindex="0"'>
			<#nested>
		</div>
	</div>
</div>
</#macro>