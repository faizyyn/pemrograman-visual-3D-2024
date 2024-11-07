/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package praktikumkelima;

import java.sql.*;
import java.sql.Connection;
import javax.swing.*;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Asus
 */
public class Web extends javax.swing.JFrame {
    Connection conn;
    private DefaultTableModel modelKaryawan;
    private DefaultTableModel modelProyek;
    private DefaultTableModel modelTransaksi;
    /**
     * Creates new form Web
     */
    public Web() {
        initComponents();
        conn = koneksi.getConnection();
        
        initTableModels();
        loadDataKaryawan();
        loadDataProyek();
        loadDataTransaksi();
        loadComboBoxKaryawan();
        loadComboBoxProyek();
        clearTransaksiFields();
       

        clearTransaksiFields();
    }
    
    private void initTableModels() {
        modelKaryawan = new DefaultTableModel(new String[] { "ID", "Nama", "Jabatan", "Departemen" }, 0);
        tbl_karyawan.setModel(modelKaryawan);

        modelProyek = new DefaultTableModel(new String[] { "ID", "Nama Proyek", "Pengerjaan Mingguan" }, 0);
        tbl_proyek.setModel(modelProyek);

        modelTransaksi = new DefaultTableModel(new String[] { "ID Karyawan", "ID Proyek", "Nama Karyawan", "Peran",  "Nama Proyek", "Durasi Kerja Mingguan" }, 0);
        tb_transaksi.setModel(modelTransaksi);
    }
     private void loadDataKaryawan() {
        modelKaryawan.setRowCount(0);
        try {
            String sql = "SELECT * FROM tb_karyawan";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelKaryawan.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("jabatan"),
                    rs.getString("departemen")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error Load Data Karyawan: " + e.getMessage());
        }
    }

        private void loadDataProyek() {
            modelProyek.setRowCount(0);
            try {
                String sql = "SELECT * FROM tb_proyek";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    modelProyek.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nama_proyek"),
                        rs.getInt("durasi_pengerjaan") 
                    });
                }
            } catch (SQLException e) {
                System.out.println("Error Load Data Proyek: " + e.getMessage());
            }
        }

        private void loadDataTransaksi() {
            modelTransaksi.setRowCount(0);
            try {
                String sql = "SELECT t.id_karyawan, t.id_proyek, t.peran, " +
                             "k.nama AS nama_karyawan, p.nama_proyek AS nama_proyek, " +
                             "p.durasi_pengerjaan " +
                             "FROM tb_transaksi t " +
                             "JOIN tb_karyawan k ON t.id_karyawan = k.id " +
                             "JOIN tb_proyek p ON t.id_proyek = p.id";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    modelTransaksi.addRow(new Object[]{
                        rs.getInt("id_karyawan"),
                        rs.getInt("id_proyek"),
                        rs.getString("nama_karyawan"),
                        rs.getString("peran"),
                        rs.getString("nama_proyek"),
                        String.format("%d Minggu" ,rs.getInt("durasi_pengerjaan"))
                    });
                }
            } catch (SQLException e) {
                System.out.println("Error Load Data Transaksi: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Gagal memuat data transaksi: " + e.getMessage());
            }
        }
         private void tambahKaryawan() {
        if (nama.getText().trim().isEmpty() || 
        jabatan.getText().trim().isEmpty() || 
        departemen.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return; 
    }
          PreparedStatement ps = null;
        try {
        String sql = "INSERT INTO tb_karyawan (nama, jabatan, departemen) VALUES (?, ?, ?)";
        ps = conn.prepareStatement(sql);
        ps.setString(1, nama.getText().trim());
        ps.setString(2, jabatan.getText().trim());
        ps.setString(3, departemen.getText().trim());

        int affectedRows = ps.executeUpdate(); 
        if (affectedRows > 0) {
            JOptionPane.showMessageDialog(this, "Data Karyawan berhasil ditambahkan.");
            loadDataKaryawan();
            clearKaryawanFields();
            loadComboBoxKaryawan();
            clearTransaksiFields();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan data karyawan.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Kesalahan saat menambah Data Karyawan: " + e.getMessage(), 
                                      "Kesalahan", JOptionPane.ERROR_MESSAGE);
    } 
        
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Kesalahan saat menutup PreparedStatement: " + e.getMessage(), 
                                          "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    
    }
