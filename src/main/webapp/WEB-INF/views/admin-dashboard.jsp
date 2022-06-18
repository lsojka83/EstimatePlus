<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jspf/admin-header.jspf" %>

                <main>
                    <div class="container-fluid px-4">
                        <h1 class="mt-4">Admin panel</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item active">UserId: ${user.id}</li>
                            <li class="breadcrumb-item active">Is an admin: ${user.admin}</li>
                        </ol>
                        <div class="row">
                            <div class="col-xl-3 col-md-6">
                                <div class="card bg-primary text-white mb-4">
                                    <div class="card-body"> Number of users in DB: ${userCount}</div>
                                </div>
                            </div>

                        </div>
                        <div class="row">
                            <div class="col-xl-3 col-md-6">
                                <div class="card bg-success text-white mb-4">
                                    <div class="card-body">Number of price lists in DB: ${priceListCount}</div>

                                </div>
                            </div>
                        </div>
                    </div>
                </main>

<%@ include file="/WEB-INF/jspf/common-footer.jspf" %>

