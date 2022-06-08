<%--
  Created by IntelliJ IDEA.
  User: Lukasz
  Date: 05.06.2022
  Time: 10:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View file</title>
</head>
<body>

<h2>Submitted File</h2>
<table>
  <tr>
    <td>OriginalFileName:</td>
    <td>${file.originalFilename}</td>
  </tr>
  <tr>
    <td>Type:</td>
    <td>${file.contentType}</td>
  </tr>
</table>

</body>
</html>
