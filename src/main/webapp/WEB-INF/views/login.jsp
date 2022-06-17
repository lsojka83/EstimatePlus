<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
    <title>Login</title>
</head>

<style>
    .content {
        padding-top: 100px;
        max-width: 500px;
        margin: auto;
    }
</style>

<body class="content">
<h1>Estimate+</h1>
<h4>Login to start</h4>

<form method="post">

    <p>
        <label for="userName">User Name</label>
        <input name="userName" id="userName" type="text">
    </p>
    <p>
        <label for="password">Password</label>
        <input name="password" id="password" type="password">
    </p>

    <p>
        <c:if test="${not empty incorrectLoginData}">
            Wrong User Name or password! Try again.
        </c:if>

    </p>

    <table>
        <tr>
            <td>
                <p>
                    <button name="button" value="login">Login</button>
                </p>
            </td>
            <td>
                <p>
                    <button name="button" value="newAccount">New account</button>
                </p>
            </td>

        </tr>

    </table>


</form>

<p>
    <a href="/?userId=1">User1</a>
</p>
<p>
    <a href="/?userId=3">User2</a>
</p>

<a href="/?userId=2">Admin
</a>
</p>


</body>
</html>
