<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ include file="/WEB-INF/jspf/user-header.jspf" %>


<div id="layoutAuthentication">
    <div id="layoutAuthentication_content">
<main>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-5">
                <div class="card shadow-lg border-0 rounded-lg mt-5">
                            <div class="card-header"><h3 class="text-center font-weight-light my-4">Enter new/edited item data</h3></div>
                            <div class="card-body">

                <form:form method="post" modelAttribute="userPriceListItem">

                    <div class="form-floating mb-3">
                            <div class="form-floating mb-3 mb-md-0">
                                <form:input path="referenceNumber" id="referenceNumber"
                                            type="text" cssClass="form-control"></form:input>
                                <form:errors path="referenceNumber"/>
                                <label for="referenceNumber">Reference number</label>
                        </div>

                            <div class="form-floating mb-3">
                                <form:input path="description" id="description"
                                            type="text" cssClass="form-control"></form:input>
                                <form:errors path="description"/>
                                <label for="description">Description</label>
                            </div>

                            <div class="form-floating mb-3">
                                <form:input path="brand" id="brand" type="text" cssClass="form-control"></form:input>
                                <form:errors path="brand"/>
                                <label for="brand">Brand</label>


                            <div class="form-floating mb-3">
                                <form:input path="comment" id="comment"
                                            type="text" cssClass="form-control"></form:input>
                                <form:errors path="comment"/>
                                <label for="comment">Comment</label>
                            </div>


                            <div class="form-floating mb-3">
                                <form:input path="unitNetPrice" id="unitNetPrice"
                                            type="number" step="0.01" cssClass="form-control"></form:input>
                                <form:errors path="unitNetPrice"/>
                                <label for="unitNetPrice">Unit price</label>
                            </div>



                            <div class="form-floating mb-3">
                                <form:input path="unit" id="unit" type="text" cssClass="form-control"/>
                                <form:errors path="unit"/>
                                <label for="unit">Unit</label>
                            </div>


                            <div class="form-floating mb-3">
                                <form:input path="baseVatRate" id="baseVatRate"
                                            type="number" cssClass="form-control"></form:input>
                                <form:errors path="baseVatRate"/>
                                <label for="baseVatRate">VAT</label>
                            </div>

                                <form:hidden path="addedOn" value="${addedOn}"></form:hidden>
                                <form:input path="vendorName" value="${userName}"
                                hidden="true"></form:input>
                    <div>
                        <button type="submit" class="btn btn-primary btn-block">Save</button>
                    </div>
                </form:form>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </main>

        </main>
    </div>

<%@ include file="/WEB-INF/jspf/common-footer.jspf" %>

