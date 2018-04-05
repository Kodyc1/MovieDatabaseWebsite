


import java.io.*;
import java.sql.*;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

import com.google.gson.Gson;

public class AjaxSearch extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException
	    {

	        response.setContentType("application/json");    // Response mime type
	        // Output stream to STDOUT
	        PrintWriter out = response.getWriter();
	        
            PreparedStatement ps = null;
            ResultSet rs = null;

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

	              String tokens = request.getParameter("term");
	              
	              String[] words = tokens.split("\\s");
	              
	              
	              ArrayList<String> list = new ArrayList<String>();
	              
	              
	              String data;
	              
	              try{
	            	  String statement = "SELECT * FROM movies where MATCH(title) Against (\'";
	              
	            	  for (int i = 0;  i < words.length; i++){
	            		  statement += " +" + words[i];
	            	  }
	            	  
	            	  statement += "*\' IN BOOLEAN MODE)";
	            	  
	            	  
//	            	  System.out.println(statement);
	            	  
	            	  ps = dbcon.prepareStatement(statement);

	            	  rs = ps.executeQuery();
	            	  while (rs.next()){
	            		  data = rs.getString("title");
	            		  list.add(data);
	            	  }
	              }
	              catch(Exception e){
	            	  System.out.println(e.getMessage());
	              }
	              
	              
	              String found = new Gson().toJson(list);
	              
//	              System.out.println(found);
	              
	              out.write(found);
	              
	              ps.close();
	 	          rs.close();	 
	                       
	           }
	        catch (SQLException ex) {
	              while (ex != null) {
	                    System.out.println ("SQL Exception:  " + ex.getMessage ());
	                    ex = ex.getNextException ();
	                }  // end while
	            }  // end catch SQLException
	        
	        catch(java.lang.Exception ex)
	            {
//	                out.println("<HTML>" +
//	                            "<HEAD><TITLE>" +
//	                            "MovieDB: Error" +
//	                            "</TITLE></HEAD>\n<BODY>" +
//	                            "<P>SQL error in doGet: " +
//	                            ex.getMessage() + "</P></BODY></HTML>");
	                return;
	            }
	        
	         out.close();
	             
	    }
}
