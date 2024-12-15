/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomerCard;

import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Nga
 */
public class UpdataInfo extends javax.swing.JFrame {

    ImageIcon icon = new ImageIcon("src/img/connect_failed.png");
    ImageIcon icon1 = new ImageIcon("src/img/icon_success.png");

    /**
     * Creates new form UpdataInfo
     */
    byte[] photo = null;
    SmartCardWord card = new SmartCardWord();
//    int idLen, nameLen, addressLen, phoneLen;
    int idLen, nameLen, addressLen, phoneLen, dobLen, insuranceLen;

//    public UpdataInfo() {
//        initComponents();
//        txtId.setEditable(false);
//        txtPhone.setEditable(false);
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
//    }
    public UpdataInfo() {
        initComponents();
        txtId.setEditable(false);
        setLocationRelativeTo(null);
        String Info = card.getInfo();
        System.out.println("info " + Info);
        byte[] bytes = card.hexStringToByteArray(Info);
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 0x03) {
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
                    insuranceLen = bytes.length - i - 1;
                }
            }
        }
        byte[] id, name, address, phone, dob, insurance;
        id = new byte[idLen];
        name = new byte[nameLen];
        address = new byte[addressLen];
        phone = new byte[phoneLen];
        dob = new byte[dobLen];
        insurance = new byte[insuranceLen];

        for (int i = 0; i < idLen; i++) {
            id[i] = bytes[i];
        }
        for (int i = 0; i < nameLen; i++) {
            name[i] = bytes[i + idLen + 1];
        }
        for (int i = 0; i < phoneLen; i++) {
            phone[i] = bytes[i + idLen + 1 + nameLen + 1];
        }
        for (int i = 0; i < addressLen; i++) {
            address[i] = bytes[i + idLen + 1 + nameLen + 1 + phoneLen + 1];
        }
        for (int i = 0; i < dobLen; i++) {
            dob[i] = bytes[i + idLen + 1 + nameLen + 1 + phoneLen + 1 + addressLen + 1];
        }
        for (int i = 0; i < insuranceLen; i++) {
            insurance[i] = bytes[i + idLen + 1 + nameLen + 1 + phoneLen + 1 + addressLen + 1 + dobLen + 1];
        }

        txtId.setText(new String(id, StandardCharsets.UTF_8));
        txtName.setText(new String(name, StandardCharsets.UTF_8));
        txtAddress.setText(new String(address, StandardCharsets.UTF_8));
        txtPhone.setText(new String(phone, StandardCharsets.UTF_8));
        txtDob.setText(new String(dob, StandardCharsets.UTF_8));
        txtInsurance.setText(new String(insurance, StandardCharsets.UTF_8));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        kGradientPanel1 = new keeptoo.KGradientPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtDob = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtInsurance = new javax.swing.JTextField();
        back = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        image = new javax.swing.JLabel();
        btnImage = new javax.swing.JButton();
        btnUpload = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("CẬP NHẬT THÔNG TIN KHÁCH HÀNG");

        jLabel2.setText("Họ Tên");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        kGradientPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(53, 66, 89), 3));
        kGradientPanel1.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel1.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel1.setPreferredSize(new java.awt.Dimension(1000, 600));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(149, 180, 204), 3));
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 600));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(53, 66, 89));
        jLabel4.setText("Họ Tên");

        txtName.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtName.setForeground(new java.awt.Color(53, 66, 89));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(53, 66, 89));
        jLabel5.setText("Số điện thoại");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(53, 66, 89));
        jLabel6.setText("Địa chỉ");

        txtAddress.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtAddress.setForeground(new java.awt.Color(53, 66, 89));
        txtAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddressActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("CẬP NHẬT THÔNG TIN");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(53, 66, 89));
        jLabel7.setText("ID");

        txtId.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtId.setForeground(new java.awt.Color(53, 66, 89));
        txtId.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtId.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(53, 66, 89));
        jLabel8.setText("Ngày sinh");

        txtDob.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtDob.setForeground(new java.awt.Color(53, 66, 89));
        txtDob.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDobActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(53, 66, 89));
        jLabel9.setText("Số bảo hiểm");

        txtInsurance.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtInsurance.setForeground(new java.awt.Color(53, 66, 89));
        txtInsurance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInsuranceActionPerformed(evt);
            }
        });

        back.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        back.setForeground(new java.awt.Color(53, 66, 89));
        back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/2099190 (1) (1).png"))); // NOI18N
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(53, 66, 89));
        jLabel3.setText("CẬP NHẬT THÔNG TIN");

        txtPhone.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtPhone.setForeground(new java.awt.Color(53, 66, 89));
        txtPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhoneActionPerformed(evt);
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
                        .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 63, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel7)
                                                .addComponent(jLabel4)
                                                .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                                .addComponent(txtId)
                                                .addComponent(jLabel5))
                                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(52, 52, 52)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel9)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtAddress)
                                            .addComponent(txtDob)
                                            .addComponent(txtInsurance, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(93, 93, 93)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(50, 50, 50))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(98, 98, 98))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel8))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInsurance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(170, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(149, 180, 204), 3));
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 600));

        image.setForeground(new java.awt.Color(53, 66, 89));
        image.setText("Ảnh");
        image.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(149, 180, 204)));

        btnImage.setBackground(new java.awt.Color(182, 205, 216));
        btnImage.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnImage.setForeground(new java.awt.Color(53, 66, 89));
        btnImage.setText("CHỌN ẢNH");
        btnImage.setToolTipText("");
        btnImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImageActionPerformed(evt);
            }
        });

        btnUpload.setBackground(new java.awt.Color(0, 102, 102));
        btnUpload.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        btnUpload.setForeground(new java.awt.Color(255, 255, 255));
        btnUpload.setText("LƯU ẢNH");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(btnImage)
                        .addGap(40, 40, 40)
                        .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(btnUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 490, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(183, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImage, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64)
                .addComponent(btnUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(134, 134, 134))
        );

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 815, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(144, 144, 144))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(243, 243, 243))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddressActionPerformed

    private void btnImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImageActionPerformed

