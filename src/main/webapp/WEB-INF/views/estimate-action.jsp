
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jspf/user-header.jspf" %>
<h2>
    Choose action:
</h2>

<p>

<form action="/user/estimate" method="post">
<%--    <form:button name="button" value="new">Create new</form:button>--%>
    1. Create new estimate:
    <input name="button" value="Create new" type="submit">

<%--    <button id="button" name=button" value="new" type="submit">Create new</button>--%>
<%--</form>--%>
</p>

<p>
    2. Select estimate to edit:
    <p>

    <select name="selectedEstimate">
        <c:forEach items="${estimatesNames}" var="name">
            <option value="${name}">${name}</option>
        </c:forEach>
    </select>
    <input name="button" value="Edit" type="submit">

<%--    <button name=button" value="edit" type="submit">Edit</button>--%>


<%--    <form:form method="post" modelAttribute="user" action="/user/estimate">--%>
<%--&lt;%&ndash;    <label for="estimates">Estimates</label>&ndash;%&gt;--%>
<%--    <form:select path="estimates" items="${estimates}" itemLabel="name" itemValue="id" multiple="false"></form:select>--%>
<%--    <form:button name="button" value="edit">Edit</form:button>--%>
<%--    </form:form>--%>
    </p>
</p>
</form>

</body>
</html>
