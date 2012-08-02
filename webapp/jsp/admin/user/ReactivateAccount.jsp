<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="appUser" scope="session" class="fr.paris.lutece.portal.web.user.AdminUserJspBean" />

<%
	response.sendRedirect( appUser.reactivateAccount( request ) );
%>