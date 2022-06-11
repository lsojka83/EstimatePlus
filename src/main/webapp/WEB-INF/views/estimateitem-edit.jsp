<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ include file="/WEB-INF/jspf/user-header.jspf" %>

<%--<span>EstimateItemId: ${estimateItem.id}</span>--%>
<span>EstimateId: ${estimate.id}</span>
<span>EstimateId: ${estimateId}</span>
<form:form method="post" modelAttribute="estimateItem">
    <div>
        <label for="individualVatRate">Individual VAT rate</label>
        <form:input path="individualVatRate" id="individualVatRate" type="number"></form:input>
        <form:errors path="individualVatRate"/>
    </div>
    <div>
        <label for="quantity">Quantity</label>
        <form:input path="quantity" id="quantity" type="number"></form:input>
        <form:errors path="quantity"/>
    </div>

    <input name="priceListItemId" value="${estimateItem.priceListItem.id}" type="number" hidden>
    <input name="estimateId" value="${estimateId}" hidden>

    <div>
        <button type="submit">Save</button>
    </div>
</form:form>

</body>
</html>

