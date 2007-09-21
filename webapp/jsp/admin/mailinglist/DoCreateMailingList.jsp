<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="mailinglist" scope="session" class="fr.paris.lutece.portal.web.mailinglist.MailingListJspBean" />

<%
        mailinglist.init( request, mailinglist.RIGHT_MANAGE_MAILINGLISTS );
        response.sendRedirect( mailinglist.doCreateMailingList( request ) );
%>
