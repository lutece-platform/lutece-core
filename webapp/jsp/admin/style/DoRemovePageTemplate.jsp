<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.style.PageTemplatesJspBean"%>

${ pageTemplatesJspBean.init( pageContext.request, PageTemplatesJspBean.RIGHT_MANAGE_PAGE_TEMPLATES ) }
${ pageContext.response.sendRedirect( pageTemplatesJspBean.doRemovePageTemplate( pageContext.request )) }
