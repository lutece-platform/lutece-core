<jsp:useBean id="adminFooter" scope="request" class="fr.paris.lutece.portal.web.admin.AdminMenuJspBean" />
<%-- Display the admin footer --%>
<%= adminFooter.getAdminMenuFooter( request ) %>
</body>
</html>