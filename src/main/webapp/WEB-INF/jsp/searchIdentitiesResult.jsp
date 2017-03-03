<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<h1 class="page-header">Search Identity Result</h1>

<div class="row placeholders"></div>
<c:if test="${empty identityList}">
	<div class="alert alert-danger" role="alert">No result found
	</div>
</c:if>

<div class="table-responsive">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>Id</th>
				<th>First Name</th>
				<th>Last Name</th>
				<th>Email Id</th>
				<th>Birth Date</th>
				<c:forEach var="attribute" items="${attributes}">
				<th><c:out value="${attribute}" /></th>
				</c:forEach>
				<th>Edit</th>
				<th>Delete</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="identity" items="${identityList}">
				<tr>
					<td><c:out value="${identity.id}" /></td>
					<td><c:out value="${identity.firstName}" /></td>
					<td><c:out value="${identity.lastName}" /></td>
					<td><c:out value="${identity.email}" /></td>
					<fmt:formatDate value="${identity.birthDate}" pattern="yyyy-MM-dd"
						var="formattedBirthDate" />
					<td><c:out value="${formattedBirthDate}" /></td>
					<c:forEach var="attribute" items="${attributes}">
						<td><c:out value="${identity.attributes[attribute]}" /></td>
					</c:forEach>	
					<td><a
						href="updateIdentity?id=<c:out value="${identity.id}" />"><span
							class="glyphicon glyphicon-edit" aria-hidden="true"></span></a></td>
					<td><a
						href="deleteIdentity?id=<c:out value="${identity.id}" />"><span
							class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

