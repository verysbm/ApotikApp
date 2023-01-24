/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.apotikapp.views.master;

import Koneksi.Koneksi;
import Koneksi.PetugasSession;
import com.apotikapp.views.MainMenu;
import com.apotikapp.views.report.ReportMenu;
import com.apotikapp.views.transaction.TransaksiPembelian;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author HP
 */
public class MasterDataSupplier extends javax.swing.JFrame {
 ResultSet Rs;
 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
 PetugasSession PetugasSession = new PetugasSession();
    /**
     * Creates new form MainMenu
     */
 
    public MasterDataSupplier() {
        initComponents();
        Datatabel();
        //Full Jframe
        setExtendedState(MAXIMIZED_BOTH);
        Locale locale=new Locale("id", "ID");
        Locale.setDefault(locale);  
        getJam();
        HakAkses();
        
         //Keluar aplikasi
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent we)
                { 
                    String ObjButtons[] = {"Yes","No"};
                    int PromptResult = JOptionPane.showOptionDialog(null,"Anda yakin ingin keluar aplikasi?","Aplikasi Apotik",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
                    if(PromptResult==JOptionPane.YES_OPTION)
                    {
                        System.exit(0);
                    }
                }
            });
    }
    
     public void HakAkses(){
        String akses = PetugasSession.getU_level();
        if( akses.equalsIgnoreCase("apoteker")){
            itemApoteker.setVisible(false);
            itemUser.setVisible(false);
        }  
    }
    
    public void getExit(){
        int confirm =JOptionPane.showConfirmDialog(this,"Anda yakin ingin keluar dari aplikasi ?","Keluar Aplikasi",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION){
            System.exit(0);
        }if(confirm == JOptionPane.NO_OPTION){   
        } 
    }
    
    public void getRefresh() {
        txtidSupplier.setText(null);
        txtnamaSupplier.setText(null);
        txtAlamat.setText(null);
        txtnoTelepon.setText(null);
    }
    
    private void Autonomor(){
        String sql = "SELECT max(RIGHT(id_supplier,4)) FROM tb_supplier ORDER BY id_supplier DESC";
        try{
            Statement state  = Koneksi.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                  ResultSet.CONCUR_UPDATABLE);
            Rs = state.executeQuery(sql);
            while (Rs.next()){
                if(Rs.first() == false){
                    txtidSupplier.setText("SPL0001");
                }else{
                    Rs.last();
                    int auto_id = Rs.getInt(1) + 1;
                    String no = String.valueOf(auto_id);
                    int noLong = no.length();
                    //MENGATUR jumlah 0
                    for (int j = 0; j < 4 - noLong; j++) {
                        no = "0" + no;
                    }
                    txtidSupplier.setText("SPL"+ no);
                }               
            }
            Rs.close();
            state.close();
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, "ERROR: \n" + e.toString(),
                    "Kesalahan", JOptionPane.WARNING_MESSAGE);
            System.out.println(""+ e.getMessage());
        }
    }
    
    private void refreshTabel() {
    DefaultTableModel model = (DefaultTableModel)tabelSupplier.getModel();
    model.setRowCount(0);
    Datatabel();
    }
    
    public void getJam() {
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                SimpleDateFormat tgl = new SimpleDateFormat("EEEE dd MMMM yyyy");
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";
                Date dt = new Date();
                int nilai_jam = dt.getHours();
                int nilai_menit = dt.getMinutes();
                int nilai_detik = dt.getSeconds();
                if (nilai_jam <= 9) {
                    nol_jam = "0";
                }
                if (nilai_menit <= 9) {
                    nol_menit = "0";
                }
                if (nilai_detik <= 9) {
                    nol_detik = "0";
                }
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                Jam.setText("  " + jam + " : " + menit + " : " + detik + "  ");
                Tanggal.setText(tgl.format(dt));
            }
        };
        new javax.swing.Timer(1000, taskPerformer).start();
    }
    
    public void Datatabel(){
       DefaultTableModel tabel = new DefaultTableModel();
       tabel.addColumn("ID Supplier");
       tabel.addColumn("Nama");
       tabel.addColumn("Alamat");
       tabel.addColumn("No Telepon");
       try{
           Statement state  = Koneksi.getConnection().createStatement();
           Rs = state.executeQuery("Select * FROM tb_supplier");
           while(Rs.next()){
               tabel.addRow(new Object[]{
                   Rs.getString(1),
                   Rs.getString(2),
                   Rs.getString(3),
                   Rs.getString(4),
               });
               tabelSupplier.setModel(tabel);
           }
           state.close();
           Rs.close();
       }
       catch(Exception e){
           System.out.println(e);
       }
    }
    
    int row=0;
    public void getKlik(){
        row=tabelSupplier.getSelectedRow();
        txtidSupplier.setText(tabelSupplier.getValueAt(row, 0).toString());
        txtnamaSupplier.setText(tabelSupplier.getValueAt(row, 1).toString());
        txtAlamat.setText(tabelSupplier.getValueAt(row, 2).toString());
        txtnoTelepon.setText(tabelSupplier.getValueAt(row, 3).toString());
        
    }
    
    // Saving data 
    public void Simpan(){
        String id = txtidSupplier.getText();
        String nama = txtnamaSupplier.getText();
        String alamat = txtAlamat.getText();
        String notelp = txtnoTelepon.getText();        
        if(id.equals("")||nama.equals("")||alamat.equals("")||notelp.equals("")){
        JOptionPane.showMessageDialog(this, "Lengkapi data anda");
        }else{
            try{
                Statement state  = Koneksi.getConnection().createStatement();
                state.executeUpdate("INSERT INTO tb_supplier VALUES ('"+id+"','"+nama+"','"+alamat+"','"+notelp+"')");
                getRefresh();                
                JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan !");
                refreshTabel();
                state.close();
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(this, "Data Gagal Disimpan !");
            }
        }
    }
    
    //Update Data
    public void editData(){
        String id = txtidSupplier.getText();
        String nama = txtnamaSupplier.getText();
        String alamat = txtAlamat.getText();
        String notelp = txtnoTelepon.getText();
        try{
            Statement state  = Koneksi.getConnection().createStatement();
            state.executeUpdate("UPDATE tb_supplier SET nama_supplier='"+nama
                    +"', alamat='"+alamat
                    +"', telp_supplier='"+notelp
                    +"' WHERE id_supplier='"+id+"';");
            JOptionPane.showMessageDialog(this, "Data Berhasil Diubah !");
            getRefresh();
            refreshTabel();
            state.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, "Data Gagal Disimpan !");
        }
    }
    
     public void deleteData(){
        String id = txtidSupplier.getText();
        try {
            Statement state  = Koneksi.getConnection().createStatement();
            state.executeUpdate("DELETE FROM tb_supplier WHERE id_supplier='"+id+"';");
            JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus !");
            getRefresh();
            refreshTabel();
            state.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data Gagal Dihapus !");
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelSupplier = new javax.swing.JTable();
        btnTambah = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        Jam = new javax.swing.JLabel();
        Tanggal = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtidSupplier = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        getIDSupplier = new javax.swing.JButton();
        txtnamaSupplier = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtnoTelepon = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        btnMObat = new javax.swing.JButton();
        btnMSupplier = new javax.swing.JButton();
        btnMApoteker = new javax.swing.JButton();
        btnMDokter = new javax.swing.JButton();
        btnMPasien = new javax.swing.JButton();
        btnCetak = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuDashboard = new javax.swing.JMenu();
        jMenuMasterData = new javax.swing.JMenu();
        itemApoteker = new javax.swing.JMenuItem();
        itemDokter = new javax.swing.JMenuItem();
        itemObat = new javax.swing.JMenuItem();
        itemPasien = new javax.swing.JMenuItem();
        itemSupplier = new javax.swing.JMenuItem();
        itemUser = new javax.swing.JMenuItem();
        jMenuTransaksi = new javax.swing.JMenu();
        jMenuLaporan = new javax.swing.JMenu();
        jMenuItemPembelian = new javax.swing.JMenuItem();
        jMenuItemPenjualan = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemObatMaster = new javax.swing.JMenuItem();
        jMenuItemSupplierMaster = new javax.swing.JMenuItem();
        jMenuItemDokter = new javax.swing.JMenuItem();
        jMenuItemApoteker = new javax.swing.JMenuItem();
        jMenuItemPasien = new javax.swing.JMenuItem();
        jMenuLogout = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Master Data Supplier");

        jPanel1.setBackground(new java.awt.Color(0, 184, 148));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Apotek Sumber Waras");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Login As : Admin");

        jPanel3.setBackground(new java.awt.Color(0, 184, 148));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Supplier"));

        tabelSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID Supplier", "Nama Supplier", "Alamat Supplier", "No Telepon"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelSupplier.setRowHeight(30);
        tabelSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelSupplierMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelSupplier);
        if (tabelSupplier.getColumnModel().getColumnCount() > 0) {
            tabelSupplier.getColumnModel().getColumn(0).setResizable(false);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/plus.png"))); // NOI18N
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        Jam.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        Jam.setForeground(new java.awt.Color(255, 255, 255));
        Jam.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Jam.setText("Waktu");

        Tanggal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Tanggal.setForeground(new java.awt.Color(255, 255, 255));
        Tanggal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Tanggal.setText("Tanggal");

        jPanel2.setBackground(new java.awt.Color(255, 255, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Form Supplier"));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        txtidSupplier.setBackground(new java.awt.Color(204, 204, 204));

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel3.setText("ID Supplier");

        getIDSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/reload.png"))); // NOI18N
        getIDSupplier.setToolTipText("Reload");
        getIDSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getIDSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getIDSupplierActionPerformed(evt);
            }
        });

        txtnamaSupplier.setToolTipText("Nama Obat");

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel5.setText("Nama Supplier");

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel8.setText("No Telepon");

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel7.setText("Alamat");

        txtAlamat.setColumns(20);
        txtAlamat.setRows(5);
        jScrollPane1.setViewportView(txtAlamat);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(getIDSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnoTelepon)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3)
                        .addComponent(jLabel5))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(txtidSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 120, Short.MAX_VALUE))
                        .addComponent(txtnamaSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(getIDSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnoTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(34, 34, 34))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtidSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtnamaSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(253, Short.MAX_VALUE)))
        );

        btnMObat.setText("Obat");
        btnMObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMObatActionPerformed(evt);
            }
        });

        btnMSupplier.setBackground(new java.awt.Color(255, 255, 102));
        btnMSupplier.setText("Supplier");

        btnMApoteker.setText("Apoteker");
        btnMApoteker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMApotekerActionPerformed(evt);
            }
        });

        btnMDokter.setText("Dokter");
        btnMDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMDokterActionPerformed(evt);
            }
        });

        btnMPasien.setText("Pasien");
        btnMPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMPasienActionPerformed(evt);
            }
        });

        btnCetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/printer-icon.png"))); // NOI18N
        btnCetak.setText("Print");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator2)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCetak, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(btnMObat, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnMSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnMApoteker, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnMDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnMPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 340, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Jam, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Tanggal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnMObat, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMApoteker, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Jam, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jLabel1)
                .addGap(1, 1, 1)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCetak, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );

        jMenuDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/home.png"))); // NOI18N
        jMenuDashboard.setText("Dashboard");
        jMenuDashboard.setAlignmentX(0.0F);
        jMenuDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuDashboardMouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenuDashboard);

        jMenuMasterData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/archive.png"))); // NOI18N
        jMenuMasterData.setText("Master Data");

        itemApoteker.setText("Data Apoteker");
        itemApoteker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemApotekerActionPerformed(evt);
            }
        });
        jMenuMasterData.add(itemApoteker);

        itemDokter.setText("Data Dokter");
        itemDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemDokterActionPerformed(evt);
            }
        });
        jMenuMasterData.add(itemDokter);

        itemObat.setText("Data Obat");
        itemObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemObatActionPerformed(evt);
            }
        });
        jMenuMasterData.add(itemObat);

        itemPasien.setText("Data Pasien");
        itemPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemPasienActionPerformed(evt);
            }
        });
        jMenuMasterData.add(itemPasien);

        itemSupplier.setText("Data Supplier");
        itemSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemSupplierActionPerformed(evt);
            }
        });
        jMenuMasterData.add(itemSupplier);

        itemUser.setText("Data User");
        jMenuMasterData.add(itemUser);

        jMenuBar1.add(jMenuMasterData);

        jMenuTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/dompet.png"))); // NOI18N
        jMenuTransaksi.setText("Transaksi");
        jMenuTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuTransaksiMouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenuTransaksi);

        jMenuLaporan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/printer-icon.png"))); // NOI18N
        jMenuLaporan.setText("Laporan");
        jMenuLaporan.setToolTipText("Report");

        jMenuItemPembelian.setText("Data Pembelian");
        jMenuItemPembelian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPembelianActionPerformed(evt);
            }
        });
        jMenuLaporan.add(jMenuItemPembelian);

        jMenuItemPenjualan.setText("Data Penjualan");
        jMenuItemPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPenjualanActionPerformed(evt);
            }
        });
        jMenuLaporan.add(jMenuItemPenjualan);

        jMenu1.setText("Master");

        jMenuItemObatMaster.setText("Data Obat");
        jMenuItemObatMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemObatMasterActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemObatMaster);

        jMenuItemSupplierMaster.setText("Data Supplier");
        jMenuItemSupplierMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSupplierMasterActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemSupplierMaster);

        jMenuItemDokter.setText("Data Dokter");
        jMenuItemDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDokterActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemDokter);

        jMenuItemApoteker.setText("Data Apoteker");
        jMenuItemApoteker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemApotekerActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemApoteker);

        jMenuItemPasien.setText("Data Pasien");
        jMenuItemPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPasienActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemPasien);

        jMenuLaporan.add(jMenu1);

        jMenuBar1.add(jMenuLaporan);

        jMenuLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logout.png"))); // NOI18N
        jMenuLogout.setToolTipText("Logout");
        jMenuLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuLogoutMouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenuLogout);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuLogoutMouseClicked
        getExit();
    }//GEN-LAST:event_jMenuLogoutMouseClicked

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        Simpan();
//        PopSupplier popApotek= new PopSupplier();
//        popApotek.setVisible(true);
//        popApotek.setAlwaysOnTop(rootPaneCheckingEnabled);
//        refreshTabel();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void getIDSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getIDSupplierActionPerformed
        Autonomor();
    }//GEN-LAST:event_getIDSupplierActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        editData();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        deleteData();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void tabelSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelSupplierMouseClicked
        // TODO add your handling code here:
        getKlik();
    }//GEN-LAST:event_tabelSupplierMouseClicked

    private void btnMObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMObatActionPerformed
        // TODO add your handling code here:
        MasterDataObat mdo = new MasterDataObat();
        mdo.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnMObatActionPerformed

    private void btnMDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMDokterActionPerformed
        MasterDataDokter mdd = new MasterDataDokter();
        mdd.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnMDokterActionPerformed

    private void btnMPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMPasienActionPerformed
        MasterDataPasien mdp = new MasterDataPasien();
        mdp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnMPasienActionPerformed

    private void btnMApotekerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMApotekerActionPerformed
        MasterDataApoteker mda = new MasterDataApoteker();
        mda.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnMApotekerActionPerformed

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
       try {
            HashMap data=new HashMap();
            InputStream is=this.getClass().getResourceAsStream("/com/apotikapp/views/report/master/LapDataSupplier.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(is);
            JasperPrint cetak_laporan = JasperFillManager.fillReport(jasperReport, data, Koneksi.getConnection());
            JasperViewer LaporanData=new JasperViewer(cetak_laporan, false);
            LaporanData.setTitle("Laporan Data ");
            LaporanData.setVisible(true);
        }catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(rootPane, "Data tidak ditemukan!", "TIDAK ADA DATA!", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnCetakActionPerformed

    private void itemApotekerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemApotekerActionPerformed
        MasterDataApoteker mda = new MasterDataApoteker();
        mda.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_itemApotekerActionPerformed

    private void itemDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemDokterActionPerformed
        MasterDataDokter mdd = new MasterDataDokter();
        mdd.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_itemDokterActionPerformed

    private void itemObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemObatActionPerformed
        MasterDataObat mdo = new MasterDataObat();
        mdo.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_itemObatActionPerformed

    private void itemPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemPasienActionPerformed
        MasterDataPasien mdp = new MasterDataPasien();
        mdp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_itemPasienActionPerformed

    private void itemSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemSupplierActionPerformed
        MasterDataSupplier mds = new MasterDataSupplier();
        mds.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_itemSupplierActionPerformed

    private void jMenuDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuDashboardMouseClicked
        String nama = null;
        MainMenu mn= new MainMenu(nama);
        mn.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuDashboardMouseClicked

    private void jMenuTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuTransaksiMouseClicked
        TransaksiPembelian mn= new TransaksiPembelian();
        mn.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuTransaksiMouseClicked

    private void jMenuItemPembelianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPembelianActionPerformed
        ReportMenu pop=new ReportMenu();
        pop.setVisible(true);
        pop.setAlwaysOnTop(rootPaneCheckingEnabled);
    }//GEN-LAST:event_jMenuItemPembelianActionPerformed

    private void jMenuItemPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPenjualanActionPerformed
        ReportMenu pop=new ReportMenu();
        pop.setVisible(true);
        pop.setAlwaysOnTop(rootPaneCheckingEnabled);
    }//GEN-LAST:event_jMenuItemPenjualanActionPerformed

    private void jMenuItemObatMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemObatMasterActionPerformed

        try {
            HashMap data=new HashMap();
            InputStream is=this.getClass().getResourceAsStream("/com/apotikapp/views/report/master/LapDataObat.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(is);
            JasperPrint cetak_laporan = JasperFillManager.fillReport(jasperReport, data, Koneksi.getConnection());
            JasperViewer LaporanData=new JasperViewer(cetak_laporan, false);
            LaporanData.setTitle("Laporan Data ");
            LaporanData.setVisible(true);
        }catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(rootPane, "Data tidak ditemukan!", "TIDAK ADA DATA!", JOptionPane.INFORMATION_MESSAGE);
        }

        //      try {
            //          File file = new File("");
            //            String sc=file.getAbsolutePath()+ "\\src\\com\\apotikapp\\views\\report\\master\\LapDataObat.jrxml";
            //            JasperDesign jasperDesign = JRXmlLoader.load(sc);
            //            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            //            JasperPrint jasperPrint = net.sf.jasperreports.engine.JasperFillManager.fillReport(jasperReport, null, Koneksi.getConnection());
            //            JasperViewer.viewReport(jasperPrint, false);
            //
            //
            //        }catch (Exception e) {
            //             JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
            //            System.out.println(e);
            //            JOptionPane.showMessageDialog(rootPane, "Data tidak ditemukan!", "TIDAK ADA DATA!", JOptionPane.INFORMATION_MESSAGE);
            //        }
    }//GEN-LAST:event_jMenuItemObatMasterActionPerformed

    private void jMenuItemSupplierMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSupplierMasterActionPerformed

        try {
            HashMap data=new HashMap();
            InputStream is=this.getClass().getResourceAsStream("/com/apotikapp/views/report/master/LapDataSupplier.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(is);
            JasperPrint cetak_laporan = JasperFillManager.fillReport(jasperReport, data, Koneksi.getConnection());
            JasperViewer LaporanData=new JasperViewer(cetak_laporan, false);
            LaporanData.setTitle("Laporan Data ");
            LaporanData.setVisible(true);
        }catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(rootPane, "Data tidak ditemukan!", "TIDAK ADA DATA!", JOptionPane.INFORMATION_MESSAGE);
        }

        //        try {
            //          File file = new File("src/com/apotikapp/views/report/master/LapDataSupplier.jrxml");
            //            JasperDesign jasperDesign = JRXmlLoader.load(file);
            //            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            //            JasperPrint jasperPrint = net.sf.jasperreports.engine.JasperFillManager.fillReport(jasperReport, null, Koneksi.getConnection());
            //            JasperViewer.viewReport(jasperPrint, false);
            //        }catch (Exception e) {
            //             JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
            //            System.out.println(e);
            //            JOptionPane.showMessageDialog(rootPane, "Data tidak ditemukan!", "TIDAK ADA DATA!", JOptionPane.INFORMATION_MESSAGE);
            //        }
    }//GEN-LAST:event_jMenuItemSupplierMasterActionPerformed

    private void jMenuItemDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDokterActionPerformed
        try {
            HashMap data=new HashMap();
            InputStream is=this.getClass().getResourceAsStream("/com/apotikapp/views/report/master/LapDataDokter.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(is);
            JasperPrint cetak_laporan = JasperFillManager.fillReport(jasperReport, data, Koneksi.getConnection());
            JasperViewer LaporanData=new JasperViewer(cetak_laporan, false);
            LaporanData.setTitle("Laporan Data ");
            LaporanData.setVisible(true);
        }catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(rootPane, "Data tidak ditemukan!", "TIDAK ADA DATA!", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemDokterActionPerformed

    private void jMenuItemApotekerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemApotekerActionPerformed
        try {
            HashMap data=new HashMap();
            InputStream is=this.getClass().getResourceAsStream("/report/LapDataApoteker.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(is);
            JasperPrint cetak_laporan = JasperFillManager.fillReport(jasperReport, data, Koneksi.getConnection());
            JasperViewer LaporanData=new JasperViewer(cetak_laporan, false);
            LaporanData.setTitle("Laporan Data ");
            LaporanData.setVisible(true);
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error " + e);
            System.out.println(e);
            JOptionPane.showMessageDialog(rootPane, "Data tidak ditemukan!", "TIDAK ADA DATA!", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemApotekerActionPerformed

    private void jMenuItemPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPasienActionPerformed

        try {
            HashMap data=new HashMap();
            InputStream is=this.getClass().getResourceAsStream("/com/apotikapp/views/report/master/LapDataPasien.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(is);
            JasperPrint cetak_laporan = JasperFillManager.fillReport(jasperReport, data, Koneksi.getConnection());
            JasperViewer LaporanData=new JasperViewer(cetak_laporan, false);
            LaporanData.setTitle("Laporan Data ");
            LaporanData.setVisible(true);
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error " + e);
            System.out.println(e);
            JOptionPane.showMessageDialog(rootPane, "Data tidak ditemukan!", "TIDAK ADA DATA!", JOptionPane.INFORMATION_MESSAGE);
        }

        //      try {
            //          File file = new File("src/com/apotikapp/views/report/master/LapDataPasien.jrxml");
            //            JasperDesign jasperDesign = JRXmlLoader.load(file);
            //            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            //            JasperPrint jasperPrint = net.sf.jasperreports.engine.JasperFillManager.fillReport(jasperReport, null, Koneksi.getConnection());
            //            JasperViewer.viewReport(jasperPrint, false);
            //        }catch (Exception e) {
            //            System.out.println(e);
            //            JOptionPane.showMessageDialog(rootPane, "Data tidak ditemukan!", "TIDAK ADA DATA!", JOptionPane.INFORMATION_MESSAGE);
            //        }
    }//GEN-LAST:event_jMenuItemPasienActionPerformed

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
            java.util.logging.Logger.getLogger(MasterDataSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MasterDataSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MasterDataSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MasterDataSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MasterDataSupplier().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Jam;
    private javax.swing.JLabel Tanggal;
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnMApoteker;
    private javax.swing.JButton btnMDokter;
    private javax.swing.JButton btnMObat;
    private javax.swing.JButton btnMPasien;
    private javax.swing.JButton btnMSupplier;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton getIDSupplier;
    private javax.swing.JMenuItem itemApoteker;
    private javax.swing.JMenuItem itemDokter;
    private javax.swing.JMenuItem itemObat;
    private javax.swing.JMenuItem itemPasien;
    private javax.swing.JMenuItem itemSupplier;
    private javax.swing.JMenuItem itemUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuDashboard;
    private javax.swing.JMenuItem jMenuItemApoteker;
    private javax.swing.JMenuItem jMenuItemDokter;
    private javax.swing.JMenuItem jMenuItemObatMaster;
    private javax.swing.JMenuItem jMenuItemPasien;
    private javax.swing.JMenuItem jMenuItemPembelian;
    private javax.swing.JMenuItem jMenuItemPenjualan;
    private javax.swing.JMenuItem jMenuItemSupplierMaster;
    private javax.swing.JMenu jMenuLaporan;
    private javax.swing.JMenu jMenuLogout;
    private javax.swing.JMenu jMenuMasterData;
    private javax.swing.JMenu jMenuTransaksi;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable tabelSupplier;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtidSupplier;
    private javax.swing.JTextField txtnamaSupplier;
    private javax.swing.JTextField txtnoTelepon;
    // End of variables declaration//GEN-END:variables

   
}
