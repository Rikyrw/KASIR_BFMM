/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kasirbfmm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.SwingUtilities;

/**
 *
 * @author user
 */
public class dashboard extends javax.swing.JFrame {
    private String userName;
   private Connection conn;
    /**
     * Creates new form dashboard
     */
    
    public dashboard() {
        this("Guest"); // Kalau dipanggil tanpa parameter, default "Guest"
    }
    //dam dim dim dum
    public dashboard(String userName) {
        this.setUndecorated(true);
        initComponents();
          this.userName = (userName != null && !userName.isEmpty()) ? userName : "Guest"; 
        nama.setText(this.userName); // Set nama user di JTextField

        // Styling JTextField (readonly, transparan)
        nama.setEditable(false); 
        nama.setOpaque(false); 
        nama.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        nama.setFocusable(false); // Biar nggak bisa di-klik
        
        
        
        // Styling tombol logout transparan
        jlogout.setOpaque(false);
        jlogout.setContentAreaFilled(false);
        jlogout.setBorderPainted(false);
        nama.setText(this.userName); // Set nama user di JTextField

        // Styling JTextField (readonly, transparan)
        nama.setEditable(false); 
        nama.setOpaque(false); 
        nama.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        // Styling tombol logout transparan
        jlogout.setOpaque(false);
        jlogout.setContentAreaFilled(false);
        jlogout.setBorderPainted(false);

        updateDashboardData();
    }
    
   private void initializeDatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/bfm_kasir1111"; // Updated database name
            String username = "root"; // Change as needed
            String password = ""; // Change as needed
            
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Failed to connect to database: " + e.getMessage(),
                "Database Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateDashboardData() {
        // Calculate today's loss
        double totalRugi = calculateTotalRugi();
        rugihariini1.setText("Rp " + String.format("%,.2f", totalRugi));
        
        // Calculate today's profit
        double totalUntung = calculateTotalUntung();
        untunghariini1.setText("Rp " + String.format("%,.2f", totalUntung));
        
