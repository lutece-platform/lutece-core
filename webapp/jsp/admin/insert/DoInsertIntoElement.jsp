<%@ page errorPage="../ErrorPage.jsp" %>
${ insertServiceSelectorJspBean.init( pageContext.request, InsertServiceSelectorJspBean.RIGHT_MANAGE_LINK_SERVICE ) }
<jsp:include page="../PortletAdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.insert.InsertServiceSelectorJspBean"%>

${ insertServiceSelectorJspBean.doInsertIntoElement( pageContext.request ) }
</div>
</body>
</html>
