/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.d_1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 *
 * @author hastr
 */
public class p9_derskayitsistemi extends javax.swing.JFrame {

    private static final String DB_URL = "jdbc:sqlite:derskayit.db";
    private int aktifOgrenciId = -1;
    private String aktifOgrenciAdSoyad = "";

    private DefaultTableModel modelTumDersler;
    private DefaultTableModel modelAldigimDersler;

    public p9_derskayitsistemi() {
        initComponents(); // NetBeans Design tarafının otomatik ürettiği arayüz kodları
        pnlDersKayit.setVisible(false);
        veritabaniKurulumYap();
        tabloModelleriniHazirla();
    }

    private Connection getCon() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Design'da oluşturduğunuz JTable'ların sütun başlıklarını kod ile ayarlıyoruz
    private void tabloModelleriniHazirla() {
        modelTumDersler = new DefaultTableModel(new String[]{"ID", "Kod", "Ders Adı", "Kredi", "Kontenjan"}, 0);
        tblTumDersler.setModel(modelTumDersler);

        modelAldigimDersler = new DefaultTableModel(new String[]{"ID", "Kod", "Ders Adı", "Kredi"}, 0);
        tblAldigimDersler.setModel(modelAldigimDersler);
    }

    // VERİTABANI VE TABLO YÜKLEME METOTLARI
    private void tablolariYukle() {
        // Tüm Dersler
        modelTumDersler.setRowCount(0);
        try (Connection con = getCon(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery("SELECT id, ders_kodu, ders_adi, kredi, kontenjan FROM dersler")) {
            while (rs.next()) {
                modelTumDersler.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("ders_kodu"), rs.getString("ders_adi"),
                    rs.getInt("kredi"), rs.getInt("kontenjan")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Aldığım Dersler
        modelAldigimDersler.setRowCount(0);
        String sql = "SELECT d.id, d.ders_kodu, d.ders_adi, d.kredi FROM kayitlar k "
                + "JOIN dersler d ON k.ders_id = d.id WHERE k.ogrenci_id = ?";
        try (Connection con = getCon(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, aktifOgrenciId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                modelAldigimDersler.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("ders_kodu"), rs.getString("ders_adi"), rs.getInt("kredi")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void veritabaniKurulumYap() {
        try (Connection con = getCon(); Statement stmt = con.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS ogrenciler (id INTEGER PRIMARY KEY AUTOINCREMENT, ogrenci_no TEXT UNIQUE, ad TEXT, soyad TEXT, sifre TEXT);");
            stmt.execute("CREATE TABLE IF NOT EXISTS dersler (id INTEGER PRIMARY KEY AUTOINCREMENT, ders_kodu TEXT UNIQUE, ders_adi TEXT, kontenjan INTEGER DEFAULT 30, kredi INTEGER);");
            stmt.execute("CREATE TABLE IF NOT EXISTS kayitlar (id INTEGER PRIMARY KEY AUTOINCREMENT, ogrenci_id INTEGER, ders_id INTEGER, UNIQUE(ogrenci_id, ders_id));");

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM ogrenciler");
            if (rs.next() && rs.getInt(1) == 0) {
                // --- Ogrenci Ekleme Kısmı ---
                stmt.execute("INSERT INTO ogrenciler (ogrenci_no, ad, soyad, sifre) VALUES ('24165010', 'Ramazan', 'Ergene', '123');");

                // --- SQL Ders Ekleme Kısmı ---
                stmt.execute("INSERT INTO dersler (ders_kodu, ders_adi, kontenjan, kredi) VALUES ('NYP101', 'Nesneye Yönelik Programlama', 30, 4);");
                stmt.execute("INSERT INTO dersler (ders_kodu, ders_adi, kontenjan, kredi) VALUES ('VTYS201', 'Veritabanı Yönetim Sistemleri', 25, 3);");
                stmt.execute("INSERT INTO dersler (ders_kodu, ders_adi, kontenjan, kredi) VALUES ('LOG202', 'Lojik Devreler II', 20, 4);");
                stmt.execute("INSERT INTO dersler (ders_kodu, ders_adi, kontenjan, kredi) VALUES ('OPT317', 'Optimizasyon Teorisi ve Teknikleri', 15, 3);");
                stmt.execute("INSERT INTO dersler (ders_kodu, ders_adi, kontenjan, kredi) VALUES ('YLM305', 'Yazılım Mühendisliği', 35, 3);");
                stmt.execute("INSERT INTO dersler (ders_kodu, ders_adi, kontenjan, kredi) VALUES ('VER102', 'Veri Yapıları ve Algoritmalar', 30, 4);");
                stmt.execute("INSERT INTO dersler (ders_kodu, ders_adi, kontenjan, kredi) VALUES ('AĞ401', 'Bilgisayar Ağları', 25, 3);");
                stmt.execute("INSERT INTO dersler (ders_kodu, ders_adi, kontenjan, kredi) VALUES ('YAP405', 'Yapay Zeka ve Makine Öğrenmesi', 10, 3);");
            }
        } catch (SQLException e) {
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

        pnlGiris = new javax.swing.JPanel();
        txtOgrenciNo = new javax.swing.JTextField();
        btnGiris = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSifre = new javax.swing.JPasswordField();
        pnlDersKayit = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTumDersler = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblAldigimDersler = new javax.swing.JTable();
        btnDersEkle = new javax.swing.JButton();
        btnDersBirak = new javax.swing.JButton();
        lblOgrenciBilgi = new javax.swing.JLabel();
        btnCikis = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnGiris.setText("Giriş Yap");
        btnGiris.addActionListener(this::btnGirisActionPerformed);

        jLabel1.setText("Öğrenci No:");

        jLabel2.setText("Şifre:");

        javax.swing.GroupLayout pnlGirisLayout = new javax.swing.GroupLayout(pnlGiris);
        pnlGiris.setLayout(pnlGirisLayout);
        pnlGirisLayout.setHorizontalGroup(
            pnlGirisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 286, Short.MAX_VALUE)
            .addGroup(pnlGirisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlGirisLayout.createSequentialGroup()
                    .addGap(28, 28, 28)
                    .addGroup(pnlGirisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlGirisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(pnlGirisLayout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSifre, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlGirisLayout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtOgrenciNo, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(pnlGirisLayout.createSequentialGroup()
                            .addGap(172, 172, 172)
                            .addComponent(btnGiris)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        pnlGirisLayout.setVerticalGroup(
            pnlGirisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 159, Short.MAX_VALUE)
            .addGroup(pnlGirisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlGirisLayout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addGroup(pnlGirisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtOgrenciNo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(pnlGirisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtSifre, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btnGiris)
                    .addContainerGap(33, Short.MAX_VALUE)))
        );

        tblTumDersler.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblTumDersler);

        tblAldigimDersler.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tblAldigimDersler);

        btnDersEkle.setText("Ekle");
        btnDersEkle.addActionListener(this::btnDersEkleActionPerformed);

        btnDersBirak.setText("Sil");
        btnDersBirak.addActionListener(this::btnDersBirakActionPerformed);

        lblOgrenciBilgi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblOgrenciBilgi.setText("jLabel3");

        btnCikis.setText("Çıkış Yap");
        btnCikis.addActionListener(this::btnCikisActionPerformed);

        javax.swing.GroupLayout pnlDersKayitLayout = new javax.swing.GroupLayout(pnlDersKayit);
        pnlDersKayit.setLayout(pnlDersKayitLayout);
        pnlDersKayitLayout.setHorizontalGroup(
            pnlDersKayitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDersKayitLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlDersKayitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlDersKayitLayout.createSequentialGroup()
                        .addComponent(lblOgrenciBilgi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCikis)
                        .addContainerGap())
                    .addGroup(pnlDersKayitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlDersKayitLayout.createSequentialGroup()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDersKayitLayout.createSequentialGroup()
                            .addGroup(pnlDersKayitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnDersBirak, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnDersEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(254, 254, 254)))))
        );
        pnlDersKayitLayout.setVerticalGroup(
            pnlDersKayitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDersKayitLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDersKayitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOgrenciBilgi, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCikis))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDersKayitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDersEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDersBirak, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlDersKayit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlGiris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(pnlGiris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlDersKayit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDersEkleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDersEkleActionPerformed
        int selectedRow = tblTumDersler.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen bir ders seçin.");
            return;
        }

        int dersId = (int) modelTumDersler.getValueAt(selectedRow, 0);
        int kontenjan = (int) modelTumDersler.getValueAt(selectedRow, 4);

        if (kontenjan <= 0) {
            JOptionPane.showMessageDialog(this, "Kontenjan dolmuştur!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String insertSql = "INSERT INTO kayitlar (ogrenci_id, ders_id) VALUES (?, ?)";
        String updateSql = "UPDATE dersler SET kontenjan = kontenjan - 1 WHERE id = ?";

        try (Connection con = getCon(); PreparedStatement pInsert = con.prepareStatement(insertSql); PreparedStatement pUpdate = con.prepareStatement(updateSql)) {

            pInsert.setInt(1, aktifOgrenciId);
            pInsert.setInt(2, dersId);
            pInsert.executeUpdate();

            pUpdate.setInt(1, dersId);
            pUpdate.executeUpdate();

            JOptionPane.showMessageDialog(this, "Ders eklendi.");
            tablolariYukle();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("UNIQUE")) {
                JOptionPane.showMessageDialog(this, "Bu dersi zaten aldınız!");
            } else {
                JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnDersEkleActionPerformed

    private void btnDersBirakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDersBirakActionPerformed
        int selectedRow = tblAldigimDersler.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen bırakılacak dersi seçin.");
            return;
        }

        int dersId = (int) modelAldigimDersler.getValueAt(selectedRow, 0);

        String deleteSql = "DELETE FROM kayitlar WHERE ogrenci_id = ? AND ders_id = ?";
        String updateSql = "UPDATE dersler SET kontenjan = kontenjan + 1 WHERE id = ?";

        try (Connection con = getCon(); PreparedStatement pDelete = con.prepareStatement(deleteSql); PreparedStatement pUpdate = con.prepareStatement(updateSql)) {

            pDelete.setInt(1, aktifOgrenciId);
            pDelete.setInt(2, dersId);
            pDelete.executeUpdate();

            pUpdate.setInt(1, dersId);
            pUpdate.executeUpdate();

            JOptionPane.showMessageDialog(this, "Ders bırakıldı.");
            tablolariYukle();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnDersBirakActionPerformed

    private void btnGirisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGirisActionPerformed
        String ogrenciNo = txtOgrenciNo.getText().trim();
        String sifre = new String(txtSifre.getPassword());

        String sql = "SELECT id, ad, soyad FROM ogrenciler WHERE ogrenci_no = ? AND sifre = ?";
        try (Connection con = getCon(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, ogrenciNo);
            pstmt.setString(2, sifre);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                aktifOgrenciId = rs.getInt("id");
                aktifOgrenciAdSoyad = rs.getString("ad") + " " + rs.getString("soyad");

                lblOgrenciBilgi.setText("Hoş Geldiniz, " + aktifOgrenciAdSoyad);
                tablolariYukle();
                JOptionPane.showMessageDialog(this, "Giriş Başarılı!");
                pnlGiris.setVisible(false);
                pnlDersKayit.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Hatalı Öğrenci No veya Şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Veritabanı Hatası: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnGirisActionPerformed

    private void btnCikisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCikisActionPerformed
        pnlDersKayit.setVisible(false);
        pnlGiris.setVisible(true);
    }//GEN-LAST:event_btnCikisActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new p9_derskayitsistemi().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCikis;
    private javax.swing.JButton btnDersBirak;
    private javax.swing.JButton btnDersEkle;
    private javax.swing.JButton btnGiris;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblOgrenciBilgi;
    private javax.swing.JPanel pnlDersKayit;
    private javax.swing.JPanel pnlGiris;
    private javax.swing.JTable tblAldigimDersler;
    private javax.swing.JTable tblTumDersler;
    private javax.swing.JTextField txtOgrenciNo;
    private javax.swing.JPasswordField txtSifre;
    // End of variables declaration//GEN-END:variables
}
