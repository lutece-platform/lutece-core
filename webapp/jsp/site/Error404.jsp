<%@ page errorPage="ErrorPagePortal.jsp" %>
<jsp:include page="PortalHeader.jsp" />

${ portalJspBean.getError404Page( pageContext.request ) }
