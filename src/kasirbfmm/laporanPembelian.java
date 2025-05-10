/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kasirbfmm;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

// Library untuk ekspor PDF
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 *
 * @author Dhimas Ananta
 */
public class laporanPembelian extends javax.swing.JFrame {
    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    
    // Format untuk tanggal
    private SimpleDateFormat formatTanggal = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatTampil = new SimpleDateFormat("dd-MM-yyyy");
    
    // Format untuk angka/uang
    private DecimalFormat formatUang = new DecimalFormat("###,###,###");
    /**
     * Creates new form laporanPembelian
     */
    public laporanPembelian() {
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        
        // Koneksi ke database
        koneksiDatabase();
        
        // Setup awal
        setupAwal();
    }

     private void koneksiDatabase() {
        try {
            // Ganti dengan kredensial database Anda
            String url = "jdbc:mysql://localhost:3306/sedod_kasir";
            String user = "root";
            String password = "";
            
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error koneksi database: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(laporanPembelian.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
     private void setupAwal() {
        // Setup untuk combo box kategori
        loadKategori();
        
        // Set tanggal default (hari ini untuk kedua field)
        String tanggalHariIni = formatTanggal.format(new Date());
        jDateChooser1.setDate(new Date());
jDateChooser2.setDate(new Date());

        
        // Tampilkan data awal
        tampilkanDataPembelian();
    }
     
     private void loadKategori() {
        try {
            // Reset combo box
            jComboBox1.removeAllItems();
            
            // Tambahkan item "Semua Kategori" sebagai default
            jComboBox1.addItem("Semua Kategori");
            
            // Query untuk mengambil semua kategori unik dari tabel barang
            String sql = "SELECT DISTINCT kategori FROM tb_barang ORDER BY kategori";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            
            // Tambahkan semua kategori ke combo box
            while (rs.next()) {
                jComboBox1.addItem(rs.getString("kategori"));
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading kategori: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(laporanPembelian.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            tutupResource();
        }
    }
     
     private void tampilkanDataPembelian() {
        try {
        String kategori = jComboBox1.getSelectedItem().toString();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        StringBuilder sqlBuilder = new StringBuilder(
            "SELECT b.kode_barang, b.nama_pemasok, b.varian, b.berat, b.stok, b.exp, " +
            "b.harga_jual, b.harga_beli, b.kategori, b.Tanggal, db.qty " +
            "FROM tb_barang b " +
            "JOIN detail_beli db ON b.kode_barang = db.kode_barang"
        );

        if (!kategori.equals("Semua Kategori")) {
            sqlBuilder.append(" WHERE b.kategori = ?");
        }

        pst = conn.prepareStatement(sqlBuilder.toString());

        if (!kategori.equals("Semua Kategori")) {
            pst.setString(1, kategori);
        }

        rs = pst.executeQuery();

        int totalBarang = 0;
        int jumlahPemasukan = 0;

        while (rs.next()) {
            String kodeBarang = rs.getString("kode_barang");
            String namaPemasok = rs.getString("nama_pemasok");
            String varian = rs.getString("varian");
            String berat = rs.getString("berat");
            int stok = rs.getInt("stok");
            String exp = rs.getString("exp");
            int hargaJual = rs.getInt("harga_jual");
            int hargaBeli = rs.getInt("harga_beli");
            String kategoriBarang = rs.getString("kategori");
            String tanggal = rs.getString("Tanggal");
            int qty = rs.getInt("qty");

            model.addRow(new Object[] {
                kodeBarang, namaPemasok, varian, berat, stok, exp,
                formatUang.format(hargaJual),
                formatUang.format(hargaBeli),
                kategoriBarang, tanggal
            });

            totalBarang += qty;
            jumlahPemasukan += qty * hargaBeli;
        }

        totalBarang1.setText(String.valueOf(totalBarang));
        totalPemasukan.setText(formatUang.format(jumlahPemasukan));

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error menampilkan data: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        Logger.getLogger(laporanPembelian.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        tutupResource();
    }
    }
     
     private boolean isValidDate(String dateStr) {
        try {
            formatTanggal.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
     
      private void tutupResource() {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(laporanPembelian.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
      private void eksporKePDF() {
        try {
            // Buat file chooser untuk memilih lokasi simpan
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Simpan Laporan Pembelian");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            
            if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
                return; // User membatalkan
            }
            
            // Dapatkan path file
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".pdf")) {
                filePath += ".pdf";
            }
            
            // Buat dokumen PDF
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            // Tambahkan judul
            Font fontJudul = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph judul = new Paragraph("LAPORAN PEMBELIAN", fontJudul);
            judul.setAlignment(Element.ALIGN_CENTER);
            document.add(judul);
            
            // Tambahkan periode
            Font fontInfo = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph info = new Paragraph("Periode: " + formatTanggal.format(jDateChooser1.getDate()) +
                                " s/d " + formatTanggal.format(jDateChooser2.getDate()), fontInfo);

            info.setAlignment(Element.ALIGN_CENTER);
            info.setSpacingAfter(20);
            document.add(info);
            
            // Buat tabel
            PdfPTable pdfTable = new PdfPTable(7); // 7 kolom sesuai dengan tabel di UI
            pdfTable.setWidthPercentage(100);
            
            // Header tabel
            String[] headers = {"No Transaksi", "Pemasok", "Kode Barang", "Nama Barang", "Qty", "Harga Beli", "Tanggal"};
            
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                pdfTable.addCell(cell);
            }
            
            // Isi tabel dengan data
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    pdfTable.addCell(model.getValueAt(i, j).toString());
                }
            }
            
            document.add(pdfTable);
            
            // Tambahkan total
            Paragraph totalInfo = new Paragraph("Total Barang: " + totalBarang1.getText() + 
                                               "          Total Pemasukan: Rp " + totalPemasukan.getText(), fontInfo);
            totalInfo.setSpacingBefore(20);
            document.add(totalInfo);
            
            document.close();
            
            JOptionPane.showMessageDialog(this, "Laporan berhasil disimpan ke " + filePath);
            
        } catch (DocumentException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error eksport PDF: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(laporanPembelian.class.getName()).log(Level.SEVERE, null, ex);
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
        penjualan1 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        retur2 = new javax.swing.JButton();
        totalPemasukan = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        totalBarang1 = new javax.swing.JTextField();

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

        penjualan1.setBorderPainted(false);
        penjualan1.setContentAreaFilled(false);
        penjualan1.setFocusPainted(false);
        penjualan1.setFocusable(false);
        penjualan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penjualan1ActionPerformed(evt);
            }
        });
        getContentPane().add(penjualan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 110, 120, 30));
        getContentPane().add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, 110, 30));
        getContentPane().add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 100, 110, 30));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 100, 120, 40));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Kode barang", "Pemasok", "Varian", "Berat", "Stok", "Exp", "Harga jual", "Harga beli", "Kategori", "Tanggal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
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
            jTable1.getColumnModel().getColumn(7).setResizable(false);
            jTable1.getColumnModel().getColumn(8).setResizable(false);
            jTable1.getColumnModel().getColumn(9).setResizable(false);
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
        totalPemasukan.setOpaque(false);
        totalPemasukan.setRequestFocusEnabled(false);
        getContentPane().add(totalPemasukan, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 680, 130, 40));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/pembeliann dan penjualan.png"))); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        totalBarang1.setEditable(false);
        totalBarang1.setBorder(null);
        totalBarang1.setOpaque(false);
        totalBarang1.setRequestFocusEnabled(false);
        getContentPane().add(totalBarang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 675, 130, 40));

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
        jual.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jual1ActionPerformed

    private void user1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_user1ActionPerformed
        user user = new user();
        user.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_user1ActionPerformed

    private void logout1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout1ActionPerformed
        login dashboard = new login();
        dashboard.setVisible(true);
        System.out.println("github perubahan");
        this.dispose();
    }//GEN-LAST:event_logout1ActionPerformed

    private void ekspor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ekspor1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_ekspor1ActionPerformed

    private void penjualan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penjualan1ActionPerformed
        laporanPenjualan laporanPenjualan = new laporanPenjualan();
        laporanPenjualan.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_penjualan1ActionPerformed

    private void retur2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retur2ActionPerformed
        retur retur = new retur();
        retur.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_retur2ActionPerformed

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
            java.util.logging.Logger.getLogger(laporanPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(laporanPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(laporanPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(laporanPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new laporanPembelian().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton barang1;
    private javax.swing.JButton dasbor1;
    private javax.swing.JButton ekspor1;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton jual1;
    private javax.swing.JButton laba;
    private javax.swing.JButton logout1;
    private javax.swing.JButton penjualan1;
    private javax.swing.JButton retur2;
    private javax.swing.JButton stokOpname;
    private javax.swing.JTextField totalBarang1;
    private javax.swing.JTextField totalPemasukan;
    private javax.swing.JButton user1;
    // End of variables declaration//GEN-END:variables
}
