<%--
  Created by IntelliJ IDEA.
  User: Lukasz
  Date: 04.06.2022
  Time: 15:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<html>--%>
<%--<head>--%>
<%--    <title>User price list</title>--%>
<%--</head>--%>
<%--<body>--%>
<%@ include file="/WEB-INF/jspf/user-header.jspf" %>


<p>
    <span>Name: ${userPriceList.name}</span>
</p>
<p>
    <span>Number of items: ${userPriceList.numberOfItems}</span>
</p>

<table border="1px">
    <thead>
    <tr>
        <th>Id</th>
        <th>vendorName</th>
        <th>referenceNumber</th>
        <th>description</th>
        <th>brand</th>
        <th>comment</th>
        <th>unitNetPrice</th>
        <th>unit</th>
        <th>baseVatRate</th>
        <th>addedOn</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="priceListItem" items="${userPriceList.priceListItems}">
        <tr>
            <td>${priceListItem.id}</td>
            <td>${priceListItem.vendorName}</td>
            <td>${priceListItem.referenceNumber}</td>
            <td>${priceListItem.description}</td>
            <td>${priceListItem.brand}</td>
            <td>${priceListItem.comment}</td>
            <td>${priceListItem.unitNetPrice}</td>
            <td>${priceListItem.unit}</td>
            <td>${priceListItem.baseVatRate}</td>
            <td>${priceListItem.addedOn}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
