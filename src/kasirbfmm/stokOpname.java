/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kasirbfmm;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Dhimas Ananta
 */
public class stokOpname extends javax.swing.JFrame {
    databasee db = new databasee();
    private String selectedKodeBarang;
    private String selectedTanggal;
    /**
     * Creates new form stokOpname
     */
    public stokOpname() {
        
        
                   this.setUndecorated(true);//untuk(x)from
        
        initComponents();
        
            jDialog1.setUndecorated(true); // Ini akan menghilangkan semua dekorasi termasuk tombol close
    jDialog1.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Nonaktifkan tombol close
        
        
                    setTanggalOtomatis();
                    //         Load data ke tabel saat form dibuka
        loadDataToTable();
        
        jtgl.setEditable(false);

        
                        //tampilkan tanggal transaksi
        tglskrg();
        
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
        
                // Tambahkan listener untuk stok fisik (kartuStok1)
        kartuStok1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                hitungSelisih();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                hitungSelisih();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                hitungSelisih();
            }
        });
        
         jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        tableClicked(evt);
    }
});

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
        
        
                c.setEditable(false); 
        c.setOpaque(false); 
        c.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        c.setFocusable(false); // Biar nggak bisa di-klik
        
            // Tambahkan listener untuk pencarian di jTextField1
    jTextField1.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            searchData();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            searchData();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            searchData();
        }
    });
        
    }
    
    
    
    
    
    
    private void searchData() {
     String keyword = jTextField1.getText().trim();
     
    try {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Kosongkan tabel
        

        String sql = "SELECT so.kode_barang, b.nama_barang, so.stok_sistem, so.stok_fisik, so.selisih, so.keterangan, so.tanggal "
                + "FROM tb_stok_opname so "
                + "JOIN tb_barang b ON so.kode_barang = b.kode_barang "
                + "WHERE so.kode_barang LIKE '%" + keyword + "%' OR b.nama_barang LIKE '%" + keyword + "%' "
                + "ORDER BY so.tanggal DESC";

        // Gunakan method ambildata() dari class databasee
        ResultSet rs = db.ambildata(sql);

        while (rs.next()) {
            Object[] row = {
                rs.getString("kode_barang"),
                rs.getString("nama_barang"),
                rs.getInt("stok_sistem"),
                rs.getInt("stok_fisik"),
                rs.getInt("selisih"),
                rs.getString("keterangan"),
                rs.getString("tanggal")
            };
            model.addRow(row);
        }

        rs.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error saat mencari data: " + e.getMessage());
    }
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
        String keyword = cari1.getText().trim();
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
            stokSistem1.setText(stok);

            // Fokus ke qty
            stokSistem1.requestFocus();

            // Tutup dialog daftar barang
            daftar_barang.dispose();

            // Otomatis hitung jika qty sudah diisi
            if (!stokSistem1.getText().isEmpty()) {
                hitungSelisih();
            }
        }
    }
    
    
    
    
     private void tableClicked(java.awt.event.MouseEvent evt) {
        
         int row = jTable1.getSelectedRow();
         if (row >= 0) {
             DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
             selectedKodeBarang = model.getValueAt(row, 0).toString();
             selectedTanggal = model.getValueAt(row, 6).toString(); // Index 6 untuk tanggal

             // Isi jTextField1 dengan nama barang yang dipilih
             jTextField1.setText(model.getValueAt(row, 1).toString());
         }
}
     
     
         public void setTanggalOtomatis() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Format tanggal
    String tanggalSekarang = sdf.format(new Date()); // Ambil tanggal sekarang
    jtgl.setText(tanggalSekarang); // Set ke JTextField (ganti dengan nama variabel JTextField tanggal kamu)
}
         
         
         
         private void loadDataToTable() {
             try {
                 // Query untuk mengambil data stok opname dengan nama barang
                 String sql = "SELECT so.kode_barang, b.nama_barang, so.stok_sistem, so.stok_fisik, so.selisih, so.keterangan, so.tanggal "
                         + "FROM tb_stok_opname so "
                         + "JOIN tb_barang b ON so.kode_barang = b.kode_barang "
                         + "ORDER BY so.tanggal DESC";

                 ResultSet rs = db.ambildata(sql);

                 // Model tabel default
                 DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                 model.setRowCount(0); // Kosongkan tabel sebelum mengisi data baru

                 // Ganti kolom tabel dengan yang baru
                 model.setColumnIdentifiers(new Object[]{"Kode Barang", "Nama Barang", "Stok Sistem", "Stok Fisik", "Selisih", "Keterangan", "Tanggal"});

                 // Isi tabel dengan data dari ResultSet
                 while (rs.next()) {
                     Object[] row = {
                         rs.getString("kode_barang"),
                         rs.getString("nama_barang"),
                         rs.getInt("stok_sistem"),
                         rs.getInt("stok_fisik"),
                         rs.getInt("selisih"),
                         rs.getString("keterangan"),
                         rs.getString("tanggal")
                     };
                     model.addRow(row);
                 }

                 rs.close();
             } catch (SQLException e) {
                 JOptionPane.showMessageDialog(this, "Error saat memuat data: " + e.getMessage());
             }
}
         
         
         
           private void hitungSelisih() {
        try {
            // Ambil nilai stok sistem dan stok fisik
            int stokSistem = stokSistem1.getText().isEmpty() ? 0 : Integer.parseInt(stokSistem1.getText());
            int stokFisik = kartuStok1.getText().isEmpty() ? 0 : Integer.parseInt(kartuStok1.getText());

            // Hitung selisih untuk tampilan UI saja
            int selisihValue = stokFisik - stokSistem;

            // Tampilkan selisih (hanya untuk preview, tidak disimpan ke database)
            selisih.setText(String.valueOf(selisihValue));

            // Beri warna berdasarkan positif/negatif
            if (selisihValue > 0) {
                selisih.setForeground(Color.GREEN);
            } else if (selisihValue < 0) {
                selisih.setForeground(Color.RED);
            } else {
                selisih.setForeground(Color.BLACK);
            }
        } catch (NumberFormatException e) {
            // Handle jika input bukan angka
            selisih.setText("");
        }
    }
           
           private void resetForm() {
    kodeBarang1.setText("");
    namaBarang1.setText("");
    stokSistem1.setText("");
    kartuStok1.setText("");
    selisih.setText("");
    keterangan1.setText("");
    selectedKodeBarang = null;
    selectedTanggal = null;
}
    
    
   private void cariBarang() { 
    String kode = kodeBarang1.getText(); 
    if (kode.length() >= 2) { // Mulai cari setelah 2 karakter diinput 
        try { 
            ResultSet rs = db.ambildata( 
                    "SELECT nama_barang, stok FROM tb_barang WHERE kode_barang LIKE '" + kode + "%' LIMIT 1"); 
            if (rs.next()) { 
                namaBarang1.setText(rs.getString("nama_barang")); 
                stokSistem1.setText(rs.getString("stok")); // Tambahan untuk mengisi stok
                 
            } else { 
                namaBarang1.setText(""); 
                stokSistem1.setText(""); // Kosongkan stok jika tidak ditemukan
                 
            } 
            rs.close(); 
        } catch (Exception e) { 
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); 
        } 
    } else {
        // Kosongkan field jika input kurang dari 2 karakter
        namaBarang1.setText("");
        stokSistem1.setText("");
    }
}
    
    
    
    public void tglskrg(){
    Date skrg = new Date();
    // Format untuk tampilan di form (readable)
    SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd");
    // Format untuk database (SQL)
    SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    // Simpan format SQL dalam variable yang bisa diakses class
    String tanggalSQL = sqlFormat.format(skrg);
    // Tampilkan format readable di form
    jtgl.setText(displayFormat.format(skrg));
}
    
        // Tambahkan method baru untuk mendapatkan tanggal SQL
    public String getTanggalSQL() {
        Date skrg = new Date();
        SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sqlFormat.format(skrg);
}

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
        cari2 = new javax.swing.JButton();
        jtgl = new javax.swing.JTextField();
        kodeBarang1 = new javax.swing.JTextField();
        namaBarang1 = new javax.swing.JTextField();
        kartuStok1 = new javax.swing.JTextField();
        stokSistem1 = new javax.swing.JTextField();
        keterangan1 = new javax.swing.JTextField();
        selisih = new javax.swing.JTextField();
        jcancel = new javax.swing.JButton();
        c = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        daftar_barang = new javax.swing.JDialog();
        cari3 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        hapus = new javax.swing.JButton();
        dasbor1 = new javax.swing.JButton();
        barang1 = new javax.swing.JButton();
        retur1 = new javax.swing.JButton();
        jual1 = new javax.swing.JButton();
        user1 = new javax.swing.JButton();
        laporan1 = new javax.swing.JButton();
        logout1 = new javax.swing.JButton();
        cari1 = new javax.swing.JButton();
        ekspor = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        edit1 = new javax.swing.JButton();
        tambah2 = new javax.swing.JButton();
        c1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        jDialog1.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        simpan.setBorder(null);
        simpan.setBorderPainted(false);
        simpan.setContentAreaFilled(false);
        simpan.setFocusPainted(false);
        simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 470, 30));

        cari2.setBorder(null);
        cari2.setBorderPainted(false);
        cari2.setContentAreaFilled(false);
        cari2.setFocusPainted(false);
        cari2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cari2ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(cari2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 130, 90, 40));

        jtgl.setBorder(null);
        jtgl.setFocusable(false);
        jtgl.setRequestFocusEnabled(false);
        jDialog1.getContentPane().add(jtgl, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 120, 20));

        kodeBarang1.setBorder(null);
        kodeBarang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kodeBarang1ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(kodeBarang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 190, 30));

        namaBarang1.setBorder(null);
        jDialog1.getContentPane().add(namaBarang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 140, 200, 30));

        kartuStok1.setBorder(null);
        kartuStok1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kartuStok1ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(kartuStok1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 120, 20));

        stokSistem1.setBorder(null);
        jDialog1.getContentPane().add(stokSistem1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 220, 130, 20));

        keterangan1.setBorder(null);
        keterangan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keterangan1ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(keterangan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 450, 30));

        selisih.setBorder(null);
        selisih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selisihActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(selisih, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 220, 90, 20));

        jcancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/foto/Cancel.png"))); // NOI18N
        jcancel.setBorder(null);
        jcancel.setBorderPainted(false);
        jcancel.setContentAreaFilled(false);
        jcancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcancelActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(jcancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 40, 50, 40));

        c.setBackground(new java.awt.Color(255, 255, 255));
        c.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jDialog1.getContentPane().add(c, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 0, 160, 90));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/stokkop_1.png"))); // NOI18N
        jDialog1.getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 0, 650, 420));

        jTextField2.setText("jTextField2");
        jDialog1.getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 40, -1, -1));

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

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/cari_data.jpg"))); // NOI18N
        daftar_barang.getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 760));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        hapus.setBorderPainted(false);
        hapus.setContentAreaFilled(false);
        hapus.setFocusPainted(false);
        hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusActionPerformed(evt);
            }
        });
        getContentPane().add(hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 100, 120, 40));

        dasbor1.setBorderPainted(false);
        dasbor1.setContentAreaFilled(false);
        dasbor1.setFocusPainted(false);
        dasbor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dasbor1ActionPerformed(evt);
            }
        });
        getContentPane().add(dasbor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 110, 20));

        barang1.setBorderPainted(false);
        barang1.setContentAreaFilled(false);
        barang1.setFocusPainted(false);
        barang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barang1ActionPerformed(evt);
            }
        });
        getContentPane().add(barang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 110, 20));

        retur1.setBorderPainted(false);
        retur1.setContentAreaFilled(false);
        retur1.setFocusPainted(false);
        retur1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retur1ActionPerformed(evt);
            }
        });
        getContentPane().add(retur1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 90, 20));

        jual1.setBorderPainted(false);
        jual1.setContentAreaFilled(false);
        jual1.setFocusPainted(false);
        jual1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jual1ActionPerformed(evt);
            }
        });
        getContentPane().add(jual1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 90, 20));

        user1.setBorderPainted(false);
        user1.setContentAreaFilled(false);
        user1.setFocusPainted(false);
        user1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user1ActionPerformed(evt);
            }
        });
        getContentPane().add(user1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 90, 20));

        laporan1.setBorderPainted(false);
        laporan1.setContentAreaFilled(false);
        laporan1.setFocusPainted(false);
        laporan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laporan1ActionPerformed(evt);
            }
        });
        getContentPane().add(laporan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 110, 20));

        logout1.setBorderPainted(false);
        logout1.setContentAreaFilled(false);
        logout1.setFocusPainted(false);
        logout1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout1ActionPerformed(evt);
            }
        });
        getContentPane().add(logout1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 690, 110, 20));

        cari1.setBorderPainted(false);
        cari1.setContentAreaFilled(false);
        cari1.setFocusPainted(false);
        cari1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cari1ActionPerformed(evt);
            }
        });
        getContentPane().add(cari1, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 100, 30, 40));

        ekspor.setBorderPainted(false);
        ekspor.setContentAreaFilled(false);
        ekspor.setFocusPainted(false);
        getContentPane().add(ekspor, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 100, 120, 40));

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setBorder(null);
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 99, 410, 30));

        edit1.setBorderPainted(false);
        edit1.setContentAreaFilled(false);
        edit1.setFocusPainted(false);
        edit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit1ActionPerformed(evt);
            }
        });
        getContentPane().add(edit1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 100, 120, 40));

        tambah2.setBorderPainted(false);
        tambah2.setContentAreaFilled(false);
        tambah2.setFocusPainted(false);
        tambah2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambah2ActionPerformed(evt);
            }
        });
        getContentPane().add(tambah2, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 100, 120, 40));

        c1.setBackground(new java.awt.Color(255, 255, 255));
        c1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        c1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c1ActionPerformed(evt);
            }
        });
        getContentPane().add(c1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 90, 140, 50));

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
                "Kode barang", "Stok sistem", "Stok fisik", "Selisih", "Keterangan", "Tanggal"
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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 162, 1010, 530));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/Stok opnameeee.png"))); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

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

    private void laporan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_laporan1ActionPerformed
        laporanPembelian laporanPembelian = new laporanPembelian();
        laporanPembelian.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_laporan1ActionPerformed

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

    private void tambah2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambah2ActionPerformed
        // TODO add your handling code here:
    jDialog1.setSize(626, 450); // Sesuaikan dengan ukuran yang diinginkan
    jDialog1.setLocationRelativeTo(this); // Supaya muncul di tengah
    jDialog1.setModal(true); // Membuat dialog bersifat modal (opsional)
    jDialog1.setVisible(true); // Menampilkan dialog
    }//GEN-LAST:event_tambah2ActionPerformed

    private void kodeBarang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kodeBarang1ActionPerformed
        // TODO add your handling code here:
    try {
            ResultSet rs = db.ambildata("select * from tb_barang where kode_barang='" + kodeBarang1.getText() + "'");
            if (rs.next()) {
                namaBarang1.setText(rs.getString("nama_barang"));
//                 harga1.setText(rs.getString("harga_jual"));
                stokSistem1.setText(rs.getString("stok"));
            } else {
                JOptionPane.showMessageDialog(null, "Kode Belum terdaftar");
                kodeBarang1.selectAll();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_kodeBarang1ActionPerformed

    private void kartuStok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kartuStok1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kartuStok1ActionPerformed

    private void selisihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selisihActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_selisihActionPerformed

    private void keterangan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keterangan1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_keterangan1ActionPerformed

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
        // TODO add your handling code here:
     try {
        // Validasi input
        if (kodeBarang1.getText().isEmpty() || stokSistem1.getText().isEmpty()
                || kartuStok1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap lengkapi semua field!");
            return;
        }

        // Ambil data dari form
        String kodeBarang = kodeBarang1.getText();
        String namaBarang = namaBarang1.getText(); // Tambahkan ini
        int stokSistem = Integer.parseInt(stokSistem1.getText());
        int stokFisik = Integer.parseInt(kartuStok1.getText());
        String keterangan = keterangan1.getText();
        String tanggal = jtgl.getText();

        String sql;
        if (selectedKodeBarang != null && selectedTanggal != null) {
            // Mode edit - update data yang ada
            sql = "UPDATE tb_stok_opname SET "
                + "nama_barang = '" + namaBarang + "', " // Tambahkan ini
                + "stok_sistem = " + stokSistem + ", "
                + "stok_fisik = " + stokFisik + ", "
                + "keterangan = '" + keterangan + "', "
                + "tanggal = '" + tanggal + "' "
                + "WHERE kode_barang = '" + selectedKodeBarang + "' "
                + "AND tanggal = '" + selectedTanggal + "'";
        } else {
            // Mode tambah - insert data baru
            sql = "INSERT INTO tb_stok_opname (kode_barang, nama_barang, stok_sistem, stok_fisik, tanggal, keterangan) "
                + "VALUES ('" + kodeBarang + "', '" + namaBarang + "', " + stokSistem + ", " + stokFisik
                + ", '" + tanggal + "', '" + keterangan + "')";
        }

        if (db.aksi(sql)) {
            JOptionPane.showMessageDialog(this, "Data stok opname berhasil disimpan!");
            resetForm();
            jDialog1.setVisible(false);
            loadDataToTable();
            
            // Reset selected data setelah simpan
            selectedKodeBarang = null;
            selectedTanggal = null;
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data stok opname!");
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Input stok harus berupa angka!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
    
    }//GEN-LAST:event_simpanActionPerformed

    private void hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusActionPerformed

 if (selectedKodeBarang == null || selectedTanggal == null) {
        JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus terlebih dahulu!");
        return;
    }
    
    int confirm = JOptionPane.showConfirmDialog(
        this, 
        "Apakah Anda yakin ingin menghapus data stok opname untuk barang " + selectedKodeBarang + "?", 
        "Konfirmasi Hapus", 
        JOptionPane.YES_NO_OPTION
    );
    
    if (confirm == JOptionPane.YES_OPTION) {
        String sql = "DELETE FROM tb_stok_opname WHERE kode_barang = '" + selectedKodeBarang 
                    + "' AND tanggal = '" + selectedTanggal + "'";
        
        if (db.aksi(sql)) {
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
            loadDataToTable();
            selectedKodeBarang = null;
            selectedTanggal = null;
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data!");
        }
    }        // TODO add your handling code here:
    }//GEN-LAST:event_hapusActionPerformed

    private void edit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit1ActionPerformed
    if (selectedKodeBarang == null || selectedTanggal == null) {
        JOptionPane.showMessageDialog(this, "Pilih data yang akan diedit terlebih dahulu!");
        return;
    }
    
    try {
        // Ambil data dari database
        String sql = "SELECT * FROM tb_stok_opname WHERE kode_barang = '" + selectedKodeBarang 
                   + "' AND tanggal = '" + selectedTanggal + "'";
        ResultSet rs = db.ambildata(sql);
        
        if (rs.next()) {
            // Isi form dialog dengan data yang dipilih
            kodeBarang1.setText(rs.getString("kode_barang"));
            namaBarang1.setText(rs.getString("nama_barang")); // Pastikan ini ada
            stokSistem1.setText(rs.getString("stok_sistem"));
            kartuStok1.setText(rs.getString("stok_fisik"));
            selisih.setText(rs.getString("selisih"));
            keterangan1.setText(rs.getString("keterangan"));
            jtgl.setText(rs.getString("tanggal"));
            
            // Tampilkan dialog edit
            jDialog1.setSize(727, 463);
            jDialog1.setLocationRelativeTo(this);
            jDialog1.setModal(true);
            jDialog1.setVisible(true);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_edit1ActionPerformed

    private void cari3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cari3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cari3ActionPerformed

    private void cari2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cari2ActionPerformed
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
    }//GEN-LAST:event_cari2ActionPerformed

    private void jcancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcancelActionPerformed
        // TODO add your handling code here:
                jDialog1.dispose();  // Tutup JDialog
        this.setVisible(true); // Pastikan JFrame tetap terlihat
    }//GEN-LAST:event_jcancelActionPerformed

    private void cari1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cari1ActionPerformed
        // TODO add your handling code here:
            searchData();
    }//GEN-LAST:event_cari1ActionPerformed

    private void c1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c1ActionPerformed

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
            java.util.logging.Logger.getLogger(stokOpname.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(stokOpname.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(stokOpname.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(stokOpname.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new stokOpname().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton barang1;
    private javax.swing.JTextField c;
    private javax.swing.JTextField c1;
    private javax.swing.JButton cari1;
    private javax.swing.JButton cari2;
    private javax.swing.JTextField cari3;
    private javax.swing.JDialog daftar_barang;
    private javax.swing.JButton dasbor1;
    private javax.swing.JButton edit1;
    private javax.swing.JButton ekspor;
    private javax.swing.JButton hapus;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton jcancel;
    private javax.swing.JTextField jtgl;
    private javax.swing.JButton jual1;
    private javax.swing.JTextField kartuStok1;
    private javax.swing.JTextField keterangan1;
    private javax.swing.JTextField kodeBarang1;
    private javax.swing.JButton laporan1;
    private javax.swing.JButton logout1;
    private javax.swing.JTextField namaBarang1;
    private javax.swing.JButton retur1;
    private javax.swing.JTextField selisih;
    private javax.swing.JButton simpan;
    private javax.swing.JTextField stokSistem1;
    private javax.swing.JButton tambah2;
    private javax.swing.JButton user1;
    // End of variables declaration//GEN-END:variables
}
