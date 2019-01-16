<jsp:useBean id="manageautoincludesAutoInclude" scope="session" class="fr.paris.lutece.portal.web.templates.AutoIncludeJspBean" />
<% String strContent = manageautoincludesAutoInclude.processController ( request , response ); %>

<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../AdminFooter.jsp" %>
