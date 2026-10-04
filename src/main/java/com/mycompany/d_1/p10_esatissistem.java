/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.d_1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author hastr
 */
public class p10_esatissistem extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(p10_esatissistem.class.getName());
    DefaultTableModel model;

    public p10_esatissistem() {
        initComponents();
        txtSatisAdet.setEditable(false);
        tblUrunler.getTableHeader().setReorderingAllowed(false);

        DBHelper.veritabaniniHazirla();

        if (tblUrunler != null) {
            model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Hiçbir hücrenin tablo üzerinden doğrudan düzenlenmesine izin verme
                    return false;
                }
            };

            // Sütun başlıklarını ayarla
            model.setColumnIdentifiers(new Object[]{"ID", "Ürün Adı", "Fiyat", "Stok", "Satış Adedi"});
            tblUrunler.setModel(model);

            // 3. Tabloya Otomatik Sıralayıcı (TableRowSorter) Ekle
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);

            // ID (0. sütun), Fiyat (2. sütun), Stok (3. sütun) ve Satış Adedi (4. sütun) 
            // metin yerine sayısal olarak doğru sıralansın diye tiplerini tanımlıyoruz:
            sorter.setComparator(0, (o1, o2) -> Integer.compare((Integer) o1, (Integer) o2));
            sorter.setComparator(2, (o1, o2) -> Double.compare((Double) o1, (Double) o2));
            sorter.setComparator(3, (o1, o2) -> Integer.compare((Integer) o1, (Integer) o2));
            sorter.setComparator(4, (o1, o2) -> Integer.compare((Integer) o1, (Integer) o2));

            tblUrunler.setRowSorter(sorter);

            // Verileri listele
            urunleriListele();
        }
    }
