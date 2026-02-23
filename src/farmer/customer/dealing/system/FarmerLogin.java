package farmer.customer.dealing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FarmerLogin extends JFrame implements ActionListener {

    JLabel username, password, text;
    JButton login, clear, register, back;
    JTextField usernumberTextField;
    JPasswordField passwordTextField;

    FarmerLogin() {

        getContentPane().setBackground(Color.WHITE);
        setTitle("FARMER LOGIN PAGE");
        setLayout(null);
        setSize(900,600);
        setLocation(300,0);

        JLabel Title = new JLabel("Farmer Login Page");
        Title.setFont(new Font("Raleway", Font.BOLD, 30));
        Title.setBounds(300,30,400,40);
        add(Title);

        username = new JLabel("User ID:");
        username.setFont(new Font("Raleway", Font.BOLD, 22));
        username.setBounds(200,180,200,30);
        add(username);

        usernumberTextField = new JTextField();
        usernumberTextField.setBounds(350,180,300,30);
        add(usernumberTextField);

        password = new JLabel("Password:");
        password.setFont(new Font("Raleway", Font.BOLD, 22));
        password.setBounds(200,230,200,30);
        add(password);

        passwordTextField = new JPasswordField();
        passwordTextField.setBounds(350,230,300,30);
        add(passwordTextField);

        login = new JButton("Login");
        login.setBounds(350,290,140,40);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.addActionListener(this);
        add(login);

        clear = new JButton("Clear");
        clear.setBounds(510,290,140,40);
        clear.setBackground(Color.BLACK);
        clear.setForeground(Color.WHITE);
        clear.addActionListener(this);
        add(clear);

        text = new JLabel("For New User");
        text.setBounds(410,340,200,30);
        add(text);

        register = new JButton("Register");
        register.setBounds(350,380,300,45);
        register.setBackground(Color.BLACK);
        register.setForeground(Color.WHITE);
        register.addActionListener(this);
        add(register);

        back = new JButton("Back");
        back.setBounds(50,500,100,30);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);

        setVisible(true);

        // 📸 AUTO SCREENSHOT
        FrameScreenshot.capture(this, "FarmerLogin_Page.png");
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == clear) {
            usernumberTextField.setText("");
            passwordTextField.setText("");
        }

        else if (ae.getSource() == login) {

            String userId = usernumberTextField.getText();
            String pass = new String(passwordTextField.getPassword());

            if (userId.equals("") || pass.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter User ID and Password");
                return;
            }

            try {
                Conn conn = new Conn();
                String query = "SELECT * FROM farmerlogin WHERE usernumber=? AND password=?";
                PreparedStatement ps = conn.c.prepareStatement(query);
                ps.setString(1, userId);
                ps.setString(2, pass);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    setVisible(false);
                    new FarmerDashboard(userId).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect User ID or Password");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if (ae.getSource() == register) {
            setVisible(false);
            new FarmerRegister().setVisible(true);
        }

        else if (ae.getSource() == back) {
            setVisible(false);
            new Welcome().setVisible(true);
        }
    }

    public static void main(String args[]) {
        new FarmerLogin();
    }
}
