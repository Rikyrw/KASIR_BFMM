package kasirbfmm;

import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class login extends javax.swing.JFrame {     
        databasee db = new databasee();
        Connection connection = null;
//       Main mainf = null;
     
    public login() {
        initComponents();
        // Tambahkan koneksi ke database di constructor

    try {
        connection = db.koneksiDB();
        if (connection == null) {
            System.out.println("Koneksi database gagal!");
        } else {
            System.out.println("Koneksi database berhasil!");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    
        jlogin.setOpaque(false); 
        jlogin.setContentAreaFilled(false); 
        jlogin.setBorderPainted(false);
        
        jusername.setOpaque(false);
        jusername.setBackground(new Color(0, 0, 0, 0));

        jpassword.setOpaque(false);
        jpassword.setBackground(new Color(0, 0, 0, 0));
        
        jinvisible.setVisible(false);
        // Fokus otomatis ke jusername biar langsung bisa scan tanpa pencet
    SwingUtilities.invokeLater(() -> jusername.requestFocusInWindow());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jeye = new javax.swing.JLabel();
        jinvisible = new javax.swing.JLabel();
        jpassword = new javax.swing.JPasswordField();
        jusername = new javax.swing.JTextField();
        jlogin = new javax.swing.JButton();
        RFIDInput = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jeye.setIcon(new javax.swing.ImageIcon(getClass().getResource("/foto/eye.png"))); // NOI18N
        jeye.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jeyeMouseClicked(evt);
            }
        });
        getContentPane().add(jeye, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 350, -1, -1));

        jinvisible.setIcon(new javax.swing.ImageIcon(getClass().getResource("/foto/Invisible.png"))); // NOI18N
        jinvisible.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jinvisibleMouseClicked(evt);
            }
        });
        getContentPane().add(jinvisible, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 350, -1, -1));

        jpassword.setBorder(null);
        getContentPane().add(jpassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 350, 260, 40));

        jusername.setBorder(null);
        jusername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jusernameActionPerformed(evt);
            }
        });
        jusername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jusernameKeyTyped(evt);
            }
        });
        getContentPane().add(jusername, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 270, 260, 50));

        jlogin.setBorder(null);
        jlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jloginActionPerformed(evt);
            }
        });
        getContentPane().add(jlogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 430, 350, 50));

        RFIDInput.setBackground(new java.awt.Color(255, 255, 255));
        RFIDInput.setUI(null);
        RFIDInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RFIDInputActionPerformed(evt);
            }
        });
        getContentPane().add(RFIDInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 50, 10, 10));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/logiin.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jloginActionPerformed
        if (jusername.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Username harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return; // Hentikan eksekusi jika ada yang kosong
        
    }
         if (jpassword.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Passwor harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return; // Hentikan eksekusi jika ada yang kosong
    }
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
    conn = db.koneksiDB();
    if (conn == null) {
        JOptionPane.showMessageDialog(null, "Database tidak dapat dihubungka!");
        return;
    } else {
        System.out.println("Database terhubung!");
    }

            String sql = "SELECT * FROM tb_user WHERE username = ? AND password = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, jusername.getText());
            pst.setString(2, jpassword.getText());
            rs = pst.executeQuery();

         if (rs.next()) {
            // Your code integration here
            jual.loggedInUserId = rs.getInt("id_user");
            jual.loggedInUsername = rs.getString("nama");
            
            dispose();
            new dashboard(jual.loggedInUsername).setVisible(true); // Or new jual() if you prefer
            // Alternatively, you could use:
            // new jual().setVisible(true);
            // this.dispose();
                
            } else {
                JOptionPane.showMessageDialog(null, "Username atau Password salah");
                jusername.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                System.out.println("Error saat menutup koneksi: " + e.getMessage());
            }
        }
        
//        
//        dashboard login = new dashboard();
//        login.setVisible(true);
//
//        // Menutup frame saat ini (opsional)
//        this.dispose();
    }//GEN-LAST:event_jloginActionPerformed

    private void jusernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jusernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jusernameActionPerformed

    private void jeyeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jeyeMouseClicked
    jeye.setVisible(false);
    jinvisible.setVisible(true);
    jpassword.setEchoChar((char)0);
    }//GEN-LAST:event_jeyeMouseClicked

    private void jinvisibleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jinvisibleMouseClicked
    jeye.setVisible(true);
    jinvisible.setVisible(false);
    jpassword.setEchoChar('*');
    }//GEN-LAST:event_jinvisibleMouseClicked

    private long lastTime = 0;
    private String buffer = "";
    private final int RFID_THRESHOLD = 30;
    
    private void jusernameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jusernameKeyTyped
        long currentTime = System.currentTimeMillis();
        char c = evt.getKeyChar();

        // Cek apakah inputnya terlalu cepat (mungkin RFID)
        if (lastTime != 0 && (currentTime - lastTime) > RFID_THRESHOLD) {
            buffer = ""; // Reset buffer jika jedanya lama
        }

        buffer += c;
        lastTime = currentTime;

        if (c == '\n' || c
                == '\r') { // RFID biasanya mengakhiri input dengan Enter
            if (buffer.length() >= 10) { // Anggap RFID minimal 8 karakter
                RFIDInput.setText(buffer); // Pindahkan ke textField tersembunyi
                jusername.setText(""); // Kosongkan kembali username
                ambilData(RFIDInput.getText().trim());
                System.out.println("Scan RFID Terdeteksi: " + buffer);

            } else {
                System.out.println("Input Manual: " + buffer);
                
            }
            buffer = ""; // Reset buffer setelah Enter ditekan
        }                    
    }//GEN-LAST:event_jusernameKeyTyped

    private void RFIDInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RFIDInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RFIDInputActionPerformed
   

    private void ambilData(String rfid) {
        if (connection == null) {
        JOptionPane.showMessageDialog(this, "Database tidak terhubung!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String query = "SELECT * FROM tb_user WHERE rfid = ? LIMIT 1";
    try (PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setString(1, rfid);
        ResultSet hasil = ps.executeQuery();
        if (hasil.next()) {
            jusername.setText(hasil.getString("username"));
            jpassword.setText(hasil.getString("password"));
            System.out.println("User ditemukan: " + hasil.getString("username"));
            // Langsung eksekusi login setelah data diisi
            jloginActionPerformed(null);
        } else {
            JOptionPane.showMessageDialog(this, "RFID tidak terdaftar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
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
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold> 

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField RFIDInput;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jeye;
    private javax.swing.JLabel jinvisible;
    private javax.swing.JButton jlogin;
    private javax.swing.JPasswordField jpassword;
    private javax.swing.JTextField jusername;
    // End of variables declaration//GEN-END:variables
}
