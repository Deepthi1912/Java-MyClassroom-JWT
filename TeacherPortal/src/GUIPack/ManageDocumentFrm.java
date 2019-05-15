/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIPack;

import LibPack.ClassRoom;
import LibPack.DataLib;
import LibPack.MessageDetails;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class ManageDocumentFrm extends javax.swing.JFrame {

    /**
     * Creates new form CommunicateFrm
     */
    ArrayList<ClassRoom> allClassDetails = null;
    String path;
    String name;
    boolean running = false;
    DataLib lib = null;
    Timer t;
    ArrayList<DataLib> list = new ArrayList<DataLib>();
    MyUploadTask Utask;

    public ManageDocumentFrm() {
        initComponents();
        setLocationRelativeTo(null);
        call_Read_ClassRoom_Servlet();
        Utask = new MyUploadTask(this);
        t = new Timer();
        t.schedule(Utask, 100, 10);
    }

    boolean call_Servlet_Read_File() {
        boolean respFromServer = false;
        try {

            String urlstr = "http://" + Settings.serverIP + ":8084/PortalServer/ReadFileServlet";
            URL url = new URL(urlstr);
            URLConnection connection = url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);

            // don't use a cached version of URL connection
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // specify the content type that binary data is sent
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
            // send and serialize the object

            out.writeObject(allClassDetails.get(jComboBox1.getSelectedIndex()).classroomid);
            out.close();

            // define a new ObjectInputStream on the input stream
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            // receive and deserialize the object, note the cast
            list = (ArrayList<DataLib>) in.readObject();

            in.close();
            if (list != null && list.size() > 0) {
                DefaultListModel lm = new DefaultListModel();
                for (int i = 0; i < list.size(); i++) {

                    lm.addElement(list.get(i).name);
                    // System.out.println("File Id: " + list.get(i).fileid);
                }
                jList1.setModel(lm);
            } else {
                DefaultListModel lm = new DefaultListModel();
                jList1.setModel(lm);
                JOptionPane.showMessageDialog(this, "No Documents Available");
            }

        } catch (Exception e) {

            System.out.println("Error: " + e);
            e.printStackTrace();
        }
        return respFromServer;
    }

    void call_Read_ClassRoom_Servlet() {
        try {

            String urlstr = "http://" + CommonVariable.serverIp + ":8084/PortalServer/ReadClassRoomServlet";
            URL url = new URL(urlstr);
            URLConnection connection = url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);

            // don't use a cached version of URL connection
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // specify the content type that binary data is sent
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
            // send and serialize the object

            out.writeObject(CommonVariable.teacherId);
            out.close();

            // define a new ObjectInputStream on the input stream
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            // receive and deserialize the object, note the cast
            allClassDetails = (ArrayList<ClassRoom>) in.readObject();

            in.close();
            if (allClassDetails != null && allClassDetails.size() > 0) {
                jComboBox1.removeAllItems();
                for (int i = 0; i < allClassDetails.size(); i++) {
                    jComboBox1.addItem(allClassDetails.get(i).classroom);
                }
            } else {
                jComboBox1.removeAllItems();
                JOptionPane.showMessageDialog(this, "No Class Room Found to Display");

            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();

        }
    }

    boolean call_Servlet() {
        boolean respFromServer = false;
        try {

            String urlstr = "http://" + Settings.serverIP + ":8084/PortalServer/FileUploadServlets";
            URL url = new URL(urlstr);
            URLConnection connection = url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);

            // don't use a cached version of URL connection
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // specify the content type that binary data is sent
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
            // send and serialize the object

            out.writeObject(lib);
            out.close();

            // define a new ObjectInputStream on the input stream
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            // receive and deserialize the object, note the cast
            respFromServer = (boolean) in.readObject();

            in.close();

        } catch (Exception e) {

            System.out.println("Error: " + e);
            e.printStackTrace();
        }
        return respFromServer;
    }

    class MyUploadTask extends TimerTask {

        ManageDocumentFrm parent;
        int chunkSize;
        boolean lastChunk = false;
        boolean done = false;

        boolean end = false;
        public byte data[];

        MyUploadTask(ManageDocumentFrm parent) {
            this.parent = parent;

        }

        @Override
        public void run() {
            if (parent.running) {
                try {
                    FileInputStream fin = new FileInputStream(new File(parent.path));
                    int available = fin.available();
                    chunkSize = available / 1024;
                    int residualSize = available - (chunkSize * 1024);
                    if (residualSize > 0) {
                        chunkSize++;
                    }
                    parent.jProgressBar1.setMaximum(chunkSize);
                    for (int i = 0; i < chunkSize; i++) {
                        lib = new DataLib();
                        lib.name = parent.name;
                        lib.classId = allClassDetails.get(jComboBox1.getSelectedIndex()).classroomid;
                        if (available > 1024) {
                            data = new byte[1024];
                            available -= 1024;
                            fin.read(data);
                            lib.data = data;
                            lib.flag = false;
                            call_Servlet();
                        } else {
                            System.out.println("data size is:" + fin.available());
                            data = new byte[available];
                            lastChunk = true;
                            fin.read(data);
                            lib.data = data;
                            lib.flag = true;
                            done = call_Servlet();
                        }

                        parent.jProgressBar1.setValue(i + 1);
                        Thread.sleep(10);
                    }
                    fin.close();
                    if (done) {
                        end = true;
                        JOptionPane.showMessageDialog(parent, "File Uploaded Successfully");
                        parent.running = false;
                        parent.jProgressBar1.setValue(0);
                    } else {
                        JOptionPane.showMessageDialog(parent, "File Uploading Failed!");
                        parent.running = false;

                    }
                } catch (Exception e) {
                    System.out.println("error in timer task" + e);
                }
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(134, 58, 150));

        jPanel2.setBackground(new java.awt.Color(134, 58, 150));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel1.setForeground(new java.awt.Color(255, 218, 0));
        jLabel1.setText("Document List");

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jLabel3.setForeground(new java.awt.Color(255, 218, 0));
        jLabel3.setText("Select Class");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setBackground(new java.awt.Color(255, 218, 0));
        jButton1.setText("Choose Document to Upload");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 218, 0));
        jButton3.setText("Refresh");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 218, 0));
        jButton2.setText("Back");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 218, 0));
        jButton4.setText("Upload Document");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                                    .addComponent(jTextField1)))
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(147, 147, 147))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 395, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        call_Servlet_Read_File();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jList1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            FileDialog fd = new FileDialog(this, "select File", FileDialog.LOAD);
            fd.setVisible(true);
            path = fd.getDirectory() + fd.getFile();
            name = fd.getFile();
            jTextField1.setText(name);
        } catch (Exception e) {
            System.out.println("error in selecting file" + e);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        new MainFrm().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        running = true;
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
