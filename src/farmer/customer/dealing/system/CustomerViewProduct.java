package farmer.customer.dealing.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerViewProduct extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JButton back, add;

    String customerId;
    String farmerId;

    CustomerViewProduct(String customerId, String farmerId) {
        this.customerId = customerId;
        this.farmerId   = farmerId;

        setTitle("Customer View Products");
        setSize(900,600);
        setLocation(300,0);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Farmer label
        JLabel farmerLabel = new JLabel("Farmer Number: " + farmerId);
        farmerLabel.setFont(new Font("Raleway", Font.BOLD, 26));
        farmerLabel.setBounds(50, 20, 600, 40);
        add(farmerLabel);

        // Table
        String[] cols = {"Product Name", "Price (₹)", "Quantity"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setFont(new Font("Raleway", Font.PLAIN, 16));
        table.setRowHeight(25);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50, 80, 800, 350);
        add(sp);

        // Buttons
        back = createButton("Back", 50, 500, 100, 30);
        add  = createButton("Add", 750, 500, 100, 30);

        loadProducts();
        setVisible(true);
    }

    // Utility to create styled buttons
    JButton createButton(String text, int x, int y, int w, int h) {
        JButton b = new JButton(text);
        b.setBounds(x, y, w, h);
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Raleway", Font.BOLD, 14));
        b.addActionListener(this);
        add(b);
        return b;
    }

    // Load farmer-specific products
    void loadProducts() {
        model.setRowCount(0); // Clear previous rows
        try {
            Conn c = new Conn();
            String query = "SELECT product_name, price, quantity FROM addproduct WHERE usernumber = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, farmerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("product_name"),
                    rs.getString("price"),
                    rs.getString("quantity")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Button actions
    public void actionPerformed(ActionEvent ae) {
        int row = table.getSelectedRow();

        if (ae.getSource() == back) {
            setVisible(false);
            new CustomerFarmerList(customerId).setVisible(true);
        }

        else if (ae.getSource() == add) {
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select product first");
                return;
            }

            String product = model.getValueAt(row, 0).toString();
            String price   = model.getValueAt(row, 1).toString();

            setVisible(false);
            new AddCustomerProduct(product, price, customerId, farmerId, this).setVisible(true);
        }
    }

    // Test main
    public static void main(String[] args) {
        new CustomerViewProduct("C001", "F001");
    }
}
