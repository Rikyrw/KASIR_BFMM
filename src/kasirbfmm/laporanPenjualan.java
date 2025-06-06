/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kasirbfmm;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.awt.*;
import java.io.File;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
/**
 *
 * @author Dhimas Ananta
 */
public class laporanPenjualan extends javax.swing.JFrame {
    DefaultTableModel model = new DefaultTableModel();
    
    databasee db = new databasee();

    /**
     * Creates new form laporanPembelian
     */
    public laporanPenjualan() {
            this.setUndecorated(true);
        
    db.koneksiDB();
        initComponents();
        
            // Setup tabel dengan model non-editable
    model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Semua cell tidak bisa di-edit
        }
    };
        
                // Setup tabel
        aturTabel();
        // Load data default (hari ini)
        tampilkanData();
        
                model.addTableModelListener(e -> {
    hitungTotalBarang();
    });
                        model.addTableModelListener(e -> {
    hitungTotalHarga();
});
                        
                        

    // Event listener untuk daritanggal
    daritanggal.addPropertyChangeListener("date", new java.beans.PropertyChangeListener() {
        @Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (daritanggal.getDate() != null && sampai_tanggal.getDate() != null) {
                tampilkanDataByTanggal();
            }
        }
    });
    
    // Event listener untuk sampai_tanggal
    sampai_tanggal.addPropertyChangeListener("date", new java.beans.PropertyChangeListener() {
        @Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (daritanggal.getDate() != null && sampai_tanggal.getDate() != null) {
                tampilkanDataByTanggal();
            }
        }
    });

         
    
    }

    
    
    
    
        private void aturTabel() {
        model.addColumn("No transaksi");
        model.addColumn("Kode barang");
        model.addColumn("Nama barang");
        model.addColumn("Tanggal");
        model.addColumn("Jumlah terjual");
        model.addColumn("Harga satuan");
        model.addColumn("Total harga");
    }
        
         public void tampilkanData() {
    try {
        // Query dengan JOIN untuk mengambil tanggal dan qty dari tb_jual
        String query = "SELECT dt.no_transaksi, dt.kode_barang, dt.nama_barang, " +
                      "tj.tanggal, dt.jumlah_barang, dt.harga_barang, dt.total " +
                      "FROM detail_transaksijual dt " +
                      "INNER JOIN tb_jual tj ON dt.no_transaksi = tj.no_transaksi " +
                      "ORDER BY dt.no_transaksi";
        
        ResultSet rs = db.ambildata(query);
        while (rs.next()) {
            model.addRow(new Object[]{ 
                rs.getString("no_transaksi"),
                rs.getString("kode_barang"),    
                rs.getString("nama_barang"),
                rs.getString("tanggal"),        // Dari tb_jual
                rs.getString("jumlah_barang"),   
                rs.getString("harga_barang"),
                rs.getString("total")          
            });
        jTable1.setModel(model);
            }
            hitungTotalBarang();
            hitungTotalHarga();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menampilkan data: " + e.getMessage());
        }
    }

          private void hitungTotalBarang() {
    int totalBarang = 0;
    
    // Loop melalui semua baris di tabel
    for (int i = 0; i < model.getRowCount(); i++) {
        // Ambil nilai dari kolom "JUMLAH TERJUAL" (kolom ke-4 dalam model tabel)
        totalBarang += Integer.parseInt(model.getValueAt(i, 4).toString());
    }
    
        // Tampilkan hasil total ke jtotal_1
        totalBarang1.setText(String.valueOf(totalBarang));
    }
              private void hitungTotalHarga() {
        int totalHarga = 0;

        // Loop melalui semua baris di tabel
        for (int i = 0; i < model.getRowCount(); i++) {
            // Ambil nilai dari kolom "TOTAL HARGA" (kolom ke-6 dalam model tabel)
            totalHarga += Integer.parseInt(model.getValueAt(i, 6).toString());
        }

        // Tampilkan hasil total ke jtotal_1
        totalPemasukan.setText(String.valueOf(totalHarga));
    }
              
              
              
              
              public void tampilkanDataByTanggal() {
    try {
        // Validasi apakah kedua tanggal sudah dipilih
        if (daritanggal.getDate() == null || sampai_tanggal.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Pilih kedua tanggal terlebih dahulu!");
            return;
        }
        
        // Validasi rentang tanggal
        if (daritanggal.getDate().after(sampai_tanggal.getDate())) {
            JOptionPane.showMessageDialog(this, "Tanggal awal tidak boleh lebih besar dari tanggal akhir!");
            return;
        }
        
        // Bersihkan tabel terlebih dahulu
        model.setRowCount(0);
        
        // Format tanggal untuk query SQL
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String tanggalAwal = sdf.format(daritanggal.getDate());
        String tanggalAkhir = sdf.format(sampai_tanggal.getDate());
        
        // Query dengan filter tanggal
        String query = "SELECT dt.no_transaksi, dt.kode_barang, dt.nama_barang, " +
                      "tj.tanggal, dt.jumlah_barang, dt.harga_barang, dt.total " +
                      "FROM detail_transaksijual dt " +
                      "INNER JOIN tb_jual tj ON dt.no_transaksi = tj.no_transaksi " +
                      "WHERE tj.tanggal BETWEEN '" + tanggalAwal + "' AND '" + tanggalAkhir + "' " +
                      "ORDER BY tj.tanggal DESC, dt.no_transaksi";
        
        ResultSet rs = db.ambildata(query);
        int totalRows = 0;
        
        while (rs.next()) {
            model.addRow(new Object[]{ 
                rs.getString("no_transaksi"),
                rs.getString("kode_barang"),    
                rs.getString("nama_barang"),
                rs.getString("tanggal"),
                rs.getString("jumlah_barang"),
                rs.getString("harga_barang"),
                rs.getString("total")          
            });
            totalRows++;
        }
        
        jTable1.setModel(model);
        hitungTotalBarang();
        hitungTotalHarga();
        
        // Tampilkan info hasil pencarian
        if (totalRows == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data penjualan pada rentang tanggal tersebut.");
        } else {
            JOptionPane.showMessageDialog(this, "Ditemukan " + totalRows + " transaksi penjualan\n" +
                                        "Periode: " + tanggalAwal + " s/d " + tanggalAkhir);
        }
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mencari data: " + e.getMessage());
        e.printStackTrace();
    }
}
              
              
              
              // Method untuk reset dan tampilkan semua data kembali
