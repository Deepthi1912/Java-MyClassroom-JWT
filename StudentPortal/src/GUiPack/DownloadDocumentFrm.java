/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUiPack;

import LibPack.DataLib;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class DownloadDocumentFrm extends javax.swing.JFrame {

    /**
     * Creates new form DownloadDocumentFrm
     */
    ArrayList<DataLib> list = new ArrayList<DataLib>();
    DataLib lib = null;
    int index;
    int id;
    boolean running = false;
    Timer t;
    MyDownloadTask dTask;
    String destPath = "";

    public DownloadDocumentFrm() {
        initComponents();
        setLocationRelativeTo(null);

        dTask = new MyDownloadTask(this);
        t = new Timer();
        t.schedule(dTask, 100, 10);
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

            out.writeObject(CommonVariable.studentClass);
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

    DataLib call_Servlet_Read_Chunk(DataLib toserverlib) {
        DataLib respFromServer = null;
        try {

            String urlstr = "http://" + Settings.serverIP + ":8084/PortalServer/FetchChunkServlet";
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

            out.writeObject(toserverlib);
            out.close();

            // define a new ObjectInputStream on the input stream
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            // receive and deserialize the object, note the cast
            respFromServer = (DataLib) in.readObject();

            in.close();

        } catch (Exception e) {

            System.out.println("Error: " + e);
            e.printStackTrace();
        }
        return respFromServer;
    }

    DataLib call_Servlet_Init_Download(DataLib toServerlib) {
        DataLib respFromServer = null;
        try {

            String urlstr = "http://" + Settings.serverIP + ":8084/PortalServer/InitializeDownloadServet";
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

            out.writeObject(toServerlib);
            out.close();

            // define a new ObjectInputStream on the input stream
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            // receive and deserialize the object, note the cast
            respFromServer = (DataLib) in.readObject();

            in.close();

        } catch (Exception e) {

            System.out.println("Error: " + e);
            e.printStackTrace();
        }
        return respFromServer;
    }

    class MyDownloadTask extends TimerTask {

        DownloadDocumentFrm parent;

        int totalChunks;
        String filename;

        MyDownloadTask(DownloadDocumentFrm parent) {
            this.parent = parent;

        }

        @Override
        public void run() {
            if (parent.running) {
                lib = new DataLib();
                //System.out.println("Parent ID: " + parent.id);
                lib.fileid = parent.id;
                lib = call_Servlet_Init_Download(lib);
                //  System.out.println("Lib Id: " + lib.fileid);
                totalChunks = lib.chunkSize;
                filename = lib.name;
                parent.jProgressBar1.setMaximum(totalChunks);
                for (int i = 0; i < totalChunks; i++) {
                    DataLib lib = new DataLib();
                    lib.chunkId = i;
                    lib.fileid = parent.id;
                    //   System.out.println("File ID To Server: " + lib.fileid);
                    lib = (DataLib) call_Servlet_Read_Chunk(lib);
                    try {
                        FileOutputStream fout = new FileOutputStream(new File(destPath + "\\" + filename), true);
                        fout.write(lib.data);
                        fout.close();
                        parent.jProgressBar1.setValue(i + 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                parent.running = false;
                parent.jProgressBar1.setValue(0);
                JOptionPane.showMessageDialog(parent, "File Downloaded Successfully");
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
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton3 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(134, 58, 150));

        jLabel1.setForeground(new java.awt.Color(255, 218, 0));
        jLabel1.setText("Document List");

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

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
        jButton4.setText("Download");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jList1MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        call_Servlet_Read_File();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        new MainFrm().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Select Destination Folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            destPath = chooser.getSelectedFile().toString();
            if (jList1.getSelectedIndex() != -1) {
                index = jList1.getSelectedIndex();
                id = list.get(index).fileid;
                running = true;
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
