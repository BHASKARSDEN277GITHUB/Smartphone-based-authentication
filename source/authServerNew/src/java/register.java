/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


/**
 *
 * @author root
 */
public class register extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String encHPass = (String) request.getParameter("encHPass");
            String encHUID = (String) request.getParameter("encHUID");
            String email = (String) request.getParameter("email");//to be stored in UT

            String hPass = decryptor.decrypt(encHPass);
            String uid = decryptor.decrypt(encHUID); //to be stored in UT
               // out.write(uid);
            /**
             * check username validity first of all
             */
            //make entry to database now with hash of uid and email
            Context initialContext = new InitialContext();
            Context environmentContext = (Context) initialContext.lookup("java:comp/env");

            // Look up our data source
            DataSource ds = (DataSource) environmentContext.lookup("jdbc/major");

            try (Connection conn = ds.getConnection()) {
                PreparedStatement s = conn.prepareStatement("select * from major.UT where uid=?");
                s.setString(1, uid);

                ResultSet rs = s.executeQuery();
                if (rs.next()) { //
                    out.write("0");
                } else {
                    //out.write(hPass+uid);

                    String x = new hashAlgo().execute("iamamastersecretkey").substring(0, 8);
                    String y = new hashAlgo().execute("MajorProject").substring(0, 8);
            //out.write(hPass+uid);

                    String nonce = ""; //calculating nonce here
                    long xor1 = Long.parseLong(hPass, 16);
                    long xor2 = Long.parseLong(x, 16);

                    long xor = xor1 ^ xor2;

                    nonce = (Long.toHexString(xor)).toUpperCase();

            //make entry to database now with hash of uid and email
                    try (Connection conn1 = ds.getConnection()) {
                        PreparedStatement s1 = conn1.prepareStatement("insert into major.UT values(?,?)");
                        s1.setString(1, uid);
                        s1.setString(2, email);

                        int a = s1.executeUpdate();
                        if (a == 1) { //registered user to the database successfully . Now send nonce , y back to the user authApp
                            out.write(y + " " + nonce);
                        }
                    }

                }
            }

        } catch (Exception ex) {
            Logger.getLogger(register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
