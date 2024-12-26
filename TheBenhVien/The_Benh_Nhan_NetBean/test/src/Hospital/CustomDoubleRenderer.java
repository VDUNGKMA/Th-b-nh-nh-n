/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Hospital;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomDoubleRenderer extends DefaultTableCellRenderer {
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

    @Override
    public void setValue(Object value) {
        if (value instanceof Double || value instanceof Float) {
            setText(decimalFormat.format(value));
        } else {
            super.setValue(value);
        }
    }
}

