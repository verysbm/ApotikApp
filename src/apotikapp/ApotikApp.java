/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package apotikapp;

import Koneksi.Koneksi;
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
        try {
            Connection c = Koneksi.getConnection();
            System.out.println(String.format("Connected to database %s " + "successfully.", c.getCatalog()));
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
}
