package farmer.customer.dealing.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerMyOrders extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JButton back;
    String customerId;

    CustomerMyOrders(String customerId) {

        this.customerId = customerId;

        setTitle("My Orders");
        setSize(1000,600);
        setLocation(300,0);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel title = new JLabel("My Orders");
        title.setFont(new Font("Raleway", Font.BOLD, 30));
        title.setBounds(350,20,300,40);
        add(title);

        // Columns
        String[] cols = {
            "Order ID",
            "Farmer ID",
            "Products & Qty",
            "Total (₹)",
            "Order Status",
            "Delivery Info",
            "Delivery Status"
        };

        model = new DefaultTableModel(cols,0);
        table = new JTable(model);
        table.setRowHeight(30);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(25,80,940,420);
        add(sp);

        back = new JButton("Back");
        back.setBounds(50,520,120,35);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Raleway", Font.BOLD, 16));
        back.addActionListener(this);
        add(back);

        loadOrders();
        setVisible(true);
    }

    void loadOrders() {
        model.setRowCount(0);

        try {
            Conn c = new Conn();

            String query =
                "SELECT order_id, farmer_id, " +
                "GROUP_CONCAT(CONCAT(product_name,' (',quantity,' ',quantity_unit,')') SEPARATOR '\\n') AS products_qty_unit, " +
                "SUM(total) AS total, " +
                "status, delivery_time, is_delivered " +
                "FROM orders WHERE customer_id=? GROUP BY order_id, farmer_id";

            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                // Order Status: Pending or Received
                String orderStatus = rs.getString("status").equalsIgnoreCase("Payment Done") 
                                     ? "Pending" : "Received";

                // Delivery Status: Not Delivered Yet or Delivered
                String deliveryStatus = rs.getInt("is_delivered") == 1 
                                        ? "Delivered" : "Not Delivered Yet";

                model.addRow(new Object[] {
                    rs.getInt("order_id"),
                    rs.getString("farmer_id"),
                    rs.getString("products_qty_unit"),
                    rs.getDouble("total"),
                    orderStatus,                     // Order Status
                    rs.getString("delivery_time"),   // Delivery Info
                    deliveryStatus                    // Delivery Status
                });
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == back) {
            setVisible(false);
            new CustomerDashboard(customerId).setVisible(true);
        }
    }

    public static void main(String[] args) {
        new CustomerMyOrders("C001");
    }
}
