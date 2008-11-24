<%@ page isErrorPage="true" %>

<%@ page import="fr.paris.lutece.portal.service.portal.PortalService" %>
<%@ page import="fr.paris.lutece.portal.service.template.AppTemplateService" %>        

<%
	PortalService.resetCache(  );
	AppTemplateService.resetCache(  );
%>