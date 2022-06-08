<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Upload file</title>
</head>
<body>

<form name="fileUpload" method="POST" action="/admin/uploadfile" enctype="multipart/form-data">
  <label>Select File</label> <br />
  <input type="file" name="file" />
  <input type="submit" name="submit" value="Upload" />
</form>

<input name="columnsNames" type="checkbox"/>

<%--<form:form method="POST" action="/admin/uploadfile" enctype="multipart/form-data">--%>
<%--    <table>--%>
<%--        <tr>--%>
<%--            <td><form:label path="file">Select a file to upload</form:label></td>--%>
<%--            <td><input type="file" name="file"/></td>--%>
<%--        </tr>--%>
<%--        <tr>--%>
<%--            <td><input type="submit" value="Submit"/></td>--%>
<%--        </tr>--%>
<%--    </table>--%>
<%--</form:form>--%>

</body>
</html>