// =========================================================================
    // --- 1. TEK DOSYA İÇİNDE GÖMÜLÜ VERİTABANI YARDIMCI SINIFI (DBHelper) ---
    // =========================================================================

    private static class DBHelper {

        private static final String URL = "jdbc:sqlite:eticaret.db";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL);
        }

        public static void veritabaniniHazirla() {
            String sqlUrunler = "CREATE TABLE IF NOT EXISTS urunler ("
                    + "id INTEGER PRIMARY KEY, "
                    + "urun_adi TEXT NOT NULL, "
                    + "fiyat REAL NOT NULL, "
                    + "stok INTEGER NOT NULL)";

            String sqlSatislar = "CREATE TABLE IF NOT EXISTS satislar ("
                    + "id INTEGER PRIMARY KEY, "
                    + "urun_id INTEGER, "
                    + "adet INTEGER NOT NULL, "
                    + "toplam_fiyat REAL NOT NULL, "
                    + "tarih DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (urun_id) REFERENCES urunler(id) ON DELETE CASCADE)";

            try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
                // SQLite Foreign Key desteğini aç
                stmt.execute("PRAGMA foreign_keys = ON;");
                stmt.execute(sqlUrunler);
                stmt.execute(sqlSatislar);

            } catch (SQLException e) {
                System.err.println("SQLite Veritabanı Hatası: " + e.getMessage());
            }
        }
    }

    // =========================================================================
    // --- 2. VERİTABANI VE İŞMANTIĞI METOTLARI (CRUD) ---
    // =========================================================================
    // Tabloyu Listeleme[cite: 1]
    public void urunleriListele() {
        if (model == null) {
            return;
        }
        model.setRowCount(0);

        // urunler ve satislar tablolarını birleştirerek toplam satılan adedi hesaplayan sorgu
        String sql = "SELECT u.id, u.urun_adi, u.fiyat, u.stok, COALESCE(SUM(s.adet), 0) AS satis_adedi "
                + "FROM urunler u "
                + "LEFT JOIN satislar s ON u.id = s.urun_id "
                + "GROUP BY u.id, u.urun_adi, u.fiyat, u.stok";

        try (Connection conn = DBHelper.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("urun_adi"),
                    rs.getDouble("fiyat"),
                    rs.getInt("stok"),
                    rs.getInt("satis_adedi")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Listeleme Hatası: " + e.getMessage());
        }
    }

    private void satisIslemiYap(int id, double fiyat, int adet) {
        double toplamFiyat = fiyat * adet;

        try (Connection conn = DBHelper.getConnection()) {
            conn.setAutoCommit(false); // Transaction Başlat

            // 1. Satış Kaydı Ekle
            String sqlSatis = "INSERT INTO satislar (urun_id, adet, toplam_fiyat) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlSatis)) {
                pstmt1.setInt(1, id);
                pstmt1.setInt(2, adet);
                pstmt1.setDouble(3, toplamFiyat);
                pstmt1.executeUpdate();
            }

            // 2. Stok Miktarını Düşür
            String sqlStok = "UPDATE urunler SET stok = stok - ? WHERE id = ?";
            try (PreparedStatement pstmt2 = conn.prepareStatement(sqlStok)) {
                pstmt2.setInt(1, adet);
                pstmt2.setInt(2, id);
                pstmt2.executeUpdate();
            }

            conn.commit(); // Transaction Onayla
            JOptionPane.showMessageDialog(this, adet + " adet satış yapıldı! Stok güncellendi.");
            urunleriListele();
            formTemizle();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Satış Hatası: " + e.getMessage());
        }
    }

    // Form Elemanlarını Temizleme
    private void formTemizle() {
        txtUrunAdi.setText("");
        txtFiyat.setText("");
        txtStok.setText("");
        txtSatisAdet.setText("");
    }

    /**
     * Creates new form p10_esatissitem
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtUrunAdi = new javax.swing.JTextField();
        txtFiyat = new javax.swing.JTextField();
        txtArama = new javax.swing.JTextField();
        txtStok = new javax.swing.JTextField();
        txtSatisAdet = new javax.swing.JTextField();
        btnEkle = new javax.swing.JButton();
        btnSatisYap = new javax.swing.JButton();
        btnGuncelle = new javax.swing.JButton();
        btnSil = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUrunler = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnCokluSat = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtArama.addActionListener(this::txtAramaActionPerformed);
        txtArama.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAramaKeyReleased(evt);
            }
        });

        btnEkle.setText("Ekle");
        btnEkle.addActionListener(this::btnEkleActionPerformed);

        btnSatisYap.setText("Satış Yap");
        btnSatisYap.addActionListener(this::btnSatisYapActionPerformed);

        btnGuncelle.setText("Güncelle");
        btnGuncelle.addActionListener(this::btnGuncelleActionPerformed);

        btnSil.setText("Sil");
        btnSil.addActionListener(this::btnSilActionPerformed);

        tblUrunler.setModel(new javax.swing.table.DefaultTableModel(
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
        tblUrunler.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUrunlerMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUrunler);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Ürün Adı :");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Fiyatı :");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Stok Adeti :");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Arama Çubuğu :");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Satılan Adet :");

        btnCokluSat.setText("Çoklu Sat");
        btnCokluSat.addActionListener(this::btnCokluSatActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtArama))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtFiyat, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtUrunAdi, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtSatisAdet, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(106, 106, 106)
                                .addComponent(btnGuncelle, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(btnSatisYap, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(44, 44, 44)
                                        .addComponent(btnCokluSat, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(41, 41, 41)
                                        .addComponent(btnSil, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(43, 59, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(txtArama))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSil, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGuncelle, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnSatisYap, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                            .addComponent(btnCokluSat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtUrunAdi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFiyat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtStok)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSatisAdet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(119, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblUrunlerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUrunlerMouseClicked
        int viewRow = tblUrunler.getSelectedRow();
        if (viewRow != -1) {
            int modelRow = tblUrunler.convertRowIndexToModel(viewRow);
            txtUrunAdi.setText(model.getValueAt(modelRow, 1).toString());
            txtFiyat.setText(model.getValueAt(modelRow, 2).toString());
            txtStok.setText(model.getValueAt(modelRow, 3).toString());
        }
    }//GEN-LAST:event_tblUrunlerMouseClicked

    private void btnSatisYapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSatisYapActionPerformed
        int selectedRow = tblUrunler.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen satış yapılacak ürünü tablodan seçin.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        double fiyat = (double) model.getValueAt(selectedRow, 2);
        int mevcutStok = (int) model.getValueAt(selectedRow, 3);
        int adet = 1;

        if (mevcutStok < adet) {
            JOptionPane.showMessageDialog(this, "Yetersiz stok! Ürün tükenmiş.");
            return;
        }

        double toplamFiyat = fiyat * adet;

        try (Connection conn = DBHelper.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Satış Kaydı Ekle (1 adet)
            String sqlSatis = "INSERT INTO satislar (urun_id, adet, toplam_fiyat) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlSatis)) {
                pstmt1.setInt(1, id);
                pstmt1.setInt(2, adet);
                pstmt1.setDouble(3, toplamFiyat);
                pstmt1.executeUpdate();
            }

            // 2. Stok Miktarını 1 Adet Düşür
            String sqlStok = "UPDATE urunler SET stok = stok - ? WHERE id = ?";
            try (PreparedStatement pstmt2 = conn.prepareStatement(sqlStok)) {
                pstmt2.setInt(1, adet);
                pstmt2.setInt(2, id);
                pstmt2.executeUpdate();
            }

            conn.commit(); // Transaction Onayla
            JOptionPane.showMessageDialog(this, "Satış yapıldı! Stok 1 azaldı, Satış Adedi 1 arttı.");
            urunleriListele();
            formTemizle();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Satış Hatası: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSatisYapActionPerformed

    private void txtAramaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAramaKeyReleased
        String arama = txtArama.getText();
        model.setRowCount(0);
        String sql = "SELECT u.id, u.urun_adi, u.fiyat, u.stok, COALESCE(SUM(s.adet), 0) AS satis_adedi "
                + "FROM urunler u "
                + "LEFT JOIN satislar s ON u.id = s.urun_id "
                + "WHERE u.urun_adi LIKE ? "
                + "GROUP BY u.id, u.urun_adi, u.fiyat, u.stok";

        try (Connection conn = DBHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + arama + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("urun_adi"),
                    rs.getDouble("fiyat"),
                    rs.getInt("stok"),
                    rs.getInt("satis_adedi")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Arama Hatası: " + e.getMessage());
        }
    }//GEN-LAST:event_txtAramaKeyReleased

    private void btnSilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSilActionPerformed
        int selectedRow = tblUrunler.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen silinecek ürünü tablodan seçin.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        String sql = "DELETE FROM urunler WHERE id = ?";

        try (Connection conn = DBHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Ürün silindi!");
            urunleriListele();
            formTemizle();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Silme Hatası: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSilActionPerformed

    private void btnEkleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEkleActionPerformed
        String sql = "INSERT INTO urunler (urun_adi, fiyat, stok) VALUES (?, ?, ?)";
        try (Connection conn = DBHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtUrunAdi.getText());
            pstmt.setDouble(2, Double.parseDouble(txtFiyat.getText()));
            pstmt.setInt(3, Integer.parseInt(txtStok.getText()));
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Ürün başarıyla eklendi!");
            urunleriListele();
            formTemizle();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ekleme Hatası: " + e.getMessage());
        }
    }//GEN-LAST:event_btnEkleActionPerformed

    private void btnGuncelleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuncelleActionPerformed
        int selectedRow = tblUrunler.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen güncellenecek ürünü tablodan seçin.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        String sql = "UPDATE urunler SET urun_adi = ?, fiyat = ?, stok = ? WHERE id = ?";

        try (Connection conn = DBHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtUrunAdi.getText());
            pstmt.setDouble(2, Double.parseDouble(txtFiyat.getText()));
            pstmt.setInt(3, Integer.parseInt(txtStok.getText()));
            pstmt.setInt(4, id);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Ürün güncellendi!");
            urunleriListele();
            formTemizle();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Güncelleme Hatası: " + e.getMessage());
        }
    }//GEN-LAST:event_btnGuncelleActionPerformed

    private void txtAramaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAramaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAramaActionPerformed

    private void btnCokluSatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCokluSatActionPerformed
        int selectedRow = tblUrunler.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen satış yapılmak istenen ürünü tablodan seçin.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        String urunAd = model.getValueAt(selectedRow, 1).toString();
        double fiyat = (double) model.getValueAt(selectedRow, 2);
        int mevcutStok = (int) model.getValueAt(selectedRow, 3);

        // Option Pane ile Satış Adedi Alma
        String girenAdetStr = JOptionPane.showInputDialog(
                this,
                "\"" + urunAd + "\" ürünü için satılacak adedi giriniz:\n(Mevcut Stok: " + mevcutStok + ")",
                "Çoklu Satış İşlemi",
                JOptionPane.QUESTION_MESSAGE
        );

        // İptal butonuna basıldıysa veya boş bırakıldıysa çık
        if (girenAdetStr == null || girenAdetStr.trim().isEmpty()) {
            return;
        }

        try {
            int satilacakAdet = Integer.parseInt(girenAdetStr.trim());

            // 1. Pozitif Sayı Kontrolü
            if (satilacakAdet <= 0) {
                JOptionPane.showMessageDialog(this, "Lütfen 0'dan büyük geçerli bir adet giriniz!", "Uyarı", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Stok Yeterlilik Kontrolü
            if (satilacakAdet > mevcutStok) {
                JOptionPane.showMessageDialog(
                        this,
                        "Yetersiz stok!\nİstenen: " + satilacakAdet + "\nMevcut Stok: " + mevcutStok,
                        "Stok Yetersiz",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Veritabanı İşlemi
            satisIslemiYap(id, fiyat, satilacakAdet);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lütfen geçerli bir tam sayı giriniz!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCokluSatActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new p10_esatissistem().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCokluSat;
    private javax.swing.JButton btnEkle;
    private javax.swing.JButton btnGuncelle;
    private javax.swing.JButton btnSatisYap;
    private javax.swing.JButton btnSil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUrunler;
    private javax.swing.JTextField txtArama;
    private javax.swing.JTextField txtFiyat;
    private javax.swing.JTextField txtSatisAdet;
    private javax.swing.JTextField txtStok;
    private javax.swing.JTextField txtUrunAdi;
    // End of variables declaration//GEN-END:variables
}
