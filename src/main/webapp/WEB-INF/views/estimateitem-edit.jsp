<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ include file="/WEB-INF/jspf/user-header.jspf" %>

<main>
    <div class="container-fluid px-4">
        <main>
            <span>EstimateId: ${estimate.id}</span>
            <span>EstimateId: ${estimateId}</span>
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-lg-7">
                        <div class="card shadow-lg border-0 rounded-lg mt-5">
                            <div class="card-header"><h3 class="text-center font-weight-light my-4">Enter edited item data</h3></div>
                            <div class="card-body">

                                <form:form method="post" modelAttribute="estimateItem">

                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <div class="form-floating mb-3 mb-md-0">
                                        <form:input path="individualVatRate" id="individualVatRate" type="number" cssClass="form-control"></form:input>
                                        <label for="individualVatRate">Individual VAT rate</label>
                                        <form:errors path="individualVatRate"/>
                                    </div>
                                    </div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <div class="form-floating mb-3 mb-md-0">
                                        <form:input path="quantity" id="quantity" type="number" cssClass="form-control"></form:input>
                                    <label for="quantity">Quantity</label>
                                    <form:errors path="quantity"/>
                                    </div>
                                        </div>
                                    </div>

                                    <input name="priceListItemId" value="${estimateItem.priceListItem.id}" type="number" hidden>
                                    <input name="estimateId" value="${estimateId}" hidden>
                                    <form:hidden path="positionInEstimate" value="${positionInEstimate}"></form:hidden>


                                    <div>
                                        <button type="submit" name="button" value="save" class="btn btn-primary btn-block">Save</button>
                                        <button type="submit" name="button" class="btn btn-primary btn-block">Save</button>
                                    </div>
                                </form:form>

                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>

</main>