public void resetFilter() {
    // Bersihkan model tabel terlebih dahulu
    model.setRowCount(0);
    
    // Reset tanggal
    daritanggal.setDate(null);
    sampai_tanggal.setDate(null);
    
    // Tampilkan data dengan model yang sudah dibersihkan
    tampilkanData();
}



public void tampilkanDataHariIni() {
    try {
        model.setRowCount(0);
        
        // Tanggal hari ini
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String tanggalHariIni = sdf.format(new java.util.Date());
        
        String query = "SELECT dt.no_transaksi, dt.kode_barang, dt.nama_barang, " +
                      "tj.tanggal, tj.qty, dt.harga_barang, dt.total " +
                      "FROM detail_transaksijual dt " +
                      "INNER JOIN tb_jual tj ON dt.no_transaksi = tj.no_transaksi " +
                      "WHERE tj.tanggal = '" + tanggalHariIni + "' " +
                      "ORDER BY dt.no_transaksi";
        
        ResultSet rs = db.ambildata(query);
        while (rs.next()) {
            model.addRow(new Object[]{ 
                rs.getString("no_transaksi"),
                rs.getString("kode_barang"),    
                rs.getString("nama_barang"),
                rs.getString("tanggal"),
                rs.getString("qty"),
                rs.getString("harga_barang"),
                rs.getString("total")          
            });
        }
        
        jTable1.setModel(model);
        hitungTotalBarang();
        hitungTotalHarga();
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
    }
}

