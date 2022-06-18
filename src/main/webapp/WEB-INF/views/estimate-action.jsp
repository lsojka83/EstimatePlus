
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jspf/user-header.jspf" %>
<main>
    <div class="container-fluid px-4">
        <h1 class="mt-4">Choose action:</h1>
        <ol class="breadcrumb mb-4">
        </ol>
        <form action="/user/estimate" method="post">
        <div class="row">
            <div class="col-xl-3 col-md-6">
                <div class="card bg-success text-white mb-4">
                    <div class="card-body">
                        1. Create new estimate:
                        <input name="button" value="Create new" type="submit">
                    </div>

                </div>
                <div class="card bg-primary text-white mb-4">
                    <div class="card-body">
                        2. Select estimate to edit:
                        <select name="selectedEstimate">
                            <c:forEach items="${estimatesNames}" var="name">
                                <option value="${name}">${name}</option>
                            </c:forEach>
                        </select>
                        <input name="button" value="Edit" type="submit">
                    </div>
                </div>
            </div>
        </div>
        </form>
        <div class="row"></div>
    </div>
</main>

<%@ include file="/WEB-INF/jspf/common-footer.jspf" %>