/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.apotikapp.views.transaction;

import Koneksi.Koneksi;
import Koneksi.PetugasSession;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author HP
 */
public class PopPenjualan extends javax.swing.JFrame {
    ResultSet Rs;
    private Object fm;
    /**
     * Creates new form PopPembelian
     */
    PetugasSession PetugasSession = new PetugasSession();
    //private Object StatUtils;
    public PopPenjualan() throws ClassNotFoundException {
        initComponents();
        lblLevel.setText(PetugasSession.getU_username());
        SelectPasien();
        SelectApoteker();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();  
        txttanggal.setText(dateFormat.format(cal.getTime()));
        txttanggal.setEnabled(false);
        TxtEmpty();
        txtIdBeli.setText(AutoIdBeli());
        Hidden();
        
        //untuk close frame
    }
    
    private void Hidden(){
        txtid_pelanggan.setVisible(false);
        txtIdBeli.setVisible(false);
        txtidPasien.setVisible(false);
        txtidApoteker.setVisible(false);
       txtnoFaktur.setEnabled(false);
    }
    
    public String idobat, namaobat, hargaobat, qty, expired, jenis;
    public String getIdObat(){
        return idobat;
    }
    
    public String getNamaObat(){
        return namaobat;
    }
    
    public String getJenisObat(){
        return jenis;
    }
    public String getQty(){
        return qty;
    }
    
    public String getHargaObat(){
        return hargaobat;
    }
    
    public void Itemterpilih(){
        DataObat fk = new DataObat();
        fk.popJual = this;
        //hrg=
        txtIdObat.setText(idobat);
        txtnamaObat.setText(namaobat);
        txtharga.setText(hargaobat);
        txtJenisObat.setSelectedItem(jenis);
        txtqty.setText("1");
        
    }
    
