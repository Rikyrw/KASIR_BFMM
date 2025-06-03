/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kasirbfmm;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Dhimas Ananta
 */
public class user extends javax.swing.JFrame {

    private databasee db;
    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;
    /**
     * Creates new form user
     */
    public user() {
        this.setUndecorated(true);
        initComponents();
        db = new databasee();
        conn = db.koneksiDB();
        loadUserData();
    }
    
     private void loadUserData() {
        try {
            String sql = "SELECT * FROM tb_user";
            rs = db.ambildata(sql);
            
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0); // Clear existing data
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_user"));
                row.add(rs.getString("nama"));
                row.add(rs.getString("username"));
                row.add(rs.getString("password"));
                row.add(rs.getString("rfid"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading user data: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
     
        private void clearFields() {
            idUserbaeu1.setText("");
            nama1.setText("");
            username1.setText("");
            kataSandi1.setText("");
            rfid.setText("");
        }
      
private void addUser() {
    try {
        String sql = "INSERT INTO tb_user (nama, username, password, rfid) VALUES (?, ?, ?, ?)";
        pst = conn.prepareStatement(sql);
        pst.setString(1, nama1.getText());
        pst.setString(2, username1.getText());
        pst.setString(3, kataSandi1.getText());
        pst.setString(4, rfid.getText());
        
        int rowsAffected = pst.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "User added successfully!");
            clearFields();
            loadUserData();
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error adding user: " + e.getMessage());
    } finally {
        try {
            if (pst != null) pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
      
private void updateUser() {
    try {
        // Validasi ID user harus numerik
        if (!idUserbaeu1.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "ID User harus angka");
            return;
        }
        
        String sql = "UPDATE tb_user SET nama=?, username=?, password=?, rfid=? WHERE id_user=?";
        pst = conn.prepareStatement(sql);
        pst.setString(1, nama1.getText());
        pst.setString(2, username1.getText());
        pst.setString(3, kataSandi1.getText());
        pst.setString(4, rfid.getText());
        pst.setInt(5, Integer.parseInt(idUserbaeu1.getText()));
        
        int rowsAffected = pst.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "User berhasil diupdate!");
            clearFields();
            loadUserData();
        } else {
            JOptionPane.showMessageDialog(null, "User dengan ID tersebut tidak ditemukan");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    } finally {
        try {
            if (pst != null) pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
        
        private void deleteUser() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a user to delete");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            int userId = (int) jTable1.getValueAt(selectedRow, 0);
            String sql = "DELETE FROM tb_user WHERE id_user=" + userId;
            
            if (db.aksi(sql)) {
                JOptionPane.showMessageDialog(null, "User deleted successfully!");
                loadUserData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error deleting user: " + e.getMessage());
        }
    }
        
        
    
        private void tambahUserBaru() {
        try {
            
        // Validasi field
        if(nama1.getText().isEmpty() || username1.getText().isEmpty() || 
           kataSandi1.getText().isEmpty() || rfid.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua field harus diisi!");
            return;
        }

        // Dapatkan ID berikutnya secara manual
        String getMaxId = "SELECT MAX(id_user) FROM tb_user";
        rs = conn.createStatement().executeQuery(getMaxId);
        int nextId = rs.next() ? rs.getInt(1) + 1 : 1; // Jika tabel kosong, mulai dari 1

        // Insert dengan ID eksplisit
        String sql = "INSERT INTO tb_user (id_user, nama, username, password, rfid) VALUES (?, ?, ?, ?, ?)";
        pst = conn.prepareStatement(sql);
        pst.setInt(1, nextId);
        pst.setString(2, nama1.getText());
        pst.setString(3, username1.getText());
        pst.setString(4, kataSandi1.getText());
        pst.setString(5, rfid.getText());
        
        pst.executeUpdate();
        
        JOptionPane.showMessageDialog(null, "User baru berhasil ditambahkan dengan ID: " + nextId);
        clearFields();
        loadUserData();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
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

        dasbor1 = new javax.swing.JButton();
        barang1 = new javax.swing.JButton();
        jual = new javax.swing.JButton();
        stokOp = new javax.swing.JButton();
        laporan = new javax.swing.JButton();
        hapus = new javax.swing.JButton();
        rfid = new javax.swing.JTextField();
        idUserbaeu1 = new javax.swing.JTextField();
        username1 = new javax.swing.JTextField();
        nama1 = new javax.swing.JTextField();
        logout2 = new javax.swing.JButton();
        simpan2 = new javax.swing.JButton();
        retur1 = new javax.swing.JButton();
        kataSandi1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        retur2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dasbor1.setBorderPainted(false);
        dasbor1.setContentAreaFilled(false);
        dasbor1.setFocusPainted(false);
        dasbor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dasbor1ActionPerformed(evt);
            }
        });
        getContentPane().add(dasbor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 100, 20));

        barang1.setBorderPainted(false);
        barang1.setContentAreaFilled(false);
        barang1.setFocusPainted(false);
        barang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barang1ActionPerformed(evt);
            }
        });
        getContentPane().add(barang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 100, 20));

        jual.setBorderPainted(false);
        jual.setContentAreaFilled(false);
        jual.setFocusPainted(false);
        jual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jualActionPerformed(evt);
            }
        });
        getContentPane().add(jual, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 80, 20));

        stokOp.setBorderPainted(false);
        stokOp.setContentAreaFilled(false);
        stokOp.setFocusPainted(false);
        stokOp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stokOpActionPerformed(evt);
            }
        });
        getContentPane().add(stokOp, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, 110, 20));

        laporan.setBorderPainted(false);
        laporan.setContentAreaFilled(false);
        laporan.setFocusPainted(false);
        laporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laporanActionPerformed(evt);
            }
        });
        getContentPane().add(laporan, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 120, 20));

        hapus.setBorderPainted(false);
        hapus.setContentAreaFilled(false);
        hapus.setFocusPainted(false);
        hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusActionPerformed(evt);
            }
        });
        getContentPane().add(hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 260, 100, 30));

        rfid.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        rfid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rfidActionPerformed(evt);
            }
        });
        getContentPane().add(rfid, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 200, 190, 30));

        idUserbaeu1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        idUserbaeu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idUserbaeu1ActionPerformed(evt);
            }
        });
        getContentPane().add(idUserbaeu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, 430, 40));

        username1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        username1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                username1ActionPerformed(evt);
            }
        });
        getContentPane().add(username1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 200, 440, 30));

        nama1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        nama1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nama1ActionPerformed(evt);
            }
        });
        getContentPane().add(nama1, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 100, 450, 40));

        logout2.setBorderPainted(false);
        logout2.setContentAreaFilled(false);
        logout2.setFocusPainted(false);
        logout2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout2ActionPerformed(evt);
            }
        });
        getContentPane().add(logout2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 690, 100, 30));

        simpan2.setBorderPainted(false);
        simpan2.setContentAreaFilled(false);
        simpan2.setFocusPainted(false);
        simpan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpan2ActionPerformed(evt);
            }
        });
        getContentPane().add(simpan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 260, 100, 30));

        retur1.setBorderPainted(false);
        retur1.setContentAreaFilled(false);
        retur1.setFocusPainted(false);
        retur1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retur1ActionPerformed(evt);
            }
        });
        getContentPane().add(retur1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 100, 20));

        kataSandi1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        kataSandi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kataSandi1ActionPerformed(evt);
            }
        });
        getContentPane().add(kataSandi1, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 200, 200, 30));

        jTable1.setBackground(new java.awt.Color(102, 102, 102));
        jTable1.setForeground(new java.awt.Color(204, 204, 204));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id user", "Nama user", "Username", "Kata Sandi", "Rfid"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 320, 1010, 380));

        retur2.setBorderPainted(false);
        retur2.setContentAreaFilled(false);
        retur2.setFocusPainted(false);
        getContentPane().add(retur2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 90, 20));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/user.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dasbor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dasbor1ActionPerformed
        dashboard dashboard = new dashboard();
        dashboard.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_dasbor1ActionPerformed

    private void barang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barang1ActionPerformed
        barang barang = new barang();
        barang.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_barang1ActionPerformed

    private void retur1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retur1ActionPerformed
        retur retur = new retur();
        retur.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_retur1ActionPerformed

    private void stokOpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stokOpActionPerformed
        stokOpname stokOpname = new stokOpname();
        stokOpname.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_stokOpActionPerformed

    private void jualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jualActionPerformed
        jual jual = new jual();
        jual.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jualActionPerformed

    private void laporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_laporanActionPerformed
        laporanPembelian laporanPembelian = new laporanPembelian();
        laporanPembelian.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_laporanActionPerformed

    private void logout2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout2ActionPerformed
    // Tampilkan dialog konfirmasi
    int response = JOptionPane.showConfirmDialog(
        this, 
        "Apakah Anda yakin ingin logout?", 
        "Konfirmasi Logout", 
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );
    
    // Jika user memilih YES (0), lakukan logout
    if (response == JOptionPane.YES_OPTION) {
        login dashboard = new login();
        dashboard.setVisible(true);
        this.dispose();
    }
    }//GEN-LAST:event_logout2ActionPerformed

    private void kataSandi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kataSandi1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kataSandi1ActionPerformed

    private void idUserbaeu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idUserbaeu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idUserbaeu1ActionPerformed

    private void nama1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nama1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nama1ActionPerformed

    private void username1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_username1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_username1ActionPerformed

    private void rfidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rfidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rfidActionPerformed

    private void simpan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpan2ActionPerformed
        // Validasi field kosong
    if (nama1.getText().isEmpty() || username1.getText().isEmpty() || 
        kataSandi1.getText().isEmpty() || rfid.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Semua field harus diisi!");
        return;
    }

    // Tampilkan dialog konfirmasi
    int confirmation = JOptionPane.showConfirmDialog(
        null, 
        "Apakah Anda yakin ingin menyimpan data user ini?\n\n" +
        "Nama: " + nama1.getText() + "\n" +
        "Username: " + username1.getText(),
        "Konfirmasi Simpan",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );

    // Jika user memilih YES
    if (confirmation == JOptionPane.YES_OPTION) {
        if (idUserbaeu1.getText().isEmpty()) {
            tambahUserBaru(); // Panggil method tambah user baru
        } else {
            updateUser(); // Panggil method update user
        }
    } else {
        JOptionPane.showMessageDialog(null, "Penyimpanan dibatalkan");
    }
        
