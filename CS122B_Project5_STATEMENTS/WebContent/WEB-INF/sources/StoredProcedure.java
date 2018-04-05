
/* A servlet to display the contents of the MySQL movieDB database */

import java.io.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class StoredProcedure extends HttpServlet
{
	private static final long serialVersionUID = 1L;


    // Use http GET

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>Results</TITLE>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"mystyle.css\">"
        		+ "<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\"></HEAD>");

        out.println("<BODY style=\"background-color: lightgrey;\">" + "<h2 style=\"display: inline-block;z-index: 1;padding:1em;\"><a href=\"dashboard\">Dashboard</a></h2>");

     
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
            
		      
		      // check which command was issued in the dashboard
              String meta = request.getParameter("meta");
              String moviestar = request.getParameter("star");
              String movie = request.getParameter("movie");
                          
              
              // get star arguments
//              String m = request.getParameter("m_id");
//              Integer m_id = null;
              
              String title = request.getParameter("title");
              String y = request.getParameter("year");
              Integer year = null;
              String director = request.getParameter("director");
              
//              String s = request.getParameter("s_id");
//              Integer s_id = null;
              String first_name = request.getParameter("fname");
              String last_name = request.getParameter("lname");

//              String g = request.getParameter("g_id");
//              Integer g_id = null;
              String gname = request.getParameter("gname");
              
//              if (m != null && y != null && s != null && g != null){
//                  m_id = Integer.parseInt(m);
//                  year = Integer.parseInt(y);
//                  s_id = Integer.parseInt(s);
//                  g_id = Integer.parseInt(g);
//              }
              
              if (y!=null){
            	  year = Integer.parseInt(y);
              }
              
              // insert star
              if (moviestar != null){
                  PreparedStatement statement = null;
                  
                  String insert = "";
    	      	  
            	  if (last_name.equals("")){
        	      	  insert += "INSERT INTO stars(first_name, last_name) VALUES(\"\",\"" + first_name + "\")";
        	      	  out.println("<h3 style=\"text-align:center;\">Inserted " + first_name + " into database!</h3>");
            	  }
            	  else{
        	      	  insert += "INSERT INTO stars(first_name, last_name) VALUES(\"" + first_name + "\",\"" + last_name + "\")";
        	      	  out.println("<h3 style=\"text-align:center;\">Inserted " + first_name + " " + last_name + " into database!</h3>");
            	  }
            	  
            	  statement = dbcon.prepareStatement(insert);
              	  
            	  @SuppressWarnings("unused")
				int star = statement.executeUpdate(insert);
            	  
            	  statement.close();
              }
              
              // display metadata
              if (meta != null){
                  // Metadata statement
                  DatabaseMetaData databaseMetaData = dbcon.getMetaData();
    		      ResultSet result = databaseMetaData.getTables(null, null, null, null);
    		      
    		      out.println("<h2 style=\"text-align: center;\">Metadata of Moviedb</h2>");
    	          out.println("<TABLE border align=\"center\">");
    	          
				  while(result.next()){
					 // table names
					 String table = result.getString(3);
					 out.println("<tr><td style=\"padding:1em;\">TABLE NAME: " + table + "</td></tr>");
						
					 ResultSet columns = databaseMetaData.getColumns(null, null, table, null);
					
					 out.println("<tr><td style=\"text-align: left; padding: 1em;\">");
					 while(columns.next()){
					 	out.println("COLUMN NAME: " + String.format("%-15s", columns.getString("COLUMN_NAME")) + columns.getString("TYPE_NAME"));
					 	out.println("</br>");
					 }
					 out.println("</td></tr>");
				  }
				  out.println("</table>");
            	  
            	  
              }
              
              // add movie through stored proecdure
              if (movie != null){
            	  // callable statement
            	  String call = "{call add_movie(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            	  CallableStatement state = dbcon.prepareCall(call);
            	  state.setInt(1, 0);
            	  state.setString(2, title);
            	  state.setInt(3, year);
            	  state.setString(4, director);
            	  
            	  state.setInt(5, 0);
            	  state.setString(6, first_name);
            	  state.setString(7, last_name);
            	  
            	  state.setInt(8, 0);
            	  state.setString(9, gname);
            	  
            	  state.registerOutParameter(10, java.sql.Types.VARCHAR);
            	  state.registerOutParameter(11, java.sql.Types.VARCHAR);
            	  state.registerOutParameter(12, java.sql.Types.VARCHAR);
            	  state.registerOutParameter(13, java.sql.Types.VARCHAR);
            	  
            	  state.executeUpdate();
            	  
            	  String error = state.getString(10);
            	  String movie_status = state.getString(11);
            	  String star_status = state.getString(12);
            	  String genre_status = state.getString(13);
            	  
            	  if (error != null){
            		  out.println("<h3 style=\'text-align:center;\'>Error: " + error + "</h3>");
            	  }
            	  else {
            		  
	            	  out.println("<h3 style=\"text-align:center;\">Movie status: " + movie_status + "</h3>");
	            	  out.println("<h3 style=\"text-align:center;\">Star status: " + star_status + "</h3>");
	            	  out.println("<h3 style=\"text-align:center;\">Genre status: " + genre_status + "</h3>");
            	  }
              }
                     
              dbcon.close();
            }
        catch (SQLException ex) {
              while (ex != null) {
                    System.out.println ("SQL Exception:  " + ex.getMessage ());
                    ex = ex.getNextException ();
                }  // end while
            }  // end catch SQLException
        
        catch(java.lang.Exception ex)
            {
//                out.println("<HTML>" +
//                            "<HEAD><TITLE>" +
//                            "MovieDB: Error" +
//                            "</TITLE></HEAD>\n<BODY>" +
//                            "<P>SQL error in doGet: " +
//                            ex.getMessage() + "</P></BODY></HTML>");
                return;
            }
         out.close();
    }
    
}
