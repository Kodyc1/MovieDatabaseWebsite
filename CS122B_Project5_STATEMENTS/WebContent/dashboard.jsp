<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Dashboard</title>
	<link rel="stylesheet" type="text/css" href="mystyle.css">
</head>
<body>

	<h2 style="display: inline-block;z-index: 1;padding:1em;"><a href="dashboard">Dashboard</a></h2>
	</br>
	
	
	<form ACTION = "StoredProcedure" METHOD = "GET" style="position:relative; padding-left:10em; display:block;">
			<div>
				<input type = "text" placeholder = "First Name" name = "fname" required>
				<input type = "text" placeholder = "Last Name" name = "lname">
				<button style="margin: 1em;" type = "submit" class = "button">Insert Star</button> 
				<input type = "hidden" name = "star" value="star"/>
			</div>
	</form>
	
	<form ACTION = "StoredProcedure" METHOD = "GET" style="position:relative; padding-left:10em; display:block;">
			<div>
				<input type = "text" placeholder = "Title" name = "title" required>
				<input type = "number" placeholder = "Year" name = "year" required>
				<input type = "text" placeholder = "Director" name = "director" required>
				
				<input type = "text" placeholder = "First Name" name = "fname" required>
				<input type = "text" placeholder = "Last Name" name = "lname" required>
				
				<input type = "text" placeholder = "Genre Name" name = "gname" required>
				
				<button style="margin: 1em;" type = "submit" class = "button">Add Movie</button> 
				<input type = "hidden" name = "movie" value="movie"/>
				
			</div>
	</form>
	
	
	<form ACTION = "StoredProcedure" METHOD = "GET" style="position:relative; padding-left:10em; display:block;">
			<div>
				<button style="margin: 1em;" type = "submit" class = "button">Show Metadata</button> 
				<input type = "hidden" name = "meta" value="meta"/>
			</div>
	</form>

</body>
</html>