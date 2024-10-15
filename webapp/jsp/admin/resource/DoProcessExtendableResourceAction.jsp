<%@ page errorPage="../ErrorPage.jsp" %>

${ pageContext.setAttribute( 'pluginActionResult', extendableResourceJspBean.doProcessExtendableResourceAction( pageContext.request, pageContext.response) ) }
${ not empty pageContext.getAttribute( 'pluginActionResult' ).redirect ? pageContext.response.sendRedirect( pageContext.getAttribute( 'pluginActionResult' ).redirect ) : '' }

<jsp:include page="../AdminHeader.jsp" />

${ not empty pageContext.getAttribute( 'pluginActionResult' ).htmlContent ? pageContext.getAttribute( 'pluginActionResult' ).htmlContent : '' }

<%@ include file="../AdminFooter.jsp" %>
