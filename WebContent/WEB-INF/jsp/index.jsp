<html>
<head>
<%@ include file="header.jsp" %>
</head>
<body>
	<div style="height: 30%;text-align: center;">
		<div style="height: 50%;text-align: center;">
		</div>
		<font size="20px;">${message}</font>
	</div>
	

	<div class="container-fluid" align="center">
		<form style="width: 30em;" method="post" action="login" name = "userD">
		  <div class="form-group row">
		    <label for="inputAccount" class="col-sm-2 col-form-label">Account</label>
		    <div class="col-sm-10">
		      <input type="text" class="form-control" id="inputAccount"  name = "account" placeholder="Account" required>
		    </div>
		  </div>
		  <div class="form-group row">
		    <label for="inputPassword" class="col-sm-2 col-form-label">Password</label>
		    <div class="col-sm-10">
		      <input type="password" class="form-control" id="inputPassword" name = "password" placeholder="Password" required>
		    </div>
		  </div>
		   <button type="submit" class="btn btn-primary mb-2" style="float: right;">Confirm</button>
		</form>
	
	
	</div>
	<!-- <div style="text-align: center">
		<h2>
			Hey You..!! This is your 1st Spring MCV Tutorial..<br> <br>
		</h2>
		<h3>
			<a href="welcome.html">Click here to See Welcome Message... </a>(to
			check Spring MVC Controller... @RequestMapping("/welcome"))
		</h3>
	</div> -->
	
<%@ include file="footer.jsp" %>
</body>
</html>