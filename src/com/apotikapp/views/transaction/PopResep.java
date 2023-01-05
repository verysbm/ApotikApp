/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.apotikapp.views.transaction;

import Koneksi.Koneksi;
import Koneksi.PetugasSession;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class PopResep extends javax.swing.JFrame {
    ResultSet Rs;
    private Object fm;
   
    /**
     * Creates new form PopPembelian
     */
    PetugasSession PetugasSession = new PetugasSession();
    //private Object StatUtils;
    public PopResep() throws ClassNotFoundException {
       // super(parent, modal);
        initComponents();
        lblLevel.setText(PetugasSession.getU_username());
        SelectDokter();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();  
        txttanggal.setText(dateFormat.format(cal.getTime()));
        TxtEmpty();
    }
    
    public String idobat, namaobat, hargaobat, qty, expired, jenis,idPasien,namaPasien;

    
    public String getIdObat(){
        return idobat;
    }
    
    public String getIdPasien(){
        return idPasien;
    }
    
    public String getNamaObat(){
        return namaobat;
    }
    
    public String getNamaPasien(){
        return namaPasien;
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
    
     public void Itempilih(){
        DataPasien fk = new DataPasien();
        fk.popResep = this;
        //hrg=
        txtPasien.setText(namaPasien);
        txtidPasien.setText(idPasien);
    }
     
    public void Itemterpilih(){
        DataObat fk = new DataObat();
        fk.popResep = this;
        //hrg=
       
        txtIdObat.setText(idobat);
        txtnamaObat.setText(namaobat);
        txtharga.setText(hargaobat);
        txtJenisObat.setSelectedItem(jenis);
        txtqty.setText("1");
        
    }
    

    private String AutoIdResep(){
        String sql = "SELECT max(RIGHT(id_resep,4)) FROM tb_resep ORDER BY id_resep DESC";
        String IDBeli = null;
        try{
            Statement state  = Koneksi.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                  ResultSet.CONCUR_UPDATABLE);
            Rs = state.executeQuery(sql);
            while (Rs.next()){
                if(Rs.first() == false){
                IDBeli = ("RSP0001");
                }else{
                    Rs.last();
                    int auto_id = Rs.getInt(1) + 1;
                    String no = String.valueOf(auto_id);
                    int noLong = no.length();
                    //MENGATUR jumlah 0
                    for (int j = 0; j < 4 - noLong; j++) {
                        no = "0" + no;
                    }
                   IDBeli = ("RSP"+ no);
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
    private void Autofaktur(){
        DateFormat bln = new SimpleDateFormat("ddMMYYYY");
        String blnth = bln.format(Calendar.getInstance().getTime());
        String sql = "SELECT MAX(RIGHT(no_faktur,6)) FROM tb_beli ORDER BY no_faktur DESC";
        try{
            Statement state  = Koneksi.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                  ResultSet.CONCUR_UPDATABLE);
            Rs = state.executeQuery(sql);
            while (Rs.next()){
                if(Rs.first() == false){
                    txtIdResep.setText("PBL"+blnth+"000001");
                }else{
                    Rs.last();
                    int auto_id = Rs.getInt(1) + 1;
                    String no = String.valueOf(auto_id);
                    int noLong = no.length();
                    //MENGATUR jumlah 0
                    for (int j = 0; j < 6 - noLong; j++) {
                        no = "0" + no;
                    }
                    txtIdResep.setText("PBL"+blnth+ no);
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
    
    private void SelectDokter() throws ClassNotFoundException{
        try {
            Statement state  = Koneksi.getConnection().createStatement();
            Rs=state.executeQuery("SELECT * FROM tb_dokter");                        
            cmbDokter.addItem("Pilih");
            while(Rs.next()){
                cmbDokter.addItem(Rs.getString("id_dokter") + " " + Rs.getString("nama_dokter"));
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
        String data6 = KodeRacik.getText();
        Integer data7 = total;
                
        if(!(data1.equals("")) && !(data2.equals("")) && !(data3.equals("")) && !(data4.equals("")) && !(data5.equals("")) ){
            Object[] row = { data6,data1, data2, data3, data4, data5, data7}; 
            DefaultTableModel model = (DefaultTableModel) tabelKeranjang.getModel();
            model.addRow(row);
//            
            
            autoSum();
                txtIdObat.setText(null);
                txtnamaObat.setText(null);
                txtJenisObat.setSelectedItem("Jenis Obat");             
                txtqty.setText(null);
                txtharga.setText(null);
                //tglExp.setDateFormatString(null);
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
            //lblTotal.setText("Rp" +total);
    }
    
    private void TxtEmpty(){
        //TableEmpty();
        BtnEnabled(false);
        txtnamaObat.setText("");
        txtid_selected.setText("");
        txtidDokter.setVisible(false);
        txtidPasien.setVisible(false);
        txtid_pelanggan.setVisible(false);
        //lblTotal.setText("");
        txtIdResep.setText(AutoIdResep());

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
        //String tgl = String.valueOf(fm.format(txttanggal.getDate()));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //tanggal hari ini
        String tglBeli = dateFormat.format(cal.getTime());
        String noFaktur = txtIdResep.getText();
        String idSupplier = txtidDokter.getText();
        String idObat =txtIdObat.getText();
        String idApoteker = PetugasSession.getU_id();
        String tglFaktur = txttanggal.getText();
        String idResep = AutoIdResep();
                
        String id,id_barang, kode;
        Integer id_barang_masuk = 0, jumlah, stok, not_found, empty = 0;
        
        DefaultTableModel model = (DefaultTableModel) tabelKeranjang.getModel();
        int rowCount = model.getRowCount();
         
        if(rowCount > 0 && !"".equals(idResep) && !"".equals(noFaktur) && !"".equals(idSupplier) && !"".equals(idApoteker)&& !"".equals(tglFaktur)){
            try{
                //masukkan data pada tb_beli
                Statement state  = Koneksi.getConnection().createStatement();
                state.executeUpdate("INSERT INTO tb_beli VALUES ('"+idResep+"','"+noFaktur+"',"
                        + "'"+idSupplier+"',"
                        + "'"+idApoteker+"',"
                        + "'"+tglBeli+"',"
                        + "'"+tglFaktur+"',"
                        + "'"+0+"')");
                
               System.out.print("Berhasil tambah data\n");
                //getRefresh();                
                //JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan !");
                //refreshTabel();
                state.close();
             // this.dispose();
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(this, "Data Gagal Disimpan !");
            }
            //get data dari tabel barang
            try {
                Statement state  = Koneksi.getConnection().createStatement();
                Rs = state.executeQuery("SELECT MAX(id_barang) as max FROM tb_barang");
                Rs.next();
                id_barang = Rs.getString("max");
               //konek.closekoneksi();
               state.close();
               System.out.print("Berhasil get data\n");
            }catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error " + e);
            }
            
            //Loop Data
            
            
            
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
        lblIdBeli = new javax.swing.JLabel();
        txtIdResep = new javax.swing.JTextField();
        txttanggal = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cmbDokter = new javax.swing.JComboBox<>();
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
        jLabel14 = new javax.swing.JLabel();
        setToKeranjang = new javax.swing.JButton();
        cariObat = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelKeranjang = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        btnTableEmpty = new javax.swing.JButton();
        btnDelRow = new javax.swing.JButton();
        txtid_selected = new javax.swing.JTextField();
        btnSaveTrasaction = new javax.swing.JButton();
        txtid_pelanggan = new javax.swing.JTextField();
        txtidDokter = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lblLevel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtPasien = new javax.swing.JTextField();
        cariPasien = new javax.swing.JLabel();
        txtidPasien = new javax.swing.JTextField();
        KodeRacik = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Transaksi Resep");
        setAlwaysOnTop(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 184, 148));

        jLabel5.setText("Tanggal");

        lblIdBeli.setText("ID Resep");

        txtIdResep.setBackground(new java.awt.Color(204, 204, 204));

        txttanggal.setBackground(new java.awt.Color(204, 204, 204));

        jLabel7.setText("Dokter");

        cmbDokter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDokterItemStateChanged(evt);
            }
        });

        txtJenisObat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jenis Obat", "Kaplet", "Pil", "Kapsul", "Botol", "Sirup", "Salep" }));

        jLabel9.setText("ID Obat");

        jLabel10.setText("Nama Obat");

        jLabel11.setText("Jenis Obat");

        jLabel12.setText("Qty");

        jLabel13.setText("Harga");

        jLabel14.setText("Kode Racik");

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

        tabelKeranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Racik", "ID Obat", "Nama Obat", "Jenis Obat", "Qty", "Harga"
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

        txtid_selected.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtid_selected.setEnabled(false);

        btnSaveTrasaction.setBackground(new java.awt.Color(255, 153, 0));
        btnSaveTrasaction.setText("Simpan Transaksi");
        btnSaveTrasaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveTrasactionActionPerformed(evt);
            }
        });

        txtidDokter.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtidDokter.setEnabled(false);

        jLabel3.setText("Login As");

        lblLevel.setText("jLabel4");

        jLabel8.setText("Pasien");

        cariPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search.png"))); // NOI18N
        cariPasien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cariPasienMouseClicked(evt);
            }
        });

        txtidPasien.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtidPasien.setEnabled(false);

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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                                    .addComponent(txtharga, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(KodeRacik, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(setToKeranjang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(lblLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblIdBeli)
                                    .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtIdResep, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txttanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtid_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(13, 13, 13)
                                        .addComponent(cmbDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cariPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtidDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtidPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txttanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(txtid_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdResep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblIdBeli)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbDokter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(txtidDokter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cariPasien)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(txtPasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(txtidPasien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cariObat)
                            .addComponent(jLabel9)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(lblLevel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13)))
                            .addComponent(jLabel14))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtqty)
                    .addComponent(txtIdObat)
                    .addComponent(txtJenisObat)
                    .addComponent(setToKeranjang, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(txtnamaObat)
                    .addComponent(KodeRacik)
                    .addComponent(txtharga))
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
                .addGap(18, 18, 18)
                .addComponent(btnSaveTrasaction, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
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
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void setToKeranjangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setToKeranjangActionPerformed
        setKeranjang();
    }//GEN-LAST:event_setToKeranjangActionPerformed

    private void cmbDokterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDokterItemStateChanged
        String[] nama_dokter = cmbDokter.getSelectedItem().toString().split("\\s+");
        String kode = nama_dokter[0];
        if(!kode.equals("Pilih")){                       
                try {
                    Statement state  = Koneksi.getConnection().createStatement();
                   Rs=state.executeQuery("SELECT id_dokter FROM tb_dokter WHERE id_dokter='"+kode+"'");
                    if(Rs.next()){
                        txtidDokter.setText(Rs.getString("id_dokter"));
                    }
                    Rs.close();
                    state.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error " + e);
                } catch (ClassNotFoundException ex) { 
                Logger.getLogger(PopResep.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }else{
           
        }
    }//GEN-LAST:event_cmbDokterItemStateChanged

    private void cariObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cariObatMouseClicked
        DataObat fk = new DataObat();
        fk.popResep =this;
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
            Logger.getLogger(PopResep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSaveTrasactionActionPerformed

    private void cariPasienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cariPasienMouseClicked
        DataPasien fk = new DataPasien();
        fk.popResep =this;
        fk.setVisible(true);
        fk.setAlwaysOnTop(true);
    }//GEN-LAST:event_cariPasienMouseClicked

   
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
            java.util.logging.Logger.getLogger(PopResep.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PopResep.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PopResep.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PopResep.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new PopResep().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PopResep.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField KodeRacik;
    private javax.swing.JButton btnDelRow;
    private javax.swing.JButton btnSaveTrasaction;
    private javax.swing.JButton btnTableEmpty;
    private javax.swing.JLabel cariObat;
    private javax.swing.JLabel cariPasien;
    private javax.swing.JComboBox<String> cmbDokter;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblIdBeli;
    private javax.swing.JLabel lblLevel;
    private javax.swing.JButton setToKeranjang;
    private javax.swing.JTable tabelKeranjang;
    private javax.swing.JTextField txtIdObat;
    private javax.swing.JTextField txtIdResep;
    private javax.swing.JComboBox<String> txtJenisObat;
    private javax.swing.JTextField txtPasien;
    private javax.swing.JTextField txtharga;
    private javax.swing.JTextField txtidDokter;
    private javax.swing.JTextField txtidPasien;
    private javax.swing.JTextField txtid_pelanggan;
    private javax.swing.JTextField txtid_selected;
    private javax.swing.JTextField txtnamaObat;
    private javax.swing.JTextField txtqty;
    private javax.swing.JTextField txttanggal;
    // End of variables declaration//GEN-END:variables


   
}
