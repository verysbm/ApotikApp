/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.apotikapp.views.master;

import Koneksi.Koneksi;
import com.apotikapp.views.Login;
import com.apotikapp.views.MainMenu;
import com.apotikapp.views.transaction.TransaksiMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class MasterDataApoteker extends javax.swing.JFrame {
 ResultSet Rs;
 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * Creates new form MainMenu
     */
    public MasterDataApoteker() {
        initComponents();
        Datatabel();
        //Full Jframe
        setExtendedState(MAXIMIZED_BOTH);
        getJam();
    }
    
    public void getExit(){
        int confirm =JOptionPane.showConfirmDialog(this,"Anda yakin ingin keluar dari aplikasi ?","Keluar Aplikasi",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION){
            System.exit(0);
        }if(confirm == JOptionPane.NO_OPTION){   
        } 
    }
    
    public void getRefresh() {
        txtidApoteker.setText(null);
        txtnamaApoteker.setText(null);
        txtjenisKelamin.setSelectedItem("Laki-laki");
        txtAlamat.setText(null);
        txtnoTelepon.setText(null);
    }
    
    private void Autonomor(){
        String sql = "SELECT max(RIGHT(id_apoteker,4)) FROM tb_apoteker ORDER BY id_apoteker DESC";
        try{
            Statement state  = Koneksi.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                  ResultSet.CONCUR_UPDATABLE);
            Rs = state.executeQuery(sql);
            while (Rs.next()){
                if(Rs.first() == false){
                    txtidApoteker.setText("APT0001");
                }else{
                    Rs.last();
                    int auto_id = Rs.getInt(1) + 1;
                    String no = String.valueOf(auto_id);
                    int noLong = no.length();
                    //MENGATUR jumlah 0
                    for (int j = 0; j < 4 - noLong; j++) {
                        no = "0" + no;
                    }
                    txtidApoteker.setText("APT"+ no);
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
    DefaultTableModel model = (DefaultTableModel)tabelApoteker.getModel();
    model.setRowCount(0);
    Datatabel();
    }
    
    public void getJam() {
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                SimpleDateFormat tgl = new SimpleDateFormat("EEEE-dd-MMM-yyyy");
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
       tabel.addColumn("ID Apoteker");
       tabel.addColumn("Nama");
       tabel.addColumn("Jenis Kelamin");
       tabel.addColumn("No Telepon");
       tabel.addColumn("Alamat");
       try{
           Statement state  = Koneksi.getConnection().createStatement();
           Rs = state.executeQuery("Select * FROM tb_apoteker");
           while(Rs.next()){
               tabel.addRow(new Object[]{
                   Rs.getString(1),
                   Rs.getString(2),
                   Rs.getString(4),
                   Rs.getString(5),
                   Rs.getString(3),
                  
               });
               tabelApoteker.setModel(tabel);
           }
          // tabelApoteker.revalidate();
           //tabelApoteker.fireTableDataChanged();
           state.close();
           Rs.close();
       }
       catch(Exception e){
           System.out.println(e);
       }
    }
    
    int row=0;
    public void getKlik(){
        row=tabelApoteker.getSelectedRow();
        txtidApoteker.setText(tabelApoteker.getValueAt(row, 0).toString());
        txtnamaApoteker.setText(tabelApoteker.getValueAt(row, 1).toString());
        //txtjenisKelamin.setSelectedItem(tabelApoteker.getValueAt(row, 4).toString());
        txtjenisKelamin.setSelectedItem(String.valueOf(tabelApoteker.getValueAt(tabelApoteker.getSelectedRow(), 2)));
        txtAlamat.setText(tabelApoteker.getValueAt(row, 4).toString());
        txtnoTelepon.setText(tabelApoteker.getValueAt(row, 3).toString());
        
    }
    
    // Saving data 
    public void Simpan(){
        String id = txtidApoteker.getText();
        String nama = txtnamaApoteker.getText();
        String jenisKelamin = txtjenisKelamin.getSelectedItem().toString();
        String alamat = txtAlamat.getText();
        String notelp = txtnoTelepon.getText();        
        if(id.equals("")||nama.equals("")||jenisKelamin.equals("")||alamat.equals("")||notelp.equals("")){
        JOptionPane.showMessageDialog(this, "Lengkapi data anda");
        }else{
            try{
                Statement state  = Koneksi.getConnection().createStatement();
                state.executeUpdate("INSERT INTO tb_apoteker VALUES ('"+id+"','"+nama+"','"+alamat+"','"+jenisKelamin+"','"+notelp+"')");
                getRefresh();                
                JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan !");
                refreshTabel();
                state.close();
             // this.dispose();
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(this, "Data Gagal Disimpan !");
            }
        }
    }
    
    //Update Data
    public void editData(){
        String id = txtidApoteker.getText();
        String nama = txtnamaApoteker.getText();
        String jenisKelamin = txtjenisKelamin.getSelectedItem().toString();
        String alamat = txtAlamat.getText();
        String notelp = txtnoTelepon.getText();
        try{
            Statement state  = Koneksi.getConnection().createStatement();
            state.executeUpdate("UPDATE tb_apoteker SET nama_apoteker='"+nama
                    +"', alamat='"+alamat
                    +"', jk_apoteker='"+jenisKelamin
                    +"', tlp_apoteker='"+notelp
                    +"' WHERE id_apoteker='"+id+"';");
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
        String id = txtidApoteker.getText();
        try {
            Statement state  = Koneksi.getConnection().createStatement();
            state.executeUpdate("DELETE from tb_apoteker WHERE id_apoteker='"+id+"';");
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
        btnMObat = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelApoteker = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        Jam = new javax.swing.JLabel();
        Tanggal = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtidApoteker = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        getIDApoteker = new javax.swing.JButton();
        txtnamaApoteker = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtjenisKelamin = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtnoTelepon = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuDashboard = new javax.swing.JMenu();
        jMenuMasterData = new javax.swing.JMenu();
        jMenuTransaksi = new javax.swing.JMenu();
        jMenuLaporan = new javax.swing.JMenu();
        jMenuItemPembelian = new javax.swing.JMenuItem();
        jMenuItemPenjualan = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemObatMaster = new javax.swing.JMenuItem();
        jMenuItemSupplierMaster = new javax.swing.JMenuItem();
        jMenuItemDokter = new javax.swing.JMenuItem();
        jMenuItemApoteker = new javax.swing.JMenuItem();
        jMenuLogout = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 184, 148));

        btnMObat.setText("Obat");
        btnMObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMObatActionPerformed(evt);
            }
        });

        jButton2.setText("Supplier");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Apotek Klinik Bersama");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Login As : Admin");

        jPanel3.setBackground(new java.awt.Color(0, 184, 148));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Data"));

        tabelApoteker.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Apoteker", "Nama", "Jenis Kelamin ", "Alamat", "No Telepon"
            }
        ));
        tabelApoteker.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelApotekerMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelApoteker);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 819, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 819, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 345, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE))
        );

        jButton4.setText("Dokter");

        jButton5.setText("Pasien");

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

        jButton6.setBackground(new java.awt.Color(255, 255, 102));
        jButton6.setText("Apoteker");

        Jam.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        Jam.setForeground(new java.awt.Color(255, 255, 255));
        Jam.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        Tanggal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Tanggal.setForeground(new java.awt.Color(255, 255, 255));
        Tanggal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jPanel2.setBackground(new java.awt.Color(255, 255, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Form Apoteker"));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        txtidApoteker.setBackground(new java.awt.Color(204, 204, 204));

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel3.setText("ID Apoteker");

        getIDApoteker.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/reload.png"))); // NOI18N
        getIDApoteker.setToolTipText("Reload");
        getIDApoteker.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getIDApoteker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getIDApotekerActionPerformed(evt);
            }
        });

        txtnamaApoteker.setToolTipText("Nama Obat");

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel5.setText("Nama Apoteker");

        jLabel6.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel6.setText("Jenis Kelamin");

        txtjenisKelamin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laki-laki", "Perempuan" }));

        txtAlamat.setColumns(20);
        txtAlamat.setRows(5);
        jScrollPane1.setViewportView(txtAlamat);

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel7.setText("Alamat");

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel8.setText("No Telepon");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(326, Short.MAX_VALUE)
                .addComponent(getIDApoteker, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3)
                        .addComponent(jLabel5)
                        .addComponent(jLabel6)
                        .addComponent(jLabel7)
                        .addComponent(jLabel8))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addComponent(txtidApoteker, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                            .addGap(103, 103, 103))
                        .addComponent(txtnamaApoteker)
                        .addComponent(jScrollPane1)
                        .addComponent(txtnoTelepon)
                        .addComponent(txtjenisKelamin, 0, 304, Short.MAX_VALUE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(getIDApoteker, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtidApoteker, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtnamaApoteker, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(txtjenisKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(24, 24, 24)
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtnoTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8))
                    .addContainerGap()))
        );

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
                        .addGap(19, 19, 19)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnHapus))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(btnMObat, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Jam, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(237, 237, 237))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Jam, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnMObat, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );

        jMenuDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/home.png"))); // NOI18N
        jMenuDashboard.setText("Dashboard");
        jMenuDashboard.setAlignmentX(0.0F);
        jMenuDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuDashboardActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenuDashboard);

        jMenuMasterData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/archive.png"))); // NOI18N
        jMenuMasterData.setText("Master Data");
        jMenuBar1.add(jMenuMasterData);

        jMenuTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/dompet.png"))); // NOI18N
        jMenuTransaksi.setText("Transaksi");
        jMenuTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuTransaksiActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenuTransaksi);

        jMenuLaporan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/printer-icon.png"))); // NOI18N
        jMenuLaporan.setText("Laporan");
        jMenuLaporan.setToolTipText("Report");

        jMenuItemPembelian.setText("Pembelian");
        jMenuLaporan.add(jMenuItemPembelian);

        jMenuItemPenjualan.setText("Penjualan");
        jMenuLaporan.add(jMenuItemPenjualan);

        jMenu1.setText("Master");

        jMenuItemObatMaster.setText("Obat");
        jMenu1.add(jMenuItemObatMaster);

        jMenuItemSupplierMaster.setText("Supplier");
        jMenu1.add(jMenuItemSupplierMaster);

        jMenuItemDokter.setText("Dokter");
        jMenu1.add(jMenuItemDokter);

        jMenuItemApoteker.setText("Apoteker");
        jMenu1.add(jMenuItemApoteker);

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

    private void jMenuDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuDashboardActionPerformed
        // TODO add your handling code here:
        MainMenu mn= new MainMenu();
        mn.setVisible(true);
    }//GEN-LAST:event_jMenuDashboardActionPerformed

    private void jMenuTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuTransaksiActionPerformed
        // TODO add your handling code here:
        TransaksiMenu tm= new TransaksiMenu();
        tm.setVisible(true);
    }//GEN-LAST:event_jMenuTransaksiActionPerformed

    private void jMenuLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuLogoutMouseClicked
        // TODO add your handling code here:
        getExit();
    }//GEN-LAST:event_jMenuLogoutMouseClicked

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        //Simpan();
        PopApoteker popApotek= new PopApoteker();
        popApotek.setVisible(true);
        popApotek.setAlwaysOnTop(rootPaneCheckingEnabled);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnMObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMObatActionPerformed
        // TODO add your handling code here:
        
        MasterDataObat mdo = new MasterDataObat();
        mdo.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnMObatActionPerformed

    private void getIDApotekerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getIDApotekerActionPerformed
        Autonomor();
    }//GEN-LAST:event_getIDApotekerActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        editData();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        deleteData();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void tabelApotekerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelApotekerMouseClicked
        // TODO add your handling code here:
        getKlik();
    }//GEN-LAST:event_tabelApotekerMouseClicked

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
            java.util.logging.Logger.getLogger(MasterDataApoteker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MasterDataApoteker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MasterDataApoteker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MasterDataApoteker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new MasterDataApoteker().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Jam;
    private javax.swing.JLabel Tanggal;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnMObat;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton getIDApoteker;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuDashboard;
    private javax.swing.JMenuItem jMenuItemApoteker;
    private javax.swing.JMenuItem jMenuItemDokter;
    private javax.swing.JMenuItem jMenuItemObatMaster;
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
    private javax.swing.JTable tabelApoteker;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtidApoteker;
    private javax.swing.JComboBox<String> txtjenisKelamin;
    private javax.swing.JTextField txtnamaApoteker;
    private javax.swing.JTextField txtnoTelepon;
    // End of variables declaration//GEN-END:variables

   
}
