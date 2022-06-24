<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ include file="/WEB-INF/jspf/admin-header.jspf" %>




<main>
    <div class="container-fluid px-4">
        <p>
            <c:if test="${!priceList.userOwned}" >
                <form:form method="get" modelAttribute="priceList" action="/admin/deletepricelist">
                    <button name="deletePriseListId" value="${priceList.id}" class="btn btn-danger"
                            id="deleteButton" onclick="return confirm('Are you sure?')"
                    >Delete Price List</button>
                </form:form>
            </c:if>
        </p>
        <div class="card mb-4">
            <div class="card-body">
                <span>Number of items: ${priceList.numberOfItems}</span>
            </div>
        </div>
        <div class="card mb-4">
            <div class="card-header">
                <i class="fas fa-table me-1"></i>
                Pricelist mame: ${priceList.name}
            </div>
            <div class="card-body">
                <table id="datatablesSimple">
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
                        <c:if test="${priceList.userOwned}">
                            <th>Actions</th>
                        </c:if>
                    </tr>
                    </thead>
                    <tfoot>
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
                        <c:if test="${priceList.userOwned}">
                            <th>Actions</th>
                        </c:if>
                    </tr>
                    </tfoot>
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
                            <c:if test="${!priceList.userOwned}">

                                <td>
                                    <a href="/admin/edititem?id=${priceListItem.id}&priceListId=${priceList.id}"
                                       class="btn btn-warning">Edit</a>
                                    <a href="/admin/deleteitem?id=${priceListItem.id}&priceListId=${priceList.id}"
                                       class="btn btn-danger"
                                       id="deleteLink" onclick="return confirm('Are you sure?')">Delete</a>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>
<%@ include file="/WEB-INF/jspf/common-footer.jspf" %>
