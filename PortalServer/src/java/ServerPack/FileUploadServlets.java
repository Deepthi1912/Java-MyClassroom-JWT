/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerPack;

import LibPack.DataLib;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileUploadServlets extends HttpServlet {

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

        try {
            //  System.out.println("Calling this Function........");
            FileOutputStream fout = new FileOutputStream(new File(Settings.storagePath + obj.name), true);
            fout.write(obj.data);
            fout.close();

            if (obj.flag) {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(connection, user, password);
                byte data[];
                FileInputStream fin = new FileInputStream(new File(Settings.storagePath + obj.name));
                data = new byte[fin.available()];
                fin.read(data);
                fin.close();
                ByteArrayInputStream bis = new ByteArrayInputStream(data);

                String ssql = "insert into files values(?,?,?,?)";
                PreparedStatement pre;
                pre = con.prepareStatement(ssql);
                pre.setInt(1, 0);
                pre.setString(2, obj.name);
                pre.setBinaryStream(3, bis, (long) (data.length));
                pre.setInt(4, obj.classId);
                pre.executeUpdate();
                con.close();
                File f = new File(Settings.storagePath + obj.name);
                if (f.exists()) {
                    f.delete();
                }
                resp = true;
            }

        } catch (Exception e) {
            System.out.println("Error in uploading file:" + e);
            resp = false;
        }

        try {
            ObjectOutputStream obOut = new ObjectOutputStream(response.getOutputStream());
            obOut.writeObject(resp);
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
