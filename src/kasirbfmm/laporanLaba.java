/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kasirbfmm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.concurrent.TimeUnit;
import java.beans.PropertyChangeListener;
import java.util.concurrent.TimeUnit;
import java.beans.PropertyChangeListener;
import java.util.Date;  // Untuk class Date
import java.util.concurrent.TimeUnit;  // Untuk TimeUnit
import java.beans.PropertyChangeListener;  // Untuk PropertyChangeListener
import java.text.SimpleDateFormat;  // Untuk SimpleDateFormat
import java.util.Calendar;  // Untuk Calendar
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 *
 * @author Dhimas Ananta
 */
public class laporanLaba extends javax.swing.JFrame {

    /**
     * Creates new form laporanPembelian
     */
    public laporanLaba() {
                this.setUndecorated(true);
        initComponents();
    initializeDatabaseConnection();
    
    // Set layout untuk jPanel1
    jPanel1.setLayout(new BorderLayout());
    
    // Kosongkan tanggal
    dariTanggal.setDate(null);
    sampaiTanggal.setDate(null);
    
    // Tampilkan grafik bulanan default TANPA scroll
    createMonthlyProfitChart();
    
    // Tambah listener untuk JDateChooser
    dariTanggal.addPropertyChangeListener("date", evt -> updateChartBasedOnDates());
    sampaiTanggal.addPropertyChangeListener("date", evt -> updateChartBasedOnDates());
    }
    private Connection conn;
    private void showDefaultChart() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, -5); // 5 bulan terakhir
    createGroupedDateRangeChart(cal.getTime(), new Date());
}
    
    private void updateChartBasedOnDates() {
    // Perbaiki nama variabel JDateChooser
    Date startDate = dariTanggal.getDate();
    Date endDate = sampaiTanggal.getDate();

    // Jika salah satu tanggal belum diisi, tampilkan grafik bulanan default
    if (startDate == null || endDate == null) {
        createMonthlyProfitChart(); // Tampilan bulanan tanpa scroll
        return;
    }
    
    // Perbaiki penulisan JOptionPane dan penutup kurung
    if (startDate.after(endDate)) {
        JOptionPane.showMessageDialog(this,
            "Tanggal mulai tidak boleh setelah tanggal akhir",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Hitung selisih hari
    long diffInMillis = endDate.getTime() - startDate.getTime();
    long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

    if (diffInDays > 60) {
        // Untuk rentang panjang, gunakan chart dengan scroll
        createGroupedDateRangeChart(startDate, endDate);
    } else {
        // Untuk rentang pendek, gunakan chart biasa
        createDateRangeProfitChart(startDate, endDate);
    }
}
   
    

     private void initializeDatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/bfm_kasir1111";
            String username = "root";
            String password = "";
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Failed to connect to database: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

     private void createDateRangeProfitChart(Date startDate, Date endDate) {
    try {
        if (conn == null || conn.isClosed()) {
            initializeDatabaseConnection();
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM");

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        
        // Hitung selisih hari antara tanggal mulai dan akhir
        long diffInMillis = endDate.getTime() - startDate.getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        
        // Jika rentang tanggal lebih dari 60 hari, buat grafik bulanan
        if (diffInDays > 60) {
    // Tetap tampilkan grafik harian tapi dengan grouping per minggu/bulan
    createGroupedDateRangeChart(startDate, endDate);
    return;
        }

        // Loop melalui setiap hari dalam rentang tanggal
        while (!cal.getTime().after(endDate)) {
            String currentDate = sdf.format(cal.getTime());
            String displayDate = displayFormat.format(cal.getTime());
            
            double profit = calculateDailyProfit(currentDate);
            double loss = calculateDailyLoss(currentDate);
            double netProfit = profit - loss;

            dataset.addValue(netProfit, "Laba/Rugi", displayDate);
            
            cal.add(Calendar.DATE, 1); // Tambah 1 hari
        }

        // Buat chart
        JFreeChart chart = ChartFactory.createLineChart(
            "",                     // title
            "Tanggal",              // x-axis label (ubah menjadi "Tanggal")
            "Nominal (Rp)",         // y-axis label
            dataset,                // data
            PlotOrientation.VERTICAL,
            false,                  // legend
            true,                   // tooltips
            false                   // urls
        );

        // Styling chart (sama seperti sebelumnya)
        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(230, 230, 230));

        Color purpleColor = new Color(128, 0, 128);
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesPaint(0, purpleColor);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));
        renderer.setSeriesFillPaint(0, purpleColor);
        renderer.setSeriesOutlinePaint(0, purpleColor);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setNumberFormatOverride(new DecimalFormat("Rp #,##0"));

        // Tambah chart ke jPanel1
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(jPanel1.getWidth(), jPanel1.getHeight()));
        jPanel1.removeAll();
        jPanel1.add(chartPanel);
        jPanel1.revalidate();
        jPanel1.repaint();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error creating chart: " + e.getMessage(),
            "Chart Error",
            JOptionPane.ERROR_MESSAGE);
    }
}
     
     private void createGroupedDateRangeChart(Date startDate, Date endDate) {
    try {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM");
        
        // Group by week if range is large
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        
        while (!cal.getTime().after(endDate)) {
            Calendar endOfWeek = (Calendar) cal.clone();
            endOfWeek.add(Calendar.DAY_OF_WEEK, 6);
            if (endOfWeek.after(endDate)) {
                endOfWeek.setTime(endDate);
            }
            
            String weekRange = displayFormat.format(cal.getTime()) + " - " + 
                             displayFormat.format(endOfWeek.getTime());
            
            double weekProfit = 0;
            double weekLoss = 0;
            
            Calendar dayCal = (Calendar) cal.clone();
            while (!dayCal.after(endOfWeek)) {
                String currentDate = sdf.format(dayCal.getTime());
                weekProfit += calculateDailyProfit(currentDate);
                weekLoss += calculateDailyLoss(currentDate);
                dayCal.add(Calendar.DATE, 1);
            }
            
            dataset.addValue(weekProfit - weekLoss, "Laba/Rugi", weekRange);
            cal.add(Calendar.WEEK_OF_YEAR, 1);
        }

        // Buat chart
        JFreeChart chart = ChartFactory.createLineChart(
            "",                     // title
            "Rentang Tanggal",      // x-axis label
            "Nominal (Rp)",         // y-axis label
            dataset,                // data
            PlotOrientation.VERTICAL,
            false,                 // legend
            true,                  // tooltips
            false                  // urls
        );
        
        // Styling chart
        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(230, 230, 230));

        Color purpleColor = new Color(128, 0, 128);
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesPaint(0, purpleColor);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));
        
        // Atur renderer untuk spacing lebih baik
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.2); // Tambah spacing antar kategori
        
        // Buat panel chart dengan scroll
        ChartPanel chartPanel = new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(2000, jPanel1.getHeight()); // Lebar besar untuk scroll
            }
        };
        
        JScrollPane scrollPane = new JScrollPane(chartPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        jPanel1.removeAll();
        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(scrollPane, BorderLayout.CENTER);
        jPanel1.revalidate();
        jPanel1.repaint();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error creating chart: " + e.getMessage(),
            "Chart Error",
            JOptionPane.ERROR_MESSAGE);
    }
}     
     
     private double calculateDailyProfit(String date) {
    double profit = 0;
    try {
        String query = "SELECT SUM((dj.harga_barang - dj.harga_beli) * dj.jumlah_barang) as profit " +
                      "FROM detail_transaksijual dj " +
                      "JOIN tb_jual tj ON dj.no_transaksi = tj.no_transaksi " +
                      "WHERE DATE(tj.tanggal) = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, date);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            profit = rs.getDouble("profit");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return profit;
}

private double calculateDailyLoss(String date) {
    double loss = 0;
    try {
        // 1. Hitung dari retur supplier
        String queryRetur = "SELECT SUM(b.harga_beli * rs.qty) as loss " +
                          "FROM tb_retur_supplier rs " +
                          "JOIN tb_barang b ON rs.kode_barang = b.kode_barang " +
                          "WHERE DATE(rs.tanggal) = ?";
        
        // 2. Hitung dari stock opname (selisih negatif)
        String queryOpname = "SELECT SUM(b.harga_beli * ABS(so.selisih)) as loss " +
                           "FROM tb_stok_opname so " +
                           "JOIN tb_barang b ON so.kode_barang = b.kode_barang " +
                           "WHERE so.selisih < 0 AND DATE(so.tanggal) = ?";
        
        PreparedStatement stmt = conn.prepareStatement(queryRetur);
        stmt.setString(1, date);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            loss += rs.getDouble("loss");
        }
        
        stmt = conn.prepareStatement(queryOpname);
        stmt.setString(1, date);
        rs = stmt.executeQuery();
        if (rs.next()) {
            loss += rs.getDouble("loss");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return loss;
}


     
    private void createMonthlyProfitChart() {
        try {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Get current year
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        
        // Hitung laba per bulan
        for (int month = 0; month < 12; month++) {
            String monthName = getMonthName(month);
            double profit = calculateMonthlyProfit(currentYear, month + 1);
            double loss = calculateMonthlyLoss(currentYear, month + 1);
            dataset.addValue(profit - loss, "Laba/Rugi", monthName);
        }

            // Create chart
            JFreeChart chart = ChartFactory.createLineChart(
                "",                     // title
                "Bulan",                // x-axis label
                "Nominal (Rp)",         // y-axis label
                dataset,                // data
                PlotOrientation.VERTICAL,
                false,                  // legend
                true,                   // tooltips
                false                   // urls
            );

            // Styling chart
            chart.setBackgroundPaint(Color.WHITE);
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(Color.WHITE);
            plot.setRangeGridlinePaint(new Color(230, 230, 230));

            // Purple color for the line
            Color purpleColor = new Color(128, 0, 128);
            LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesStroke(0, new BasicStroke(3.0f));
            renderer.setSeriesPaint(0, purpleColor);
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));
            renderer.setSeriesFillPaint(0, purpleColor);
            renderer.setSeriesOutlinePaint(0, purpleColor);

            // Format y-axis as currency
            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setNumberFormatOverride(new DecimalFormat("Rp #,##0"));

            // Add chart to jPanel1
             ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(jPanel1.getWidth(), jPanel1.getHeight()));
        
        jPanel1.removeAll();
        jPanel1.add(chartPanel); // Tanpa JScrollPane
        jPanel1.revalidate();
        jPanel1.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error creating chart: " + e.getMessage(),
                "Chart Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getMonthName(int month) {
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "Mei", "Jun", 
                              "Jul", "Ags", "Sep", "Okt", "Nov", "Des"};
        return monthNames[month];
    }

    private double calculateMonthlyProfit(int year, int month) {
        double profit = 0;
        try {
            String query = "SELECT SUM((dj.harga_barang - dj.harga_beli) * dj.jumlah_barang) as profit " +
                          "FROM detail_transaksijual dj " +
                          "JOIN tb_jual tj ON dj.no_transaksi = tj.no_transaksi " +
                          "WHERE YEAR(tj.tanggal) = ? AND MONTH(tj.tanggal) = ?";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, year);
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                profit = rs.getDouble("profit");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return profit;
    }

    private double calculateMonthlyLoss(int year, int month) {
        double loss = 0;
        try {
            // 1. Calculate from supplier returns
            String queryRetur = "SELECT SUM(b.harga_beli * rs.qty) as loss " +
                              "FROM tb_retur_supplier rs " +
                              "JOIN tb_barang b ON rs.kode_barang = b.kode_barang " +
                              "WHERE YEAR(rs.tanggal) = ? AND MONTH(rs.tanggal) = ?";
            
            // 2. Calculate from stock opname (negative differences)
            String queryOpname = "SELECT SUM(b.harga_beli * ABS(so.selisih)) as loss " +
                               "FROM tb_stok_opname so " +
                               "JOIN tb_barang b ON so.kode_barang = b.kode_barang " +
                               "WHERE so.selisih < 0 AND YEAR(so.tanggal) = ? AND MONTH(so.tanggal) = ?";
            
            PreparedStatement stmt = conn.prepareStatement(queryRetur);
            stmt.setInt(1, year);
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                loss += rs.getDouble("loss");
            }
            
            stmt = conn.prepareStatement(queryOpname);
            stmt.setInt(1, year);
            stmt.setInt(2, month);
            rs = stmt.executeQuery();
            if (rs.next()) {
                loss += rs.getDouble("loss");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loss;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        penjualan = new javax.swing.JButton();
        dasbor1 = new javax.swing.JButton();
        stokOpname = new javax.swing.JButton();
        barang1 = new javax.swing.JButton();
        jual1 = new javax.swing.JButton();
        user1 = new javax.swing.JButton();
        logout1 = new javax.swing.JButton();
        ekspor1 = new javax.swing.JButton();
        pembelian = new javax.swing.JButton();
        retur2 = new javax.swing.JButton();
        reset = new javax.swing.JButton();
        dariTanggal = new com.toedter.calendar.JDateChooser();
        sampaiTanggal = new com.toedter.calendar.JDateChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        penjualan.setBorderPainted(false);
        penjualan.setContentAreaFilled(false);
        penjualan.setFocusPainted(false);
        penjualan.setFocusable(false);
        penjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penjualanActionPerformed(evt);
            }
        });
        getContentPane().add(penjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 110, 120, 30));

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
        getContentPane().add(jual1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 100, 20));

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

        reset.setBorder(null);
        reset.setBorderPainted(false);
        reset.setContentAreaFilled(false);
        reset.setFocusPainted(false);
        reset.setRequestFocusEnabled(false);
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });
        getContentPane().add(reset, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 100, 120, 40));
        getContentPane().add(dariTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 110, 130, -1));
        getContentPane().add(sampaiTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 110, 130, -1));

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setForeground(new java.awt.Color(204, 204, 204));
        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 160, 1010, 560));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fotobaru/Labaa (1).png"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void penjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penjualanActionPerformed
        laporanPenjualan laporanPenjualan = new laporanPenjualan();
        laporanPenjualan.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_penjualanActionPerformed

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
        // TODO add your handling code here:
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

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
        // TODO add your handling code here:
        dariTanggal.setDate(null);
    sampaiTanggal.setDate(null);
    
    // Tampilkan grafik bulanan default
    createMonthlyProfitChart();
    }//GEN-LAST:event_resetActionPerformed

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
            java.util.logging.Logger.getLogger(laporanLaba.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(laporanLaba.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(laporanLaba.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(laporanLaba.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new laporanLaba().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton barang1;
    private com.toedter.calendar.JDateChooser dariTanggal;
    private javax.swing.JButton dasbor1;
    private javax.swing.JButton ekspor1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jual1;
    private javax.swing.JButton logout1;
    private javax.swing.JButton pembelian;
    private javax.swing.JButton penjualan;
    private javax.swing.JButton reset;
    private javax.swing.JButton retur2;
    private com.toedter.calendar.JDateChooser sampaiTanggal;
    private javax.swing.JButton stokOpname;
    private javax.swing.JButton user1;
    // End of variables declaration//GEN-END:variables
}
