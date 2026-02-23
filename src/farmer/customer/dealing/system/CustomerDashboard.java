package farmer.customer.dealing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomerDashboard extends JFrame implements ActionListener {

    JButton viewFarmersBtn, myOrdersBtn, logoutBtn, searchBtn;
    JTextField searchTextField;
    String customerId;

    CustomerDashboard(String customerId) {

        this.customerId = customerId;

        // Frame settings
        getContentPane().setBackground(Color.WHITE);
        setTitle("Customer Dashboard");
        setLayout(null);
        setSize(900, 600);
        setLocation(300, 0);

        // Title
        JLabel title = new JLabel("Welcome, " + customerId);
        title.setFont(new Font("Raleway", Font.BOLD, 32));
        title.setBounds(260, 20, 450, 60);
        add(title);

        // Search label & field
        JLabel searchLbl = new JLabel("Search Product:");
        searchLbl.setFont(new Font("Raleway", Font.BOLD, 22));
        searchLbl.setBounds(180, 90, 200, 40);
        add(searchLbl);

        searchTextField = new JTextField();
        searchTextField.setBounds(360, 95, 260, 30);
        add(searchTextField);

        searchBtn = new JButton("Search");
        searchBtn.setBounds(640, 95, 100, 30);
        styleButton(searchBtn, 16);
        add(searchBtn);

        // View Farmers button
        viewFarmersBtn = new JButton("View Farmers");
        viewFarmersBtn.setBounds(100, 200, 300, 50);
        styleButton(viewFarmersBtn, 20);
        add(viewFarmersBtn);

        // My Orders button
        myOrdersBtn = new JButton("My Orders");
        myOrdersBtn.setBounds(100, 280, 300, 50);
        styleButton(myOrdersBtn, 20);
        add(myOrdersBtn);

        // Logout button
        logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(750, 500, 100, 30);
        styleButton(logoutBtn, 14);
        add(logoutBtn);

        // 🔑 Press Enter in search field triggers search
        searchTextField.addActionListener(e -> searchBtn.doClick());

        setVisible(true);
    }

    void styleButton(JButton btn, int fontSize) {
        btn.setFont(new Font("Raleway", Font.BOLD, fontSize));
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == logoutBtn) {
            setVisible(false);
            new Welcome().setVisible(true);
        }

        else if (ae.getSource() == viewFarmersBtn) {
            setVisible(false);
            new CustomerFarmerList(customerId).setVisible(true);
        }

        else if (ae.getSource() == myOrdersBtn) {
            setVisible(false);
            new CustomerMyOrders(customerId).setVisible(true);
        }

        else if (ae.getSource() == searchBtn) {
            String keyword = searchTextField.getText().trim();

            if (keyword.equals("")) {
                JOptionPane.showMessageDialog(this, "Please enter product name");
                return;
            }

            setVisible(false);
            new SearchProductResult(customerId, keyword).setVisible(true);
        }
    }

    public static void main(String[] args) {
        new CustomerDashboard("TestUser");
    }
}
