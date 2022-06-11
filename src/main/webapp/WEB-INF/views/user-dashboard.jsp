
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jspf/user-header.jspf" %>


<p>
    Number of estimates: ${numberOfEstimates}
</p>
<p>
    User estimates:
    <br>
    <c:forEach items="${estimates}" var="estimate">
        - <a href="/user/estimateform?estimateId=${estimate.id} ">${estimate.name}</a>

        <br>
    </c:forEach>
</p>

</body>
</html>
