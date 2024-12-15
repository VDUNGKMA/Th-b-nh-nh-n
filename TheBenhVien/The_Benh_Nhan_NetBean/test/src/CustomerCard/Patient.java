/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomerCard;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.smartcardio.Card;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Nga
 */
public class Patient extends javax.swing.JFrame {

    /**
     * Creates new form KhachHang
     */
    SmartCardWord card = new SmartCardWord();
    InfoPatient info = new InfoPatient();
//    int idLen, nameLen, addressLen, phoneLen;
    int idLen, nameLen, addressLen, phoneLen, dobLen, insuranceLen;
//    public KhachHang() {
//        initComponents();
//        setLocationRelativeTo(null);
//        String Info = card.getInfo();
//        System.out.println("info " + Info);
//        byte[] bytes = card.hexStringToByteArray(Info);
//        for (int i = 0; i < bytes.length; i++) {
//            if (bytes[i] == 0x03) {
//                if (idLen == 0) {
//                    idLen = i;
//                } else if (nameLen == 0) {
//                    nameLen = i - idLen - 1;
//                } else if (phoneLen == 0) {
//                    phoneLen = i - idLen - nameLen - 2;
//                    addressLen = bytes.length - i - 1;
//                }
//            }
//        }
//        
//        byte[] id, name, address, phone;
//        id = new byte[idLen];
//        name = new byte[nameLen];
//        address = new byte[addressLen];
//        phone = new byte[phoneLen];
//        for (int i = 0; i < idLen; i++) {
//            id[i] = bytes[i];
//        }
//        
//        for (int i = 0; i < nameLen; i++) {
//            name[i] = bytes[i + idLen + 1];
//        }
//        
//        for (int i = 0; i < phoneLen; i++) {
//            phone[i] = bytes[i + idLen + 1 + nameLen + 1];
//        }
//        
//        for (int i = 0; i < addressLen; i++) {
//            address[i] = bytes[i + idLen + 1 + nameLen + 1 + phoneLen + 1];
//        }        
//        
//        txtId.setText(new String(id, StandardCharsets.UTF_8));
//        txtName.setText(new String(name, StandardCharsets.UTF_8));
//        txtAddress.setText(new String(address, StandardCharsets.UTF_8));
//        txtPhone.setText(new String(phone, StandardCharsets.UTF_8));
//        //So du, tich diem 
//        String du= card.checkScore();
//        String temp= du.substring(1);
//        String[] divide=temp.split("ff");
//        //1000000
//        int soDu= Integer.valueOf(divide[0]);
//        txtSoDu.setText(String.valueOf(soDu));
//        int diem= Integer.valueOf(divide[1]);
//        txtScore.setText(String.valueOf(diem));
//        
//        //avatar
//        String studentAvatar = card.getAvatar();
//        if (studentAvatar.equals("") == false) {
//            byte[] bytesAvatar = card.hexStringToByteArray(studentAvatar);
//            ByteArrayInputStream bais = new ByteArrayInputStream(bytesAvatar);
//            BufferedImage b;
//            try {
//                b = ImageIO.read(bais);
//                ImageIcon icon = new ImageIcon(b.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH));
//                icon.getImage();
//                image.setIcon(icon);
//            } catch (IOException ex) {
//                
//            }
//        }
//        
//    }
//

    public Patient() {
        initComponents();
        setLocationRelativeTo(null);

        // Retrieve the card information
        String Info = card.getInfo();
        System.out.println("Info: " + Info);
        byte[] bytes = card.hexStringToByteArray(Info);

        // Parse lengths of individual fields
        int lastDelimiter = -1; // Vị trí của ký tự 0x03 cuối cùng
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 0x03) {
                lastDelimiter = i; // Lưu vị trí
                if (idLen == 0) {
                    idLen = i;
                } else if (nameLen == 0) {
                    nameLen = i - idLen - 1;
                } else if (phoneLen == 0) {
                    phoneLen = i - idLen - nameLen - 2;
                } else if (addressLen == 0) {
                    addressLen = i - idLen - nameLen - phoneLen - 3;
                } else if (dobLen == 0) {
                    dobLen = i - idLen - nameLen - phoneLen - addressLen - 4;
                }
            }
        }
