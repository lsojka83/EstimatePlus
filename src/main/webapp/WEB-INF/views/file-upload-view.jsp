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
${error}
</body>
</html>
