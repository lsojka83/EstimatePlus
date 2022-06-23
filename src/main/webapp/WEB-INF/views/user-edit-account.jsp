
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>



<%@ include file="/WEB-INF/jspf/user-header.jspf" %>

<main>
    <div class="container-fluid px-4">
        <div id="layoutAuthentication">
            <div id="layoutAuthentication_content">
                <main>
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-lg-7">
                                <div class="card shadow-lg border-0 rounded-lg mt-5">
                                    <div class="card-header"><h3 class="text-center font-weight-light my-4">Edit
                                        Account</h3></div>
                                    <div class="card-body">

                                        <form:form method="post" modelAttribute="user">
                                                <div class="form-floating mb-3 mb-md-0">
                                                    <form:input path="userName" id="userName"
                                                                type="text" cssClass="form-control"></form:input>
                                                    <label for="userName">User name</label>
                                                    <form:errors path="userName"/>
                                                </div>
                                                <div class="form-floating mb-3">
                                                    <form:input path="email" id="email" type="text"  cssClass="form-control"></form:input>
                                                    <label for="email">Email</label>
                                                    <form:errors path="email"/>
                                                </div>
                                                <div class="form-floating mb-3 mb-md-0">
                                                    <form:input path="password" id="password" element="div" type="password" cssClass="form-control"></form:input>
                                                    <form:errors path="password" cssClass="form-control"/>
                                                    <label for="password">Password</label>
                                                    <c:if test="${not empty invalidPassword}">
                                                        ${invalidPassword}
                                                    </c:if>
                                                </div>

                                        <form:hidden path="admin"></form:hidden>


                                        <div class="mt-4 mb-0">
                                            <div class="d-grid">
                                                <div class="btn btn-primary btn-block">
                                                    <button type="submit" element="button" class="btn btn-primary btn-block">Save</button>
                                                </div>
                                            </div>
                                            </form:form>

                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                </main>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/jspf/common-footer.jspf" %>

