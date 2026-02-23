package farmer.customer.dealing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FarmerDashboard extends JFrame implements ActionListener {

    JButton addproduct, viewproduct, vieworder, logout;
    String farmerId;

    FarmerDashboard(String farmerId) {

        this.farmerId = farmerId;

        setTitle("Farmer Dashboard");
        setSize(900,600);
        setLocation(300,0);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Welcome Farmer : " + farmerId);
        title.setFont(new Font("Raleway", Font.BOLD, 32));
        title.setBounds(220,20,500,50);
        add(title);

        addproduct = createButton("Add Product", 100, 200);
        viewproduct = createButton("View Products", 100, 280);
        vieworder   = createButton("View Orders", 100, 360);

        logout = new JButton("Logout");
        logout.setBounds(750,500,100,30);
        logout.setFont(new Font("Raleway", Font.BOLD, 14));
        logout.setBackground(Color.BLACK);
        logout.setForeground(Color.WHITE);
        logout.addActionListener(this);
        add(logout);

        setVisible(true);
    }

    // ===== Reusable Button Method =====
    JButton createButton(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 300, 50);
        btn.setFont(new Font("Raleway", Font.BOLD, 20));
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.addActionListener(this);
        add(btn);
        return btn;
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == addproduct) {
            setVisible(false);
            new AddProduct(farmerId).setVisible(true);
        }
        else if (ae.getSource() == viewproduct) {
            setVisible(false);
            new ViewProduct(farmerId).setVisible(true);
        }
        else if (ae.getSource() == vieworder) {
            setVisible(false);
            new FarmerViewOrders(farmerId).setVisible(true);
        }
        else if (ae.getSource() == logout) {
            setVisible(false);
            new Welcome().setVisible(true);
        }
    }

    public static void main(String args[]) {
        new FarmerDashboard("TEST123"); // testing only
    }
}
