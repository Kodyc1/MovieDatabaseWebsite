import java.io.*;
import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

@WebServlet("/Login")
public class login extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	
	public String getServletInfo()
    {
       return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    // Use http GET
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
	{
		doGet(request,response);
	}
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        
        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        
		HttpSession session = request.getSession();
		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
//		System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
		boolean valid = VerifyUtils.verify(gRecaptchaResponse);

        LinkedHashMap<String,Integer>Cart = new LinkedHashMap<String, Integer>();
		session.setAttribute("Cart", Cart);

        try {

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
  	      	  String username = request.getParameter("uname");
  	      	  String password = request.getParameter("pword");
  	      	  String query = "SELECT COUNT(*) from customers WHERE email = \"" + username + "\" AND password = \"" + password + "\"";
  	      	  
  	      	  ResultSet result;
  	      	  PreparedStatement statement = dbcon.prepareStatement(query);
  	      	  result = statement.executeQuery(query);
  	      	  int e = 0;
  	      	  while(result.next()){
  	      		  e = result.getInt(1);
  	      	  }
  	      	  if(e == 0){
  	      		 //response.sendRedirect("index.jsp?errorMessage=Invalid");
	  	      	RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
	
	  	      	request.setAttribute("errormsg", "Wrong Username or Password");
	
	  	      	rd.forward(request, response);  

  	      	  }
  	      	  else{
  	    		if (!valid) {
  	  		    //errorString = "Captcha invalid!";
  	    	      	RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
  	    	      	request.setAttribute("errormsg", "Wrong Username or Password/Invalid ReCaptcha");
  	    	      	rd.forward(request, response);  
  	    		}
  	    		statement.close();
  	    		result.close();
  	    		response.sendRedirect("mainPage");	
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
