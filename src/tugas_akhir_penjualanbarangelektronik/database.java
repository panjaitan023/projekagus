/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugas_akhir_penjualanbarangelektronik;
import java.sql.*;
import javax.swing.JOptionPane;

public class database {
   public static Connection conn;
    public static Statement stm;
    
  public static void main (String [] args){
  }
  public void config(){
      try{
            String url="jdbc:mysql://localhost/penjualan";
            String user="root";
            String pass="";
            Class.forName("com.mysql.jdbc.Driver");
            conn=DriverManager.getConnection(url,user,pass);
            stm=conn.createStatement();
            JOptionPane.showMessageDialog(null, "Koneksi Berhasil", "Informasi", JOptionPane.INFORMATION_MESSAGE);
      }catch (Exception e){
         JOptionPane.showMessageDialog(null, "Koneksi Gagal");
      }
  }  
}
