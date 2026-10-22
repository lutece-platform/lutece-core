<%@ page errorPage="ErrorPagePortal.jsp" %>
<jsp:include page="PortalHeader.jsp" />

${ portalJspBean.getError405Page( pageContext.request ) }
