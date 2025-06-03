/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kasirbfmm;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JDialog;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 *
 * @author Dhimas Ananta
 */
public class retur extends javax.swing.JFrame {
        databasee db = new databasee();
        long sumTotal = 0;
        private int selectedReturId = -1; // Variabel untuk menyimpan ID retur yang dipilih
          DefaultTableModel model = new DefaultTableModel();

    /**
     * Creates new form retur
     */
    
    public retur() {
            this.setUndecorated(true);//untuk(x)from
        
        initComponents();
            jtanggal.setEditable(false);
            noTrans1.setEditable(false);
        
            // Untuk jbarang1
        jDialog1.setUndecorated(true); // Ini akan menghilangkan semua dekorasi termasuk tombol close
        jDialog1.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Nonaktifkan tombol close
        
        kodeBarang1.setBackground(Color.WHITE);
        kodeBarang1.setOpaque(true);
        
        namaBarang1.setBackground(Color.WHITE);
        namaBarang1.setOpaque(true);
        
        alasan.setBackground(Color.WHITE);
        alasan.setOpaque(true);
        
        
cri.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        cariDataRetur();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        // Panggil cariDataRetur() saat ada penghapusan teks
        cariDataRetur();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        cariDataRetur();
    }
});
        
        tombolcari1.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        cariDataRetur();
    }
});
        
//        tidak.addActionListener(new java.awt.event.ActionListener() {
//    public void actionPerformed(java.awt.event.ActionEvent evt) {
//        jDialog3.setVisible(false);
//    }
//});

//silang.addActionListener(new java.awt.event.ActionListener() {
//    public void actionPerformed(java.awt.event.ActionEvent evt) {
//        jDialog3.setVisible(false);
//    }
//});
//
//ya2.addActionListener(new java.awt.event.ActionListener() {
//    public void actionPerformed(java.awt.event.ActionEvent evt) {
//        hapusDataRetur();
//        jDialog3.setVisible(false);
//    }
//});
        // Tambahkan mouse listener untuk tabel
    jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
        
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int row = jTable1.rowAtPoint(evt.getPoint());
            if (row >= 0 && evt.getClickCount() == 1) {
                jTable1.setRowSelectionInterval(row, row);
            }
        }
    });

        setTanggalOtomatis();
        

        tampilDataRetur();
        
                 buatNomor();
                 
        kodeBarang1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                cariBarang();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                cariBarang();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                cariBarang();
            }
        });
//                     model = new DefaultTableModel();
//    model.addColumn("Kode");
//    model.addColumn("Nama");
//    model.addColumn("qty");
//    model.addColumn("Alasan");
//    model.addColumn("Kategori");
//    jTable1.setModel(model);