    private String AutoIdBeli(){
        String sql = "SELECT max(RIGHT(id_beli,4)) FROM tb_beli ORDER BY id_beli DESC";
        String IDBeli = null;
        try{
            Statement state  = Koneksi.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                  ResultSet.CONCUR_UPDATABLE);
            Rs = state.executeQuery(sql);
            while (Rs.next()){
                if(Rs.first() == false){
                IDBeli = ("BL0001");
                }else{
                    Rs.last();
                    int auto_id = Rs.getInt(1) + 1;
                    String no = String.valueOf(auto_id);
                    int noLong = no.length();
                    //MENGATUR jumlah 0
                    for (int j = 0; j < 4 - noLong; j++) {
                        no = "0" + no;
                    }
                   IDBeli = ("BL"+ no);
                }               
            }
            Rs.close();
            state.close();
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, "ERROR: \n" + e.toString(),
                    "Kesalahan", JOptionPane.WARNING_MESSAGE);
            System.out.println(""+ e.getMessage());
        }
        return IDBeli;
    }
    private void AutoIdJual(){
        DateFormat bln = new SimpleDateFormat("ddMMYYYY");
        String blnth = bln.format(Calendar.getInstance().getTime());
        String sql = "SELECT MAX(RIGHT(id_transaksi,6)) FROM tb_jual ORDER BY id_transaksi DESC";
        try{
            Statement state  = Koneksi.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                  ResultSet.CONCUR_UPDATABLE);
            Rs = state.executeQuery(sql);
            while (Rs.next()){
                if(Rs.first() == false){
                    txtnoFaktur.setText("PJL"+blnth+"000001");
                }else{
                    Rs.last();
                    int auto_id = Rs.getInt(1) + 1;
                    String no = String.valueOf(auto_id);
                    int noLong = no.length();
                    //MENGATUR jumlah 0
                    for (int j = 0; j < 6 - noLong; j++) {
                        no = "0" + no;
                    }
                    txtnoFaktur.setText("PJL"+blnth+ no);
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
    
    private void SelectPasien() throws ClassNotFoundException{
        try {
            Statement state  = Koneksi.getConnection().createStatement();
            Rs=state.executeQuery("SELECT id_pasien, nama_pasien FROM tb_pasien");                        
            cmbPasien.addItem("Pilih");
            while(Rs.next()){
                cmbPasien.addItem(Rs.getString("id_pasien") + " " + Rs.getString("nama_pasien"));
            }
             Rs.close();
             state.close();
             Koneksi.getConnection().close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error " + e);
        } 
    }
    
    private void SelectApoteker() throws ClassNotFoundException{
        try {
            Statement state  = Koneksi.getConnection().createStatement();
            Rs=state.executeQuery("SELECT id_apoteker, nama_apoteker FROM tb_apoteker");                        
            cmbApoteker.addItem("Pilih");
            while(Rs.next()){
                cmbApoteker.addItem(Rs.getString("id_apoteker") + " " + Rs.getString("nama_apoteker"));
            }
             Rs.close();
             state.close();
             Koneksi.getConnection().close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error " + e);
        } 
    }
    
    
    private void setKeranjang(){
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        //String total = null;
        Integer a = Integer.parseInt(txtharga.getText());
        Integer b = Integer.parseInt(txtqty.getText());
        Integer total = a*b;
        
        Integer jumlah =0;
        String data1 = txtIdObat.getText();
        String data2 = txtnamaObat.getText();
        String data3 = txtJenisObat.getSelectedItem().toString();
        String data4 = txtqty.getText();
        String data5 = txtharga.getText();
        Integer data6 = total;
                
        if(!(data1.equals("")) && !(data2.equals("")) && !(data3.equals("")) && !(data4.equals("")) && !(data5.equals("")) ){
            Object[] row = { data1, data2, data3, data4, data5, data6}; 
            DefaultTableModel model = (DefaultTableModel) tabelKeranjang.getModel();
            model.addRow(row);
//            
            
            autoSum();
                txtIdObat.setText(null);
                txtnamaObat.setText(null);
                txtJenisObat.setSelectedItem("Jenis Obat");             
                txtqty.setText(null);
                txtharga.setText(null);
                
        }else{
            JOptionPane.showMessageDialog(null, "Terdapat inputan yang kosong.");
        }

    }
    
    public void autoSum(){
            int total = 0;
            for (int i =0; i< tabelKeranjang.getRowCount(); i++){
                   int amount = Integer.parseInt((String)tabelKeranjang.getValueAt(i, 5).toString());
                   total += amount;
            }
            //txtSubTotal.setText(""+total);
            lblTotal.setText(""+total);
    }
    
    private void TxtEmpty(){
        //TableEmpty();
        BtnEnabled(false);
        txtnamaObat.setText("");
        txtid_selected.setText("");
        txtidPasien.setVisible(true);
        txtid_pelanggan.setVisible(false);
        lblTotal.setText("");
    }
    
    private void TableEmpty(){
        DefaultTableModel model = (DefaultTableModel) tabelKeranjang.getModel();
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
    }
    private void BtnEnabled(boolean x){
        btnDelRow.setEnabled(x);
    }
    
    private void GetData_View(){
        String row = Integer.toString(tabelKeranjang.getSelectedRow());
        txtid_selected.setText(row);
        BtnEnabled(true);
    }
    
    public void SaveData() throws ClassNotFoundException{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //tanggal hari ini
        String tglJual = dateFormat.format(cal.getTime());
        String noFaktur = txtnoFaktur.getText();
        String idPasien = txtidPasien.getText();
        String idObat =txtIdObat.getText();
        String idApoteker = txtidApoteker.getText();
        String tglFaktur = txttanggal.getText();
        String total = lblTotal.getText();
        String jenisTrx = "Penjualan";
        
        String id,id_barang, kode;
        Integer id_barang_masuk = 0,qty, jumlah, stok, not_found, empty = 0;
        
        DefaultTableModel model = (DefaultTableModel) tabelKeranjang.getModel();
        int rowCount = model.getRowCount();
         
        if(rowCount > 0 && !"".equals(noFaktur) && !"".equals(idPasien) && !"".equals(idApoteker)&& !"".equals(tglFaktur)){
            try{
                //masukkan data pada tb_beli
                Statement state  = Koneksi.getConnection().createStatement();
                state.executeUpdate("INSERT INTO tb_jual VALUES ('"+noFaktur+"','"+idPasien+"',"
                        + "'"+idApoteker+"',"
                        + "'"+tglFaktur+"',"
                        + "'"+jenisTrx+"',"
                        + "'"+total+"')");
               System.out.print("Berhasil tambah data\n");
                //getRefresh();                
                //JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan !");
                //refreshTabel();
                state.close();
                //this.dispose();
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, "Error " + e);
                JOptionPane.showMessageDialog(this, "Data Gagal Disimpan !");
            }
            
            //insert tabel Penjualan detail
            try {
                Statement state  = Koneksi.getConnection().createStatement();
                int baris = tabelKeranjang.getRowCount();
                String idBelii = txtIdBeli.getText();
                for (int i = 0; i < baris; i++) {
                    state.executeUpdate ("INSERT INTO tb_jual_detail(id_transaksi, id_obat, qty, subtotal) VALUES('"
                            + noFaktur+"','"
                            + ""+ tabelKeranjang.getValueAt(i, 0) +"','"
                            + ""+ tabelKeranjang.getValueAt(i, 3) 
                            +"','"+ tabelKeranjang.getValueAt(i, 5) +"')");
                    empty = 1;
                }
                state.close();
            } catch (Exception e) {
                //JOptionPane.showMessageDialog(null, "Error " + e);
                System.out.println(e +"simpan pembelian detail error");
            }
            JOptionPane.showMessageDialog(this, "Penjualan Berhasil Disimpan !");
            
            String petugas = PetugasSession.getU_username();
            try {
                HashMap data=new HashMap();
                data.put("petugas", petugas);
                data.put("id_transaksi", txtnoFaktur.getText());
                InputStream is=this.getClass().getResourceAsStream("/com/apotikapp/views/report/notaJual.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(is);
                JasperPrint cetak_laporan = JasperFillManager.fillReport(jasperReport, data, Koneksi.getConnection());
                JasperViewer LaporanData=new JasperViewer(cetak_laporan, false);
                LaporanData.setTitle("Nota Pembelian");
                LaporanData.setVisible(true);
            }catch (Exception e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(rootPane, "Data tidak ditemukan!", "TIDAK ADA DATA!", JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Terdapat inputan yang kosong.");
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

        jCalendar1 = new com.toedter.calendar.JCalendar();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtnoFaktur = new javax.swing.JTextField();
        txttanggal = new javax.swing.JTextField();
        getIDResep = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        cmbPasien = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        txtIdObat = new javax.swing.JTextField();
        txtqty = new javax.swing.JTextField();
        txtJenisObat = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtnamaObat = new javax.swing.JTextField();
        txtharga = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        setToKeranjang = new javax.swing.JButton();
        cariObat = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblTotal = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelKeranjang = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        btnTableEmpty = new javax.swing.JButton();
        btnDelRow = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtid_selected = new javax.swing.JTextField();
        btnSaveTrasaction = new javax.swing.JButton();
        txtid_pelanggan = new javax.swing.JTextField();
        txtidPasien = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lblLevel = new javax.swing.JLabel();
        cmbApoteker = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtidApoteker = new javax.swing.JTextField();
        txtIdBeli = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Transaksi Penjualan");
        setAlwaysOnTop(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 184, 148));

        jLabel5.setText("Tanggal");

        jLabel6.setText("No Faktur");

        txtnoFaktur.setBackground(new java.awt.Color(204, 204, 204));

        txttanggal.setBackground(new java.awt.Color(204, 204, 204));

        getIDResep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/reload.png"))); // NOI18N
        getIDResep.setToolTipText("Reload");
        getIDResep.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getIDResep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getIDResepActionPerformed(evt);
            }
        });

        jLabel7.setText("Pasien");

        cmbPasien.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbPasienItemStateChanged(evt);
            }
        });
        cmbPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPasienActionPerformed(evt);
            }
        });

        txtJenisObat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jenis Obat", "Kaplet", "Pil", "Kapsul", "Botol", "Sirup", "Salep" }));

        jLabel9.setText("ID Obat");

        jLabel10.setText("Nama Obat");

        jLabel11.setText("Jenis Obat");

        jLabel12.setText("Qty");

        jLabel13.setText("Harga");

        setToKeranjang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/sell.png"))); // NOI18N
        setToKeranjang.setText("Keranjang");
        setToKeranjang.setToolTipText("Reload");
        setToKeranjang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setToKeranjang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setToKeranjangActionPerformed(evt);
            }
        });

        cariObat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search.png"))); // NOI18N
        cariObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cariObatMouseClicked(evt);
            }
        });

        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblTotal.setText("total");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel2.setText("Rp.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        tabelKeranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Obat", "Nama Obat", "Jenis Obat", "Qty", "Harga", "Total"
            }
        ));
        tabelKeranjang.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabelKeranjang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelKeranjangMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabelKeranjangMouseReleased(evt);
            }
        });
        tabelKeranjang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tabelKeranjangKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tabelKeranjang);

        btnTableEmpty.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        btnTableEmpty.setText("Hapus Semua");
        btnTableEmpty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTableEmptyActionPerformed(evt);
            }
        });

        btnDelRow.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        btnDelRow.setText("Hapus Yang Terpilih");
        btnDelRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelRowActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Total");

        txtid_selected.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtid_selected.setEnabled(false);

        btnSaveTrasaction.setBackground(new java.awt.Color(255, 153, 0));
        btnSaveTrasaction.setText("Simpan Transaksi");
        btnSaveTrasaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveTrasactionActionPerformed(evt);
            }
        });

        txtidPasien.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtidPasien.setEnabled(false);

        jLabel3.setText("Login As");

        lblLevel.setText("jLabel4");

        cmbApoteker.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbApotekerItemStateChanged(evt);
            }
        });

        jLabel8.setText("Apoteker");

        txtidApoteker.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtidApoteker.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtIdObat, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cariObat, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(54, 54, 54)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(txtnamaObat, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtJenisObat, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(txtqty, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txtharga))
                                .addGap(18, 18, 18)
                                .addComponent(setToKeranjang, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnTableEmpty)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDelRow)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtid_selected, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSaveTrasaction, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txttanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtnoFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(getIDResep))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(txtid_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtIdBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(cmbPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtidPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(cmbApoteker, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtidApoteker, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(389, 389, 389)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(lblLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(70, 70, 70))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txttanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtid_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txtnoFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(getIDResep, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7))
                            .addComponent(txtidPasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbApoteker, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(txtidApoteker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(lblLevel))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cariObat, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel10)
                                .addComponent(jLabel11)
                                .addComponent(jLabel12)
                                .addComponent(jLabel13)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtharga)
                            .addComponent(txtqty)
                            .addComponent(txtIdObat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtJenisObat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setToKeranjang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtnamaObat))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnTableEmpty, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnDelRow, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtid_selected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSaveTrasaction, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 17, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void getIDResepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getIDResepActionPerformed
        AutoIdJual();
    }//GEN-LAST:event_getIDResepActionPerformed

    private void setToKeranjangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setToKeranjangActionPerformed
        setKeranjang();
    }//GEN-LAST:event_setToKeranjangActionPerformed

    private void cmbPasienItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbPasienItemStateChanged
        String[] nama_kategori = cmbPasien.getSelectedItem().toString().split("\\s+");
        String kode = nama_kategori[0];
        if(!kode.equals("Pilih")){                       
                try {
                    Statement state  = Koneksi.getConnection().createStatement();
                   Rs=state.executeQuery("SELECT id_pasien FROM tb_pasien WHERE id_pasien='"+kode+"'");
                    if(Rs.next()){
                        txtidPasien.setText(Rs.getString("id_pasien"));
                    }
                    Rs.close();
                    state.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error " + e);
                } catch (ClassNotFoundException ex) { 
                Logger.getLogger(PopPenjualan.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }else{
           
        }
    }//GEN-LAST:event_cmbPasienItemStateChanged

    private void cariObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cariObatMouseClicked
        DataObat fk = new DataObat();
        fk.popJual = this;
        fk.setVisible(true);
        fk.setAlwaysOnTop(true);

    }//GEN-LAST:event_cariObatMouseClicked

    private void tabelKeranjangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelKeranjangMouseClicked
        // TODO add your handling code here:
        GetData_View();
    }//GEN-LAST:event_tabelKeranjangMouseClicked

    private void tabelKeranjangMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelKeranjangMouseReleased
        // TODO add your handling code here:
        GetData_View();
    }//GEN-LAST:event_tabelKeranjangMouseReleased

    private void tabelKeranjangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelKeranjangKeyReleased
        // TODO add your handling code here:
        GetData_View();
    }//GEN-LAST:event_tabelKeranjangKeyReleased

    private void btnTableEmptyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTableEmptyActionPerformed
        // TODO add your handling code here:
        int ok = JOptionPane.showConfirmDialog(null, "Anda yakin ingin menghapus semua baris ini?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);
        if(ok==0) {
            TableEmpty();
        }
    }//GEN-LAST:event_btnTableEmptyActionPerformed

    private void btnDelRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelRowActionPerformed
        // TODO add your handling code here:
        int ok = JOptionPane.showConfirmDialog(null, "Anda yakin ingin menghapus baris ini?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);
        if(ok==0) {
            int row = Integer.parseInt(txtid_selected.getText());
            DefaultTableModel model = (DefaultTableModel) tabelKeranjang.getModel();
            model.removeRow(row);
            BtnEnabled(false);
        }
    }//GEN-LAST:event_btnDelRowActionPerformed

    private void btnSaveTrasactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveTrasactionActionPerformed
        try {
            // TODO add your handling code here:
            SaveData();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PopPenjualan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSaveTrasactionActionPerformed

    private void cmbApotekerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbApotekerItemStateChanged
        String[] nama_kategori = cmbApoteker.getSelectedItem().toString().split("\\s+");
        String kode = nama_kategori[0];
        if(!kode.equals("Pilih")){                       
                try {
                    Statement state  = Koneksi.getConnection().createStatement();
                   Rs=state.executeQuery("SELECT id_apoteker FROM tb_apoteker WHERE id_apoteker='"+kode+"'");
                    if(Rs.next()){
                        txtidApoteker.setText(Rs.getString("id_apoteker"));
                    }
                    Rs.close();
                    state.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error " + e);
                } catch (ClassNotFoundException ex) { 
                Logger.getLogger(PopPenjualan.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }else{
           
        }
    }//GEN-LAST:event_cmbApotekerItemStateChanged

    private void cmbPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPasienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPasienActionPerformed

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
            java.util.logging.Logger.getLogger(PopPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PopPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PopPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PopPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new PopPenjualan().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PopPenjualan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelRow;
    private javax.swing.JButton btnSaveTrasaction;
    private javax.swing.JButton btnTableEmpty;
    private javax.swing.JLabel cariObat;
    private javax.swing.JComboBox<String> cmbApoteker;
    private javax.swing.JComboBox<String> cmbPasien;
    private javax.swing.JButton getIDResep;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblLevel;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JButton setToKeranjang;
    private javax.swing.JTable tabelKeranjang;
    private javax.swing.JTextField txtIdBeli;
    private javax.swing.JTextField txtIdObat;
    private javax.swing.JComboBox<String> txtJenisObat;
    private javax.swing.JTextField txtharga;
    private javax.swing.JTextField txtidApoteker;
    private javax.swing.JTextField txtidPasien;
    private javax.swing.JTextField txtid_pelanggan;
    private javax.swing.JTextField txtid_selected;
    private javax.swing.JTextField txtnamaObat;
    private javax.swing.JTextField txtnoFaktur;
    private javax.swing.JTextField txtqty;
    private javax.swing.JTextField txttanggal;
    // End of variables declaration//GEN-END:variables


   
}
