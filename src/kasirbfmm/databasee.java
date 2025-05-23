 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kasirbfmm;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


/**
 *
 * @author Lenovo
 */
public class databasee {
    Connection con;
    Statement st;
    ResultSet rs;
    
    private final String driver = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost/bfm_kasir1111"; // nama database kamu
    private final String user = "root";
    private final String password = "";

    public Connection koneksiDB() {
        try {
            if (con == null || con.isClosed()) {
                Class.forName(driver);
                con = DriverManager.getConnection(url, user, password);
                System.out.println("Koneksi database berhasil!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Koneksi database gagal: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        }
        return con;
    
}

public ResultSet ambildata(String SQL) {
     try {
            if (con == null || con.isClosed()) {
                koneksiDB();
            }
            st = con.createStatement();
            rs = st.executeQuery(SQL);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat mengambil data: " + e.getMessage());
        }
        return rs;
}

public boolean aksi(String SQL) {
     try {
            if (con == null || con.isClosed()) {
                koneksiDB();
            }
            st = con.createStatement();
            st.executeUpdate(SQL);
            return true;
        } catch (SQLException e) {
            System.out.println("Error saat melakukan aksi: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Gagal melakukan aksi database: " + e.getMessage());
            return false;
    }
}

public ResultSet cariBarangByBarcode(int barcode) {
    try {
        if (con == null || con.isClosed()) {
            koneksiDB();
        }
        st = con.createStatement();
        rs = st.executeQuery("SELECT * FROM tb_barang WHERE barcode = " + barcode);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error saat mencari barang: " + e.getMessage());
    }
    return rs;
}

    Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

//    public Connection koneksiDB() {
//    try {
//        Class.forName(driver);
//        con = DriverManager.getConnection(url, user, pwd);
//        System.out.println("Koneksi ke database berhasil.");
//        return con;
//    } catch (ClassNotFoundException | SQLException e) {
//        System.out.println("Koneksi database gagal: " + e.getMessage());
//        JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
//        return null;
//    }
//}
    

   
    


