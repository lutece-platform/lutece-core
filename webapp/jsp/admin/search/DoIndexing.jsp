<%@ page errorPage="../ErrorPage.jsp" %>
${ searchIndexationJspBean.init( pageContext.request, SearchIndexationJspBean.RIGHT_INDEXER ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.search.SearchIndexationJspBean"%>

${ searchIndexationJspBean.doIndexing( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>