// Inisialisasi tabel daftar barang
        initTabelDaftarBarang();

        // Event listener untuk pencarian
        // Event listener untuk pencarian
        cari3.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchBarang();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchBarang();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchBarang();
            }
        });

        // Event listener untuk klik tabel
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    selectBarangFromTable();
                }
            }
        });

        jTable2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    selectBarangFromTable();
                }
            }
        });

        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable2.setRowSelectionAllowed(true);
        
            jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tabelReturMouseClicked(evt);
        }
    });
        
    }
    
    
        // Tambahkan di class jual
    private void loadDataBarang(String keyword) {
        try {
            DefaultTableModel modelBarang = (DefaultTableModel) jTable2.getModel();
            modelBarang.setRowCount(0); // Kosongkan tabel

            String sql = "SELECT kode_barang, nama_barang, stok FROM tb_barang";
            if (keyword != null && !keyword.isEmpty()) {
                sql += " WHERE nama_barang LIKE '%" + keyword + "%' OR kode_barang LIKE '%" + keyword + "%'";
            }

            ResultSet rs = db.ambildata(sql);
            while (rs.next()) {
                modelBarang.addRow(new Object[]{
                    rs.getString("kode_barang"),
                    rs.getString("nama_barang"),
                    //                rs.getInt("harga_jual"),
                    rs.getInt("stok")
                });
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error memuat data barang: " + e.getMessage());
        }
    }

    private void initTabelDaftarBarang() {
        DefaultTableModel modelBarang = new DefaultTableModel();
        modelBarang.addColumn("Kode Barang");
        modelBarang.addColumn("Nama Barang");
//    modelBarang.addColumn("Harga Jual");
        modelBarang.addColumn("Stok");
        jTable2.setModel(modelBarang);

        // Atur lebar kolom
        jTable2.getColumnModel().getColumn(0).setPreferredWidth(150);
        jTable2.getColumnModel().getColumn(1).setPreferredWidth(300);
//    jTable2.getColumnModel().getColumn(2).setPreferredWidth(100);
        jTable2.getColumnModel().getColumn(2).setPreferredWidth(50);

        // Load data awal
        loadDataBarang(null);
    }

    private void searchBarang() {
        String keyword = cari3.getText().trim();
        loadDataBarang(keyword);
    }

    private void selectBarangFromTable() {
        int selectedRow = jTable2.getSelectedRow();
        if (selectedRow >= 0) {
            // Ambil data dari baris yang dipilih
            String kode = jTable2.getValueAt(selectedRow, 0).toString();
            String nama = jTable2.getValueAt(selectedRow, 1).toString();
            String stok = jTable2.getValueAt(selectedRow, 2).toString();

            // Isi ke form jual
            kodeBarang1.setText(kode);
            namaBarang1.setText(nama);
            qty1.setText(stok);

            // Fokus ke qty
            qty1.requestFocus();

            // Tutup dialog daftar barang
            daftar_barang.dispose();

        }
    }
    
    
    
    
    private void cariDataRetur() {
    String keyword = cri.getText().trim();
    
    // Jika kolom pencarian kosong, tampilkan semua data atau kosongkan tabel
    if (keyword.isEmpty()) {
        tampilDataRetur(); // Kembalikan ke tampilan semua data
        return;
        
    }
    
    
    
    

    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID");
    model.addColumn("Kode Barang");
    model.addColumn("Nama Barang");
    model.addColumn("Qty");
    model.addColumn("Kategori");
    model.addColumn("Alasan");
    model.addColumn("Tanggal");
    
    jTable1.setModel(model);
    model.setRowCount(0);

    try {
        ResultSet rs = db.ambildata(
            "SELECT rs.id_retur, rs.kode_barang, rs.nama_barang, rs.qty, " +
            "b.kategori, rs.alasan, rs.tanggal " +
            "FROM tb_retur_supplier rs " +
            "LEFT JOIN tb_barang b ON rs.kode_barang = b.kode_barang " +
            "WHERE rs.kode_barang LIKE '%" + keyword + "%' " +
            "OR LOWER(rs.nama_barang) LIKE LOWER('%" + keyword + "%') " +
            "OR LOWER(rs.alasan) LIKE LOWER('%" + keyword + "%')");

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_retur"),
                rs.getString("kode_barang"),
                rs.getString("nama_barang"),
                rs.getInt("qty"),
                rs.getString("kategori"),
                rs.getString("alasan"),
                rs.getDate("tanggal")
            });
        }
        rs.close();
        
        // Hide the ID column
        jTable1.removeColumn(jTable1.getColumnModel().getColumn(0));
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal melakukan pencarian: " + e.getMessage());
    }
}
    
    
    private void hapusDataRetur() {
    int[] selectedRows = jTable1.getSelectedRows();
    if (selectedRows.length == 0) {
        JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus terlebih dahulu!");
        return;
    }

    try {
        db.aksi("SET FOREIGN_KEY_CHECKS=0");
        
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            int row = selectedRows[i];
            int idRetur = (int) model.getValueAt(row, 0); // Kolom ID (hidden)
            String kodeBarang = (String) model.getValueAt(row, 1); // Kolom kode barang
            
            // 1. Ambil qty untuk mengembalikan stok
            ResultSet rs = db.ambildata("SELECT qty FROM tb_retur_supplier WHERE id_retur = " + idRetur);
            if (rs.next()) {
                int qty = rs.getInt("qty");
                
                // 2. Update stok barang (kembalikan stok)
                db.aksi("UPDATE tb_barang SET stok = stok + " + qty + 
                       " WHERE kode_barang = '" + kodeBarang + "'");
                
                // 3. Hapus dari tb_retur_supplier
                db.aksi("DELETE FROM tb_retur_supplier WHERE id_retur = " + idRetur);
                
                // 4. Insert ke kartu stok untuk mencatat pengembalian stok
                String tanggal = jtanggal.getText();
                db.aksi("INSERT INTO kartu_stok (tanggal, kode_barang, jenis_transaksi, " +
                       "qty_masuk, stok_akhir, keterangan) " +
                       "SELECT '" + tanggal + "', '" + kodeBarang + "', 'penyesuaian', " + 
                       qty + ", stok, 'Pembatalan retur' " +
                       "FROM tb_barang WHERE kode_barang = '" + kodeBarang + "'");
            }
            rs.close();
        }
        
        db.aksi("SET FOREIGN_KEY_CHECKS=1");
        JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
        tampilDataRetur();
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error saat menghapus: " + e.getMessage());
    } finally {
        try {
            db.aksi("SET FOREIGN_KEY_CHECKS=1");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mengaktifkan foreign key: " + e.getMessage());
        }
    }
}
    
    private void cariBarang() {
        String kode = kodeBarang1.getText();
        if (kode.length() >= 2) { // Mulai cari setelah 2 karakter diinput
            try {
                ResultSet rs = db.ambildata(
                        "SELECT nama_barang FROM tb_barang WHERE kode_barang LIKE '" + kode + "%' LIMIT 1");
                if (rs.next()) {
                    namaBarang1.setText(rs.getString("nama_barang"));
                } else {
                    namaBarang1.setText("");
                }
                rs.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

        public void setTanggalOtomatis() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Format tanggal
    String tanggalSekarang = sdf.format(new java.util.Date()); // Ambil tanggal sekarang
    jtanggal.setText(tanggalSekarang); // Set ke JTextField (ganti dengan nama variabel JTextField tanggal kamu)
//    tanggal1.setText(tanggalSekarang); // Set ke JTextField (ganti dengan nama variabel JTextField tanggal kamu)
}
        
         private void buatNomor() {
        try {
            ResultSet rs = db.ambildata("SELECT id_retur as auto FROM tb_retur_supplier ORDER BY id_retur DESC");
            if (rs.next()) {
                int noUrut = Integer.parseInt(rs.getString("auto")) + 1;
                noTrans1.setText(String.valueOf(noUrut));
            } else {
                noTrans1.setText("1");
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saat generate nomor: " + e.getMessage());
        }
    }

         
    private void simpanRetur() {
    try {
        // Validasi field kosong
        if (kodeBarang1.getText().isEmpty() || namaBarang1.getText().isEmpty() || 
            qty1.getText().isEmpty() || alasan.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        // Validasi qty harus angka positif
        int qty;
        try {
            qty = Integer.parseInt(qty1.getText());
            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Qty harus lebih dari 0");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Qty harus berupa angka");
            return;
        }

        // Cek apakah kode barang ada di database
        String kodeBarang = kodeBarang1.getText();
        ResultSet checkBarang = db.ambildata("SELECT * FROM tb_barang WHERE kode_barang = '" + kodeBarang + "'");
        if (!checkBarang.next()) {
            JOptionPane.showMessageDialog(this, "Kode barang tidak ditemukan!");
            checkBarang.close();
            return;
        }
        checkBarang.close();

        // Cek stok cukup (HANYA UNTUK MODE TAMBAH DATA BARU)
        if(selectedReturId == -1) {
            ResultSet stokBarang = db.ambildata("SELECT stok FROM tb_barang WHERE kode_barang = '" + kodeBarang + "'");
            stokBarang.next();
            int stok = stokBarang.getInt("stok");
            stokBarang.close();
            
            if (qty > stok) {
                JOptionPane.showMessageDialog(this, "Stok tidak mencukupi! Stok tersedia: " + stok);
                return;
            }
        }

        db.aksi("SET FOREIGN_KEY_CHECKS=0");

        // LOGIKA EDIT DATA
        if(selectedReturId != -1) {
            try {
                // Ambil qty lama
                ResultSet rsOld = db.ambildata("SELECT qty FROM tb_retur_supplier WHERE id_retur = " + selectedReturId);
                if(rsOld.next()) {
                    int oldQty = rsOld.getInt("qty");
                    int selisih = oldQty - qty;
                    
                    // Update data retur
                    String updateSql = "UPDATE tb_retur_supplier SET qty = " + qty + 
                                     " WHERE id_retur = " + selectedReturId;
                    
                    boolean berhasil = db.aksi(updateSql);
                    
                    if(berhasil) {
                        // Update stok barang
                        db.aksi("UPDATE tb_barang SET stok = stok + " + selisih + 
                               " WHERE kode_barang = '" + kodeBarang + "'");
                        
                        // Update kartu stok
                        String tanggal = jtanggal.getText();
                        db.aksi("INSERT INTO kartu_stok (tanggal, kode_barang, jenis_transaksi, " +
                               "qty_masuk, qty_keluar, stok_akhir, keterangan) " +
                               "SELECT '" + tanggal + "', '" + kodeBarang + "', 'penyesuaian', " + 
                               (selisih > 0 ? selisih : 0) + ", " + 
                               (selisih < 0 ? -selisih : 0) + ", stok, 'Edit retur' " +
                               "FROM tb_barang WHERE kode_barang = '" + kodeBarang + "'");
                        
                        JOptionPane.showMessageDialog(this, "Data retur berhasil diupdate!");
                        resetForm();
                        tampilDataRetur();
                        jDialog1.setVisible(false);
                        selectedReturId = -1;
                    }
                }
                rsOld.close();
            } catch(SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error saat update: " + ex.getMessage());
            }
        } 
        // LOGIKA TAMBAH DATA BARU
        else {
            String tanggal = jtanggal.getText();
            int idRetur = Integer.parseInt(noTrans1.getText());
            String noRetur = noTrans1.getText();
            String namaBarang = namaBarang1.getText();
            String alasanText = alasan.getText();

            // Ambil kd_trans_beli
            int kd_trans_beli = 0;
            ResultSet checkTransBeli = db.ambildata(
                "SELECT db.id_detail_beli " +
                "FROM detail_beli db " +
                "JOIN tb_barang b ON db.kode_barang = b.kode_barang " +
                "WHERE db.kode_barang = '" + kodeBarang + "' " +
                "ORDER BY db.id_detail_beli DESC LIMIT 1");
            
            if (checkTransBeli.next()) {
                kd_trans_beli = checkTransBeli.getInt("id_detail_beli");
            }
            checkTransBeli.close();

            // Query insert
            String sql = "INSERT INTO tb_retur_supplier (id_retur, no_retur, kd_trans_beli, kode_barang, " +
                         "nama_barang, qty, alasan, tanggal, id_user) VALUES (" +
                         idRetur + ", '" + noRetur + "', " + 
                         (kd_trans_beli > 0 ? kd_trans_beli : "NULL") + ", '" + 
                         kodeBarang + "', '" + namaBarang + "', " + qty + ", '" + 
                         alasanText + "', '" + tanggal + "', 2)";

            boolean berhasil = db.aksi(sql);

            if (berhasil) {
                // Update stok barang
                db.aksi("UPDATE tb_barang SET stok = stok - " + qty + 
                       " WHERE kode_barang = '" + kodeBarang + "'");
                
                // Insert ke kartu stok
                db.aksi("INSERT INTO kartu_stok (tanggal, kode_barang, jenis_transaksi, " +
                       "qty_keluar, stok_akhir, keterangan) " +
                       "SELECT '" + tanggal + "', '" + kodeBarang + "', 'retur', " + 
                       qty + ", stok, 'Retur barang ke supplier' " +
                       "FROM tb_barang WHERE kode_barang = '" + kodeBarang + "'");

                JOptionPane.showMessageDialog(this, "Data retur berhasil disimpan!");
                resetForm();
                buatNomor();
                jDialog1.setVisible(false);
            }
        }

        db.aksi("SET FOREIGN_KEY_CHECKS=1");
        tampilDataRetur();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
    } finally {
        try {
            db.aksi("SET FOREIGN_KEY_CHECKS=1");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mengaktifkan foreign key: " + e.getMessage());
        }
    }
}

    private void resetForm() {
    kodeBarang1.setText("");
    namaBarang1.setText("");
    qty1.setText("");
    alasan.setText("");
    selectedReturId = -1; // Reset ID retur yang dipilih
    
    // Aktifkan kembali semua field
    kodeBarang1.setEnabled(true);
    namaBarang1.setEnabled(true);
    alasan.setEnabled(true);
    qty1.setEnabled(true);
    
    // Generate nomor transaksi baru jika dalam mode tambah
    if (selectedReturId == -1) {
        buatNomor();
    }
    }
   
    private javax.swing.JDialog jBarang2;
private javax.swing.JTextField editQty;
private javax.swing.JButton btnSimpanEdit;
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        simpan = new javax.swing.JButton();
        alasan = new javax.swing.JTextField();
        namaBarang1 = new javax.swing.JTextField();
        qty1 = new javax.swing.JTextField();
        kodeBarang1 = new javax.swing.JTextField();
        jtanggal = new javax.swing.JTextField();
        noTrans1 = new javax.swing.JTextField();
        Jcari2 = new javax.swing.JButton();
        jcancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jDialog2 = new javax.swing.JDialog();
        simpan1 = new javax.swing.JButton();
        alasan1 = new javax.swing.JTextField();
        namaBarang2 = new javax.swing.JTextField();
        qty2 = new javax.swing.JTextField();
        kodeBarang2 = new javax.swing.JTextField();
        jtanggal1 = new javax.swing.JTextField();
        noTrans2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        daftar_barang = new javax.swing.JDialog();
        cari3 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        tombolhapus = new javax.swing.JButton();
        tombollogout = new javax.swing.JButton();
        tombolstokOpname = new javax.swing.JButton();
        tomboljual1 = new javax.swing.JButton();
        tomboluser1 = new javax.swing.JButton();
        tombollaporan1 = new javax.swing.JButton();
        tomboldasbor1 = new javax.swing.JButton();
        tombolcari1 = new javax.swing.JButton();
        tomboltambah1 = new javax.swing.JButton();
        tomboledit1 = new javax.swing.JButton();
        cri = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        tombolbarang2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        jDialog1.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        simpan.setBorder(null);
        simpan.setBorderPainted(false);
        simpan.setContentAreaFilled(false);
        simpan.setFocusPainted(false);
        simpan.setRequestFocusEnabled(false);
        simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, 460, 30));

        alasan.setBackground(new java.awt.Color(255, 255, 255));
        alasan.setBorder(null);
        alasan.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jDialog1.getContentPane().add(alasan, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 250, 200, 20));

        namaBarang1.setBackground(new java.awt.Color(255, 255, 255));
        namaBarang1.setBorder(null);
        namaBarang1.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        namaBarang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaBarang1ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(namaBarang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 170, 200, 20));

        qty1.setBackground(new java.awt.Color(255, 255, 255));
        qty1.setBorder(null);
        qty1.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jDialog1.getContentPane().add(qty1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 200, 20));

        kodeBarang1.setBackground(new java.awt.Color(255, 255, 255));
        kodeBarang1.setBorder(null);
        kodeBarang1.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        kodeBarang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kodeBarang1ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(kodeBarang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 190, 30));

        jtanggal.setBorder(null);
        jtanggal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtanggalActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(jtanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, 100, 20));

        noTrans1.setBorder(null);
        noTrans1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noTrans1ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(noTrans1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 100, 20));

        Jcari2.setBorder(null);
        Jcari2.setBorderPainted(false);
        Jcari2.setContentAreaFilled(false);
        Jcari2.setFocusPainted(false);
        Jcari2.setFocusable(false);
        Jcari2.setRequestFocusEnabled(false);
        Jcari2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Jcari2ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(Jcari2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 170, 90, 30));

        jcancel.setBackground(new java.awt.Color(255, 255, 255));
        jcancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/foto/Cancel.png"))); // NOI18N
        jcancel.setBorder(null);
        jcancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcancelActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(jcancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 40, 50, 40));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/return_2.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jDialog1.getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-30, -60, 710, 490));

        jDialog2.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        simpan1.setBorder(null);
        simpan1.setBorderPainted(false);
        simpan1.setContentAreaFilled(false);
        simpan1.setFocusPainted(false);
        simpan1.setRequestFocusEnabled(false);
        simpan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpan1ActionPerformed(evt);
            }
        });
        jDialog2.getContentPane().add(simpan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 310, 470, 40));

        alasan1.setBackground(new java.awt.Color(255, 255, 255));
        alasan1.setBorder(null);
        alasan1.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jDialog2.getContentPane().add(alasan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 250, 190, 30));

        namaBarang2.setBackground(new java.awt.Color(255, 255, 255));
        namaBarang2.setBorder(null);
        namaBarang2.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        namaBarang2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaBarang2ActionPerformed(evt);
            }
        });
        jDialog2.getContentPane().add(namaBarang2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 170, 190, 30));

        qty2.setBackground(new java.awt.Color(255, 255, 255));
        qty2.setBorder(null);
        qty2.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jDialog2.getContentPane().add(qty2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 250, 190, 30));

        kodeBarang2.setBackground(new java.awt.Color(255, 255, 255));
        kodeBarang2.setBorder(null);
        kodeBarang2.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jDialog2.getContentPane().add(kodeBarang2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 200, 20));

        jtanggal1.setBackground(new java.awt.Color(153, 153, 153));
        jtanggal1.setBorder(null);
        jtanggal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtanggal1ActionPerformed(evt);
            }
        });
        jDialog2.getContentPane().add(jtanggal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 100, 20));

        noTrans2.setBackground(new java.awt.Color(153, 153, 153));
        noTrans2.setBorder(null);
        noTrans2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noTrans2ActionPerformed(evt);
            }
        });
        jDialog2.getContentPane().add(noTrans2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 100, 20));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/return_1.png"))); // NOI18N
        jLabel3.setText("jLabel1");
        jDialog2.getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, -20, 580, 420));

        daftar_barang.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cari3.setBorder(null);
        cari3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cari3ActionPerformed(evt);
            }
        });
        daftar_barang.getContentPane().add(cari3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 1150, 20));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Kode barang", "Nama barang", "Stok"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setColumnSelectionAllowed(true);
        jScrollPane2.setViewportView(jTable2);

        daftar_barang.getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 1160, 520));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/cari_data.jpg"))); // NOI18N
        daftar_barang.getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 760));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tombolhapus.setBorderPainted(false);
        tombolhapus.setContentAreaFilled(false);
        tombolhapus.setFocusPainted(false);
        tombolhapus.setFocusable(false);
        tombolhapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolhapusActionPerformed(evt);
            }
        });
        getContentPane().add(tombolhapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 100, 130, 40));

        tombollogout.setBorderPainted(false);
        tombollogout.setContentAreaFilled(false);
        tombollogout.setFocusPainted(false);
        tombollogout.setFocusable(false);
        tombollogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombollogoutActionPerformed(evt);
            }
        });
        getContentPane().add(tombollogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 690, 110, 20));

        tombolstokOpname.setBorderPainted(false);
        tombolstokOpname.setContentAreaFilled(false);
        tombolstokOpname.setFocusPainted(false);
        tombolstokOpname.setFocusable(false);
        tombolstokOpname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolstokOpnameActionPerformed(evt);
            }
        });
        getContentPane().add(tombolstokOpname, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 120, 20));

        tomboljual1.setBorderPainted(false);
        tomboljual1.setContentAreaFilled(false);
        tomboljual1.setFocusPainted(false);
        tomboljual1.setFocusable(false);
        tomboljual1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tomboljual1ActionPerformed(evt);
            }
        });
        getContentPane().add(tomboljual1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 90, 20));

        tomboluser1.setBorderPainted(false);
        tomboluser1.setContentAreaFilled(false);
        tomboluser1.setFocusPainted(false);
        tomboluser1.setFocusable(false);
        tomboluser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tomboluser1ActionPerformed(evt);
            }
        });
        getContentPane().add(tomboluser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 90, 20));

        tombollaporan1.setBorderPainted(false);
        tombollaporan1.setContentAreaFilled(false);
        tombollaporan1.setFocusPainted(false);
        tombollaporan1.setFocusable(false);
        tombollaporan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombollaporan1ActionPerformed(evt);
            }
        });
        getContentPane().add(tombollaporan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 110, 20));

        tomboldasbor1.setBorderPainted(false);
        tomboldasbor1.setContentAreaFilled(false);
        tomboldasbor1.setFocusPainted(false);
        tomboldasbor1.setFocusable(false);
        tomboldasbor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tomboldasbor1ActionPerformed(evt);
            }
        });
        getContentPane().add(tomboldasbor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 110, 20));

        tombolcari1.setBorderPainted(false);
        tombolcari1.setContentAreaFilled(false);
        tombolcari1.setFocusPainted(false);
        tombolcari1.setFocusable(false);
        tombolcari1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolcari1ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolcari1, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 100, -1, 40));

        tomboltambah1.setBorderPainted(false);
        tomboltambah1.setContentAreaFilled(false);
        tomboltambah1.setFocusPainted(false);
        tomboltambah1.setFocusable(false);
        tomboltambah1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tomboltambah1ActionPerformed(evt);
            }
        });
        getContentPane().add(tomboltambah1, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 100, 120, 40));

        tomboledit1.setBorderPainted(false);
        tomboledit1.setContentAreaFilled(false);
        tomboledit1.setFocusPainted(false);
        tomboledit1.setFocusable(false);
        tomboledit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tomboledit1ActionPerformed(evt);
            }
        });
        getContentPane().add(tomboledit1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 100, 130, 40));

        cri.setBorder(null);
        getContentPane().add(cri, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, 400, 30));

        jTable1.setBackground(new java.awt.Color(102, 102, 102));
        jTable1.setForeground(new java.awt.Color(204, 204, 204));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Kode Barang", "Nama Barang", "Qty", "Kategori", "Alasan", "Tanggal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 162, 1020, 530));

        tombolbarang2.setBorderPainted(false);
        tombolbarang2.setContentAreaFilled(false);
        tombolbarang2.setFocusPainted(false);
        tombolbarang2.setFocusable(false);
        tombolbarang2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolbarang2ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolbarang2, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 200, 100, 20));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/retur.png"))); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tombolhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolhapusActionPerformed
        // TODO add your handling code here:
//    jDialog3.setSize(319, 330); // Sesuaikan dengan ukuran yang diinginkan
//    jDialog3.setLocationRelativeTo(this); // Supaya muncul di tengah
//    jDialog3.setModal(true); // Membuat dialog bersifat modal (opsional)
//    jDialog3.setVisible(true); // Menampilkan dialog


    if (selectedReturId == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus terlebih dahulu!");
        return;
    }

    // Tampilkan dialog konfirmasi
    int confirm = JOptionPane.showConfirmDialog(
        this, 
        "Apakah Anda yakin ingin menghapus data retur ini?", 
        "Konfirmasi Hapus", 
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            // 1. Ambil data retur yang akan dihapus
            ResultSet rs = db.ambildata(
                "SELECT kode_barang, qty FROM tb_retur_supplier WHERE id_retur = " + selectedReturId);
            
            if (rs.next()) {
                String kodeBarang = rs.getString("kode_barang");
                int qty = rs.getInt("qty");
                
                // 2. Kembalikan stok barang
                db.aksi("UPDATE tb_barang SET stok = stok + " + qty + 
                       " WHERE kode_barang = '" + kodeBarang + "'");
                
                // 3. Catat di kartu stok
                String tanggal = jtanggal.getText();
                db.aksi("INSERT INTO kartu_stok (tanggal, kode_barang, jenis_transaksi, " +
                       "qty_masuk, stok_akhir, keterangan) " +
                       "SELECT '" + tanggal + "', '" + kodeBarang + "', 'penyesuaian', " + 
                       qty + ", stok, 'Pembatalan retur' " +
                       "FROM tb_barang WHERE kode_barang = '" + kodeBarang + "'");
                
                // 4. Hapus data retur
                db.aksi("DELETE FROM tb_retur_supplier WHERE id_retur = " + selectedReturId);
                
                JOptionPane.showMessageDialog(this, "Data retur berhasil dihapus!");
                tampilDataRetur(); // Refresh tabel
                selectedReturId = -1; // Reset selected ID
            }
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage());
        }
    }

    }//GEN-LAST:event_tombolhapusActionPerformed

    private void tomboldasbor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tomboldasbor1ActionPerformed
        dashboard dashboard = new dashboard();
        dashboard.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tomboldasbor1ActionPerformed

    private void tombolcari1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolcari1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tombolcari1ActionPerformed

    private void tomboltambah1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tomboltambah1ActionPerformed
    resetForm(); // Reset form sebelum menampilkan dialog tambah
    buatNomor(); // Generate nomor transaksi baru
    jDialog1.setSize(637, 410); // Sesuaikan dengan ukuran yang diinginkan
    jDialog1.setLocationRelativeTo(this); // Supaya muncul di tengah
    jDialog1.setModal(true); // Membuat dialog bersifat modal (opsional)
    jDialog1.setVisible(true); // Menampilkan dialog
    }//GEN-LAST:event_tomboltambah1ActionPerformed

    private void tomboledit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tomboledit1ActionPerformed
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data yang akan diedit terlebih dahulu!");
        return;
    }

    // Get the actual model (accounting for hidden columns)
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    
    // Column indexes might be different now - adjust accordingly
    selectedReturId = (int) model.getValueAt(selectedRow, 0); // ID is column 0
    String kodeBarang = (String) model.getValueAt(selectedRow, 1);
    String namaBarang = (String) model.getValueAt(selectedRow, 2);
    String qty = model.getValueAt(selectedRow, 3).toString();
    String alasanText = (String) model.getValueAt(selectedRow, 5);

    // Fill the form
    kodeBarang1.setText(kodeBarang);
    namaBarang1.setText(namaBarang);
    qty1.setText(qty);
    alasan.setText(alasanText);

    // Disable fields that shouldn't be edited
    kodeBarang1.setEnabled(false);
    namaBarang1.setEnabled(false);
    alasan.setEnabled(false);

    // Show the edit dialog
    jDialog1.setSize(637, 410);
    jDialog1.setLocationRelativeTo(this);
    jDialog1.setModal(true);
    jDialog1.setVisible(true);

    }//GEN-LAST:event_tomboledit1ActionPerformed

    private void tombolbarang2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolbarang2ActionPerformed
        barang barang = new barang();
        barang.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tombolbarang2ActionPerformed

    private void tombolstokOpnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolstokOpnameActionPerformed
        stokOpname stokOpname = new stokOpname();
        stokOpname.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tombolstokOpnameActionPerformed

    private void tomboljual1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tomboljual1ActionPerformed
        jual jual = new jual();
        jual.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tomboljual1ActionPerformed

    private void tomboluser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tomboluser1ActionPerformed
        user user = new user();
        user.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tomboluser1ActionPerformed

    private void tombollaporan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombollaporan1ActionPerformed
        laporanPembelian laporanPembelian = new laporanPembelian();
        laporanPembelian.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tombollaporan1ActionPerformed

    private void tombollogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombollogoutActionPerformed
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
    }//GEN-LAST:event_tombollogoutActionPerformed

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
       simpanRetur();
    }//GEN-LAST:event_simpanActionPerformed

    private void namaBarang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaBarang1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaBarang1ActionPerformed

    private void simpan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpan1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_simpan1ActionPerformed

    private void namaBarang2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaBarang2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaBarang2ActionPerformed

    private void jtanggalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtanggalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtanggalActionPerformed

    private void kodeBarang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kodeBarang1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kodeBarang1ActionPerformed

    private void noTrans1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noTrans1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noTrans1ActionPerformed

    private void noTrans2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noTrans2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noTrans2ActionPerformed

    private void jtanggal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtanggal1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtanggal1ActionPerformed

    private void Jcari2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Jcari2ActionPerformed
        // TODO add your handling code here:
            // Muat ulang data barang
    loadDataBarang(null);
    
    // Kosongkan field pencarian
    cari3.setText("");
    
    // Tampilkan dialog
    daftar_barang.setSize(1370, 768);
    daftar_barang.setLocationRelativeTo(this);
    daftar_barang.setModal(true);
    daftar_barang.setVisible(true);
    }//GEN-LAST:event_Jcari2ActionPerformed

    private void cari3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cari3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cari3ActionPerformed

    private void jcancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcancelActionPerformed
    resetForm(); // Panggil method resetForm
    jDialog1.dispose();  // Tutup JDialog
    this.setVisible(true); // Pastikan JFrame tetap terlihat
    }//GEN-LAST:event_jcancelActionPerformed

    
 
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
            java.util.logging.Logger.getLogger(retur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(retur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(retur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(retur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new retur().setVisible(true);
            }
        });
    }
