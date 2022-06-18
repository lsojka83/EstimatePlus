<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jspf/admin-header.jspf" %>

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
  <tr>
    <td>Error:</td>
    <td>${error}
    </td>
  </tr>
</table>

<%@ include file="/WEB-INF/jspf/common-footer.jspf" %>

