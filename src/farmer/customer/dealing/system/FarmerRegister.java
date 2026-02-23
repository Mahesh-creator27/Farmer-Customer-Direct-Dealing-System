package farmer.customer.dealing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;

public class FarmerRegister extends JFrame implements ActionListener {

    JLabel farmername, contact, email, address;
    JTextField farmernameTextField, contactTextField, emailTextField, addressTextField;
    JButton submit, reset;

    FarmerRegister() {

        setTitle("Farmer Registration");
        setSize(900,600);
        setLocation(300,0);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Farmer Registration");
        title.setFont(new Font("Raleway", Font.BOLD, 30));
        title.setBounds(330, 20, 400, 50);
        add(title);

        farmername = new JLabel("Name:");
        farmername.setFont(new Font("Raleway", Font.BOLD, 22));
        farmername.setBounds(200,100,150,30);
        add(farmername);

        farmernameTextField = new JTextField();
        farmernameTextField.setBounds(350,100,300,30);
        add(farmernameTextField);

        contact = new JLabel("Contact:");
        contact.setFont(new Font("Raleway", Font.BOLD, 22));
        contact.setBounds(200,150,150,30);
        add(contact);

        contactTextField = new JTextField();
        contactTextField.setBounds(350,150,300,30);
        add(contactTextField);

        email = new JLabel("Email:");
        email.setFont(new Font("Raleway", Font.BOLD, 22));
        email.setBounds(200,200,150,30);
        add(email);

        emailTextField = new JTextField();
        emailTextField.setBounds(350,200,300,30);
        add(emailTextField);

        address = new JLabel("Address:");
        address.setFont(new Font("Raleway", Font.BOLD, 22));
        address.setBounds(200,250,150,30);
        add(address);

        addressTextField = new JTextField();
        addressTextField.setBounds(350,250,300,30);
        add(addressTextField);

        submit = new JButton("Submit");
        submit.setBounds(480,450,200,45);
        styleButton(submit);
        add(submit);

        reset = new JButton("Reset");
        reset.setBounds(200,450,200,45);
        styleButton(reset);
        add(reset);

        setVisible(true);

        // 📸 AUTO SCREENSHOT
        FrameScreenshot.capture(this, "FarmerRegister_Page.png");
    }

    void styleButton(JButton btn) {
        btn.setFont(new Font("Raleway", Font.BOLD, 20));
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == reset) {
            farmernameTextField.setText("");
            contactTextField.setText("");
            emailTextField.setText("");
            addressTextField.setText("");
            return;
        }

        if (ae.getSource() == submit) {

            String name = farmernameTextField.getText().trim();
            String contactNo = contactTextField.getText().trim();
            String emailId = emailTextField.getText().trim();
            String addr = addressTextField.getText().trim();

            if (name.equals("") || contactNo.equals("") || emailId.equals("") || addr.equals("")) {
                JOptionPane.showMessageDialog(this, "All fields are required");
                return;
            }

            if (!contactNo.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Enter valid 10 digit mobile number");
                return;
            }

            if (!emailId.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(this, "Enter valid email address");
                return;
            }

            // 🔐 Generate USER ID & PASSWORD
            Random r = new Random();
            String usernumber = "mah" + (1000 + r.nextInt(9000)) + "xyz";
            String password = "P" + (1000 + r.nextInt(9000));

            try {
                Conn c = new Conn();

                String q1 = "INSERT INTO farmerregister VALUES (?,?,?,?,?)";
                PreparedStatement ps1 = c.c.prepareStatement(q1);
                ps1.setString(1, usernumber);
                ps1.setString(2, name);
                ps1.setString(3, contactNo);
                ps1.setString(4, emailId);
                ps1.setString(5, addr);

                String q2 = "INSERT INTO farmerlogin VALUES (?,?)";
                PreparedStatement ps2 = c.c.prepareStatement(q2);
                ps2.setString(1, usernumber);
                ps2.setString(2, password);

                ps1.executeUpdate();
                ps2.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Registration Successful!\n\nUser ID : " + usernumber +
                        "\nPassword : " + password
                );

                setVisible(false);
                new FarmerLogin().setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Database Error");
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        new FarmerRegister();
    }
}