// Tính insuranceLen dựa trên lastDelimiter
        insuranceLen = bytes.length - lastDelimiter - 1;

        // Allocate byte arrays for fields
        byte[] id = new byte[idLen];
        byte[] name = new byte[nameLen];
        byte[] address = new byte[addressLen];
        byte[] phone = new byte[phoneLen];
        byte[] dob = new byte[dobLen];
        byte[] insurance = new byte[insuranceLen];

        // Fill the byte arrays
        System.arraycopy(bytes, 0, id, 0, idLen);
        System.arraycopy(bytes, idLen + 1, name, 0, nameLen);
        System.arraycopy(bytes, idLen + nameLen + 2, phone, 0, phoneLen);
        System.arraycopy(bytes, idLen + nameLen + phoneLen + 3, address, 0, addressLen);
        System.arraycopy(bytes, idLen + nameLen + phoneLen + addressLen + 4, dob, 0, dobLen);
        System.arraycopy(bytes, idLen + nameLen + phoneLen + addressLen + dobLen + 5, insurance, 0, insuranceLen);

        // Set text fields
        txtId.setText(new String(id, StandardCharsets.UTF_8));
        txtName.setText(new String(name, StandardCharsets.UTF_8));
        txtAddress.setText(new String(address, StandardCharsets.UTF_8));
        txtPhone.setText(new String(phone, StandardCharsets.UTF_8));
        txtDob.setText(new String(dob, StandardCharsets.UTF_8));
        txtInsurance.setText(new String(insurance, StandardCharsets.UTF_8));
        System.out.println("Insurance: " + new String(insurance, StandardCharsets.UTF_8));

        String sodu = card.checkBalance(); // Gọi phương thức checkBalance() từ thẻ
        System.out.println("check so du: " + sodu); // In số dư để kiểm tra
        long soDuTK = Long.parseLong(sodu); // Chuyển đổi số dư thành kiểu long
        txtSoDu.setText(String.valueOf(soDuTK)); // Cập nhật số dư trên giao diện
        // Display avatar (if available)
        String studentAvatar = card.getAvatar();
        if (!studentAvatar.isEmpty()) {
            byte[] bytesAvatar = card.hexStringToByteArray(studentAvatar);
            try (ByteArrayInputStream bais = new ByteArrayInputStream(bytesAvatar)) {
                BufferedImage imageBuffer = ImageIO.read(bais);
                ImageIcon icon = new ImageIcon(imageBuffer.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH));
                image.setIcon(icon);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error loading avatar!", "Error", JOptionPane.ERROR_MESSAGE);
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

        jTextField6 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        kGradientPanel1 = new keeptoo.KGradientPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtId = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtName = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtSoDu = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtDob = new javax.swing.JLabel();
        image = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        btnNapTien = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtInsurance = new javax.swing.JLabel();

        jTextField6.setMinimumSize(new java.awt.Dimension(6, 28));

        jLabel7.setText("Tài khoản");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(213, 234, 226));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(149, 180, 204), 3));

        kGradientPanel1.setBackground(new java.awt.Color(0, 102, 102));
        kGradientPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(53, 66, 89), 3));
        kGradientPanel1.setkEndColor(new java.awt.Color(0, 102, 102));
        kGradientPanel1.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel1.setPreferredSize(new java.awt.Dimension(1005, 690));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 58)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(53, 66, 89));
        jLabel1.setText("Thông tin bệnh nhân");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(53, 66, 89));
        jLabel2.setText("ID");

        txtId.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        txtId.setForeground(new java.awt.Color(53, 66, 89));
        txtId.setText("42384328742");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(53, 66, 89));
        jLabel3.setText("Họ tên");

        txtName.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        txtName.setForeground(new java.awt.Color(53, 66, 89));
        txtName.setText("nguyen van a");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(53, 66, 89));
        jLabel5.setText("Địa chỉ");

        txtAddress.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        txtAddress.setForeground(new java.awt.Color(53, 66, 89));
        txtAddress.setText("ha noi");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(53, 66, 89));
        jLabel4.setText("Số điện thoại");

        txtPhone.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        txtPhone.setForeground(new java.awt.Color(53, 66, 89));
        txtPhone.setText("0987631123");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(53, 66, 89));
        jLabel6.setText("Số dư");

        txtSoDu.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        txtSoDu.setForeground(new java.awt.Color(53, 66, 89));
        txtSoDu.setText("90000000");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(53, 66, 89));
        jLabel8.setText("Ngày sinh");

        txtDob.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        txtDob.setForeground(new java.awt.Color(53, 66, 89));
        txtDob.setText("12/12/2002");

        image.setForeground(new java.awt.Color(255, 255, 255));
        image.setText("Ảnh");
        image.setAutoscrolls(true);
        image.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(53, 66, 89), 2));

        btnUpdate.setBackground(new java.awt.Color(53, 66, 89));
        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setText("CẬP NHẬT");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnNapTien.setBackground(new java.awt.Color(53, 66, 89));
        btnNapTien.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        btnNapTien.setForeground(new java.awt.Color(255, 255, 255));
        btnNapTien.setText("NẠP TIỀN");
        btnNapTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNapTienActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(53, 66, 89));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("ĐỔI MÃ PIN");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(53, 66, 89));
        jLabel9.setText("Số bảo hiểm");

        txtInsurance.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        txtInsurance.setForeground(new java.awt.Color(53, 66, 89));
        txtInsurance.setText("212749fiwu");

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(173, 173, 173)
                        .addComponent(btnNapTien, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(124, 124, 124)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(kGradientPanel1Layout.createSequentialGroup()
                            .addGap(409, 409, 409)
                            .addComponent(jLabel1))
                        .addGroup(kGradientPanel1Layout.createSequentialGroup()
                            .addGap(99, 99, 99)
                            .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addGap(87, 87, 87))
                                .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addGap(27, 27, 27))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                    .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3))
                                    .addGap(88, 88, 88)))
                            .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                    .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                            .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(63, 63, 63))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(74, 74, 74)))
                                    .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtSoDu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtDob, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                                        .addComponent(txtInsurance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(33, 33, 33)
                            .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(263, Short.MAX_VALUE))
        );

        kGradientPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnNapTien, btnUpdate, jButton1});

        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6)
                            .addComponent(txtSoDu))
                        .addGap(31, 31, 31)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel8)
                            .addComponent(txtDob))
                        .addGap(31, 31, 31)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel9)
                            .addComponent(txtInsurance))
                        .addGap(31, 31, 31)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNapTien, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(209, 209, 209))
        );

        kGradientPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtAddress, txtPhone});

        kGradientPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnNapTien, btnUpdate, jButton1});

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1400, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        UpdataInfo update = new UpdataInfo();
        update.setVisible(true);
        update.setLocationRelativeTo(null);
        this.setVisible(false);
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnNapTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNapTienActionPerformed
        // TODO add your handling code here:
        FormVerify xacThuc = new FormVerify();
        xacThuc.setVisible(true);
        xacThuc.setLocationRelativeTo(null);
        this.setVisible(false);

    }//GEN-LAST:event_btnNapTienActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        FormChangePin changePin = new FormChangePin();
        changePin.setVisible(true);
        changePin.setLocationRelativeTo(null);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Patient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Patient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Patient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Patient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Patient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNapTien;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel image;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField6;
    private keeptoo.KGradientPanel kGradientPanel1;
    private javax.swing.JLabel txtAddress;
    private javax.swing.JLabel txtDob;
    private javax.swing.JLabel txtId;
    private javax.swing.JLabel txtInsurance;
    private javax.swing.JLabel txtName;
    private javax.swing.JLabel txtPhone;
    private javax.swing.JLabel txtSoDu;
    // End of variables declaration//GEN-END:variables
}