// Mở hộp thoại để chọn ảnh
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Times New Roman", Font.BOLD, 24)));
        JFileChooser jFileChooser = new JFileChooser();
        int result = jFileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) { // Kiểm tra nếu người dùng đã chọn file
            File f = jFileChooser.getSelectedFile();
            String fileName = f.getAbsolutePath();

            // Lấy phần mở rộng của tệp
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
                JOptionPane.showMessageDialog(null, "Chỉ hỗ trợ định dạng JPG và PNG.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Hiển thị ảnh trong JLabel
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(fileName)
                        .getImage()
                        .getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH));
                image.setIcon(imageIcon); // Hiển thị ảnh

                // Đọc ảnh và chuyển thành byte array
                BufferedImage bImage = ImageIO.read(f);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                // Sử dụng định dạng tương ứng
                ImageIO.write(bImage, extension.equals("png") ? "png" : "jpg", bos);
                photo = bos.toByteArray();
                bos.close();

                System.out.println("Tệp ảnh đã chuyển thành byte array: " + photo.length + " bytes");
                JOptionPane.showMessageDialog(null, "Ảnh đã sẵn sàng để upload.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Lỗi khi tải ảnh: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Không có ảnh nào được chọn.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_btnImageActionPerformed

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed
        // TODO add your handling code here:
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Times New Roman", Font.BOLD, 24)));
        System.out.println("Test " + photo);
        if (card.connectCard()) {
            System.out.println("Kết nối đến applet");
        }
        String anh = String.format("%x", new BigInteger(1, photo));
        System.out.println("anh " + anh);
       byte[] avatarData = photo;  // Sử dụng trực tiếp mảng byte đã có
        boolean resUpload = card.uploadImage(avatarData);

        if (resUpload == true) {
            JOptionPane.showMessageDialog(null, "Upload ảnh thành công!", "", JOptionPane.INFORMATION_MESSAGE, icon1);
        } else {
            JOptionPane.showMessageDialog(null, "Upload ảnh thất bại!", "", JOptionPane.INFORMATION_MESSAGE, icon);
        }

    }//GEN-LAST:event_btnUploadActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Times New Roman", Font.BOLD, 24)));
        if (txtName.getText().equals("") || txtAddress.getText().equals("") || txtDob.getText().equals("")
                || txtInsurance.getText().equals("") || txtPhone.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đủ thông tin!", "", JOptionPane.INFORMATION_MESSAGE, icon);
        } else {
            String dob = txtDob.getText();
            if (!isValidDateFormat(dob)) {
                JOptionPane.showMessageDialog(null, "Ngày tháng năm sinh không đúng định dạng (dd/MM/yyyy) hoặc lớn hơn ngày hiện tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Lấy số điện thoại ban đầu từ cơ sở dữ liệu
            String currentPhone = DBConnection.getPhoneFromDatabase(txtId.getText()); // Hàm lấy số điện thoại ban đầu từ DB
            String phone = txtPhone.getText();

            // Kiểm tra định dạng số điện thoại
            String phoneRegex = "^(032|033|034|035|036|037|038|039|096|097|098|086|083|084|085|081|082|088|091|094|070|079|077|076|078|090|093|089|056|058|092|059|099)[0-9]{7}$";
            if (!phone.matches(phoneRegex)) {
                JOptionPane.showMessageDialog(null, "Số điện thoại không đúng định dạng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Nếu số điện thoại mới khác số ban đầu, kiểm tra trùng lặp
            if (!phone.equals(currentPhone) && DBConnection.isPhoneExist(phone)) {
                JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại trong hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tiếp tục xử lý cập nhật
            String stName = String.format("%x", new BigInteger(1, txtName.getText().getBytes()));
            String address = String.format("%x", new BigInteger(1, txtAddress.getText().getBytes()));
            dob = String.format("%x", new BigInteger(1, txtDob.getText().getBytes()));
            String insurance = String.format("%x", new BigInteger(1, txtInsurance.getText().getBytes()));
            String phoneHex = String.format("%x", new BigInteger(1, phone.getBytes()));

            String dataReq = stName + "03" + address + "03" + dob + "03" + insurance + "03" + phoneHex;

            System.out.println("dataReq: " + dataReq);

            // Gửi dữ liệu đến thẻ
            boolean res = card.changeInfo(card.hexStringToByteArray(dataReq));
            // Gửi dữ liệu đến db
            String formattedDob = convertDateToDatabaseFormat(txtDob.getText());
            boolean dbUpdateResult = DBConnection.updateUserInfo(
                    txtId.getText(), // ID người dùng
                    txtName.getText(), // Tên
                    txtAddress.getText(), // Địa chỉ
                    formattedDob, // Ngày sinh
                    txtInsurance.getText(), // Bảo hiểm
                    txtPhone.getText() // Số điện thoại
            );
            if (res) {
                JOptionPane.showMessageDialog(null, "Cập nhật thành công !", "", JOptionPane.INFORMATION_MESSAGE, icon1);
            } else {
                JOptionPane.showMessageDialog(null, "Cập nhật thất bại !", "", JOptionPane.INFORMATION_MESSAGE, icon);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    public static String convertDateToDatabaseFormat(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Trả về null nếu có lỗi
        }
    }

    public static boolean isValidDateFormat(String date) {
        // Định nghĩa định dạng ngày/tháng/năm (dd/MM/yyyy)
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); // Không cho phép ngày không hợp lệ (VD: 30/02)

        try {
            // Thử chuyển chuỗi ngày sang kiểu `Date`
            Date parsedDate = sdf.parse(date);

            // Lấy ngày hiện tại
            Date currentDate = new Date();

            // Kiểm tra ngày sinh có nhỏ hơn hoặc bằng ngày hiện tại không
            if (parsedDate.after(currentDate)) {
                return false; // Ngày sinh không hợp lệ vì lớn hơn ngày hiện tại
            }

            return true; // Ngày hợp lệ
        } catch (ParseException e) {
            return false; // Ngày không hợp lệ
        }
    }

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        // TODO add your handling code here:
        Patient customer = new Patient();
        customer.setVisible(true);
        customer.setLocationRelativeTo(null);
        this.setVisible(false);
    }//GEN-LAST:event_backActionPerformed

    private void txtDobActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDobActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDobActionPerformed

    private void txtInsuranceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInsuranceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInsuranceActionPerformed

    private void txtPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneActionPerformed

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
            java.util.logging.Logger.getLogger(UpdataInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdataInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdataInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdataInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UpdataInfo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JButton btnImage;
    private javax.swing.JButton btnUpload;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    private keeptoo.KGradientPanel kGradientPanel1;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtDob;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtInsurance;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPhone;
    // End of variables declaration//GEN-END:variables
}