        // Count near-expired items
        int jumlahHampirExpired = countHampirKadaluwarsa();
        hampirkadaluwarsa.setText(String.valueOf(jumlahHampirExpired));
    }

    private double calculateTotalRugi() {
    double totalRugi = 0;
    try {
        if (conn == null || conn.isClosed()) {
            initializeDatabaseConnection();
        }
        
        // 1. Calculate from supplier returns
        String queryRetur = "SELECT SUM(b.harga_beli * rs.qty) as total_rugi " +
                           "FROM tb_retur_supplier rs " +
                           "JOIN tb_barang b ON rs.kode_barang = b.kode_barang " +
                           "WHERE DATE(rs.tanggal) = CURDATE()";
        
        // 2. Calculate from negative price items in log
        String queryLog = "SELECT SUM(harga_jual) as total_rugi_log " +
                         "FROM log_barang " +
                         "WHERE harga_jual < 0 AND DATE(deleted_at) = CURDATE()";
        
        // 3. Calculate from stock opname losses (where physical stock < system stock)
        String queryOpname = "SELECT SUM(b.harga_beli * ABS(so.selisih)) as total_rugi_opname " +
                           "FROM tb_stok_opname so " +
                           "JOIN tb_barang b ON so.kode_barang = b.kode_barang " +
                           "WHERE so.selisih < 0 AND DATE(so.tanggal) = CURDATE()";
        
        Statement stmt = conn.createStatement();
        
        // Execute first query (retur supplier)
        ResultSet rs = stmt.executeQuery(queryRetur);
        if (rs.next()) {
            totalRugi += rs.getDouble("total_rugi");
        }
        
        // Execute second query (negative price items)
        rs = stmt.executeQuery(queryLog);
        if (rs.next()) {
            totalRugi += Math.abs(rs.getDouble("total_rugi_log"));
        }
        
        // Execute third query (stock opname losses)
        rs = stmt.executeQuery(queryOpname);
        if (rs.next()) {
            totalRugi += rs.getDouble("total_rugi_opname");
        }
        
    } catch (Exception e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, 
            "Error calculating loss: " + e.getMessage(),
            "Calculation Error",
            javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    return totalRugi;
}

    private double calculateTotalUntung() {
        double totalUntung = 0;
        try {
            if (conn == null || conn.isClosed()) {
                initializeDatabaseConnection();
            }
            
            // Calculate from today's sales
            String query = "SELECT SUM((dj.harga_barang - dj.harga_beli) * dj.jumlah_barang) as total_untung " +
                          "FROM detail_transaksijual dj " +
                          "JOIN tb_jual tj ON dj.no_transaksi = tj.no_transaksi " +
                          "WHERE DATE(tj.tanggal) = CURDATE()";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                totalUntung = rs.getDouble("total_untung");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error calculating profit: " + e.getMessage(),
                "Calculation Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return totalUntung;
    }

    private int countHampirKadaluwarsa() {
        int count = 0;
        try {
            if (conn == null || conn.isClosed()) {
                initializeDatabaseConnection();
            }
            
            // Count items expiring within 7 days
            String query = "SELECT COUNT(*) as jumlah " +
                          "FROM tb_barang " +
                          "WHERE exp BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                count = rs.getInt("jumlah");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error counting near-expired items: " + e.getMessage(),
                "Calculation Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return count;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tombolStokop = new javax.swing.JButton();
        nama = new javax.swing.JTextField();
        jlogout = new javax.swing.JButton();
        tombolBarang1 = new javax.swing.JButton();
        tombolUser = new javax.swing.JButton();
        tombolLaporan = new javax.swing.JButton();
        tombolRetur1 = new javax.swing.JButton();
        tombolJual1 = new javax.swing.JButton();
        hampirkadaluwarsa = new javax.swing.JTextField();
        rugihariini1 = new javax.swing.JTextField();
        untunghariini1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tombolStokop.setBorderPainted(false);
        tombolStokop.setContentAreaFilled(false);
        tombolStokop.setFocusPainted(false);
        tombolStokop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolStokopActionPerformed(evt);
            }
        });
        getContentPane().add(tombolStokop, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, 100, 20));

        nama.setEditable(false);
        nama.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        nama.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nama.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaActionPerformed(evt);
            }
        });
        getContentPane().add(nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 40, 130, 20));

        jlogout.setBackground(new java.awt.Color(51, 51, 51));
        jlogout.setBorder(null);
        jlogout.setBorderPainted(false);
        jlogout.setContentAreaFilled(false);
        jlogout.setFocusPainted(false);
        jlogout.setFocusable(false);
        jlogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jlogoutActionPerformed(evt);
            }
        });
        getContentPane().add(jlogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 690, 110, 20));

        tombolBarang1.setBorderPainted(false);
        tombolBarang1.setContentAreaFilled(false);
        tombolBarang1.setFocusPainted(false);
        tombolBarang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolBarang1ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolBarang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 120, 20));

        tombolUser.setBorderPainted(false);
        tombolUser.setContentAreaFilled(false);
        tombolUser.setFocusPainted(false);
        tombolUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolUserActionPerformed(evt);
            }
        });
        getContentPane().add(tombolUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 90, 20));

        tombolLaporan.setBorderPainted(false);
        tombolLaporan.setContentAreaFilled(false);
        tombolLaporan.setFocusPainted(false);
        tombolLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolLaporanActionPerformed(evt);
            }
        });
        getContentPane().add(tombolLaporan, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 120, 20));

        tombolRetur1.setBorderPainted(false);
        tombolRetur1.setContentAreaFilled(false);
        tombolRetur1.setFocusPainted(false);
        tombolRetur1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolRetur1ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolRetur1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 100, 20));

        tombolJual1.setBorderPainted(false);
        tombolJual1.setContentAreaFilled(false);
        tombolJual1.setFocusPainted(false);
        tombolJual1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolJual1ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolJual1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 100, 20));
        getContentPane().add(hampirkadaluwarsa, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 340, 250, 80));
        getContentPane().add(rugihariini1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 340, 250, 80));
        getContentPane().add(untunghariini1, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 340, 250, 80));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/desktop.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    // Tambahkan method ini di dalam class dashboard (sebelum initComponents())
 
    
    private void jlogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jlogoutActionPerformed
        login dashboard = new login();
        dashboard.setVisible(true);
        System.out.println("github perubahan");
        this.dispose();
    }//GEN-LAST:event_jlogoutActionPerformed

    private void tombolBarang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolBarang1ActionPerformed
        barang barang = new barang();
        barang.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tombolBarang1ActionPerformed

    private void tombolStokopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolStokopActionPerformed
        stokOpname stokOpname = new stokOpname ();
        stokOpname.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tombolStokopActionPerformed

    private void tombolUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolUserActionPerformed
        user user = new user();
        user.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tombolUserActionPerformed

    private void tombolLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolLaporanActionPerformed
        laporanPembelian laporanPembelian = new laporanPembelian();
        laporanPembelian.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tombolLaporanActionPerformed

    private void namaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaActionPerformed

    private void tombolJual1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolJual1ActionPerformed
        jual jual = new jual();
        jual.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tombolJual1ActionPerformed

    private void tombolRetur1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolRetur1ActionPerformed
        retur retur = new retur();
        retur.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tombolRetur1ActionPerformed

    
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
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dashboard("Guest").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField hampirkadaluwarsa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton jlogout;
    private javax.swing.JTextField nama;
    private javax.swing.JTextField rugihariini1;
    private javax.swing.JButton tombolBarang1;
    private javax.swing.JButton tombolJual1;
    private javax.swing.JButton tombolLaporan;
    private javax.swing.JButton tombolRetur1;
    private javax.swing.JButton tombolStokop;
    private javax.swing.JButton tombolUser;
    private javax.swing.JTextField untunghariini1;
    // End of variables declaration//GEN-END:variables
}
