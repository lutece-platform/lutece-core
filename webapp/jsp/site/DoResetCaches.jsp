<%@ page isErrorPage="true" %>

<%@ page import="fr.paris.lutece.portal.service.cache.CacheService"%>
<%@ page import="fr.paris.lutece.portal.service.template.AppTemplateService" %>        

<%
	CacheService.resetCaches( );
	AppTemplateService.resetCache(  );
%>