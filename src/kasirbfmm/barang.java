package kasirbfmm;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;



public class barang extends javax.swing.JFrame {
    
    private Connection con;
    private Statement st;
    private ResultSet RSsupplier;
    private String sql = "";
    
  DefaultTableModel model = new DefaultTableModel();
  
  databasee db = new databasee ();
    public barang() {
    this.setUndecorated(true);
    initComponents();

    db.koneksiDB(); // <-- koneksi database harus dipanggil dulu sebelum viewdata()!

    model = new DefaultTableModel();
    model.addColumn("Kode");
    model.addColumn("Nama");
    model.addColumn("Varian");
    model.addColumn("Berat");
    model.addColumn("Stok");
    model.addColumn("Exp");
    model.addColumn("Harga Jual");
    model.addColumn("Harga Beli");
    model.addColumn("Barcode");
    model.addColumn("Kategori");
    model.addColumn("Pemasok");
    model.addColumn("Tanggal");
    jTable1.setModel(model);

    viewdata(); // baru tampilkan data

    isianBersih();
    setTanggalOtomatis();
    tanggal.setEditable(false);
    tanggal1.setEditable(false);
        
        jcancel.setOpaque(false);
        jcancel.setContentAreaFilled(false);
        jcancel.setBorderPainted(false);
        jcancel.setFocusPainted(false);
        jcancel.setForeground(Color.WHITE);

        simpan.setOpaque(false);
        simpan.setContentAreaFilled(false);
        simpan.setFocusPainted(false);

        jnm_barang.setOpaque(false);
        jnm_barang.setBackground(new Color(0, 0, 0, 0));
        jnm_barang1.setOpaque(false);
        jnm_barang1.setBackground(new Color(0, 0, 0, 0));
        jkd_barang.setOpaque(false);
        jkd_barang.setBackground(new Color(0, 0, 0, 0));
        jkd_barang1.setOpaque(false);
        jkd_barang1.setBackground(new Color(0, 0, 0, 0));
        jstok.setOpaque(false);
        jstok.setBackground(new Color(0, 0, 0, 0));
        jstok1.setOpaque(false);
        jstok1.setBackground(new Color(0, 0, 0, 0));
        jhrg_jual.setOpaque(false);
        jhrg_jual.setBackground(new Color(0, 0, 0, 0));
        jhrg_jual1.setOpaque(false);
        jhrg_jual1.setBackground(new Color(0, 0, 0, 0));
        jhrg_beli.setOpaque(false);
        jhrg_beli.setBackground(new Color(0, 0, 0, 0));
        jhrg_beli1.setOpaque(false);
        jhrg_beli1.setBackground(new Color(0, 0, 0, 0));
        jnm_pemasok.setOpaque(false);
        jnm_pemasok.setBackground(new Color(0, 0, 0, 0));
        jnm_pemasok1.setOpaque(false);
        jnm_pemasok1.setBackground(new Color(0, 0, 0, 0));
        jvarian.setOpaque(false);
        jvarian.setBackground(new Color(0, 0, 0, 0));
        jvarian1.setOpaque(false);
        jvarian1.setBackground(new Color(0, 0, 0, 0));
        jberat.setOpaque(false);
        jberat.setBackground(new Color(0, 0, 0, 0));
        jberat1.setOpaque(false);
        jberat1.setBackground(new Color(0, 0, 0, 0));
        jtgl_exp.setOpaque(false);
        jtgl_exp.setBackground(new Color(0, 0, 0, 0));
        jexp1.setOpaque(false);
        jexp1.setBackground(new Color(0, 0, 0, 0));
        jbarcode.setOpaque(false);
        jbarcode.setBackground(new Color(0, 0, 0, 0));   
        
        jbarcode1.setOpaque(false);
        jbarcode1.setBackground(new Color(0, 0, 0, 0));   
        
        b.setEditable(false); 
        b.setOpaque(false); 
        b.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        b.setFocusable(false); // Biar nggak bisa di-klik
        
        c.setEditable(false); 
        c.setOpaque(false); 
        c.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        c.setFocusable(false); // Biar nggak bisa di-klik
        
        db.koneksiDB();
        
        jtgl_exp.setBorder(BorderFactory.createEmptyBorder());
        jtgl_exp.setOpaque(false);
        jtgl_exp.getDateEditor().getUiComponent().setBackground(new Color(0, 0, 0, 0));
        jtgl_exp.getDateEditor().getUiComponent().setOpaque(false);
        jtgl_exp.getDateEditor().getUiComponent().setBorder(BorderFactory.createEmptyBorder());
        jTable1.setFocusable(false); // Biar nggak bisa di-klik
        
      // Di constructor atau method inisialisasi, tambahkan DocumentListener untuk jcari
jcari.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        searchByName();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        searchByName();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        searchByName();
    }
});
        
      
    }
    
    private void tampilData(){
    
    model = new DefaultTableModel();
    model.addColumn("Kode");
    model.addColumn("Nama");
    model.addColumn("Varian");
    model.addColumn("Berat");
    model.addColumn("Stok");
    model.addColumn("Exp");
//    model.addColumn("Tanggal");
    model.addColumn("Harga Jual");
    model.addColumn("Harga Beli");
    model.addColumn("Barcode");
    model.addColumn("Kategori");
    model.addColumn("Pemasok");
    model.addColumn("Tanggal");

    jTable1.setModel(model);
}

     public void viewdata() {
       model.setRowCount(0); // hapus data lama

    try {
        ResultSet rs = db.ambildata("SELECT * FROM tb_barang");
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("kode_barang"),
                rs.getString("nama_barang"),
                rs.getString("varian"),
                rs.getString("berat"),
                rs.getString("stok"),
                rs.getString("exp"),
//                rs.getString("tanggal"),
                rs.getString("harga_jual"),
                rs.getString("harga_beli"),
                rs.getString("barcode"),
                rs.getString("kategori"),
                rs.getString("nama_pemasok"),
                rs.getString("tanggal"),
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal load data: " + e.getMessage());
    }
    
    }
          private void isianBersih(){
            jkd_barang.setText("");
            jnm_barang.setText("");
//            jvarian.setText("");
//            jberat.setText("");
            jhrg_jual.setText("");
//            jexp.setText("");
            jstok.setText("");
//            jhrg_beli.setText("");
//            jbarcode.setText("");
//            jnm_pemasok.setText("");
            jkategori.setSelectedItem("");
           }
    
    public void setTanggalOtomatis() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Format tanggal
    String tanggalSekarang = sdf.format(new Date()); // Ambil tanggal sekarang
    tanggal.setText(tanggalSekarang); // Set ke JTextField (ganti dengan nama variabel JTextField tanggal kamu)
    tanggal1.setText(tanggalSekarang); // Set ke JTextField (ganti dengan nama variabel JTextField tanggal kamu)
}
    private void updateData() {
    try {
         int hargaBeli;
        try {
            hargaBeli = Integer.parseInt(jhrg_beli.getText().replace(",", ""));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Harga Beli harus berupa angka!");
            return;
        }

        String sql = "UPDATE tb_barang SET " +
                "nama_barang = '" + jnm_barang.getText() + "', " +
                "kategori = '" + jkategori.getSelectedItem() + "', " +
                "varian = '" + jvarian.getText() + "', " +
                "berat = '" + jberat.getText() + "', " +
                "stok = '" + jstok.getText() + "', " +
                "exp = '" + new java.sql.Date(jtgl_exp.getDate().getTime()) + "', " +
                "harga_jual = '" + jhrg_jual.getText() + "', " +
                "harga_beli = " + hargaBeli + ", " + // Pastikan harga_beli berupa angka
                "barcode = '" + jbarcode.getText() + "', " +
                "nama_pemasok = '" + jnm_pemasok.getText() + "' " +
                "WHERE kode_barang = '" + jkd_barang.getText() + "'";

        db.aksi(sql);
        JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");
        isianBersih();
        viewdata();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal update data: " + e.getMessage());
    }
}

    private void deleteData() {
         int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus dari tabel!");
        return;
    }

    String kode = model.getValueAt(selectedRow, 0).toString();

    int pilihan = JOptionPane.showConfirmDialog(null, "Yakin mau hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
    if (pilihan == JOptionPane.YES_OPTION) {
        try {
            String sql = "DELETE FROM tb_barang WHERE kode_barang = '" + kode + "'";
            db.aksi(sql);
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
            isianBersih();
            viewdata();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal hapus data: " + e.getMessage());
        }
    }
}
    
    private void updateData1() {
   try {
        // Format tanggal exp (jika ada)
        String expFormatted = "";
        if (jexp1.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            expFormatted = sdf.format(jexp1.getDate());
        }
        
        // Format tanggal hari ini (untuk kolom Tanggal)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());

        String sql = "UPDATE tb_barang SET " +
                "nama_barang = '" + jnm_barang1.getText() + "', " +
                "kategori = '" + jkategori1.getSelectedItem() + "', " +
                "varian = '" + jvarian1.getText() + "', " +
                "berat = '" + jberat1.getText() + "', " +
                "stok = '" + jstok1.getText() + "', " +
                "exp = '" + expFormatted + "', " +
                "harga_jual = '" + jhrg_jual1.getText() + "', " +
                "harga_beli = " + jhrg_beli1.getText() + ", " +
                "barcode = '" + jbarcode1.getText() + "', " +
                "nama_pemasok = '" + jnm_pemasok1.getText() + "', " +
                "Tanggal = '" + currentDate + "' " +  // Hanya tanggal tanpa waktu
                "WHERE kode_barang = '" + jkd_barang1.getText() + "'";

        db.aksi(sql);
        JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");
        viewdata();
        jbarang2.dispose();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal update data: " + e.getMessage());
        e.printStackTrace();
    }
}
    
    
    // Method untuk pencarian berdasarkan nama
