<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Home</title>
</head>
<body>
	<h1>RFI Application Login</h1>
	<P>The time on the server is ${serverTime}.</P>
	
	<form action="/controller/login" method="post">
	<div>
		<input type="text" id="username" name="username" /> <input
			type="password" id="password" name="password" /> <input type="submit" id="submit" />
	</div>
	</form>
</body>
</html>
