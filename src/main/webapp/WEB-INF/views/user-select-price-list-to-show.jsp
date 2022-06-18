
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jspf/user-header.jspf" %>


<main>
    <div class="container-fluid px-4">
        <h1 class="mt-4">Choose a pricelist to show:</h1>
        <ol class="breadcrumb mb-4">
        </ol>
        <div class="row">
            <div class="col-xl-3 col-md-6">
                <div class="card bg-primary text-white mb-4">
                    <div class="card-body">
                        <form method="post" action="/user/showpricelist">
                            <select name="selectedPriceListId" class="btn bg-white">
                                <c:forEach items="${userAvailablePriceLists}" var="pricelist">
                                    <option value="${pricelist.id}">${pricelist.name}</option>
                                </c:forEach>
                            </select>
                            <input name="button" value="Show" type="submit" class="btn bg-white">
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="row"></div>
    </div>
</main>

<%@ include file="/WEB-INF/jspf/common-footer.jspf" %>
