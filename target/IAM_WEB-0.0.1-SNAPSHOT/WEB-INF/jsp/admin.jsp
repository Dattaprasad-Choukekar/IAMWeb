<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<h1 class="page-header">List of Attributes</h1>

<div class="row placeholders"></div>

<!-- <h2 class="sub-header">List of Identities</h2> -->
<div class="table-responsive">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>Name</th>
				<th>Type</th>
				<th>Is Mandatory</th>
				<th>Delete</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="atribute" items="${attributeList}">
					<tr id="<c:out value="${atribute.id}" />">
					<td><c:out value="${atribute.name}" /></td>
					<td><c:out value="${atribute.type}" /></td>
					<td><c:out value="${atribute.isMandatory}" /></td>
					<td><a
						href="deleteAttribute?id=<c:out value="${atribute.id}" />"><span
							class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
