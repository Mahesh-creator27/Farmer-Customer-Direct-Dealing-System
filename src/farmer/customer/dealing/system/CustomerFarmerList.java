package farmer.customer.dealing.system;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class CustomerFarmerList extends JFrame {

    JTable table;
    DefaultTableModel model;
    String customerId;

    public CustomerFarmerList(String customerId) {
        this.customerId = customerId;

        // Frame settings
        setTitle("Available Farmers");
        setSize(800, 550);
        setLocation(300, 100);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("Available Farmers");
        title.setFont(new Font("Raleway", Font.BOLD, 26));
        title.setBounds(260, 20, 300, 40);
        add(title);

        // Table model
        model = new DefaultTableModel(
                new Object[]{"Farmer Name", "UserNumber", "Action"}, 0
        );

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Raleway", Font.PLAIN, 16));

        // Hide UserNumber column
        TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(1).setMinWidth(0);
        tcm.getColumn(1).setMaxWidth(0);
        tcm.getColumn(1).setWidth(0);

        // Button column
        tcm.getColumn(2).setCellRenderer(new ButtonRenderer());
        tcm.getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(100, 90, 600, 320);
        add(sp);

        // Back Button
        JButton back = new JButton("Back");
        back.setFont(new Font("Raleway", Font.BOLD, 16));
        back.setBounds(350, 440, 120, 35);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.addActionListener(e -> {
            setVisible(false);
            new CustomerDashboard(customerId).setVisible(true);
        });
        add(back);

        loadFarmers();
        setVisible(true);
    }

    // Load farmers from DB
    void loadFarmers() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery(
                    "SELECT usernumber, name FROM farmerregister"
            );

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("name"),
                        rs.getString("usernumber"),
                        "View Products"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Button Renderer
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("View Products");
            setBackground(Color.BLACK);
            setForeground(Color.WHITE);
            setFont(new Font("Raleway", Font.BOLD, 14));
        }

        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            return this;
        }
    }

    // Button Editor
    class ButtonEditor extends DefaultCellEditor {

        JButton button;
        JTable table;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);

            button = new JButton("View Products");
            button.setBackground(Color.BLACK);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Raleway", Font.BOLD, 14));

            button.addActionListener(e -> {
                int row = table.getSelectedRow();

                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Select a farmer first");
                    return;
                }

                String farmerId = table.getValueAt(row, 1).toString();

                CustomerFarmerList.this.setVisible(false);
                new CustomerViewProduct(customerId, farmerId).setVisible(true);
            });
        }

        public Component getTableCellEditorComponent(
                JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.table = table;
            return button;
        }

        public Object getCellEditorValue() {
            return "View Products";
        }
    }

    // Test main
    public static void main(String[] args) {
        new CustomerFarmerList("C001");
    }
}
