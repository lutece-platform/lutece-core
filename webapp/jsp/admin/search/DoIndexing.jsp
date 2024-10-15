<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.search.SearchIndexationJspBean"%>

${ searchIndexationJspBean.init( pageContext.request, SearchIndexationJspBean.RIGHT_INDEXER ) }
${ searchIndexationJspBean.doIndexing( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>