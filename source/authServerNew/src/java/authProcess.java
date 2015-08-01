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
public class authProcess extends HttpServlet {

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

            //get values from request
            String CID = (String) request.getParameter("CID");
            String N = (String) request.getParameter("N");
            String C = (String) request.getParameter("C");
            String hUID = (String) request.getParameter("hUID");

            //create a check if entry exists in RT . if not then give warning
            //make entry to database now with hash of uid and email
            Context initialContext = new InitialContext();
            Context environmentContext = (Context) initialContext.lookup("java:comp/env");

            // Look up our data source
            DataSource ds = (DataSource) environmentContext.lookup("jdbc/major");

            Connection conn = ds.getConnection();
            PreparedStatement s = conn.prepareStatement("select * from major.RT where uid=?");
            s.setString(1, hUID);

            ResultSet rs = s.executeQuery();
            if (rs.next()) { //entry is there
                int status = rs.getInt("status");
                if (status == 1) {
                    out.write("2");
                } else {
                    String y = new hashAlgo().execute("MajorProject").substring(0, 8);

                    //i now have y , CID , N , C , hUID
                    String hPass = "";
                    String B = "";
                    String newC = "";

                    long xor1 = Long.parseLong(CID, 16);
                    long xor2 = Long.parseLong(N, 16);
                    long xor3 = Long.parseLong(y, 16);

                    long xor = xor2 ^ xor3;

                    String z = (Long.toHexString(xor)).toUpperCase();
                    String hZ = new hashAlgo().execute(z).substring(0, 8);

                    long xor4 = Long.parseLong(hZ, 16);

                    long xor5 = xor1 ^ xor4; //hPass
                    hPass = (Long.toHexString(xor5)).toUpperCase();

                    long b = xor1 ^ xor5;
                    String b1 = (Long.toHexString(b)).toUpperCase();
                    B = new hashAlgo().execute(b1).substring(0, 8);
                    long b2 = Long.parseLong(B, 16);

                    //now i have b also 
                    long c = xor2 ^ b2 ^ xor3;

                    newC = (Long.toHexString(c)).toUpperCase();
                    newC = new hashAlgo().execute(newC).substring(0, 8);

                    //compare newC and recieved  c and based  upon that authenticate
                    if (newC.equals(C)) {
                        //update status in RT
                        PreparedStatement s9 = conn.prepareStatement("update RT set status=? where uid=?");
                        s9.setInt(1, 1);
                        s9.setString(2,hUID);

                        int a=s9.executeUpdate();
                        if(a==1){
                            out.write("1");
                        }else {
                            out.write("3");
                        }
                        
                        
                    } else {
                        out.write("3");
                    }
                }
            } else {
                out.write("0");
            }

            //String b = (Long.toHexString(xor5)).toUpperCase()
            //  out.write(CID+"\n"+N+"\n"+C+"\n"+hUID);
        } catch (Exception ex) {
            Logger.getLogger(authProcess.class.getName()).log(Level.SEVERE, null, ex);
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
