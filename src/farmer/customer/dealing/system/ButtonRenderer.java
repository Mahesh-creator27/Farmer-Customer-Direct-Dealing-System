package farmer.customer.dealing.system;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setFont(new Font("Raleway", Font.BOLD, 14));
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        if (value != null) {
            // ✅ Display dynamic button text
            setText(value.toString());

            // ✅ Optional tooltip for clarity
            setToolTipText("Click to view " + value.toString() + "'s products");
        } else {
            setText("View Products");
            setToolTipText("Click to view products");
        }

        // Highlight row when selected
        if (isSelected) {
            setBackground(Color.DARK_GRAY);
        } else {
            setBackground(Color.BLACK);
        }

        return this;
    }
}
