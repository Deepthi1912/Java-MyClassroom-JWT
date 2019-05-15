/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIPack;

import LibPack.ClassRoom;
import LibPack.StudentDetails;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class StudentMgtFrm extends javax.swing.JFrame {

    /**
     * Creates new form MainFrm
     */
    StudentDetails obj = null;
    ArrayList<ClassRoom> allClassDetails = null;
    ArrayList<StudentDetails> allStudentDetails = null;

    public StudentMgtFrm() {
        initComponents();
        setLocationRelativeTo(null);
        call_Read_ClassRoom_Servlet();
    }

    void call_Read_Student_Servlet() {
        try {

            String urlstr = "http://" + CommonVariable.serverIp + ":8084/PortalServer/ReadStudentServlet";
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
            allStudentDetails = (ArrayList<StudentDetails>) in.readObject();

            in.close();
            if (allStudentDetails != null && allStudentDetails.size() > 0) {
                DefaultListModel lm = new DefaultListModel();
                for (int i = 0; i < allStudentDetails.size(); i++) {
                    lm.addElement(allStudentDetails.get(i).stname);
                }
                jList1.setModel(lm);
            } else {
                DefaultListModel lm = new DefaultListModel();
                jList1.setModel(lm);
                JOptionPane.showMessageDialog(this, "No Student Found to Display");

            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();

        }
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

    void call_Add_Student_Servlet() {
        try {

            String urlstr = "http://" + CommonVariable.serverIp + ":8084/PortalServer/AddStudentServlet";
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

            out.writeObject(obj);
            out.close();

            // define a new ObjectInputStream on the input stream
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            // receive and deserialize the object, note the cast
            int resp = (int) in.readObject();

            in.close();

            if (resp == 2) {
                JOptionPane.showMessageDialog(this, "Student Already Added");
            } else if (resp == 1) {
                JOptionPane.showMessageDialog(this, "Student Created Successful");
                jTextField2.setText("");
                jTextField3.setText("");
                jTextField4.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Some thing went wrong please try again");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();

        }
    }

    int match_Class_Index(int id) {
        int index = -1;
        try {

            for (int i = 0; i < allClassDetails.size(); i++) {
                if (id == allClassDetails.get(i).classroomid) {
                    index = i;
                    break;
                }
            }
        } catch (Exception e) {
        }
        return index;
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
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(134, 58, 150));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 218, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Student Management");

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 218, 0));
        jLabel3.setText("Class Room Name");

        jButton1.setBackground(new java.awt.Color(255, 218, 0));
        jButton1.setText("Submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 218, 0));
        jButton2.setText("HOME");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel4.setText("Student List");

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 203, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 218, 0));
        jLabel5.setText("Student Roll No");

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 218, 0));
        jLabel6.setText("Student Name");

        jButton3.setBackground(new java.awt.Color(255, 218, 0));
        jButton3.setText("Refresh List");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 218, 0));
        jLabel7.setText("Student Password");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, 218, Short.MAX_VALUE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel6)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton3)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (jTextField2.getText().isEmpty() || jTextField3.getText().isEmpty() || jTextField4.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All Fields are Mandatory");
            return;
        } else {
            obj = new StudentDetails();
            obj.RollNO = Integer.parseInt(jTextField2.getText());
            obj.stname = jTextField3.getText();
            obj.classId = allClassDetails.get(jComboBox1.getSelectedIndex()).classroomid;
            obj.pwd = jTextField4.getText();
            call_Add_Student_Servlet();
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        new MainFrm().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        call_Read_Student_Servlet();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        // TODO add your handling code here:
        if (jList1.getSelectedIndex() != -1) {
            int index = jList1.getSelectedIndex();
            jTextField2.setText("" + allStudentDetails.get(index).RollNO);
            jTextField3.setText("" + allStudentDetails.get(index).stname);
            jTextField4.setText("" + allStudentDetails.get(index).pwd);
            int k = match_Class_Index(allStudentDetails.get(index).classId);
            if (k != -1) {
                jComboBox1.setSelectedIndex(k);
            }

        }
    }//GEN-LAST:event_jList1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
