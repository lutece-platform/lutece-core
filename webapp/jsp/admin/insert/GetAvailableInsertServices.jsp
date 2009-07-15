<%@ page errorPage="../ErrorPage.jsp" %>
<%@page import="fr.paris.lutece.portal.web.insert.InsertServiceSelectorJspBean"%>
<jsp:include page="InsertServiceHeader.jsp" />

<jsp:useBean id="insertServiceSelector" scope="session" class="fr.paris.lutece.portal.web.insert.InsertServiceSelectorJspBean" />

<% insertServiceSelector.init( request , InsertServiceSelectorJspBean.RIGHT_MANAGE_LINK_SERVICE ); %>
<%= insertServiceSelector.getServicesListPage( request ) %>

<%@ include file="../AdminFooter.jsp" %>
