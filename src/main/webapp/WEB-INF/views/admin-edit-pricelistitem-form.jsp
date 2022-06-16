<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ include file="/WEB-INF/jspf/admin-header.jspf" %>


<form:form method="post" modelAttribute="priceListItem">

  <div>
    <label for="referenceNumber">Reference number</label>
    <form:input path="referenceNumber" id="referenceNumber" type="text"></form:input>
    <form:errors path="referenceNumber"/>
  </div>
  <div>
    <label for="description">Description</label>
    <form:input path="description" id="description" type="text"></form:input>
    <form:errors path="description"/>
  </div>
  <div>
    <label for="brand">Brand</label>
    <form:input path="brand" id="brand" type="text"></form:input>
    <form:errors path="brand"/>
  </div>
  <div>
    <label for="comment">Comment</label>
    <form:input path="comment" id="comment" type="text"></form:input>
    <form:errors path="comment"/>
  </div>
  <div>
    <label for="unitNetPrice">Unit price</label>
    <form:input path="unitNetPrice" id="unitNetPrice" type="number"></form:input>
    <form:errors path="unitNetPrice"/>
  </div>
  <div>
    <label for="unit">Unit</label>
    <form:input path="unit" id="unit" type="text"/>
    <form:errors path="unit"/>
  </div>
  <div>
    <label for="baseVatRate">VAT</label>
    <form:input path="baseVatRate" id="baseVatRate" type="number"></form:input>
    <form:errors path="baseVatRate"/>
  </div>

    <form:hidden path="addedOn" value="${addedOn}"></form:hidden>

<%--    <input name="priceListId" value="${priceListId}" hidden>--%>

  <form:input path="vendorName" value="${userName}" hidden="true"></form:input>
  <div>
    <button type="submit">Save</button>
  </div>
</form:form>

</body>
</html>
