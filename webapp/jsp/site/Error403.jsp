<%@ page errorPage="ErrorPagePortal.jsp" %>
<jsp:include page="PortalHeader.jsp" />

${ portalJspBean.getError403Page( pageContext.request ) }
