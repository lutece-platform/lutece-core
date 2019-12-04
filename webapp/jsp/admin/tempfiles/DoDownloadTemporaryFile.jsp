<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:useBean id="jspBean" scope="session" class="fr.paris.lutece.portal.web.tempfiles.TemporaryFilesJspBean" />
<%
jspBean.init( request, fr.paris.lutece.portal.web.tempfiles.TemporaryFilesJspBean.CORE_TEMP_FILES); 
response.sendRedirect( jspBean.doDownloadFile( request, response ) );
%>