private void searchByName() {
    String keyword = jcari.getText().trim();
    
    if (keyword.isEmpty()) {
        viewdata(); // Tampilkan semua data jika kolom pencarian kosong
    } else {
        model.setRowCount(0); // Clear existing data
        
        try {
            // Dapatkan koneksi database
            Connection conn = db.koneksiDB(); // Pastikan memanggil koneksiDB() bukan koneks1DB()
            
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Tidak dapat terhubung ke database");
                return;
            }

            String sql = "SELECT * FROM tb_barang WHERE nama_barang LIKE ?";
            
            // Menggunakan try-with-resources untuk auto-close resources
            try (var pst = conn.prepareStatement(sql)) {
                pst.setString(1, "%" + keyword + "%");
                
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getString("kode_barang"),
                            rs.getString("nama_barang"),
                            rs.getString("varian"),
                            rs.getString("berat"),
                            rs.getString("stok"),
                            rs.getString("exp"),
                            rs.getString("harga_jual"),
                            rs.getString("harga_beli"),
                            rs.getString("barcode"),
                            rs.getString("kategori"),
                            rs.getString("nama_pemasok"),
                            rs.getString("tanggal")
                        });
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error akses database: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbarang1 = new javax.swing.JDialog();
        jcancel = new javax.swing.JButton();
        tanggal = new javax.swing.JTextField();
        jbarcode = new javax.swing.JTextField();
        noTrans = new javax.swing.JTextField();
        jnm_barang = new javax.swing.JTextField();
        jkd_barang = new javax.swing.JTextField();
        jstok = new javax.swing.JTextField();
        jhrg_jual = new javax.swing.JTextField();
        jhrg_beli = new javax.swing.JTextField();
        jnm_pemasok = new javax.swing.JTextField();
        jvarian = new javax.swing.JTextField();
        jberat = new javax.swing.JTextField();
        jkategori = new javax.swing.JComboBox<>();
        c = new javax.swing.JTextField();
        b = new javax.swing.JTextField();
        simpan = new javax.swing.JButton();
        jtgl_exp = new com.toedter.calendar.JDateChooser();
        a = new javax.swing.JLabel();
        jbarang2 = new javax.swing.JDialog();
        jcancel1 = new javax.swing.JButton();
        tanggal1 = new javax.swing.JTextField();
        jbarcode1 = new javax.swing.JTextField();
        noTrans1 = new javax.swing.JTextField();
        jnm_barang1 = new javax.swing.JTextField();
        jkd_barang1 = new javax.swing.JTextField();
        jstok1 = new javax.swing.JTextField();
        jhrg_jual1 = new javax.swing.JTextField();
        jhrg_beli1 = new javax.swing.JTextField();
        jnm_pemasok1 = new javax.swing.JTextField();
        jvarian1 = new javax.swing.JTextField();
        jberat1 = new javax.swing.JTextField();
        simpan1 = new javax.swing.JButton();
        jkategori1 = new javax.swing.JComboBox<>();
        c1 = new javax.swing.JTextField();
        jexp1 = new com.toedter.calendar.JDateChooser();
        b1 = new javax.swing.JTextField();
        a1 = new javax.swing.JLabel();
        jDialog3 = new javax.swing.JDialog();
        kodeBarang1 = new javax.swing.JTextField();
        namaBarang1 = new javax.swing.JTextField();
        stokKeluar1 = new javax.swing.JTextField();
        stokMasuk1 = new javax.swing.JTextField();
        stokAkhir = new javax.swing.JTextField();
        simpan2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tombolstokop = new javax.swing.JButton();
        tombollUser = new javax.swing.JButton();
        tombolLaporan = new javax.swing.JButton();
        tombolLogout1 = new javax.swing.JButton();
        tombolCari1 = new javax.swing.JButton();
        tombolTambah1 = new javax.swing.JButton();
        tombolhapussss = new javax.swing.JButton();
        kategori = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        tombolDasbor2 = new javax.swing.JButton();
        tombolJual1 = new javax.swing.JButton();
        tombolEdit2 = new javax.swing.JButton();
        tombolRetur1 = new javax.swing.JButton();
        tombolKartustokk = new javax.swing.JButton();
        jcari = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        jbarang1.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jcancel.setBackground(new java.awt.Color(255, 255, 255));
        jcancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/foto/Cancel.png"))); // NOI18N
        jcancel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jcancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcancelActionPerformed(evt);
            }
        });
        jbarang1.getContentPane().add(jcancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 50, 40));

        tanggal.setBorder(null);
        tanggal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tanggalActionPerformed(evt);
            }
        });
        jbarang1.getContentPane().add(tanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, 110, 30));

        jbarcode.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jbarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbarcodeActionPerformed(evt);
            }
        });
        jbarang1.getContentPane().add(jbarcode, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 380, 180, 40));

        noTrans.setBorder(null);
        noTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noTransActionPerformed(evt);
            }
        });
        jbarang1.getContentPane().add(noTrans, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 110, 30));

        jnm_barang.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jnm_barang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jnm_barangActionPerformed(evt);
            }
        });
        jbarang1.getContentPane().add(jnm_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 180, 50));

        jkd_barang.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jkd_barang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jkd_barangActionPerformed(evt);
            }
        });
        jbarang1.getContentPane().add(jkd_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, 180, 50));

        jstok.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jstok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jstokActionPerformed(evt);
            }
        });
        jbarang1.getContentPane().add(jstok, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 290, 180, 50));

        jhrg_jual.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jhrg_jual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jhrg_jualActionPerformed(evt);
            }
        });
        jbarang1.getContentPane().add(jhrg_jual, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 370, 180, 50));

        jhrg_beli.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jhrg_beli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jhrg_beliActionPerformed(evt);
            }
        });
        jbarang1.getContentPane().add(jhrg_beli, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 450, 180, 50));

        jnm_pemasok.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jnm_pemasok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jnm_pemasokActionPerformed(evt);
            }
        });
        jbarang1.getContentPane().add(jnm_pemasok, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 530, 180, 50));

        jvarian.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jvarian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jvarianActionPerformed(evt);
            }
        });
        jbarang1.getContentPane().add(jvarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 180, 50));

        jberat.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jberat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jberatActionPerformed(evt);
            }
        });
        jbarang1.getContentPane().add(jberat, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 210, 180, 50));

        jkategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Makanan", "Minuman", "Sembako" }));
        jbarang1.getContentPane().add(jkategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 460, 140, 40));

        c.setBackground(new java.awt.Color(255, 255, 255));
        c.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jbarang1.getContentPane().add(c, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, -20, 160, 90));

        b.setBackground(new java.awt.Color(255, 255, 255));
        b.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jbarang1.getContentPane().add(b, new org.netbeans.lib.awtextra.AbsoluteConstraints(-92, -34, 150, 60));

        simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanActionPerformed(evt);
            }
        });
        jbarang1.getContentPane().add(simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 530, 130, 50));
        jbarang1.getContentPane().add(jtgl_exp, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 300, 180, 30));

        a.setIcon(new javax.swing.ImageIcon(getClass().getResource("/foto/popup edit dan tambah(1).png"))); // NOI18N
        a.setText("jLabel2");
        jbarang1.getContentPane().add(a, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 580, -1));

        jbarang2.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jcancel1.setBackground(new java.awt.Color(255, 255, 255));
        jcancel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/foto/Cancel.png"))); // NOI18N
        jcancel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jcancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcancel1ActionPerformed(evt);
            }
        });
        jbarang2.getContentPane().add(jcancel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 50, 40));

        tanggal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tanggal1ActionPerformed(evt);
            }
        });
        jbarang2.getContentPane().add(tanggal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, 110, -1));

        jbarcode1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jbarcode1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbarcode1ActionPerformed(evt);
            }
        });
        jbarang2.getContentPane().add(jbarcode1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 380, 180, 50));

        noTrans1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noTrans1ActionPerformed(evt);
            }
        });
        jbarang2.getContentPane().add(noTrans1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 110, -1));

        jnm_barang1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jnm_barang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jnm_barang1ActionPerformed(evt);
            }
        });
        jbarang2.getContentPane().add(jnm_barang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 180, 50));

        jkd_barang1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jkd_barang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jkd_barang1ActionPerformed(evt);
            }
        });
        jbarang2.getContentPane().add(jkd_barang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, 180, 50));

        jstok1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jstok1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jstok1ActionPerformed(evt);
            }
        });
        jbarang2.getContentPane().add(jstok1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 300, 160, 30));

        jhrg_jual1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jhrg_jual1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jhrg_jual1ActionPerformed(evt);
            }
        });
        jbarang2.getContentPane().add(jhrg_jual1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 370, 180, 50));

        jhrg_beli1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jhrg_beli1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jhrg_beli1ActionPerformed(evt);
            }
        });
        jbarang2.getContentPane().add(jhrg_beli1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 450, 180, 50));

        jnm_pemasok1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jnm_pemasok1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jnm_pemasok1ActionPerformed(evt);
            }
        });
        jbarang2.getContentPane().add(jnm_pemasok1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 530, 180, 50));

        jvarian1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jvarian1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jvarian1ActionPerformed(evt);
            }
        });
        jbarang2.getContentPane().add(jvarian1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 180, 50));

        jberat1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jberat1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jberat1ActionPerformed(evt);
            }
        });
        jbarang2.getContentPane().add(jberat1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 210, 180, 50));

        simpan1.setBorderPainted(false);
        simpan1.setContentAreaFilled(false);
        simpan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpan1ActionPerformed(evt);
            }
        });
        jbarang2.getContentPane().add(simpan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 530, 130, 50));

        jkategori1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Makanan", "Minuman", "Sembako", " " }));
        jbarang2.getContentPane().add(jkategori1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 460, 140, 40));

        c1.setBackground(new java.awt.Color(255, 255, 255));
        c1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jbarang2.getContentPane().add(c1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, -20, 160, 90));
        jbarang2.getContentPane().add(jexp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 299, 190, 30));

        b1.setBackground(new java.awt.Color(255, 255, 255));
        b1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jbarang2.getContentPane().add(b1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-92, -34, 150, 60));

        a1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/foto/popup edit dan tambah(1).png"))); // NOI18N
        a1.setText("jLabel2");
        jbarang2.getContentPane().add(a1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 580, -1));

        jDialog3.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        kodeBarang1.setBorder(null);
        kodeBarang1.setRequestFocusEnabled(false);
        jDialog3.getContentPane().add(kodeBarang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 180, 20));

        namaBarang1.setBorder(null);
        namaBarang1.setRequestFocusEnabled(false);
        jDialog3.getContentPane().add(namaBarang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, 190, 20));

        stokKeluar1.setBorder(null);
        stokKeluar1.setRequestFocusEnabled(false);
        jDialog3.getContentPane().add(stokKeluar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 130, 20));

        stokMasuk1.setBorder(null);
        stokMasuk1.setRequestFocusEnabled(false);
        jDialog3.getContentPane().add(stokMasuk1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 180, 120, 20));

        stokAkhir.setBorder(null);
        stokAkhir.setRequestFocusEnabled(false);
        jDialog3.getContentPane().add(stokAkhir, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 180, 130, 20));

        simpan2.setBorder(null);
        simpan2.setBorderPainted(false);
        simpan2.setContentAreaFilled(false);
        simpan2.setFocusPainted(false);
        simpan2.setRequestFocusEnabled(false);
        jDialog3.getContentPane().add(simpan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 450, 30));

        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jDialog3.getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, 40, 40));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/kartus stok.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jDialog3.getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 0, 620, 300));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tombolstokop.setBorderPainted(false);
        tombolstokop.setContentAreaFilled(false);
        tombolstokop.setFocusPainted(false);
        tombolstokop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolstokopActionPerformed(evt);
            }
        });
        getContentPane().add(tombolstokop, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 120, 20));

        tombollUser.setBorderPainted(false);
        tombollUser.setContentAreaFilled(false);
        tombollUser.setFocusPainted(false);
        tombollUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombollUserActionPerformed(evt);
            }
        });
        getContentPane().add(tombollUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 90, 20));

        tombolLaporan.setBorderPainted(false);
        tombolLaporan.setContentAreaFilled(false);
        tombolLaporan.setFocusPainted(false);
        tombolLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolLaporanActionPerformed(evt);
            }
        });
        getContentPane().add(tombolLaporan, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 120, 20));

        tombolLogout1.setBorderPainted(false);
        tombolLogout1.setContentAreaFilled(false);
        tombolLogout1.setFocusPainted(false);
        tombolLogout1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolLogout1ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolLogout1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 690, 130, 20));

        tombolCari1.setBorderPainted(false);
        tombolCari1.setContentAreaFilled(false);
        tombolCari1.setFocusPainted(false);
        tombolCari1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolCari1ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolCari1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 100, 40, 40));

        tombolTambah1.setBorderPainted(false);
        tombolTambah1.setContentAreaFilled(false);
        tombolTambah1.setFocusPainted(false);
        tombolTambah1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tombolTambah1MouseClicked(evt);
            }
        });
        tombolTambah1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolTambah1ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolTambah1, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 100, 120, 40));

        tombolhapussss.setBorderPainted(false);
        tombolhapussss.setContentAreaFilled(false);
        tombolhapussss.setFocusPainted(false);
        tombolhapussss.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolhapussssActionPerformed(evt);
            }
        });
        getContentPane().add(tombolhapussss, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 100, 120, 40));

        kategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Makanan", "Minuman", "Sembako" }));
        kategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kategoriActionPerformed(evt);
            }
        });
        getContentPane().add(kategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 100, 110, 40));

        jTable1.setBackground(new java.awt.Color(102, 102, 102));
        jTable1.setForeground(new java.awt.Color(204, 204, 204));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Kode", "Nama", "Varian", "Berat", "Stok", "Exp", "Harga jual", "Harga beli", "Barcode", "Kategori", "Tanggal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setFocusable(false);
        jTable1.setSelectionBackground(new java.awt.Color(204, 204, 204));
        jTable1.setSelectionForeground(new java.awt.Color(76, 52, 98));
        jTable1.setShowGrid(true);
        jTable1.getTableHeader().setReorderingAllowed(false);
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
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setResizable(false);
            jTable1.getColumnModel().getColumn(7).setResizable(false);
            jTable1.getColumnModel().getColumn(8).setResizable(false);
            jTable1.getColumnModel().getColumn(9).setResizable(false);
            jTable1.getColumnModel().getColumn(10).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 180, 1020, 540));

        tombolDasbor2.setBorderPainted(false);
        tombolDasbor2.setContentAreaFilled(false);
        tombolDasbor2.setFocusPainted(false);
        tombolDasbor2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolDasbor2ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolDasbor2, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 150, 120, 20));

        tombolJual1.setBorderPainted(false);
        tombolJual1.setContentAreaFilled(false);
        tombolJual1.setFocusPainted(false);
        tombolJual1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolJual1ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolJual1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 100, 20));

        tombolEdit2.setText(" ");
        tombolEdit2.setBorderPainted(false);
        tombolEdit2.setContentAreaFilled(false);
        tombolEdit2.setFocusPainted(false);
        tombolEdit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolEdit2ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolEdit2, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 100, 120, 40));

        tombolRetur1.setBorderPainted(false);
        tombolRetur1.setContentAreaFilled(false);
        tombolRetur1.setFocusPainted(false);
        tombolRetur1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolRetur1ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolRetur1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 120, 20));

        tombolKartustokk.setBorderPainted(false);
        tombolKartustokk.setContentAreaFilled(false);
        tombolKartustokk.setFocusPainted(false);
        tombolKartustokk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolKartustokkActionPerformed(evt);
            }
        });
        getContentPane().add(tombolKartustokk, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 100, 120, 40));

        jcari.setBorder(null);
        getContentPane().add(jcari, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, 250, 30));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/barangg (1).jpg"))); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void kategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kategoriActionPerformed
