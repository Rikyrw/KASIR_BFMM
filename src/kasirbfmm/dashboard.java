/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kasirbfmm;

/**
 *
 * @author user
 */
public class dashboard extends javax.swing.JFrame {

    /**
     * Creates new form dashboard
     */
    public dashboard() {
        initComponents();
        jlogout.setOpaque(false);
        jlogout.setContentAreaFilled(false);
        jlogout.setBorderPainted(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jlogout = new javax.swing.JButton();
        tombolLaba = new javax.swing.JButton();
        tombolDasbor = new javax.swing.JButton();
        tombolBarang1 = new javax.swing.JButton();
        tombolBeli1 = new javax.swing.JButton();
        tombolJual1 = new javax.swing.JButton();
        tomboluser1 = new javax.swing.JButton();
        tombolPembelian1 = new javax.swing.JButton();
        tombolPenjualan1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jlogout.setBorder(null);
        jlogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jlogoutActionPerformed(evt);
            }
        });
        getContentPane().add(jlogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 720, 120, 30));

        tombolLaba.setBorderPainted(false);
        tombolLaba.setContentAreaFilled(false);
        tombolLaba.setFocusPainted(false);
        tombolLaba.setOpaque(false);
        tombolLaba.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolLabaActionPerformed(evt);
            }
        });
        getContentPane().add(tombolLaba, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 500, 120, 30));

        tombolDasbor.setBorderPainted(false);
        tombolDasbor.setContentAreaFilled(false);
        tombolDasbor.setFocusPainted(false);
        tombolDasbor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolDasborActionPerformed(evt);
            }
        });
        getContentPane().add(tombolDasbor, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 145, 120, 30));

        tombolBarang1.setBorderPainted(false);
        tombolBarang1.setContentAreaFilled(false);
        tombolBarang1.setFocusPainted(false);
        tombolBarang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolBarang1ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolBarang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 120, 30));

        tombolBeli1.setBorderPainted(false);
        tombolBeli1.setContentAreaFilled(false);
        tombolBeli1.setFocusPainted(false);
        tombolBeli1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolBeli1ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolBeli1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 120, 30));

        tombolJual1.setBorderPainted(false);
        tombolJual1.setContentAreaFilled(false);
        tombolJual1.setFocusPainted(false);
        tombolJual1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolJual1ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolJual1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, 120, 30));

        tomboluser1.setBorderPainted(false);
        tomboluser1.setContentAreaFilled(false);
        tomboluser1.setFocusPainted(false);
        tomboluser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tomboluser1ActionPerformed(evt);
            }
        });
        getContentPane().add(tomboluser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 120, 30));

        tombolPembelian1.setBorderPainted(false);
        tombolPembelian1.setContentAreaFilled(false);
        tombolPembelian1.setFocusPainted(false);
        tombolPembelian1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolPembelian1ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolPembelian1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 120, 30));

        tombolPenjualan1.setBorderPainted(false);
        tombolPenjualan1.setContentAreaFilled(false);
        tombolPenjualan1.setFocusPainted(false);
        tombolPenjualan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolPenjualan1ActionPerformed(evt);
            }
        });
        getContentPane().add(tombolPenjualan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 120, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/foto/desktop.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jlogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jlogoutActionPerformed
        login dashboard = new login();
        dashboard.setVisible(true);
        System.out.println("github perubahan");
        this.dispose();
    }//GEN-LAST:event_jlogoutActionPerformed

    private void tombolLabaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolLabaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tombolLabaActionPerformed

    private void tombolDasborActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolDasborActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tombolDasborActionPerformed

    private void tombolBarang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolBarang1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tombolBarang1ActionPerformed

    private void tombolBeli1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolBeli1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tombolBeli1ActionPerformed

    private void tombolJual1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolJual1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tombolJual1ActionPerformed

    private void tomboluser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tomboluser1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tomboluser1ActionPerformed

    private void tombolPembelian1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolPembelian1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tombolPembelian1ActionPerformed

    private void tombolPenjualan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolPenjualan1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tombolPenjualan1ActionPerformed

    
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
                new dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton jlogout;
    private javax.swing.JButton tombolBarang1;
    private javax.swing.JButton tombolBeli1;
    private javax.swing.JButton tombolDasbor;
    private javax.swing.JButton tombolJual1;
    private javax.swing.JButton tombolLaba;
    private javax.swing.JButton tombolPembelian1;
    private javax.swing.JButton tombolPenjualan1;
    private javax.swing.JButton tomboluser1;
    // End of variables declaration//GEN-END:variables
}
