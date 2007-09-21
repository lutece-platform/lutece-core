<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="level" scope="session" class="fr.paris.lutece.portal.web.features.LevelsJspBean" />

<% 
        level.init( request , level.RIGHT_MANAGE_LEVELS ); 
        response.sendRedirect( level.doCreateLevel( request ) );
%>

