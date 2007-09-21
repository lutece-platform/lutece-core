<%-- This JSP is displayed in a IFrame. It doesn't need the admin header and footer--%>
<%@ page errorPage="ErrorPage.jsp" %>

<jsp:useBean id="admin" scope="session" class="fr.paris.lutece.portal.web.admin.AdminPageJspBean" />
<jsp:useBean id="adminPagePortlet" scope="session" class="fr.paris.lutece.portal.web.admin.AdminPagePortletJspBean" />

<%
    admin.init( request , admin.RIGHT_MANAGE_ADMIN_SITE );
 %>

	<jsp:forward page="<%=adminPagePortlet.doModifyPortlet( request )%>"/>

