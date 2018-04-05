

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class Browse
 */
@WebServlet("/Browse")
public class Browse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
//		String loginUser = "mytestuser";
//        String loginPasswd = "mypassword";
//        String loginUrl = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>Fabflix: Browse</TITLE>"
        		+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"/mystyle.css\">"
        		+ "<link href=\"/css/bootstrap.min.css\" rel=\"stylesheet\"></HEAD>");
        out.println("<BODY style=\"background-color: lightgrey;\">" + "<h2 style=\"display: inline-block;z-index: 1;padding:1em;\"><a href=\"mainPage\">Fabflix</a></h2>"
        		+ "<h2 id=\"cart\" style=\"float:right;display: inline-block;z-index: 1;padding:1em;\">"
        		+ "<a href=\"ShoppingCart\">My Cart</a></h2>");

     
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
              PreparedStatement statement = null;
              PreparedStatement statement2 = null;
              PreparedStatement starStatement = null;
              PreparedStatement genreStatement = null;
              
              
              String limit = request.getParameter("limit");
              String genres = request.getParameter("genre");
              String name = request.getParameter("name");
              String page = request.getParameter("page");
              String order = request.getParameter("order");
              
              String query = "SELECT DISTINCT M.id, M.year, M.director, M.banner_url, M.title from movies M, genres_in_movies GM, genres G ";
              String count = "SELECT COUNT(DISTINCT M.id) from movies M, genres_in_movies GM, genres G ";

              
              out.println("<form action=\"Browse\">");
              out.println("<select name=\"limit\">");
              out.println("<option value = 10> Show 10 </option>");
              out.println("<option value = 25> Show 25 </option>");
              out.println("<option value = 50> Show 50 </option>");
              out.println("<option value = 100> Show 100 </option>");
              out.println("</select><br>");
              
              
              if (genres != null){
            	  String g = " WHERE M.id = GM.movie_id AND G.id = GM.genre_id AND G.id = " + genres;
            	  query += g;
            	  count += g;
            	  out.println("<input type = \"hidden\" name = \"genre\" value = \"" + genres + "\">");
              }
              if (name != null){
            	  String n = " WHERE M.title LIKE \"" + name + "%\"";
            	  query += n;
            	  count += n;
            	  out.println("<input type = \"hidden\" name = \"name\" value = \"" + name + "\">");
              }
              
              if (order != null)
    	      {
    	    	  if (order.equals("TA"))
    	    	  {
    	    		  query+= " ORDER BY title asc";
                	  out.println("<input type = \"hidden\" name = \"order\" value = \"" + order + "\">");
    	    	  }
    	          else if (order.equals("TD"))
    	          {
    	        	  query+= " ORDER BY title desc";
                	  out.println("<input type = \"hidden\" name = \"order\" value = \"" + order + "\">");
    	          }
    	          else if (order.equals("YA"))
    	          {
    	        	  query += " ORDER BY year asc";
                	  out.println("<input type = \"hidden\" name = \"order\" value = \"" + order + "\">");
    	          }
    	          else if(order.equals("YD"))
    	          {
    	        	  query += " ORDER BY year desc";
                	  out.println("<input type = \"hidden\" name = \"order\" value = \"" + order + "\">");
    	          }
    	      }
              
    	      if(limit != null)
    	      {
    	    	  Integer offset = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);
    	    	  query += " LIMIT " + limit + " OFFSET " + offset.toString();
    	    	  //System.out.println(query);
    	      }
              
              
              out.println("<input type=\"hidden\" name=\"page\" value=\"" + 1 + "\" />");

    	      out.println("<input type = \"submit\"/>");
              
              out.println("</form>");	
              
              statement = dbcon.prepareStatement(query);
              statement2 = dbcon.prepareStatement(count);
              
              ResultSet rs = statement.executeQuery(query);
              ResultSet rowcount = statement2.executeQuery(count);
              
              
              String uri = request.getScheme() + "://" +

	             request.getServerName() + 
	
	             ("http".equals(request.getScheme()) && request.getServerPort() == 80 || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "" : ":" + request.getServerPort() ) +
	
	             request.getRequestURI() +
	
	            (request.getQueryString() != null ? "?" + request.getQueryString() : "");
	
              // System.out.println(uri);

            String link = "";
            String link2 = "";
            if(order == null){
          	  	link += "<a href = \"" + uri.substring(0, uri.indexOf("&page=")) + "&order=TA" + "&page=1\">Title</a>";
          	  	link2 += "<a href = \"" + uri.substring(0, uri.indexOf("&page=")) + "&order=YA" + "&page=1\">Year</a>";
            }
            else if(order.equals("TA")){
            	  link += "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=TD" + "&page=1\">Title</a>";
            	  	link2 += "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=YA" + "&page=1\">Year</a>";
            }
            else if(order.equals("TD")){
            	  link += "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=TA" + "&page=1\">Title</a>";
            	  	link2 += "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=YA" + "&page=1\">Year</a>";
            } 

            else if(order.equals("YA")){
          	  	link += "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=TA" + "&page=1\">Title</a>";
            	  link2 += "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=YD" + "&page=1\">Year</a>";
            } else if(order.equals("YD")){	 
          	  	link += "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=TA" + "&page=1\">Title</a>";
            	  link2 += "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=YA" + "&page=1\">Year</a>";
            }
            

            out.println("<TABLE border align=\"center\">");
    
            out.println("<tr>" +
              "<td>" + "Image" +"</td>" +
			  "<td>" + "ID" + "</td>" +
			  "<td>" + link + "</td>" +			  
			  "<td>" + link2 + "</td>" +
			  "<td>" + "Director" + "</td>" +
			  "<td>" + "Stars" + "</td>" +
			  "<td>" + "Genres" + "</td>" + 
			  "</tr>");
              while (rs.next())
              {
            	  String m_IG = rs.getString("M.banner_url");
                  String m_ID = rs.getString("M.id");
                  String m_TI = rs.getString("M.title");
                  String m_YR = rs.getString("M.year");
                  String m_DR = rs.getString("M.director");
                  String table = "<tr>" +
							  "<td><img src=" + m_IG + " style= height:200px;/></td>" +
							  "<td rowspan=\"2\">" + m_ID + "</td>" +
                              "<td rowspan=\"2\"><a href=MoviePage?id=" + m_ID + ">" + m_TI + "</a></td>" +
                              "<td rowspan=\"2\">" + m_YR + "</td>" +
                              "<td rowspan=\"2\">" + m_DR + "</td><td rowspan=\"2\">" ;
                  
                  String starQuery = "Select S.first_name, S.last_name, S.id from movies M, stars_in_movies SM, stars S where M.id = SM.movie_id and S.id = SM.star_id ";
        	      String genreQuery = "Select G.name from genres G, genres_in_movies GM, movies M WHERE M.id = GM.movie_id and G.id = GM.genre_id ";
            
                  starQuery += "AND M.id = " + m_ID;
             
                  genreQuery += "AND M.id = " + m_ID;
                  
                  starStatement = dbcon.prepareStatement(starQuery);
                  genreStatement = dbcon.prepareStatement(genreQuery);

                  ResultSet rsStar = starStatement.executeQuery(starQuery);
                  ResultSet gStar = genreStatement.executeQuery(genreQuery);
                  
                  while(rsStar.next()){
                	  String starFName = rsStar.getString("first_name");
                	  String starLName = rsStar.getString("last_name");
                	  String starid = rsStar.getString("id");
                	  table += "<p><a href=StarPage?id=" + starid +">" + starFName + " " + starLName + "</a></p>";
                  }
                  table += "</td><td rowspan=\"2\">";
                  while(gStar.next()){
                	  String genre = gStar.getString("name");
                	  table += "<p>" + genre  + "</p>";
                  }
                  table += "</td></tr>";
                  table += "<tr><td><a href=\"ShoppingCart?id=" + m_ID + "&quantity=1\">" + "Add to Cart</a></td></tr>";
                  out.println(table);
              }
              out.println("</TABLE>");
              
              
             Integer rowCount = null;
             while(rowcount.next()){
            	 rowCount  =  rowcount.getInt(1);
             }
              
              
              //System.out.println(rowCount);
              
	              
              
              if(limit != null){
            	  if (rowCount  > 0){
	            	  double l = Math.ceil(rowCount/Double.parseDouble(limit));
	            	  int z = (int) l;
	            	  String f = "" + z;
	            	  Integer p = Integer.parseInt(page);
	            	  if(page.equals("1") && z > 1){
	            		  out.println("<a href = " + uri.substring(0, uri.indexOf("&page=")) + "&page=" + (p+1) + "><button style=\"margin:auto; display:block; padding:2em; text-align:center;\">Next</button></a>");
	            	  }
	            	  else if(page.equals(f) && z != 1){
	            		  out.println("<a href = " + uri.substring(0, uri.indexOf("&page=")) + "&page=" + (p-1) + "><button style=\"margin:auto; display:block; padding:2em; text-align:center;\">Prev</button></a>");
	            	  } else if(z != 1){
	            		  out.println("<a href = " + uri.substring(0, uri.indexOf("&page=")) + "&page=" + (p-1) + "><button style=\"margin:auto; display:block; padding:2em; text-align:center;\">Prev</button></a>");
	            		  out.println("<a href = " + uri.substring(0, uri.indexOf("&page=")) + "&page=" + (p+1) + "><button style=\"margin:auto; display:block; padding:2em; text-align:center;\">Next</button></a>");
	            	  }
            	  }
              }
              
              rs.close();
              statement.close();         
              statement2.close();
              starStatement.close();
              genreStatement.close();
        } 

        catch (SQLException ex) {
            while (ex != null) {
                  System.out.println ("SQL Exception:  " + ex.getMessage ());
                  ex = ex.getNextException ();
              }  // end while
        }  // end catch SQLException
      
        catch(java.lang.Exception ex)
          {
//              out.println("<HTML>" +
//                          "<HEAD><TITLE>" +
//                          "MovieDB: Error" +
//                          "</TITLE></HEAD>\n<BODY>" +
//                          "<P>SQL error in doGet: " +
//                          ex.getMessage() + "</P></BODY></HTML>");
              return;
        }
        
       out.close();		
	}

	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
