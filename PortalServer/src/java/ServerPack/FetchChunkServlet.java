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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FetchChunkServlet extends HttpServlet {

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
        DataLib obj = null;
        String connection = "jdbc:mysql://localhost:3306/portaldb";
        String user = "root";
        String password = "root";
        Connection con = null;
        Statement stmt = null;
        boolean resp = false;
        try {
            ObjectInputStream in = new ObjectInputStream(request.getInputStream());
            obj = (DataLib) in.readObject();
            in.close();
        } catch (Exception e) {
            System.out.println("Error in part1");
        }
        DataLib lib = new DataLib();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(connection, user, password);
        } catch (Exception e) {
            System.out.println("Error in part2");
        }
        System.out.println("Obj: " + obj.fileid);

        try {
            String ssql = "select content from files where fid='" + obj.fileid + "'";
            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st.executeQuery(ssql);
            while (rs.next()) {
                byte[] barr = rs.getBytes("content");
                int buffer = barr.length - (obj.chunkId * 1024);

                if (buffer > 1024) {
                    lib.data = new byte[1024];
                    for (int i = 0; i < 1024; i++) {
                        lib.data[i] = barr[(obj.chunkId * 1024) + i];
                    }
                } else {
                    lib.data = new byte[buffer];
                    for (int i = 0; i < buffer; i++) {
                        lib.data[i] = barr[(obj.chunkId * 1024) + i];
                    }
                }
            }

            con.close();
        } catch (Exception e) {
            System.out.println("Error in fetchChunk method: " + e);
        }

        try {
            ObjectOutputStream obOut = new ObjectOutputStream(response.getOutputStream());
            obOut.writeObject(lib);
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
