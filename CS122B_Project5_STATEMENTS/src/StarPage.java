
/* A servlet to display the contents of the MySQL movieDB database */

import java.io.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class StarPage extends HttpServlet
{
   
	private static final long serialVersionUID = 1L;

	public String getServletInfo()
    {
       return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    // Use http GET

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
//        String loginUser = "mytestuser";
//        String loginPasswd = "mypassword";
//        String loginUrl = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>Fabflix: Found Results</TITLE>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"mystyle.css\">"
        		+ "<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\"></HEAD>");

        out.println("<BODY style=\"background-color: lightgrey;\">" + "<h2 style=\"display: inline-block;z-index: 1;padding:1em;\"><a href=\"mainPage\">Fabflix</a></h2>"
        		+ "<h2 id=\"cart\" style=\"float:right;display: inline-block;z-index: 1;padding:1em;\">"
        		+ "<a href=\"ShoppingCart\">My Cart</a></h2>");        
        out.println("<h3 style=\"text-align:center;\">Star Page</h3>");
        


        
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
              
              String id = request.getParameter("id");
              
              
              String query = "SELECT id, first_name, last_name, dob, photo_url from stars WHERE id = " + id;
           
	              // Perform the query
              
              PreparedStatement statement =  dbcon.prepareStatement(query);
              
              ResultSet rs = statement.executeQuery(query);

    
              out.println("<TABLE border align=\"center\">");

              // Iterate through each row of rs

    
	      out.println("<tr>" +
              "<td>" + "Image" +"</td>" +
			  "<td>" + "ID" + "</td>" +
			  "<td>" + "Name" + "</td>" +
			  "<td>" + "Date of Birth" + "</td>" +
			  "<td>" + "Movies" + "</td>" +
			  "</tr>");
	      
              while (rs.next())
              {
                  String s_ID = rs.getString("id");
                  String fn = rs.getString("first_name");
                  String ln = rs.getString("last_name");
                  String dob = rs.getString("dob");
                  String photo = rs.getString("photo_url");
                  String table = "<tr>" +
							  "<td><img src=" + photo + " style= height:200px;/></td>" +
							  "<td>" + s_ID + "</td>" +
                              "<td>" + fn + " " + ln + "</td>" +
                              "<td>" + dob + "</td><td>" ;
                  
                  String movieQuery = "Select M.id, M.title from movies M, stars_in_movies SM, stars S where M.id = SM.movie_id and S.id = SM.star_id ";
            
                  
                  movieQuery += "AND S.id = " + s_ID;
             

                  PreparedStatement starStatement = dbcon.prepareStatement(movieQuery);

                  ResultSet rsMovie = starStatement.executeQuery(movieQuery);
                  
                  while(rsMovie.next()){
                	  String title = rsMovie.getString("title");
                	  String movieid = rsMovie.getString("id");
                	  table += "<p><a href=MoviePage?id=" + movieid +">" + title + "</a></p>";
                  }
                  table += "</td></tr>";
                  out.println(table);
                  
                  starStatement.close();
                  rsMovie.close();
                  
              }
              out.println("</TABLE>");
              
      
              rs.close();
              statement.close();
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
