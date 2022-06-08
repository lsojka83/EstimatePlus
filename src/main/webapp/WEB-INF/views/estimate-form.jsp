<%--
  Created by IntelliJ IDEA.
  User: Lukasz
  Date: 06.06.2022
  Time: 17:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<%--<html>--%>
<%--<head>--%>
<%--    <title>Estimate form</title>--%>
<%--</head>--%>
<%--<body>--%>
<%@ include file="/WEB-INF/jspf/user-header.jspf" %>

<%--<label for="estimateName">Estimane name:</label>--%>
<%--    <input id="estimateName" value="${estimate.name}" type="text">--%>
<p>
    <form:form method="post" modelAttribute="estimate">
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
        <th>Comment</th>
        <th>Unit Net Price</th>
        <th>Unit</th>
        <th>Vat Rate</th>
        <th>Quantity</th>
<%--        <th>Price</th>--%>
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
            <td>${estimateItem.priceListItem.comment}</td>
            <td>${estimateItem.priceListItem.unitNetPrice}</td>
            <td>${estimateItem.priceListItem.unit}</td>
            <td>${estimateItem.priceListItem.baseVatRate}</td>
            <td>${estimateItem.quantity}</td>
<%--            <td>${estimateItem.price}</td>--%>
            <td> <a href="/estimate/edit?id=${estimateItem.id}">Edit</a></td>
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


<div>
    <form:button name="button" value="save">Save to DB</form:button>
</div>
</form:form>
</p>

<%--<form method="post" action="/user/estimateform">--%>
<%--    <button name="button" value="save" type="submit">Save to DB</button>--%>
<%--</form>--%>
<%--</p>--%>

<p>
<h4>Find item by reference number:</h4>
<form method="post" action="/user/estimateform">
    <input value="" name="searchedItem" type="text"/>
    <input name="estimateId" value="${estimate.id}" hidden>

    <button name="button" value="findPriceListItem" type="submit">Search</button>
</form>
</p>

<p>
<p>
<table border="1px">
    <thead>
    <tr>
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
    <tr>
        <td>${priceListItem.id}</td>
        <td>${priceListItem.referenceNumber}</td>
        <td>${priceListItem.description}</td>
        <td>${priceListItem.brand}</td>
        <td>${priceListItem.comment}</td>
        <td>${priceListItem.unitNetPrice}</td>
        <td>${priceListItem.unit}</td>
        <td>${priceListItem.baseVatRate}</td>
    </tr>
    </tbody>
</table>
<form method="post" action="/user/estimateform" name="post">
    <input value="${priceListItem.id}" name="priceListItemId" type="text" hidden/>
    <input name="estimateId" value="${estimate.id}" hidden>
    <button name="button" value="addEstimateItem" type="submit">Add item</button>
</form>
</p>




</p>




</body>
</html>