public void tampilDataRetur() {
    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            // Seluruh cell tidak bisa di-edit
            return false;
        }
    };
    // Add columns including the ID column
    model.addColumn("ID"); // Hidden column
    model.addColumn("Kode Barang");
    model.addColumn("Nama Barang");
    model.addColumn("Qty");
    model.addColumn("Kategori");
    model.addColumn("Alasan");
    model.addColumn("Tanggal");
    
    jTable1.setModel(model);
    model.setRowCount(0);

    try {
        ResultSet rs = db.ambildata(
            "SELECT rs.id_retur, rs.kode_barang, rs.nama_barang, rs.qty, " +
            "b.kategori, rs.alasan, rs.tanggal " +
            "FROM tb_retur_supplier rs " +
            "LEFT JOIN tb_barang b ON rs.kode_barang = b.kode_barang");

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_retur"),       // Kolom 0 (ID Retur)
                rs.getString("kode_barang"), // Kolom 1
                rs.getString("nama_barang"), // Kolom 2
                rs.getInt("qty"),           // Kolom 3
                rs.getString("kategori"),    // Kolom 4
                rs.getString("alasan"),      // Kolom 5
                rs.getDate("tanggal")        // Kolom 6
            });
        }
        rs.close();
        
        // Hide the ID column
        jTable1.removeColumn(jTable1.getColumnModel().getColumn(0));
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menampilkan data: " + e.getMessage());
    }
}

