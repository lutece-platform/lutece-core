<%@ page isErrorPage="true" %>

<%@ page import="fr.paris.lutece.portal.service.search.IndexationService" %>

<%
	IndexationService.processIndexing( true );
%>
