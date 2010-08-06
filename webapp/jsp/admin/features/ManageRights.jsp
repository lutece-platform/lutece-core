<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="right" scope="session" class="fr.paris.lutece.portal.web.features.RightJspBean" />

<% right.init( request , right.RIGHT_MANAGE_RIGHTS ); %>
<%= right.getManageRights( request ) %>

<%@ include file="../AdminFooter.jsp" %>