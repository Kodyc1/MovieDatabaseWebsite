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
	<title>Checkout</title>
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="mystyle.css"/>
</head>
<body style="background-color: lightgrey;">
	<h1 style="text-align:center;">Checkout</h1>
	<form ACTION = "Checkout" METHOD = "POST" >
		
		<label style="text-align:center;" class = "text"><b>First Name</b></label>
		<input style="margin:auto; display:block;text-align:center;" type = "text" placeholder = "First Name" name = "fname" required/>
		
		<label style="text-align:center;" class = "text"><b>Last Name</b></label>
		<input style="margin:auto; display:block;text-align:center;" type = "text" placeholder = "Last Name" name = "lname" required/>
		
		<label style="text-align:center;" class = "text"><b>Credit Card</b></label>
		<input style="margin:auto; display:block;text-align:center;" type = "number" placeholder = "Credit card number" name = "ccid" required/>
		
		<label style="text-align:center;" class = "text"><b>Expiration Date</b></label>
		<input style="margin:auto; display:block;text-align:center;margin-bottom:1em;" type = "date" placeholder = "yyyy-mm-dd" name = "exp" required/>
		
		<button style="margin:auto; display:block;" type = "submit" class = "button">Checkout</button> 
	</form>
	
	    <%
	   		if(request.getAttribute("errormsg")!=null)
	   		{
	       		out.println(request.getAttribute("errormsg"));
	   		}
		%>
	

</body>
</html>