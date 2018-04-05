<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@page import="java.sql.*,
 javax.sql.*,
 java.io.IOException,
 javax.servlet.http.*,
 javax.servlet.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="mystyle.css"/>
	<title>LOGIN</title>
	<script src='https://www.google.com/recaptcha/api.js'></script>
</head>
<body style="background-color: lightgrey;">
	<div id="welcome">
		Welcome to Fabflix!
	</div>
	<form ACTION = "Login" METHOD = "POST" >

		<label style="text-align:center;" class = "text"><b>Username</b></label>
		<input style="margin:auto; display:block;text-align:center;" type = "text" placeholder = "Username" name = "uname" required/>
		
		<label style="text-align:center;" class = "text"><b> Password </b></label>
		<input style="margin:auto; display:block;text-align:center;margin-bottom:1em;" type = "password" placeholder = "Password" name = "pword" required/>
		
		<button style="margin:auto; display:block;" type = "submit" class = "button">Login</button> 
		
		<div style="width: 50%; margin:auto; padding-left: 3em;" class="g-recaptcha" data-sitekey="6Lcs6x8UAAAAAJYP2dGqvBMQRwvkCL5y79pPV6qi"></div>

	</form>
	    <%
    		if(request.getAttribute("errormsg")!=null)
    		{
        		out.println(request.getAttribute("errormsg"));
    		}
		%>
</body>
</html>


