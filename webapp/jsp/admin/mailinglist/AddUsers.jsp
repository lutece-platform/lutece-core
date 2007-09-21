<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="mailinglist" scope="session" class="fr.paris.lutece.portal.web.mailinglist.MailingListJspBean" />

<% mailinglist.init( request, mailinglist.RIGHT_MANAGE_MAILINGLISTS ) ; %>
<%= mailinglist.getAddUsers( request ) %>

<%@ include file="../AdminFooter.jsp" %>