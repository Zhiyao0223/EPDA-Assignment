<%--
    Document   : checkSession
    Created on : Mar 11, 2024, 12:55:19 AM
    Author     : USER

    This file is used to prevent unauthorized login by hardcording web URL.
--%>

<%@ page import="javax.servlet.http.*" %>
<%
    if (session == null || session.getAttribute("user") == null) { %>
<script async>
    (async () => {
        await alert("Unauthorized access.");
        window.location.href = "login.jsp";
    })();
</script>
<% }%>
