package farmer.customer.dealing.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SearchProductResult extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JButton view, back;
    String customerId, keyword;

    SearchProductResult(String customerId, String keyword) {

        this.customerId = customerId;
        this.keyword = keyword;

        setTitle("Search Results");
        setSize(900,600);
        setLocation(300,0);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel title = new JLabel("Search Results for: \"" + keyword + "\"");
        title.setFont(new Font("Raleway", Font.BOLD, 24));
        title.setBounds(50, 10, 600, 30);
        add(title);

        String[] cols = {"Farmer ID", "Product", "Price", "Quantity"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50,50,800,400);
        add(sp);

        // View Button
        view = new JButton("View Farmer");
        view.setBounds(600,470,150,35);
        view.setBackground(Color.BLACK);
        view.setForeground(Color.WHITE);
        view.addActionListener(this);
        add(view);

        // Back Button
        back = new JButton("Back");
        back.setBounds(50,470,100,35);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);

        loadData();
        setVisible(true);
    }

    void loadData() {
        try {
            Conn c = new Conn();
            String q = "SELECT usernumber, product_name, price, quantity " +
                       "FROM addproduct WHERE product_name LIKE ?";
            PreparedStatement ps = c.c.prepareStatement(q);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("usernumber"),
                        rs.getString("product_name"),
                        rs.getString("price"),
                        rs.getString("quantity")
                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {

        if(ae.getSource() == view) {

            int row = table.getSelectedRow();
            if(row == -1) {
                JOptionPane.showMessageDialog(this, "Select a product first");
                return;
            }

            String farmerId = model.getValueAt(row, 0).toString();

            setVisible(false);
            // ✅ Correct order: customerId first, farmerId second
            new CustomerViewProduct(customerId, farmerId).setVisible(true);
        }

        if(ae.getSource() == back) {
            setVisible(false);
            new CustomerDashboard(customerId).setVisible(true);
        }
    }

    // TEST MAIN
    public static void main(String[] args) {
        new SearchProductResult("C001", "Tomato");
    }
}
