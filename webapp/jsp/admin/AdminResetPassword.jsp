<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeaderSessionLess.jsp" />

<jsp:useBean id="login" scope="request" class="fr.paris.lutece.portal.web.user.AdminLoginJspBean" />

<%= login.getResetPassword( request ) %>

<%@ include file="AdminFooter.jsp" %>

