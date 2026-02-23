package farmer.customer.dealing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerLogin extends JFrame implements ActionListener {

    JLabel userid, password, text;
    JButton login, clear, register, back;
    JTextField usernumberTextField;
    JPasswordField passwordTextField;

    CustomerLogin() {

        getContentPane().setBackground(Color.WHITE);
        setTitle("Customer Login");
        setLayout(null);
        setSize(900, 600);
        setLocation(300, 0);

        JLabel title = new JLabel("Customer Login");
        title.setFont(new Font("Raleway", Font.BOLD, 30));
        title.setBounds(320, 30, 400, 50);
        add(title);

        userid = new JLabel("User ID:");
        userid.setFont(new Font("Raleway", Font.BOLD, 22));
        userid.setBounds(200, 200, 150, 30);
        add(userid);

        usernumberTextField = new JTextField();
        usernumberTextField.setBounds(350, 200, 250, 30);
        add(usernumberTextField);

        password = new JLabel("Password:");
        password.setFont(new Font("Raleway", Font.BOLD, 22));
        password.setBounds(200, 250, 150, 30);
        add(password);

        passwordTextField = new JPasswordField();
        passwordTextField.setBounds(350, 250, 250, 30);
        add(passwordTextField);

        login = new JButton("Login");
        login.setBounds(350, 310, 120, 40);
        styleButton(login);
        add(login);

        clear = new JButton("Clear");
        clear.setBounds(480, 310, 120, 40);
        styleButton(clear);
        add(clear);

        text = new JLabel("New User?");
        text.setFont(new Font("Raleway", Font.PLAIN, 16));
        text.setBounds(450, 360, 200, 30);
        add(text);

        register = new JButton("Register");
        register.setBounds(350, 400, 250, 40);
        styleButton(register);
        add(register);

        back = new JButton("Back");
        back.setBounds(30, 500, 100, 35);
        styleButton(back);
        add(back);

        // Press ENTER to login
        passwordTextField.addActionListener(this);

        setVisible(true);
    }

    void styleButton(JButton b) {
        b.setFont(new Font("Raleway", Font.BOLD, 16));
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        b.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == clear) {
            usernumberTextField.setText("");
            passwordTextField.setText("");
        }

        else if (ae.getSource() == login || ae.getSource() == passwordTextField) {

            String user = usernumberTextField.getText().trim();
            String pass = String.valueOf(passwordTextField.getPassword());

            if (user.equals("") || pass.equals("")) {
                JOptionPane.showMessageDialog(this,
                        "Please enter User ID and Password",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Conn conn = new Conn();
                String query =
                    "SELECT * FROM customerlogin WHERE usernumber=? AND password=?";
                PreparedStatement ps = conn.c.prepareStatement(query);
                ps.setString(1, user);
                ps.setString(2, pass);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    setVisible(false);
                    new CustomerDashboard(user).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid User ID or Password",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Database error occurred",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        else if (ae.getSource() == register) {
            setVisible(false);
            new CustomerRegister().setVisible(true);
        }

        else if (ae.getSource() == back) {
            setVisible(false);
            new Welcome().setVisible(true);
        }
    }

    public static void main(String[] args) {
        new CustomerLogin();
    }
}
