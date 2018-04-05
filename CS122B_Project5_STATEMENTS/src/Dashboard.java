

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class Checkout
 */
@WebServlet("/Dashboard")
public class Dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// "INSERT INTO sales(customer_id, movie_id, sale_date) VALUES(" + creditcardid + "," + movieid + ",CURDATE())";

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    	{
    		doGet(request,response);
    	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        response.setContentType("text/html");    // Response mime type

//        HttpSession session = request.getSession();
        
        
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try
           {
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
              
  	      	  String email = request.getParameter("email");
  	      	  String password = request.getParameter("password");
  	      	  
  	      	  String query = "SELECT COUNT(*) from employees WHERE email = \"" + email + "\" AND password = \"" + password + "\"";
  	      	  
//  	      	  System.out.println(query);
  	      	  
  	      	  ResultSet result;
  	      	  PreparedStatement statement = dbcon.prepareStatement(query);
  	      	  result = statement.executeQuery(query);
  	      	  int e = 0;
  	      	  while(result.next()){
  	      		  e = result.getInt(1);
  	      	  }
  	      	  
  	      	  
  	      	  if(e == 0){
  	      		 //response.sendRedirect("index.jsp?errorMessage=Invalid");
	  	      	RequestDispatcher rd = request.getRequestDispatcher("_dashboard.jsp");
	
	  	      	request.setAttribute("errormsg", "You are not an employee");
	
	  	      	rd.forward(request, response);  

  	      	  }
  	      	  else{
  	      		statement.close();
  	      		result.close();
    	      	response.sendRedirect("dashboard");
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