private void updateKaryawan() {
    // Validasi ID Karyawan
    if (id_karyawan.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "ID Karyawan harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return; // Hentikan eksekusi jika ID kosong
    }

    int id;
    try {
        id = Integer.parseInt(id_karyawan.getText().trim()); 
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID Karyawan tidak valid.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        return; 
    }

    
    if (nama.getText().trim().isEmpty() || 
        jabatan.getText().trim().isEmpty() || 
        departemen.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return; 
    }

    PreparedStatement ps = null; 
    try {
        String sql = "UPDATE tb_karyawan SET nama = ?, jabatan = ?, departemen = ? WHERE id = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, nama.getText().trim());
        ps.setString(2, jabatan.getText().trim());
        ps.setString(3, departemen.getText().trim());
        ps.setInt(4, id);

        int affectedRows = ps.executeUpdate(); 
        if (affectedRows > 0) {
            JOptionPane.showMessageDialog(this, "Data Karyawan berhasil diupdate.");
            loadDataKaryawan();
            clearKaryawanFields();
            loadComboBoxKaryawan();
            clearTransaksiFields();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data karyawan. ID tidak ditemukan.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Kesalahan saat memperbarui Data Karyawan: " + e.getMessage(), 
                                      "Kesalahan", JOptionPane.ERROR_MESSAGE);
    } 
    
}
  private void hapusKaryawan() {
    try {
        int id = Integer.parseInt(id_karyawan.getText()); 
        String sql = "DELETE FROM tb_karyawan WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data Karyawan berhasil dihapus.");
        loadDataKaryawan();
        clearKaryawanFields();
        loadComboBoxKaryawan();
        clearTransaksiFields();
    } catch (SQLException e) {
        System.out.println("Error Hapus Data Karyawan: " + e.getMessage());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID tidak valid");
    }
    loadComboBoxKaryawan();  
}
   private void tambahProyek() {
        if (id_proyek.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "ID proyek tidak boleh kosong.", 
                                      "Peringatan", JOptionPane.WARNING_MESSAGE);
        return; 
    }
    try {
        String sql = "INSERT INTO tb_proyek (nama_proyek, durasi_pengerjaan) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nmproyek.getText());  
        ps.setString(2, durasi.getText()); 
        ps.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data Proyek berhasil ditambahkan.");
        loadDataProyek();
        clearProyekFields(); 
        loadComboBoxProyek();
        clearTransaksiFields();
    } catch (SQLException e) {
        System.out.println("Error Tambah Data Proyek: " + e.getMessage());
    }
}
   private void updateProyek() {
         if (id_proyek.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "ID proyek tidak boleh kosong.", 
                                      "Peringatan", JOptionPane.WARNING_MESSAGE);
        return; 
    }
    try {
        int id = Integer.parseInt(id_proyek.getText()); 
        String sql = "UPDATE tb_proyek SET nama_proyek = ?, durasi_pengerjaan = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nmproyek.getText()); 
        ps.setString(2, durasi.getText());  
        ps.setInt(3, id);
        
        
        ps.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data Proyek berhasil diupdate.");
        loadDataProyek();
        clearProyekFields();  
        loadComboBoxProyek();
        clearTransaksiFields();
    } catch (SQLException e) {
        System.out.println("Error Update Data Proyek: " + e.getMessage());
    }
}
   private void hapusProyek() {
    try {
        int id = Integer.parseInt(id_proyek.getText()); 

        DefaultTableModel model = (DefaultTableModel) tbl_proyek.getModel();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            int tableId = (int) model.getValueAt(i, 0); 
            if (tableId == id) {
             
                String sqlDelete = "DELETE FROM tb_proyek WHERE id = ?";
                PreparedStatement psDelete = conn.prepareStatement(sqlDelete);
                psDelete.setInt(1, id);
                psDelete.executeUpdate();
                
                
                model.removeRow(i); 
                JOptionPane.showMessageDialog(this, "Data Proyek berhasil dihapus dari tabel dan database.");
                loadDataProyek();
                clearProyekFields();
                loadComboBoxProyek();
                clearTransaksiFields();
 
                clearProyekFields();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Data Proyek tidak ditemukan di tabel.");
        
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID tidak valid");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error menghapus data: " + e.getMessage());
    }
}
 private void loadComboBoxKaryawan() {
    kar1.removeAllItems();
    try {
        String sql = "SELECT id, nama, jabatan, departemen FROM tb_karyawan";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String tampilan = String.format("ID: %d - %s (%s, %s)", 
                rs.getInt("id"),
                rs.getString("nama"),
                rs.getString("jabatan"),
                rs.getString("departemen")
            );
            kar1.addItem(tampilan);
        }
    } catch (SQLException e) {
        System.out.println("Kesalahan Memuat Data Karyawan: " + e.getMessage());
    }
}

    private void loadComboBoxProyek() {
    pro2.removeAllItems();
    try {
        String sql = "SELECT id, nama_proyek, durasi_pengerjaan FROM tb_proyek";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String tampilan = String.format("ID: %d - %s (%s)", 
                rs.getInt("id"),
                rs.getString("nama_proyek"),
                rs.getString("durasi_pengerjaan")
            );
            pro2.addItem(tampilan);
        }
    } catch (SQLException e) {
        System.out.println("Kesalahan Memuat Data Proyek: " + e.getMessage());
    }
}
    private int getSelectedId(String textComboBox) {
    if (textComboBox == null) return -1;
    try {
        String idPart = textComboBox.substring(textComboBox.indexOf("ID: ") + 4);
        return Integer.parseInt(idPart.substring(0, idPart.indexOf(" -")));
    } catch (Exception e) {
        System.out.println("Error parsing ID: " + e.getMessage());
        return -1;
    }
}
    
        private void tambahTransaksi() {
            try {
                if (kar1.getSelectedItem() == null || pro2.getSelectedItem() == null || peran.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Pilih karyawan, proyek, dan isi peran terlebih dahulu");
                    return;
                }

                String sql = "INSERT INTO tb_transaksi (id_karyawan, id_proyek, peran) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);

                int idKaryawan = getSelectedId(kar1.getSelectedItem().toString());
                int idProyek = getSelectedId(pro2.getSelectedItem().toString());

                ps.setInt(1, idKaryawan);
                ps.setInt(2, idProyek);
                ps.setString(3, peran.getText().trim());

                int result = ps.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Data Transaksi berhasil ditambahkan");
                    loadDataTransaksi();
                    clearTransaksiFields();
                }
            } catch (SQLException e) {
                System.out.println("Kesalahan Menambah Data Transaksi: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Gagal menambah data: " + e.getMessage());
            }
        }
         private void updateTransaksi() {
                if (peran.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Peran tidak boleh kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return; 
        }
               PreparedStatement ps = null; 
    try {
        String sql = "UPDATE tb_transaksi SET peran = ? WHERE id_karyawan = ? AND id_proyek = ?";
        ps = conn.prepareStatement(sql);

        int idKaryawan = getSelectedId(kar1.getSelectedItem().toString());
        int idProyek = getSelectedId(pro2.getSelectedItem().toString());

        ps.setString(1, peran.getText().trim());
        ps.setInt(2, idKaryawan);
        ps.setInt(3, idProyek);

        int affectedRows = ps.executeUpdate(); 
        if (affectedRows > 0) {
            JOptionPane.showMessageDialog(this, "Data Transaksi berhasil diperbarui");
            loadDataTransaksi();
            clearTransaksiFields();
        } else {
            JOptionPane.showMessageDialog(this, "Tidak ada data transaksi yang diperbarui. Pastikan ID karyawan dan proyek benar.", 
                                          "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Kesalahan Memperbarui Data Transaksi: " + e.getMessage(), 
                                      "Kesalahan", JOptionPane.ERROR_MESSAGE);
    } 
    }

    private void hapusTransaksi() {
    // Memastikan ComboBox tidak kosong
    if (kar1.getSelectedItem() == null || pro2.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(this, "Silakan pilih karyawan dan proyek sebelum menghapus transaksi.", 
                                      "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    PreparedStatement ps = null; 
    try {
        String sql = "DELETE FROM tb_transaksi WHERE id_karyawan = ? AND id_proyek = ?";
        ps = conn.prepareStatement(sql);
        
        int idKaryawan = getSelectedId(kar1.getSelectedItem().toString());
        int idProyek = getSelectedId(pro2.getSelectedItem().toString());
        
        ps.setInt(1, idKaryawan);
        ps.setInt(2, idProyek);
        
        int affectedRows = ps.executeUpdate(); 
        if (affectedRows > 0) {
            JOptionPane.showMessageDialog(this, "Data Transaksi berhasil dihapus.");
            loadDataTransaksi();
            clearTransaksiFields();
        } else {
            JOptionPane.showMessageDialog(this, "Tidak ada transaksi yang dihapus. Pastikan ID karyawan dan proyek benar.", 
                                          "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Kesalahan Menghapus Data Transaksi: " + e.getMessage(), 
                                      "Error", JOptionPane.ERROR_MESSAGE);
    } 
    
}
   private void clearKaryawanFields() {
        id_karyawan.setText("");
        nama.setText("");
        jabatan.setText("");
        departemen.setText("");
    }

    private void clearProyekFields() {
        id_proyek.setText("");
        nmproyek.setText("");
        durasi.setText("");
    }
    
    private void clearTransaksiFields() {
        kar1.setSelectedIndex(-1);
        pro2.setSelectedIndex(-1);
        peran.setText("");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        Karyawan = new javax.swing.JPanel();
        Atas = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        bawah = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        tengah = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        id_karyawan = new javax.swing.JTextField();
        nama = new javax.swing.JTextField();
        jabatan = new javax.swing.JTextField();
        departemen = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_karyawan = new javax.swing.JTable();
        tmbhk = new javax.swing.JButton();
        upk = new javax.swing.JButton();
        hpsk = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        Atas1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        bawah1 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        tengah1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        id_proyek = new javax.swing.JTextField();
        nmproyek = new javax.swing.JTextField();
        durasi = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_proyek = new javax.swing.JTable();
        tmbhp = new javax.swing.JButton();
        upp = new javax.swing.JButton();
        hpsp = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        Atas2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        bawah2 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        tengah2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        peran = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tb_transaksi = new javax.swing.JTable();
        tmbht = new javax.swing.JButton();
        upt = new javax.swing.JButton();
        hpst = new javax.swing.JButton();
        kar1 = new javax.swing.JComboBox<>();
        pro2 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBackground(new java.awt.Color(204, 153, 255));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        Karyawan.setBackground(new java.awt.Color(204, 204, 255));
        Karyawan.setLayout(new java.awt.BorderLayout());

        Atas.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        jLabel1.setText("Karyawan");
        Atas.add(jLabel1);

        jLabel2.setIcon(new javax.swing.ImageIcon("C:\\Users\\Asus\\Downloads\\Foto Buat GUI\\Konsep_Rapat_Kerja_Tim_Konsep__Clipart_Kantor__Tim__Kerja_PNG_dan_Vektor_dengan_Background_Transparan_untuk_Unduh_Gratis-removebg-preview.png")); // NOI18N
        Atas.add(jLabel2);

        Karyawan.add(Atas, java.awt.BorderLayout.PAGE_START);

        bawah.setBackground(new java.awt.Color(204, 204, 255));
        bawah.setPreferredSize(new java.awt.Dimension(531, 50));
        bawah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setBackground(new java.awt.Color(255, 204, 204));
        jButton1.setText("Exit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        bawah.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, -1, -1));

        jButton2.setBackground(new java.awt.Color(204, 255, 204));
        jButton2.setText("Reset");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        bawah.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, -1, -1));

        Karyawan.add(bawah, java.awt.BorderLayout.PAGE_END);

        tengah.setBackground(new java.awt.Color(153, 153, 255));

        jLabel3.setText("ID                 :");

        jLabel4.setText("Nama           :");

        jLabel5.setText("Jabatan        :");

        jLabel6.setText("Departemen :");

        jabatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jabatanActionPerformed(evt);
            }
        });

        tbl_karyawan.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_karyawan.setEnabled(false);
        jScrollPane1.setViewportView(tbl_karyawan);

        tmbhk.setBackground(new java.awt.Color(153, 204, 255));
        tmbhk.setText("Tambah");
        tmbhk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tmbhkActionPerformed(evt);
            }
        });

        upk.setBackground(new java.awt.Color(204, 255, 204));
        upk.setText("Update");
        upk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upkActionPerformed(evt);
            }
        });

        hpsk.setBackground(new java.awt.Color(255, 204, 204));
        hpsk.setText("Hapus");
        hpsk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hpskActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tengahLayout = new javax.swing.GroupLayout(tengah);
        tengah.setLayout(tengahLayout);
        tengahLayout.setHorizontalGroup(
            tengahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tengahLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(tengahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tengahLayout.createSequentialGroup()
                        .addComponent(tmbhk, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(upk, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(hpsk, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tengahLayout.createSequentialGroup()
                        .addGroup(tengahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(31, 31, 31)
                        .addGroup(tengahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(departemen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                            .addComponent(jabatan, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nama)))
                    .addGroup(tengahLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(30, 30, 30)
                        .addComponent(id_karyawan, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(135, Short.MAX_VALUE))
        );
        tengahLayout.setVerticalGroup(
            tengahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tengahLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(tengahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(id_karyawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tengahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(14, 14, 14)
                .addGroup(tengahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jabatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(tengahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(departemen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(40, 40, 40)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(tengahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tmbhk)
                    .addComponent(upk)
                    .addComponent(hpsk))
                .addContainerGap(79, Short.MAX_VALUE))
        );

        Karyawan.add(tengah, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Karyawan", Karyawan);

        Atas1.setBackground(new java.awt.Color(204, 204, 255));

        jLabel7.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        jLabel7.setText("Proyek");
        Atas1.add(jLabel7);

        jLabel8.setIcon(new javax.swing.ImageIcon("C:\\Users\\Asus\\Downloads\\Foto Buat GUI\\Pekerja_Kantor_Pria_Mengetik_Di_Komputer__Papan_Ketik__Internet__Orang_PNG_dan_Vektor_dengan_Background_Transparan_untuk_Unduh_Gratis-removebg-preview.png")); // NOI18N
        Atas1.add(jLabel8);

        bawah1.setBackground(new java.awt.Color(204, 204, 255));
        bawah1.setPreferredSize(new java.awt.Dimension(531, 50));
        bawah1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton6.setBackground(new java.awt.Color(255, 204, 204));
        jButton6.setText("Exit");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        bawah1.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, -1, -1));

        jButton7.setBackground(new java.awt.Color(204, 255, 204));
        jButton7.setText("Reset");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        bawah1.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, -1, -1));

        tengah1.setBackground(new java.awt.Color(153, 153, 255));

        jLabel9.setText("ID                         :");

        jLabel10.setText("Nama Proyek        :");

        jLabel11.setText("Durasi Pengerjaan :");

        durasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                durasiActionPerformed(evt);
            }
        });

        tbl_proyek.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_proyek.setEnabled(false);
        jScrollPane2.setViewportView(tbl_proyek);

        tmbhp.setBackground(new java.awt.Color(153, 204, 255));
        tmbhp.setText("Tambah");
        tmbhp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tmbhpActionPerformed(evt);
            }
        });

        upp.setBackground(new java.awt.Color(204, 255, 204));
        upp.setText("Update");
        upp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uppActionPerformed(evt);
            }
        });

        hpsp.setBackground(new java.awt.Color(255, 204, 204));
        hpsp.setText("Hapus");
        hpsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hpspActionPerformed(evt);
            }
        });

        jLabel12.setText("Minggu");

        javax.swing.GroupLayout tengah1Layout = new javax.swing.GroupLayout(tengah1);
        tengah1.setLayout(tengah1Layout);
        tengah1Layout.setHorizontalGroup(
            tengah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tengah1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(tengah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tengah1Layout.createSequentialGroup()
                        .addGroup(tengah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tengah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(id_proyek, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(tengah1Layout.createSequentialGroup()
                                .addGroup(tengah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(durasi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                                    .addComponent(nmproyek))
                                .addGap(26, 26, 26)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(tengah1Layout.createSequentialGroup()
                        .addComponent(tmbhp, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(upp, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(hpsp, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(68, Short.MAX_VALUE))
        );
        tengah1Layout.setVerticalGroup(
            tengah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tengah1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(tengah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(id_proyek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tengah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nmproyek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(14, 14, 14)
                .addGroup(tengah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(durasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(38, 38, 38)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(tengah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tmbhp)
                    .addComponent(upp)
                    .addComponent(hpsp))
                .addContainerGap(103, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 633, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Atas1, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tengah1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bawah1, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 692, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Atas1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(tengah1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(bawah1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Proyek", jPanel2);

        Atas2.setBackground(new java.awt.Color(204, 204, 255));

        jLabel13.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        jLabel13.setText("Transaksi");
        Atas2.add(jLabel13);

        jLabel14.setIcon(new javax.swing.ImageIcon("C:\\Users\\Asus\\Downloads\\Foto Buat GUI\\รูปไอคอนการชำระเงิน_PNG___รูปหกด้าน__อีคอมเมิร์ซ__เช็คเอาท์ภาพ_PNG_และ_PSD_สำหรับดาวน์โหลดฟรี-removebg-preview.png")); // NOI18N
        Atas2.add(jLabel14);

        bawah2.setBackground(new java.awt.Color(204, 204, 255));
        bawah2.setPreferredSize(new java.awt.Dimension(531, 50));
        bawah2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton11.setBackground(new java.awt.Color(255, 204, 204));
        jButton11.setText("Exit");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        bawah2.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, -1, -1));

        jButton12.setBackground(new java.awt.Color(204, 255, 204));
        jButton12.setText("Reset");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        bawah2.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, -1, -1));

        tengah2.setBackground(new java.awt.Color(153, 153, 255));
        tengah2.setPreferredSize(new java.awt.Dimension(566, 550));

        jLabel15.setText("Karyawan :");

        jLabel16.setText("Projek     :");

        jLabel17.setText("Peran      :");

        peran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                peranActionPerformed(evt);
            }
        });

        tb_transaksi.setModel(new javax.swing.table.DefaultTableModel(
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
        tb_transaksi.setEnabled(false);
        jScrollPane3.setViewportView(tb_transaksi);

        tmbht.setBackground(new java.awt.Color(153, 204, 255));
        tmbht.setText("Tambah");
        tmbht.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tmbhtActionPerformed(evt);
            }
        });

        upt.setBackground(new java.awt.Color(204, 255, 204));
        upt.setText("Update");
        upt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uptActionPerformed(evt);
            }
        });

        hpst.setBackground(new java.awt.Color(255, 204, 204));
        hpst.setText("Hapus");
        hpst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hpstActionPerformed(evt);
            }
        });

        kar1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        pro2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout tengah2Layout = new javax.swing.GroupLayout(tengah2);
        tengah2.setLayout(tengah2Layout);
        tengah2Layout.setHorizontalGroup(
            tengah2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tengah2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(tengah2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tengah2Layout.createSequentialGroup()
                        .addGroup(tengah2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(31, 31, 31)
                        .addGroup(tengah2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(peran, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(kar1, javax.swing.GroupLayout.Alignment.LEADING, 0, 169, Short.MAX_VALUE)
                            .addComponent(pro2, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(tengah2Layout.createSequentialGroup()
                        .addComponent(tmbht, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(upt, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(hpst, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        tengah2Layout.setVerticalGroup(
            tengah2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tengah2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(tengah2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(kar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tengah2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(pro2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(tengah2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(peran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(33, 33, 33)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(tengah2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tmbht)
                    .addComponent(upt)
                    .addComponent(hpst))
                .addContainerGap(106, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 633, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 9, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Atas2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tengah2, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
                        .addComponent(bawah2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 692, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Atas2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(tengah2, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(bawah2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Transaksi", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 713, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int option = JOptionPane.showConfirmDialog(this,"ingin keluar?","Konfirmasi",JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,"Terimakasih ","Pesan",JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jabatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jabatanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jabatanActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
         int option = JOptionPane.showConfirmDialog(this,"ingin keluar?","Konfirmasi",JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,"Terimakasih ","Pesan",JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void durasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_durasiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_durasiActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
         int option = JOptionPane.showConfirmDialog(this,"ingin keluar?","Konfirmasi",JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,"Terimakasih ","Pesan",JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void peranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_peranActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_peranActionPerformed

    private void tmbhkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tmbhkActionPerformed
        // TODO add your handling code here:
       tambahKaryawan(); 
    }//GEN-LAST:event_tmbhkActionPerformed

    private void tmbhpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tmbhpActionPerformed
        // TODO add your handling code here:
        tambahProyek();
    }//GEN-LAST:event_tmbhpActionPerformed

    private void uppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uppActionPerformed
        // TODO add your handling code here:
          updateProyek();
    }//GEN-LAST:event_uppActionPerformed

    private void upkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upkActionPerformed
        // TODO add your handling code here:
        updateKaryawan();
    }//GEN-LAST:event_upkActionPerformed

    private void hpskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hpskActionPerformed
        // TODO add your handling code here:
        hapusKaryawan();     
    }//GEN-LAST:event_hpskActionPerformed

    private void hpspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hpspActionPerformed
        // TODO add your handling code here:
        hapusProyek();
    }//GEN-LAST:event_hpspActionPerformed

    private void tmbhtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tmbhtActionPerformed
        // TODO add your handling code here:
        tambahTransaksi();
    }//GEN-LAST:event_tmbhtActionPerformed

    private void uptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uptActionPerformed
        // TODO add your handling code here:
        updateTransaksi();
    }//GEN-LAST:event_uptActionPerformed

    private void hpstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hpstActionPerformed
        // TODO add your handling code here:
        hapusTransaksi();
    }//GEN-LAST:event_hpstActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        id_karyawan.setText("");
        nama.setText("");
        jabatan.setText("");
        departemen.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        id_proyek.setText("");
        nmproyek.setText("");
        durasi.setText("");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        kar1.setSelectedIndex(-1);
        pro2.setSelectedIndex(-1);
        peran.setText("");
    }//GEN-LAST:event_jButton12ActionPerformed

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
            java.util.logging.Logger.getLogger(Web.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Web.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Web.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Web.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Web().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Atas;
    private javax.swing.JPanel Atas1;
    private javax.swing.JPanel Atas2;
    private javax.swing.JPanel Karyawan;
    private javax.swing.JPanel bawah;
    private javax.swing.JPanel bawah1;
    private javax.swing.JPanel bawah2;
    private javax.swing.JTextField departemen;
    private javax.swing.JTextField durasi;
    private javax.swing.JButton hpsk;
    private javax.swing.JButton hpsp;
    private javax.swing.JButton hpst;
    private javax.swing.JTextField id_karyawan;
    private javax.swing.JTextField id_proyek;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jabatan;
    private javax.swing.JComboBox<String> kar1;
    private javax.swing.JTextField nama;
    private javax.swing.JTextField nmproyek;
    private javax.swing.JTextField peran;
    private javax.swing.JComboBox<String> pro2;
    private javax.swing.JTable tb_transaksi;
    private javax.swing.JTable tbl_karyawan;
    private javax.swing.JTable tbl_proyek;
    private javax.swing.JPanel tengah;
    private javax.swing.JPanel tengah1;
    private javax.swing.JPanel tengah2;
    private javax.swing.JButton tmbhk;
    private javax.swing.JButton tmbhp;
    private javax.swing.JButton tmbht;
    private javax.swing.JButton upk;
    private javax.swing.JButton upp;
    private javax.swing.JButton upt;
    // End of variables declaration//GEN-END:variables
}
