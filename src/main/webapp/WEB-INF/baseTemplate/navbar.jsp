<!-- <h3 class="nav navbar-nav navbar-right"> Datta Prasad</h3> -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<ul class="nav navbar-nav navbar-right">
	<li><a >Welcome ${pageContext.request.userPrincipal.name} </a></li>
	<li><a href="j_spring_security_logout">Logout</a></li>
</ul> 
<!-- <form class="navbar-form navbar-right">
	<input type="text" class="form-control" placeholder="Search...">
</form> -->