
/* A servlet to display the contents of the MySQL movieDB database */

import java.io.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class MoviePage extends HttpServlet
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
        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>Fabflix: Found Results</TITLE>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"mystyle.css\">"
        		+ "<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\"></HEAD>");

        out.println("<BODY style=\"background-color: lightgrey;\">" + "<h2 style=\"display: inline-block;z-index: 1;padding:1em;\"><a href=\"mainPage\">Fabflix</a></h2>"
        		+ "<h2 id=\"cart\" style=\"float:right;display: inline-block;z-index: 1;padding:1em;\">"
        		+ "<a href=\"ShoppingCart\">My Cart</a></h2>");
        out.println("<h3 style=\"text-align:center;\">Movie Page</h3>");
        
//        PreparedStatement statement = null;
//        PreparedStatement starStatement = null;
//        PreparedStatement genreStatement = null;
//        
        
        
        ResultSet rsStar = null;
        ResultSet gStar = null;
        
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
	            
              // Declare our statement
              
              String id = request.getParameter("id");
              String title = request.getParameter("title");
              
              Statement statement = dbcon.createStatement();
              Statement starStatement = dbcon.createStatement();
              Statement genreStatement = dbcon.createStatement();
              
              String query = "SELECT DISTINCT id, year, director, banner_url, title, trailer_url from movies WHERE id = " + id;
              String q2 = "SELECT DISTINCT id, year, director, banner_url, title, trailer_url from movies WHERE title = \"" + title + "\"";
              

	              // Perform the query
              if (id != null){
//            	  statement = dbcon.prepareStatement(query);
                  rs = statement.executeQuery(query);
              }
              if (title != null){
//            	  statement = dbcon.prepareStatement(q2);
            	  rs = statement.executeQuery(q2);
              }
    
              out.println("<TABLE border align=\"center\">");

              // Iterate through each row of rs

    
	      out.println("<tr>" +
              "<td>" + "Image" +"</td>" +
			  "<td>" + "ID" + "</td>" +
			  "<td>" + "Title" + "</td>" +
			  "<td>" + "Year" + "</td>" +
			  "<td>" + "Director" + "</td>" +
			  "<td>" + "Stars" + "</td>" +
			  "<td>" + "Genres" + "</td>" + 
			  "<td>" + "Trailer" + "</td>" + 
			  "</tr>");
	     
	      
              while (rs.next())
              {
            	  String m_IG = rs.getString("banner_url");
            	  String m_ID = rs.getString("id");
                  String m_TI = rs.getString("title");
                  String m_YR = rs.getString("year");
                  String m_DR = rs.getString("director");
                  String m_TR = rs.getString("trailer_url");
                  System.out.println(m_ID);
                  String table = "<tr>" +
							  "<td><img src=" + m_IG + " style= height:200px;/></td>" +
							  "<td>" + m_ID + "</td>" +
                              "<td>" + m_TI + "</td>" +
                              "<td>" + m_YR + "</td>" +
                              "<td>" + m_DR + "</td><td>" ;
                  
                  String starQuery = "Select S.first_name, S.last_name, S.id from movies M, stars_in_movies SM, stars S where M.id = SM.movie_id and S.id = SM.star_id ";
        	      String genreQuery = "Select G.name, G.id from genres G, genres_in_movies GM, movies M WHERE M.id = GM.movie_id and G.id = GM.genre_id ";
            
                  
                  starQuery += "AND M.id = " + m_ID;
             
                  genreQuery += "AND M.id = " + m_ID;
//
//                  starStatement = dbcon.prepareStatement(starQuery);
//                  genreStatement = dbcon.prepareStatement(genreQuery);
                  rsStar = starStatement.executeQuery(starQuery);
                  gStar = genreStatement.executeQuery(genreQuery);
                  
                  while(rsStar.next()){
                	  String starFName = rsStar.getString("first_name");
                	  String starLName = rsStar.getString("last_name");
                	  String starid = rsStar.getString("id");
                	  table += "<p><a href=StarPage?id=" + starid +">" + starFName + " " + starLName + "</a></p>";
                  }
                  table += "</td><td>";
                  while(gStar.next()){
                	  String genre = gStar.getString("name");
                	  String gid = gStar.getString("G.id");
                	  table += "<p><a href=Browse?genre=" + gid  + "&page=1>" + genre + "</a></p>";
                  }
                  table += "</td><td><a href=" + m_TR + ">" + m_TR + "</a>";  
                  table += "</td></tr>";
                  out.println(table);

                  out.println("</TABLE>");
                  
                  
                  out.println("<a href=\"ShoppingCart?id=" + m_ID + "&quantity=1\">" + "Add to Cart</a>");
              }
      
              rs.close();
              statement.close();

              starStatement.close();
              genreStatement.close();
              rsStar.close();
              gStar.close();
            
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
    
    /* public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
	doGet(request, response);
	} */
}
