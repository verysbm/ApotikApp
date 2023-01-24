/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package apotikapp;

import Koneksi.Koneksi;
import com.apotikapp.views.Login;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

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
            
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                
                if("Nimbus".equals(info.getName())) {
                    
                    UIManager.setLookAndFeel(info.getClassName());
                    
                    Login lg= new Login();
                    lg.setVisible(true);
 //                   new ApotikApp();
 //                   break;
                } else {
                    
                }
            }
        } catch (Exception e) {
            
        }
        
    }
    
}
