package farmer.customer.dealing.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class ViewProduct extends JFrame {

    JTable table;
    DefaultTableModel model;
    String farmerId;

    ViewProduct(String farmerId) {

        this.farmerId = farmerId;

        setTitle("View Products");
        setSize(900,600);
        setLocation(300,0);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Farmer Product List");
        title.setFont(new Font("Raleway", Font.BOLD, 30));
        title.setBounds(280,20,400,40);
        add(title);

        JLabel farmerLbl = new JLabel("Farmer ID : " + farmerId);
        farmerLbl.setFont(new Font("Raleway", Font.BOLD, 18));
        farmerLbl.setBounds(100,70,400,30);
        add(farmerLbl);

        // ===== TABLE =====
        String[] cols = {"Product Name", "Price", "Quantity"};
        model = new DefaultTableModel(cols,0);
        table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(100,110,700,330);
        add(sp);

        // ===== BUTTONS =====
        JButton delete = new JButton("Delete Product");
        delete.setBounds(150,470,160,35);
        style(delete);
        add(delete);

        JButton refresh = new JButton("Refresh");
        refresh.setBounds(370,470,120,35);
        style(refresh);
        add(refresh);

        JButton back = new JButton("Back");
        back.setBounds(560,470,120,35);
        style(back);
        add(back);

        // ===== ACTIONS =====
        delete.addActionListener(e -> deleteProduct());
        refresh.addActionListener(e -> loadProductData());
        back.addActionListener(e -> {
            setVisible(false);
            new FarmerDashboard(farmerId).setVisible(true);
        });

        loadProductData();
        setVisible(true);
    }

    // ===== Button Style =====
    void style(JButton btn) {
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Raleway", Font.BOLD, 14));
    }

    // ===== Load Data =====
    void loadProductData() {
        model.setRowCount(0); // 🔥 IMPORTANT (avoid duplicate rows)

        try {
            Conn c = new Conn();
            String query =
                "SELECT product_name, price, quantity " +
                "FROM addproduct WHERE usernumber = '"+farmerId+"'";

            ResultSet rs = c.s.executeQuery(query);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("product_name"),
                    rs.getInt("price"),
                    rs.getInt("quantity")
                });
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ===== Delete Product =====
    void deleteProduct() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,"Select product to delete");
            return;
        }

        String productName = model.getValueAt(row,0).toString();

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete product : " + productName + " ?",
            "Confirm",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Conn c = new Conn();
            String query =
                "DELETE FROM addproduct " +
                "WHERE usernumber='"+farmerId+"' AND product_name='"+productName+"'";

            c.s.executeUpdate(query);

            JOptionPane.showMessageDialog(this,"Product deleted");
            loadProductData();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        new ViewProduct("TEST123");
    }
}
