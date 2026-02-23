package farmer.customer.dealing.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class AddCustomerProduct extends JFrame implements ActionListener {

    JLabel lblProduct, lblPrice, lblQty, lblUnit, lblTotal, lblGrand;
    JTextField tfProduct, tfPrice, tfQty, tfTotal, tfGrand;
    JComboBox<String> cbUnit;
    JButton addCart, back, buy;

    JTable table;

    public static DefaultTableModel model;
    public static double grandTotal = 0;

    JFrame previousPage;    
    String customerId;
    String farmerId;

    public AddCustomerProduct(String product, String price,
                       String customerId, String farmerId,
                       JFrame previousPage)
    {
        this.customerId = customerId;
        this.farmerId = farmerId;
        this.previousPage = previousPage;

        setTitle("Add To Cart");
        setSize(900,700);
        setLocation(300,0);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Product
        lblProduct = new JLabel("Product:");
        lblProduct.setBounds(50,40,120,30);
        add(lblProduct);

        tfProduct = new JTextField(product);
        tfProduct.setBounds(180,40,220,30);
        tfProduct.setEditable(false);
        add(tfProduct);

        // Price
        lblPrice = new JLabel("Price (₹/unit):");
        lblPrice.setBounds(50,90,120,30);
        add(lblPrice);

        tfPrice = new JTextField(price);
        tfPrice.setBounds(180,90,220,30);
        tfPrice.setEditable(false);
        add(tfPrice);

        // Quantity
        lblQty = new JLabel("Quantity:");
        lblQty.setBounds(50,140,120,30);
        add(lblQty);

        tfQty = new JTextField();
        tfQty.setBounds(180,140,100,30);
        add(tfQty);

        // Unit selection
        lblUnit = new JLabel("Unit:");
        lblUnit.setBounds(300,140,60,30);
        add(lblUnit);

        cbUnit = new JComboBox<>(new String[]{"kg", "quintal", "piece"});
        cbUnit.setBounds(360,140,100,30);
        add(cbUnit);

        // Total
        lblTotal = new JLabel("Total:");
        lblTotal.setBounds(50,190,120,30);
        add(lblTotal);

        tfTotal = new JTextField();
        tfTotal.setBounds(180,190,220,30);
        tfTotal.setEditable(false);
        add(tfTotal);

        // Add to Cart
        addCart = new JButton("Add To Cart");
        addCart.setBounds(500,140,150,35);
        addCart.setBackground(Color.BLACK);
        addCart.setForeground(Color.WHITE);
        addCart.addActionListener(this);
        add(addCart);

        // Back Button
        back = new JButton("Back");
        back.setBounds(50,600,100,30);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);

        // Buy Button
        buy = new JButton("Buy");
        buy.setBounds(720,600,100,30);
        buy.setBackground(Color.BLACK);
        buy.setForeground(Color.WHITE);
        buy.addActionListener(this);
        add(buy);

        // Table
        if(model == null) {
            model = new DefaultTableModel(
                new String[]{"Product","Price","Qty","Unit","Total"},0);
        }

        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50,250,750,330);
        add(sp);

        // Grand Total
        lblGrand = new JLabel("Grand Total:");
        lblGrand.setBounds(500,580,120,30);
        add(lblGrand);

        tfGrand = new JTextField(String.valueOf(grandTotal));
        tfGrand.setBounds(620,580,180,30);
        tfGrand.setEditable(false);
        add(tfGrand);

        // Calculate total dynamically
        tfQty.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                calculateTotal();
            }
        });
        cbUnit.addActionListener(e -> calculateTotal());

        setVisible(true);
    }

    private void calculateTotal() {
        try {
            double price = Double.parseDouble(tfPrice.getText());
            double qty = Double.parseDouble(tfQty.getText());
            String unit = (String) cbUnit.getSelectedItem();

            double multiplier = 1;
            if(unit.equals("quintal")) multiplier = 100; // 1 quintal = 100 kg
            double total = price * qty * multiplier;

            tfTotal.setText(String.valueOf(total));
        } catch(Exception ex) {
            tfTotal.setText("");
        }
    }

    public void actionPerformed(ActionEvent ae) {

        if(ae.getSource() == addCart) {

            if(tfQty.getText().equals("")) {
                JOptionPane.showMessageDialog(null,"Enter quantity");
                return;
            }

            double price = Double.parseDouble(tfPrice.getText());
            double qty = Double.parseDouble(tfQty.getText());
            String unit = (String) cbUnit.getSelectedItem();

            double multiplier = 1;
            if(unit.equals("quintal")) multiplier = 100;
            double total = price * qty * multiplier;

            model.addRow(new Object[]{
                tfProduct.getText(), price, qty, unit, total
            });

            grandTotal += total;
            tfGrand.setText(String.valueOf(grandTotal));

            tfQty.setText("");
            tfTotal.setText("");
        }

        else if(ae.getSource() == back) {
            setVisible(false);
            previousPage.setVisible(true);
        }

        else if(ae.getSource() == buy) {

            if(model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null,"Cart is empty");
                return;
            }

            setVisible(false);
            // grandTotal cast to int for backward compatibility with PaymentPage
            new PaymentPage((int)grandTotal, customerId, farmerId, this).setVisible(true);
        }
    }
}
