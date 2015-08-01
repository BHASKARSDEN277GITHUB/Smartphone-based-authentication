<%-- 
    Document   : message
    Created on : 18 May, 2015, 12:07:08 AM
    Author     : root
--%>

<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.naming.Context"%>
<%@page import="java.security.MessageDigest"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>Message</title>
    </head>
    <body>
        <%!
    
        public String execute(String arg)throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
 	
 	byte[] bytestr=arg.getBytes();
	
	md.update(bytestr);
        byte[] mdbytes = md.digest();
 	
	//convert byte array to hex ..
	
	return	(Hex.encodeHexString(mdbytes)).toUpperCase();
    }
    %>
        <%
            //set inheader for refreshing page
            response.setIntHeader("refresh", 5);    // setting response header refresh after every 5 seconds ..

            String uid = (String) request.getParameter("uid");

            //check if user is registered or not 
            //calculate hash of uid
            String hUID = execute(uid).substring(0, 8);

            //Get connection to data source ..
            Context initialContext = new InitialContext();
            Context environmentContext = (Context) initialContext.lookup("java:comp/env");

            // Look up our data source
            DataSource ds = (DataSource) environmentContext.lookup("jdbc/major");

            Connection conn = ds.getConnection();

            /**
             * read email id from the database table RUT ..
             *
             */
            PreparedStatement stat = conn.prepareStatement("select * from UT where uid=?");
            stat.setString(1, hUID);

            ResultSet res = stat.executeQuery();
            String email = "";
            if (res.next())//this means user is registered
            {
                email = res.getString("email");
                //now create an entry to RT table if not exists , if exists check  status
                PreparedStatement s1 = conn.prepareStatement("select * from RT where uid=?");
                s1.setString(1, hUID);
                ResultSet rs1 = s1.executeQuery();

                if (rs1.next()) {//entry exists
                    int status = rs1.getInt("status");
                    if (status == 1) {//check time differnece if time difference is less than 5 minutes create cookie go okay 
                        //else make update entry of status to 0
                        String time = rs1.getString("time");

                        //find time difference okay ( for staus===1 and time<=2)
                        String arrayDate[] = time.split(":");
                        long day1 = Long.parseLong(arrayDate[0]);
                        long hour1 = Long.parseLong(arrayDate[1]);
                        long min1 = Long.parseLong(arrayDate[2]);
                        long sec1 = Long.parseLong(arrayDate[3]);

                        //get current timeData
                        Date date = new Date();
                        long day = date.getDate();
                        long hour = date.getHours();
                        long min = date.getMinutes();
                        long sec = date.getSeconds();

                        if (((day - day1) == 0) && ((hour - hour1) == 0) && ((min - min1) <= 1)) {//
                            //update with current time and its okay //redirect him to home page
                            
                            /**
                             * update time value to current time ..
                             */
                            
                            String dateTime = day + ":" + hour + ":" + min + ":" + sec;
                            PreparedStatement s9 = conn.prepareStatement("update RT set time=? where uid=?");
                            s9.setString(1, dateTime);
                            s9.setString(2, hUID);
                            s9.executeUpdate();

                            //add cookie redirect to home page
                            /**
                             * add cookie ..
                             */
                            Cookie c = new Cookie("user", hUID); //uid ..
                            c.setMaxAge(60 * 1);
                            response.addCookie(c);

                            /**
                             * redirect to server ..
                             */
                            response.sendRedirect("home.jsp");
                            
                        } else {//for previous entries 

                            String dateTime = day + ":" + hour + ":" + min + ":" + sec;
                            //update with current time and status 0
                            PreparedStatement s9 = conn.prepareStatement("update RT set time=? where uid=?");
                            s9.setString(1, dateTime);
                            s9.setString(2, hUID);
                            s9.executeUpdate();

                            PreparedStatement s10 = conn.prepareStatement("update RT set status=0 where uid=?");
                            s10.setString(1, hUID);
                            s10.executeUpdate();
                            out.write("Start and Use your auth App to get authenticated");

                        }

                    } else {
                        //wait my dear magic will happen
                        Date date = new Date();
                        long day = date.getDate();
                        long hour = date.getHours();
                        long min = date.getMinutes();
                        long sec = date.getSeconds();
                        String dateTime = day + ":" + hour + ":" + min + ":" + sec;
                            //update with current time and status 0
                            PreparedStatement s100 = conn.prepareStatement("update RT set time=? where uid=?");
                            s100.setString(1, dateTime);
                            s100.setString(2, hUID);
                            s100.executeUpdate();
                            out.write("Start and Use your auth App to get authenticated");
                    }

                } else {//make an entry to databse with status 0 and current time
                    //get current timeData
                    Date date = new Date();
                    long day = date.getDate();
                    long hour = date.getHours();
                    long min = date.getMinutes();
                    long sec = date.getSeconds();

                    String dateTime = day + ":" + hour + ":" + min + ":" + sec;

                    PreparedStatement s3 = conn.prepareStatement("insert into RT values(?,0,?)");
                    s3.setString(1, hUID);
                    s3.setString(2, dateTime);

                    s3.executeUpdate();
                    
                    out.write("Start and Use your auth App to get authenticated");

                }

            } else {
                out.write("You are not registered to the auth Server \nPlease download the auth App and register to the auth Server \nThank you");
            }


        %>
    </body>
</html>
