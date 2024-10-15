<%@page import="fr.paris.lutece.util.sql.TransactionManager"%>
<%@ page isErrorPage="true" %>

<jsp:include page="PortalHeader.jsp" />

<%
	TransactionManager.rollBackEveryTransaction( exception );
%>

${ portalJspBean.getError500Page( pageContext.request, pageContext.exception ) }
