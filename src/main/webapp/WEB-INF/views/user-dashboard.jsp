<%--
  Created by IntelliJ IDEA.
  User: Lukasz
  Date: 08.06.2022
  Time: 07:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jspf/user-header.jspf" %>


<p>
    Number of estimates: ${numberOfEstimates}
</p>
<p>
    <c:forEach items="${estimates}" var="estimate">
        - <a href="/user/estimateform?estimateId=${estimate.id} ">${estimate.name}</a>

        <br>
    </c:forEach>
</p>

</body>
</html>
