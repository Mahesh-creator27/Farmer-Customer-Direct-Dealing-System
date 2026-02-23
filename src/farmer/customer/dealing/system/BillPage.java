package farmer.customer.dealing.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BillPage extends JFrame {

    JTable table;
    DefaultTableModel model;
    int orderId;

    JLabel farmerInfo, customerInfo, totalLabel;

    public BillPage(int orderId) {
        this.orderId = orderId;

        setTitle("Order Invoice / Bill");
        setSize(900,600);
        setLocation(300,0);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel title = new JLabel("INVOICE / BILL");
        title.setFont(new Font("Raleway", Font.BOLD, 30));
        title.setBounds(330,10,300,40);
        add(title);

        // Farmer & Customer Info
        farmerInfo = new JLabel();
        farmerInfo.setBounds(40,70,400,100);
        farmerInfo.setVerticalAlignment(SwingConstants.TOP);
        add(farmerInfo);

        customerInfo = new JLabel();
        customerInfo.setBounds(460,70,400,100);
        customerInfo.setVerticalAlignment(SwingConstants.TOP);
        add(customerInfo);

        // Table for order items
        String[] cols = {"Product", "Price (₹/unit)", "Quantity", "Unit", "Total (₹)"};
        model = new DefaultTableModel(cols,0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(40,190,820,250);
        add(sp);

        // Grand Total
        totalLabel = new JLabel();
        totalLabel.setFont(new Font("Raleway", Font.BOLD, 22));
        totalLabel.setBounds(600,460,300,40);
        add(totalLabel);

        loadBill();

        setVisible(true);
    }

    void loadBill() {
        try {
            Conn c = new Conn();

            // Farmer + Customer Details
            String infoQuery =
                    "SELECT f.name AS farmername, f.contact AS fcontact, f.email AS femail, " +
                    "c.name AS customername, c.contact AS ccontact, c.email AS cemail " +
                    "FROM orders o " +
                    "JOIN farmerregister f ON o.farmer_id = f.usernumber " +
                    "JOIN customerregister c ON o.customer_id = c.usernumber " +
                    "WHERE o.order_id=? LIMIT 1";

            PreparedStatement ps1 = c.c.prepareStatement(infoQuery);
            ps1.setInt(1, orderId);
            ResultSet rs1 = ps1.executeQuery();

            if(rs1.next()) {
                farmerInfo.setText("<html><b>Farmer Details</b><br>" +
                        "Name: "+rs1.getString("farmername")+"<br>" +
                        "Contact: "+rs1.getString("fcontact")+"<br>" +
                        "Email: "+rs1.getString("femail")+"</html>");

                customerInfo.setText("<html><b>Customer Details</b><br>" +
                        "Name: "+rs1.getString("customername")+"<br>" +
                        "Contact: "+rs1.getString("ccontact")+"<br>" +
                        "Email: "+rs1.getString("cemail")+"</html>");
            }

            // Order Items
            String orderQuery =
                    "SELECT product_name, price, quantity, quantity_unit, total " +
                    "FROM orders WHERE order_id=?";

            PreparedStatement ps2 = c.c.prepareStatement(orderQuery);
            ps2.setInt(1, orderId);
            ResultSet rs2 = ps2.executeQuery();

            double grandTotal = 0;

            while(rs2.next()) {
                model.addRow(new Object[] {
                        rs2.getString("product_name"),
                        String.format("%.2f", rs2.getDouble("price")),
                        String.format("%.2f", rs2.getDouble("quantity")),
                        rs2.getString("quantity_unit"),
                        String.format("%.2f", rs2.getDouble("total"))
                });
                grandTotal += rs2.getDouble("total");
            }

            totalLabel.setText("Grand Total : ₹ " + String.format("%.2f", grandTotal));

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BillPage(1); // test order id
    }
}
