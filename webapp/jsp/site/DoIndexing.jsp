<jsp:include page="AdminHeader.jsp" />

<jsp:useBean id="indexing" scope="session" class="fr.paris.lutece.portal.web.indexer.IndexerJspBean" />
<jsp:useBean id="user" scope="session" class="fr.paris.lutece.portal.web.user.UserJspBean" />

<%
    if( user.check( "DEF_INDEXER" ) )
    {
%>
       <%= indexing.doIndexing( request ) %>
<%
    }
    else
    {
        response.sendRedirect( user.getAccessDeniedUrl() );
    }
%>


<%@ include file="../AdminFooter.jsp" %>