<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="/WEB-INF/jspf/user-header.jspf" %>
<span>estimateId=${estimate.id}</span>
<p>
    <form:form method="post" modelAttribute="estimate" action="/user/estimateform">
<div>
    <label for="name">Name</label>
    <form:input path="name" type="text" ></form:input>
    <form:errors path="name"/>
</div>

<table border="1px">
    <thead>
    <tr>
        <th>Id</th>
        <th>Reference Number</th>
        <th>Description</th>
        <th>Brand</th>
        <th>Unit Net Price</th>
        <th>Quantity</th>
        <th>Unit</th>
        <th>Total price</th>
        <th>Vat Rate</th>
        <th>Comment</th>
        <th>Actions</th>

    </tr>
    </thead>
    <tbody>
    <c:forEach var="estimateItem" items="${estimate.estimateItems}">
        <tr>
            <td>${estimateItem.id}</td>
            <td>${estimateItem.priceListItem.referenceNumber}</td>
            <td>${estimateItem.priceListItem.description}</td>
            <td>${estimateItem.priceListItem.brand}</td>
            <td>${estimateItem.priceListItem.unitNetPrice}</td>
            <td>${estimateItem.quantity}</td>
            <td>${estimateItem.priceListItem.unit}</td>
            <td>${estimateItem.totalNetPrice}</td>
            <td>${estimateItem.individualVatRate}</td>
            <td>${estimateItem.priceListItem.comment}</td>
            <td>
                <a href="/user/editestimateitem?id=${estimateItem.id}&piId=${estimateItem.priceListItem.id}&refNo=${estimateItem.priceListItem.referenceNumber}&estimateId=${estimate.id}">Edit</a>
                <a href="/user/deleteestimateitem?id=${estimateItem.id}&piId=${estimateItem.priceListItem.id}&refNo=${estimateItem.priceListItem.referenceNumber}&estimateId=${estimate.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>


<div>Total Net amount: ${estimate.totalNetAmount}</div>
<div>Total VAT: ${estimate.totalVatAmount}</div>
<div>Total Gross amount: ${estimate.totalGrossAmount}</div>
<div>Number of items: ${estimate.numberOfItems}</div>

        <input name="estimateId" value="${estimate.id}" hidden>
<%--        <form:hidden path="id" name="estimateId"  value="${estimate.id}"/>--%>

<br>
<div>
    <form:button name="button" value="save">Save estimate to DB</form:button>
    <form:button name="button" value="delete">Delete estimate from DB</form:button>
    <form:button name="button" value="download">Download as Excel</form:button>

</div>
</form:form>
</p>

<%--<a href="data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8,${excelFile}" download="${estimate.name}.xlsx">Save</a>--%>
<%--<a href="data:application/xml;charset=utf-8,${excelFile}" download="${estimate.name}.xlsx">Save</a>--%>


<%--<form method="post" action="/user/estimateform">--%>
<%--    <button name="button" value="save" type="submit">Save to DB</button>--%>
<%--</form>--%>
<%--</p>--%>

<p>
<h4>Find item by reference number:</h4>
<form method="post" action="/user/estimateform">
    <input value="" name="searchedItemReferenceNumber" type="text"/>
    <input name="estimateId" value="${estimate.id}" hidden>
    <button name="button" value="findPriceListItem" type="submit">Search</button>
<%--</form>--%>
</p>

<p>
<%--<form method="post" action="/user/estimateform" name="post">--%>
<p>
<table border="1px">
    <thead>
    <tr>
        <th>Select one:</th>
        <th>Id</th>
        <th>Reference Number</th>
        <th>Description</th>
        <th>Brand</th>
        <th>Comment</th>
        <th>Unit Net Price</th>
        <th>Unit</th>
        <th>Vat Rate</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${searchResult}" var="priceListItem" varStatus="loopStatus">
    <tr>
        <td>
            <c:choose>
                <c:when test="${loopStatus.isFirst()}">
<%--                    <input type="radio" name="priceListItemId" value="${priceListItem.id}" checked>--%>
                    <input type="radio" name="priceListItemId" value="${priceListItem.id}" checked></input>
                </c:when>
                <c:otherwise>
                    <input type="radio" name="priceListItemId" value="${priceListItem.id}"></input>
                </c:otherwise>
            </c:choose>
        </td>
        <td>${priceListItem.id}</td>
        <td>${priceListItem.referenceNumber}</td>
        <td>${priceListItem.description}</td>
        <td>${priceListItem.brand}</td>
        <td>${priceListItem.comment}</td>
        <td>${priceListItem.unitNetPrice}</td>
        <td>${priceListItem.unit}</td>
        <td>${priceListItem.baseVatRate}</td>
    </tr>
    </c:forEach>
    </tbody>
</table>

<%--    <input value="${priceListItem.id}" name="priceListItemId" type="text" hidden/>--%>
<%--    <input value="${priceListItem.id}" name="priceListItemId" type="text"/>--%>
<%--    <input value="selection" name="priceListItemId" type="text" hidden/>--%>
    <input name="estimateId" value="${estimate.id}" hidden>
    <button name="button" value="addEstimateItem" type="submit">Add item</button>
</form>
</p>
</p>

</body>
</html>
