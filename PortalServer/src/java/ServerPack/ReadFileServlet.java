/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerPack;

import LibPack.DataLib;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReadFileServlet extends HttpServlet {

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
        DataLib lib;
        ArrayList<DataLib> list = new ArrayList<DataLib>();
        String connection = "jdbc:mysql://localhost:3306/portaldb";
        String user = "root";
        String password = "root";
        Connection con = null;
        Statement stmt = null;
        int classid = 0;
        try {
            ObjectInputStream in = new ObjectInputStream(request.getInputStream());
            classid = (int) in.readObject();
            in.close();
        } catch (Exception e) {
            System.out.println("Error in part1");
        }

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(connection, user, password);
        } catch (Exception e) {
            System.out.println("Error in part2");
        }

        try {
            String ssql = "select fid,fname from files where classID=" + classid;
            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st.executeQuery(ssql);
            while (rs.next()) {
                lib = new DataLib();
                lib.fileid = rs.getInt("fid");
                lib.name = rs.getString("fname");
                list.add(lib);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("error in showList:" + e);
        }

        try {
            ObjectOutputStream obOut = new ObjectOutputStream(response.getOutputStream());
            obOut.writeObject(list);
            obOut.close();
        } catch (Exception e) {
            System.out.println("Error in part4");
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
