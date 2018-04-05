import java.io.*;
import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;


@WebServlet("/ShoppingCart")
public class ShoppingCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		
		@SuppressWarnings("unchecked")
		LinkedHashMap<String,Integer> Cart = (LinkedHashMap<String,Integer>)session.getAttribute("Cart");
				
		
		if(Cart == null){
			Cart = new LinkedHashMap<String, Integer>();
			session.setAttribute("Cart", Cart);
		}
		
		String id = request.getParameter("id");
		String quantity = request.getParameter("quantity");
			Integer number = null;
		String delete = request.getParameter("delete");
    	String newQuantity = request.getParameter("newQuantity");
		
		if (quantity != null){
			number = Integer.parseInt(quantity);
		}
		
	

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
	    
	    String title = "Shopping Cart";
	    String docType =
	      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
	      "Transitional//EN\">\n";
	    out.println(docType +
	                "<HTML>\n" +
	                "<HEAD><TITLE>" + title + "</TITLE>"
	                		+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"mystyle.css\">"
	                		+ "<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\"></HEAD>");
        out.println("<BODY style=\"background-color: lightgrey;\">" + "<h2 style=\"display: inline-block;z-index: 1;padding:1em;\"><a href=\"mainPage\">Fabflix</a></h2>"
        		+ "<h2 id=\"cart\" style=\"float:right;display: inline-block;z-index: 1;padding:1em;\">"
        		+ "<a href=\"ShoppingCart\">My Cart</a></h2>");
	    out.println("<h2 align=\"center\">Shopping Cart</h2>");
	    
	    
	    int total_movies = 0;
	   synchronized(Cart) {

	            out.println("<input type = \"hidden\" name = \"newQuantity\" value = \"" + newQuantity + "\">");
	            out.println("<input type = \"hidden\" name = \"id\" value = \"" + id + "\">");

	            @SuppressWarnings("unused")
				String uri = request.getScheme() + "://" +

	             request.getServerName() + 
	
	             ("http".equals(request.getScheme()) && request.getServerPort() == 80 || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "" : ":" + request.getServerPort() ) +
	
	             request.getRequestURI() +
	
	            (request.getQueryString() != null ? "?" + request.getQueryString() : "");
	            
	            //System.out.println(uri);
		   
			 if (id != null && quantity != null){
				 if (Cart.containsKey(id)) {
			 		Cart.put(id, Cart.get(id) + number); 	 	
				 }
				 else {
				 	Cart.put(id, number); 
				 }
			 }	
			 if (id != null && newQuantity !=null){
				
				if (Integer.parseInt(newQuantity) <= 0)
				{
					Cart.remove(id);
				} else {
					Cart.put(id,Integer.parseInt(newQuantity));
				}
			 }
			 if (id != null && delete != null){
			 	 Cart.remove(id); 
			 }
		
		
			 if (Cart.size() == 0) {
				  
			    out.println("<I>No items</I>");
			  
			 } 
			 else {
			  
				out.println("<table border align=\"center\">");
				out.println("<tr><td>Movie</td>"
						+ "<td>Price</td>"
						+ "<td>Quantity</td>"
						+ "</tr>");
				
				try{
				
		              Context initCtx = new InitialContext();
		        		
		              Context envCtx = (Context) initCtx.lookup("java:comp/env");


		              // Look up our data source
		              DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");

		              // the following commented lines are direct connections without pooling
		              //Class.forName("org.gjt.mm.mysql.Driver");
		              //Class.forName("com.mysql.jdbc.Driver").newInstance();
		              //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

		              if (ds == null)
		                  out.println("ds is null.");

		              Connection dbcon = ds.getConnection();
		              if (dbcon == null)
		                  out.println("dbcon is null.");
					
		            PreparedStatement statement = null;
		            ResultSet rs = null;
		            
					@SuppressWarnings("rawtypes")
					Set set = Cart.entrySet();
					
					@SuppressWarnings("rawtypes")
					Iterator i = set.iterator();
					
					while(i.hasNext()){
						@SuppressWarnings("unchecked")
						Map.Entry<String, Integer> me = (Map.Entry<String, Integer>)i.next();
						if (me.getValue() > 0){
				            String query = "SELECT title FROM movies WHERE id = " + me.getKey();
				            
				            statement = dbcon.prepareStatement(query);
							
				            rs = statement.executeQuery(query);
	
				            while(rs.next()){
				            	String movie_title = rs.getString("title");
				            	out.println("<tr><td><a href=MoviePage?id=" + me.getKey() + ">" + movie_title + "</a></td>"
				            			+ "<td>$100.00</td>"
				            			+ "<form action = \"ShoppingCart\" METHOD = \"GET\">"
				            			+ "<td><input type=\"number\" value=" + me.getValue() +" name = \"newQuantity\"></td>"
				        	            +"<input type = \"hidden\" name = \"id\" value = \"" + me.getKey() + "\">"
										 
				            			+ "<td><button type = \"submit\">"
										+ "Update"
										+ "</button></td>"
										+ "</form>"
								
										+ "<td>"
										+ "<a href=\"ShoppingCart?id=" + me.getKey() + "&delete=delete\"><button>Delete</button></a>"
										+ "</td>"
										
										+ "</tr>");
	
				            }
				            total_movies += me.getValue();
						}
					}
					
					statement.close();
					rs.close();
					
		        } 
	
		        catch (SQLException ex) {
		        	Cart = new LinkedHashMap<String, Integer>();
					session.setAttribute("Cart", Cart);
		            while (ex != null) {
		                  System.out.println ("SQL Exception:  " + ex.getMessage ());
		                  ex = ex.getNextException ();
		              } 
		        }  
		        catch(java.lang.Exception ex)
		          {
		        	
	//	              out.println("<HTML>" +
	//	                          "<HEAD><TITLE>" +
	//	                          "MovieDB: Error" +
	//	                          "</TITLE></HEAD>\n<BODY>" +
	//	                          "<P>SQL error in doGet: " +
	//	                          ex.getMessage() + "</P></BODY></HTML>");
		              return;
		        }
			    
				out.println("</table>");
				
				out.println("<h4 align=\"center\">Total: $" + (total_movies * 100) + ".00");

				
				out.println("<a href=\"checkout.jsp\"><button align=\"center\">Checkout</button></a>");
			  }
		}
	   
	   out.println("</BODY></HTML>");
       out.close();		

	}

}