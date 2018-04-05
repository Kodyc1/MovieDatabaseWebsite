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
	<title>Dashboard Login</title>
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="mystyle.css"/>
</head>
<body style="background-color: lightgrey;">
	<h1 style="text-align:center;">Login to Dashboard</h1>
	<form ACTION = "Dashboard" METHOD = "POST" >
		
		<label style="text-align:center;" class = "text"><b>Email</b></label>
		<input style="margin:auto; display:block;text-align:center;" type = "text" placeholder = "Email" name = "email" required/>
		
		<label style="text-align:center;" class = "text"><b>Password</b></label>
		<input style="margin:auto; display:block;text-align:center;margin-bottom:1em;" type = "password" placeholder = "Password" name = "password" required/>

		<button style="margin:auto; display:block;" type = "submit" class = "button">Login</button> 
	</form>
	
	    <%
	   		if(request.getAttribute("errormsg")!=null)
	   		{
	       		out.println(request.getAttribute("errormsg"));
	   		}
		%>
	

</body>
</html>