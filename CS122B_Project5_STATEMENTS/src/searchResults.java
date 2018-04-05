
/* A servlet to display the contents of the MySQL movieDB database */

import java.io.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

import java.lang.Math;

public class searchResults extends HttpServlet
{

	private static final long serialVersionUID = 1L;


    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
    	///////////////////////////////////////////
    	long startTimeS = System.nanoTime();
    	long TJ = 0;
    	///////////////////////////////////////////

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>Fabflix: Found Results</TITLE>");
        out.println(//"<link rel=\"stylesheet\" type=\"text/css\" href=\"mystyle.css\">"+ "<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">"+ 
        		"<link rel=\"stylesheet\" type=\"text/css\" href=\"mystyle.css\">"
        		+ "<script src='https://code.jquery.com/jquery-3.1.0.min.js'></script>"
        		+ "<script src=\"http://qtip2.com/v/stable/jquery.qtip.js\"></script>"
        		+ "<script src=\"https://unpkg.com/imagesloaded@4/imagesloaded.pkgd.min.js\"></script>"
        		+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://qtip2.com/v/stable/jquery.qtip.css\"></HEAD>");

        out.println("<BODY style=\"background-color: lightgrey;\">" + "<h2 style=\"display: inline-block;z-index: 1;padding:1em;\"><a href=\"mainPage\">Fabflix</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"ShoppingCart\">My Cart</a></h2>");
        
//        out.println("<style>.link{padding:1em; margin:1em; } .link:hover:after{  height: 100px; width: 200px; position:absolute; background: rgba(0,0,0,.7);  border-radius: 5px;  color: #fff;  padding: 2em;  content: attr(data-title);    z-index: 99;}</style>");

        
        try
           {
        	
        	long startTimeJ = System.nanoTime();


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
            Statement statement = dbcon.createStatement();
            Statement statement2 = dbcon.createStatement();
            Statement starStatement = dbcon.createStatement();
            Statement genreStatement = dbcon.createStatement();
//              PreparedStatement statement = null;
//              PreparedStatement statement2 = null;
//              PreparedStatement starStatement = null;
//              PreparedStatement genreStatement = null;
              
              String limit = request.getParameter("limit");
              String title = request.getParameter("title");
    	      String year = request.getParameter("year");
    	      String director = request.getParameter("director");
    	      String fname = request.getParameter("fname");
    	      String lname = request.getParameter("lname");
              String page = request.getParameter("page");
              String order = request.getParameter("order");
              
              String query = "SELECT DISTINCT M.id, M.year, M.director, M.banner_url, M.title, M.trailer_url from movies M, stars_in_movies SM, stars S where M.id = SM.movie_id and S.id = SM.star_id ";
              String count = "SELECT COUNT(DISTINCT M.id)  from movies M, stars_in_movies SM, stars S where M.id = SM.movie_id and S.id = SM.star_id ";
              
              
           // Iterate through each row of rs
              
              out.println("<form action=\"searchResults\">");
              out.println("<select name=\"limit\">");
              out.println("<option value = 10> Show 10 </option>");
              out.println("<option value = 25> Show 25 </option>");
              out.println("<option value = 50> Show 50 </option>");
              out.println("<option value = 100> Show 100 </option>");
              out.println("</select><br>");

                 
                   
              if(title != null){
    	    	  query += "and M.title like \"%" + title + "%\" ";
    	    	  count += "and M.title like \"%" + title + "%\" ";
                  out.println("<input type = \"hidden\" name = \"title\" value = \"" + title + "\">");
    	      } 
    	      if(year != null){
    	    	  query += "and M.year like \"%" + year + "%\" ";
    	    	  count += "and M.year like \"%" + year + "%\" ";
                  out.println("<input type = \"hidden\" name = \"year\" value = \"" + year + "\">");
    	      }
    	      if(director != null){
    	    	  query += "and M.director like \"%" + director + "%\" ";
    	    	  count += "and M.director like \"%" + director + "%\" ";
                  out.println("<input type = \"hidden\" name = \"director\" value = \"" + director + "\">");
    	      } 
    	      if(fname != null){
    	    	  query += "and S.first_name like \"%" + fname + "%\" ";
    	    	  count += "and S.first_name like \"%" + fname + "%\" ";
                  out.println("<input type = \"hidden\" name = \"fname\" value = \"" + fname + "\">");

    	      } 
    	      if(lname != null){
    	    	  query += "and S.last_name like \"%" + lname + "%\" ";
    	    	  count += "and S.last_name like \"%" + lname + "%\" ";
                  out.println("<input type = \"hidden\" name = \"lname\" value = \"" + lname + "\">");

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
    	    	  //System.out.println(limit);
    	    	  Integer offset = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);
    	    	  query += " LIMIT " + Integer.parseInt(limit) + " OFFSET " + offset;
    	      }

    	        
              out.println("<input type=\"hidden\" name=\"page\" value=\"" + 1 + "\" />");

    	      out.println("<input type = \"submit\"/>");
    	      
              out.println("</form>");	
              

              // Perform the query
              //System.out.println(query);

              //System.out.println(count);
                            
//              statement = dbcon.prepareStatement(query);
//              statement2 = dbcon.prepareStatement(count);

              ResultSet rs = statement.executeQuery(query);
              ResultSet rowcount = statement2.executeQuery(count);
              
              ResultSet gStar = null;
              ResultSet rsStar = null;
    
              String uri = request.getScheme() + "://" +

	             request.getServerName() + 
	
	             ("http".equals(request.getScheme()) && request.getServerPort() == 80 || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "" : ":" + request.getServerPort() ) +
	
	             request.getRequestURI() +
	
	            (request.getQueryString() != null ? "?" + request.getQueryString() : "");
	
              //System.out.println(uri);

              // Iterate through each row of rs
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
                  String m_TR = rs.getString("M.trailer_url");
                  String table = "<tr>" +
							  "<td><img src=" + m_IG + " style= height:200px;/></td>" +
							  "<td rowspan=\"2\">" + m_ID + "</td>" +
                              "<td rowspan=\"2\"><a data-director=\"" + m_DR + "\" data-year=\"" + m_YR + "\" data-id =\"" + m_ID + "\" data-trailer = \"" + m_TR + "\" data-image =\"" + m_IG + "\" data-title=\"" + m_TI + "\" class=\"url\" id=" + m_ID + " href=MoviePage?id=" + m_ID + ">" + m_TI + "</a></td>" +
                              "<td rowspan=\"2\">" + m_YR + "</td>" +
                              "<td rowspan=\"2\">" + m_DR + "</td><td id=\"" + m_ID + "s\" class=rowspan=\"2\">" ;
                  
                  String starQuery = "Select S.first_name, S.last_name, S.id from movies M, stars_in_movies SM, stars S where M.id = SM.movie_id and S.id = SM.star_id ";
        	      String genreQuery = "Select G.name from genres G, genres_in_movies GM, movies M WHERE M.id = GM.movie_id and G.id = GM.genre_id ";
            
                  
                  starQuery += "AND M.id = " + m_ID;
             
                  genreQuery += "AND M.id = " + m_ID;
                  
//                  starStatement = dbcon.prepareStatement(starQuery);
//                  genreStatement = dbcon.prepareStatement(genreQuery);

                  rsStar = starStatement.executeQuery(starQuery);
                  gStar = genreStatement.executeQuery(genreQuery);
                  
                  //////////////////////////////////////////////////////
                  long endTimeJ = System.nanoTime();
                  TJ = endTimeJ - startTimeJ;
                  //////////////////////////////////////////////////////
                  
                  while(rsStar.next()){
                	  String starFName = rsStar.getString("first_name");
                	  String starLName = rsStar.getString("last_name");
                	  String starid = rsStar.getString("id");
                	  table += "<p><a href=StarPage?id=" + starid +">" + starFName + " " + starLName + "</a></p>";
                  }
                  table += "</td><td id=\"" + m_ID + "g\" rowspan=\"2\">";
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
              
              
              
              if(limit != null){
            	  if(rowCount > 0){
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
              dbcon.close();
              
              
              out.println("<table class=\"tooltip\" style=\"display:none;\"><tr>"
              		+ "<td id=\"title\"></td>"
              		+ "<td id=\"pic\"></td>"
              		+ "<td id=\"director\"></td>"
              		+ "<td id=\"year\"></td>"
              		+ "</tr><tr>"
              		+ "<td id=\"trailer\"></td>"
              		+ "<td id=\"stars\"></td>"
              		+ "<td id=\"genres\"></td>"
              		+ "</tr><tr>"
              		+ "<td id=\"cart\"></td></tr></table>");
                            
              out.println("<script>"
              		+ "$(document).ready(function(){"
              		+ "$('.url').hover(function(){"
              		+ "var sid = '#' + $(this).data(\"id\") + 's';"
              		+ "var gid = '#' + $(this).data(\"id\") + 'g';"
              		+ "var title =  $(this).data(\"title\");"
              		+ "var year =  $(this).data(\"year\");"
              		+ "var director =  $(this).data(\"director\");"
              		+ "var image = \"<img style='height:200px;width:150px;' src='\" + $(this).data(\"image\") + \"'/>\";"
              		+ "var trailer = '<a href=\"' + $(this).data(\"trailer\") + '\">Trailer</a>';"
              		+ "var cart = '<a href=\"ShoppingCart?id=' + $(this).data(\"id\") + '&quantity=1\">Add to Cart</a>';"
//              		+ "console.log(title);"
//              		+ "console.log(year);"
//              		+ "console.log(director);"
//              		+ "console.log(image);"
//              		+ "console.log(trailer);"
//              		+ "console.log(cart);"
//              		+ "console.log($(sid).html());"
//              		+ "console.log($(gid).html());"
              		+ "$('#title').html(title);"
              		+ "$('#year').html(year);"
              		+ "$('#director').html(director);"
              		+ "$('#trailer').html(trailer);"
              		+ "$('#pic').html(image);"
              		+ "$('#stars').html($(sid).html());"
              		+ "$('#genres').html($(gid).html());"
              		+ "$('#cart').html(cart);"
              		+ "});"
              		+ "$('.url').each(function(){"
              		+ "console.log($(this));"
              		+ "$(this).qtip({"
              		+ "content: $('.tooltip'),"
              		+ "            hide: {"
              		+ "                fixed: true,"
              		+ "                delay: 300            },"
              		+ "style:{"
              		+ "width:3000}"
              		+ ""
              		+ "});"
              		+ "});"
              		+ "});"
              		+ "</script>");
              
              statement.close();
              statement2.close();
              starStatement.close();
              genreStatement.close();
              rs.close();
              rowcount.close();
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
         
         ///////////////////////////////////////////////////
         long endTimeS = System.nanoTime();
         long TS = endTimeS - startTimeS;
         ///////////////////////////////////////////////////
         
         BufferedWriter bw = null;
 		 FileWriter fw = null;
 		 
 		 BufferedWriter bw2 = null;
		 FileWriter fw2 = null;
         
         File file = new File("/home/ubuntu/logs/TS_log.txt");
         File file2 = new File("/home/ubuntu/logs/TJ_log.txt");
         
         if (!file.exists()) {
        	 file.createNewFile(); 
		 }
         if (!file2.exists()) {
        	 file2.createNewFile();
		 }
         
         fw = new FileWriter(file.getAbsoluteFile(), true);
		 bw = new BufferedWriter(fw);
		 
		 fw2 = new FileWriter(file2.getAbsoluteFile(), true);
		 bw2 = new BufferedWriter(fw2);
		 
		 bw.write(String.valueOf(TS));
		 bw.newLine();
		 bw2.write(String.valueOf(TJ));
		 bw2.newLine();
         
		 bw.flush();
		 bw2.flush();
		 
		 bw.close();
		 fw.close();
		
		 bw2.close();
		 fw2.close();
		
//		 System.out.println("TS: " + TS);
//         System.out.println("TJ: " + TJ);
    }
    
    /* public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
	doGet(request, response);
	} */
}
