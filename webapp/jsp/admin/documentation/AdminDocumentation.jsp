<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeaderSessionLess.jsp" />

<jsp:useBean id="documentation" scope="request" class="fr.paris.lutece.portal.web.documentation.AdminDocumentationJspBean" />
<%
	String htmlCode = documentation.getDocumentation( request );
	if ( htmlCode  == null )
	{
		response.sendRedirect(documentation.doAdminMessage( request ));
	}
%>

<%= htmlCode  %>

<%@ include file="../AdminFooter.jsp" %>