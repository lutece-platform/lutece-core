<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeaderSessionLess.jsp" />

<jsp:useBean id="login" scope="request" class="fr.paris.lutece.portal.web.user.AdminLoginJspBean" />

<% response.sendRedirect( login.doLogout( request ));  %>

<%@ include file="AdminFooter.jsp" %>

