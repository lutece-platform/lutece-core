<%@ page errorPage="../ErrorPage.jsp" %>
${ mailingListJspBean.init( pageContext.request, MailingListJspBean.RIGHT_MANAGE_MAILINGLISTS ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.mailinglist.MailingListJspBean"%>

${ mailingListJspBean.getModifyMailinglist( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>