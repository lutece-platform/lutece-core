<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="style" scope="session" class="fr.paris.lutece.portal.web.style.StylesJspBean" />

<%
    style.init(request, style.RIGHT_MANAGE_STYLE );
    response.sendRedirect( style.doCreateStyle ( request ));
%>

