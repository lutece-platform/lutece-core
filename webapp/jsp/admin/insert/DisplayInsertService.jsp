<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="InsertServiceHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.insert.InsertServiceSelectorJspBean"%>

${ insertServiceSelectorJspBean.init( pageContext.request, InsertServiceSelectorJspBean.RIGHT_MANAGE_LINK_SERVICE ) }
${ insertServiceSelectorJspBean.displayService( pageContext.request ) }
</div>
</body>
</html>