//            String selectedCategory = kategori.getSelectedItem().toString();
//    String searchText = jcari.getText().trim();
//    
//    if (searchText.isEmpty()) {
//        // If search field is empty, filter by category only
//        if (selectedCategory.equals("Makanan") || selectedCategory.equals("Minuman") || selectedCategory.equals("Sembako")) {
//            filterByCategory(selectedCategory);
//        } else {
//            viewdata(); // Show all data if no specific category selected
//        }
//    } else {
//        // If search field has text, search with both category and keyword
//        searchWithCategory(selectedCategory, searchText);
//    }
    }//GEN-LAST:event_kategoriActionPerformed

    private void tombolstokopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolstokopActionPerformed
        stokOpname stokOpname = new stokOpname();
        stokOpname.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tombolstokopActionPerformed

    private void tombolreturActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolreturActionPerformed
        jual jual = new jual();
        jual.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tombolreturActionPerformed

    private void tombollUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombollUserActionPerformed
       user user = new user();
       user.setVisible(true);
       this.dispose();
    }//GEN-LAST:event_tombollUserActionPerformed

    private void tombolTambah1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tombolTambah1MouseClicked

    }//GEN-LAST:event_tombolTambah1MouseClicked

    private void tombolTambah1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolTambah1ActionPerformed
    jbarang1.setSize(580, 660); // Sesuaikan dengan ukuran yang diinginkan
    jbarang1.setLocationRelativeTo(this); // Supaya muncul di tengah
    jbarang1.setModal(true); // Membuat dialog bersifat modal (opsional)
    jbarang1.setVisible(true); // Menampilkan dialog
    
    }//GEN-LAST:event_tombolTambah1ActionPerformed

    private void jcancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcancelActionPerformed
        jbarang1.dispose();  // Tutup JDialog
        this.setVisible(true); // Pastikan JFrame tetap terlihat
    }//GEN-LAST:event_jcancelActionPerformed

    private void tanggalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tanggalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tanggalActionPerformed

    private void noTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noTransActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noTransActionPerformed

    private void jbarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbarcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbarcodeActionPerformed

    private void jnm_barangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jnm_barangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jnm_barangActionPerformed

    private void jkd_barangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jkd_barangActionPerformed
       try {

            ResultSet rs=db.ambildata("select * from tb_barang where kode_barang='" + jkd_barang.getText() + "'");
            if (rs.next()) {
                jnm_barang.setText( rs.getString("nama_barang"));
                jkd_barang.setText(rs.getString("kode_barang"));
                jhrg_jual.setText( rs.getString("harga_jual"));
                jhrg_beli.setText( rs.getString("harga_beli"));
                jstok.setText( rs.getString("stok"));
                jnm_pemasok.setText( rs.getString("nama_pemasok"));
                jvarian.setText( rs.getString("varian"));
                jberat.setText( rs.getString("berat"));
                jtgl_exp.setDate(rs.getDate("exp"));
                jbarcode.setText( rs.getString("barcode"));
                jkategori.setSelectedItem(rs.getString("kategori"));
                simpan.setEnabled(false);
                // Simpan.setEnabled(false);
                // hapus.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog( null, "Data Belum terdaftar, tambahkan");
                jnm_barang.requestFocus();

            }
        } catch (SQLException e) {
        }
    }//GEN-LAST:event_jkd_barangActionPerformed

    private void jstokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jstokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jstokActionPerformed

    private void jhrg_jualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jhrg_jualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jhrg_jualActionPerformed

    private void jhrg_beliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jhrg_beliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jhrg_beliActionPerformed

    private void jnm_pemasokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jnm_pemasokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jnm_pemasokActionPerformed

    private void jvarianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jvarianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jvarianActionPerformed

    private void jberatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jberatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jberatActionPerformed

    private void tombolLogout1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolLogout1ActionPerformed
        login dashboard = new login();
        dashboard.setVisible(true);
        System.out.println("github perubahan");
        this.dispose();
    }//GEN-LAST:event_tombolLogout1ActionPerformed

    private void tombolhapussssActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolhapussssActionPerformed
    deleteData();
                              

    }//GEN-LAST:event_tombolhapussssActionPerformed

    private void jcancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcancel1ActionPerformed
        jbarang2.dispose();  // Tutup JDialog
        this.setVisible(true); // Pastikan JFrame tetap terlihat
    }//GEN-LAST:event_jcancel1ActionPerformed

    private void tanggal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tanggal1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tanggal1ActionPerformed

    private void jbarcode1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbarcode1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbarcode1ActionPerformed

    private void noTrans1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noTrans1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noTrans1ActionPerformed

    private void jnm_barang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jnm_barang1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jnm_barang1ActionPerformed

    private void jkd_barang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jkd_barang1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jkd_barang1ActionPerformed

    private void jstok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jstok1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jstok1ActionPerformed

    private void jhrg_jual1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jhrg_jual1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jhrg_jual1ActionPerformed

    private void jhrg_beli1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jhrg_beli1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jhrg_beli1ActionPerformed

    private void jnm_pemasok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jnm_pemasok1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jnm_pemasok1ActionPerformed

    private void jvarian1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jvarian1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jvarian1ActionPerformed

    private void jberat1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jberat1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jberat1ActionPerformed

    private void simpan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpan1ActionPerformed

                                                 
      updateData1(); // BUKAN updateData() biasa

    }//GEN-LAST:event_simpan1ActionPerformed

    private void tombolJual1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolJual1ActionPerformed
        jual jual = new jual();
        jual.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tombolJual1ActionPerformed

    private void tombolEdit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolEdit2ActionPerformed
 
        
        int row = jTable1.getSelectedRow();
        
                // Set stok tapi buat tidak bisa diedit
        jstok1.setText(model.getValueAt(row, 4).toString());
        jstok1.setEditable(false);  // Tambahkan ini
        jstok1.setEnabled(false);    // Dan ini untuk visual yang lebih jelas
        jstok1.setBackground(Color.white); // Optional: beri warna berbeda
        
    if (row >= 0) {
        // Isi form edit
        jkd_barang1.setText(model.getValueAt(row, 0).toString());
        jnm_barang1.setText(model.getValueAt(row, 1).toString());
        jvarian1.setText(model.getValueAt(row, 2).toString());
        jberat1.setText(model.getValueAt(row, 3).toString());
        jstok1.setText(model.getValueAt(row, 4).toString());
        jexp1.setDate(java.sql.Date.valueOf(model.getValueAt(row, 5).toString()));
        jhrg_jual1.setText(model.getValueAt(row, 6).toString());
        jhrg_beli1.setText(model.getValueAt(row, 7).toString());
        jbarcode1.setText(model.getValueAt(row, 8).toString());
        jkategori1.setSelectedItem(model.getValueAt(row, 9).toString());
        jnm_pemasok1.setText(model.getValueAt(row, 10).toString());

        jbarang2.setSize(580, 660);
        jbarang2.setLocationRelativeTo(this);
        jbarang2.setModal(true);
        jbarang2.setVisible(true);
        
        
            // Disable fields that shouldn't be edited
    jstok.setEnabled(false);
    } else {
        JOptionPane.showMessageDialog(this, "Pilih data yang mau diedit dulu!");
    }

    }//GEN-LAST:event_tombolEdit2ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        int row = jTable1.getSelectedRow(); // ambil baris yang dipilih
         // Set form tambah
