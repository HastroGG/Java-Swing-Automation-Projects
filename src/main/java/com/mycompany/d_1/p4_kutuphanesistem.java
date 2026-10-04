/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.d_1;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hastr
 */
public class p4_kutuphanesistem extends javax.swing.JFrame {

    static class kitap {

        private String kitapID;
        private String kitapAd;
        private String kitapYazar;
        private String kitapKategori;
        private boolean kitapDurumu;

        public kitap(String kitapID, String kitapAd, String kitapYazar, String kitapKategori, boolean kitapDurumu) {
            this.kitapID = kitapID;
            this.kitapAd = kitapAd;
            this.kitapYazar = kitapYazar;
            this.kitapKategori = kitapKategori;
            this.kitapDurumu = kitapDurumu;
        }

        public String getId() {
            return kitapID;
        }

        public String getAd() {
            return kitapAd;
        }

        public String getYazar() {
            return kitapYazar;
        }

        public String getKategori() {
            return kitapKategori;
        }

        public boolean getDurum() {
            return kitapDurumu;
        }

        public void setDurum(boolean kitapDurumu) {
            this.kitapDurumu = kitapDurumu;
        }

        public String toFileFormat() {
            return kitapID + ";" + kitapAd + ";" + kitapYazar + ";" + kitapKategori + ";" + kitapDurumu;
        }

        public static kitap fromFileFormat(String line) {
            String[] p = line.split(";");
            if (p.length < 5) {
                return null;
            }
            return new kitap(p[0], p[1], p[2], p[3], Boolean.parseBoolean(p[4]));
        }
    }

    private List<kitap> kitapListesi = new ArrayList<>();
    private final String DOSYA_ADI = "kitaplar.txt";

    public p4_kutuphanesistem() {
        initComponents();
        tabloKur();
    }

    private void tabloKur() {
        DefaultTableModel tableModel = new DefaultTableModel() {
            public boolean tabloDuzenlenebilme(int row, int column) {
                return false;
            }
        };
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.setModel(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("Kitap Adı");
        tableModel.addColumn("Yazarı");
        tableModel.addColumn("Kategori");
        tableModel.addColumn("Durum");
    }

    private void dosyadanOkuAndTabloyaYukle() {
        kitapListesi.clear();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        File file = new File("kitaplar.txt");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "C:/ dizininde dosya oluşturulamadı! Lütfen NetBeans'i Yönetici Olarak Çalıştırın.\nHata: " + e.getMessage(),
                        "Erişim Hatası",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                if (!satir.trim().isEmpty()) {
                    kitap k = kitap.fromFileFormat(satir);
                    if (k != null) {
                        kitapListesi.add(k);
                        model.addRow(new Object[]{
                            k.getId(),
                            k.getAd(),
                            k.getYazar(),
                            k.getKategori(),
                            k.getDurum() ? "Mevcut" : "Ödünçte"
                        });
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Dosya okuma hatası: " + e.getMessage());
        }
    }

    private void dosyayaKaydet() {
        File file = new File(DOSYA_ADI);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            for (kitap k : kitapListesi) {
                writer.write(k.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "HATA! yönetici olarak çalıştırmayı deneyin" + e.getMessage(),
                    "Yazma Hatası",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tabloYenile() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        for (kitap k : kitapListesi) {
            model.addRow(new Object[]{
                k.getId(),
                k.getAd(),
                k.getYazar(),
                k.getKategori(),
                k.getDurum() ? "Mevcut" : "Ödünçte"
            });
        }
    }

    private void tabloyuDosyayaKaydet() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder satir = new StringBuilder();
                for (int j = 0; j < model.getColumnCount(); j++) {
                    satir.append(model.getValueAt(i, j).toString());
                    if (j < model.getColumnCount() - 1) {
                        satir.append(";");
                    }
                }
                writer.write(satir.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "HATA! " + e.getMessage());
        }
    }

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(p4_kutuphanesistem.class.getName());

    /**
     * Creates new form p4_kutuphanesistem
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        kitapEkle = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        txtID = new javax.swing.JTextField();
        txtAd = new javax.swing.JTextField();
        txtYazar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtKategori = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable1);

        kitapEkle.setText("Ekle");
        kitapEkle.addActionListener(this::kitapEkleActionPerformed);

        jButton1.setText("Ödünç Ver");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Teslim Al");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        txtAd.addActionListener(this::txtAdActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Kitap ID:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Kitap Yazar:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Kitap Ad:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Kitap Kategori:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(160, 160, 160))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(kitapEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAd, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                            .addComponent(txtYazar, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtKategori)
                            .addComponent(txtID))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAd, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtYazar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(kitapEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(130, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void kitapEkleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kitapEkleActionPerformed
        String id = txtID.getText().trim();
        String ad = txtAd.getText().trim();
        String yazar = txtYazar.getText().trim();
        String kategori = txtKategori.getText().trim();

        if (id.isEmpty() || ad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID ve Kitap Adı boş bırakılamaz!");
            return;
        }
        kitap yeniKitap = new kitap(id, ad, yazar, kategori, true);

        kitapListesi.add(yeniKitap);
        dosyayaKaydet();
        tabloYenile();

        txtID.setText("");
        txtAd.setText("");
        txtYazar.setText("");
        txtKategori.setText("");
        dosyadanOkuAndTabloyaYukle();
    }//GEN-LAST:event_kitapEkleActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen ödünç verilecek kitabı seçin.");
            return;
        }

        int modelRow = jTable1.convertRowIndexToModel(selectedRow);
        kitap secilenKitap = kitapListesi.get(modelRow);

        if (!secilenKitap.getDurum()) {
            JOptionPane.showMessageDialog(this, "Bu kitap zaten ödünç verilmiş!");
            return;
        }

        secilenKitap.setDurum(false);

        dosyayaKaydet();
        tabloYenile();
        dosyadanOkuAndTabloyaYukle();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen teslim alınacak kitabı seçin.");
            return;
        }

        int modelRow = jTable1.convertRowIndexToModel(selectedRow);
        kitap secilenKitap = kitapListesi.get(modelRow);

        if (secilenKitap.getDurum()) {
            JOptionPane.showMessageDialog(this, "Bu kitap zaten kütüphanede!");
            return;
        }
        secilenKitap.setDurum(true);

        dosyayaKaydet();
        tabloYenile();
        dosyadanOkuAndTabloyaYukle();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtAdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAdActionPerformed

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
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new p4_kutuphanesistem().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton kitapEkle;
    private javax.swing.JTextField txtAd;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtKategori;
    private javax.swing.JTextField txtYazar;
    // End of variables declaration//GEN-END:variables
}
