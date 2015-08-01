<%-- 
    Document   : home
    Created on : 20 May, 2015, 1:38:51 AM
    Author     : root
--%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="style/final_major.css">
        <title>home</title>
    </head>
    <body>
        <%
          //read uid from cookie ..

            //get cookies for this domain ..
            int exists = 0;
            String name = "";
            Cookie mycookie = null;
            Cookie[] cookies = request.getCookies();
            Cookie cookie = null;
            int i; //iterator ..
            if (cookies != null) {
                for (i = 0; i < cookies.length; i++) {
                    cookie = cookies[i];
                    name = cookie.getName();
                    if (name.equals("user")) {
                        mycookie = cookie;
                        exists = 1;
                        break;
                    }

                }
            }
            String uid = "";
            if (exists == 1) {
                uid = mycookie.getValue();
                //redirect to home page
                //request.getRequestDispatcher("login.jsp").forward(request, response);

            } else {
                //redirect to login page . With message please login again ..

                request.getRequestDispatcher("index.jsp").forward(request, response);
            }

        %>

        <div id="wrapper">
            <div id="header">
                This is a demo service provider
            </div>
            <div id="content">
                </br> </br> </br> </br> 

                Successfully Authenticated <br>
                User ID : <%=uid%> (Hash of User ID)
                
                <br>Click to <a href="logout"><b>LOGOUT</b></a>
                </br> </br> </br> </br>
                </br> </br> </br>
            </div>
            <div id="footer">
                <small>

                    <i>
                        Under the Guidance of : Dr. Naveen Chauhan , CSED </br>
                        Developed By : Bhaskar Kalia , Anurag Singh ,  Sushant Thakur , Nisha Kumari , Premlata Negi , CSE Final Year , NITH 
                    </i>

                </small> 
            </div>
        </div>
       
    </body>
</html>

