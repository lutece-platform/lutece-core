<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="right" scope="session" class="fr.paris.lutece.portal.web.features.RightJspBean" />

<% 
	right.init( request,  right.RIGHT_MANAGE_RIGHTS ) ; 
	response.sendRedirect( right.doAssignUsers( request ) );
%>


