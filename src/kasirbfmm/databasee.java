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
    private final String driver = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost/aditkasir";
    private final String user = "root";
    private final String password = "";


public Connection koneksiDB() {
    try {
        Class.forName(driver);
         con = DriverManager.getConnection(url, user, password);
        System.out.println("Koneksi database berhasil!");
        return con;
    } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Koneksi database gagal: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
            return null;
    }
}

public ResultSet ambildata(String SQL) {
    ResultSet rs = null;
    try {
         if (con == null) { // Cek apakah koneksi sudah dibuat
                koneksiDB();
         }
        Connection con = (Connection) getConnection(); // Ambil koneksi database
        PreparedStatement pst = con.prepareStatement(SQL);
        rs = pst.executeQuery();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error saat mengambil data: " + e.getMessage());
    }
    return rs;
}

public boolean aksi(String SQL) {
    try {
         if (con == null) { // Pastikan koneksi sudah dibuat
                koneksiDB();
            }
        Statement st = con.createStatement();
        st.executeUpdate(SQL);
        return true; // Jika berhasil
    } catch (SQLException e) {
        System.out.println("Error:\nAksi gagal diakses! " + e.getMessage());
        return false; // Jika gagal
    }
}

public Connection getConnection() {
    return koneksiDB(); // Mengembalikan koneksi dari metode koneksiDB()
}

    int ubahdata(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    int updatedata(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    static void aksi(boolean query) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

//    Object getConnection() {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }

    PreparedStatement prepareStatement(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    private boolean validasiData() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
    

   
    }


