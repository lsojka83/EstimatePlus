<%@ page import="java.util.Collections" %>
<%@ page import="pl.estimateplus.entity.EstimateItem" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.ui.Model" %>
<%@ page import="pl.estimateplus.entity.Estimate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="/WEB-INF/jspf/user-header.jspf" %>

<main>
    <div class="container-fluid px-4">
        <form:form method="post" modelAttribute="estimate" action="/user/estimateform">
        <div class="card mb-4">
            <div class="card-body">

                <label for="name">Name</label>
                <form:input path="name" type="text"></form:input>
                <form:errors path="name"/>
<%--                estimateId=${estimate.id}--%>
            </div>
        </div>
        <div class="card mb-4">
            <div class="card-header">
                <i class="fas fa-table me-1"></i>
                Estimate content
            </div>
            <div class="card-body">
                <table id="datatablesSimple">
                    <thead>
                    <tr>
                        <th>Id</th>
                        <th>#</th>
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
<%--                    <%--%>
<%--                        List list = ((Estimate) model.getAttribute("estimate")).getEstimateItems();--%>
<%--                        list.sort(Comparator.comparing(EstimateItem::getPositionInEstimate));--%>
<%--                        model.setAttribute("estimate",list);--%>
<%--                    %>--%>
<%--                    <c:forEach var="estimateItem" items="${estimate.estimateItems}">--%>
                    <c:forEach var="estimateItem" items="${estimate.estimateItems}">
                        <tr>
                            <td>${estimateItem.id}</td>
                            <td>${estimateItem.positionInEstimate}</td>
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
                                <a href="/user/editestimateitem?id=${estimateItem.id}&piId=${estimateItem.priceListItem.id}&refNo=${estimateItem.priceListItem.referenceNumber}&estimateId=${estimate.id}"
                                   class="btn btn-warning">Edit</a>
                                <a href="/user/deleteestimateitem?id=${estimateItem.id}&piId=${estimateItem.priceListItem.id}&refNo=${estimateItem.priceListItem.referenceNumber}&estimateId=${estimate.id}"
                                   class="btn btn-danger" id="deleteLink" onclick="return confirm('Are you sure?')">Delete</a>
<%--                                   class="btn btn-danger" id="deleteLink" onclick="confirmActionDeleteLink()">Delete</a>--%>
                                <a href="/user/moveupestimateitem?id=${estimateItem.id}&piId=${estimateItem.priceListItem.id}"
                                   class="btn btn-success">Move up</a>
                                <a href="/user/movedownestimateitem?id=${estimateItem.id}&piId=${estimateItem.priceListItem.id}"
                                   class="btn btn-success">Move down</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="row">
                    <div class="col-xl-3 col-md-6">
                        <c:if test="${estimateChanged}">
                        <div class="card bg-danger text-white mb-4">
                            <div class="card-body">Estimate not saved! Click "Save estimate to DB" to save. </div>
                        </div>
                        </c:if>
                    </div>
                    <div class="col-xl-3 col-md-6">
                    </div>

                <div class="col-xl-3 col-md-6 align-items-end">
                    <div class="card bg-success text-white mb-4">
                        <div class="card-body ">
                            <div>Total Net amount: ${estimate.totalNetAmount}</div>
                            <div>Total VAT: ${estimate.totalVatAmount}</div>
                            <div>Total Gross amount: ${estimate.totalGrossAmount}</div>
                            <div>Number of items: ${estimate.numberOfItems}</div>
                        </div>
                    </div>
                </div>
                </div>

                <br>
                <div>
                    <button name="button" value="save" class="btn btn-success">Save estimate to DB</button>
                    <button name="button" value="delete" class="btn btn-danger" id="deleteButton" onclick="return confirm('Are you sure?')">Delete estimate from DB</button>
                    <button name="button" value="download" class="btn btn-primary">Download as Excel</button>
                </div>
                <input name="estimateId" value="${estimate.id}" hidden>
                </form:form>
                </p>
            </div>
        </div>


        <div class="card mb-4">
            <h4>Find item by reference number:</h4>
            <form method="post" action="/user/estimateform">
                <input value="" name="searchedItemReferenceNumber" class="form-control" type="text"
                       placeholder="Search for..." aria-label="Search for..." aria-describedby="btnNavbarSearch"/>
                <div class="input-group">
                    <input name="estimateId" value="${estimate.id}" hidden>
                    <button name="button" value="findPriceListItem" type="submit" id="btnNavbarSearch" class="btn btn-primary"
                            id="btnNavbarSearch">Search
                    </button>
                    <i class="fas fa-search"></i></button>
                </div>
                <c:if test="${not empty searchResult}">
                <div class="card-body">
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
                            <tr border="1px">
                                <td border="1px">
                                    <c:choose>
                                        <c:when test="${loopStatus.isFirst()}">
                                            <input type="radio" name="priceListItemId" value="${priceListItem.id}"
                                                   checked></input>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="priceListItemId"
                                                   value="${priceListItem.id}"></input>
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
                </div>
                    <button name="button" value="addEstimateItem" type="submit" class="btn btn-success">Add item
                    </button>
                </c:if>
                <input name="estimateId" value="${estimate.id}" hidden>
            </form>
        </div>
    </div>
</main>
<%@ include file="/WEB-INF/jspf/common-footer.jspf" %>
