<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="pagetemplate" scope="session" class="fr.paris.lutece.portal.web.style.PageTemplatesJspBean" />

<% 
    pagetemplate.init( request , pagetemplate.RIGHT_MANAGE_PAGE_TEMPLATES ); 
    response.sendRedirect( pagetemplate.doCreatePageTemplate( request ) );
%>


