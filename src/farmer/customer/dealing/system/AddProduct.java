package farmer.customer.dealing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddProduct extends JFrame implements ActionListener {

    JLabel lblProduct, lblPrice, lblQty;
    JTextField tfProduct, tfPrice, tfQty;
    JButton addItem, clear, back;
    String farmerId;

    AddProduct(String farmerId) {

        this.farmerId = farmerId;

        setTitle("Add Product");
        setSize(900,600);
        setLocation(300,0);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Product Data Feeding Form");
        title.setFont(new Font("Raleway", Font.BOLD, 28));
        title.setBounds(260,20,500,50);
        add(title);

        lblProduct = createLabel("Product Name:", 100, 200);
        tfProduct = createTextField(300, 200);

        lblPrice = createLabel("Price (₹):", 100, 260);
        tfPrice = createTextField(300, 260);

        lblQty = createLabel("Quantity:", 100, 320);
        tfQty = createTextField(300, 320);

        addItem = createButton("Add Item", 650, 500);
        clear   = createButton("Clear", 100, 500);
        back    = createButton("Back", 260, 500);

        setVisible(true);
    }

    // ===== Helper Methods =====
    JLabel createLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Raleway", Font.BOLD, 22));
        lbl.setBounds(x, y, 250, 30);
        add(lbl);
        return lbl;
    }

    JTextField createTextField(int x, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 300, 30);
        add(tf);
        return tf;
    }

    JButton createButton(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 120, 35);
        btn.setFont(new Font("Raleway", Font.BOLD, 14));
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.addActionListener(this);
        add(btn);
        return btn;
    }

    // ===== Button Actions =====
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == addItem) {

            String product = tfProduct.getText().trim();
            String priceTxt = tfPrice.getText().trim();
            String qtyTxt = tfQty.getText().trim();

            if (product.equals("") || priceTxt.equals("") || qtyTxt.equals("")) {
                JOptionPane.showMessageDialog(this, "All fields are required");
                return;
            }

            try {
                int price = Integer.parseInt(priceTxt);
                int qty   = Integer.parseInt(qtyTxt);

                Conn c = new Conn();
                String query =
                    "INSERT INTO addproduct(usernumber, product_name, price, quantity) " +
                    "VALUES('" + farmerId + "','" + product + "'," + price + "," + qty + ")";

                c.s.executeUpdate(query);

                JOptionPane.showMessageDialog(this, "Product added successfully");

                setVisible(false);
                new FarmerDashboard(farmerId).setVisible(true);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Price and Quantity must be numbers");
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        else if (ae.getSource() == clear) {
            tfProduct.setText("");
            tfPrice.setText("");
            tfQty.setText("");
        }

        else if (ae.getSource() == back) {
            setVisible(false);
            new FarmerDashboard(farmerId).setVisible(true);
        }
    }

    public static void main(String args[]) {
        new AddProduct("TEST123");
    }
}
