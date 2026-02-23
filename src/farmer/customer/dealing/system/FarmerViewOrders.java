package farmer.customer.dealing.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FarmerViewOrders extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JButton received, back;
    String farmerId;

    FarmerViewOrders(String farmerId) {

        this.farmerId = farmerId;

        setTitle("FarmerViewOrders");
        setSize(900,600);
        setLocation(300,0);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel title = new JLabel("Orders Received");
        title.setFont(new Font("Raleway", Font.BOLD, 30));
        title.setBounds(300,20,400,40);
        add(title);

        String[] cols = {
            "Order ID",
            "Customer ID",
            "Products",
            "Total Amount",
            "Order Date",
            "Status"
        };

        model = new DefaultTableModel(cols,0);
        table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50,80,800,380);
        add(sp);

        received = new JButton("Received");
        style(received);
        received.setBounds(600,480,120,35);
        received.addActionListener(this);
        add(received);

        back = new JButton("Back");
        style(back);
        back.setBounds(50,480,100,35);
        back.addActionListener(this);
        add(back);

        loadOrders();
        setVisible(true);
    }

    // ===== Button Styling =====
    void style(JButton btn) {
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Raleway", Font.BOLD, 14));
    }

    // ===== Load Orders =====
    void loadOrders() {

        model.setRowCount(0);

        try {
            Conn c = new Conn();

            String query =
                "SELECT order_id, customer_id, " +
                "GROUP_CONCAT(product_name SEPARATOR ', ') AS products, " +
                "SUM(total) AS total, MAX(order_date) AS order_date, status " +
                "FROM orders WHERE farmer_id=? " +
                "GROUP BY order_id, customer_id, status";

            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, farmerId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("order_id"),
                    rs.getString("customer_id"),
                    rs.getString("products"),
                    rs.getInt("total"),
                    rs.getTimestamp("order_date"),
                    rs.getString("status")
                });
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // ===== Button Actions =====
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == received) {

            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order");
                return;
            }

            String currentStatus = model.getValueAt(row, 5).toString();

            if (currentStatus.equalsIgnoreCase("Delivered")) {
                JOptionPane.showMessageDialog(this, "Order already delivered");
                return;
            }

            int orderId = Integer.parseInt(model.getValueAt(row, 0).toString());

            try {
                Conn c = new Conn();

                String q =
                  "UPDATE orders SET " +
                  "status='Delivered', " +
                  "delivery_time='Delivered within 2 days' " +
                  "WHERE order_id=?";

                PreparedStatement ps = c.c.prepareStatement(q);
                ps.setInt(1, orderId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                    this,
                    "Order marked as Delivered\nCustomer will be notified"
                );

                loadOrders();

            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        if (ae.getSource() == back) {
            setVisible(false);
            new FarmerDashboard(farmerId).setVisible(true);
        }
    }

    public static void main(String[] args) {
        new FarmerViewOrders("FARMER001");
    }
}
