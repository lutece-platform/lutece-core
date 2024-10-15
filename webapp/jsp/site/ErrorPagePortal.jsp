<%@ page isErrorPage="true" %>
<jsp:include page="PortalHeader.jsp" />

${ portalJspBean.getError500Page( pageContext.request, pageContext.exception ) }
