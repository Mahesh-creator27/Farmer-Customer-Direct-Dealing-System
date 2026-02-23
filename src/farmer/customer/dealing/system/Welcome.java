package farmer.customer.dealing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Welcome extends JFrame implements ActionListener {

    JButton farmer, customer;
    JLabel image;

    Welcome() {

        setTitle("Farmer To Customer Direct Selling System");
        setSize(900, 650);
        setLocation(300, 0);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== Background Image =====
        ImageIcon i1 = new ImageIcon(
                ClassLoader.getSystemResource("icons/kisanConnect.jpg"));

        Image i2 = i1.getImage().getScaledInstance(900, 650, Image.SCALE_SMOOTH);
        image = new JLabel(new ImageIcon(i2));
        image.setBounds(0, 0, 900, 650);
        add(image);

        // ===== Title =====
        JLabel title = new JLabel("Kisan Connect");
        title.setFont(new Font("Raleway", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setBounds(300, 30, 400, 50);
        image.add(title);

        // ===== Tagline =====
        JLabel tagline = new JLabel("Kisan Se Seedha Grahak Tak");
        tagline.setFont(new Font("Raleway", Font.BOLD, 24));
        tagline.setForeground(Color.WHITE);
        tagline.setBounds(250, 90, 500, 40);
        image.add(tagline);

        // ===== Farmer Button =====
        farmer = new JButton("Farmer");
        farmer.setBounds(300, 350, 300, 50);
        styleButton(farmer);
        image.add(farmer);

        // ===== Customer Button =====
        customer = new JButton("Customer");
        customer.setBounds(300, 430, 300, 50);
        styleButton(customer);
        image.add(customer);

        setVisible(true);

        // 📸 AUTO SCREENSHOT when page opens
        FrameScreenshot.capture(this, "Welcome_Page.png");
    }

    void styleButton(JButton btn) {
        btn.setFont(new Font("Raleway", Font.BOLD, 20));
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == farmer) {
            setVisible(false);
            new FarmerLogin().setVisible(true);
        }

        else if (ae.getSource() == customer) {
            setVisible(false);
            new CustomerLogin().setVisible(true);
        }
    }

    public static void main(String args[]) {
        new Welcome();
    }
}