//        tambahUserBaru();
    
    }//GEN-LAST:event_simpan2ActionPerformed

    private void hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusActionPerformed
         deleteUser();
    }//GEN-LAST:event_hapusActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
    // Kosongkan ID saat klik tabel agar selalu mode tambah user baru
    idUserbaeu1.setText("");
    
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow >= 0) {
        nama1.setText(jTable1.getValueAt(selectedRow, 1).toString());
        username1.setText(jTable1.getValueAt(selectedRow, 2).toString());
        kataSandi1.setText(jTable1.getValueAt(selectedRow, 3).toString());
        rfid.setText(jTable1.getValueAt(selectedRow, 4).toString());
    }
    }//GEN-LAST:event_jTable1MouseClicked

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
            java.util.logging.Logger.getLogger(user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new user().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton barang1;
    private javax.swing.JButton dasbor1;
    private javax.swing.JButton hapus;
    private javax.swing.JTextField idUserbaeu1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton jual;
    private javax.swing.JTextField kataSandi1;
    private javax.swing.JButton laporan;
    private javax.swing.JButton logout2;
    private javax.swing.JTextField nama1;
    private javax.swing.JButton retur1;
    private javax.swing.JButton retur2;
    private javax.swing.JTextField rfid;
    private javax.swing.JButton simpan2;
    private javax.swing.JButton stokOp;
    private javax.swing.JTextField username1;
    // End of variables declaration//GEN-END:variables
}