private void tabelReturMouseClicked(java.awt.event.MouseEvent evt) {
    int row = jTable1.getSelectedRow();
    if (row >= 0) {
        // Ambil id_retur dari baris yang dipilih
        String kodeBarang = jTable1.getValueAt(row, 0).toString();
        try {
            ResultSet rs = db.ambildata("SELECT id_retur FROM tb_retur_supplier WHERE kode_barang = '" + kodeBarang + "'");
            if (rs.next()) {
                selectedReturId = rs.getInt("id_retur");
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}

 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Jcari2;
    private javax.swing.JTextField alasan;
    private javax.swing.JTextField alasan1;
    private javax.swing.JTextField cari3;
    private javax.swing.JTextField cri;
    private javax.swing.JDialog daftar_barang;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton jcancel;
    private javax.swing.JTextField jtanggal;
    private javax.swing.JTextField jtanggal1;
    private javax.swing.JTextField kodeBarang1;
    private javax.swing.JTextField kodeBarang2;
    private javax.swing.JTextField namaBarang1;
    private javax.swing.JTextField namaBarang2;
    private javax.swing.JTextField noTrans1;
    private javax.swing.JTextField noTrans2;
    private javax.swing.JTextField qty1;
    private javax.swing.JTextField qty2;
    private javax.swing.JButton simpan;
    private javax.swing.JButton simpan1;
    private javax.swing.JButton tombolbarang2;
    private javax.swing.JButton tombolcari1;
    private javax.swing.JButton tomboldasbor1;
    private javax.swing.JButton tomboledit1;
    private javax.swing.JButton tombolhapus;
    private javax.swing.JButton tomboljual1;
    private javax.swing.JButton tombollaporan1;
    private javax.swing.JButton tombollogout;
    private javax.swing.JButton tombolstokOpname;
    private javax.swing.JButton tomboltambah1;
    private javax.swing.JButton tomboluser1;
    // End of variables declaration//GEN-END:variables
}
