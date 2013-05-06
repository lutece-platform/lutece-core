<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="publicUrlBean" scope="session" class="fr.paris.lutece.portal.web.security.PublicUrlJspBean" />

<%
	publicUrlBean.init( request, fr.paris.lutece.portal.web.security.PublicUrlJspBean.RIGHT_MANAGE ) ; 
	response.sendRedirect( publicUrlBean.getConfirmRemovePublicUrl(request) );
%>

