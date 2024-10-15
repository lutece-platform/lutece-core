<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.mailinglist.MailingListJspBean"%>

${ mailingListJspBean.init( pageContext.request, MailingListJspBean.RIGHT_MANAGE_MAILINGLISTS ) }
${ mailingListJspBean.getManageMailinglists( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>