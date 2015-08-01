<html>
<head>
	<title>authServer</title>
	<link rel="stylesheet" type="text/css" href="style/final_major.css">
	
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
                out.write("cookie does not exist");
                //redirect to home page
                request.getRequestDispatcher("home.jsp").forward(request, response);
                
            } else {
                //redirect to login page . With message please login again ..

                
                //request.getRequestDispatcher("login.jsp").forward(request, response);
                
            }

    %>
	<div id="wrapper">
		<div id="header">
			This is a demo service provider<%=uid%>
		</div>
		 <!-- header closed -->
		
		<div id="content">
			 </br> </br> </br> </br> 
			Enter UID to start Authentication Process
			<form action="message.jsp" method="get">
				<table>
					<tr>
					<input type="text" name="uid" size="40">
					<input type="submit" value ="Continue" >
					</tr>
				</table>
			</form>
			 </br> </br> </br> </br>
			 </br> </br> </br>
	`	</div>
		<!-- content closed -->
		<div>
		</div>
		<div id="footer">
			<small>

                    <i>
                        Under the Guidance of : Dr. Naveen Chauhan , CSED </br>
                        Developed By : Bhaskar Kalia , Anurag Singh ,  Sushant Thakur , Nisha Kumari , Premlata Negi , CSE Final Year , NITH 
                    </i>

                </small> 
			
		</div>
		<!-- footer closed -->
	</div> 
	<!-- wrapper closed -->
	
</body>

