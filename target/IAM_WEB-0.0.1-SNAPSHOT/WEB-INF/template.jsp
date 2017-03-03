	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<div class="form-group">
		<label class="control-label col-sm-2" for="token">token:</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="token" id="token"
	<c:forEach var="entry" items="${identity.attributes }" varStatus="status">        
      <c:if test="${entry.key == 'token'}">
		value="${entry.value}"
      </c:if>
</c:forEach>

   					placeholder="Enter token" 


				
				>
		</div>
	</div>