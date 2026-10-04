/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.d_1;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hastr
 */
public class p5_satisotomasyon extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(p5_satisotomasyon.class.getName());

    /**
     * Creates new form p5_satisotomasyon
     */
    public p5_satisotomasyon() {
        initComponents();
        tabloKur();
        sepeteEkleme();
        sepetSilme();
    }

    public void tabloKur() {
        // Dukkan Tablosu
        DefaultTableModel dukkan = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableDukkan.setModel(dukkan);
        dukkan.addColumn("Ürün Adı");
        dukkan.addColumn("Tutar");

        dukkan.addRow(new Object[]{"İslamköy Ekmek", "40"});
        dukkan.addRow(new Object[]{"Kastamonu Taşköprü Sarımsağı", "25"});
        dukkan.addRow(new Object[]{"Tulum Peyniri", "450"});
        dukkan.addRow(new Object[]{"Denizli Horozu", "8000"});
        dukkan.addRow(new Object[]{"Erzurum Kıtlama Şeker", "50"});
        tableDukkan.getTableHeader().setReorderingAllowed(false);

        // Sepet Tablosu
        DefaultTableModel sepet = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSepet.setModel(sepet);
        sepet.addColumn("Ürün Adı");
        sepet.addColumn("Miktar");
        sepet.addColumn("Tutarı");
        tableSepet.getTableHeader().setReorderingAllowed(false);
    }

    private void sepeteEkleme() {
        tableDukkan.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    sepeteEkle();
                }
            }
        });
    }

    private void sepetSilme() {
        tableSepet.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedRow = tableSepet.getSelectedRow();
                    if (selectedRow != -1) {
                        DefaultTableModel sepet = (DefaultTableModel) tableSepet.getModel();
                        int miktar = Integer.parseInt(sepet.getValueAt(selectedRow, 1).toString());

                        if (miktar > 1) {
                            double birimFiyat = Double.parseDouble(sepet.getValueAt(selectedRow, 2).toString()) / miktar;
                            int yeniMiktar = miktar - 1;
                            sepet.setValueAt(yeniMiktar, selectedRow, 1);
                            sepet.setValueAt(yeniMiktar * birimFiyat, selectedRow, 2);
                        } else {
                            sepet.removeRow(selectedRow);
                        }
                        toplamTutarHesapla();
                    }
                }
            }
        });
    }

    private void toplamTutarHesapla() {
        double toplam = 0;
        for (int i = 0; i < tableSepet.getRowCount(); i++) {
            toplam += Double.parseDouble(tableSepet.getValueAt(i, 2).toString());
        }
        lblToplamTutar.setText("Toplam Tutar: " + toplam + " TL");
    }

    private void sepeteEkle() {
        int selectedRow = tableDukkan.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = tableDukkan.convertRowIndexToModel(selectedRow);

        String urunAd = tableDukkan.getValueAt(modelRow, 0).toString();
        double birimFiyat = Double.parseDouble(tableDukkan.getValueAt(modelRow, 1).toString());

        DefaultTableModel sepet = (DefaultTableModel) tableSepet.getModel();

        boolean urunBulundu = false;

        for (int i = 0; i < tableSepet.getRowCount(); i++) {
            String sepetUrunAd = tableSepet.getValueAt(i, 0).toString();

            if (sepetUrunAd.equalsIgnoreCase(urunAd)) {
                int mevcutMiktar = Integer.parseInt(tableSepet.getValueAt(i, 1).toString());
                int yeniMiktar = mevcutMiktar + 1;
                double yeniTutar = yeniMiktar * birimFiyat;

                tableSepet.setValueAt(yeniMiktar, i, 1);
                tableSepet.setValueAt(yeniTutar, i, 2);

                urunBulundu = true;
                break;
            }
        }

        if (!urunBulundu) {
            sepet.addRow(new Object[]{urunAd, 1, birimFiyat});
        }
        toplamTutarHesapla();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableSepet = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableDukkan = new javax.swing.JTable();
        lblToplamTutar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tableSepet.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tableSepet);

        tableDukkan.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tableDukkan);

        lblToplamTutar.setText("Toplam Tutar : 0TL");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(389, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblToplamTutar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(81, 81, 81))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(29, 29, 29)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(441, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblToplamTutar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(36, 36, 36)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(82, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        java.awt.EventQueue.invokeLater(() -> new p5_satisotomasyon().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblToplamTutar;
    private javax.swing.JTable tableDukkan;
    private javax.swing.JTable tableSepet;
    // End of variables declaration//GEN-END:variables
}
