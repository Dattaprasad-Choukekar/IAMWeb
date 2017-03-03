<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<h1 class="page-header">Update identity</h1>

<div class="row placeholders"></div>

<!-- <h2 class="sub-header">List of Identities</h2> -->
<form class="form-horizontal" role="form" action="" method="post">
	<input type="hidden" name="identityId" id="identityId"
		value="<c:out value="${identity.id}" />">
	<div class="form-group">
		<label class="control-label col-sm-2" for="firstName">First
			Name:</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="firstName"
				id="firstName" value="<c:out value="${identity.firstName}" />"   required>
		</div>
	</div>
	<div class="form-group">
		<label class="control-label col-sm-2" for="lastName">Last
			Name:</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="lastName" id="lastName"
				value="<c:out value="${identity.lastName}" />"  required>
		</div>
	</div>
	<div class="form-group">
		<label class="control-label col-sm-2" for="email">Email:</label>
		<div class="col-sm-10">
			<input type="email" class="form-control" name="email" id="email"
				value="<c:out value="${identity.email}" />">
		</div>
	</div>
	<div class="form-group">
		<label class="control-label col-sm-2" for="email">Email:</label>
		<div class="col-sm-10">
			<fmt:formatDate value="${identity.birthDate}" pattern="yyyy-MM-dd"
				var="formattedBirthDate" />
			<input type="date" class="form-control" name="birthDate"
				id="birthDate" value="<c:out value="${formattedBirthDate}" />">
		</div>
	</div>
	<jsp:include page="attributetemplate.jsp"/>
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-10">
			<button type="submit" class="btn btn-default">Update</button>
		</div>
	</div>
</form>
