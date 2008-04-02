<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="documentation" scope="request" class="fr.paris.lutece.portal.web.documentation.AdminDocumentationJspBean" />

<%= documentation.getSummaryDocumentation( request ) %>

<%@ include file="../AdminFooter.jsp" %>