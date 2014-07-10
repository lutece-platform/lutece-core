<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../PortletAdminHeader.jsp" />

<jsp:useBean id="insertServiceSelector" scope="session" class="fr.paris.lutece.portal.web.insert.InsertServiceSelectorJspBean" />

<% insertServiceSelector.init( request , insertServiceSelector.RIGHT_MANAGE_LINK_SERVICE ); %>
<%= insertServiceSelector.doInsertIntoElement( request ) %>
</div>
</body>
</html>
