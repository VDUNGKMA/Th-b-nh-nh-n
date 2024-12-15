package Hospital;

import CustomerCard.SmartCardWord;
import CustomerCard.InfoPatient;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class FormPay extends javax.swing.JFrame {

    InfoPatient customer = new InfoPatient();
    SmartCardWord card = new SmartCardWord();
    boolean isConnect = false;
    ImageIcon icon = new ImageIcon("src/img/connect_failed.png");
    ImageIcon icon1 = new ImageIcon("src/img/icon_success.png");
    private int productCount = 0;

    public FormPay() {
        initComponents();
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Times New Roman", Font.BOLD, 24)));
        if (card.connectCard()) {
            isConnect = true;
            JOptionPane.showMessageDialog(this, "Kết nối đến thẻ thành công!", "", JOptionPane.INFORMATION_MESSAGE, icon1);

        } else {
            JOptionPane.showMessageDialog(this, "Chưa kết nối được thẻ!", "", JOptionPane.INFORMATION_MESSAGE, icon);
            isConnect = false;
        }
    }

    public void addTable(String Name, Double Price) {
//        Double tqty;
//
//        String Qty = JOptionPane.showInputDialog("Nhập số lượng");
//        if (Qty == null) {
//            Qty = "";
//        }
//        if (Qty.isEmpty()) {
////            tqty = Double.valueOf(0);
//            tqty = 0.0;
//        } else {
//
//            tqty = Double.valueOf(Qty);
//            if (tqty > 0) {
//                Double Tot_Price = Price * tqty;
//
//                DecimalFormat df = new DecimalFormat("00");
//                String d11 = df.format(Tot_Price);
//
//                DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
//
//                Vector v = new Vector();
//                v.add(Name);
//                v.add(Qty);
//                v.add(d11);
//                dt.addRow(v);
//            }
//
//        }
//        cart_cal();
        int quantity;

        try {
            String Qty = JOptionPane.showInputDialog("Nhập số lượng (nguyên):");
            if (Qty == null || Qty.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Số lượng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            quantity = Integer.parseInt(Qty); // Ép kiểu thành số nguyên

            if (quantity > 0) {
                double totalPrice = Price * quantity;

                // Định dạng tiền tệ
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String formattedPrice = currencyFormat.format(totalPrice);

                DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();

                Vector v = new Vector();
                v.add(Name); // Tên sản phẩm
                v.add(quantity); // Số lượng
                v.add(formattedPrice); // Tổng giá (định dạng tiền tệ)
                dt.addRow(v);

                cart_cal(); // Cập nhật tổng tiền
            } else {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cart_cal() {

//        int numofrow = jTable1.getRowCount();
//        double total = 0;
//        for (int i = 0; i < numofrow; i++) {
//            double value = Double.parseDouble(jTable1.getValueAt(i, 2).toString());
//            total += value;
//
//        }
//
//        DecimalFormat df = new DecimalFormat("00");
//        String d1 = df.format(total);
//        Too.setText(d1);
        int numofrow = jTable1.getRowCount();
        double total = 0;

        for (int i = 0; i < numofrow; i++) {
            String value = jTable1.getValueAt(i, 2).toString().replaceAll("[^\\d]", ""); // Xóa định dạng tiền tệ
            total += Double.parseDouble(value);
        }

        // Định dạng tổng tiền
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotal = currencyFormat.format(total);
        Too.setText(formattedTotal); // Cập nhật tổng tiền

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        kGradientPanel1 = new keeptoo.KGradientPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Too = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        kGradientPanel1.setkEndColor(new java.awt.Color(0, 102, 102));
        kGradientPanel1.setkStartColor(new java.awt.Color(0, 102, 102));
        kGradientPanel1.setPreferredSize(new java.awt.Dimension(1053, 600));

        jTable2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jTable2.setForeground(new java.awt.Color(53, 66, 89));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"#K001", "Nabumetone",  new Double(50000.0)},
                {"#S002", "Diclofenac",  new Double(2000.0)},
                {"#S003", "Paracetamol",  new Double(30000.0)},
                {"#C002", "ibuprofen",  new Double(70000.0)},
                {"#SPF2", "Panadol",  new Double(40000.0)},
                {"#SPF3", "Aspirin",  new Double(600000.0)}
            },
            new String [] {
                "ID", "Tên sản phẩm", "Đơn giá"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable2.setSelectionBackground(new java.awt.Color(0, 102, 102));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jTable1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jTable1.setForeground(new java.awt.Color(53, 66, 89));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Số lượng", "Đơn giá"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(53, 66, 89));
        jLabel1.setText("Tổng:");

        Too.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        Too.setForeground(new java.awt.Color(53, 66, 89));
        Too.setText("0");

        jButton13.setBackground(new java.awt.Color(0, 102, 102));
        jButton13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("THANH TOÁN");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Too, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(Too)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnAdd.setText("Thêm ");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setText("Sửa ");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnEdit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(367, 367, 367))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addComponent(btnAdd)
                        .addGap(18, 18, 18)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete)
                        .addGap(0, 287, Short.MAX_VALUE)))
                .addGap(55, 55, 55)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        int numrow = jTable1.getRowCount();
        int tot = 0;
        for (int i = 0; i < numrow; i++) {
            int val = Integer.parseInt(jTable1.getValueAt(i, 2).toString());
            tot += val;
        }
        customer.setPay(tot);
        Too.setText(Integer.toString(tot));

        FormVerifyPay pay = new FormVerifyPay(customer);
        System.out.println("get customer" + pay.customer.getPay());
        pay.setVisible(true);
        pay.setLocationRelativeTo(null);
        this.setVisible(false);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        TableModel model = jTable2.getModel();
        int indexs[] = jTable2.getSelectedRows();
//        Object[] row = new Object[3];
        DefaultTableModel model2 = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < indexs.length; i++) {

//            row[0] = model.getValueAt(indexs[i], 0);
//            row[2] = model.getValueAt(indexs[i], 2);
//            String name = String.valueOf(row[2]);
//            addTable(String.valueOf(row[0]), Double.parseDouble(name));
            String id = model.getValueAt(indexs[i], 0).toString(); // Get product ID
            double price = Double.parseDouble(model.getValueAt(indexs[i], 2).toString()); // Parse price directly
            addTable(id, price); // Call addTable directly with parsed values
        }
    }//GEN-LAST:event_jTable2MouseClicked
    public void addProductToTable(String id, String name, double price) {
        DefaultTableModel tableModel = (DefaultTableModel) jTable2.getModel();
        tableModel.addRow(new Object[]{id, name, price});
    }
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
//        String productName = JOptionPane.showInputDialog("Nhập tên sản phẩm:");
//        if (productName == null || productName.trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        String productPriceStr = JOptionPane.showInputDialog("Nhập đơn giá sản phẩm (VNĐ):");
//        if (productPriceStr == null || productPriceStr.trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Đơn giá không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        try {
//            double productPrice = Double.parseDouble(productPriceStr);
//            productCount++; // Tăng số lượng sản phẩm
//            String productId = String.format("#%03d", productCount);
//
//            DefaultTableModel tableModel = (DefaultTableModel) jTable2.getModel();
//            tableModel.addRow(new Object[]{productId, productName, productPrice});
//
//            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "Đơn giá phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//        }
        String productName = JOptionPane.showInputDialog("Nhập tên sản phẩm:");
        if (productName == null || productName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String productPriceStr = JOptionPane.showInputDialog("Nhập đơn giá sản phẩm (VNĐ):");
        if (productPriceStr == null || productPriceStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Đơn giá không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double productPrice = Double.parseDouble(productPriceStr);
            productCount++;
            String productId = String.format("#%03d", productCount);
            addProductToTable(productId, productName, productPrice);
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Đơn giá phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
         int selectedRow = jTable2.getSelectedRow(); // Lấy hàng được chọn
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.removeRow(selectedRow); // Xóa hàng khỏi bảng

        JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
       int selectedRow = jTable2.getSelectedRow(); // Lấy hàng được chọn
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Lấy dữ liệu hiện tại
    DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    String currentName = model.getValueAt(selectedRow, 1).toString();
    String currentPrice = model.getValueAt(selectedRow, 2).toString();

    // Hiển thị dialog để nhập dữ liệu mới
    String newName = JOptionPane.showInputDialog(this, "Nhập tên sản phẩm mới:", currentName);
    if (newName == null || newName.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String newPriceStr = JOptionPane.showInputDialog(this, "Nhập đơn giá mới:", currentPrice);
    try {
        double newPrice = Double.parseDouble(newPriceStr);

        // Cập nhật dữ liệu trong bảng
        model.setValueAt(newName, selectedRow, 1);
        model.setValueAt(newPrice, selectedRow, 2);

        JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Đơn giá phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnEditActionPerformed

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
            java.util.logging.Logger.getLogger(FormPay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormPay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormPay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormPay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormPay().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Too;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton jButton13;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private keeptoo.KGradientPanel kGradientPanel1;
    // End of variables declaration//GEN-END:variables
}
