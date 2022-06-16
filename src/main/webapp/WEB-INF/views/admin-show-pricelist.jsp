<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jspf/admin-header.jspf" %>


<p>
    <span>Name: ${priceList.name}</span>
</p>
<p>
    <span>Number of items: ${priceList.numberOfItems}</span>
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
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="priceListItem" items="${priceList.priceListItems}">
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
            <td>
                <a href="/admin/edititem?id=${priceListItem.id}&priceListId=${priceList.id}">Edit</a>
                <a href="/admin/deleteitem?id=${priceListItem.id}&priceListId=${priceList.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
