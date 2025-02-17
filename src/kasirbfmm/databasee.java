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
    private final String pwd = "";


public void koneksi(){
    try {
        Class .forName(driver);
        con = DriverManager.getConnection(url, user, pwd);
        System.out.println("Koneksi Berhasil");
    } catch (Exception e){
        System.out.println("Error:\nKoneksi Data Gagal\n"+e.getMessage());
        
    }
}

public ResultSet ambildata(String SQL) {
    try {
        Statement st=con.createStatement();
        return st.executeQuery(SQL);
    } catch (Exception e) {
        System.out.println("Error:\nPengecekan data gagal diakses !");
        return null;
    }
}

public boolean aksi(String SQL) {
    try {
        Statement st = con.createStatement();
        st.executeUpdate(SQL);
        return true; // Jika berhasil
    } catch (SQLException e) {
        System.out.println("Error:\nAksi gagal diakses! " + e.getMessage());
        return false; // Jika gagal
    }
}

    int ubahdata(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    int updatedata(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    boolean aksi(boolean query) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    Object getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    PreparedStatement prepareStatement(String query) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Connection koneksiDB() {
    try {
        Class.forName(driver);
        con = DriverManager.getConnection(url, user, pwd);
        System.out.println("Koneksi ke database berhasil.");
        return con;
    } catch (ClassNotFoundException | SQLException e) {
        System.out.println("Koneksi database gagal: " + e.getMessage());
        JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        return null;
    }
}
    

   
    }


