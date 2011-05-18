<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="appUser" scope="session" class="fr.paris.lutece.portal.web.user.AdminUserJspBean" />

<%
	appUser.init( request, "CORE_USERS_MANAGEMENT" ) ; 
	response.sendRedirect( appUser.doInsertRegularExpression( request ) );  
%>

