<%
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>