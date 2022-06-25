
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jspf/admin-header.jspf" %>

<main>
    <div class="container-fluid px-4">
        <h1 class="mt-4">Assign columns from file to columns in DB:</h1>
        <ol class="breadcrumb mb-4">
        </ol>
        <div class="row">
            <div class="col-xl-3 col-md-6">
                <div class="card bg-primary text-white mb-4">
                    <div class="card-body">
                        <form method="post" action="/admin/uploadfile" enctype="multipart/form-data">
                            SELECT A PRESET:
                            <select name="preset" class="btn bg-white">
                                    <option value="-">By selected columns</option>
                                    <option value="legrand">Predefined - Legrand</option>
                            </select>
                            </p>
                            OR SELECT COLUMNS INDIVIDUALLY:
                            <p>
                            referenceNumber -
                            <select name="referenceNumber" class="btn bg-white">
                                <c:forEach items="${columnsFromFile}" var="columnName">
                                    <option value="${columnName.key}">${columnName.value}</option>
                                </c:forEach>
                            </select>
                            </p>
                            <p>
                            description -
                            <select name="description" class="btn bg-white">
                                <c:forEach items="${columnsFromFile}" var="columnName">
                                    <option value="${columnName.key}">${columnName.value}</option>
                                </c:forEach>
                            </select>
                            </p>
                            <p>
                            brand -
                            <select name="brand" class="btn bg-white">
                                <c:forEach items="${columnsFromFile}" var="columnName">
                                    <option value="${columnName.key}">${columnName.value}</option>
                                </c:forEach>
                            </select>
                            </p>
                            <p>
                                comment -
                            <select name="comment" class="btn bg-white">
                                <c:forEach items="${columnsFromFile}" var="columnName">
                                    <option value="${columnName.key}">${columnName.value}</option>
                                </c:forEach>
                            </select>
                            </p>
                            <p>
                                unitNetPrice -
                            <select name="unitNetPrice" class="btn bg-white">
                                <c:forEach items="${columnsFromFile}" var="columnName">
                                    <option value="${columnName.key}">${columnName.value}</option>
                                </c:forEach>
                            </select
                            </p>
                            <p>
                                unit -
                            <select name="unit" class="btn bg-white">
                                <c:forEach items="${columnsFromFile}" var="columnName">
                                    <option value="${columnName.key}">${columnName.value}</option>
                                </c:forEach>
                            </select>
                            </p>
                            <p>
                                baseVatRate -
                            <select name="baseVatRate" class="btn bg-white">
                                <c:forEach items="${columnsFromFile}" var="columnName">
                                    <option value="${columnName.key}">${columnName.value}</option>
                                </c:forEach>
                            </select>
                            </p>
                            <p>
                            <input name="button" value="Save to DB" type="submit" class="btn bg-white">
                            </p>

                            <input type="hidden" name="firstRowIsColumnsNames" value="${firstRowIsColumnsNames}">
                            <input type="hidden" name="fileName" value="${fileName}">
<%--                            <input type="hidden" name="file" value="${file}">--%>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="row"></div>
    </div>
</main>

<%@ include file="/WEB-INF/jspf/common-footer.jspf" %>