//    jkd_barang.setText(model.getValueAt(row, 0).toString());
//    jnm_barang.setText(model.getValueAt(row, 1).toString());
//    jstok.setText(model.getValueAt(row, 4).toString());
//    jhrg_jual.setText(model.getValueAt(row, 6).toString());
//    jhrg_beli.setText(model.getValueAt(row, 7).toString());
//    jnm_pemasok.setText(model.getValueAt(row, 10).toString());
//    jvarian.setText(model.getValueAt(row, 2).toString());
//    jberat.setText(model.getValueAt(row, 3).toString());
//    jtgl_exp.setDate(java.sql.Date.valueOf(model.getValueAt(row, 5).toString()));
//    jbarcode.setText(model.getValueAt(row, 8).toString());
//    jkategori.setSelectedItem(model.getValueAt(row, 9).toString());

    // Set form edit
    jkd_barang1.setText(model.getValueAt(row, 0).toString());
    jnm_barang1.setText(model.getValueAt(row, 1).toString());
    jstok1.setText(model.getValueAt(row, 4).toString());
    jhrg_jual1.setText(model.getValueAt(row, 6).toString());
    jhrg_beli1.setText(model.getValueAt(row, 7).toString());
    jnm_pemasok1.setText(model.getValueAt(row, 10).toString());
    jvarian1.setText(model.getValueAt(row, 2).toString());
    jberat1.setText(model.getValueAt(row, 3).toString());
    jexp1.setDate(java.sql.Date.valueOf(model.getValueAt(row, 5).toString()));
    jbarcode1.setText(model.getValueAt(row, 8).toString());
    jkategori1.setSelectedItem(model.getValueAt(row, 9).toString());

    simpan.setEnabled(false); // matikan simpan agar tidak nambah dobel
    
    }//GEN-LAST:event_jTable1MouseClicked

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
       try {
        ResultSet rs = db.ambildata("SELECT * FROM tb_barang WHERE kode_barang='" + jkd_barang.getText() + "'");
        if (rs.next()) {
            JOptionPane.showMessageDialog(null, "Kode barang sudah terdaftar!");
        } else {
            // Format tanggal exp (jika ada)
            String expFormatted = "";
            if (jtgl_exp.getDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                expFormatted = sdf.format(jtgl_exp.getDate());
            }
            
            // Format tanggal hari ini (tanpa waktu)
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(new Date());

            String sql = "INSERT INTO tb_barang(kode_barang, nama_barang, kategori, varian, berat, stok, exp, harga_jual, harga_beli, barcode, nama_pemasok, Tanggal) VALUES ('"
                + jkd_barang.getText() + "','"
                + jnm_barang.getText() + "','"
                + jkategori.getSelectedItem() + "','"
                + jvarian.getText() + "','"
                + jberat.getText() + "','"
                + jstok.getText() + "','"
                + expFormatted + "','"
                + jhrg_jual.getText() + "','"
                + jhrg_beli.getText() + "','"
                + jbarcode.getText() + "','"
                + jnm_pemasok.getText() + "','"
                + currentDate + "')";  // Hanya tanggal tanpa waktu
//                + currentDate + "')"; //tanggal otomtis

            db.aksi(sql);
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");

            // setelah simpan, refresh tabel
            model.setRowCount(0);
            viewdata();

            // kosongkan isian form
            isianBersih();

            // tutup form pop up
            Window window = SwingUtilities.getWindowAncestor(simpan);
            if (window instanceof JDialog) {
                window.dispose();
            }
        }
    } catch (Exception e) {
    e.printStackTrace(); // biar error kelihatan di console
    JOptionPane.showMessageDialog(null, "Gagal menyimpan data: " + e.getMessage());
    }
    }//GEN-LAST:event_simpanActionPerformed

    private void tombolDasbor2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolDasbor2ActionPerformed
        dashboard dashboard = new dashboard();
        dashboard.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tombolDasbor2ActionPerformed

    private void tombolRetur1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolRetur1ActionPerformed
        retur retur = new retur();
        retur.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tombolRetur1ActionPerformed

    private void tombolLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolLaporanActionPerformed
        laporanPembelian laporanPembelian = new laporanPembelian();
        laporanPembelian.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tombolLaporanActionPerformed

    private void tombolKartustokkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolKartustokkActionPerformed
        // TODO add your handling code here:
    jDialog3.setSize(580, 345); // Sesuaikan dengan ukuran yang diinginkan
    jDialog3.setLocationRelativeTo(this); // Supaya muncul di tengah
    jDialog3.setModal(true); // Membuat dialog bersifat modal (opsional)
    jDialog3.setVisible(true); // Menampilkan dialog
    }//GEN-LAST:event_tombolKartustokkActionPerformed

    private void tombolCari1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolCari1ActionPerformed

    }//GEN-LAST:event_tombolCari1ActionPerformed

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
            java.util.logging.Logger.getLogger(barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new barang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel a;
    private javax.swing.JLabel a1;
    private javax.swing.JTextField b;
    private javax.swing.JTextField b1;
    private javax.swing.JTextField c;
    private javax.swing.JTextField c1;
    private javax.swing.JButton jButton1;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JDialog jbarang1;
    private javax.swing.JDialog jbarang2;
    private javax.swing.JTextField jbarcode;
    private javax.swing.JTextField jbarcode1;
    private javax.swing.JTextField jberat;
    private javax.swing.JTextField jberat1;
    private javax.swing.JButton jcancel;
    private javax.swing.JButton jcancel1;
    private javax.swing.JTextField jcari;
    private com.toedter.calendar.JDateChooser jexp1;
    private javax.swing.JTextField jhrg_beli;
    private javax.swing.JTextField jhrg_beli1;
    private javax.swing.JTextField jhrg_jual;
    private javax.swing.JTextField jhrg_jual1;
    private javax.swing.JComboBox<String> jkategori;
    private javax.swing.JComboBox<String> jkategori1;
    private javax.swing.JTextField jkd_barang;
    private javax.swing.JTextField jkd_barang1;
    private javax.swing.JTextField jnm_barang;
    private javax.swing.JTextField jnm_barang1;
    private javax.swing.JTextField jnm_pemasok;
    private javax.swing.JTextField jnm_pemasok1;
    private javax.swing.JTextField jstok;
    private javax.swing.JTextField jstok1;
    private com.toedter.calendar.JDateChooser jtgl_exp;
    private javax.swing.JTextField jvarian;
    private javax.swing.JTextField jvarian1;
    private javax.swing.JComboBox<String> kategori;
    private javax.swing.JTextField kodeBarang1;
    private javax.swing.JTextField namaBarang1;
    private javax.swing.JTextField noTrans;
    private javax.swing.JTextField noTrans1;
    private javax.swing.JButton simpan;
    private javax.swing.JButton simpan1;
    private javax.swing.JButton simpan2;
    private javax.swing.JTextField stokAkhir;
    private javax.swing.JTextField stokKeluar1;
    private javax.swing.JTextField stokMasuk1;
    private javax.swing.JTextField tanggal;
    private javax.swing.JTextField tanggal1;
    private javax.swing.JButton tombolCari1;
    private javax.swing.JButton tombolDasbor2;
    private javax.swing.JButton tombolEdit2;
    private javax.swing.JButton tombolJual1;
    private javax.swing.JButton tombolKartustokk;
    private javax.swing.JButton tombolLaporan;
    private javax.swing.JButton tombolLogout1;
    private javax.swing.JButton tombolRetur1;
    private javax.swing.JButton tombolTambah1;
    private javax.swing.JButton tombolhapussss;
    private javax.swing.JButton tombollUser;
    private javax.swing.JButton tombolstokop;
    // End of variables declaration//GEN-END:variables

}
