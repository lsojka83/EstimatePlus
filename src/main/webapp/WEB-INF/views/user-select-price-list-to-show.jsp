
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jspf/user-header.jspf" %>
<h2>
    Choose price list to show:
</h2>

<form method="post" action="/user/showpricelist">
<p>
    <select name="selectedPriceListId">
        <c:forEach items="${userAvailablePriceLists}" var="pricelist">
            <option value="${pricelist.id}">${pricelist.name}</option>
        </c:forEach>
    </select>
    <input name="button" value="Show" type="submit">
</p>
</form>

</body>
</html>
