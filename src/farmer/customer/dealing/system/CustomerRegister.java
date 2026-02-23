package farmer.customer.dealing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class CustomerRegister extends JFrame implements ActionListener {

    JLabel customername, contact, email, address;
    JTextField nameTextField, contactTextField, emailTextField, addressTextField;
    JButton submit, reset;

    CustomerRegister() {

        // Frame settings
        getContentPane().setBackground(Color.WHITE);
        setTitle("Customer Registration");
        setLayout(null);
        setSize(900, 600);
        setLocation(300, 0);

        // Title label
        JLabel Title = new JLabel("Customer Details");
        Title.setFont(new Font("Raleway", Font.BOLD, 30));
        Title.setBounds(320, 20, 300, 50);
        add(Title);

        // Name
        customername = new JLabel("Name:");
        customername.setFont(new Font("Raleway", Font.BOLD, 22));
        customername.setBounds(200, 90, 150, 30);
        add(customername);

        nameTextField = new JTextField();
        nameTextField.setBounds(350, 90, 300, 30);
        add(nameTextField);

        // Contact
        contact = new JLabel("Contact:");
        contact.setFont(new Font("Raleway", Font.BOLD, 20));
        contact.setBounds(200, 140, 150, 30);
        add(contact);

        contactTextField = new JTextField();
        contactTextField.setBounds(350, 140, 300, 30);
        add(contactTextField);

        // Email
        email = new JLabel("Email:");
        email.setFont(new Font("Raleway", Font.BOLD, 20));
        email.setBounds(200, 190, 150, 30);
        add(email);

        emailTextField = new JTextField();
        emailTextField.setBounds(350, 190, 300, 30);
        add(emailTextField);

        // Address
        address = new JLabel("Address:");
        address.setFont(new Font("Raleway", Font.BOLD, 20));
        address.setBounds(200, 240, 150, 30);
        add(address);

        addressTextField = new JTextField();
        addressTextField.setBounds(350, 240, 300, 30);
        add(addressTextField);

        // Submit Button
        submit = new JButton("Submit");
        submit.setBounds(550, 500, 300, 50);
        styleButton(submit);
        add(submit);

        // Reset Button
        reset = new JButton("Reset");
        reset.setBounds(50, 500, 300, 50);
        styleButton(reset);
        add(reset);

        setVisible(true);
    }

    void styleButton(JButton b) {
        b.setFont(new Font("Raleway", Font.BOLD, 18));
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        b.addActionListener(this);
    }

    // 🔹 Generate Custom User ID
    private String generateUserId(String name) {
        Random rnd = new Random();
        String firstThree = name.length() >= 3 ? name.substring(0, 3).toLowerCase() : name.toLowerCase();
        int year = 2001; // You can replace this with dynamic DOB if available
        String letters = "";
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < 3; i++) {
            letters += alphabet.charAt(rnd.nextInt(alphabet.length()));
        }
        return firstThree + year + letters;
    }

    public void actionPerformed(ActionEvent ae) {

        String customerName = nameTextField.getText().trim();
        String contact = contactTextField.getText().trim();
        String email = emailTextField.getText().trim();
        String address = addressTextField.getText().trim();

        if (ae.getSource() == submit) {

            if (customerName.equals("") || contact.equals("") || email.equals("") || address.equals("")) {
                JOptionPane.showMessageDialog(this,
                        "Please fill all the fields",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Generate user ID and password
                String userNumber = generateUserId(customerName); // 🔹 Custom ID like mah2001xyz
                String password = "" + Math.abs((new Random().nextLong() % 90000L) + 10000L); // random password

                Conn c = new Conn();
                String query1 = "INSERT INTO customerregister(usernumber, name, contact, email, address) " +
                        "VALUES('" + userNumber + "','" + customerName + "','" + contact + "','" + email + "','" + address + "')";
                String query2 = "INSERT INTO customerlogin(usernumber, password) VALUES('" + userNumber + "','" + password + "')";

                c.s.executeUpdate(query1);
                c.s.executeUpdate(query2);

                JOptionPane.showMessageDialog(this,
                        "Registration Successful!\nUser Number: " + userNumber + "\nPassword: " + password,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                setVisible(false);
                new CustomerLogin().setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Database error occurred",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } else if (ae.getSource() == reset) {
            nameTextField.setText("");
            contactTextField.setText("");
            emailTextField.setText("");
            addressTextField.setText("");
        }
    }

    public static void main(String args[]) {
        new CustomerRegister();
    }
}
