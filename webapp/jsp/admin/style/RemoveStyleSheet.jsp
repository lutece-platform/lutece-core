<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="stylesheet" scope="session" class="fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean" />

<% 
    stylesheet.init(request,  stylesheet.RIGHT_MANAGE_STYLESHEET  ) ; 
    response.sendRedirect( stylesheet.getRemoveStyleSheet( request ));
%>