private void exportToExcel() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Simpan sebagai Excel");
    fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
    fileChooser.setSelectedFile(new File("Laporan_Penjualan.xlsx"));
    
    int userSelection = fileChooser.showSaveDialog(this);
    
    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();
        
        if (!filePath.endsWith(".xlsx")) {
            filePath += ".xlsx";
        }
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Laporan Penjualan");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < jTable1.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(jTable1.getColumnName(i));
            }
            
            // Create data rows
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < jTable1.getColumnCount(); j++) {
                    Cell cell = row.createCell(j);
                    Object value = jTable1.getValueAt(i, j);
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                }
            }
            
            // Auto size columns
            for (int i = 0; i < jTable1.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
                JOptionPane.showMessageDialog(this, "Data berhasil diexport ke Excel!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal export ke Excel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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

        laba = new javax.swing.JButton();
        dasbor1 = new javax.swing.JButton();
        stokOpname = new javax.swing.JButton();
        barang1 = new javax.swing.JButton();
        jual1 = new javax.swing.JButton();
        user1 = new javax.swing.JButton();
        logout1 = new javax.swing.JButton();
        ekspor1 = new javax.swing.JButton();
        pembelian = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        retur2 = new javax.swing.JButton();
        totalPemasukan = new javax.swing.JTextField();
        totalBarang1 = new javax.swing.JTextField();
        daritanggal = new com.toedter.calendar.JDateChooser();
        sampai_tanggal = new com.toedter.calendar.JDateChooser();
        jreset = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        laba.setBorderPainted(false);
        laba.setContentAreaFilled(false);
        laba.setFocusPainted(false);
        laba.setFocusable(false);
        laba.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labaActionPerformed(evt);
            }
        });
        getContentPane().add(laba, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 110, 120, 30));

        dasbor1.setBorderPainted(false);
        dasbor1.setContentAreaFilled(false);
        dasbor1.setFocusPainted(false);
        dasbor1.setFocusable(false);
        dasbor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dasbor1ActionPerformed(evt);
            }
        });
        getContentPane().add(dasbor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 100, 20));

        stokOpname.setBorderPainted(false);
        stokOpname.setContentAreaFilled(false);
        stokOpname.setFocusPainted(false);
        stokOpname.setFocusable(false);
        stokOpname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stokOpnameActionPerformed(evt);
            }
        });
        getContentPane().add(stokOpname, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, 100, 20));

        barang1.setBorderPainted(false);
        barang1.setContentAreaFilled(false);
        barang1.setFocusPainted(false);
        barang1.setFocusable(false);
        barang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barang1ActionPerformed(evt);
            }
        });
        getContentPane().add(barang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 100, 20));

        jual1.setBorderPainted(false);
        jual1.setContentAreaFilled(false);
        jual1.setFocusPainted(false);
        jual1.setFocusable(false);
        jual1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jual1ActionPerformed(evt);
            }
        });
        getContentPane().add(jual1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 100, 20));

        user1.setBorderPainted(false);
        user1.setContentAreaFilled(false);
        user1.setFocusPainted(false);
        user1.setFocusable(false);
        user1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user1ActionPerformed(evt);
            }
        });
        getContentPane().add(user1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 100, 20));

        logout1.setBorderPainted(false);
        logout1.setContentAreaFilled(false);
        logout1.setFocusPainted(false);
        logout1.setFocusable(false);
        logout1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout1ActionPerformed(evt);
            }
        });
        getContentPane().add(logout1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 690, 100, 30));

        ekspor1.setBorderPainted(false);
        ekspor1.setContentAreaFilled(false);
        ekspor1.setFocusPainted(false);
        ekspor1.setFocusable(false);
        ekspor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ekspor1ActionPerformed(evt);
            }
        });
        getContentPane().add(ekspor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 680, 120, 40));

        pembelian.setBorderPainted(false);
        pembelian.setContentAreaFilled(false);
        pembelian.setFocusPainted(false);
        pembelian.setFocusable(false);
        pembelian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pembelianActionPerformed(evt);
            }
        });
        getContentPane().add(pembelian, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 110, 120, 30));

        jTable1.setBackground(new java.awt.Color(102, 102, 102));
        jTable1.setForeground(new java.awt.Color(204, 204, 204));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No transaksi", "Kode barang", "Nama barang", "Tanggal", "Harga satuan", "Total harga", "Jumlah terjual"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(263, 162, 1000, 450));

        retur2.setBorderPainted(false);
        retur2.setContentAreaFilled(false);
        retur2.setFocusPainted(false);
        retur2.setFocusable(false);
        retur2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retur2ActionPerformed(evt);
            }
        });
        getContentPane().add(retur2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 100, 20));

        totalPemasukan.setEditable(false);
        totalPemasukan.setBorder(null);
        totalPemasukan.setRequestFocusEnabled(false);
        getContentPane().add(totalPemasukan, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 690, 100, 20));

        totalBarang1.setEditable(false);
        totalBarang1.setBorder(null);
        totalBarang1.setRequestFocusEnabled(false);
        getContentPane().add(totalBarang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 690, 110, 20));
        getContentPane().add(daritanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 110, 140, -1));
        getContentPane().add(sampai_tanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 110, 130, -1));

        jreset.setBorder(null);
        jreset.setBorderPainted(false);
        jreset.setContentAreaFilled(false);
        jreset.setFocusPainted(false);
        jreset.setRequestFocusEnabled(false);
        jreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jresetActionPerformed(evt);
            }
        });
        getContentPane().add(jreset, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 100, 120, 40));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/penjualannn (1).png"))); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void labaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labaActionPerformed
        laporanLaba laporanLaba = new laporanLaba();
        laporanLaba.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_labaActionPerformed

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

    private void stokOpnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stokOpnameActionPerformed
        stokOpname stokOpname = new stokOpname();
        stokOpname.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_stokOpnameActionPerformed

    private void jual1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jual1ActionPerformed
        jual jual = new jual();
        jual.dispose();
        this.dispose();
    }//GEN-LAST:event_jual1ActionPerformed

    private void user1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_user1ActionPerformed
        user user = new user();
        user.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_user1ActionPerformed

    private void logout1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout1ActionPerformed
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
    }//GEN-LAST:event_logout1ActionPerformed

    private void ekspor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ekspor1ActionPerformed
            // Buat dialog pilihan export
    Object[] options = {"Export ke Excel",  "Batal"};
    int choice = JOptionPane.showOptionDialog(
        this,
        "Pilih format export:",
        "Export Data",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[0]
    );
    
    if (choice == 0) {
        exportToExcel();
//    } else if (choice == 1) {
//        exportToPDF();
//    }
    }
    }//GEN-LAST:event_ekspor1ActionPerformed

    private void pembelianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pembelianActionPerformed
        laporanPembelian laporanPembelian = new laporanPembelian();
        laporanPembelian.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_pembelianActionPerformed

    private void retur2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retur2ActionPerformed
        retur retur = new retur();
        retur.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_retur2ActionPerformed

    private void jresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jresetActionPerformed
// Panggil method resetFilter yang sudah ada
        resetFilter();
    }//GEN-LAST:event_jresetActionPerformed

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
            java.util.logging.Logger.getLogger(laporanPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(laporanPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(laporanPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(laporanPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new laporanPenjualan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton barang1;
    private com.toedter.calendar.JDateChooser daritanggal;
    private javax.swing.JButton dasbor1;
    private javax.swing.JButton ekspor1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton jreset;
    private javax.swing.JButton jual1;
    private javax.swing.JButton laba;
    private javax.swing.JButton logout1;
    private javax.swing.JButton pembelian;
    private javax.swing.JButton retur2;
    private com.toedter.calendar.JDateChooser sampai_tanggal;
    private javax.swing.JButton stokOpname;
    private javax.swing.JTextField totalBarang1;
    private javax.swing.JTextField totalPemasukan;
    private javax.swing.JButton user1;
    // End of variables declaration//GEN-END:variables
}
