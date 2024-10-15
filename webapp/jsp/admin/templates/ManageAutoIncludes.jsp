<%@ page errorPage="../ErrorPage.jsp" %>

${ pageContext.setAttribute( 'strContent', autoIncludeJspBean.processController( pageContext.request , pageContext.response ) ) }

<jsp:include page="../AdminHeader.jsp" />

${ pageContext.getAttribute( 'strContent' ) }

<%@ include file="../AdminFooter.jsp" %>
