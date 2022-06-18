<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ include file="/WEB-INF/jspf/admin-header.jspf" %>

<main>
    <div class="container-fluid px-4">
        <h1 class="mt-4">Pricelist upload from excel file</h1>
        <ol class="breadcrumb mb-4">
        </ol>
        <div class="row">
            <div class="col-xl-3 col-md-6">
                <div class="card bg-primary text-white mb-4">
                    <div class="card-body">
                        <form name="fileUpload" method="POST" action="/admin/uploadfile" enctype="multipart/form-data">
                            <label>Select File</label> <br />
                            <input type="file" name="file" class="btn btn-warning"/>
                            <input type="submit" name="submit" value="Upload" class="btn btn-success"/>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="row"></div>
    </div>
</main>

<%@ include file="/WEB-INF/jspf/common-footer.jspf" %>
