<%@ page errorPage="ErrorPagePortal.jsp" %>

<jsp:include page="PortalHeader.jsp" />

${ portalJspBean.getLegalInfos( pageContext.request ) }
