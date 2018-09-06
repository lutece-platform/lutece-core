<jsp:useBean id="triggers" scope="session" class="fr.paris.lutece.portal.web.system.TriggersJspBean" />
<% triggers.init( request , fr.paris.lutece.portal.web.system.DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT ); %>
<% String strContent = triggers.processController ( request , response ); %>

<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../AdminFooter.jsp" %>
