<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <title>Login - Estimate+</title>
    <link href="<c:url value="/theme/css/styles.css"/>" rel="stylesheet" type="text/css">
    <script src="https://use.fontawesome.com/releases/v6.1.0/js/all.js" crossorigin="anonymous"></script>
</head>
<body class="bg-primary">
<div id="layoutAuthentication">
    <div id="layoutAuthentication_content">
        <main>
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-lg-7">
                        <div class="card shadow-lg border-0 rounded-lg mt-5">
                            <div class="card-header"><h3 class="text-center font-weight-light my-4">Create Account</h3></div>
                            <div class="card-body">
                                <form:form method="post" modelAttribute="user">
                                    <div class="form-floating mb-3">
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
                                    <div class="form-floating mb-3">
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
                            <div class="card-footer text-center py-3">
                                <div class="small"><a href="/">Have an account? Go to login</a></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
    <div id="layoutAuthentication_footer">
        <footer class="py-4 bg-light mt-auto">
            <div class="container-fluid px-4">
                <div class="d-flex align-items-center justify-content-between small">
                    <div class="text-muted">Copyright &copy;Estimate+ 2022</div>
                      </div>
            </div>
        </footer>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
<script src="<c:url value="/theme/js/scripts.js"/>" defer></script>
</body>
</html>