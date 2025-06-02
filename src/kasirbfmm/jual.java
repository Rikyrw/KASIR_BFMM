/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kasirbfmm;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
/**
 *
 * @author Dhimas Ananta
 */
public class jual extends javax.swing.JFrame {
    databasee db = new databasee();
    DefaultTableModel model = new DefaultTableModel();
        long sumTotal = 0;
    /**
     * Creates new form jual
     */
    public jual() { 
        this.setUndecorated(true);
        initComponents();
        
            tanggal.setEditable(false);
            noTransaksi1.setEditable(false);
        
                //tampilkan tanggal transaksi
        tglskrg();
                //tampilkan nomor transaksi
        buatnomor();
                //tampilkan tabel
        aturTabel();
         // Set focus ke kodeBarang2 saat form dibuka
    kodeBarang2.requestFocusInWindow();
    
    
    qty1.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        hitungJumlahHarga();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        hitungJumlahHarga();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        hitungJumlahHarga();
    }
});
    
    
    bayar1.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        hitungKembalian();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        hitungKembalian();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        hitungKembalian();
    }
});
    
            kodeBarang2.getDocument().addDocumentListener(new DocumentListener() {
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
        // Inisialisasi tabel daftar barang
        initTabelDaftarBarang();

        // Event listener untuk pencarian
        cari1.getDocument().addDocumentListener(new DocumentListener() {
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
    
    }
    
    
    // Tambahkan di class jual
    private void loadDataBarang(String keyword) {
        try {
            DefaultTableModel modelBarang = (DefaultTableModel) jTable2.getModel();
            modelBarang.setRowCount(0); // Kosongkan tabel

            String sql = "SELECT kode_barang, nama_barang, harga_jual, stok FROM tb_barang";
            if (keyword != null && !keyword.isEmpty()) {
                sql += " WHERE nama_barang LIKE '%" + keyword + "%' OR kode_barang LIKE '%" + keyword + "%'";
            }

            ResultSet rs = db.ambildata(sql);
            while (rs.next()) {
                modelBarang.addRow(new Object[]{
                    rs.getString("kode_barang"),
                    rs.getString("nama_barang"),
                    rs.getInt("harga_jual"),
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
        modelBarang.addColumn("Harga Jual");
        modelBarang.addColumn("Stok");
        jTable2.setModel(modelBarang);

        // Atur lebar kolom
        jTable2.getColumnModel().getColumn(0).setPreferredWidth(150);
        jTable2.getColumnModel().getColumn(1).setPreferredWidth(300);
        jTable2.getColumnModel().getColumn(2).setPreferredWidth(100);
        jTable2.getColumnModel().getColumn(3).setPreferredWidth(50);

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
            String harga = jTable2.getValueAt(selectedRow, 2).toString();
            String stok = jTable2.getValueAt(selectedRow, 3).toString();

            // Isi ke form jual
            kodeBarang2.setText(kode);
            namaBarang.setText(nama);
            harga1.setText(harga);
            stok1.setText(stok);

            // Fokus ke qty
            qty1.requestFocus();

            // Tutup dialog daftar barang
            daftar_barang.dispose();

            // Otomatis hitung jika qty sudah diisi
            if (!qty1.getText().isEmpty()) {
                hitungJumlahHarga();
            }
        }
    }
    
    
       private void cariBarang() { 
    String kode = kodeBarang2.getText(); 
    if (kode.length() >= 2) { // Mulai cari setelah 2 karakter diinput 
        try { 
            ResultSet rs = db.ambildata( 
                    "SELECT nama_barang, stok FROM tb_barang WHERE kode_barang LIKE '" + kode + "%' LIMIT 1"); 
            if (rs.next()) { 
                namaBarang.setText(rs.getString("nama_barang")); 
                stok1.setText(rs.getString("stok")); // Tambahan untuk mengisi stok
                 
            } else { 
                namaBarang.setText(""); 
                stok1.setText(""); // Kosongkan stok jika tidak ditemukan
                 
            } 
            rs.close(); 
        } catch (Exception e) { 
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); 
        } 
    } else {
        // Kosongkan field jika input kurang dari 2 karakter
        namaBarang.setText("");
        stok1.setText("");
    }
}
    
    
    // Method untuk menghitung kembalian
    private void hitungKembalian() {
        try {
            if (!bayar1.getText().isEmpty() && !total1.getText().isEmpty()) {
                int bayar = Integer.parseInt(bayar1.getText());
                int total = Integer.parseInt(total1.getText());

                if (bayar < total) {
                    kembalian1.setText("Uang kurang!");
                } else {
                    int kembalian = bayar - total;
                    kembalian1.setText(String.valueOf(kembalian));
                }
            }
        } catch (NumberFormatException ex) {
            kembalian1.setText("Input tidak valid");
        }
    }
    
    
    
    // Method untuk menghitung jumlah harga
private void hitungJumlahHarga() {
    try {
        if (!qty1.getText().isEmpty() && !harga1.getText().isEmpty()) {
            int qty = Integer.parseInt(qty1.getText());
            int harga = Integer.parseInt(harga1.getText());
            int stok = Integer.parseInt(stok1.getText());
            
            if (qty > stok) {
                JOptionPane.showMessageDialog(null, "Stok tidak mencukupi! Stok tersedia: " + stok);
                qty1.setText("");
                return;
            }
            
            int total = qty * harga;
            jumlahHarga1.setText(String.valueOf(total));
        }
    } catch (NumberFormatException ex) {
        // Biarkan kosong jika input tidak valid
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
    tanggal.setText(displayFormat.format(skrg));
}

 public void buatnomor(){
        try {
            ResultSet rs = db.ambildata("Select no_transaksi as auto from tb_jual ORDER by no_transaksi desc");
            
            if (rs.next()){
                int no_urut = Integer.parseInt(rs.getString("auto")) + 1;
                noTransaksi1.setText(Integer.toString(no_urut));
            } else {
                int not_t = 1;
                noTransaksi1.setText(Integer.toString(not_t));
            }
            rs.close();
        } catch (Exception e) {
        }
    }

    // Tambahkan method baru untuk mendapatkan tanggal SQL
    public String getTanggalSQL() {
        Date skrg = new Date();
        SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sqlFormat.format(skrg);
}
    
    //perintah membuat judul tabel pada form transaksi jual
        
       public void aturTabel(){
    model.addColumn("KODE BARANG");
    model.addColumn("NAMA BARANG");
    model.addColumn("HARGA BARANG");
    model.addColumn("JUMLAH BARANG");
    model.addColumn("BARCODE"); // Kolom tambahan untuk barcode
    jTable1.setModel(model);

    }
        
        private void isianBersih(){
        kodeBarang2.setText("");
        namaBarang.setText("");
        stok1.setText("");
        harga1.setText("");
        qty1.setText("");
        jumlahHarga1.setText("");
    }
        
    private String formatCurrency(int amount) {
    return String.format("%,d", amount).replace(",", ".");
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        daftar_barang = new javax.swing.JDialog();
        cari1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        barang = new javax.swing.JButton();
        dasbor1 = new javax.swing.JButton();
        barang1 = new javax.swing.JButton();
        stokOpname = new javax.swing.JButton();
        user = new javax.swing.JButton();
        laporan = new javax.swing.JButton();
        logout1 = new javax.swing.JButton();
        simpan = new javax.swing.JButton();
        hapus1 = new javax.swing.JButton();
        tambah2 = new javax.swing.JButton();
        cetak1 = new javax.swing.JButton();
        tanggal = new javax.swing.JTextField();
        noTransaksi1 = new javax.swing.JTextField();
        namaBarang = new javax.swing.JTextField();
        kembalian1 = new javax.swing.JTextField();
        kodeBarang2 = new javax.swing.JTextField();
        harga1 = new javax.swing.JTextField();
        stok1 = new javax.swing.JTextField();
        qty1 = new javax.swing.JTextField();
        jumlahHarga1 = new javax.swing.JTextField();
        total1 = new javax.swing.JTextField();
        Jcari2 = new javax.swing.JButton();
        bayar1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        retur1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        daftar_barang.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cari1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cari1ActionPerformed(evt);
            }
        });
        daftar_barang.getContentPane().add(cari1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 1150, -1));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Kode barang", "Nama barang", "Harga jual", "Stok"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setColumnSelectionAllowed(true);
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setResizable(false);
            jTable2.getColumnModel().getColumn(1).setResizable(false);
            jTable2.getColumnModel().getColumn(2).setResizable(false);
            jTable2.getColumnModel().getColumn(3).setResizable(false);
        }

        daftar_barang.getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 1160, 520));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/cari_data.jpg"))); // NOI18N
        daftar_barang.getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 760));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        barang.setBorderPainted(false);
        barang.setContentAreaFilled(false);
        barang.setFocusPainted(false);
        getContentPane().add(barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 360, 120, 40));

        dasbor1.setBorderPainted(false);
        dasbor1.setContentAreaFilled(false);
        dasbor1.setFocusPainted(false);
        dasbor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dasbor1ActionPerformed(evt);
            }
        });
        getContentPane().add(dasbor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 110, 20));

        barang1.setBorderPainted(false);
        barang1.setContentAreaFilled(false);
        barang1.setFocusPainted(false);
        barang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barang1ActionPerformed(evt);
            }
        });
        getContentPane().add(barang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 110, 20));

        stokOpname.setBorderPainted(false);
        stokOpname.setContentAreaFilled(false);
        stokOpname.setFocusPainted(false);
        stokOpname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stokOpnameActionPerformed(evt);
            }
        });
        getContentPane().add(stokOpname, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, 110, 20));

        user.setBorderPainted(false);
        user.setContentAreaFilled(false);
        user.setFocusPainted(false);
        user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userActionPerformed(evt);
            }
        });
        getContentPane().add(user, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 390, 80, 30));

        laporan.setBorderPainted(false);
        laporan.setContentAreaFilled(false);
        laporan.setFocusPainted(false);
        laporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laporanActionPerformed(evt);
            }
        });
        getContentPane().add(laporan, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 110, 20));

        logout1.setBorderPainted(false);
        logout1.setContentAreaFilled(false);
        logout1.setFocusPainted(false);
        logout1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout1ActionPerformed(evt);
            }
        });
        getContentPane().add(logout1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 690, 130, 30));

        simpan.setBorderPainted(false);
        simpan.setContentAreaFilled(false);
        simpan.setFocusPainted(false);
        simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanActionPerformed(evt);
            }
        });
        getContentPane().add(simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 680, 110, 40));

        hapus1.setBorderPainted(false);
        hapus1.setContentAreaFilled(false);
        hapus1.setFocusPainted(false);
        hapus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapus1ActionPerformed(evt);
            }
        });
        getContentPane().add(hapus1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 360, 130, 40));

        tambah2.setBorderPainted(false);
        tambah2.setContentAreaFilled(false);
        tambah2.setFocusPainted(false);
        tambah2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambah2ActionPerformed(evt);
            }
        });
        getContentPane().add(tambah2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 360, 130, 40));

        cetak1.setBorderPainted(false);
        cetak1.setContentAreaFilled(false);
        cetak1.setFocusPainted(false);
        cetak1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetak1ActionPerformed(evt);
            }
        });
        getContentPane().add(cetak1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 680, 130, 40));

        tanggal.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        getContentPane().add(tanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 110, 110, 20));

        noTransaksi1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        getContentPane().add(noTransaksi1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 110, 110, 20));

        namaBarang.setBackground(new java.awt.Color(255, 255, 255));
        namaBarang.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        getContentPane().add(namaBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 210, 450, 30));

        kembalian1.setBackground(new java.awt.Color(255, 255, 255));
        kembalian1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        getContentPane().add(kembalian1, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 690, 100, 30));

        kodeBarang2.setBackground(new java.awt.Color(255, 255, 255));
        kodeBarang2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        kodeBarang2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kodeBarang2ActionPerformed(evt);
            }
        });
        getContentPane().add(kodeBarang2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 209, 340, 30));

        harga1.setBackground(new java.awt.Color(255, 255, 255));
        harga1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        getContentPane().add(harga1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 300, 190, 30));

        stok1.setBackground(new java.awt.Color(255, 255, 255));
        stok1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        getContentPane().add(stok1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 300, 190, 30));

        qty1.setBackground(new java.awt.Color(255, 255, 255));
        qty1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        qty1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qty1ActionPerformed(evt);
            }
        });
        getContentPane().add(qty1, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 300, 190, 30));

        jumlahHarga1.setBackground(new java.awt.Color(255, 255, 255));
        jumlahHarga1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        getContentPane().add(jumlahHarga1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 300, 190, 20));

        total1.setBackground(new java.awt.Color(255, 255, 255));
        total1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        getContentPane().add(total1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 690, 100, 30));

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
        getContentPane().add(Jcari2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 210, 90, 30));

        bayar1.setBackground(new java.awt.Color(255, 255, 255));
        bayar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        bayar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayar1ActionPerformed(evt);
            }
        });
        getContentPane().add(bayar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 690, 100, 30));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode Barang", "Nama Barang", "Jumlah Barang", "Harga Barang", "Barcode"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(263, 422, 1020, 230));

        retur1.setBorderPainted(false);
        retur1.setContentAreaFilled(false);
        retur1.setFocusPainted(false);
        retur1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retur1ActionPerformed(evt);
            }
        });
        getContentPane().add(retur1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 110, 20));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/jual_1.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void barang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barang1ActionPerformed
        barang barang = new barang();
        barang.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_barang1ActionPerformed

    private void dasbor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dasbor1ActionPerformed
        dashboard dashboard = new dashboard();
        dashboard.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_dasbor1ActionPerformed

    private void laporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_laporanActionPerformed
        laporanPembelian laporanPembelian = new laporanPembelian();
        laporanPembelian.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_laporanActionPerformed

    private void retur1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retur1ActionPerformed
        retur retur = new retur();
        retur.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_retur1ActionPerformed

    private void stokOpnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stokOpnameActionPerformed
        stokOpname stokOpname = new stokOpname();
        stokOpname.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_stokOpnameActionPerformed

    private void userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userActionPerformed
        user user = new user();
        user.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_userActionPerformed

    private void logout1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout1ActionPerformed
        login dashboard = new login();
        dashboard.setVisible(true);
        System.out.println("github perubahan");
        this.dispose();
    }//GEN-LAST:event_logout1ActionPerformed

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
       sumTotal = 0;
    int row = jTable1.getRowCount();
    boolean isDiskon = false;
    boolean isEsKrimGratis = false;

    try {
        // Validasi input
        if (noTransaksi1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nomor transaksi tidak boleh kosong!");
            return;
        }

        if (row == 0) {
            JOptionPane.showMessageDialog(null, "Tabel transaksi kosong, tidak ada data untuk disimpan!");
            return;
        }

        // Periksa diskon dan promo
        LocalDate today = LocalDate.now();
        if (today.getDayOfMonth() == today.getMonthValue()) {
            isDiskon = true;
        }
        if (today.getDayOfWeek() == DayOfWeek.FRIDAY) {
            isEsKrimGratis = true;
        }

        // Hitung total harga
        int totalHarga = Integer.parseInt(total1.getText());
        if (isDiskon) {
            totalHarga /= 2;
            JOptionPane.showMessageDialog(null, "Diskon 50% diterapkan! Total setelah diskon: " + totalHarga);
        }

        // Dapatkan id_user dari session
        int id_user = 1; // Ganti dengan cara mendapatkan id_user yang login

        // Simpan data transaksi ke tb_jual
        String sqlTransaksi = "INSERT INTO tb_jual (no_transaksi, id_user, tanggal, qty, total, bayar) VALUES (" + 
                "'" + noTransaksi1.getText() + "', " + 
                "'" + id_user + "', " + 
                "'" + getTanggalSQL() + "', " + 
                "'" + row + "', " + 
                "'" + totalHarga + "', " + 
                "'" + bayar1.getText() + "')"; 
        db.aksi(sqlTransaksi); 

        // Simpan data ke tabel detail transaksi 
        for (int i = 0; i < row; i++) { 
            String kodeBarang = jTable1.getValueAt(i, 0).toString(); 
            String namaBarang = jTable1.getValueAt(i, 1).toString();
            String hargaBarang = jTable1.getValueAt(i, 2).toString(); 
            String jumlahBarang = jTable1.getValueAt(i, 3).toString(); 
            String barcode = jTable1.getValueAt(i, 4) != null ? jTable1.getValueAt(i, 4).toString() : null;

            // Ambil data tambahan dari tb_barang
            ResultSet rsBarang = db.ambildata("SELECT harga_beli, varian FROM tb_barang WHERE kode_barang = '" + kodeBarang + "'");
            int hargaBeli = 0;
            String varian = "-";
            if (rsBarang.next()) {
                hargaBeli = rsBarang.getInt("harga_beli");
                varian = rsBarang.getString("varian");
            }
            rsBarang.close();

            // Hitung total harga per item
            double totalHargaItem = Double.parseDouble(jumlahBarang) * Double.parseDouble(hargaBarang);

            // Query insert detail transaksi dengan barcode
            String sqlDetail = "INSERT INTO detail_transaksijual " +
                    "(no_transaksi, kode_barang, nama_barang, jumlah_barang, harga_barang, harga_beli, varian, total, barcode) VALUES (" + 
                    "'" + noTransaksi1.getText() + "', " + 
                    "'" + kodeBarang + "', " + 
                    "'" + namaBarang + "', " + 
                    "'" + jumlahBarang + "', " + 
                    "'" + hargaBarang + "', " +
                    "'" + hargaBeli + "', " +
                    "'" + varian + "', " +
                    "'" + totalHargaItem + "', " +
                    (barcode != null ? "'" + barcode + "'" : "NULL") + ")";
            db.aksi(sqlDetail); 

            // Update stok barang 
            String sqlUpdate = "UPDATE tb_barang SET stok = stok - " + jumlahBarang + 
                    " WHERE kode_barang = '" + kodeBarang + "'"; 
            db.aksi(sqlUpdate); 
        }

        // Promo es krim
        if (isEsKrimGratis) {
            JOptionPane.showMessageDialog(null, "Selamat! Anda mendapatkan es krim gratis karena hari ini Jumat!");
        }

        JOptionPane.showMessageDialog(null, "Data telah berhasil disimpan!");

        // Reset form dan tabel
        model.setRowCount(0);
        buatnomor();
        isianBersih();
        sumTotal = 0;

    } catch (SQLException ex) {
        Logger.getLogger(jual.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan database: " + ex.getMessage());
    } catch (Exception ex) {
        Logger.getLogger(jual.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage());
    }
    }//GEN-LAST:event_simpanActionPerformed

    private void bayar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayar1ActionPerformed
        // TODO add your handling code here:
        int b1, b2, kembalian, bayar = Integer.parseInt(bayar1.getText());
        b1 = Integer.parseInt(bayar1.getText());
        b2 = Integer.parseInt(total1.getText());
        kembalian = b1 - b2;
        kembalian1.setText(String.valueOf(kembalian));
    }//GEN-LAST:event_bayar1ActionPerformed

    private void kodeBarang2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kodeBarang2ActionPerformed
        try {
        String input = kodeBarang2.getText().trim();
        ResultSet rs;
        
        // Cari barang berdasarkan kode atau barcode
        if (input.matches("\\d+")) { // Jika input adalah angka (barcode)
            rs = db.ambildata("SELECT * FROM tb_barang WHERE barcode = '" + input + "'");
        } else { // Jika input adalah kode barang
            rs = db.ambildata("SELECT * FROM tb_barang WHERE kode_barang = '" + input + "'");
        }
        
        if (rs.next()) {
            // Simpan kode_barang yang sebenarnya, bukan barcode
            kodeBarang2.setText(rs.getString("kode_barang"));
            namaBarang.setText(rs.getString("nama_barang"));
            harga1.setText(rs.getString("harga_jual"));
            stok1.setText(rs.getString("stok"));
            qty1.requestFocus();
        } else {
            JOptionPane.showMessageDialog(null, "Kode/Barcode belum terdaftar");
            kodeBarang2.selectAll();
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
            // Pindah focus ke tombol tambah setelah enter di qty
    tambah2.requestFocus();
    }//GEN-LAST:event_kodeBarang2ActionPerformed

    private void qty1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qty1ActionPerformed
        // TODO add your handling code here:
    try {
        int stok_barang = Integer.parseInt(stok1.getText());
        int qty = Integer.parseInt(qty1.getText());
        
        // Validasi stok
        if (qty <= 0) {
            JOptionPane.showMessageDialog(null, "Jumlah tidak boleh nol atau negatif!");
            qty1.setText("");
            qty1.requestFocus();
            return;
        }
        
        if (qty > stok_barang) {
            JOptionPane.showMessageDialog(null, "Maaf, stok tidak mencukupi!\nStok tersedia: " + stok_barang);
            qty1.setText("");
            qty1.requestFocus();
            return;
        }
        
        // Hitung total harga jika validasi berhasil
        int harga = Integer.parseInt(harga1.getText());
        int total = harga * qty;
        jumlahHarga1.setText(String.valueOf(total));
        tambah2.requestFocus();
        
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Input harus berupa angka!");
        qty1.setText("");
        qty1.requestFocus();
    }
    }//GEN-LAST:event_qty1ActionPerformed

    private void tambah2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambah2ActionPerformed
     // TODO add your handling code here:
    try {
        // Validasi stok
        int stok_barang = Integer.parseInt(stok1.getText());
        if (stok_barang < 1) {
            JOptionPane.showMessageDialog(null, "Maaf, stok sudah mencapai stok minimum!");
            return;
        }
        
        long qyt = Long.parseLong(qty1.getText());
        long hrg = Long.parseLong(harga1.getText());
        long totalItem = hrg * qyt;
        String kodeBarang = kodeBarang2.getText();
        
        // Cek apakah barang sudah ada di tabel
        boolean barangSudahAda = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(kodeBarang)) {
                // Jika barang sudah ada, update jumlahnya
                long jumlahLama = Long.parseLong(model.getValueAt(i, 3).toString());
                long jumlahBaru = jumlahLama + qyt;
                
                // Validasi stok untuk jumlah baru
                if (jumlahBaru > stok_barang) {
                    JOptionPane.showMessageDialog(null, "Maaf, stok tidak mencukupi!\nStok tersedia: " + stok_barang);
                    return;
                }
                
                model.setValueAt(String.valueOf(jumlahBaru), i, 3);
                barangSudahAda = true;
                break;
            }
        }
        
        // Jika barang belum ada di tabel, tambahkan baris baru
        if (!barangSudahAda) {
            // Ambil barcode untuk ditampilkan di tabel
            ResultSet rs = db.ambildata("SELECT barcode FROM tb_barang WHERE kode_barang = '" + kodeBarang + "'");
            String barcode = "";
            if (rs.next()) {
                barcode = rs.getString("barcode");
            }
            
            model.addRow(new Object[]{
                kodeBarang, 
                namaBarang.getText(), 
                harga1.getText(), 
                qty1.getText(),
                barcode
            });
        }
        
        // Update total
        sumTotal += totalItem;
        total1.setText(String.valueOf(sumTotal));
        
        // Konfirmasi tambah
        int n = JOptionPane.showConfirmDialog(this, "Tambah lagi?", "Konfirmasi", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (n == JOptionPane.YES_OPTION) {
            isianBersih();
            kodeBarang2.requestFocus();
        } else {
            isianBersih();
            bayar1.requestFocus();
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_tambah2ActionPerformed

    private void cetak1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetak1ActionPerformed
try {
        // Check if there's a transaction to print
        if (noTransaksi1.getText().isEmpty() || jTable1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Tidak ada transaksi untuk dicetak.");
            return;
        }

        // Calculate total price after discounts
        int totalHarga = Integer.parseInt(total1.getText());
        int diskon = 0;
        boolean isDiskon = false;
        boolean isFreeIceCream = false;

        // Check for "pretty date" discount (day = month)
        LocalDate today = LocalDate.now();
        if (today.getDayOfMonth() == today.getMonthValue()) {
            isDiskon = true;
            diskon = totalHarga / 2; // 50% discount
            totalHarga -= diskon;
        }

        // Check for Friday free ice cream
        if (today.getDayOfWeek() == DayOfWeek.FRIDAY) {
            isFreeIceCream = true;
        }

        // Format time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String tanggalStr = dateFormat.format(new Date());
        String waktuStr = timeFormat.format(new Date());

        // Build receipt with thermal printer formatting
        StringBuilder nota = new StringBuilder();
        
        // ESC/POS commands for printer formatting
        nota.append((char)27).append((char)97).append((char)1); // Center align
        nota.append((char)27).append((char)33).append((char)16); // Double height
        
        // Store header
        nota.append("BUNDA FIENS MART\n");
        
        
        // Reset to normal font and left align
        nota.append("--------------------------------\n");
        nota.append((char)27).append((char)33).append((char)0);
        nota.append((char)27).append((char)97).append((char)0);
        
        // Store information
        nota.append("Jl. Jombang no 29, Ajung Jember Utara\n");
        nota.append("Telp: 081234567890\n");
        nota.append("Kasir: Aditya Fadni Athaullah\n");
        
        // Transaction details
        nota.append("--------------------------------\n");
        nota.append(String.format("%-12s: %s\n", "No Transaksi", noTransaksi1.getText()));
        nota.append(String.format("%-12s: %s-%s\n", "Waktu",tanggalStr, waktuStr));
        

        // Item list
        nota.append("--------------------------------\n");
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            String barang = jTable1.getValueAt(i, 1).toString();
            int jumlah = Integer.parseInt(jTable1.getValueAt(i, 3).toString());
            int harga = Integer.parseInt(jTable1.getValueAt(i, 2).toString());
            int total = jumlah * harga;

            nota.append(barang).append("\n");
            nota.append(String.format("%5d  %8s  %8s\n", 
                jumlah, 
                formatCurrency(harga), 
                formatCurrency(total)));
        }

        // Payment summary
//        nota.append("\n");
        nota.append("--------------------------------\n");
        nota.append(String.format("%-12s: %12s\n", "HARGA JUAL", formatCurrency(Integer.parseInt(total1.getText()))));
        
        if (isDiskon) {
            nota.append(String.format("%-12s: %12s\n", "DISKON 50%", formatCurrency(diskon)));
        }
        
        nota.append("--------------------------------\n");
        nota.append(String.format("%-12s: %12s\n", "TOTAL", formatCurrency(totalHarga)));
        nota.append(String.format("%-12s: %12s\n", "TUNAI", formatCurrency(Integer.parseInt(bayar1.getText()))));
        
        int kembalian = Integer.parseInt(bayar1.getText()) - totalHarga;
        nota.append(String.format("%-12s: %12s\n", "KEMBALI", formatCurrency(kembalian)));
        nota.append("--------------------------------\n\n");
        
        // Promo information
        if (isDiskon) {
            nota.append("  ANDA HEMAT: ").append(formatCurrency(diskon)).append("\n\n");
        }
        
        if (isFreeIceCream) {
            nota.append("  FREE ICE CREAM (JUMAT BERKAH)\n\n");
        }
        
        // Footer
        nota.append("=== LAYANAN KONSUMEN ===\n");
        nota.append("SMS/WA : 081234567890\n");
        nota.append("Email  : adityfn@gmail.com\n\n");

        // Paper cut command
        nota.append((char)29).append((char)86).append((char)65).append((char)3);

        // Print directly to thermal printer
        try {
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            if (printService != null) {
                DocPrintJob job = printService.createPrintJob();
                byte[] bytes = nota.toString().getBytes("CP437");
                Doc doc = new SimpleDoc(bytes, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
                
                int printConfirm = JOptionPane.showConfirmDialog(null, "Cetak nota sekarang?", "Konfirmasi Cetak", JOptionPane.YES_NO_OPTION);
                if (printConfirm == JOptionPane.YES_OPTION) {
                    job.print(doc, null);
                    JOptionPane.showMessageDialog(null, "Nota berhasil dicetak.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Tidak ada printer yang terdeteksi.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mencetak: " + e.getMessage());
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mencetak: " + e.getMessage());
    }
    }//GEN-LAST:event_cetak1ActionPerformed

    private void hapus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapus1ActionPerformed
       // TODO add your handling code here:
    // Mendapatkan baris yang dipilih di tabel
        int selectedRow = jTable1.getSelectedRow();

        // Validasi apakah ada baris yang dipilih
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Pilih baris yang akan dihapus terlebih dahulu!");
            return;
        }

        // Konfirmasi penghapusan
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus baris ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Mendapatkan nilai total dari baris yang akan dihapus
                int harga = Integer.parseInt(jTable1.getValueAt(selectedRow, 2).toString());
                int qty = Integer.parseInt(jTable1.getValueAt(selectedRow, 3).toString());
                int totalRow = harga * qty;

                // Mengurangi totalRow dari sumTotal
                sumTotal -= totalRow;
                total1.setText(String.valueOf(sumTotal));

                // Menghapus baris dari model tabel
                model.removeRow(selectedRow);

                // Reset kembalian jika ada
                if (!bayar1.getText().isEmpty() && !total1.getText().isEmpty()) {
                    int bayar = Integer.parseInt(bayar1.getText());
                    int total = Integer.parseInt(total1.getText());
                    kembalian1.setText(String.valueOf(bayar - total));
                }

                JOptionPane.showMessageDialog(null, "Baris berhasil dihapus");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Gagal menghapus baris: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_hapus1ActionPerformed

    private void Jcari2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Jcari2ActionPerformed
    // Muat ulang data barang
    loadDataBarang(null);
    
    // Kosongkan field pencarian
    cari1.setText("");
    
    // Tampilkan dialog
    daftar_barang.setSize(1370, 768);
    daftar_barang.setLocationRelativeTo(this);
    daftar_barang.setModal(true);
    daftar_barang.setVisible(true);
    // TODO add your handling code here:
                // TODO add your handling code here:
//    daftar_barang.setSize(1370, 768); // Sesuaikan dengan ukuran yang diinginkan
//    daftar_barang.setLocationRelativeTo(this); // Supaya muncul di tengah
//    daftar_barang.setModal(true); // Membuat dialog bersifat modal (opsional)
//    daftar_barang.setVisible(true); // Menampilkan dialog
    }//GEN-LAST:event_Jcari2ActionPerformed

    private void cari1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cari1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cari1ActionPerformed

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
            java.util.logging.Logger.getLogger(jual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new jual().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Jcari2;
    private javax.swing.JButton barang;
    private javax.swing.JButton barang1;
    private javax.swing.JTextField bayar1;
    private javax.swing.JTextField cari1;
    private javax.swing.JButton cetak1;
    private javax.swing.JDialog daftar_barang;
    private javax.swing.JButton dasbor1;
    private javax.swing.JButton hapus1;
    private javax.swing.JTextField harga1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jumlahHarga1;
    private javax.swing.JTextField kembalian1;
    private javax.swing.JTextField kodeBarang2;
    private javax.swing.JButton laporan;
    private javax.swing.JButton logout1;
    private javax.swing.JTextField namaBarang;
    private javax.swing.JTextField noTransaksi1;
    private javax.swing.JTextField qty1;
    private javax.swing.JButton retur1;
    private javax.swing.JButton simpan;
    private javax.swing.JTextField stok1;
    private javax.swing.JButton stokOpname;
    private javax.swing.JButton tambah2;
    private javax.swing.JTextField tanggal;
    private javax.swing.JTextField total1;
    private javax.swing.JButton user;
    // End of variables declaration//GEN-END:variables
}
