

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Servlet implementation class Checkout
 */
@WebServlet("/Checkout")
public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// "INSERT INTO sales(customer_id, movie_id, sale_date) VALUES(" + creditcardid + "," + movieid + ",CURDATE())";

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    	{
    		doGet(request,response);
    	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    	String loginUser = "mytestuser";
//        String loginPasswd = "mypassword";
//        String loginUrl = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";
        
        response.setContentType("text/html");    // Response mime type

        HttpSession session = request.getSession();
		
		@SuppressWarnings("unchecked")
		LinkedHashMap<String,Integer> Cart = (LinkedHashMap<String,Integer>)session.getAttribute("Cart");
        
        
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        

        try
           {
              //Class.forName("org.gjt.mm.mysql.Driver");
              Class.forName("com.mysql.jdbc.Driver").newInstance();

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
              
              // Declare our statement
              PreparedStatement statement = null;
              
  	      	  String first_name = request.getParameter("fname");
  	      	  String last_name = request.getParameter("lname");
  	      	  String id = request.getParameter("ccid");
  	      	  String expiration = request.getParameter("exp");
  	      	  String query = "SELECT COUNT(*) from creditcards WHERE first_name = \"" + first_name + "\" AND last_name = \"" + last_name + "\" AND id = " + id + " AND expiration = \"" + expiration + "\"";
  	      	  
  	      	  //System.out.println(query);
  	      	  
  	      	  statement = dbcon.prepareStatement(query);
  	      	  
  	      	  ResultSet result;
  	      	  result = statement.executeQuery(query);
  	      	  int e = 0;
  	      	  while(result.next()){
  	      		  e = result.getInt(1);
  	      	  }
  	      	  
  	      	  
  	      	  
  	      	  if(e == 0){
  	      		 //response.sendRedirect("index.jsp?errorMessage=Invalid");
	  	      	RequestDispatcher rd = request.getRequestDispatcher("checkout.jsp");
	
	  	      	request.setAttribute("errormsg", "Invalid credentials");
	
	  	      	rd.forward(request, response);  

  	      	  }
  	      	  else{
  	      		  synchronized(Cart){
  	      			 
					
					@SuppressWarnings("rawtypes")
					Set set = Cart.entrySet();
					
					@SuppressWarnings("rawtypes")
					Iterator i = set.iterator();
					
					while(i.hasNext()){
						
						 @SuppressWarnings("unchecked")
						Map.Entry<String, Integer> me = (Map.Entry<String, Integer>)i.next();
						 
						 String customer = "SELECT id FROM customers WHERE cc_id = " + id;
						 
						 PreparedStatement find =  dbcon.prepareStatement(customer);
						 ResultSet rs = find.executeQuery(customer);
						 
						 int x = 0;
						 
						 while(rs.next()){
							 x = rs.getInt(1);
						 }
						 
						
		  	      		 String insert = "INSERT INTO sales(customer_id, movie_id, sale_date) VALUES(" + x + "," + me.getKey() + ",CURDATE())";
		  	      		 //System.out.println(insert);
		  	      		 PreparedStatement s = dbcon.prepareStatement(insert);

						 @SuppressWarnings("unused")
						int res = s.executeUpdate(insert);
						 
						 s.close();
						 find.close();
						 rs.close();
						
					}
  	      			  
  	      		  }

  	      		  statement.close();
  	      		  result.close();
  	      		  
	  	      		Cart = new LinkedHashMap<String, Integer>();
	  				session.setAttribute("Cart", Cart);

   	      		 response.sendRedirect("confirmation.html");	
  	      	  }
           }
  	      	  
        catch(java.lang.Exception ex)
            {
                out.println("<HTML>" +
                            "<HEAD><TITLE>" +
                            "MovieDB: Error" +
                            "</TITLE></HEAD>\n<BODY>" +
                            "<P>SQL error in doGet: " +
                            ex.getMessage() + "</P></BODY></HTML>");
                return;
            }
         out.close();
	
	
	
	
	}



}
