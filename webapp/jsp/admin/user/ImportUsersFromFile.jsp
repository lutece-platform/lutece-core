<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="appUser" scope="session" class="fr.paris.lutece.portal.web.user.AdminUserJspBean" />

<%
	appUser.init( request, "CORE_USERS_MANAGEMENT" ) ;
	String strContent = appUser.getImportUsersFromFile( request );
%>
<jsp:include page="../AdminHeader.jsp"  flush="true" />

<%= strContent %>

<%@ include file="../AdminFooter.jsp" %>