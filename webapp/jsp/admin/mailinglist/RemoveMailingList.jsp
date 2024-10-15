<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.mailinglist.MailingListJspBean"%>

${ mailingListJspBean.init( pageContext.request, MailingListJspBean.RIGHT_MANAGE_MAILINGLISTS ) }
${ pageContext.response.sendRedirect( mailingListJspBean.getConfirmRemoveMailingList( pageContext.request )) }
