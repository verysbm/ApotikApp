/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package apotikapp;

import Koneksi.Koneksi;
import com.apotikapp.views.Login;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author recap
 */
public class ApotikApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Login lg= new Login();
        lg.setVisible(true);
    }
    
}
