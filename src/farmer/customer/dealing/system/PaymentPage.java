package farmer.customer.dealing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PaymentPage extends JFrame implements ActionListener {

    JLabel lblTitle, lblAmount, lblMethod;
    JRadioButton cash, upi, card;
    ButtonGroup bg;
    JButton pay, back;

    double totalAmount;
    String customerId, farmerId;
    JFrame previousPage;

    public PaymentPage(double totalAmount, String customerId, String farmerId, JFrame previousPage) {

        this.totalAmount = totalAmount;
        this.customerId = customerId;
        this.farmerId = farmerId;
        this.previousPage = previousPage;

        setTitle("Payment Page");
        setSize(600,400);
        setLocation(350,150);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblTitle = new JLabel("Payment");
        lblTitle.setFont(new Font("Raleway", Font.BOLD, 28));
        lblTitle.setBounds(220,20,200,40);
        add(lblTitle);

        lblAmount = new JLabel("Total Amount : ₹ " + totalAmount);
        lblAmount.setFont(new Font("Raleway", Font.BOLD, 20));
        lblAmount.setBounds(180,80,300,30);
        add(lblAmount);

        lblMethod = new JLabel("Select Payment Method:");
        lblMethod.setFont(new Font("Raleway", Font.BOLD, 18));
        lblMethod.setBounds(50,130,300,30);
        add(lblMethod);

        cash = new JRadioButton("Cash On Delivery");
        upi  = new JRadioButton("UPI");
        card = new JRadioButton("Debit / Credit Card");

        cash.setBounds(80,170,200,30);
        upi.setBounds(80,210,200,30);
        card.setBounds(80,250,250,30);

        cash.setBackground(Color.WHITE);
        upi.setBackground(Color.WHITE);
        card.setBackground(Color.WHITE);

        bg = new ButtonGroup();
        bg.add(cash); bg.add(upi); bg.add(card);

        add(cash); add(upi); add(card);

        back = new JButton("Back");
        back.setBounds(100,310,120,35);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);

        pay = new JButton("Pay Now");
        pay.setBounds(350,310,120,35);
        pay.setBackground(Color.BLACK);
        pay.setForeground(Color.WHITE);
        pay.addActionListener(this);
        add(pay);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == back) {
            setVisible(false);
            previousPage.setVisible(true);
            return;
        }

        if (ae.getSource() == pay) {

            if (!cash.isSelected() && !upi.isSelected() && !card.isSelected()) {
                JOptionPane.showMessageDialog(this, "Select payment method");
                return;
            }

            String paymentMethod =
                    cash.isSelected() ? "Cash On Delivery" :
                    upi.isSelected()  ? "UPI" : "Card";

            try {
                Conn c = new Conn();

                int generatedOrderId = -1;

                for (int i = 0; i < AddCustomerProduct.model.getRowCount(); i++) {

                    String query =
                        "INSERT INTO orders(customer_id, farmer_id, product_name, price, quantity, quantity_unit, total, status, payment_method) " +
                        "VALUES(?,?,?,?,?,?,?,?,?)";

                    PreparedStatement ps = c.c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                    ps.setString(1, customerId);
                    ps.setString(2, farmerId);
                    ps.setString(3, AddCustomerProduct.model.getValueAt(i,0).toString());
                    ps.setDouble(4, Double.parseDouble(AddCustomerProduct.model.getValueAt(i,1).toString()));
                    ps.setDouble(5, Double.parseDouble(AddCustomerProduct.model.getValueAt(i,2).toString()));
                    ps.setString(6, AddCustomerProduct.model.getValueAt(i,3).toString());
                    ps.setDouble(7, Double.parseDouble(AddCustomerProduct.model.getValueAt(i,4).toString()));
                    ps.setString(8, "Payment Done");
                    ps.setString(9, paymentMethod);

                    ps.executeUpdate();

                    // 🔑 Get generated order_id (only once)
                    if (generatedOrderId == -1) {
                        ResultSet rs = ps.getGeneratedKeys();
                        if (rs.next()) {
                            generatedOrderId = rs.getInt(1);
                        }
                    }
                }

                JOptionPane.showMessageDialog(
                    this,
                    "Payment Successful\nOrder ID : " + generatedOrderId
                );

                AddCustomerProduct.model.setRowCount(0);
                AddCustomerProduct.grandTotal = 0;

                setVisible(false);
                new BillPage(generatedOrderId).setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Payment Failed");
            }
        }
    }
}
