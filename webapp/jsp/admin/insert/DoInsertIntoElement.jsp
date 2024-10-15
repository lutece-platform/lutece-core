<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../PortletAdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.insert.InsertServiceSelectorJspBean"%>

${ insertServiceSelectorJspBean.init( pageContext.request, InsertServiceSelectorJspBean.RIGHT_MANAGE_LINK_SERVICE ) }
${ insertServiceSelectorJspBean.doInsertIntoElement( pageContext.request ) }
</div>
</body>
</html>
