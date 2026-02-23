package farmer.customer.dealing.system;

import javax.swing.*;
import java.awt.*;

public class ButtonEditor extends DefaultCellEditor {

    protected JButton button;
    private boolean clicked;

    JTable table;
    String customerId;   // ✅ receive from list page
    String farmerId;     // ✅ taken from table

    // ✅ constructor now receives customerId
    public ButtonEditor(JCheckBox checkBox, String customerId) {
        super(checkBox);

        this.customerId = customerId;

        button = new JButton("View Products");
        button.setOpaque(true);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);

        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {

        this.table = table;

        // ✅ hidden column (index 1) se farmerId
        farmerId = table.getValueAt(row, 1).toString();

        clicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (clicked) {
            new CustomerViewProduct(customerId, farmerId).setVisible(true);
        }
        clicked = false;
        return "View Products";
    }

    @Override
    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }
}
