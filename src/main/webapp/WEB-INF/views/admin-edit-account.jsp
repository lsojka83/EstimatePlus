
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>



<%@ include file="/WEB-INF/jspf/admin-header.jspf" %>


<form:form method="post" modelAttribute="user">
    <div>
        <label for="userName">User name</label>
        <form:input path="userName" id="userName" type="text"></form:input>
        <form:errors path="userName"/>
    </div>
    <div>
        <label for="email">Email</label>
        <form:input path="email" id="email" type="text"></form:input>
        <form:errors path="email"/>
    </div>

    <div>
        <label for="password">Password</label>
        <form:input path="password" id="password" type="password"></form:input>
        <form:errors path="password"/>
        <c:if test="${not empty invalidPassword}">
            ${invalidPassword}
        </c:if>
    </div>

    <form:hidden path="admin"></form:hidden>

    <form:button type="submit">Save</form:button>

</form:form>

    </tbody>
</table>

</body>
</html>
