<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeaderSessionLess.jsp" />

<jsp:useBean id="adminMessage" scope="request" class="fr.paris.lutece.portal.web.admin.AdminMessageJspBean" />

<%=	 adminMessage.getMessage( request ) %>
<%@ include file="AdminFooter.jsp" %>